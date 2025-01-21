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
package uk.ac.leeds.ccg.r2d.entities;

import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Triangle;

/**
 * For visualising a triangle.
 *
 * @author Andy Turner
 */
public class Triangle extends Entity {

    /**
     * The triangle geometry
     */
    public V2D_Triangle triangle;

    /**
     * Create a new instance.
     *
     * @param triangle What {@link #triangle} is set to.
     * @param id What {@link #p} is set to.
     */
    public Triangle(V2D_Triangle triangle, Data_ID_long id) {
        super(id);
        this.triangle = triangle;
    }
}
