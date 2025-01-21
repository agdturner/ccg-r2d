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

import java.util.ArrayList;
import uk.ac.leeds.ccg.data.id.Data_ID_long;
import uk.ac.leeds.ccg.r2d.d.entities.TriangleDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_EnvelopeDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_TriangleDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_VectorDouble;

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
     * The triangles to render.
     */
    public ArrayList<TriangleDouble> triangles;
    
    /**
     * long
     */
    long nextID;

    /**
     * Create a new instance.
     *
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode for any rounding.
     */
    
    /**
     * @param envelope The initial envelope.
     */
    public UniverseDouble(V2D_EnvelopeDouble envelope) {
        nextID = 0L;
        triangles = new ArrayList<>();
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
     * Adds the triangle and returns it's id.
     * @param t The triangle to add. 
     */
    public Data_ID_long addTriangle(V2D_TriangleDouble t){
        Data_ID_long id = getNextID();
        triangles.add(new TriangleDouble(t, id));
        envelope = envelope.union(t.getEnvelope());
        return id;
    }
}
