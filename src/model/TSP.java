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
	
	public static boolean agregarCercano (Instance ins){
		return ins.agregarCercano();
	}
	public static boolean agregarCentral (Instance ins){
		return ins.agregarMediana();
	}
	public static boolean agregarLejano (Instance ins){
		return ins.agregarLejano();
	}
	public static boolean eliminarPeor (Instance ins){
		return ins.eliminarPeor();
	}
	public static boolean eliminarUltimo (Instance ins){
		return ins.eliminarUltimo();
	}
	public static boolean swap (Instance ins){
		return ins.swap();
	}
	public static boolean opt2 (Instance ins){
		return ins.opt2();
	}
	public static boolean inverse (Instance ins){
		return ins.inverse();
	}
}