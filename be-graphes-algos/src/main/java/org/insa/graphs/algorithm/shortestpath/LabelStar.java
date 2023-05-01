package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label implements Comparable<Label>{

    private double inf;

    public LabelStar(Node node,ShortestPathData donneData){

        super(node);

        /* Cas 1 : On cherche LE PLUS COURT chemin */
        if(donneData.getMode()== AbstractInputData.Mode.LENGTH) {
            this.inf = (double)Point.distance(node.getPoint(), donneData.getDestination().getPoint());
        }

        /* Cas 2 : On cherche LE PLUS RAPIDE chemin */
        /* vitesse en kilomètres donc convertir en mètres */
        else {
            int vitesse = Math.max(donneData.getGraph().getGraphInformation().getMaximumSpeed(), donneData.getMaximumSpeed());
            this.inf = (double)Point.distance(node.getPoint(), donneData.getDestination().getPoint())/(vitesse*1000/3600);
        }
    }
    
   @Override 
    /* Renvoyer le coût de l'origine jusqu'au noeud + la distance du vol d'oiseau du noeud jusqu'à la destination */
    public double get_total_cost() {
        return this.inf + this.get_realized_cost();
    }
}
