package bayesiannetwork43;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Group 43
 */
public class BayesianNetwork43 {

    private static List<ProbabilityTable> _tables;

    private static String queryVar;

    private static Map<String, String> givenValues = new HashMap<>();

    private final static String FILE = "alarm.txt";//"spiegelhalter.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            _tables = ProbabilityTable.readFile(FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        }
        readQuery();
        
    }

    private static void readQuery() {
        Scanner scanner = new Scanner(System.in);
        scanner.next(); //ignore P(
        queryVar = scanner.next(); //store query variable
        scanner.next(); //ignore |
        boolean stop = false;
        while (!stop) {
            String variable = scanner.next();
            if (! (variable.equals(")"))) { //end of query
                variable = variable.replace('=',' ');
                String[] parts = variable.split("\\s+"); // split on space
                variable = parts[0];
                String value = parts[1];
                givenValues.put(variable, value);
            } else {
                stop = true;
            }
        }
    }

    /**
     * Returns the list of all tables that were imported.
     * @return the list of all tables.
     */
    public static List<ProbabilityTable> getTables() {
        return _tables;
    }
}
