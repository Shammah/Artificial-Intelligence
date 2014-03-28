
import bayesiannetwork43.Probability;
import bayesiannetwork43.ProbabilityTable;
import bayesiannetwork43.ProbabilityTable.Row;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tue.win.util.Pair;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Group 43
 */
public class ProbabilityTableTest {
    
    protected List<ProbabilityTable> tables;
    
    public ProbabilityTableTest() {
        
    }
    
    @Before
    public void readTables() throws IOException {
        tables = ProbabilityTable.readFile("spiegelhalter.txt");
    }
    
    @Test
    public void headerTest() {
        List<String> t1 = new ArrayList<>();
        t1.add("BirthAsphyxia");
        
        List<String> t2 = new ArrayList<>();
        t2.add("HypDistrib");
        t2.add("DuctFlow");
        t2.add("CardiacMixing");
        
        List<String> t3 = new ArrayList<>();
        t3.add("LowerBodyO2");
        t3.add("HypDistrib");
        t3.add("HypoxiaInO2");
        assertEquals(t1, tables.get(0).getHeaders());
        assertEquals(t2, tables.get(1).getHeaders());
        assertEquals(t3, tables.get(7).getHeaders());        
    }
    
    @Test
    public void rowTest() {        
        List<String> r1Conditions = new ArrayList<>();
        r1Conditions.add("Normal");
        r1Conditions.add("Congested");
        r1Conditions.add("Normal");
        Row r1 = new Row(r1Conditions, new Probability(0.05));
        assertTrue(tables.get(4).getRows().contains(r1));
        
        tables.get(4).addRow(r1);
        r1.second = r1.second.add(r1.second);
        assertTrue(tables.get(4).getRows().contains(r1));
        
        List<String> r2Conditions = new ArrayList<>();
        r1Conditions.add("Normal");
        r1Conditions.add("Congested");
        Row r2 = new Row(r2Conditions, new Probability(0.05));
        
        try {
            tables.get(4).addRow(r2);
            fail();
        } catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void columnValueSetTest() {
        Set<String> expected = new HashSet<>();
        expected.add("PFC");
        expected.add("TGA");
        expected.add("Fallot");
        expected.add("PAIVS");
        expected.add("TAPVD");
        expected.add("Lung");
        
        assertEquals(expected, tables.get(13).getColumnValueSet("Disease"));
        
        try {
            tables.get(13).getColumnValueSet("Omnomnomnom");
            fail();
        } catch (IndexOutOfBoundsException ex) {
            
        }
    }
    
    @Test
    public void getNameTest() {
        assertEquals("HypDistrib", tables.get(1).getName());
        assertEquals("ChestXray", tables.get(4).getName());
        assertEquals("Grunting", tables.get(5).getName());
        assertEquals("RUQO2", tables.get(8).getName());
        assertEquals("Age", tables.get(13).getName());
        
        ProbabilityTable table = new ProbabilityTable();
        assertEquals("", table.getName());
    }
    
    @Test
    public void getColumnIndexTest() {
        assertEquals(0, tables.get(0).getColumnIndex("BirthAsphyxia"));
        assertEquals(2, tables.get(2).getColumnIndex("LungParench"));
        assertEquals(1, tables.get(15).getColumnIndex("Disease"));
        assertEquals(-1, tables.get(15).getColumnIndex("Omnomnomnom"));
    }
    
    @Test
    public void sumOfConditionsTest() {
        String[] bValues = new String[]{"Normal", "Congested"};
        Pair<String, String[]> B = new Pair<>("LungParench", bValues);
        
        String[] cValues = new String[]{"yes"};
        Pair<String, String[]> C = new Pair<>("Sick", cValues);
        
        List<Pair<String, String[]>> conditions = new ArrayList<>();
        conditions.add(B);
        conditions.add(C);
        
        assertEquals(2.0, tables.get(5).sumOfConditions(conditions), 0.000001);
    }
    
    @Test
    public void marginalizationTest() {
        ProbabilityTable marginalizedTable  = tables.get(9).marginalize("CO2Report");
        ProbabilityTable correctTable       = new ProbabilityTable();
        correctTable.getHeaders().add("CO2");
        
        List<String> row1Vars = new ArrayList<>();
        row1Vars.add("Normal");
        correctTable.addRow(new Row(row1Vars, new Probability(1.0)));
        
        List<String> row2Vars = new ArrayList<>();
        row2Vars.add("Low");
        correctTable.addRow(new Row(row2Vars, new Probability(1.0)));
        
        List<String> row3Vars = new ArrayList<>();
        row3Vars.add("High");
        correctTable.addRow(new Row(row3Vars, new Probability(1.0)));
        
        assertEquals(correctTable, marginalizedTable);
    }
}
