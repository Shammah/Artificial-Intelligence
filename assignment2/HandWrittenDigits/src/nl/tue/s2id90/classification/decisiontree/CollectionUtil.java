/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.decisiontree;

import java.util.Collection;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author huub
 */
public class CollectionUtil {
    /** 
     * @see list argument list
     * @return the most occurring element in list; when more elements occur equally
     * often, the earliest occurring element is returned.
     **/
    public static <T> T mostOccurringElement(Collection<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }
        return max==null?null:max.getKey();
    }
}
