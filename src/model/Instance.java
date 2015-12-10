package model;


import java.util.ArrayList;
import java.util.Collections;

public class Instance {
	private int mejorResultado;
	private int totalCiudades;
	private ArrayList<ArrayList<Integer>> matrizCostos = new ArrayList<>();//Matriz de costos
	private ArrayList<Integer> listaCiudadesDisponibles = new ArrayList<>(); //Lista de Ciudades totales
	private ArrayList<Integer> listaCiudadesAgregadas = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesCercanas = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesMedias = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesLejanas = new ArrayList<>(); //Lista de Ciudades Agregadas
	public Instance(){

	}

	public Instance(int mejorResultado, 
			int totalCiudades, 
			ArrayList<Integer> listaCiudadesAgregadas,
			ArrayList<Integer> listaCiudadesDisponibles, 
			ArrayList<ArrayList<Integer>> matrizCostos,
			ArrayList<Integer> ciudadesCercanas, 
			ArrayList<Integer> ciudadesMedias,
			ArrayList<Integer> ciudadesLejanas) 
	{
		this.mejorResultado = mejorResultado;
		this.totalCiudades = totalCiudades;
		this.matrizCostos = matrizCostos;
		this.listaCiudadesDisponibles = listaCiudadesDisponibles;
		this.listaCiudadesAgregadas = listaCiudadesAgregadas;
		this.ciudadesCercanas = ciudadesCercanas;
		this.ciudadesMedias = ciudadesMedias;
		this.ciudadesLejanas = ciudadesLejanas;

	}

	@Override
	public Instance clone(){
		Instance clone = new Instance();
		clone.mejorResultado = this.mejorResultado;
		clone.totalCiudades = this.totalCiudades;
		clone.matrizCostos = this.matrizCostos;
		// ArrayList<Integer> listaTemporal = new ArrayList<>(listaCiudadesDisponibles); 
		clone.listaCiudadesDisponibles = new ArrayList<>(this.listaCiudadesDisponibles);
		clone.listaCiudadesAgregadas = new ArrayList<>(this.listaCiudadesAgregadas);
		clone.ciudadesCercanas = new ArrayList<>(this.ciudadesCercanas);
		clone.ciudadesMedias = new ArrayList<>(this.ciudadesMedias);
		clone.ciudadesLejanas = new ArrayList<>(this.ciudadesLejanas);
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
		for (ArrayList<Integer> iterable_element : matrizCostos) {
			response += iterable_element;
			response += "\n";
		}
		response += "]\n";
		return response;
	}


	//TERMINALES

