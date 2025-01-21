/*
 * Copyright 2022 Centre for Computational Geography.
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
package uk.ac.leeds.ccg.r2d.d.entities;

import java.awt.Color;
import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.r2d.entities.Entity;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegmentDouble;

/**
 * A class for rendering lines.
 * 
 * @author Andy Turner
 */
public class LineDouble extends Entity {
    
    /**
     * The line to render.
     */
    public V2D_LineSegmentDouble l;
    
    /**
     * @param l What {@link l} is set to. 
     */
    public LineDouble(V2D_LineSegmentDouble l, Data_ID_long id) {
        super(id);
        this.l = l;
    }
}
