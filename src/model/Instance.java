package model;


import java.util.ArrayList;
import java.util.Collections;

public class Instance {
	private String nombreInstancia;
	private int mejorResultado;
	private int totalCiudades;
	private double worstEdge;
	private ArrayList<ArrayList<Double>> matrizCostos = new ArrayList<>();//Matriz de costos
	private ArrayList<Integer> listaCiudadesDisponibles = new ArrayList<>(); //Lista de Ciudades totales
	private ArrayList<Integer> listaCiudadesAgregadas = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesCercanas = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesMedias = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesLejanas = new ArrayList<>(); //Lista de Ciudades Agregadas
	public Instance(){

	}

	public Instance(
						String nombreInstancia,
						int mejorResultado, 
						int totalCiudades, 
						ArrayList<Integer> listaCiudadesAgregadas,
						ArrayList<Integer> listaCiudadesDisponibles, 
						ArrayList<ArrayList<Double>> matrizCostos2,
						ArrayList<Integer> ciudadesCercanas, 
						ArrayList<Integer> ciudadesMedias,
						ArrayList<Integer> ciudadesLejanas,
						double worstEdge
					) 
	{
		this.nombreInstancia = nombreInstancia;
		this.mejorResultado = mejorResultado;
		this.totalCiudades = totalCiudades;
		this.matrizCostos = matrizCostos2;
		this.listaCiudadesDisponibles = listaCiudadesDisponibles;
		this.listaCiudadesAgregadas = listaCiudadesAgregadas;
		this.ciudadesCercanas = ciudadesCercanas;
		this.ciudadesMedias = ciudadesMedias;
		this.ciudadesLejanas = ciudadesLejanas;
		this.worstEdge = worstEdge;

	}

	@Override
	public Instance clone(){
		Instance clone = new Instance();
		clone.nombreInstancia = this.nombreInstancia;
		clone.mejorResultado = this.mejorResultado;
		clone.totalCiudades = this.totalCiudades;
		clone.matrizCostos = this.matrizCostos;
		// ArrayList<Integer> listaTemporal = new ArrayList<>(listaCiudadesDisponibles); 
		clone.listaCiudadesDisponibles = new ArrayList<>(this.listaCiudadesDisponibles);
//		System.out.println("CD Antes " + this.listaCiudadesDisponibles);
//		System.out.println("CD Dsps " + clone.listaCiudadesDisponibles);
		clone.listaCiudadesAgregadas = new ArrayList<>(this.listaCiudadesAgregadas);
//		System.out.println("CA Antes " + this.listaCiudadesAgregadas);
//		System.out.println("CA Dsps " + clone.listaCiudadesAgregadas);
		clone.ciudadesCercanas = new ArrayList<>(this.ciudadesCercanas);
		clone.ciudadesMedias = new ArrayList<>(this.ciudadesMedias);
		clone.ciudadesLejanas = new ArrayList<>(this.ciudadesLejanas);
		clone.worstEdge = worstEdge;
//		for (Integer temp : this.LCT) {clone.LCT.add(temp);}
//		for (Integer temp : this.LCA) {clone.LCA.add(temp);}
//		for (Integer temp : this.ciudadesCercanas) {clone.ciudadesCercanas.add(temp);}
//		for (Integer temp : this.ciudadesMedias) {clone.ciudadesMedias.add(temp);}
//		for (Integer temp : this.ciudadesLejanas) {clone.ciudadesLejanas.add(temp);}
		return clone;
	}


	@Override
	public String toString(){
		String response ="N: "+totalCiudades+"\n";
		for (ArrayList<Double> iterable_element : matrizCostos) {
			response += iterable_element;
			response += "\n";
		}
		response += "]\n";
		return response;
	}


	//TERMINALES
	/**
	 * Agrega el vecino que agregue el menor costo al circuito
	 * @return verdadero si puede agregar una ciudad; falso cc.
	 */
	public boolean agregarMejorVecino() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			int posicion = 0;
			double costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
					.get(listaCiudadesDisponibles.get(1));

