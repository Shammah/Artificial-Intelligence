package bayesiannetwork43;

import bayesiannetwork43.ProbabilityTable.Row;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.win.util.Pair;

/**
 *
 * @author Group 43
 */
public class BayesianNetwork43 {

    public List<ProbabilityTable> _tables;
    private String queryVar;
    private Map<String, String> givenValues = new HashMap<>();

    private final static String FILE = "spiegelhalter.txt";//"spiegelhalter.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BayesianNetwork43 bn = null;
        try {
            bn = new BayesianNetwork43();
            bn._tables = ProbabilityTable.readFile(FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        }
        bn.readQuery(null);
        ProbabilityTable output = bn.eliminate();
        bn.writeOutput(output);
    }

    public void readQuery(String input) {
        Scanner scanner;
        if (input != null) {
            scanner = new Scanner(input);
        } else {
            scanner = new Scanner(System.in);
        }
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
                String value = parts[1].replaceAll(",", "");
                givenValues.put(variable, value);
            } else {
                stop = true;
            }
        }
    }

    public ProbabilityTable eliminate() {
        ProbabilityTable result;
        Set<String>  relevantVariables      = findRelevantVariables();
        List<String> topologicalVariables   = topologicalSort();

        Iterator<String> iter               = topologicalVariables.iterator();
        String firstVar = iter.next();
        while (true) {
            if (relevantVariables.contains(firstVar)) {
                result                      = getTable(firstVar);
                break;
            } else {
                firstVar = iter.next();
            }
        }

        List<Pair<String, String[]>> filter = new ArrayList<>();
        String filterValue                  = givenValues.get(result.getName());
        if (filterValue != null) {
            filter.add(new Pair<>(result.getName(), new String[] { filterValue }));
            result                              = result.filter(filter);
        }

        while (iter.hasNext()) {
            String variable = iter.next();
            if (!relevantVariables.contains(variable)) {
                continue;
            }

            // Are we fixed or free?
            ProbabilityTable varTable = getTable(variable);
            if (givenValues.containsKey(variable)) {
                filter = new ArrayList<>();
                filter.add(new Pair<>(variable, new String[] { givenValues.get(variable) }));
                varTable = varTable.filter(filter);
                result = result.multiply(varTable);
            } else if (!variable.equals(queryVar)){
                result = result.multiply(varTable).marginalize(variable);
            } else {
                result = result.multiply(varTable);
            }
        }

        return result.normalize();
    }

    public List<String> topologicalSort() {
        List<String> result = new ArrayList<>();
        Set<String> variables = new HashSet<>(getVariables());
        Set<String> leaves;

        while (! variables.isEmpty()) {
            leaves = findLeaves(variables);
            Set<String> toRemove = new HashSet<>();
            for (String node : variables) {
                if (leaves.contains(node)) {
                    result.add(node);
                    toRemove.add(node);
                }
            }
            variables.removeAll(toRemove);
            toRemove.clear();
        }
        return result;
    }

    /**
     * Finds all leaves in the Bayesian network.
     * @return a list of leaves.
     */
    public Set<String> findLeaves(Set<String> variables) {
        variables = new HashSet(variables);
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

    /**
     * Returns the list of all tables that were imported.
     * @return the list of all tables.
     */
    public List<ProbabilityTable> getTables() {
        return _tables;
    }

    /**
     * Returns a table by looking for its table name.
     * @param header The name of the table.
     * @return The table if present, null otherwise.
     */
    public ProbabilityTable getTable(String header) {
        ProbabilityTable result = null;
        for (ProbabilityTable table : _tables) {
            if (table.getName().equals(header)) {
                result = table;
            }
        }
        assert result != null;
        return result;
    }

    /**
     * Returns a list of all variables in the network.
     * @return a list of all variables in the network.
     */
    public List<String> getVariables() {
        List<String> variables = new ArrayList<>();

        for (ProbabilityTable table : _tables) {
            variables.add(table.getName());
        }

        return variables;
    }

    public Set<String> findRelevantVariables() {
        Set<String> relevantVars = new HashSet<>();
        relevantVars.add(queryVar);

        // Add all variables from the given set.
        for (String variable : givenValues.keySet()) {
            relevantVars.add(variable);
        }

        List<String> currentNodes = new ArrayList<>(relevantVars);
        List<String> futureNodes = new ArrayList<>();
        while (! currentNodes.isEmpty()) {
            for (String currentNode : currentNodes) {
                List<String> parents = new ArrayList(getTable(currentNode).getParents());
                while (! parents.isEmpty()) {
                    List<String> toRemove= new ArrayList<>();
                    for (String parent : parents) {
                        relevantVars.add(parent);
                        futureNodes.add(parent);
                        toRemove.add(parent);
                    }
                    parents.removeAll(toRemove);

                }
            }
            currentNodes.clear();
            List<String> toRemove= new ArrayList<>();
            for (String node : futureNodes) {
                currentNodes.add(node);
                toRemove.add(node);
            }
            futureNodes.removeAll(toRemove);
        }
        return relevantVars;
    }

    public void writeOutput(ProbabilityTable probs) {
        String output = "(";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.####", symbols);

        System.out.println(probs);
        for (Row row : probs.getRows()) {
            output += format.format(row.second.getValue()) + ", ";
        }
        output = output.substring(0, output.length() - 2);
        output += ")";
        System.out.println(output);
    }
}
