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
	private ArrayList<Integer> ciudadesCercanasCentro = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesLejanasCentro = new ArrayList<>(); //Lista de Ciudades Agregadas
	public Instance(){

	}

	public Instance(
						String nombreInstancia,
						int mejorResultado, 
						int totalCiudades, 
						ArrayList<Integer> listaCiudadesAgregadas,
						ArrayList<Integer> listaCiudadesDisponibles, 
						ArrayList<ArrayList<Double>> matrizCostos2,
						ArrayList<Integer> ciudadesCercanasCentro, 
						ArrayList<Integer> ciudadesLejanasCentro,
						double worstEdge
					) 
	{
		this.nombreInstancia = nombreInstancia;
		this.mejorResultado = mejorResultado;
		this.totalCiudades = totalCiudades;
		this.matrizCostos = matrizCostos2;
		this.listaCiudadesDisponibles = listaCiudadesDisponibles;
		this.listaCiudadesAgregadas = listaCiudadesAgregadas;
		this.ciudadesCercanasCentro = ciudadesCercanasCentro;
		this.ciudadesLejanasCentro = ciudadesLejanasCentro;
		this.worstEdge = worstEdge;

	}

	@Override
	public Instance clone(){
		Instance clone = new Instance();
		clone.nombreInstancia = this.nombreInstancia;
		clone.mejorResultado = this.mejorResultado;
		clone.totalCiudades = this.totalCiudades;
		clone.matrizCostos = this.matrizCostos;
		clone.listaCiudadesDisponibles = new ArrayList<>(this.listaCiudadesDisponibles);
		clone.listaCiudadesAgregadas = new ArrayList<>(this.listaCiudadesAgregadas);
		clone.ciudadesCercanasCentro = new ArrayList<>(this.ciudadesCercanasCentro);
		clone.ciudadesLejanasCentro = new ArrayList<>(this.ciudadesLejanasCentro);
		clone.worstEdge = worstEdge;
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
	public boolean agregarCercaCentro() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			for (int i = 0; i < ciudadesCercanasCentro.size(); i++) {
				if (listaCiudadesDisponibles.contains(ciudadesCercanasCentro.get(i))) {
					listaCiudadesAgregadas.add(ciudadesCercanasCentro.get(i));
					listaCiudadesDisponibles.remove(listaCiudadesDisponibles.indexOf(ciudadesCercanasCentro.get(i)));
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
	public boolean agregarLejosCentro() {
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		} else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			for (int i = 0; i < ciudadesLejanasCentro.size(); i++) {
				if (listaCiudadesDisponibles.contains(ciudadesLejanasCentro.get(i))) {
					listaCiudadesAgregadas.add(ciudadesLejanasCentro.get(i));
					listaCiudadesDisponibles.remove(listaCiudadesDisponibles.indexOf(ciudadesLejanasCentro.get(i)));
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Agrega al circuito un nuevo nodo que tenga la menor distancia entre los actuales.
	 * Ejemplo:
	 * 		[ A, B, C]
	 * 		ingreso D... siendo A->D < B->D < C->D
	 * 		resultando:
	 * 		[ A, D, B, C]
	 * @return verdadero si puede agregar, falso cc
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
			double menorArco = matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaCiudadesDisponibles.get(0));
			int nodo_i = 0;
			int nodo_j = 0;
			for (int i = 0; i < listaCiudadesAgregadas.size(); i++) {
				for (int j = 0; j < listaCiudadesDisponibles.size(); j++) {
					double arcoActual = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesDisponibles.get(j));
					if (arcoActual < menorArco){
						nodo_i = i;
						nodo_j = j;
						menorArco = arcoActual;
					}
				}
			}
			listaCiudadesAgregadas.add(nodo_i, listaCiudadesDisponibles.get(nodo_j));
			listaCiudadesDisponibles.remove(listaCiudadesDisponibles.get(nodo_j));
			return true;
		}
	}
	
	/** Agrega al circuito un nuevo nodo que tenga la mayor distancia entre los actuales.
	 * Ejemplo:
	 * 		[ A, B, C]
	 * 		ingreso D... siendo A->D < B->D < C->D
	 * 		resultando:
	 * 		[ A, B, D, C]
	 * @return verdadero si puede agregar, falso cc
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
			double mayorArco = matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaCiudadesDisponibles.get(0));
			int nodo_i = 0;
			int nodo_j = 0;
			for (int i = 0; i < listaCiudadesAgregadas.size(); i++) {
				for (int j = 0; j < listaCiudadesDisponibles.size(); j++) {
					double arcoActual = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesDisponibles.get(j));
					if (arcoActual > mayorArco){
						nodo_i = i;
						nodo_j = j;
						mayorArco = arcoActual;
					}
				}
			}
			listaCiudadesAgregadas.add(nodo_i, listaCiudadesDisponibles.get(nodo_j));
			listaCiudadesDisponibles.remove(listaCiudadesDisponibles.get(nodo_j));
			return true;
		}
	}	

	/**
	 * Elimina el último nodo de la lista de nodos agregados
	 * @return verdadero si puede eliminar, falso cc
	 */
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
	 * Optimizador que utiliza la heurística 2-opt
	 * @return lista optimizada
	 */
	public boolean opt2(){
		if (listaCiudadesAgregadas.size() < 4) {
			return false;
		}
		if (listaCiudadesDisponibles.size() > 0) {
			return false;
		}
		double new_distance = costo(listaCiudadesAgregadas);
		double best_current_distance = new_distance;
		int numMejoras = 0;
		while (true){
			//if(listaCiudadesDisponibles.size()==0){
			
			for (int i = 1; i < listaCiudadesAgregadas.size(); i++) {
				for (int k = i + 1; k < listaCiudadesAgregadas.size(); k++) {
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
			// System.out.println("      best: " + best_current_distance);
			// System.out.println("      new: " + new_distance);
			if (costo(listaCiudadesAgregadas) == best_current_distance) {
				if (numMejoras > 0) {
					return true;
				} else {
					return false;
				}
			}
			
		}
//		if (new_distance == best_current_distance && numMejoras > 0) {
//			return true;
//		} else {
//			return false;
//		}
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

	/**
	 * Cambia el orden de las ciudades de la siguiente forma:
	 * la primera con la última, la segunda con la penúltima, etc.
	 * @return verdaro si produce mejora, falso cc
	 */
	public boolean invertir(){
		if (listaCiudadesAgregadas.size() < 3) {
			return false;
		}
		double costo_inicial = costo(listaCiudadesAgregadas);
		double costo_antes = costo_inicial;
		double costo_final = costo_inicial;
		int nodoCentroCircuito,
			numIteraciones = (listaCiudadesAgregadas.size() -1 )/ 2;
		if (listaCiudadesAgregadas.size() % 2 == 0){
			nodoCentroCircuito = -1;
		} else {
			nodoCentroCircuito = listaCiudadesAgregadas.size() / 2;
		}
		
		for (int i = 1; i < numIteraciones; i++) {
			if (i != nodoCentroCircuito) {
				ArrayList<Integer> LTemp = new ArrayList<Integer>(listaCiudadesAgregadas);
				Collections.swap(LTemp, i, LTemp.size() - i);
				costo_final = costo(LTemp);
				if (costo_final < costo_antes) {
					Collections.swap(listaCiudadesAgregadas, i, listaCiudadesAgregadas.size() - i);
					costo_antes = costo_final;
				}

			} else {
				break;
			}
		}
		if (costo_antes < costo_inicial) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Elimina los nodos i, j que componen el peor arco
	 * @return
	 */
	public boolean eliminarPeorArco() {
		if (listaCiudadesAgregadas.size() < 3) {
			return false;
		} else if (listaCiudadesAgregadas.size() == 3){
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(1));
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(2));
			listaCiudadesAgregadas.remove(1);
			listaCiudadesAgregadas.remove(1);
			return true;
		}
		
		double peorArco = matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaCiudadesAgregadas.get(1));
		int posicion = 0;

		for (int i = 1; i < listaCiudadesAgregadas.size() - 1; i++) {
			double arcoActual = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i+1));
			if(peorArco < arcoActual){
				posicion = i;
				peorArco = arcoActual;
			}
		}
		if (posicion == 0 ){
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion + 1));
			listaCiudadesAgregadas.remove(posicion + 1);
		} else {
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion));
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion + 1));
			listaCiudadesAgregadas.remove(posicion);
			listaCiudadesAgregadas.remove(posicion);
		}
		
		return true;
	}
	
	/**
	 * Elimina el nodo i que compone el peor arco
	 * @return
	 */
	public boolean eliminarPeorNodoi() {
		if (listaCiudadesAgregadas.size() < 2) {
			return false;
		} else if (listaCiudadesAgregadas.size() == 2){
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(1));
			listaCiudadesAgregadas.remove(1);
			return true;
		}
		double peorArco = matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaCiudadesAgregadas.get(1));
		int posicion = 0;

		for (int i = 1; i < listaCiudadesAgregadas.size() - 1; i++) {
			double arcoActual = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i + 1));
			if (peorArco < arcoActual) {
				posicion = i;
				peorArco = arcoActual;
			}
		}
		if (posicion != 0 ){
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion));
			listaCiudadesAgregadas.remove(posicion);
		}
		
		return true;
	}
	
	/**
	 * Elimina el nodo j que compone el peor arco
	 * @return
	 */
	public boolean eliminarPeorNodoj() {
		if (listaCiudadesAgregadas.size() < 2) {
			return false;
		} else if (listaCiudadesAgregadas.size() == 2){
			listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(1));
			listaCiudadesAgregadas.remove(1);
			return true;
		}
		double peorArco = matrizCostos.get(listaCiudadesAgregadas.get(0)).get(listaCiudadesAgregadas.get(1));
		int posicion = 0;

		for (int i = 1; i < listaCiudadesAgregadas.size() - 1; i++) {
			double arcoActual = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i+1));
			if(peorArco < arcoActual){
				posicion = i;
				peorArco = arcoActual;
			}
		}
		listaCiudadesDisponibles.add(listaCiudadesAgregadas.get(posicion + 1));
		listaCiudadesAgregadas.remove(posicion + 1);
	
		return true;
	}
	
	/**
	 * Calcula el costo del circuito de la lista final de ciudades
	 * @return costo del circuito
	 */
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

	/**
	 * Calcula el costo del circuito de una lista específica
	 * @return costo del circuito
	 */
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

	/**
	 * Imprime en pantalla el circuito actual, número de ciudades y resultado obtenido vs el óptimo
	 * @return log del resultado
	 */
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