	public boolean agregarMejorVecino(){
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		}
		else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}

			int posicion = -1;
			int costo = 1000000000;

			for (int j = 0; j < listaCiudadesDisponibles.size()-2; j++) {
				if(costo > matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1)).get(listaCiudadesDisponibles.get(j))){
					posicion = j;
					costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1)).get(listaCiudadesDisponibles.get(j));
				}
			}
			//Todas las ciudades que quedan son infactibles
			if (posicion == -1) {
				return false;
			}
			//Agrego la ciudad
			else {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(posicion));
				listaCiudadesDisponibles.remove(posicion);
				return true;
			}
		}
	}

	public boolean agregarPeorVecino(){
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		}
		else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}
			
			int posicion = 0;
			int costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1)).get(listaCiudadesDisponibles.get(0));
			
			for (int j = 0; j < listaCiudadesDisponibles.size()-2; j++) {
				if(costo < matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1)).get(listaCiudadesDisponibles.get(j))){					
					posicion = j;
					costo = matrizCostos.get(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1)).get(listaCiudadesDisponibles.get(j));
				}
			}
			listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(posicion));
			listaCiudadesDisponibles.remove(posicion);
			return true;				
		}
	}
	
	public boolean agregarCercano(){
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		}
		else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}
			// LCT

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

	public boolean agregarMediana(){
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		}
		else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}
			// LCT

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

	public boolean agregarLejano(){
		if (listaCiudadesDisponibles.size() == 0) {
			return false;
		}
		else {
			if (listaCiudadesDisponibles.size() == 1) {
				listaCiudadesAgregadas.add(listaCiudadesDisponibles.get(0));
				listaCiudadesDisponibles.remove(0);
				return true;
			}
			// LCT

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
		if (listaCiudadesDisponibles.size() == 1) {
			return false;
		}
		int arco = -2;
		int posicion= -3;

		for (int i = 1; i < listaCiudadesAgregadas.size() - 2; i++) {
			if(arco > matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i+1))){
				posicion = i;
				arco = matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesDisponibles.get(i+1));
			}
		}

		if (arco == -2) {
			return false;
		}

		else {
			@SuppressWarnings("unchecked")
			ArrayList<Integer> LTemp =(ArrayList<Integer>) listaCiudadesAgregadas.clone();
			LTemp.remove(posicion);
			LTemp.remove(posicion+1);

			listaCiudadesDisponibles.add(0, listaCiudadesAgregadas.get(posicion));
			listaCiudadesDisponibles.add(0, listaCiudadesAgregadas.get(posicion + 1));
			listaCiudadesAgregadas.remove(posicion);
			listaCiudadesAgregadas.remove(posicion + 1);
			return true;
			
			
		}
	}

	public boolean eliminarUltimo(){
		if (listaCiudadesAgregadas.size()==1 || listaCiudadesDisponibles.size()==1){
			return false;
		}
		else{
			listaCiudadesDisponibles.add(0, listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1));
			listaCiudadesAgregadas.remove(listaCiudadesAgregadas.size()-1);
			return true;
		}
	}

	public boolean opt2(){
		if (listaCiudadesAgregadas.size()<4) {
			return false;
		}
		if(listaCiudadesDisponibles.size()==0){
			while(true){
				int best_distance = costo(listaCiudadesAgregadas);
				for (int i = 1; i < listaCiudadesAgregadas.size()-1; i++) {
					for (int k = i + 1; k < listaCiudadesAgregadas.size()-1; k++) {
						ArrayList<Integer> new_route = optSwap(listaCiudadesAgregadas, i, k);
						if ( costo(new_route) <= best_distance ) {
							listaCiudadesAgregadas = new ArrayList<Integer> (new_route);
						}
					}
				}
				if (costo(listaCiudadesAgregadas) == best_distance){
					return true;
				}
			}
		}
		else {
			while(true){
				int best_distance = costo(listaCiudadesAgregadas);
				for (int i = 1; i < listaCiudadesAgregadas.size(); i++) {
					for (int k = i + 1; k < listaCiudadesAgregadas.size(); k++) {
						ArrayList<Integer> new_route = optSwap(listaCiudadesAgregadas, i, k);
						if ( costo(new_route) <= best_distance) {
							listaCiudadesAgregadas = new ArrayList<Integer> (new_route);
						}
					}
				}
				if (costo(listaCiudadesAgregadas) == best_distance){
					return true;
				}
			}
		}
	}

	public ArrayList<Integer> optSwap(ArrayList<Integer> route, int i, int k) {
		ArrayList<Integer> new_route = new ArrayList<Integer>();
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		if (i==1) {
			l1.add(route.get(0));
		}
		else {
			l1 = new ArrayList<Integer>(route.subList(0, i));
		}
		ArrayList<Integer> l2 = new ArrayList<Integer>(route.subList(i, k+1));
		Collections.reverse(l2);
		ArrayList<Integer> l3 = new ArrayList<Integer>(route.subList(k+1, route.size()));
		new_route.addAll(l1);
		new_route.addAll(l2);
		new_route.addAll(l3);
		return new_route;
	}

	public boolean swap(){
		if (listaCiudadesAgregadas.size()<3){
			return false;
		}
		if (listaCiudadesDisponibles.size() == 0) {
			int costo = costo(listaCiudadesAgregadas);
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
			int costo = costo(listaCiudadesAgregadas);
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

	public boolean inverse() {
		if (listaCiudadesDisponibles.size() == 1) {
			ArrayList<Integer> LTemp = new ArrayList<Integer> (listaCiudadesAgregadas);
			LTemp.remove(0);
			Collections.reverse(LTemp);
			LTemp.add(0, listaCiudadesAgregadas.get(0));

			if (costo(LTemp) < costo(listaCiudadesAgregadas)) {
				listaCiudadesAgregadas = new ArrayList<Integer> (LTemp);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			ArrayList<Integer> LTemp = new ArrayList<Integer> (listaCiudadesAgregadas);
			if (listaCiudadesAgregadas.size() > 1) {
				LTemp.remove(0);
				LTemp.remove(LTemp.size()-1);

				Collections.reverse(LTemp);
				LTemp.add(0, listaCiudadesAgregadas.get(0));
				LTemp.add(listaCiudadesAgregadas.get(listaCiudadesAgregadas.size()-1));
			}

			if (costo(LTemp)<costo(listaCiudadesAgregadas)) {
				listaCiudadesAgregadas = new ArrayList<Integer> (LTemp);
				return true;
			}
			else {
				return false;
			}
		}

	}

	public double costoCircuito() {
		double response = 0.0;
		if (listaCiudadesAgregadas.size() >= 2) {
			for (int i = 0; i < listaCiudadesAgregadas.size() - 2; i++) {
				response += matrizCostos.get(listaCiudadesAgregadas.get(i)).get(listaCiudadesAgregadas.get(i + 1));
			}
		}
		// else {
		// response = 400000000;
		// }
		return response;
	}

	public int costo(ArrayList<Integer> lista){
		int response =0;
		if(lista.size()>=2){
			for (int i=0; i<lista.size()-2;i++) {
				response += matrizCostos.get(lista.get(i)).get(lista.get(i+1));
			}
		}else{
			response =400000000;
		}
		return response;
	}

	public String printResult(){
		String response = "LCA = "+this.listaCiudadesAgregadas + "\n";
		response += "Costo = "+this.costoCircuito() + "\n";
		return response;
	}

	public boolean isNew(){
		if(listaCiudadesAgregadas.size()>1){
			return false;
		}else{
			return true;
		}
	}

	public int getTotalCiudades() {
		return totalCiudades;
	}

	public ArrayList<ArrayList<Integer>> getMatrizCostos() {
		return matrizCostos;
	}

	public ArrayList<Integer> getListaCiudadesAgregadas() {
		return listaCiudadesAgregadas;
	}

	public ArrayList<Integer> getListaCiudadesDisponibles(){
		return listaCiudadesDisponibles;
	}

	public int getOptimo(){
		return mejorResultado;
	}

}

