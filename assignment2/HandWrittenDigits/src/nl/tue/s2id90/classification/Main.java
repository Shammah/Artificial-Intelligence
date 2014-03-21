package nl.tue.s2id90.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.data.digits.HandWrittenDigits;
import nl.tue.s2id90.classification.data.digits.LabeledImage;
import nl.tue.s2id90.classification.data.digits.features.Doubles;
import nl.tue.s2id90.classification.data.digits.features.ImageFeatures;
import nl.tue.s2id90.classification.data.golf.GolfData;
import nl.tue.s2id90.classification.data.ski.SkiData;
import nl.tue.s2id90.classification.decisiontree.DecisionTree43;
import nl.tue.s2id90.classification.knn.KNN43;
import nl.tue.s2id90.classification.knn.KNNDigits;
import nl.tue.s2id90.classification.labeledtree.DotUtil;

/**
 *
 * @author Roy Stoof (0767157)
 */
public class Main {

    public static void main(String[] a) {
        treeImages();
    }

    public static void golf() {
        GolfData data = new GolfData();
        // Create dataset for confusion matrix.
        LabeledDataset2 dataSet = new LabeledDataset2();
        dataSet.putAll(data.getClassification());

        DecisionTree43<Features, GolfData.PLAY> tree = new DecisionTree43<>(dataSet);
        //DotUtil.showDotInFrame(tree.toDot(), "Golf");
        DotUtil.showDotInBrowser(tree.toDot());
    }

    public static void ski() {
        SkiData data = new SkiData();
        // Create dataset for confusion matrix.
        LabeledDataset2 dataSet = new LabeledDataset2();
        dataSet.putAll(data.getClassification());

        DecisionTree43<Features, SkiData.SKIING> tree = new DecisionTree43<>(dataSet);
        DotUtil.showDotInFrame(tree.toDot(), "Ski");
    }

    public static void treeImages() {
        try {
            // Load test and training data.
            List<LabeledImage> trainingImages, testImages;
            trainingImages = HandWrittenDigits.getTrainingData(600, true);
            testImages = HandWrittenDigits.getTestData();

            // Show test data.
            //DigitsUtil.showImages("woei!", testImages.subList(0, 25), 5);
            // Convert training data to a format that Classifier understands.
            Map<ImageFeatures<Double>, Byte> trainingDataset, testDataset;
            trainingDataset = new HashMap<>();
            for (LabeledImage image : trainingImages) {
                trainingDataset.put(new Doubles(image), image.getLabel());
            }

            testDataset = new HashMap<>();
            for (LabeledImage image : testImages) {
                testDataset.put(new Doubles(image), image.getLabel());
            }

            // Create dataset for confusion matrix.
            LabeledDataset2 testData = new LabeledDataset2();
            testData.putAll(testDataset);
            LabeledDataset2 trainingData = new LabeledDataset2();
            trainingData.putAll(trainingDataset);

            // Runs the neirest neighbour algorithm and show the confusion matrix.
            DecisionTree43<ImageFeatures<Double>,Byte> tree;
            tree = new DecisionTree43<>(trainingData);
            new ConfusionMatrixPanel(testData, tree.getConfusionMatrix(testDataset)).showIt();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void knn() {
        try {
            // Load test and training data.
            List<LabeledImage> trainingImages, testImages;
            trainingImages = HandWrittenDigits.getTrainingData(600, true);
            testImages = HandWrittenDigits.getTestData();

            // Show test data.
            //DigitsUtil.showImages("woei!", testImages.subList(0, 25), 5);
            // Convert training data to a format that Classifier understands.
            Map<ImageFeatures<Double>, Byte> trainingDataset, testDataset;
            trainingDataset = new HashMap<>();
            for (LabeledImage image : trainingImages) {
                trainingDataset.put(new Doubles(image), image.getLabel());
            }

            testDataset = new HashMap<>();
            for (LabeledImage image : testImages) {
                testDataset.put(new Doubles(image), image.getLabel());
            }

            // Create dataset for confusion matrix.
            LabeledDataset2 testData = new LabeledDataset2();
            testData.putAll(testDataset);

            // Runs the neirest neighbour algorithm and show the confusion matrix.
            KNN43 knn = new KNNDigits(trainingDataset, 50);
            new ConfusionMatrixPanel(testData, knn.getConfusionMatrix(testDataset)).showIt();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
