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
package uk.ac.leeds.ccg.r2d.d;

import java.awt.Color;
import java.util.ArrayList;
import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.r2d.d.entities.PolygonDouble;
import uk.ac.leeds.ccg.r2d.d.entities.PolygonNoInternalHolesDouble;
import uk.ac.leeds.ccg.r2d.d.entities.TriangleDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_EnvelopeDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonNoInternalHolesDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_TriangleDouble;

/**
 * A class that holds reference to visible and invisible objects.
 *
 * @author Andy Turner
 */
public class UniverseDouble {

    /**
     * Envelope
     */
    V2D_EnvelopeDouble envelope;

    /**
     * The triangles.
     */
    public ArrayList<TriangleDouble> triangles;
    
    /**
     * The polygons with no internal holes.
     */
    public ArrayList<PolygonNoInternalHolesDouble> pnih;
    
    /**
     * The polygons.
     */
    public ArrayList<PolygonDouble> polygons;
    
    /**
     * The grids to render.
     */
    public ArrayList<Grids_GridDouble> grids;
        
    /**
     * long
     */
    long nextID;

    /**
     * Create a new instance.
     *
     * @param envelope The initial envelope.
     */
    public UniverseDouble(V2D_EnvelopeDouble envelope) {
        nextID = 0L;
        triangles = new ArrayList<>();
        pnih = new ArrayList<>();
        polygons = new ArrayList<>();
        grids = new ArrayList<>();
        this.envelope = envelope;
    }

    /**
     * @return The next id. 
     */
    private Data_ID_long getNextID(){
        Data_ID_long id = new Data_ID_long(nextID);
        nextID ++;
        return id;
    }
    
    /**
     * Adds the triangle and returns entity.
     * @param triangle The triangle to add.
     * @return The TriangleDouble.
     */
    public TriangleDouble addTriangle(V2D_TriangleDouble triangle){
        TriangleDouble t = new TriangleDouble(triangle, getNextID());
        triangles.add(t);
        envelope = envelope.union(triangle.getEnvelope());
        return t;
    }
    
    /**
     * Adds the triangle and returns entity.
     * @param triangle The triangle to add.
     * @return The Triangle.
     * @param color The colour of the triangle.
     * @param colorEdge The colour of the edge of the triangle.
     */
    public TriangleDouble addTriangle(V2D_TriangleDouble triangle, Color color, Color colorEdge){
        TriangleDouble t = new TriangleDouble(triangle, getNextID(), color, colorEdge);
        triangles.add(t);
        envelope = envelope.union(triangle.getEnvelope());
        return t;
    }
    
    /**
     * Adds the triangle and returns entity.
     * @param triangle The triangle to add.
     * @return The Triangle.
     * @param color The colour of the triangle.
     * @param colorPQ The colour of the triangle PQ edge.
     * @param colorQR The colour of the triangle QR edge.
     * @param colorRP The colour of the triangle RP edge.
     */
    public TriangleDouble addTriangle(V2D_TriangleDouble triangle, Color color, 
            Color colorPQ, Color colorQR, Color colorRP){
        TriangleDouble t = new TriangleDouble(triangle, getNextID(), color, 
                colorPQ, colorQR, colorRP);
        triangles.add(t);
        envelope = envelope.union(triangle.getEnvelope());
        return t;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @return The Triangle.
     */
    public PolygonNoInternalHolesDouble addPolygonNoInternalHoles(
            V2D_PolygonNoInternalHolesDouble polygon){
        PolygonNoInternalHolesDouble p = new PolygonNoInternalHolesDouble(polygon, getNextID());
        pnih.add(p);
        envelope = envelope.union(polygon.getEnvelope());
        return p;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @return The TriangleDouble.
     */
    public PolygonDouble addPolygon(V2D_PolygonDouble polygon){
        PolygonDouble t = new PolygonDouble(polygon, getNextID());
        polygons.add(t);
        envelope = envelope.union(polygon.getEnvelope());
        return t;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @return The Polygon.
     * @param color The colour of the polygon.
     * @param colorEdge The colour of the edge of the polygon.
     */
    public PolygonDouble addPolygon(V2D_PolygonDouble polygon, Color color, Color colorEdge){
        PolygonDouble t = new PolygonDouble(polygon, getNextID(), color, colorEdge);
        polygons.add(t);
        envelope = envelope.union(polygon.getEnvelope());
        return t;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @return The Polygon.
     * @param color The colour of the polygon.
     * @param colorInternalEdge What {@link #colorInternalEdge} is set to.
     * @param colorExternalEdge What {@link #colorExternalEdge} is set to.
     */
    public PolygonDouble addPolygon(V2D_PolygonDouble polygon, Color color, 
            Color colorInternalEdge, Color colorExternalEdge){
        PolygonDouble t = new PolygonDouble(polygon, getNextID(), color, 
                colorInternalEdge, colorExternalEdge);
        polygons.add(t);
        envelope = envelope.union(polygon.getEnvelope());
        return t;
    }
    
    /**
     * Adds the grid.
     * @param grid The grid to add. 
     */
    public void addGrid(Grids_GridDouble grid) {
        grids.add(grid);
    }
}
