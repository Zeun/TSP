package model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.security.sasl.AuthorizeCallback;

import ec.*;
import ec.gp.*;
import ec.gp.koza.KozaFitness;
import ec.simple.SimpleProblemForm;
import ec.util.*;

public class TSProblem extends GPProblem implements SimpleProblemForm {

	private static final long serialVersionUID = -8430160211244271537L;
	
	public static int LOG_FILE;
	public static int RESULTS_FILE;
	public static int HEURISTICS_FILE;
	public static int DOT_FILE;
	public static int JOB_NUMBER;
	public static double ALFA = 0.95;
	public static double BETA = 1 - ALFA;
	public static double GAMMA = 0.55;
	public static double DELTA = 1 - GAMMA;
	public static long startGenerationTime;
	public static long endGenerationTime;
	public static String semillas;
	public static int elites;
	public static final double IND_MAX_REL_ERR = 0.05;
	public static final double IND_MAX_NODES = 15.0;
	public static int JOBS;
	public static int SUBPOPS;
	
	ArrayList<TSPData> data_island1;
	ArrayList<TSPData> data_island2;
	ArrayList<ArrayList<TSPData>> data;
	
	@Override
	public TSProblem clone() {
		TSProblem tsp = (TSProblem) super.clone();
		return tsp;
	}
	
