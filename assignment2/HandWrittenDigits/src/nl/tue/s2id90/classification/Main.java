package nl.tue.s2id90.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
    private static int TRAININGSIZE = 1000;

    /** Number of trees used in the random forest */
    private static int NRTREES = 5;
    
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] a) throws IOException {
        String strategy = "";
        String data     = "";
        
        System.out.println("------------------------");
        System.out.println("Welcome to assignment 3 of Group43 for Artificial "
                + "Intelligence.\nIn this menu, you will be given various options "
                + "on what you want to calculate. What would you like to do?");
        System.out.println("------------------------");
        System.out.println("1) k-Nearest Neighbour");
        System.out.println("2) Decision Tree");
        System.out.println("3) Random Forest");
        System.out.println("------------------------");
        
        // Ask for strategy.
        switch(input.nextInt()) {
            case 1:
                strategy = "knn";
                break;
            case 2:
                strategy = "decision";
                break;
            case 3:
                strategy = "random";
        }
        
        // Ask for data type.
        System.out.println("------------------------");
        System.out.println("Which dataset do you want to classify?");
        System.out.println("------------------------");
        System.out.println("1) Handwritten Digits");
        System.out.println("2) Golf");
        System.out.println("3) Ski");
        
        switch(input.nextInt()) {
            case 1:
                data = "digits";
                break;
            case 2:
                data = "golf";
                break;
            case 3:
                data = "ski";
        }
        
        // Ask for the size of the dataset if we use handwritten digits.
        if (data.equals("digits")) {
            System.out.println("------------------------");
            System.out.println("How many handwritten digits do you want in your training data?");
            System.out.println("------------------------");
            TRAININGSIZE = Math.max(0, Math.min(input.nextInt(), 15000));
        }
        
        // Ask for the number of trees.
        if (strategy.equals("random")) {
            System.out.println("------------------------");
            System.out.println("The number of random trees = ?");
            System.out.println("------------------------");
            NRTREES = Math.max(1, input.nextInt());
        }
        
        switch(data) {
            case "golf":
                golf(strategy);
                break;
            case "ski":
                ski(strategy);
                break;
            case "digits":
                digits(strategy);
        }
    }

    public static void golf(String strategy) {
        if (strategy.equals("decision")) {
            GolfData data = new GolfData();
            
            // Create dataset for confusion matrix.
            LabeledDataset2 dataSet = new LabeledDataset2();
            dataSet.putAll(data.getClassification());

            DecisionTree43<Features, GolfData.PLAY> tree = new DecisionTree43<>(dataSet);
            DotUtil.showDotInFrame(tree.toDot(), "Golf");
        } else {
            System.out.println("To be implemented");
        }
    }

    public static void ski(String strategy) {
        if (strategy.equals("decision")) {
            SkiData data = new SkiData();
            
            // Create dataset for confusion matrix.
            LabeledDataset2 dataSet = new LabeledDataset2();
            dataSet.putAll(data.getClassification());

            DecisionTree43<Features, SkiData.SKIING> tree = new DecisionTree43<>(dataSet);
            DotUtil.showDotInFrame(tree.toDot(), "Ski");
        } else {
            System.out.println("To be implemented");
        }
    }

    public static void digits(String strategy) throws IOException {
        readDigitData();
        
        switch (strategy) {
            case "decision":
                // Runs the decision tree algorithm and show the confusion matrix.
                DecisionTree43<ImageFeatures<Double>, Byte> tree;
                
                System.out.println("Building decision tree ...");
                tree = new DecisionTree43<>(trainingData);
                
                System.out.println("Error Rate: " + tree.errorRate(testDataset));
                System.out.println("Pruning ...");
                tree.prune(testData);
                
                System.out.println("Classifying ...");
                new ConfusionMatrixPanel(testData, tree.getConfusionMatrix(testDataset)).showIt();
                break;
                
            case "random":
                RandomForest<ImageFeatures<Double>, Byte> forest;
                
                System.out.println("Building random forest tree ...");
                forest = new RandomForest<>(trainingData, NRTREES);
                
                System.out.println("Classifying ...");
                new ConfusionMatrixPanel(testData, forest.getConfusionMatrix(testDataset)).showIt();
                break;
                
            default:
                // Runs the nearest neighbour algorithm and show the confusion matrix.
                System.out.println("------------------------");
                System.out.println("k = ?");
                System.out.println("------------------------");
                
                int k = Math.max(1, input.nextInt());
                
                KNN43 knn = new KNNDigits(trainingDataset, k);
                new ConfusionMatrixPanel(testData, knn.getConfusionMatrix(testDataset)).showIt();
                break;
        }
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
                trainingDataset.put(new Doubles(image), image.getLabel());
            }

            testDataset = new HashMap<>();
            for (LabeledImage image : testImages) {
                testDataset.put(new Doubles(image), image.getLabel());
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
