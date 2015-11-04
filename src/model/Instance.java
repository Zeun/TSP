package model;


import java.util.ArrayList;
import java.util.Collections;

public class Instance {
	private int bestResult;
	private int CT;
	private ArrayList<ArrayList<Integer>> MD = new ArrayList<>();//Matriz de costos
	private ArrayList<Integer> LCT = new ArrayList<>(); //Lista de Ciudades totales
	private ArrayList<Integer> LCA = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesCercanas = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesCentrales = new ArrayList<>(); //Lista de Ciudades Agregadas
	private ArrayList<Integer> ciudadesLejanas = new ArrayList<>(); //Lista de Ciudades Agregadas
	public Instance(){
		
	}
	
	public Instance(
			int bestResult,
			int CT,
			ArrayList<Integer> LCA,
			ArrayList<Integer> LCT,
			ArrayList<ArrayList<Integer>> MD,
			ArrayList<Integer> ciudadesCercanas,
			ArrayList<Integer> ciudadesCentrales,
			ArrayList<Integer> ciudadesLejanas
		){
		
		this.bestResult = bestResult;
		this.CT = CT;
		this.MD = MD;
		this.LCT = LCT;
		this.LCA = LCA;
		this.ciudadesCercanas=ciudadesCercanas;
		this.ciudadesCentrales=ciudadesCentrales;
		this.ciudadesLejanas=ciudadesLejanas;
		
		for (int i=1; i<CT+1;i++) {
			LCT.add(i);
		}
		
		LCA.add(0);
		ArrayList<Integer> LTemp = new ArrayList<Integer>(LCT);

		LTemp.remove(LTemp.size()-1);
		int objetos = LTemp.size()/3;
		int resto = LTemp.size()%3;

		int mayor;
		int posicion;
		int contador = 1;

		for (int i = 0;i < objetos;i++){
		  mayor = 10000000;
		  posicion = -3;
			for (int j=0; j < LTemp.size(); j++){
				if (mayor > MD.get(LCA.get(0)).get(LTemp.get(j))){
					mayor=MD.get(LCA.get(0)).get(LTemp.get(j));
					posicion=j;
				}
			}

		  if (contador == 1){
		    ciudadesCercanas.add(posicion);
		    LTemp.remove(posicion);
		  }
		  else if (contador == 2){
		    ciudadesCentrales.add(posicion);
		    LTemp.remove(posicion);
		  }
		  else{
		    ciudadesCentrales.add(posicion);
		    LTemp.remove(posicion);
		  }

		  if (i == objetos-1 && contador < 3){
		    i=0;
		    contador ++;
		    if (contador == 3){
		      objetos+= resto;
		    }
		  }
		}
	}
	
	@Override
	public Instance clone(){
		Instance clone = new Instance();
		clone.bestResult = this.bestResult;
		clone.CT = this.CT;
		clone.MD = this.MD;
		
		for (Integer temp : this.LCT) {clone.LCT.add(temp);}
		for (Integer temp : this.LCA) {clone.LCA.add(temp);}
		for (Integer temp : this.ciudadesCercanas) {clone.ciudadesCercanas.add(temp);}
		for (Integer temp : this.ciudadesCentrales) {clone.ciudadesCentrales.add(temp);}
		for (Integer temp : this.ciudadesLejanas) {clone.ciudadesLejanas.add(temp);}
		return clone;
	}
	
	@Override
	public String toString(){
		String response ="N: "+CT+"\n";
		for (ArrayList<Integer> iterable_element : MD) {
			response += iterable_element;
			response += "\n";
		}
		response += "]\n";
		return response;
	}
	
	//TERMINALES
	
	public boolean agregarMejorVecino(){
		if (LCT.size() == 0) {
			return false;
		}
		else {
			if (LCT.size() == 1) {
				LCA.add(LCT.get(0));
				LCT.remove(0);
				return true;
			}
			
			int posicion = -1;
			int costo = 1000000000;
			
			for (int j = 0; j < LCT.size()- 2; j++) {
				if(MD.get(LCA.get(LCA.size()-1)).get(LCT.get(j)) != -1 && costo > MD.get(LCA.get(LCA.size()-1)).get(LCT.get(j))){					
					posicion = j;
					costo = MD.get(LCA.get(LCA.size()-1)).get(LCT.get(j));
				}
			}
			//Todas las ciudades que quedan son infactibles
			if (posicion == -1) {
				return false;
			}
			//Agrego la ciudad
			else {
				LCA.add(LCT.get(posicion));
				LCT.remove(posicion);
				return true;				
			}
		}
	}
	
