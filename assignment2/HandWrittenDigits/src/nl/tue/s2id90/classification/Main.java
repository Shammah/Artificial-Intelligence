package nl.tue.s2id90.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.data.digits.DigitsUtil;
import nl.tue.s2id90.classification.data.digits.HandWrittenDigits;
import nl.tue.s2id90.classification.data.digits.LabeledImage;
import nl.tue.s2id90.classification.data.digits.features.Doubles;
import nl.tue.s2id90.classification.data.digits.features.Doubles43;
import nl.tue.s2id90.classification.data.digits.features.ImageFeatures;
import nl.tue.s2id90.classification.data.golf.GolfData;
import nl.tue.s2id90.classification.data.ski.SkiData;
import nl.tue.s2id90.classification.decisiontree.DecisionTree43;
import nl.tue.s2id90.classification.decisiontree.RandomForest;
import nl.tue.s2id90.classification.knn.KNN43;
import nl.tue.s2id90.classification.knn.KNNDigits;
import nl.tue.s2id90.classification.labeledtree.DotUtil;

/**
 *
 * @author Group 43
 */
public class Main {

    /** Maps for handwritten digits. */
    private static Map<ImageFeatures<Double>, Byte> trainingDataset, testDataset;

    /** Training and test data formatted as LabeledDataSet2 for handwritten digits */
    private static LabeledDataset2 testData, trainingData;

    /** Size of the training data for handwritten digits */
    private final static int TRAININGSIZE = 1000;

    /** Number of trees used in the random forest */
    private final static int NRTREES = 5;

    public static void main(String[] a) {
        treeDigits();
    }

    public static void golf() {
        GolfData data = new GolfData();
        // Create dataset for confusion matrix.
        LabeledDataset2 dataSet = new LabeledDataset2();
        dataSet.putAll(data.getClassification());

        DecisionTree43<Features, GolfData.PLAY> tree = new DecisionTree43<>(dataSet);
        DotUtil.showDotInFrame(tree.toDot(), "Golf");
    }

    public static void ski() {
        SkiData data = new SkiData();
        // Create dataset for confusion matrix.
        LabeledDataset2 dataSet = new LabeledDataset2();
        dataSet.putAll(data.getClassification());

        DecisionTree43<Features, SkiData.SKIING> tree = new DecisionTree43<>(dataSet);
        DotUtil.showDotInFrame(tree.toDot(), "Ski");
    }

    public static void treeDigits() {
        System.out.println("reading data");
        readDigitData();
        // Runs the decision tree algorithm and show the confusion matrix.
        DecisionTree43<ImageFeatures<Double>, Byte> tree;
        System.out.println("building tree");
        tree = new DecisionTree43<>(trainingData);
        System.out.println("prune " + tree.errorRate(testDataset));
        tree.prune(testData);
        System.out.println("classifying");
        new ConfusionMatrixPanel(testData, tree.getConfusionMatrix(testDataset)).showIt();
    }

    public static void randomForestDigits() {
        readDigitData();
        RandomForest<ImageFeatures<Double>, Byte> forest;
        System.out.println("building tree");
        forest = new RandomForest<>(trainingData, NRTREES);
        System.out.println("classifying");
        new ConfusionMatrixPanel(testData, forest.getConfusionMatrix(testDataset)).showIt();
    }

    public static void knnDigits() throws IOException {
        readDigitData();
        // Runs the nearest neighbour algorithm and show the confusion matrix.
        KNN43 knn = new KNNDigits(trainingDataset, 50);
        new ConfusionMatrixPanel(testData, knn.getConfusionMatrix(testDataset)).showIt();
    }

    private static void readDigitData() {
         try {
            // Load test and training data.
            List<LabeledImage> trainingImages, testImages;
            trainingImages = HandWrittenDigits.getTrainingData(TRAININGSIZE, true);
            testImages = HandWrittenDigits.getTestData();

            // Convert training data to a format that Classifier understands.
            trainingDataset = new HashMap<>();
            for (LabeledImage image : trainingImages) {
                trainingDataset.put(new Doubles43(image), image.getLabel());
            }

            testDataset = new HashMap<>();
            for (LabeledImage image : testImages) {
                testDataset.put(new Doubles43(image), image.getLabel());
            }

            // Create dataset for confusion matrix.
            testData = new LabeledDataset2();
            testData.putAll(testDataset);

            trainingData = new LabeledDataset2();
            trainingData.putAll(trainingDataset);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
