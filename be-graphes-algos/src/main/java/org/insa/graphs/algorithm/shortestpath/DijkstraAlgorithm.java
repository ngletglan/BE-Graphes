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

    /* Nombre de sommets visités */
    protected int nb_sommets_visites;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.nb_sommets_visites = 0;
    }

    @Override
    protected ShortestPathSolution doRun() {

        ShortestPathSolution solution = null;

        // Retrieve the graph
        boolean fin = false;
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        // Initialize array of labels
        Label[] labels = new Label[nbNodes];

        for (Node i : graph.getNodes()) {
            labels[i.getId()] = new_label(i,data);
        }  
        labels[data.getOrigin().getId()].realized_cost = 0;  

        // Initialize the priority heap
        BinaryHeap<Label> priority_heap = new BinaryHeap<Label>();

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        // Insert the debut node
        Label debut = new_label(data.getOrigin(),data);
        labels[debut.get_current_node().getId()] = debut;
        priority_heap.insert(debut);
        debut.set_in_tas();
        debut.set_realized_cost(0);

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
                /* Ex: il y a des chemins inaccessibles par certains transports */
                if (!data.isAllowed(arcIter)) {
                    continue;
                } 
                Node successor = arcIter.getDestination();

                /* Récupérer le label correspondant au noeud */
                Label successorLabel = labels[successor.getId()]; 

                /* Si le label n'existe pas on le crée */
                if (successorLabel == null) {
                    /* Informer l'observateur de cette création ?? */
                    notifyNodeReached(arcIter.getDestination());
                    successorLabel = new_label(successor,data);
                    labels[successorLabel.get_current_node().getId()] = successorLabel;

                    /* Incrémenter le nombre de sommets visités */
                    this.nb_sommets_visites++;
                } 

                /* Si le successeur n'est pas marqué.. */
                if (!successorLabel.get_marked()) {

                    /* Si ce coût est meilleur que le coût initial alors on met à jour le coût */
                    if((successorLabel.get_total_cost()>(current.get_cost()+data.getCost(arcIter)
						+(successorLabel.get_total_cost()-successorLabel.get_cost()))) || (successorLabel.get_cost() == Double.POSITIVE_INFINITY)) {
                        successorLabel.set_realized_cost(current.get_cost() + data.getCost(arcIter));
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
                arc = predecessorArcs[arc.getOrigin().getId()];
            } 

            /* Renverser le path */
            Collections.reverse(arcs);

            /* Créer la solution finale */
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));

            /* Indiquer le coût du chemin trouvé */
            double cout_dijkstra = labels[data.getDestination().getId()].get_total_cost();
            System.out.println("Le coût du chemin trouvé par Dijsktra est " + cout_dijkstra);


            /* Comparer avec le coût calculé par la classe Path */
            Path shortest_path = new Path(graph, arcs);
            double cout_path = shortest_path.getLength();
            System.out.println("Le coût du chemin trouvé par Path est " + cout_path);

            if (Math.abs(cout_path - cout_dijkstra) < 0.01) {
                System.out.println("Le résultat trouvé par Dijsktra est cohérent avec celui calculé par Path");
            } else {
                System.out.println("Le résultat trouvé par Dijsktra n'est pas cohérent avec celui calculé par Path, il faut le vérifier!");
            }

        }  

        return solution;
    }

    /* Créer le label correspondant au node */
    protected Label new_label(Node node,ShortestPathData Data) {
        return new Label(node);
    } 

    /* Compter le nombre de sommets visités */
    protected int get_nb_sommets_visites() {
        return this.nb_sommets_visites;
    } 

}