	public boolean agregarPeorVecino(){
		if (LCT.size() == 0) {
			return false;
		}
		else {
			if (LCT.size() == 1) {
				LCA.add(LCT.get(0));
				LCT.remove(0);
				return true;
			}
			
			int posicion = -1;
			int costo = -2;
			
			for (int j = 0; j < LCT.size()-2; j++) {
				if(MD.get(LCA.get(LCA.size()-1)).get(LCT.get(j)) != -1 && costo < MD.get(LCA.get(LCA.size()-1)).get(LCT.get(j))){					
					posicion = j;
					costo = MD.get(LCA.get(LCA.size()-1)).get(LCT.get(j));
				}
			}
			//Todas las ciudades que quedan son infactibles
			if (posicion == -1) {
				return false;
			}
			//Agrego la ciudad
			else {
				LCA.add(LCT.get(posicion));
				LCT.remove(posicion);
				return true;				
			}
		}
	}
	
	public boolean agregarCercano(){
		if (LCT.size() == 0) {
			return false;
		}
		else {
			if (LCT.size() == 1) {
				LCA.add(LCT.get(0));
				LCT.remove(0);
				return true;
			}
			// LCT
			
			for (int i = 0; i < ciudadesCercanas.size(); i++) {
				if (MD.get(LCA.get(LCA.size()-1)).get(ciudadesCercanas.get(i)) != -1 && LCT.contains(ciudadesCercanas.get(i))) {
					LCA.add(ciudadesCercanas.get(i));
					LCT.remove(LCT.indexOf(ciudadesCercanas.get(i)));
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean agregarCentral(){
		if (LCT.size() == 0) {
			return false;
		}
		else {
			if (LCT.size() == 1) {
				LCA.add(LCT.get(0));
				LCT.remove(0);
				return true;
			}
			// LCT
			
			for (int i = 0; i < ciudadesCentrales.size(); i++) {
				if (MD.get(LCA.get(LCA.size()-1)).get(ciudadesCentrales.get(i)) != -1 && LCT.contains(ciudadesCentrales.get(i))) {
					LCA.add(ciudadesCentrales.get(i));
					LCT.remove(LCT.indexOf(ciudadesCentrales.get(i)));
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean agregarLejano(){
		if (LCT.size() == 0) {
			return false;
		}
		else {
			if (LCT.size() == 1) {
				LCA.add(LCT.get(0));
				LCT.remove(0);
				return true;
			}
			// LCT
			
			for (int i = 0; i < ciudadesLejanas.size(); i++) {
				if (MD.get(LCA.get(LCA.size()-1)).get(ciudadesLejanas.get(i)) != -1 && LCT.contains(ciudadesLejanas.get(i))) {
					LCA.add(ciudadesLejanas.get(i));
					LCT.remove(LCT.indexOf(ciudadesLejanas.get(i)));
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean eliminarPeor(){
		if (LCT.size() == 1) {
			return false;
		}
		int arco = -2;
		int posicion= -3;
		
		for (int i = 1; i < LCA.size() - 2; i++) {
			if(MD.get(LCA.get(i)).get(LCA.get(i+1)) != -1 && arco > MD.get(LCA.get(i)).get(LCA.get(i+1))){					
				posicion = i;
				arco = MD.get(LCA.get(i)).get(LCT.get(i+1));
			}
		}
		
		if (arco == -2) {
			return false;
		}
		
		else {
			ArrayList<Integer> LTemp = new ArrayList<Integer> (LCA);
			LTemp.remove(posicion);
			LTemp.remove(posicion+1);
			if (verificar(LTemp)){
				LCT.add(0, LCA.get(posicion));
				LCT.add(0, LCA.get(posicion+1));
				LCA.remove(posicion);
				LCA.remove(posicion+1);
				return true;
			}
			return false;
		}
	}
	
	public boolean eliminarUltimo(){
		if (LCA.size()==1 || LCT.size()==1){
			return false;
		}
		else{
			LCT.add(0, LCA.get(LCA.size()-1));
			LCA.remove(LCA.size()-1);
			return true;
		}
	}
	
	public boolean opt2(){
		if (LCA.size()<4) {
			return false;
		}
		if(LCT.size()==0){
			while(true){
				int best_distance = costo(LCA);
				for (int i = 1; i < LCA.size()-1; i++) {
					for (int k = i + 1; k < LCA.size()-1; k++) {
						ArrayList<Integer> new_route = optSwap(LCA, i, k);
						if ( costo(new_route) < best_distance && verificar(new_route)) {
							LCA = new ArrayList<Integer> (new_route);
						}
					}
				}
				if (costo(LCA) == best_distance){
					return true;
				}
			}
		}
		else {
			while(true){
				int best_distance = costo(LCA);
				for (int i = 1; i < LCA.size(); i++) {
					for (int k = i + 1; k < LCA.size(); k++) {
						ArrayList<Integer> new_route = optSwap(LCA, i, k);
						if ( costo(new_route) < best_distance && verificar(new_route)) {
							LCA = new ArrayList<Integer> (new_route);
						}
					}
				}
				if (costo(LCA) == best_distance){
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
		if (LCA.size()<3){
			return false;
		}
		if (LCT.size() == 0) {
			int costo = costo(LCA);
			for (int i=1; i < LCA.size() - 2; i++) {
				ArrayList<Integer> LTemp = new ArrayList<Integer> (LCA);
				Collections.swap(LTemp,i,i+1);
				if(costo(LTemp) < costo(LCA) && verificar(LTemp)){					
					Collections.swap(LCA,i,i+1);
				}
			}
			if (costo != costo(LCA)) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			int costo = costo(LCA);
			for (int i=1; i<LCA.size(); i++) {
				ArrayList<Integer> LTemp = new ArrayList<Integer> (LCA);
				Collections.swap(LTemp,i,i+1);
				if(costo(LTemp) < costo(LCA) && verificar(LTemp)){					
					Collections.swap(LCA,i,i+1);
				}
			}
			if (costo != costo(LCA)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean inverse() {
		if (LCT.size() == 1) {
			ArrayList<Integer> LTemp = new ArrayList<Integer> (LCA);
			LTemp.remove(0);
			Collections.reverse(LTemp);
			LTemp.add(0, LCA.get(0));
			
			if (verificar(LTemp) && costo(LTemp) < costo(LCA)) {
				LCA = new ArrayList<Integer> (LTemp);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			ArrayList<Integer> LTemp = new ArrayList<Integer> (LCA);
			if (LCA.size() > 1) {
				LTemp.remove(0);
				LTemp.remove(LTemp.size()-1);

				Collections.reverse(LTemp);
				LTemp.add(0, LCA.get(0));
				LTemp.add(LCA.get(LCA.size()-1));
			}
			
			if (verificar(LTemp) && costo(LTemp)<costo(LCA)) {
				LCA = new ArrayList<Integer> (LTemp);
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	
	private boolean verificar(ArrayList<Integer> lista){
		for(int i=0;i < lista.size()-1;i++){
			if(MD.get(lista.get(i)).get(lista.get(i+1)) ==-1) {
				return false;
			}
		}
		return true;
	}
	
	public int cost(){	
		int response =0;
		if(LCA.size()>=2){
			for (int i=0; i < LCA.size()-1;i++) {				
				response += MD.get(LCA.get(i)).get(LCA.get(i+1));
			}
		}else{
			response =400000000;
		}
		return response;
	}
	
	public int costo(ArrayList<Integer> lista){
		int response =0;
		if(lista.size()>=2){
			for (int i=0; i < lista.size()-1;i++) {			
				response += MD.get(lista.get(i)).get(lista.get(i+1));
			}
		}else{
			response = 400000000;
		}
		return response;
	}
	
	public String printResult(){
		String response = "LCA = " + this.LCA + "\n";
		response += "Costo = " + this.cost() + "\n";
		return response;
	}
	
	public boolean isNew() {
		if (LCA.size()>1) {
			return false;
		}
		else {
			return true;
		}
	}

	public int getCT() {
		return CT;
	}

	public ArrayList<ArrayList<Integer>> getMD() {
		return MD;
	}

	public ArrayList<Integer> getLCA() {
		return LCA;
	}

	public ArrayList<Integer> getLCT(){
		return LCT;
	}	
	
	public int getBestResult(){
		return bestResult;
	}	
	
}

