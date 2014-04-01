package bayesiannetwork43;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Group 43
 */
public class BayesianNetwork43 {

    public static List<ProbabilityTable> _tables;

    private static String queryVar;

    private static Map<String, String> givenValues = new HashMap<>();

    private final static String FILE = "spiegelhalter.txt";//"spiegelhalter.txt";

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
        Probability[] output = enumerateAsk();
        writeOutput(output);
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

    public static Probability[] enumerateAsk() {
        System.out.println(queryVar);
        int n = getTable(queryVar).getColumnValueSet(queryVar).size();
        Map<String, Probability> output = new HashMap<>();
        for (String value : getTable(queryVar).getColumnValueSet(queryVar)) {
            output.put(value, enumerate(getVariables(), extendMap(givenValues, queryVar, value)));
        }
        return normalize(output);
    }

    public static Probability enumerate(List<String> variables, Map<String, String> givenValues) {
        if (variables.isEmpty()) {
            return new Probability(1.0);
        }
        String variable = variables.get(0);
        List<String> tailVars = variables.subList(1, variables.size());
        if (givenValues.keySet().contains(variable)) { //variable is fixed
            String value = givenValues.get(variable);
           // return getTable(variable).getParents() * (enumerate(tailVars, givenValues));
        } else { //variable is free
            //return enumerate(tailVars, extendMap(givenValues, variable, value));
        }
        return null;//TODO
    }

    public static ProbabilityTable findFormula() {
        ProbabilityTable result = new ProbabilityTable();
        Set<String> leaves = findLeaves();
        Iterator iter = leaves.iterator();
        ProbabilityTable factor = getTable((String) iter.next());
        while(iter.hasNext()) {
            String nextVariable = (String) iter.next();
            factor = factor.multiply(getTable(nextVariable));
        }
    }

    public static Set<String> findLeaves() {
        Set<String> variables = new HashSet(getVariables());
        Set<String> parents = new HashSet();
        for (String variable : variables) {
           for (ProbabilityTable table : _tables) {
               if (table.getName().equals(variable)) {
                   for (String parent : table.getParents()) {
                       parents.add(parent);
                   }
               }
           }
        }
        variables.removeAll(parents);
        return variables;
    }

    private static Map<String, String> extendMap(Map<String, String> map, String variable, String value) {
        Map<String, String> result = new HashMap<>(map);
        result.put(variable, value);
        return result;
    }

    public static Probability[] normalize(Map<String, Probability> map) {
        Probability[] result = new Probability[map.size()];
        double sum = 0;
        for (String variable : map.keySet()) {
            sum += map.get(variable).getValue();
        }
        int i = 0;
        for (String variable : map.keySet()) {
            result[i] = new Probability(map.get(variable).getValue() * 1.0/sum);
            i ++;
        }
        return result;
    }

    /**
     * Returns the list of all tables that were imported.
     * @return the list of all tables.
     */
    public static List<ProbabilityTable> getTables() {
        return _tables;
    }

    public static ProbabilityTable getTable(String header) {
        ProbabilityTable result = null;
        for (ProbabilityTable table : _tables) {
            if (table.getName().equals(header)) {
                result = table;
            }
        }
        assert result != null;
        return result;
    }

    public static List<String> getVariables() {
        List<String> variables = new ArrayList<>();
        for (ProbabilityTable table : _tables) {
            variables.add(table.getName());
        }
        return variables;
    }

    public static void writeOutput(Probability[] probs) {
        String output = "(";
        for (Probability prob : probs) {
            output += prob.toString();
        }
        output += ")";
        System.out.println(output);
    }
}
