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
import uk.ac.leeds.ccg.r2d.entities.TriangleEntity;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Triangle_d;

/**
 * For representing a triangle entity.
 *
 * @author Andy Turner
 */
public class Triangle_t extends TriangleEntity {

    private static final long serialVersionUID = 1L;

    /**
     * The triangle geometry
     */
    public V2D_Triangle_d triangle;
    
    /**
     * Create a new instance.
     *
     * @param triangle What {@link #triangle} is set to.
     * @param id What {@link #id} is set to.
     */
    public Triangle_t(V2D_Triangle_d triangle, Data_ID_long id){
        super(id);
        this.triangle = triangle;
    }
    
    /**
     * Create a new instance.
     *
     * @param triangle What {@link #triangle} is set to.
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param edgeColor What {@link #edgeColor} is set to.
     */
    public Triangle_t(V2D_Triangle_d triangle, Data_ID_long id,
            Color color, Color colorEdge){
        super(id, color, colorEdge);
        this.triangle = triangle;
    }
    
    /**
     * Create a new instance.
     *
     * @param triangle What {@link #triangle} is set to.
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param colorPQ What {@link #colorPQ} is set to.
     * @param colorQR What {@link #colorQR} is set to.
     * @param colorRP What {@link #colorRP} is set to.
     */
    public Triangle_t(V2D_Triangle_d triangle, Data_ID_long id,
            Color color, Color colorPQ, Color colorQR, Color colorRP) {
        super(id, color, colorPQ, colorQR, colorRP);
        this.triangle = triangle;
    }
}
