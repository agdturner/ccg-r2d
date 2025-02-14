/*
 * Copyright 2020 Andy Turner, University of Leeds.
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
package uk.ac.leeds.ccg.r2d;

import ch.obermuhlner.math.big.BigRational;
import java.math.RoundingMode;
import uk.ac.leeds.ccg.v2d.core.V2D_Environment;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Envelope;
import uk.ac.leeds.ccg.v2d.geometry.V2D_LineSegment;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Point;

/**
 *
 * @author Andy Turner
 */
public class Axes {

    V2D_Point xMin;
    V2D_Point xMax;
    V2D_Point yMin;
    V2D_Point yMax;
    public V2D_LineSegment yAxis;
    public V2D_LineSegment xAxis;

    /**
     * Create the axes.
     */
    public Axes(V2D_Environment env, V2D_Envelope e, int oom, RoundingMode rm) {
        // Create x axis
        xMin = new V2D_Point(env, e.getXMin(oom, rm), BigRational.ZERO);
        xMax = new V2D_Point(env, e.getXMax(oom, rm), BigRational.ZERO);
        xAxis = new V2D_LineSegment(xMin, xMax, oom, rm);
        // Create y axis
        yMin = new V2D_Point(env, BigRational.ZERO, e.getYMin(oom, rm));
        yMax = new V2D_Point(env, BigRational.ZERO, e.getYMax(oom, rm));
        yAxis = new V2D_LineSegment(yMin, yMax, oom, rm);
    }

}
