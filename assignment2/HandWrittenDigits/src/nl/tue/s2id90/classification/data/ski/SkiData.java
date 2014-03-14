/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.data.ski;

import java.util.HashMap;
import java.util.Map;
import nl.tue.s2id90.classification.data.Features;
import static nl.tue.s2id90.classification.data.ski.SkiData.SKIING.*;
import static nl.tue.s2id90.classification.data.ski.SkiData.SnowDist.*;
import static nl.tue.s2id90.classification.data.ski.SkiData.Wheater.*;
import static nl.tue.s2id90.classification.data.ski.SkiData.Day.*;

/**
 * Data set taken from Chapter 8 of Ertel "Introduction to AI"
 * @author huub
 */
public class SkiData {
    public enum SnowDist { CLOSE,  FAR };       // distance to ski area with snow
    public enum Wheater  { SUNNY,  NOT_SUNNY}; 
    public enum Day      {WEEKEND, WEEKDAY} 
    public enum SKIING   { GO,     STAY};          // decision: go skiing of stay home
    
    // {Day, Snow_Dist<=100, isWeekend, isSunny}, {goSkiing}
    Object[][][] data = {
        {{CLOSE, WEEKEND,     SUNNY}, { GO}},
        {{CLOSE, WEEKEND,     SUNNY}, { GO}},
        {{CLOSE, WEEKEND, NOT_SUNNY}, { GO}},
        {{CLOSE, WEEKDAY,     SUNNY}, { GO}},
        {{  FAR, WEEKEND,     SUNNY}, { GO}},
        {{  FAR, WEEKEND,     SUNNY}, { GO}},
        {{  FAR, WEEKEND,     SUNNY}, {STAY}},
        {{  FAR, WEEKEND, NOT_SUNNY}, {STAY}},
        {{  FAR, WEEKDAY,     SUNNY}, {STAY}},
        {{  FAR, WEEKDAY,     SUNNY}, {STAY}},
        {{  FAR, WEEKDAY, NOT_SUNNY}, {STAY}}
    };
    
    public Map<Features,SkiData.SKIING> getClassification() {    
        Map<Features,SkiData.SKIING> map = new HashMap<>();
        for(Object[][] o : data) {
            Object[] f = o[0]; // features
            SkiFeatures ff = new SkiFeatures(f);
            map.put(ff,(SkiData.SKIING)o[1][0]);
        }
        return map;
    }
}