/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.data.golf;


import java.util.HashMap;
import java.util.Map;
import nl.tue.s2id90.classification.data.Features;
import static nl.tue.s2id90.classification.data.golf.GolfData.PLAY.*;
import static nl.tue.s2id90.classification.data.golf.GolfData.Wheater.*;
import static nl.tue.s2id90.classification.data.golf.GolfData.WIND.*;

/**
 * Data set taken from Chapter 8 of Ertel "Introduction to AI"
 * @author huub
 */
public class GolfData {
    public enum Wheater  { SUNNY,  OVERCAST, RAINY}; 
    public enum WIND      {YES, NO};
    public enum PLAY   { PLAY,     DONTPLAY};          // decision: go skiing of stay home
    
    // {Day, Snow_Dist<=100, isWeekend, isSunny}, {goSkiing}
    Object[][][] data = {
        // outlook temperature  humidity windy   play
        {{SUNNY      , 85,  85 , NO  }, {DONTPLAY }},
        {{SUNNY      , 80,  90 , YES }, {DONTPLAY }},
        {{OVERCAST   , 83,  78 , NO  }, {PLAY     }},
        {{RAINY      , 70,  96 , NO  }, {PLAY     }},
        {{RAINY      , 68,  80 , NO  }, {PLAY     }},
        {{RAINY      , 65,  70 , YES }, {DONTPLAY }},
        {{OVERCAST   , 64,  65 , YES }, {PLAY     }},
        {{SUNNY      , 72,  95 , NO  }, {DONTPLAY }},
        {{SUNNY      , 69,  70 , NO  }, {PLAY     }},
        {{RAINY      , 75,  80 , NO  }, {PLAY     }},
        {{SUNNY      , 75,  70 , YES }, {PLAY     }},
        {{OVERCAST   , 72,  90 , YES }, {PLAY     }},
        {{OVERCAST   , 81,  75 , NO  }, {PLAY     }},
        {{RAINY      , 71,  80 , YES }, {DONTPLAY }}
    };
    
    public Map<Features,PLAY> getClassification() {    
        Map<Features,PLAY> map = new HashMap<>();
        for(Object[][] o : data) {
            Object[] f = o[0]; // features
            GolfFeatures ff = new GolfFeatures(f);
            map.put(ff,(PLAY)o[1][0]);
        }
        return map;
    }
}