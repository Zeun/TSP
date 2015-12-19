package model;

import java.io.*;
import java.util.*;

import javax.management.PersistentMBean;

import ec.util.Output;

public class FileIO {
	public static int newLog(Output output, String filename) throws IOException {
		System.out.println(filename);
		FileWriter fw = new FileWriter(filename, false);
		fw.write("");
		fw.close();
		File file = new File(filename);
		return output.addLog(file, true);
	}

	public static void readInstances(ArrayList<TSPData> data, final File folder) throws IOException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				readInstances(data, fileEntry);
			} else {
				System.out.println("Leyendo: " + fileEntry.getName());
				TSPData sop = new TSPData();
				sop.setInstance(readFile(fileEntry.getPath()));
				data.add(sop);
			}
		}
	}

	private static Instance readFile(String filename) throws IOException {

		File file = new File(filename);
		Scanner s = new Scanner(file);
		// Nombre instancia
		String nombreInstancia = file.getName();
		// Óptimo instancia
		int mejorResultado = Integer.parseInt(s.nextLine());
		// Número de nodos (ciudades) de la instancia
		int totalCiudades = Integer.parseInt(s.nextLine());
		// Matriz de costos
		ArrayList<ArrayList<Double>> matrizCostos = new ArrayList<>();
		// Lista de ciudades totales disponibles
		ArrayList<Integer> listaCiudadesDisponibles = new ArrayList<>();
		// Lista de ciudades agregadas al circuito
		ArrayList<Integer> listaCiudadesAgregadas = new ArrayList<>();
		// Lista de ciudades cercanas al centro
		ArrayList<Integer> ciudadesCercanas = new ArrayList<>();
		// Lista de ciudades lejanas al centro
		ArrayList<Integer> ciudadesLejanas = new ArrayList<>();
		ArrayList<Double> listTemp;
		// Peor arco
		double temp, peorArco = -1;

		for (int i = 0; i < totalCiudades; i++) {
			listTemp = new ArrayList<>();
			for (int j = 0; j < totalCiudades; j++) {
				temp = s.nextDouble();
				listTemp.add(temp);
				if(peorArco < temp){
					peorArco = temp;
				}
			}
			matrizCostos.add(listTemp);
			listaCiudadesDisponibles.add(i + 1);
		}
		s.close();
		// System.out.println(peorArco);
		listaCiudadesDisponibles.remove(listaCiudadesDisponibles.size() - 1);
		listaCiudadesAgregadas.add(0);	
		// System.out.println(listaCiudadesDisponibles);
		
		ArrayList<Double> listaDistancias = new ArrayList<>();
		// System.out.println("ciudades disponibles: " + listaTemporal);
		// listaTemporal.remove(listaTemporal.size()-1);
		int elementos = listaCiudadesDisponibles.size() / 2;
		
		for (int i = 0; i < listaCiudadesDisponibles.size(); i++) {
			double sum = 0;
			for(Double d : matrizCostos.get(listaCiudadesDisponibles.get(i)))
			    sum += d;
			listaDistancias.add(sum);
		}
		// System.out.println(listaDistancias);
		// Ordenarlas de menor a mayor
		ArrayList<Double> listaTemporal = new ArrayList<Double>(listaDistancias);
		// System.out.println(listaTemporal);
		Collections.sort(listaDistancias);
		// System.out.println(listaDistancias);
		for (int i = 0; i<listaDistancias.size();i++){
			if (i < elementos){
				// System.out.println("cc " + listaDistancias.get(i));
				ciudadesCercanas.add(listaTemporal.indexOf(listaDistancias.get(i)) + 1);
			} else {
				// System.out.println("cl " + listaDistancias.get(i));
				ciudadesLejanas.add(listaTemporal.indexOf(listaDistancias.get(i)) + 1);
			}
		}
//		System.out.println("cc " + ciudadesCercanas);
//		System.out.println("cm " + ciudadesMedias);
//		System.out.println("cl " + ciudadesLejanas);

		return new Instance(nombreInstancia, mejorResultado, totalCiudades, listaCiudadesAgregadas, listaCiudadesDisponibles,
				matrizCostos, ciudadesCercanas, ciudadesLejanas, peorArco);
	}

	public static void repairDot(int JOB_NUMBER, int JOBS, int subpopulation) throws IOException {
		File file;
		int jobs = JOBS;
		if (jobs == 1) {
			file = new File("out/results/evolution" + JOB_NUMBER + "/BestIndividual.dot");
		} else {
			file = new File("out/results/evolution" + JOB_NUMBER + "/job." + JOB_NUMBER + ".BestIndividual.dot");
		}
		Scanner s = new Scanner(file);
		StringBuilder buffer = new StringBuilder();
		int i = 1;
		String label = "";
		String txt;
		while (s.hasNextLine()) {
			txt = s.nextLine();
			if (!txt.isEmpty()) {
				if (i == 1) {
					// label = s.nextLine();
					label = txt;
				} else if (i > 4) {
					// txt = s.nextLine();
					buffer.append(txt + "\n");
					if (i == 5)
						buffer.append(label + "\n");
				}
				i++;

			} else {
				writeFile(buffer.toString(), "out/results/evolution" + JOB_NUMBER + "/job" + JOB_NUMBER + ".subpop"
						+ subpopulation + ".BestIndividual.dot");
				buffer.setLength(0);
				i = 1;
				label = "";
			}
		}
		s.close();
	}

	public static void writeFile(String line, String filename) throws IOException {
		File file = new File(filename);

		// if (!file.exists()) {
		file.createNewFile();
		// }

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line);
		bw.close();
	}

	public static void dot_a_png(int job_number, int subpopulation) {
		try {
			System.out.println("[dot_a_png]");
			String dotPath = "C:/Program Files (x86)/Graphviz2.38/bin/dot.exe";
			String fileInputPath = "out/results/evolution" + job_number + "/job" + job_number + ".subpop"
					+ subpopulation + ".BestIndividual.dot";
			String fileOutputPath = "out/results/evolution" + job_number + "/job" + job_number + ".subpop"
					+ subpopulation + ".BestIndividual.png";
			// System.out.println(dotPath);
			// System.out.println(fileInputPath);
			// System.out.println(fileOutputPath);

			Runtime rt = Runtime.getRuntime();
			rt.exec(dotPath + " -Tpng " + fileInputPath + " -o " + fileOutputPath);

		} catch (IOException ioe) {
			System.out.println(ioe);
		} finally {
		}

	}
}
