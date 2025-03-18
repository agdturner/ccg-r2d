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
package uk.ac.leeds.ccg.r2d.d;

import uk.ac.leeds.ccg.v2d.core.d.V2D_Environment_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_AABB_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegment_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Point_d;

/**
 *
 * @author Andy Turner
 */
public class Axes_d {

    V2D_Point_d xMin;
    V2D_Point_d xMax;
    V2D_Point_d yMin;
    V2D_Point_d yMax;
    public V2D_LineSegment_d yAxis;
    public V2D_LineSegment_d xAxis;

    /**
     * Create the axes.
     */
    public Axes_d(V2D_Environment_d env, V2D_AABB_d e) {
        // Create x axis
        xMin = new V2D_Point_d(env, e.getXMin(), 0d);
        xMax = new V2D_Point_d(env, e.getXMax(), 0d);
        xAxis = new V2D_LineSegment_d(xMin, xMax);
        // Create y axis
        yMin = new V2D_Point_d(env, 0d, e.getYMin());
        yMax = new V2D_Point_d(env, 0d, e.getYMax());
        yAxis = new V2D_LineSegment_d(yMin, yMax);
    }

}