	@Override
	public void setup(final EvolutionState state, final Parameter base) {	
		JOB_NUMBER = ((Integer) (state.job[0])).intValue();
		super.setup(state, base);
		if (!(input instanceof TSPData)) {
			state.output.fatal("Obteniendo instancias de prueba desde archivo");
		}
		JOBS = state.parameters.getInt(new ec.util.Parameter("jobs"), null);
		SUBPOPS = state.parameters.getInt(new ec.util.Parameter("pop.subpops"), null);
		elites = state.parameters.getInt(new ec.util.Parameter("breed.elite.0"), null);
		semillas = state.parameters.getString(new ec.util.Parameter("seed.0"), null);
		data_island1 = new ArrayList<TSPData>();
		data_island2 = new ArrayList<TSPData>();
		data = new ArrayList<ArrayList<TSPData>>();
		
		try {
			LOG_FILE = FileIO.newLog(state.output, "out/TSPLog.out");
			(new File("out/results/evolution" + (JOB_NUMBER))).mkdirs();
			RESULTS_FILE = FileIO.newLog(state.output, "out/results/evolution" + (JOB_NUMBER) + "/TSPResults.out");
			state.output.print("Generacion" + ", ", RESULTS_FILE);
			state.output.print("N� Gen" + ", ", RESULTS_FILE);
			state.output.print("Isla" + ", ", RESULTS_FILE);
			state.output.print("N� Islas" + ", ", RESULTS_FILE);
			state.output.print("Tiempo(ms)" + ", ", RESULTS_FILE);
			state.output.print("Individuo" + ", ", RESULTS_FILE);
			state.output.print("Resultado obtenido" + ", ", RESULTS_FILE);
			state.output.print("Resultado �ptimo" + ", ", RESULTS_FILE);
			// state.output.print("N� Elementos" + ", ", RESULTS_FILE);
			state.output.print("Error relativo" + ", ", RESULTS_FILE);
			state.output.print("Error relativo num ciudades" + ", ", RESULTS_FILE);
			state.output.print("Hits" + ", ", RESULTS_FILE);
			state.output.print("Error relativo tama�o �rbol" + ", ", RESULTS_FILE);
			state.output.print("Profundidad �rbol" + ", ", RESULTS_FILE);
			state.output.print("Tama�o �rbol" + ", ", RESULTS_FILE);
			state.output.println("Nombre Instancia" + " ", RESULTS_FILE);
			
			
			
			// DOT_FILE = FileIO.newLog(state.output, "out/results/evolution"  + (JOB_NUMBER) + "/job."+(JOB_NUMBER)+".BestIndividual.dot");
			DOT_FILE = FileIO.newLog(state.output, "out/results/evolution" + JOB_NUMBER + "/BestIndividual.dot");
			final File folder_island1;
			// Si tengo m�s de una poblaci�n, uso 2 grupos de instancias
			if (SUBPOPS > 1){
				folder_island1 = new File("data/evaluacion_island1");
				final File folder_island2 = new File("data/evaluacion_island2");
				FileIO.readInstances(data_island2, folder_island2);
				data.add(data_island2);
			} else {
				folder_island1 = new File("data/evaluacion_canonico");
			}
			FileIO.readInstances(data_island1, folder_island1);
			
			data.add(data_island1);
			
			
			System.out.println("Lectura desde archivo terminada con Exito!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("TSProblem: Evolucionando...");
		startGenerationTime = System.nanoTime();	
	}

	@Override
	public void evaluate(
			final EvolutionState state,
	        final Individual individual,
	        final int subpopulation,
	        final int threadnum) {
			
			if (!individual.evaluated) {
			
			GPIndividual gpind = (GPIndividual) individual;
			
			state.output.println("Generacion: " + state.generation + "\nSubpopulation: " + subpopulation + "\nTSProblem: evaluando el individuo [" + gpind.toString() + "]\n", LOG_FILE);
			gpind.printIndividualForHumans(state, LOG_FILE);
			
			int hits = 0;
			double relErrAcum = 0.0, 
					sizeRelErrAcum = 0.0, 
					promedioPeorArco = 0.0,
					nodesResult = 0.0,
					pobSize = data.get(subpopulation % 2).size(),
					instanceRelErr, 
					err, 
					size, 
					instanceSizeRelErr;
			
			// Si el tama�o del �rbol es mayor al permitido
			if(gpind.size() > IND_MAX_NODES){
				nodesResult = Math.abs(IND_MAX_NODES - gpind.size() ) / IND_MAX_NODES;
			}				
			
			state.output.println("\n---- Iniciando evaluacion ---\nNum de Nodos:" + gpind.size(), LOG_FILE);

			for (int i = 0; i < pobSize; i++) {

				Instance auxData = new Instance();
				auxData = data.get(subpopulation % 2).get(i).getInstance().clone();
				
				TSPData aux = new TSPData();
				aux.instance = auxData;
				
				gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;	//escribir individuos en formato dot				
				long timeInit, timeEnd;
				timeInit = System.nanoTime();	//inicio cronometro
				gpind.trees[0].child.eval(state, threadnum, aux, stack, gpind, this);	//evaluar el individuo gpind para la instancia i
				timeEnd = System.nanoTime();	//fin cronometro
				
				//Diferencia entre el resultado obtenido y el �ptimo
				err = Math.abs( auxData.costoCircuito() - auxData.getOptimo());
				//Error relativo entre la diferencia entre el resultado obtenido y el �ptimo
				instanceRelErr = err / (auxData.getOptimo());
				
				//Diferencia entre la cantidad de nodos usadas y el total a usar
				size = Math.abs( auxData.getListaCiudadesAgregadas().size()- auxData.getTotalCiudades());
				instanceSizeRelErr = size/(double)(auxData.getTotalCiudades());
				
				// N�mero de hits (s�lo cuentan si es soluci�n factible y tiene error menor al 5%
				if(instanceRelErr < IND_MAX_REL_ERR && instanceSizeRelErr == 0.0){
					hits++;
				}
				
				// Descomentar para ver mochila en pantalla
				// System.out.println(auxData.printResult());
				
				// Log de resultados por instancia
				state.output.print(state.generation + ", ", RESULTS_FILE);
				state.output.print(state.numGenerations + ", ", RESULTS_FILE);
				state.output.print(subpopulation + ", ", RESULTS_FILE);
				state.output.print(SUBPOPS + ", ", RESULTS_FILE);
				state.output.print((timeEnd - timeInit) + ", ", RESULTS_FILE);
				state.output.print(gpind.toString() + ", ", RESULTS_FILE);
				state.output.print(auxData.costoCircuito() + ", ", RESULTS_FILE);
				state.output.print(auxData.getOptimo() + ", ", RESULTS_FILE);
				// state.output.print(auxData.numeroElementos() + ", ", RESULTS_FILE);
				state.output.print(instanceRelErr + ", ", RESULTS_FILE);
				state.output.print(instanceSizeRelErr + ", ", RESULTS_FILE);
				state.output.print(hits + ", ", RESULTS_FILE);
				state.output.print(nodesResult + ", ", RESULTS_FILE);
				state.output.print(gpind.trees[0].child.depth() + ", ", RESULTS_FILE);
				state.output.print(gpind.size() + ", ", RESULTS_FILE);
				state.output.println(auxData.getNombreInstancia() + " ", RESULTS_FILE);
				
				// Acumulaci�n de errores para cada instancia del algoritmo
				relErrAcum += instanceRelErr;
				sizeRelErrAcum += instanceSizeRelErr;
				promedioPeorArco += auxData.getWorstEdge();
				
			}
			
			Runtime garbage = Runtime.getRuntime();
			garbage.gc();
			
			state.output.println("---- Evaluacion terminada ----", LOG_FILE);
			
			double profitResult,
				errResult = relErrAcum / pobSize,
				sizeResult = sizeRelErrAcum / pobSize,
				hitsResult = Math.abs(hits - pobSize) / pobSize;
			promedioPeorArco /= pobSize;
			
			// Si tengo m�s de una poblaci�n, uso islas... cc uso f obj est�ndar
			if (SUBPOPS > 1){
				// Funcion objetivo para cada isla
				// Las primeras 2 islas se evaluan con f1 y f2
				if (subpopulation < 2){
					// Si la isla es par, funcion obj con hit
					if (subpopulation % 2 == 0){
						// Funcion objetivo considerando el numero de hits
						profitResult = hitsResult;
						state.output.println("Fitness Hits ", LOG_FILE);
					} else { // Si la isla es par, funcion obj con err relativo
						// Funcion objetivo tradicional con el error relativo
						// profitResult = (errResult*ALFA + sizeResult*BETA);
						profitResult = errResult;
						state.output.println("Fitness error relativo ", LOG_FILE);
					}
				} else { // Las ultimas 2 islas se evaluan con f2 y f1 
					// Si la isla es impar, funcion obj con hit
					if (subpopulation % 2 != 0){
						// Funcion objetivo considerando el numero de hits
						profitResult = hitsResult;
						state.output.println("Fitness Hits ", LOG_FILE);
					} else { // Si la isla es par, funcion obj con err relativo
						// Funcion objetivo tradicional con el error relativo
						// profitResult = (errResult*ALFA + sizeResult*BETA);
						profitResult = errResult;
						state.output.println("Fitness error relativo ", LOG_FILE);
					}
				}
			} else {
				// System.out.println(sizeResult);
				// Funcion objetivo combinada para caso canonico
				// profitResult = ALFA * (errResult * GAMMA + sizeResult * DELTA) + BETA * hitsResult;
				profitResult = ALFA * errResult + BETA * hitsResult;
			}
			
			
			
			// profitResult = ALFA * errResult + BETA * hitsResult;
			// state.output.println("Fitness = ALFA*(ALFA*(ALFA*Error Relativo + BETA*Error Num Ciudades) + BETA*Num Hits) + BETA*Num Nodos", LOG_FILE); 
			// state.output.println("Fitness = " + ALFA + "*(" + ALFA + "*(" + ALFA + "*" + errResult
			//		+ " + " + BETA + "*" + sizeResult + ") + " + BETA + "*" + hitsResult + ") + " + BETA + "*" + nodesResult , LOG_FILE);
			state.output.println("Fitness = " + (ALFA*profitResult + BETA*nodesResult + promedioPeorArco *sizeResult), LOG_FILE);
			// state.output.println("Error relativo de la cantidad de nodos = " + nodesResult, LOG_FILE);
			state.output.println("===================================== \n", LOG_FILE);
			KozaFitness f = ((KozaFitness) gpind.fitness);
			// System.out.println("subpop" + subpopulation);
			float fitness = (float)(ALFA*profitResult + BETA*nodesResult + promedioPeorArco*sizeResult);
			f.setStandardizedFitness(state, fitness);
			f.hits = hits;
			gpind.evaluated = true;
		}
	}
	
	@Override
	public void describe(final EvolutionState state,
			final Individual individual,
			final int subpopulation,
			final int threadnum,
			final int log) {
		
		endGenerationTime = System.nanoTime();	//fin cronometro evolución
		String message_time = "Evolution duration: " + (endGenerationTime - startGenerationTime) / 1000000 + " ms";	
		state.output.message(message_time);
		PrintWriter dataOutput = null;
		Charset charset = Charset.forName("UTF-8");
		try {
			dataOutput = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(
							"out/results/job." + JOB_NUMBER + ".subpop" + subpopulation + ".BestIndividual.in"),
					charset)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataOutput.println(Population.NUM_SUBPOPS_PREAMBLE + Code.encode(1));
		dataOutput.println(Population.SUBPOP_INDEX_PREAMBLE + Code.encode(0));
		dataOutput.println(Subpopulation.NUM_INDIVIDUALS_PREAMBLE + Code.encode(1));
		dataOutput.println(Subpopulation.INDIVIDUAL_INDEX_PREAMBLE + Code.encode(0));
		
//		individual.evaluated = false;
		((GPIndividual)individual).printIndividual(state, dataOutput);
		
		dataOutput.println("\nJob: " + JOB_NUMBER);
		dataOutput.println("Isla: " + subpopulation);
		dataOutput.println("Generacion: " + state.generation);
		dataOutput.println(message_time);
		
		dataOutput.close();

		GPIndividual gpind = (GPIndividual) individual;
		gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;
		String indid = gpind.toString().substring(19);
		state.output.println("label=\"Individual=" + indid + " Fitness=" + ((KozaFitness) gpind.fitness).standardizedFitness() + " Hits=" + ((KozaFitness) gpind.fitness).hits + " Size=" + gpind.size() + " Depth=" + gpind.trees[0].child.depth() + "\";", DOT_FILE);
		gpind.printIndividualForHumans(state, DOT_FILE);
		
		try {
			FileIO.repairDot(JOB_NUMBER, JOBS, subpopulation);
			FileIO.dot_a_png(TSProblem.JOB_NUMBER, subpopulation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
