package model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
	public static double BETA = 1- ALFA;
	public static long startGenerationTime;
	public static long endGenerationTime;
	public static String semillas;
	public static int elites;
	public static final double IND_MAX_REL_ERR = 0.01;
	public static final double IND_MAX_NODES = 15.0;
	
	ArrayList<TSPData> data;
	
	@Override
	public TSProblem clone() {
		TSProblem mkpp = (TSProblem) super.clone();
		return mkpp;
	}
	
	@Override
	public void setup(final EvolutionState state, final Parameter base) {	
		JOB_NUMBER = ((Integer)(state.job[0])).intValue();
		super.setup(state, base);
		
		if (!(input instanceof TSPData)){
			state.output.fatal("Obteniendo instancias de prueba desde archivo");
		}
		
		elites=  state.parameters.getInt(new ec.util.Parameter("breed.elite.0"),null);	
		semillas=  state.parameters.getString(new ec.util.Parameter("seed.0"),null);
		data = new ArrayList<TSPData>();
		
		try {
			LOG_FILE = FileIO.newLog(state.output, "out/TSPLog.out");
			(new File("out/results/evolution" + (JOB_NUMBER))).mkdirs();
			RESULTS_FILE = FileIO.newLog(state.output, "out/results/evolution" + (JOB_NUMBER) + "/TSPResults.out");
			DOT_FILE = FileIO.newLog(state.output, "out/results/evolution"  + (JOB_NUMBER) + "/job."+(JOB_NUMBER)+".BestIndividual.dot");
			final File folder = new File("data/evaluacion");
			
			FileIO.readInstances(data, folder);
			
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
			
			ArrayList<TSPData> auxData = new ArrayList<TSPData>();
			
			GPIndividual gpind = (GPIndividual) individual;
			
			state.output.println("\n\nGeneracion:" + state.generation + "\nTSProblem: evaluando el individuo [" + gpind.toString() + "]\n", LOG_FILE);
			gpind.printIndividualForHumans(state, LOG_FILE);
			
			int hits = 0;
			double relErrAcum = 0.0, relErrAcum2 = 0.0;
			double nodesResult = 0.0;
			double instanceRelErr, err, size, sizeRel;
			//ArrayList<Integer> casa= new ArrayList<Integer>();
			
			if(gpind.size() > IND_MAX_NODES)
				nodesResult = Math.abs(IND_MAX_NODES - gpind.size() ) / IND_MAX_NODES;
			
			state.output.println("\n---- Iniciando evaluacion ---\nNum de Nodos:" + gpind.size(), LOG_FILE);
			
//			for(int i = 0; i < data.size(); i++) {
//				if(!data.get(i).instance.isNew()){
//					System.out.println(data.get(i).getInstance().printResult());
//				}		
//			}

			for(int i = 0; i < data.size(); i++) {
				auxData.add(data.get(i).clone());	//nuevo data (vaciar mochila)							
				//state.output.println("\n## Evaluando en instancia: " + auxData.get(i).getInstance().getName() + " ##\n", LOG_FILE);
				gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;	//escribir individuos en formato dot				
				long timeInit, timeEnd;
				timeInit = System.nanoTime();	//inicio cronometro
				gpind.trees[0].child.eval(state, threadnum, auxData.get(i), stack, gpind, this);	//evaluar el individuo gpind para la instancia i
				timeEnd = System.nanoTime();	//fin cronometro
				
				//Diferencia entre el resultado obtenido y el óptimo
				err = Math.abs( auxData.get(i).getInstance().cost() - auxData.get(i).getInstance().getBestResult());
				//Error relativo entre la diferencia entre el resultado obtenido y el óptimo
				instanceRelErr = err/(auxData.get(i).getInstance().getBestResult());
				//Diferencia entre la cantidad de nodos usadas y el total a usar
				size = Math.abs( auxData.get(i).getInstance().getLCA().size()- auxData.get(i).getInstance().getCT());
				sizeRel = size/(float)(auxData.get(i).getInstance().getCT());
				//Hits
//				if(err == 0 && size == 0) {
//					hits++;
//				}
				if(instanceRelErr < IND_MAX_REL_ERR){
					hits++;
				}
				//System.out.println(auxData.get(i).getInstance().printResult());
				
				//*log result*/
				state.output.print(state.generation+" ", RESULTS_FILE);
				state.output.print(state.numGenerations+" ", RESULTS_FILE);
				state.output.print((timeEnd - timeInit) +" ", RESULTS_FILE);
				state.output.print(gpind.toString()+" ", RESULTS_FILE);
				state.output.print(auxData.get(i).getInstance().cost() + " ", RESULTS_FILE);
				state.output.print(auxData.get(i).getInstance().getBestResult() + " ", RESULTS_FILE);
				state.output.print(instanceRelErr +" ", RESULTS_FILE);
				state.output.print(gpind.trees[0].child.depth()+"", RESULTS_FILE);
				state.output.print((BETA*nodesResult + ALFA*instanceRelErr+""), RESULTS_FILE);
				state.output.print(gpind.size() + " ", RESULTS_FILE);
				state.output.print(hits +" ", RESULTS_FILE);		
				state.output.print(size +" ", RESULTS_FILE);		
				state.output.println(nodesResult +" ", RESULTS_FILE);				

				
				relErrAcum += instanceRelErr;
				relErrAcum2 += sizeRel;
				state.output.print("Time: [init= " + timeInit + "], [end= " + timeEnd + "], [dif= " + (timeEnd - timeInit) + "]", LOG_FILE);
				//casa=auxData.get(i).getInstance().getLCA();
				//System.out.println(hits+" "+ auxData.get(i).getInstance().cost()+"   "+auxData.get(i).getInstance().getBestResult()+"  "+auxData.get(i).getInstance().getLCA());
			}
			
			Runtime garbage = Runtime.getRuntime();
			garbage.gc();
			
			state.output.println("---- Evaluacion terminada ----", LOG_FILE);
			
			double profitResult = relErrAcum / auxData.size();
			double profitResult2 = relErrAcum2 / auxData.size();
			state.output.println(" Error relativo de la cantidad de nodos = " + nodesResult, LOG_FILE);
			state.output.println(" Error relativo del profit = " + profitResult, LOG_FILE);
			state.output.println(" profitResult = " + profitResult, LOG_FILE);
			KozaFitness f = ((KozaFitness) gpind.fitness);
			
			float fitness = (float)(ALFA*(ALFA*profitResult + BETA*profitResult2) + BETA*nodesResult);
			f.setStandardizedFitness(state, fitness);
			f.hits = hits;
			gpind.evaluated = true;
			//Guardar individuo acá revisar codigo Camilo
		}
	}
	
	@Override
	public void describe(final EvolutionState state,
			final Individual individual,
			final int subpopulation,
			final int threadnum,
			final int log) {
		
		endGenerationTime = System.nanoTime();	//fin cronometro evoluciÃ³n
		state.output.message("Evolution duration: " + (endGenerationTime - startGenerationTime) / 1000000 + " ms");	//duraciÃ³n evoluciÃ³n en ms
		PrintWriter dataOutput = null;
		Charset charset = Charset.forName("UTF-8");
		try {			
			dataOutput = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out/results/job."+JOB_NUMBER+".BestIndividual.in"), charset)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataOutput.println(Population.NUM_SUBPOPS_PREAMBLE + Code.encode(1));
		dataOutput.println(Population.SUBPOP_INDEX_PREAMBLE + Code.encode(0));
		dataOutput.println(Subpopulation.NUM_INDIVIDUALS_PREAMBLE + Code.encode(1));
		dataOutput.println(Subpopulation.INDIVIDUAL_INDEX_PREAMBLE + Code.encode(0));
		
		individual.evaluated = false;
		((GPIndividual)individual).printIndividual(state, dataOutput);
		dataOutput.close();

		GPIndividual gpind = (GPIndividual) individual;
		gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;
		String indid = gpind.toString().substring(19);
		state.output.println("label=\"Individual=" + indid + " Fitness=" + ((KozaFitness) gpind.fitness).standardizedFitness() + " Hits=" + ((KozaFitness) gpind.fitness).hits + " Size=" + gpind.size() + " Depth=" + gpind.trees[0].child.depth() + "\";", DOT_FILE);
		gpind.printIndividualForHumans(state, DOT_FILE);
		
		try {
			FileIO.repairDot(JOB_NUMBER);
			FileIO.dot_a_png(TSProblem.JOB_NUMBER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
