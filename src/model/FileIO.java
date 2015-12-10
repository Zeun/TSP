package model;

import java.io.*;
import java.util.*;

import ec.util.Output;

public class FileIO {
	public static int newLog(Output output, String filename) throws IOException {// Se
																					// crea
																					// un
																					// nuevo
																					// archivo
																					// para
																					// escribir
																					// la
																					// salida
																					// en
																					// el??????
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
		int mejorResultado = Integer.parseInt(s.nextLine());
		int totalCiudades = Integer.parseInt(s.nextLine());

		ArrayList<ArrayList<Integer>> matrizCostos = new ArrayList<>();// Matriz
																		// de
																		// costos
		ArrayList<Integer> listaCiudadesDisponibles = new ArrayList<>(); // Lista
																			// de
																			// Ciudades
																			// totales
		ArrayList<Integer> listaCiudadesAgregadas = new ArrayList<>(); // Lista
																		// de
																		// Ciudades
																		// Agregadas
		ArrayList<Integer> ciudadesCercanas = new ArrayList<>(); // Lista de
																	// Ciudades
																	// Agregadas
		ArrayList<Integer> ciudadesMedias = new ArrayList<>(); // Lista de
																// Ciudades
																// Agregadas
		ArrayList<Integer> ciudadesLejanas = new ArrayList<>(); // Lista de
																// Ciudades
																// Agregadas
		ArrayList<Integer> listTemp;

		for (int i = 0; i < totalCiudades; i++) {
			listTemp = new ArrayList<>();
			for (int j = 0; j < totalCiudades; j++) {
				listTemp.add(s.nextInt());
			}
			matrizCostos.add(listTemp);
			listaCiudadesDisponibles.add(i + 1);
		}
		// System.out.println(MD);
		s.close();

		// for (int i=1; i<CT+1;i++) {
		// listaCiudadesDisponibles.add(i);
		// }

		listaCiudadesAgregadas.add(0);
		listaCiudadesDisponibles.remove(listaCiudadesDisponibles.size() - 1);
		ArrayList<Integer> listaTemporal = new ArrayList<Integer>(listaCiudadesDisponibles);
		// System.out.println("ciudades disponibles: " + listaTemporal);
		// listaTemporal.remove(listaTemporal.size()-1);
		int elementos = listaTemporal.size() / 3;
		int diferencia = listaTemporal.size() % 3;

		int mayor;
		int posicion;
		int contador = 1;
		int i = 1;
		while (i <= elementos) {
			mayor = 10000000;
			posicion = -3;
			// System.out.println("i: " + i);
			for (int j = 0; j < listaTemporal.size() - 1; j++) {
				if (mayor > matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaTemporal.get(j))) {
					mayor = matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaTemporal.get(j));
					posicion = j;
				}
			}

			if (contador == 1) {
				ciudadesCercanas.add(listaTemporal.get(posicion));
				listaTemporal.remove(posicion);
			} else if (contador == 2) {
				ciudadesMedias.add(listaTemporal.get(posicion));
				listaTemporal.remove(posicion);
			} else {
				if (posicion != -3) {
					ciudadesLejanas.add(listaTemporal.get(posicion));
					listaTemporal.remove(posicion);
				}
			}

			if (i == elementos && contador < 3) {
				i = 0;
				contador++;
			}
			i++;
		}
		if (listaTemporal.size() > 0) {
			ciudadesLejanas.addAll(listaTemporal);
		}
		// System.out.println("ciudades cerca " + ciudadesCercanas);
		// System.out.println("ciudades medio " + ciudadesMedias);
		// System.out.println("ciudades lejos " + ciudadesLejanas);
		return new Instance(mejorResultado, totalCiudades, listaCiudadesAgregadas, listaCiudadesDisponibles,
				matrizCostos, ciudadesCercanas, ciudadesMedias, ciudadesLejanas);
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
