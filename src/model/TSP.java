package model;
public class TSP {	

	/**************************
	 * 	TERMINALES
	 **************************/
	public static boolean agregarMejorVecino (Instance ins){
		return ins.agregarMejorVecino();
	}
	public static boolean agregarPeorVecino (Instance ins){
		return ins.agregarPeorVecino();
	}
	public static boolean agregarCercaCentro (Instance ins){
		return ins.agregarCercaCentro();
	}
	public static boolean agregarLejosCentro (Instance ins){
		return ins.agregarLejosCentro();
	}
	public static boolean agregarCercano (Instance ins){
		return ins.agregarCercano();
	}
	public static boolean agregarLejano (Instance ins){
		return ins.agregarLejano();
	}
	public static boolean agregarArcoMenor (Instance ins){
		return ins.agregarArcoMenor();
	}
	public static boolean agregarArcoMayor (Instance ins){
		return ins.agregarArcoMayor();
	}
	public static boolean invertir (Instance ins){
		return ins.invertir();
	}
	public static boolean opt2 (Instance ins){
		return ins.opt2();
	}	
	public static boolean eliminarPeorArco (Instance ins){
		return ins.eliminarPeorArco();
	}
	public static boolean eliminarPeorNodoi (Instance ins){
		return ins.eliminarPeorNodoi();
	}
	public static boolean eliminarPeorNodoj (Instance ins){
		return ins.eliminarPeorNodoj();
	}
}