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

import java.awt.Color;
import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Polygon;

/**
 * For representing a polygon entity.
 *
 * @author Andy Turner
 */
public class Polygon extends PolygonEntity {

    private static final long serialVersionUID = 1L;

    /**
     * The polygon geometry
     */
    public V2D_Polygon polygon;
    
    /**
     * Create a new instance.
     *
     * @param polygon What {@link #polygon} is set to.
     * @param id What {@link #id} is set to.
     */
    public Polygon(V2D_Polygon polygon, Data_ID_long id){
        super(id);
        this.polygon = polygon;
    }
    
    /**
     * Create a new instance.
     *
     * @param polygon What {@link #polygon} is set to.
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param edgeColor What {@link #edgeColor} is set to.
     */
    public Polygon(V2D_Polygon polygon, Data_ID_long id,
            Color color, Color colorEdge){
        super(id, color, colorEdge);
        this.polygon = polygon;
    }
    
    /**
     * Create a new instance.
     *
     * @param polygon What {@link #polygon} is set to.
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param colorInternalEdge What {@link #colorInternalEdge} is set to.
     * @param colorExternalEdge What {@link #colorExternalEdge} is set to.
     */
    public Polygon(V2D_Polygon polygon, Data_ID_long id,
            Color color, Color colorInternalEdge, Color colorExternalEdge) {
        super(id, color, colorInternalEdge, colorExternalEdge);
        this.polygon = polygon;
    }
}
