
import bayesiannetwork43.BayesianNetwork43;
import bayesiannetwork43.ProbabilityTable;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Group 43
 * @since 1-apr-2014
 */
public class BayesianNetworkTest {

    protected List<ProbabilityTable> tables;

    @Before
    public void readTables() throws IOException {
        tables = ProbabilityTable.readFile("alarm.txt");
        BayesianNetwork43._tables = tables;
    }

    @Test
    public void testGetChildren() {
        System.out.println(BayesianNetwork43.findLeaves());
    }
}
