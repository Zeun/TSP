package model;


import java.io.*;
import java.util.*;

import ec.util.Output;

public class FileIO {
	public static int newLog(Output output, String filename) throws IOException {//Se crea un nuevo archivo para escribir la salida en el??????
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
	        } 
	        else {
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
		int bestResult = Integer.parseInt(s.nextLine());
		int CT = Integer.parseInt(s.nextLine());
		
		ArrayList<ArrayList<Integer>> MD = new ArrayList<>();//Matriz de costos
		ArrayList<Integer> LCT = new ArrayList<>(); //Lista de Ciudades totales
		ArrayList<Integer> LCA = new ArrayList<>(); //Lista de Ciudades Agregadas
		ArrayList<Integer> ciudadesCercanas = new ArrayList<>(); //Lista de Ciudades Agregadas
		ArrayList<Integer> ciudadesMedias = new ArrayList<>(); //Lista de Ciudades Agregadas
		ArrayList<Integer> ciudadesLejanas = new ArrayList<>(); //Lista de Ciudades Agregadas
		ArrayList<Integer> listTemp;
		
		for (int i=0; i<CT; i++) {			
			listTemp = new ArrayList<>();
			for(int j=0; j<CT; j++){
				listTemp.add(s.nextInt());
			}
			MD.add(listTemp);
		}
		
		s.close();
		return new Instance(bestResult, CT, LCA,LCT,MD,ciudadesCercanas,ciudadesMedias,ciudadesLejanas);
	}
	
	public static void repairDot(int JOB_NUMBER) throws IOException {
		File file = new File("out/results/evolution"+JOB_NUMBER+"/job." + (JOB_NUMBER) + ".BestIndividual.dot");
		Scanner s = new Scanner(file);
		StringBuilder buffer = new StringBuilder();
		int i = 1;
		String label = "";
		
		while(s.hasNextLine()) {
			if(i == 1)
				label = s.nextLine();
			else if(i > 4) {
				buffer.append(s.nextLine() + "\n");
				if(i == 5)
					buffer.append(label + "\n");
			}
			else
				s.nextLine();
			i++;
		}
		
		writeFile(buffer.toString(), "out/results/evolution" + JOB_NUMBER + "/BestIndividual.dot");
		s.close();
	}
	
	public static void writeFile(String line, String filename) throws IOException {
		File file = new File(filename);
		
		//if (!file.exists()) {
			file.createNewFile();
		//}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line);
		bw.close();
	}
	
	public static void dot_a_png(int job_number) {
		try {
			System.out.println("[dot_a_png]");
			
			String dotPath = "C:/Program Files (x86)/Graphviz2.38/bin/dot.exe";
			String fileInputPath =	"out/results/evolution"+ job_number+"/BestIndividual.dot";
			String fileOutputPath =	"out/results/evolution"+ job_number+"/job." + job_number + ".BestIndividual.png";
			System.out.println(dotPath);
			System.out.println(fileInputPath);
			System.out.println(fileOutputPath);

			Runtime rt = Runtime.getRuntime();
			rt.exec(dotPath+" -Tpng "+fileInputPath+" -o "+fileOutputPath);

		} catch (IOException ioe) {
			System.out.println (ioe);
		} finally {
		}

	}
}
