package org.insa.algo.strongconnectivity;

import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
import org.insa.graph.Node;

public class StronglyConnectedComponentsSolution extends AbstractSolution {
	
	// Components
	private ArrayList<ArrayList<Node>> components;

	protected StronglyConnectedComponentsSolution(StronglyConnectedComponentsInstance instance) {
		super(instance);
	}
	
	protected StronglyConnectedComponentsSolution(StronglyConnectedComponentsInstance instance, 
					   Status status, ArrayList<ArrayList<Node>> components) {
		super(instance, status);
		this.components = components;
	}
	
	/**
	 * @return Components of the solution, if any.
	 */
	public ArrayList<ArrayList<Node>> getComponents() { return components; }

}