			for (int j = 0; j < listaCiudadesDisponibles.size(); j++) {
				if (costo > matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
						.get(listaCiudadesDisponibles.get(j))) {
					posicion = j;
					costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
							.get(listaCiudadesDisponibles.get(j));
				}
			}
			// Agrego la ciudad
			listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(posicion));
			listaCiudadesDisponibles.remove(posicion);
			return true;

		}
	}

	/**
	 * Agrega el vecino que agregue el mayor costo al circuito
	 * @return verdadero si puede agregar una ciudad; falso cc.
	 */
	public boolean agregarPeorVecino() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			int posicion = 0;
			double costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
					.get(listaCiudadesDisponibles.get(0));

			for (int j = 0; j < listaCiudadesDisponibles.size(); j++) {
				if (costo < matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
						.get(listaCiudadesDisponibles.get(j))) {
					posicion = j;
					costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
							.get(listaCiudadesDisponibles.get(j));
				}
			}
			// Agrego ciudad
			listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(posicion));
			listaCiudadesDisponibles.remove(posicion);
			return true;
		}
	}
	
	/**
	 * Agrego el vecino más cercano
	 * @return verdadero si puede agregar una ciudad; falso cc.
	 */
	public boolean agregarCercano() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			for (int i = 0; i < ciudadesCercanas.size(); i++) {
				if (listaCiudadesDisponibles.contains(ciudadesCercanas.get(i))) {
					listaCiudadesAgregadas.add(ciudadesCercanas.get(i));
					listaCiudadesDisponibles.remove(listaCiudadesDisponibles.indexOf(ciudadesCercanas.get(i)));
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Agrego el vecino a distancia media
	 * @return verdadero si puede agregar una ciudad; falso cc.
	 */
	public boolean agregarMediana() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			for (int i = 0; i < ciudadesMedias.size(); i++) {
				if (listaCiudadesDisponibles.contains(ciudadesMedias.get(i))) {
					listaCiudadesAgregadas.add(ciudadesMedias.get(i));
					listaCiudadesDisponibles.remove(listaCiudadesDisponibles.indexOf(ciudadesMedias.get(i)));
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Agrego el vecino más lejano
	 * @return verdadero si puede agregar una ciudad; falso cc.
	 */
	public boolean agregarLejano() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			for (int i = 0; i < ciudadesLejanas.size(); i++) {
				if (listaCiudadesDisponibles.contains(ciudadesLejanas.get(i))) {
					listaCiudadesAgregadas.add(ciudadesLejanas.get(i));
					listaCiudadesDisponibles.remove(listaCiudadesDisponibles.indexOf(ciudadesLejanas.get(i)));
					return true;
				}
			}
			return false;
		}
	}

	public boolean eliminarPeor(){
		if (listaCiudadesAgregadas.size() == 1) {
			return false;
		}
		double arco = matrizCostos.get(listaCiudadesAgregadas.get(1)).get(listaCiudadesAgregadas.get(2));
		int posicion = 1;

		for (int i = 1; i < listaCiudadesAgregadas.size() - 2; i++) {
			if(arco > matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i+1))){
				posicion = i;
				arco = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesDisponibles.get(i+1));
			}
		}
