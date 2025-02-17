/*
 * Copyright 2025 Andy Turner, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.r2d.grids;

import java.awt.Color;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.stats.range.Stats_RangeDouble;

/**
 * For colouring grids.
 * @author Andy Turner
 */
public class Colour_MapDouble {
    
    /**
     * The colour map.
     */
    public TreeMap<Stats_RangeDouble, Color> cm;
    
    /**
     * Create a new instance.
     */
    public Colour_MapDouble() {
        cm = new TreeMap<>();
    }
    
    /**
     * @param range The range.
     * @param color The colour.
     */
    public void addRange(Stats_RangeDouble range, Color color) {
        cm.put(range, color);
    }
    
    /**
     * For getting a colour.
     * @param value The value to get the colour.
     * @return The colour for the value. 
     */
    public Color getColour(double value){
        Iterator<Stats_RangeDouble> ite = cm.keySet().iterator();
        while (ite.hasNext()) {
            Stats_RangeDouble range = ite.next();
            if (range.contains(value)) {
                return cm.get(range);
            }
        }
        return Color.BLACK;
    }
    
}
