package nl.tue.s2id90.classification.data.digits;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * This class has static functions to read a provided database with handwritten
 * digits. There are separate methods for reading the training data and the test
 * data.The results are stored in lists of LabeledImages.
 * <pre>
 *{@code
 *      // read the whole database with training data, and return a shuffled list of images
 *      List<LabeledImage> images = HandWrittenDigits.getTrainingData(60000,true);
 *
 *      // show 40 of the read images in a popup frame
 *      final int rows=20, cols=20;
 *      Util.showImages("test",images.subList(0, rows*cols),cols);
 *}
 * </pre>
 *
 * @see LabeledImage
 * @author huub
 */
public class HandWrittenDigits {

    final static private int IDX3 = 2051;
    final static private int IDX1 = 2049;

    /**
     * Return the first n labeled images. If shuffled==true, first shuffle the
     * data set and then return the first n values.
     *
     * @param n requested number of labeled values to return; if n==-1, return
     * all training data.
     * @param shuffled true if result needs to be shuffled
     * @return list of n labeled values from the training data
     * @throws java.io.IOException when database can not be opened or read.
     *
     */
    public static List<LabeledImage> getTrainingData(int n, boolean shuffled) throws IOException {
        HandWrittenDigits hwd = new HandWrittenDigits();
        String labelsFile = "data/train-labels-idx1-ubyte.gz";
        String imagesFile = "data/train-images-idx3-ubyte.gz";
        List<LabeledImage> list = Arrays.asList(read(imagesFile, labelsFile));
        if (shuffled) {
            Collections.shuffle(list);
        }
        return n >= 0 ? list.subList(0, Math.min(n, list.size())) : list;
    }

    /**
     * @return all the test data
     * @throws java.io.IOException
     *
     */
    public static List<LabeledImage> getTestData() throws IOException {
        HandWrittenDigits hwd = new HandWrittenDigits();
        String labelsFile = "data/t10k-labels-idx1-ubyte.gz";
        String imagesFile = "data/t10k-images-idx3-ubyte.gz";
        return Arrays.asList(read(imagesFile, labelsFile));
    }

    /**
     * combines a file with values (idx3) and a file with labels (idx1) in to an
     * array of LabeledImages.
     *
     * @param imagesFile idx3 file
     * @param labelsFile idx1 file
     * @throws java.io.IOException
     * @return array of labeled values based on the given files.
     */
    private static LabeledImage[] read(String imagesFile, String labelsFile) throws IOException {
        log("read labels");
        byte[] labels = readGzippedIDX1(labelsFile);

        log("read data");
        LabeledImage[] images = readGzippedIDX3(imagesFile);

        log("label images");
        for (int i = 0; i < labels.length; i = i + 1) {
            if (i % 10000 == 0) {
                System.err.println("(" + i + ")");
            }
            images[i].setLabel(labels[i]);
        }
        return images;
    }

    private static void log(Object s) {
        System.err.println(String.valueOf(s));
    }

    /**
     * @return values from a gzipped idx3 file. *
     */
    private static LabeledImage[] readGzippedIDX3(String file) throws IOException {
        try (BufferedInputStream is
                = new BufferedInputStream(new FileInputStream(file))) {
            return readIDX3(new GZIPInputStream(is));
        }
    }

    /**
     * @return labels from a gzipped idx1 file. *
     */
    private static byte[] readGzippedIDX1(String file) throws IOException {
        try (BufferedInputStream is
                = new BufferedInputStream(new FileInputStream(file))) {
            return readIDX1(new GZIPInputStream(is));
        }
    }

    /**
     * @return array of LabeledImages; note that these images are un-labeled.
     * @param is
     * @throws IOException
     */
    private static LabeledImage[] readIDX3(InputStream is) throws IOException {
        final int id = getInt(is);
        if (id != IDX3) {
            throw new IllegalArgumentException("not an IDX3 file");
        }
        final int n = getInt(is);
        final int noRows = getInt(is);
        final int noCols = getInt(is);
        LabeledImage[] images = new LabeledImage[n];

        log(Arrays.asList(new Integer[]{id, n, noRows, noCols}));

        for (int i = 0; i < n; i++) {
            if (i % 10000 == 0) {
                System.err.println("(" + i + ")");
            }
            images[i] = getImage(is, noRows, noCols);
        }
        return images;
    }

    /**
     * @return array of labels *
     */
    private static byte[] readIDX1(InputStream is) throws IOException {
        final int id = getInt(is);
        if (id != IDX1) {
            throw new IllegalArgumentException("not an IDX1 file");
        }

        final int noLabels = getInt(is);

        log(Arrays.asList(new Integer[]{id, noLabels}));

        return read(is, noLabels);
    }

    /**
     * @return next integer on the input stream is *
     */
    private static int getInt(InputStream is) throws IOException {
        return ByteBuffer.wrap(read(is, 4)).getInt();
    }

    /**
     * @param is input stream to read values from
     * @param rows height of values
     * @param cols width of values
     * @return next values in the input stream
     * @throws IOException
     */
    private static LabeledImage getImage(InputStream is, int rows, int cols) throws IOException {
        return new LabeledImage(cols, rows, read(is, cols * rows));
    }

    /**
     * reads noBytes from the input stream. *
     */
    private static byte[] read(InputStream is, int noBytes) throws IOException {
        byte[] h = new byte[noBytes];
        int read = 0;
        while (read < noBytes) {
            read += is.read(h, read, noBytes - read);
        }
        return h;
    }
}
