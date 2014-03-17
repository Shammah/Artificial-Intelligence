package decisiontreetest;

import static junit.framework.Assert.assertEquals;
import nl.tue.s2id90.classification.decisiontree.Information;
import org.junit.Test;

/**
 *
 * @author Group43
 * @since 16-mrt-2014
 */
public class InformationTest {

    @Test
    public void testlog2() {
        assertEquals(3.0, Information.log2(8));
        assertEquals(4.0, Information.log2(16));
    }

    @Test
    public void testxlog2x() {
        assertEquals(24.0, Information.xlog2x(8));
        assertEquals(8.0, Information.xlog2x(4));
    }

    @Test
    public void testEntropy() {
        assertEquals(1.0, Information.entropy(0.5,0.5));
        assertEquals(-0.0, Information.entropy(1, 0, 0, 0));
        assertEquals(0.81, Math.round((Information.entropy(0.75,0.25)) * 100.0)/100.0);
    }
}
