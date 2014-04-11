package nl.tue.s2id90.classification.decisiontree;

/**
 *
 * @author Group43
 */
public abstract class Information {
    /**
     * @param p probability distribution
     * @pre \sum p = 1
     * @return the entropy of the probability distribution p.
    **/
    public static double entropy(double ... p) {
        double result = 0;
        
        for (int i = 0; i < p.length; i ++) {
            result += xlog2x(p[i]);
        }
        
        return -1 * result;
    }

    /**
     * Returns the base 2 logarithm of {@code x}. log2 x is defined as 0.
     * @param x argument
     * @pre x < 0
     * @throws IllegalArgumentException if x < 0
     * @return  2log(x)
     **/
    public static double log2(double x) {
        if (x < 0) {
            throw new IllegalArgumentException("Information.log2.pre violated:"
                    + "x < 0");
        }
        else if (x == 0) {
            return 0;
        } else { // x > 0
            return Math.log10(x)/ Math.log10(2);
        }
    }

    /**
     * @param x argument
     * @return  x 2log(x)
     **/
    public static double xlog2x(double x) {
        return x * log2(x);
    }
}
