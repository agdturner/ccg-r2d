/*
 * Copyright 2025 University of Leeds.
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
package uk.ac.leeds.ccg.r2d.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import uk.ac.leeds.ccg.v2d.core.d.V2D_EnvironmentDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonNoInternalHolesDouble;

public class GSHHGDouble {

    public HashMap<Integer, V2D_PolygonDouble> polygons;

    /**
     * @param p The path to the GSHHS file.
     * @param env The environment.
     * @param scale The scale to multiply x coordinate values by.
     * @param epsilon The tolerance within which two vectors are regarded as
     * equal.
     */
    public GSHHGDouble(Path p, V2D_EnvironmentDouble env, int scale, double epsilon) {

        polygons = new HashMap<>();

        /**
         * For looking up polygons from their id. Key are id, values are
         * position in collection.
         */
        HashMap<Integer, Integer> lookup = new HashMap<>();

        /**
         * For storing the collection of contained polygons.
         */
        HashSet<Integer> contained = new HashSet<>();

        try {
            int xmin = Integer.MAX_VALUE;
            int ymin = Integer.MAX_VALUE;
            int xmax = -Integer.MAX_VALUE;
            int ymax = -Integer.MAX_VALUE;
            try (DataInputStream in = new DataInputStream(new FileInputStream(p.toFile()))) {
                byte[] data = in.readNBytes(4);
                while (data[0] != -1) {
                    /*
                    * Global Self-consistent Hierarchical High-resolution Shorelines
                    * int id; Unique polygon id number, starting at 0
                    * int n; Number of points in this polygon
                    * int flag; = level + version << 8 + greenwich << 16 + source << 24 + river << 25
                    * flag contains 5 items, as follows:
                    * low byte: level = flag & 255: Values: 1 land, 2 lake, 3 island_in_lake, 4 pond_in_island_in_lake
                    * 2nd byte: version = (flag >> 8) & 255: Values: Should be 12 for GSHHG release 12 (i.e., version 2.2)
                    * 3rd byte: greenwich = (flag >> 16) & 1: Values: Greenwich is 1 if Greenwich is crossed
                    * 4th byte: source = (flag >> 24) & 1: Values: 0 = CIA WDBII, 1 = WVS
                    * 4th byte: river = (flag >> 25) & 1: Values: 0 = not set, 1 = river-lake and level = 2
                    * int west, east, south, north; min/max extent in micro-degrees
                    * int area; Area of polygon in 1/10 km^2
                    * int area_full; Area of original full-resolution polygon in 1/10 km^2
                    * int container; Id of container polygon that encloses this polygon (-1 if none)
                    * int ancestor; Id of ancestor polygon in the full resolution set that was the source of this polygon (-1 if none)
                     */
                    int id = ByteBuffer.wrap(data).getInt();
                    System.out.println("Creating Polygon id=" + id);
                    int n = in.readInt();
                    System.out.println("n=" + n);
                    int flag = in.readInt();
                    System.out.println("flag=" + flag);
                    int west = in.readInt();
                    System.out.println("west=" + west);
                    int east = in.readInt();
                    System.out.println("east=" + east);
                    int south = in.readInt();
                    System.out.println("south=" + south);
                    int north = in.readInt();
                    System.out.println("north=" + north);
                    int area = in.readInt();
                    System.out.println("area=" + area);
                    int area_full = in.readInt();
                    System.out.println("area_full=" + area_full);
                    int container = in.readInt();
                    System.out.println("container=" + container);
                    int ancestor = in.readInt();
                    System.out.println("ancestor=" + ancestor);
                    V2D_PointDouble[] points = new V2D_PointDouble[n];
                    //HashMap<Integer, V2D_LineSegmentDouble> externalEdges = new HashMap<>();
                    int x00 = in.readInt();
                    int y00 = in.readInt();
                    int x0 = x00;
                    int y0 = y00;
                    if (n > 1) {
                        points[0] = new V2D_PointDouble(env, (double) (x0 * scale) / 1000000d, (double) (y0 * scale) / 1000000d);
                        xmin = Math.min(xmin, x0);
                        xmax = Math.max(xmax, x0);
                        ymin = Math.min(ymin, y0);
                        ymax = Math.max(ymax, y0);
                        int x1 = in.readInt();
                        int y1 = in.readInt();
                        if (x0 > 180000000 && x1 < 180000000) {
                            //System.out.println("Crossleft i = " + i);
                            x1 = x1 + 360000000;
                        }
                        if (x0 < 180000000 && x1 > 180000000) {
                            //System.out.println("Crossright i = " + i);
                            x1 = x1 - 360000000;
                        }
                        points[1] = new V2D_PointDouble(env, (double) (x1 * scale) / 1000000d, (double) (y1 * scale) / 1000000d);
                        xmin = Math.min(xmin, x1);
                        xmax = Math.max(xmax, x1);
                        ymin = Math.min(ymin, y1);
                        ymax = Math.max(ymax, y1);
                        //externalEdges.put(externalEdges.size(), new V2D_LineSegmentDouble(points[0], points[1]));
                        for (int i = 2; i < n; i++) {
//                            System.out.println("i=" + i + " out of " + n);
//                            if (n == 221 && i == 220) {
//                                int debug = 1;
//                            }
                            x0 = x1;
                            y0 = y1;
                            x1 = in.readInt();
                            y1 = in.readInt();
                            if (x0 > 180000000 && x1 < 180000000) {
                                //System.out.println("Crossleft i = " + i);
                                x1 = x1 + 360000000;
                            }
                            if (x0 < 180000000 && x1 > 180000000) {
                                //System.out.println("Crossright i = " + i);
                                x1 = x1 - 360000000;
                            }
                            points[i] = new V2D_PointDouble(env, (double) (x1 * scale) / 1000000d, (double) (y1 * scale) / 1000000d);
                            xmin = Math.min(xmin, x1);
                            xmax = Math.max(xmax, x1);
                            ymin = Math.min(ymin, y1);
                            ymax = Math.max(ymax, y1);
                            //externalEdges.put(externalEdges.size(), new V2D_LineSegmentDouble(points[i - 1], points[i]));
                        }
                        if (x1 != x00) {
                            // This happens with the antarctic polygon.
                            int debug = 1;
                        } else {
                            try {
                                V2D_PolygonNoInternalHolesDouble polygon = new V2D_PolygonNoInternalHolesDouble(points, epsilon);
                                if (container == -1 || contained.contains(container)) {
                                    HashMap<Integer, V2D_PolygonNoInternalHolesDouble> internalHoles = new HashMap<>();
                                    int id2 = polygons.size();
                                    lookup.put(id, id2);
                                    polygons.put(id2, new V2D_PolygonDouble(polygon, epsilon));
                                } else {
                                    int id2 = lookup.get(container);
                                    if (polygons.containsKey(id2)) {
                                        V2D_PolygonDouble containerPolygon = polygons.get(id2);
                                        containerPolygon.internalHoles.put(containerPolygon.internalHoles.size(), polygon);
                                        contained.add(id);
                                    } else {
                                        System.out.println("Container polygon not yet formulated!");
                                        // Store the polygon to be added as and when...
                                    }
                                }
                            } catch (Exception e) {
                                int debug = 1;
                            }
                        }
                    }

                    data = in.readNBytes(4);
                    if (data.length == 0) {
                        break;
                    }
                }
            }
            System.out.println("xmin " + xmin);
            System.out.println("xmax " + xmax);
            System.out.println("ymin " + ymin);
            System.out.println("ymax " + ymax);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
