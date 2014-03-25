package bayesiannetwork43;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Group 43
 */
public class BayesianNetwork43 {
    
    private static final List<ProbabilityTable> _tables = new ArrayList<>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            readDatabase("spiegelhalter.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BayesianNetwork43.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int i = 5;
    }

    private static void readDatabase(String fileName) throws FileNotFoundException, IOException {
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
            
            String[] columns = line.replaceAll("\\s+", " ").trim().split(" ");
            
            // Create a new table and set the headers
            if (newTable) {
                ProbabilityTable table = new ProbabilityTable();
                table.getHeaders().addAll(Arrays.asList(columns));
                getTables().add(table);
                
                // We are done creating a new table and continue to the next line.
                newTable = false;
                continue;
            }
            
            // At this point, we do not need to create a new table.
            // Instead, we will create a new row and add it to latest table.
            ProbabilityTable.Row row = new ProbabilityTable.Row();
            row.first   = Arrays.asList(Arrays.copyOfRange(columns, 0, columns.length - 1));
            row.second  = Double.parseDouble(columns[columns.length - 1]);
            getTables().get(getTables().size() - 1).addRow(row);
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
