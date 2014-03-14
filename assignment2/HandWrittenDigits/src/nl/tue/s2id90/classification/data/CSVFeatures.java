/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author huub
 * @param <T> type of the comma separated elements
 */
public class CSVFeatures<T> implements Features {
        List<T> a = new ArrayList<>();
        
        // add element d
        public void add(T d) {
            a.add(d);
        }
        
        // add all elements in d
        public void add(T ... d) {
            a.addAll(Arrays.asList(d));
        }
        
        @Override
        public T get(int i) {
            return a.get(i);
        }

        @Override
        public int getNumberOfAttributes() {
            return a.size();
        }

        @Override
        public boolean isContinuous(int i) {
            return true;
        }

        @Override
        public String getName(int i) {
            return Integer.toString(i);
        }     
        
        public String toString() {
            return a.toString();
        }
}