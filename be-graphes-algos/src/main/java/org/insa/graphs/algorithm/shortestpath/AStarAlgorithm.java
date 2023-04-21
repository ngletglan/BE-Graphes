package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    protected Label new_label(Node node,ShortestPathData donneData) {
        return new LabelStar(node,donneData);
    } 

}
