/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author huub
 */
public class CSV {

    private String[] fieldNames = null;

    public String[] getFieldNames() {
        return fieldNames;
    }

    public Map<CSVFeatures<Double>, String> getData(File f) throws FileNotFoundException {
        try (Scanner s = new Scanner(f)) {
            Map<CSVFeatures<Double>, String> result = new HashMap<>();

            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.startsWith("#")) {
                    continue; // skipping comments
                } else if (line.startsWith("!")) {
                    fieldNames = line.substring(1).split(",");
                } else {
                    String[] fields = line.split(",");
                    if (fields.length != fieldNames.length) {
                        System.err.println("skipping line :" + line);
                    } else {
                        CSVFeatures<Double> features = new CSVFeatures();
                        for (int i = 0; i < fields.length - 1; i++) {
                            features.add(Double.parseDouble(fields[i]));
                        }
                        result.put(features, fields[fields.length - 1]);
                    }
                }
            }
            return result;
        }
    }

}
