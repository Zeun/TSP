package functions;

import model.TSPData;
import model.TSProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class IfThen extends GPNode {

	private static final long serialVersionUID = -8117533832171836800L;

	public String toString() { return "If_Then"; }
	
	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 2) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		TSPData tspData = (TSPData) input;
		TSProblem tsProblem = (TSProblem) problem;
		
		children[0].eval(state, thread, tspData, stack, individual, tsProblem);

		if(tspData.getResult()) {
			children[1].eval(state, thread, tspData, stack, individual, tsProblem);
			tspData.setResult(true);
			return;
		}
		
		tspData.setResult(false);
    }
}