//		ArrayList<Integer> LTemp = new ArrayList<Integer>(listaCiudadesAgregadas);
//		LTemp.remove(posicion);
//		LTemp.remove(posicion);
		listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion));
		listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion + 1));
		listaCiudadesAgregadas.remove(posicion);
		listaCiudadesAgregadas.remove(posicion);
		return true;
			
			
	}

	public boolean eliminarUltimo(){
		if (listaCiudadesAgregadas.size() == 1) {
			return false;
		} else {
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1));
			listaCiudadesAgregadas.remove(listaCiudadesAgregadas.size() - 1);
			return true;
		}
	}
	
	/**
	 * Optimizador 2opt
	 * @return lista optimizada
	 */
	public boolean opt2(){
		if (listaCiudadesAgregadas.size() < 4) {
			return false;
		}
		//if(listaCiudadesDisponibles.size()==0){
		double new_distance = costo(listaCiudadesAgregadas);
		double best_current_distance = new_distance;
		int numMejoras = 0;
		for (int i = 1; i < listaCiudadesAgregadas.size() - 1; i++) {
			for (int k = i + 1; k < listaCiudadesAgregadas.size() - 1; k++) {
				ArrayList<Integer> new_route = optSwap(listaCiudadesAgregadas, i, k);
				new_distance = costo(new_route);
				if (new_distance < best_current_distance) {
					listaCiudadesAgregadas = new ArrayList<Integer>(new_route);
					best_current_distance = new_distance;
					numMejoras++;
				}
			}
		}
		Runtime garbage = Runtime.getRuntime();
		garbage.gc();
		if (new_distance == best_current_distance && numMejoras > 0) {
			return true;
		} else {
			return false;
		}
				
		
	}

	public ArrayList<Integer> optSwap(ArrayList<Integer> route, int i, int k) {
		ArrayList<Integer> new_route = new ArrayList<Integer>();
		// ArrayList<Integer> l1 = new ArrayList<Integer>();
		if (i==1) {
			// l1.add(route.get(0));
			new_route.add(route.get(0));
		}
		else {
			// l1 = new ArrayList<Integer>(route.subList(0, i));
			new_route.addAll(route.subList(0, i));
		}
		ArrayList<Integer> l2 = new ArrayList<Integer>(route.subList(i, k+1));
		Collections.reverse(l2);
		new_route.addAll(l2);
		new_route.addAll(route.subList(k+1, route.size()));
		//ArrayList<Integer> l3 = new ArrayList<Integer>(route.subList(k+1, route.size()));
		/*new_route.addAll(l1);
		new_route.addAll(l2);
		new_route.addAll(l3);
		l1.clear();
		l2.clear();
		l3.clear();*/
		
		return new_route;
	}

	public boolean swap(){
		if (listaCiudadesAgregadas.size()<3){
			return false;
		}
		if (listaCiudadesDisponibles.size() == 0) {
			double costo = costo(listaCiudadesAgregadas);
			for (int i=1; i < listaCiudadesAgregadas.size() - 2; i++) {
				ArrayList<Integer> LTemp = new ArrayList<Integer> (listaCiudadesAgregadas);
				Collections.swap(LTemp,i,i+1);
				if(costo(LTemp) < costo(listaCiudadesAgregadas)){
					Collections.swap(listaCiudadesAgregadas,i,i+1);
				}
			}
			if (costo != costo(listaCiudadesAgregadas)) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			double costo = costo(listaCiudadesAgregadas);
			for (int i=1; i<listaCiudadesAgregadas.size()-1; i++) {
				ArrayList<Integer> LTemp = new ArrayList<Integer> (listaCiudadesAgregadas);
				Collections.swap(LTemp,i,i+1);
				if(costo(LTemp) < costo(listaCiudadesAgregadas)){
					Collections.swap(listaCiudadesAgregadas,i,i+1);
				}
			}
			if (costo != costo(listaCiudadesAgregadas)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * Invierte todo los elementos de una lista ignorando los bordes
	 * @return lista invertida
	 */
	public boolean inverse() {
		// Si es menor o igual a 3 no pasa nada y se retorna true
		if (listaCiudadesAgregadas.size() <= 2) {
//			ArrayList<Integer> LTemp = new ArrayList<Integer> (listaCiudadesAgregadas);
//			LTemp.remove(0);
//			Collections.reverse(LTemp);
//			LTemp.add(0, listaCiudadesAgregadas.get(0));
//
//			if (costo(LTemp) < costo(listaCiudadesAgregadas)) {
//				listaCiudadesAgregadas = new ArrayList<Integer> (LTemp);
//				return true;
//			}
//			else {
//				return false;
//			}
			return false;
		}
		else {
			ArrayList<Integer> LTemp = new ArrayList<Integer>(listaCiudadesAgregadas);
			// System.out.println(LTemp);
			if (listaCiudadesDisponibles.size() == 0)
			LTemp.remove(0);
			// LTemp.remove(LTemp.size() - 1);

			Collections.reverse(LTemp);
			LTemp.add(0, listaCiudadesAgregadas.get(0));
			// LTemp.add(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1));
			
			if (costo(LTemp) < costo(listaCiudadesAgregadas)) {
				listaCiudadesAgregadas = new ArrayList<Integer>(LTemp);
				return true;
			} else {
				return false;
			}
		}

	}

	public double costoCircuito() {
		double total = 0.0;
		if (listaCiudadesAgregadas.size() >= 2) {
			for (int i = 0; i < listaCiudadesAgregadas.size() - 1; i++) {
				total += matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i + 1));
			}
			 total += matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size() - 1))
			 		.get(listaCiudadesAgregadas.get(0));
		} else {
			total = 400000000;
		}
		return total;
	}

	public double costo(ArrayList<Integer> lista){
		double total = 0.0;
		if (lista.size() >= 2) {
			for (int i = 0; i < lista.size() - 1; i++) {
				total += matrizCostos.get(lista.get(i)).get(lista.get(i + 1));
			}
			 total += matrizCostos.get(lista.get(lista.size() - 1))
			 		.get(lista.get(0));
		} else {
			total = 400000000;
		}
		return total;
	}

	public String printResult(){
		String response = "lista ciudades agregadas = " + this.listaCiudadesAgregadas + "\n";
		response += "ciudades = " + this.listaCiudadesAgregadas.size() + " / " + this.getTotalCiudades() + "\n";
		response += "costo = " + this.costoCircuito() + " / " + this.getOptimo() + "\n";
		return response;
	}

	public boolean isNew(){
		if(listaCiudadesAgregadas.size()>1){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 
	 * @return número de ciudades
	 */
	public int getTotalCiudades() {
		return totalCiudades;
	}

	public ArrayList<ArrayList<Double>> getMatrizCostos() {
		return matrizCostos;
	}

	public ArrayList<Integer> getListaCiudadesAgregadas() {
		return listaCiudadesAgregadas;
	}

	public ArrayList<Integer> getListaCiudadesDisponibles(){
		return listaCiudadesDisponibles;
	}
	
	/**
	 * 
	 * @return óptimo instancia
	 */
	public int getOptimo(){
		return mejorResultado;
	}
	
	/**
	 * 
	 * @return nombre instancia
	 */
	public String getNombreInstancia(){
		return nombreInstancia;
	}
	
	/**
	 * 
	 * @return peor arco de la instancia
	 */
	public double getWorstEdge(){
		return worstEdge;
	}

}

