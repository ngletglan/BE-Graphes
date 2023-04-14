package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{

    /*
     * Les attributs
     */
    
     /* Sommet associé à ce label(sommet ou numéro de sommet) */
     private Node current_node;

     /* Booléen, vrai si le coût min de ce sommet est définitivement connu par l'algorithm */
     private boolean marked;

     /* Valeur courant du plus court chemin depuis l'origine vers le sommet */
     protected double realized_cost;

     /* Le sommet précédent sur le chemin correspondant au plus court chemin courant */
     private Node father_node;

     /* Vrai si le sommet est dans le tas */
     private boolean in_tas;

    /* 
     * Les constructeurs
     */
    public Label(Node current_node) {
        this.current_node = current_node;
        this.marked = false;
        this.realized_cost = Float.POSITIVE_INFINITY;
        this.father_node = null;
        this.in_tas = false;
    } 

    /*
     * Les getters
     */

    public Node get_current_node() {
        return this.current_node;
    } 

    public boolean get_marked() {
        return this.marked;
    }

    public double get_realized_cost() {
        return this.realized_cost;
    }

    public Node get_father_node() {
        return this.father_node;
    }

    public boolean get_in_tas() {
        return this.in_tas;
    } 

    /* Renvoyer le cout de ce label(Pour le moment cost = realized_cost) */
    public double get_cost() {
        return this.realized_cost;
    }

    /*
     * Les setteurs
     */

    public void set_marked() {
        this.marked = true;
    }

    public void set_realized_cost(double realized_cost) {
        this.realized_cost = realized_cost;
    }

    public void set_father_node(Node father_node) {
        this.father_node = father_node;
    }

    public void set_in_tas() {
        this.in_tas = true;
    }

    /* Comparer 2 labels en fonction de leur cout total */
    public int compareTo(Label autre){

        int resultat;

        if (this.get_cost() > autre.get_cost()) {
            resultat = 1;
        } 
        else if (this.get_cost() < autre.get_cost()) {
            resultat = -1;
        }
        else {
            resultat = 0;
        } 

        return resultat;
    } 

}
