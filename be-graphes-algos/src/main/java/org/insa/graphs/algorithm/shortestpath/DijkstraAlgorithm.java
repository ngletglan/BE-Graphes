package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        ShortestPathSolution solution = null;

        // Retrieve the graph
        boolean fin = false;
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();

        // Initialize array of labels
        Label[] labels = new Label[nbNodes];

        for (Label i : labels) {
            i.realized_cost = Float.POSITIVE_INFINITY; 
            i.set_father_node(null);
        } 
        labels[data.getOrigin().getId()].realized_cost = 0;  

        // Initialize the priority heap
        BinaryHeap<Label> priority_heap = new BinaryHeap<Label>();

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        // Insert the debut node
        Label debut = new Label(data.getOrigin());
        labels[debut.get_current_node().getId()] = debut;
        priority_heap.insert(debut);
        debut.set_in_tas();
        debut.set_realized_cost(0);

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        /* Tant qu'il existe des sommets non marquées.. */
        while (!fin && !priority_heap.isEmpty()) {

            Label current = priority_heap.deleteMin();

            // Indiquer aux observateurs que le node a été marqué 
            notifyNodeMarked(current.get_current_node());
            current.set_marked();

            /* Si le sommet initial est la destination, on s'arrête */
            if (current.get_current_node() == data.getDestination()) {
                fin = true;
            } 

            /* Parcourir des successeurs du sommet initial */
            Iterator<Arc> successorArc = current.get_current_node().iterator();

            while (successorArc.hasNext()) {

                Arc arcIter = successorArc.next();

                /* On doit d'abord vérifier qu'on peut prendre cet arc */
                if (!data.isAllowed(arcIter)) {
                    continue;
                } 
                Node successor = arcIter.getDestination();

                /* Récupérer le label correspondant au noeud */
                Label successorLabel = labels[successor.getId()]; 

                /* Si le label n'existe pas on le crée */
                if (successorLabel == null) {
                    /* Informer l'observateur de cette création ?? */
                    successorLabel = new Label(successor);
                    labels[successorLabel.get_current_node().getId()] = successorLabel;
                } 

                /* Si le successeur n'est pas marqué.. */
                if (!successorLabel.get_marked()) {

                    /* Si ce coût est meilleur que le coût initial alors on met à jour le coût */
                    if ((successorLabel.get_realized_cost() > (current.get_realized_cost() + data.getCost(arcIter)
                    + successorLabel.get_realized_cost()+successorLabel.get_cost()))) 
                    || (successorLabel.get_realized_cost() == Float.POSITIVE_INFINITY) {
                        successorLabel.set_realized_cost(current.get_realized_cost() + data.getCost(arcIter));
                        successorLabel.set_father_node(current.get_current_node());

                    /* Si le label est déjà dans le tas */
                    /* On met à jour sa position */
                    if (successorLabel.get_in_tas()) {
                        priority_heap.remove(successorLabel);
                    } 
                    /* Sinon on l'ajoute dans le tas */
                    else {
                        successorLabel.set_in_tas();
                    } 
                    priority_heap.insert(successorLabel);
                    predecessorArcs[arcIter.getDestination().getId()] = arcIter;
                }   
                }  
            } 
        } 

        /* Si la destination n'a pas de prédécesseur, alors la solution est ?? */
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            /* Sinon on a trouvé une solution */

            /* Indiquer aux observateurs qu'on a trouvé une solution */
            notifyDestinationReached(data.getDestination());

            /* Créer le path */
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[data.getOrigin().getId()];
            } 

            /* Renverser le path */
            Collections.reverse(arcs);

            /* Créer la solution finale */
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));

        }  

        return solution;
    }

}
