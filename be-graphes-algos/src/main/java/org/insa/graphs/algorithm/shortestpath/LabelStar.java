package org.insa.graphs.algorithm.shortestpath;

import javax.xml.crypto.Data;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {
    private ShortestPathData data;
    public LabelStar(Node node,ShortestPathData donneData){
	super(node);
	this.data=donneData;
    }

    public double estimer_cout(){
	double cout=0;
	Node currentNode=this.get_current_node();
	if(this.data.getMode() == ShortestPathData.Mode.LENGTH){
            cout=currentNode.getPoint().distanceTo(this.data.getDestination().getPoint());
	}
        else if(this.data.getMode() == ShortestPathData.Mode.TIME){
            cout=currentNode.getPoint().distanceTo(this.data.getDestination().getPoint())/this.data.getGraph().getGraphInformation().getMaximumSpeed();
	    }
    
    return cout;
    }

    public double get_total_cost() {
	return get_realized_cost()+this.estimer_cout();
    }
}

