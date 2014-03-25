package bayesiannetwork43;

import java.util.ArrayList;
import java.util.List;
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
    private List<String>    _headers    = new ArrayList<>();
    private List<Row>       _rows       = new ArrayList<>();
    
    /**
     * A row is a tuple, whose first element is a list of conditions and whose
     * second element is the probability.
     */
    public static class Row extends Pair<List<String>, Double>{
        /**
         * Constructor.
         * @param first List of conditionals.
         * @param second  Probability.
         */
        public Row(List<String> first, Double second) {
            super(first, second);
        }
        
        /**
         * Constructor.
         */
        public Row() {
            super(null, null);
            first   = new ArrayList<>();
            second  = 0.0;
        }
    }
    
    /**
     * Constructor.
     */
    public ProbabilityTable() {
        
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
        return _rows;
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
        
        getRows().add(row);
    }
}
