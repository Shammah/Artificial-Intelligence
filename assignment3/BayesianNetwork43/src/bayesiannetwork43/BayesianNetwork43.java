package bayesiannetwork43;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Group 43
 */
public class BayesianNetwork43 {
    
    private static List<ProbabilityTable> _tables;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            _tables = ProbabilityTable.readFile("spiegelhalter.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(getTables().get(1).getColumnValueSet(2));
    }
    
    /**
     * Returns the list of all tables that were imported.
     * @return the list of all tables.
     */
    public static List<ProbabilityTable> getTables() {
        return _tables;
    }
}
