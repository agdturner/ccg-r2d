/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.leeds.ccg.r2d.d.entities;

import java.awt.Color;
import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.r2d.entities.PolygonNoInternalHolesEntity;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonNoInternalHolesDouble;

/**
 *
 * @author Andy Turner
 */
public class PolygonNoInternalHolesDouble extends PolygonNoInternalHolesEntity {
    
    private static final long serialVersionUID = 1L;

    /**
     * The polygon geometry
     */
    public V2D_PolygonNoInternalHolesDouble polygon;
    
    /**
     * Create a new instance.
     *
     * @param polygon What {@link #polygon} is set to.
     * @param id What {@link #id} is set to.
     */
    public PolygonNoInternalHolesDouble(
            V2D_PolygonNoInternalHolesDouble polygon, Data_ID_long id) {
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
    public PolygonNoInternalHolesDouble(
            V2D_PolygonNoInternalHolesDouble polygon, Data_ID_long id,
            Color color, Color colorEdge){
        super(id, color, colorEdge);
        this.polygon = polygon;
    }
}
