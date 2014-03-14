/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification;

import java.util.Map;
import nl.tue.s2id90.classification.data.Features;

/**
 * Interface for classes that implement a classifier. A classifier according to
 * this interface gives for a given feature vector v of type F as output 
 * a label of type L. 
 * @author huub
 * @param <F>  type of feature vector
 * @param <L>  label, the result of classification
 */
public interface Classifier<F extends Features, L> {
    /**
     * returns the classification label
     * @param v vector of features
     * @return classification label
     */
    L classify(F v);

    /** returns the fraction of incorrectly classified feature vectors.
     * @param testData
     * @return the fraction of the feature vectors in the testData that is wrongly
     *         classified by this classifier.
     */
    double errorRate(Map<F, L> testData);
    
    /** returns confusion matrix.
     * returns a map m such that m.get(a).get(b) is the number of
     * times target a has been recognized as b. This is conceptually equivalent
     * to a, so-called, confusion matrix.
     * @param testData dataset for which matrix is computed
     * @return confusion matrix.
     * @see <a href="http://en.wikipedia.org/wiki/Confusion_matrix">wikipedia entry on confusion matrix</a>
     */
    Map<L,Map<L,Integer>> getConfusionMatrix(Map<F,L> testData);
}
