package bayesiannetwork43;

import bayesiannetwork43.ProbabilityTable.Row;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
 * This class represents our Bayesian network.
 * 
 * @author Group 43
 */
public class BayesianNetwork43 {

    /** The tables in the bayesian network. */
    public List<ProbabilityTable> _tables;

    /** The query variable. */
    private String queryVar;

    /** {@code Map} containing the fixed variables and their values. */
    private final Map<String, String> givenValues = new HashMap<>();

    /** The file which we use to create this bayesian network.
     * "spiegelhalter.txt" gives the BirthAsphyxia network, while
     * "alarm.txt" gives the burglary network.
     */
    private final static String FILE = "spiegelhalter.txt";

    /**
     * Main method. Initializes the Bayesian network, reads the query, computes
     * the result and writes the output to the screen.
     *
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
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        bn.readQuery(input);
        ProbabilityTable output = bn.eliminate();
        bn.writeOutput(output);
    }

    /**
     * Reads the query from {@code System.in}. For testing purposes, an input
     * via {@code String} is also possible. If this {@code String} is {@code null},
     * input from {@code System.in} is used.
     *
     * @param input possible input via {@code String}
     * @modifies {@code this.queryVar, this.givenValues}
     * @post the query has been read properly
     */
    public void readQuery(String input) {
        Scanner scanner;
        if (input != null) {
            //test case
            scanner = new Scanner(input);
        } else {
            scanner = new Scanner(System.in);
        }
        scanner.next(); //ignore P(
        queryVar = scanner.next(); //store query variable
        scanner.next(); //ignore |
        boolean stop = false; //determines whether we need to stop
        while (! stop) {
            String variable = scanner.next(); //the next variable and value pair
            if (! (variable.equals(")"))) { //end of query
                variable = variable.replaceFirst("=", " "); //replaceFirst since there are data containing e.g. >= 5.0
                String[] parts = variable.split("\\s+"); // split on space
                variable = parts[0];
                String value = parts[1].replaceAll(",", "");
                givenValues.put(variable, value);
            } else {
                stop = true;
            }
        }
    }

    /**
     * Executes the variable elimination algorithm.
     *
     * @return A {@code ProbabilityTable} containing n rows if {@code queryVar}
     * is n-valued, where the probability is the probability asked for in the query.
     */
    public ProbabilityTable eliminate() {
        ProbabilityTable result; //the resulting {@code ProbabilityTable}
        Set<String>  relevantVariables      = findRelevantVariables();
        List<String> topologicalVariables   = topologicalSort();

        // we loop over the variables in (reversed) topological sorted order.
        Iterator<String> iter               = topologicalVariables.iterator();
        String firstVar = iter.next(); //the first relevant factor in the sorted order
        while (! relevantVariables.contains(firstVar)) {
            firstVar = iter.next();
        }
        result                              = getTable(firstVar);

        // we apply a filter according to the query if the factor is a fixed variable
        List<Pair<String, String[]>> filter = new ArrayList<>();
        String filterValue                  = givenValues.get(result.getName());
        if (filterValue != null) {
            filter.add(new Pair<>(result.getName(), new String[] { filterValue }));
            result                              = result.filter(filter);
        }

        // loop over all relevant variables
        while (iter.hasNext()) {
            String variable = iter.next();
            if (relevantVariables.contains(variable)) { // we skip nonrelevant variables
                ProbabilityTable varTable = getTable(variable); //next factor
                // Are we fixed or free?
                if (givenValues.containsKey(variable)) {
                    //fixed variable. we filter according to the query and multiply with the result
                    filter = new ArrayList<>();
                    filter.add(new Pair<>(variable, new String[]{givenValues.get(variable)}));
                    varTable = varTable.filter(filter);
                    result = result.multiply(varTable);
                } else if (!variable.equals(queryVar)) {
                    // free variable. We multiply and marginalize. (eliminate the variable)
                    result = result.multiply(varTable).marginalize(variable);
                } else {
                    // query variable. multiply the full table withour eliminating.
                    result = result.multiply(varTable);
                }
            }
        }
        //normalize the result such that the probabilities sum up to one.
        return result.normalize();
    }

