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
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_TriangleDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_VectorDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.light.V2D_VDouble;

/**
 * For visualising a triangle.
 *
 * @author Andy Turner
 */
public class TriangleDouble extends Entity {

    /**
     * The triangle geometry
     */
    public V2D_TriangleDouble triangle;
    
    /**
     * Create a new instance.
     *
     * @param triangle What {@link #triangle} is set to.
     * @param normal What {@link #normal} is set to.
     * @param attribute What {@link #attribute} is set to.
     */
    public TriangleDouble(V2D_TriangleDouble triangle, Data_ID_long id) {
        super(id);
        this.triangle = triangle;
    }
}
