/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.knn;

import java.io.IOException;
import java.util.Map;
import nl.tue.s2id90.classification.Classifier;
import nl.tue.s2id90.classification.data.Features;

/**
 * K-nearest neighbor classifier.
 * <pre>
 *{@code 
 *            // read handwritten digits training data
 *            List<LabeledImage> trainingData = HandWrittenDigits.getTrainingData(15000, true);
 *            Map<ImageFeatures<Double>,Byte> trainingDataset = new HashMap<>();
 *            for(LabeledImage image : trainingData) {
 *                  trainingDataset.put(new Doubles(image),image.getLabel());
 *            }
 *            KNN knn = new KNN<ImageFeatures<Double>,Byte>(trainingData);
 *            ...
 *}
 * </pre>
 * @param <F> feature vector
 * @param <L> classification label
 * @see Classifier
 */
public abstract class KNN<F extends Features,L> implements Classifier<F,L>  {

    protected Map<F,L> trainingData;
    protected int k;                 // the number of nearest neighbours to consider

    public KNN(Map<F,L> trainingData, int k) throws IOException {
        this.trainingData = trainingData;
    }

    /**
     * computes the distance between two feature vectors. This distance is
     * used by the nearest neighbor algorithm.
     * <p>
     * A typical implementation extends this class to implement this method.
     * For instance, using inner classes :
     * <pre>
     *{@code 
     *             // create a KNN object for features <code>MyFeatures</code>
     *             // and Label type MyLabel.
     *             KNN classifier = new KNN<MyFeatures,MyLabel>(trainingDataset) {
     *                 // give an implementation of the distance function
     *                 public double distance(MyFeatures f0, MyFeatures f1) {
     *                     // compute the distances between f0 and f1
     *                     // using f0.get(i), and f1.get(i)
     *                 }
     *             };
     
     *}
     *</pre>
     * Or a separate class: <pre>
     *{@code 
     *             public class MyKNN extends KNN<MyFeatures,MyLabel> {
     *                  public MyKNN(Map<MyFeatures,MyLabel> trainingData) {
     *                       super(trainingData);
     *                  }
     *                 // give an implementation of the distance function
     *                 public double distance(MyFeatures f0, MyFeatures f1) {
     *                     // compute the distances between f0 and f1
     *                     // using f0.get(i), and f1.get(i)
     *                 }
     *             };     
     *}
     * </pre>
     * @param f0 
     * @param f1
     * @return distance between the two features
     **/
    abstract double distance(F f0, F f1);

    /**
     * 
     * @param v
     * @return 
     * @see Classifier#classify(Features) 
     */
    @Override
    abstract public L classify(F v);

    /**
     * @param testData
     * @see Classifier#errorRate
     **/
    @Override
    abstract public double errorRate(Map<F, L> testData);

    
    /**
     * @param testData
     * @see Classifier#getConfusionMatrix(Map) 
     */
    @Override
    abstract public Map<L, Map<L, Integer>> getConfusionMatrix(Map<F, L> testData);    
}