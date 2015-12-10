package functions;

import model.TSPData;
import model.TSProblem;
import ec.util.Parameter;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class DoWhile extends GPNode {

	private static final long serialVersionUID = 4322751948463734062L;

	public String toString() { return "Do_While"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 2) {
            state.output.error("Incorrect number of children for node " +  toStringForError() + " at " + individualBase);
        }
    }

	@Override
	public void eval(
		final EvolutionState state, final int thread,
		final GPData input, final ADFStack stack,
		final GPIndividual individual, final Problem problem) {

		boolean x, y;
		int i, n;
		double lastCost;
		TSPData tspdata = (TSPData) input;
		TSProblem tsproblem = (TSProblem) problem;
		
		children[0].eval(state, thread, tspdata, stack, individual, tsproblem );

		n = tspdata.getInstance().getListaCiudadesDisponibles().size();
		x = tspdata.getResult();
		i = 0;
		lastCost = -1;
		y = true;
		
		//Revisar
		while(x && y && i < n) {
			children[1].eval(state, thread, tspdata, stack, individual, tsproblem);
			if(tspdata.getInstance().costoCircuito() == lastCost) {
					y = false;
			}
			else {
				lastCost = tspdata.getInstance().costoCircuito();
				children[0].eval(state, thread, tspdata, stack, individual, tsproblem);
				x = tspdata.getResult();
				//System.out.println("Resultado del \"WHILE(izq)\":\t" + x );
				//System.out.println(n);
				i++;
			}
		}
		//Si el while iteró al menos una vez se considera resultado verdaderos, caso contrario es falso
		if(i > 0)
			tspdata.setResult(true);
		else
			tspdata.setResult(false);
		//state.output.println("Resultado del \"WHILE(out)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
	}
}