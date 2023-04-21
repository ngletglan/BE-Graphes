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
        double cout;
        Node currentNode=this.get_current_node();
        if(this.data.getMode()== ShortestPathData.Mode.LENGTH){
            cout=currentNode.getPoint().distanceTo(this.data.getDestination().getPoint());
        }
        else{
            cout=currentNode.getPoint().distanceTo(this.data.getDestination().getPoint())/this.data.getGraph().getGraphInformation().getMaximumSpeed();
        }

        return cout;
    }
    
    public double get_total_cost() {
        return get_realized_cost()+this.estimer_cout();
    }
}
