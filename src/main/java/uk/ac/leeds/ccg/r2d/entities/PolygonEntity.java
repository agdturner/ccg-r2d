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

/**
 * For representing a triangle entity.
 *
 * @author Andy Turner
 */
public class PolygonEntity extends Entity {

    private static final long serialVersionUID = 1L;
    
    /**
     * For the colour of the external edges.
     */
    Color colorExternalEdge;

    /**
     * For the colour of the internal edges.
     */
    Color colorInternalEdge;

    /**
     * Create a new instance.
     *
     * @param id What {@link #id} is set to.
     */
    public PolygonEntity(Data_ID_long id){
        super(id);
    }
    
    /**
     * Create a new instance.
     *
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param edgeColor What {@link #edgeColor} is set to.
     */
    public PolygonEntity(Data_ID_long id,
            Color color, Color colorEdge){
        super(id, color, colorEdge);
    }
    
    /**
     * Create a new instance.
     *
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param colorExternalEdge What {@link #colorExternalEdge} is set to.
     * @param colorInternalEdge What {@link #colorInternalEdge} is set to.
     */
    public PolygonEntity(Data_ID_long id,
            Color color, Color colorExternalEdge, Color colorInternalEdge) {
        super(id, color);
        this.colorExternalEdge = colorExternalEdge;
        this.colorInternalEdge = colorInternalEdge;
    }
    
    /**
     * @return Sets the colour of the entity including the edge.
     * @param color The colour that is set.
     */
    public void setColor(Color color) {
        this.color = color;
        colorExternalEdge = null;
        colorInternalEdge = null;
        this.colorEdge = color;
    }
    
    /**
     * @return The colour of the colorExternalEdge setting it first if it is null. 
     */
    public Color getColorExternalEdge() {
        if (colorExternalEdge == null) {
            colorExternalEdge = colorEdge;
        }
        return colorExternalEdge;
    }
    
    /**
     * @param color The colour to use in rendering the PQ Edge. 
     */
    public void setColorExternalEdge(Color color) {
        colorExternalEdge = color;
    }
    
    /**
     * @return The colour of the QR Edge setting it first if it is null. 
     */
    public Color getColorInternalEdge() {
        if (colorInternalEdge == null) {
            colorInternalEdge = colorEdge;
        }
        return colorInternalEdge;
    }
    
    /**
     * @param color What {@link #colorInternalEdge} is set to.
     */
    public void setColorInternalEdge(Color color) {
        colorInternalEdge = color;
    }
    
}
