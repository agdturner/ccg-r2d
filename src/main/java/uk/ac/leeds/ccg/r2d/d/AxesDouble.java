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

import uk.ac.leeds.ccg.v2d.geometry.d.V2D_EnvelopeDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegmentDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;

/**
 *
 * @author Andy Turner
 */
public class AxesDouble {

    V2D_PointDouble xMin;
    V2D_PointDouble xMax;
    V2D_PointDouble yMin;
    V2D_PointDouble yMax;
    public V2D_LineSegmentDouble yAxis;
    public V2D_LineSegmentDouble xAxis;

    /**
     * Create the axes.
     */
    public AxesDouble(V2D_EnvelopeDouble e) {
        // Create x axis
        xMin = new V2D_PointDouble(e.getXMin(), 0d);
        xMax = new V2D_PointDouble(e.getXMax(), 0d);
        xAxis = new V2D_LineSegmentDouble(xMin, xMax);
        // Create y axis
        yMin = new V2D_PointDouble(0d, e.getYMin());
        yMax = new V2D_PointDouble(0d, e.getYMax());
        yAxis = new V2D_LineSegmentDouble(yMin, yMax);
    }

}
