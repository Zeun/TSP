package terminals;

import model.TSP;
import model.TSPData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class AgregarCercaCentro extends GPNode {

	private static final long serialVersionUID = 4184426788689751137L;

	public String toString() { 
		return "ACercaCentro";
	}
	
	public void checkConstraints (
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 0) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	@Override
	public void eval(final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		TSPData tspData = (TSPData) input;		
		tspData.setResult(TSP.agregarCercaCentro(tspData.getInstance()));
	}
}
