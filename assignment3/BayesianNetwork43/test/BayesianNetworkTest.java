
import bayesiannetwork43.BayesianNetwork43;
import bayesiannetwork43.ProbabilityTable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Group 43
 * @since 1-apr-2014
 */
public class BayesianNetworkTest {

    protected List<ProbabilityTable> tables;
    
    protected BayesianNetwork43 instance;

    @Before
    public void readTables() throws IOException {
        tables = ProbabilityTable.readFile("alarm.txt");
        instance = new BayesianNetwork43();
        instance._tables = tables;
    }

    @Test
    public void testGetChildren() {
        System.out.println(instance.findLeaves(new HashSet<>(instance.getVariables())));
    }
    
        
    @Test
    public void testRelevantVariables() throws IOException {
        tables = ProbabilityTable.readFile("alarm.txt");
        instance.readQuery("P( Alarm | John=yes )");
        Set<String> relevant = instance.findRelevantVariables();
        Set<String> testSet = new HashSet<>(Arrays.asList(new String[]{"John", "Alarm", "Burglary", "Earthquake"}));
        assertTrue(relevant.containsAll(testSet));        
    }
    
    @Test
    public void testTopologicalSort() throws IOException {
        instance._tables = ProbabilityTable.readFile("spiegelhalter.txt");
        //instance.readQuery("P( Earthquake | Alarm=yes )");
        List<String> sorted = instance.topologicalSort();
        System.out.println("sorting: " + sorted);
    }
    
    @Test
    public void testEliminate() {
        instance.readQuery("P( Burglary | John=yes, Mary=yes )");
        System.out.println(instance.eliminate());
    }
}
