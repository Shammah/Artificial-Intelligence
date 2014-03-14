/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.decisiontree;

/**
 *
 * @author huub
 */
public abstract class Information {
    /**
     * @param p probability distribution
     * @return the entropy of the probability distribution p. **/
    public static double entropy(double ... p) {
        throw new UnsupportedOperationException("Should be implemented.");
    }
    
    /**
     * @param x argument
     * @return  2log(x)
     **/
    public static double log2(double x) {
        throw new UnsupportedOperationException("Should be implemented.");
    }
    
    /**
     * @param x argument
     * @return  2log(x)
     **/
    public static double xlog2x(double x) {
        throw new UnsupportedOperationException("Should be implemented.");
    }
}