    /**
     * Returns a reversed topological sort over the complete bayesian network.
     *
     * @return a {@code List} containing all variables such that a parent of a
     * vertex is always found further in the {@code List}
     */
    public List<String> topologicalSort() {
        List<String> result = new ArrayList<>();
        Set<String> variables = new HashSet<>(getVariables());
        Set<String> leaves;

        while (! variables.isEmpty()) {
            leaves = findLeaves(variables); //find leaves only on these variables
            Set<String> toRemove = new HashSet<>(); //leaves to be removed
            for (String node : variables) {
                if (leaves.contains(node)) { // we have a leaf
                    result.add(node);
                    toRemove.add(node);
                }
            }
            variables.removeAll(toRemove); //remove all leaves from the network
            toRemove.clear();
        }
        return result;
    }

    /**
     * Finds all leaves in the Bayesian network containing only the vertexes
     * corresponding to {@code variables}.
     *
     * @param variables the variables that we want to consider in the network
     * @pre {@code variables != null}
     * @return a {@code Set} containing the leaves in the network, excluding any
     * vertexes not in {@code variables}
     */
    public Set<String> findLeaves(Set<String> variables) {
        variables = new HashSet(variables); // needed so we do not modify the actual variables
        Set<String> parents = new HashSet(); //set containing all variables that are parents

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
     * Returns a {@code List} containing all tables that were imported.
     *
     * @return a {@code List} containing all tables that were imported
     */
    public List<ProbabilityTable> getTables() {
        return _tables;
    }

    /**
     * Returns the {@code ProbabilityTable} which corresponds to variable
     * {@code header}. Uses simple linear search.
     *
     * @param header The name of the {@code ProbabilityTable}
     * @pre {@code header != null}
     * @return The {@code ProbabilityTable} corresponding to {@code header} if
     * {@code header} is a variable in the Bayesian network, {@code null} otherwise.
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
     * Returns a {@code List} containing all variables in this Bayesian network.
     * In other words, all headers in the data set.
     * @return a {@code List} containing all variables in this Bayesian network.
     */
    public List<String> getVariables() {
        List<String> variables = new ArrayList<>();

        for (ProbabilityTable table : _tables) {
            variables.add(table.getName());
        }

        return variables;
    }

    /**
     * Finds the relevant variables in this Bayesian network according to the
     * given query. A variable is irrelevant if it does not influence the result
     * of the query. Since only parents and ancestors influence a particular
     * variable, we find the variables that are ancestors of the variables given
     * in the query.
     *
     * @return a {@code Set} containing the relevant variables in this Bayesian
     * network according to the given query.
     */
    public Set<String> findRelevantVariables() {
        Set<String> relevantVars = new HashSet<>();
        relevantVars.add(queryVar); //the query variable is relevant

        // Add all variables from the given set.
        for (String variable : givenValues.keySet()) {
            relevantVars.add(variable); // the given variables are relevant
        }

        //{@code List} representing the vertexes on the current level at which we are looking
        List<String> currentNodes = new ArrayList<>(relevantVars);
        //{@code List} containing all vertexes that we will be looking at during the next iteration
        List<String> futureNodes = new ArrayList<>();
        while (! currentNodes.isEmpty()) {
            for (String currentNode : currentNodes) {
                List<String> parents = new ArrayList(getTable(currentNode).getParents()); //find the parents
                while (! parents.isEmpty()) {
                    List<String> toRemove= new ArrayList<>();
                    for (String parent : parents) {
                        //the parent is relevant, we will look at it in the next round
                        //and remove it since we added it to the relevant variables
                        relevantVars.add(parent);
                        futureNodes.add(parent);
                        toRemove.add(parent);
                    }
                    parents.removeAll(toRemove);

                }
            }
            currentNodes.clear();
            List<String> toRemove= new ArrayList<>();
            // go one level higher
            for (String node : futureNodes) {
                currentNodes.add(node);
                toRemove.add(node);
            }
            futureNodes.removeAll(toRemove);
        }
        return relevantVars;
    }

    /**
     * Writes the output of the query to the screen.
     *
     * @param probs {@code ProbabilityTable} containing n rows for an n-valued
     * query variable, with fixed values for the given variables and one of the
     * n values for the query variable
     * @pre {@code probs != null}
     * @post the result of the query is written to the screen
     */
    public void writeOutput(ProbabilityTable probs) {
        String output = "("; //start with an opening parenthesis
        //make sure the decimal seperator is a dot, not a comma.
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat format = new DecimalFormat("#.####", symbols);

        int columnIndex = probs.getColumnIndex(queryVar);
        for (Row row : probs.getRows()) {
            //add the probability to the result, together with the label
            output += row.first.get(columnIndex) + " = " + format.format(row.second.getValue()) + ", ";
        }
        //remove the last comma and space
        output = output.substring(0, output.length() - 2);
        output += ")"; //add the closing parenthesis
        System.out.println(output);
    }
}
