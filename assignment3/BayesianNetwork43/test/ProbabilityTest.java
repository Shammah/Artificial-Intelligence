import bayesiannetwork43.Probability;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Group 43
 */
public class ProbabilityTest {
    
    public ProbabilityTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void rangeTest() {
        try {
            Probability p = new Probability(-3);
            Assert.fail("-3 is not in [0, 1].");
        } catch(IllegalArgumentException ex) {
            
        }
        
        try {
            Probability p = new Probability(3.14);
            Assert.fail("3.14 is not in [0, 1].");
        } catch(IllegalArgumentException ex) {
            
        }
        
        try {
            Probability p = new Probability(0);
            Assert.assertEquals(0, p.getValue(), 0.000001);
        } catch(IllegalArgumentException ex) {
            Assert.fail("0 is in the range [0, 1].");
        }
        
        try {
            Probability p = new Probability(1);
            Assert.assertEquals(1, p.getValue(), 0.000001);
        } catch(IllegalArgumentException ex) {
            Assert.fail("1 is in the range [0, 1].");
        }
        
        try {
            Probability p = new Probability(1.0 / Math.PI);
            Assert.assertEquals(1.0 / Math.PI, p.getValue(), 0.000001);
        } catch(IllegalArgumentException ex) {
            Assert.fail(1.0 / Math.PI + " is in the range [0, 1].");
        }
    }
    
    @Test
    public void complementTest() {
        Probability p = new Probability(0.75);
        Assert.assertEquals(0.25, p.complement().getValue(), 0.00001);
    }
    
    @Test
    public void addTest() {
        Probability p1 = new Probability(0.75);
        Probability p2 = new Probability(0.1);
        Assert.assertEquals(0.85, p1.add(p2).getValue(), 0.000001);
        
        Probability p3 = new Probability(0.26);
        try {
            p1.add(p3);
            Assert.fail("A probability of 1.01 should not be possible.");
        } catch(IllegalArgumentException ex) {
            
        }
    }
}
