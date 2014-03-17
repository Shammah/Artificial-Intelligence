package nl.tue.s2id90.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.classification.data.digits.DigitsUtil;
import nl.tue.s2id90.classification.data.digits.HandWrittenDigits;
import nl.tue.s2id90.classification.data.digits.LabeledImage;
import nl.tue.s2id90.classification.data.digits.features.Doubles;
import nl.tue.s2id90.classification.data.digits.features.ImageFeatures;
import nl.tue.s2id90.classification.knn.KNN43;
import nl.tue.s2id90.classification.knn.KNNDigits;

/**
 *
 * @author Roy Stoof (0767157)
 */
public class Main {
    public static void main(String[] a) {
        try {
            // Load test and training data.
            List<LabeledImage> trainingImages, testImages;
            trainingImages = HandWrittenDigits.getTrainingData(15, true);
            testImages     = HandWrittenDigits.getTestData();
            
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
            
            // Runs the neirest neighbour algorithm and show the confusion matrix.
            KNN43 knn = new KNNDigits(trainingDataset, 2);
            new ConfusionMatrixPanel(null, knn.getConfusionMatrix(testDataset)).showIt();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}