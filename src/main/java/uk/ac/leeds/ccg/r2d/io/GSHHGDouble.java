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
import java.util.ArrayList;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_ConvexHullDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegmentDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonDouble;

public class GSHHGDouble {

    public ArrayList<V2D_PolygonDouble> polygons;

    public GSHHGDouble(Path p, double epsilon) {
        
        polygons = new ArrayList<>();
        
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(p.toFile()));
            byte[] data = in.readNBytes(4);
            while (data[0] != -1) {
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
                ArrayList<V2D_LineSegmentDouble> externalEdges = new ArrayList<>();
                double x0 = in.readInt();
                double y0 = in.readInt();
                points[0] = new V2D_PointDouble(x0, y0);
                double xmin = x0;
                double ymin = y0;
                double xmax = x0;
                double ymax = y0;
                double x1 = in.readInt();
                double y1 = in.readInt();
                points[1] = new V2D_PointDouble(x1 /1000000d, y1 /1000000d);
                xmin = Math.min(xmin, x1);
                xmax = Math.max(xmax, x1);
                ymin = Math.min(ymin, y1);
                ymax = Math.max(ymax, y1);
                for (int i = 2; i < n; i++) {
                    x0 = x1;
                    y0 = y1;
                    points[0] = points[1];
                    x1 = in.readInt();
                    y1 = in.readInt();
                    if (x0 > 180 && x1 < 180) {
                        System.out.println("Crossleft i = " + i);
                    }
                    if (x0 < 180 && x1 > 180) {
                        System.out.println("Crossright i = " + i);
                    }
                    points[i] = new V2D_PointDouble(x1 /1000000d, y1 /1000000d);
                    xmin = Math.min(xmin, x1);
                    xmax = Math.max(xmax, x1);
                    ymin = Math.min(ymin, y1);
                    ymax = Math.max(ymax, y1);
                    externalEdges.add(new V2D_LineSegmentDouble(points[i - 1], points[i]));
                }
                V2D_ConvexHullDouble ch = new V2D_ConvexHullDouble(epsilon, points);
//                V2D_PolygonDouble polygon = new V2D_PolygonDouble(ch);
//                V2D_PolygonDouble polygon = new V2D_PolygonDouble(ch, externalEdges,
//                        externalHoles, internalEdges, internalHoles);
                V2D_PolygonDouble polygon = new V2D_PolygonDouble(ch, externalEdges,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                polygons.add(polygon);
                data = in.readNBytes(4);
                if (data.length == 0) {
                    break;
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
