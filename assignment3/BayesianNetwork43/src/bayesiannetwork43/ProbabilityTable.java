package bayesiannetwork43;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tue.win.util.Pair;

/**
 * A probability table relates stochastic variables with conditional probabilities.
 * 
 * A probability table consists of a list of headers, which define the name of
 * the columns, and a list of rows. A row is defined as a pair, where the first
 * element is a list of conditions, and the second element the probability.
 * 
 * For example, for the table:
 * ----------------------------------------
 * | header1 | header 2 | header 3|       |
 * | Dead    | Pulse    | Talks   |  0.5  |
 * | Alive   | Pulse    | Talks   |  0.3  |
 * ----------------------------------------
 * 
 * gives the headers: ["header1", "header2", "header3"]
 * and the rows:     (["Dead", "Pulse", "Talks"], 0.5)
 *                   (["Alive", "Pulse", "Talks"], 0.3)
 * 
 * @assert _rows.first.size() == _headers.size()
 * @author Group 43
 */
public class ProbabilityTable {
    private List<String>                    _headers;
    private Map<List<String>, Probability>  _rows;
    
    /**
     * A row is a tuple, whose first element is a list of conditions and whose
     * second element is the probability.
     */
    public static class Row extends Pair<List<String>, Probability> {

        /**
         * Constructor.
         * @param first List of conditionals.
         * @param second  Probability.
         */
        public Row(List<String> first, Probability second) {
            super(first, second);
        }
        
        /**
         * Constructor.
         */
        public Row() {
            super(null, null);
            first   = new ArrayList<>();
            second  = new Probability(0.0);
        }
        
        @Override
        public String toString() {
            String output = "([" + first.get(0);
            for (String conditional : first.subList(1, first.size())) {
                output += ", " + conditional;
            }
            
            output += "], " + second.getValue() + ")";
            return output;
        }
        
        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public boolean equals(Row row) {
            return first.equals(row.first) && second.equals(row.second); 
        }
    }
    
    /**
     * Constructor.
     */
    public ProbabilityTable() {
        _headers = new ArrayList<>();
        _rows    = new HashMap<>();
    }
    
    /**
     * Returns the list of headers for this table.
     * @return the list of headers.
     */
    public List<String> getHeaders() {
        return _headers;
    }
    
    /**
     * Returns the rows currently present in the table.
     * @return the rows of the table.
     */
    public List<Row> getRows() {
        List<Row> rows = new ArrayList<>();
        
        for (List<String> key : _rows.keySet()) {
            rows.add(new Row(key, _rows.get(key)));
        }
        
        return rows;
    }
    
    /**
     * Adds a new row to the probability table.
     * @param row The row to be added to the table.
     * @throws IllegalArgumentException The number of conditionals does not equal
     *                                  the number of headers.
     */
    public void addRow(Row row) throws IllegalArgumentException {
        if (row.first.size() != getHeaders().size()) {
            throw new IllegalArgumentException("The amount of conditions does not"
                    + "equal the amount of headers.");
        }
        
        // If the row does not yet exist, we simply add it.
        // Else, we add the given probability to the existing probability.
        if (!_rows.containsKey(row.first)) {
            _rows.put(row.first, row.second);
        } else {
            _rows.put(row.first, _rows.get(row.first).add(row.second));
        }
    }
    
    /**
     * Returns a unique set of values that a certain column of a table can take.
     * @param column The column to fetch the set of values from.
     * @return The set of all values in the column.
     */
    public Set<String> getColumnValueSet(int column) {
        HashSet<String> values = new HashSet<>();
        
        for (Row row : getRows()) {
            values.add(row.first.get(column));
        }
        
        return values;
    }
    
    /**
     * Returns a unique set of values that a certain column of a table can take.
     * @param header The name of the header.
     * @return The set of all values in the column.
     */
    public Set<String> getColumnValueSet(String header) {
        int headerIndex = getColumnIndex(header);
        
        if (headerIndex == -1) {
            throw new IndexOutOfBoundsException("Could not find the column index"
                    + "of the header '" + header + "'.");
        } else {
            return getColumnValueSet(headerIndex);
        }
    }
    
    /**
     * The name of a table is the name of the variable of what the probabilities are for.
     * 
     * More specifically, given the table:
     * ----------------------------------------
     * | header1 | header 2 | header 3|       |
     * | Dead    | Pulse    | Talks   |  0.5  |
     * | Alive   | Pulse    | Talks   |  0.3  |
     * ----------------------------------------
     * 
     * the name of the table will be: "header1".
     * 
     * @return the name of the table.
     */
    public String getName() {
        List<String> headers = getHeaders();
        if (headers.isEmpty()) {
            return "";
        } else {
            return headers.get(0);
        }
    }
    
