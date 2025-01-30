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
import uk.ac.leeds.ccg.r2d.entities.Entity;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_TriangleDouble;

/**
 * For representing a triangle entity.
 *
 * @author Andy Turner
 */
public class TriangleEntity extends Entity {

    private static final long serialVersionUID = 1L;
    
    /**
     * For the colour of the PQ edge.
     */
    Color colorPQ;

    /**
     * For the colour of the QR edge.
     */
    Color colorQR;

    /**
     * For the colour of the RP edge.
     */
    Color colorRP;
    
    
    /**
     * Create a new instance.
     *
     * @param id What {@link #id} is set to.
     */
    public TriangleEntity(Data_ID_long id){
        super(id);
    }
    
    /**
     * Create a new instance.
     *
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param edgeColor What {@link #edgeColor} is set to.
     */
    public TriangleEntity(Data_ID_long id,
            Color color, Color colorEdge){
        super(id, color, colorEdge);
    }
    
    /**
     * Create a new instance.
     *
     * @param id What {@link #id} is set to.
     * @param color What {@link #color} is set to.
     * @param colorPQ What {@link #colorPQ} is set to.
     * @param colorQR What {@link #colorQR} is set to.
     * @param colorRP What {@link #colorRP} is set to.
     */
    public TriangleEntity(Data_ID_long id,
            Color color, Color colorPQ, Color colorQR, Color colorRP) {
        super(id, color);
        this.colorPQ = colorPQ;
        this.colorQR = colorQR;
        this.colorRP = colorRP;
    }
    
    
    
    /**
     * @return Sets the colour of the entity including the edge.
     * @param color The colour that is set.
     */
    public void setColor(Color color) {
        this.color = color;
        colorPQ = null;
        colorQR = null;
        colorRP = null;
        this.colorEdge = color;
    }
    
    /**
     * @return The colour of the PQ Edge setting it first if it is null. 
     */
    public Color getColorPQ() {
        if (colorPQ == null) {
            colorPQ = colorEdge;
        }
        return colorPQ;
    }
    
    /**
     * @param color The colour to use in rendering the PQ Edge. 
     */
    public void setColorPQ(Color color) {
        colorPQ = color;
    }
    
    /**
     * @return The colour of the QR Edge setting it first if it is null. 
     */
    public Color getColorQR() {
        if (colorQR == null) {
            colorQR = colorEdge;
        }
        return colorQR;
    }
    
    /**
     * @param color The colour to use in rendering the QR Edge. 
     */
    public void setColorQR(Color color) {
        colorQR = color;
    }
    
    /**
     * @return The colour of the RP Edge setting it first if it is null. 
     */
    public Color getColorRP() {
        if (colorRP == null) {
            colorRP = colorEdge;
        }
        return colorRP;
    }
    
    /**
     * @param color The colour to use in rendering the RP Edge. 
     */
    public void setColorRP(Color color) {
        colorRP = color;
    }
}
