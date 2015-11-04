package functions;

import model.TSPData;
import model.TSProblem;
//import model.MISProblemEvo;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class Not extends GPNode {

	private static final long serialVersionUID = -247934018405822631L;

	public String toString() { return "Not"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {

		super.checkConstraints(state, tree, typicalIndividual, individualBase);

		if (children.length != 1) {
			state.output.error("Incorrect number of children for node " +  toStringForError() + " at " + individualBase);
		}
	}

	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		TSPData tspData = (TSPData) input;
		TSProblem tsProblem = (TSProblem) problem;

		children[0].eval(state, thread, tspData, stack, individual, tsProblem);

		tspData.setResult(!tspData.getResult());
	}
}