    /**
     * Tries to find the column index of a given header in string-format.
     * @param header The header to find the column index for.
     * @return The column index of the header. -1 if no index was found.
     */
    public int getColumnIndex(String header) {
        List<String> headers = getHeaders();
        
        for (int i = 0; i < headers.size(); i++) {
            if (header.equals(headers.get(i))) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Reads a list of tables from a plain text file.
     * @param fileName The name of the file that contains the tables.
     * @return a list of tables.
     * @throws FileNotFoundException The file could not be found.
     * @throws IOException The file could somehow not be read.
     */
    public static List<ProbabilityTable> readFile(String fileName)
            throws FileNotFoundException, IOException {
        // All tables that will be scanned will be stored in here.
        List<ProbabilityTable> tables = new ArrayList<>();

        // Open up a file reader for scanning of the database.
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        
        // The line that we will process.
        String line;
        boolean newTable = true;
        
        // Read until there are no more lines to be read.
        while ((line = br.readLine()) != null) {
            // A new table might be ahead!
            if (line.equals("")) {
                newTable = true;
                continue;
            }
            
            // Remove all unnessecary whitespace.
            String[] columns = line.replaceAll("\\s+", " ").trim().split(" ");
            
            // Create a new table and set the headers
            if (newTable) {
                ProbabilityTable table = new ProbabilityTable();
                table.getHeaders().addAll(Arrays.asList(columns));
                tables.add(table);
                
                // We are done creating a new table and continue to the next line.
                newTable = false;
                continue;
            }
            
            // At this point, we do not need to create a new table.
            // Instead, we will create a new row and add it to latest table.
            // Everything but the last column are conditionals.
            // The last value of the columns array is the actual probability.
            Row row = new Row();
            row.first   = Arrays.asList(Arrays.copyOfRange(columns, 0, columns.length - 1));
            row.second  = new Probability(Double.parseDouble(columns[columns.length - 1]));
            tables.get(tables.size() - 1).addRow(row);
        }
        
        return tables;
    }
    
    /**
     * This calculates the sum of all probabilities that satisfy a certain set of conditions.
     * 
     * For example, we want the probability that:
     *     P(A | B = "foo" OR B = "bar" AND C = "Cloudy")
     * 
     * then, we let:
     *     Pair<String, String[]> B = new Pair<>("B", ["foo", "bar"]);
     *     Pair<String, String[]> C = new Pair<>("C", ["Cloudy"]);
     *     sumOfConditions(A + B);
     * 
     * where A is the 'name' of this table, where all these probabilities are for.
     * 
     * [<Header, [Possible Column Values]>]
     * @param conditions The list of condition pairs.
     * @return The sum of the probability.
     */
    public double sumOfConditions(List<Pair<String, String[]>> conditions) {
        // To make life easier, we will use header indices instead of strings.
        // We replace the header strings with indices.
        List<Pair<Integer, String[]>> iConditions = new ArrayList<>(conditions.size());
        for (Pair<String, String[]> condition : conditions) {
            int index = getColumnIndex(condition.first);
            if (index == -1) {
                throw new IndexOutOfBoundsException("Could not find the column index"
                        + "of the header '" + condition.first + "'.");
            }
            Pair<Integer, String[]> newPair = new Pair<>(index, condition.second);
            iConditions.add(newPair);
        }
        
        // Now that we have the header indices, we can iterate through all
        // the rows and see if they match the given conditions.
        //
        // So, for each row
        double sum = 0.0;
        for (Row row : getRows()) {
            boolean meetsAllConditions = true;
            
            // We check each condition. This will be basically an 'and'-clause.
            // Each condition can be equal to multiple values though.
            // Think of P(A | B = "foo" OR B = "bar")
            for (Pair<Integer, String[]> condition : iConditions) {
                // Thus, a condition can be composed out of multiple 'or'-clauses.
                boolean containsValue = false;
                
                for (String value : condition.second) {
                    // And that 'or'-clause is true if at least one value matches.
                    if (value.equals(row.first.get(condition.first))) {
                        containsValue = true;
                        break;
                    }
                }
                
                // The row meets only all conditions if for all conditions at least
                // one of the condition values equals the value.
                meetsAllConditions &= containsValue;
            }
            
            // If all conditions are met on that row, we can use it in our sum.
            if (meetsAllConditions) {
                sum += row.second.getValue();
            }
        }
        
        return sum;
    }
}