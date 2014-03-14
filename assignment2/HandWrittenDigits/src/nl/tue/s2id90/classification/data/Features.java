/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.data;

/**
 * An interface representing a vector of features.
 * @author huub
 */
public interface Features {
    /** returns value of the i-th feature.
     * @param i index of a coordinate
     * @return value of attribute i
     **/
    Object get(int i); 
    
    /** returns the number of features.
     * @return the number of attributes
     **/
    int getNumberOfAttributes();
    
    /**
     * returns whether or not the i-th feature is continuous.
     * @param i dimension
     * @return whether or not dimension i is continuous  **/
    boolean isContinuous(int i);
    
    /**
     * returns the name of the i-th feature.
     * @param i dimension
     * @return name of i-th feature
     */
    String getName(int i);
}
