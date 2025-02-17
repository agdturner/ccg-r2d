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
package uk.ac.leeds.ccg.r2d;

import java.awt.Color;
import java.math.RoundingMode;
import java.util.ArrayList;
import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.r2d.entities.Polygon;
import uk.ac.leeds.ccg.r2d.entities.PolygonNoInternalHoles;
import uk.ac.leeds.ccg.r2d.entities.Triangle;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Envelope;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Polygon;
import uk.ac.leeds.ccg.v2d.geometry.V2D_PolygonNoInternalHoles;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Triangle;

/**
 * A class that holds reference to visible and invisible objects.
 *
 * @author Andy Turner
 */
public class Universe {

    /**
     * Envelope
     */
    V2D_Envelope envelope;

    /**
     * The triangles to render.
     */
    public ArrayList<Triangle> triangles;
    
    /**
     * The polygons with no internal holes.
     */
    public ArrayList<PolygonNoInternalHoles> pnih;
    
    /**
     * The polygons.
     */
    public ArrayList<Polygon> polygons;
    
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
    public Universe(V2D_Envelope envelope) {
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
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     * @return The Triangle.
     */
    public Triangle addTriangle(V2D_Triangle triangle, int oom, RoundingMode rm){
        Triangle t = new Triangle(triangle, getNextID());
        triangles.add(t);
        envelope = envelope.union(triangle.getEnvelope(oom, rm), oom);
        return t;
    }
    
    /**
     * Adds the triangle and returns entity.
     * @param triangle The triangle to add.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     * @param color The colour of the triangle.
     * @param colorEdge The colour of the edge of the triangle.
     * @return The Triangle.
     */
    public Triangle addTriangle(V2D_Triangle triangle, int oom, RoundingMode rm, Color color, Color colorEdge){
        Triangle t = new Triangle(triangle, getNextID(), color, colorEdge);
        triangles.add(t);
        envelope = envelope.union(triangle.getEnvelope(oom, rm), oom);
        return t;
    }
    
    /**
     * Adds the triangle and returns entity.
     * @param triangle The triangle to add.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     * @param color The colour of the triangle.
     * @param colorPQ The colour of the triangle PQ edge.
     * @param colorQR The colour of the triangle QR edge.
     * @param colorRP The colour of the triangle RP edge.
     * @return The Triangle.
     */
    public Triangle addTriangle(V2D_Triangle triangle, int oom, RoundingMode rm, Color color, 
            Color colorPQ, Color colorQR, Color colorRP){
        Triangle t = new Triangle(triangle, getNextID(), color, colorPQ, colorQR, colorRP);
        triangles.add(t);
        envelope = envelope.union(triangle.getEnvelope(oom, rm), oom);
        return t;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     * @return The Triangle.
     */
    public PolygonNoInternalHoles addPolygonNoInternalHoles(V2D_PolygonNoInternalHoles polygon, int oom, RoundingMode rm){
        PolygonNoInternalHoles p = new PolygonNoInternalHoles(polygon, getNextID());
        pnih.add(p);
        envelope = envelope.union(polygon.getEnvelope(oom, rm), oom);
        return p;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @return The Polygon.
     * @param color The colour of the polygon.
     * @param colorEdge The colour of the edge of the polygon.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     */
    public PolygonNoInternalHoles addPolygon(V2D_PolygonNoInternalHoles polygon, int oom, RoundingMode rm,
            Color color, Color colorEdge){
        PolygonNoInternalHoles p = new PolygonNoInternalHoles(polygon, getNextID(), color, colorEdge);
        pnih.add(p);
        envelope = envelope.union(polygon.getEnvelope(oom, rm), oom);
        return p;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     * @return The Triangle.
     */
    public Polygon addPolygon(V2D_Polygon polygon, int oom, RoundingMode rm){
        Polygon t = new Polygon(polygon, getNextID());
        polygons.add(t);
        envelope = envelope.union(polygon.getEnvelope(oom, rm), oom);
        return t;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @return The Polygon.
     * @param color The colour of the polygon.
     * @param colorEdge The colour of the edge of the polygon.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     */
    public Polygon addPolygon(V2D_Polygon polygon, int oom, RoundingMode rm,
            Color color, Color colorEdge){
        Polygon t = new Polygon(polygon, getNextID(), color, colorEdge);
        polygons.add(t);
        envelope = envelope.union(polygon.getEnvelope(oom, rm), oom);
        return t;
    }
    
    /**
     * Adds the polygon and returns entity.
     * @param polygon The polygon to add.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     * @param colorInternalEdge What {@link #colorInternalEdge} is set to.
     * @param colorExternalEdge What {@link #colorExternalEdge} is set to.
     * @return The Polygon.
     */
    public Polygon addPolygon(V2D_Polygon polygon, int oom, RoundingMode rm,
            Color color, Color colorInternalEdge, Color colorExternalEdge){
        Polygon t = new Polygon(polygon, getNextID(), color, 
                colorInternalEdge, colorExternalEdge);
        polygons.add(t);
        envelope = envelope.union(polygon.getEnvelope(oom, rm), oom);
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
