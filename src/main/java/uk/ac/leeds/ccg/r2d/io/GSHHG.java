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

import ch.obermuhlner.math.big.BigRational;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import uk.ac.leeds.ccg.math.arithmetic.Math_BigRational;
import uk.ac.leeds.ccg.v2d.geometry.V2D_ConvexHull;
import uk.ac.leeds.ccg.v2d.geometry.V2D_LineSegment;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Point;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Polygon;

public class GSHHG {

    public ArrayList<V2D_Polygon> polygons;

    public GSHHG(Path p, int oom, RoundingMode rm) {

        polygons = new ArrayList<>();

        try {
            int xmin = Integer.MAX_VALUE;
            int ymin = Integer.MAX_VALUE;
            int xmax = -Integer.MAX_VALUE;
            int ymax = -Integer.MAX_VALUE;
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
                V2D_Point[] points = new V2D_Point[n];
                ArrayList<V2D_LineSegment> externalEdges = new ArrayList<>();
                int x0 = in.readInt();
                int y0 = in.readInt();
                if (n > 1) {
                    points[0] = new V2D_Point(BigRational.valueOf(x0, 1000000), BigRational.valueOf(y0, 1000000));
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
                    points[1] = new V2D_Point(BigRational.valueOf(x1, 1000000), BigRational.valueOf(y1, 1000000));
                    xmin = Math.min(xmin, x1);
                    xmax = Math.max(xmax, x1);
                    ymin = Math.min(ymin, y1);
                    ymax = Math.max(ymax, y1);
                    externalEdges.add(new V2D_LineSegment(points[0], points[1], oom, rm));
                    for (int i = 2; i < n; i++) {
                        x0 = x1;
                        y0 = y1;
                        points[0] = points[1];
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
                        points[i] = new V2D_Point(BigRational.valueOf(x1, 1000000), BigRational.valueOf(y1, 1000000));
                        xmin = Math.min(xmin, x1);
                        xmax = Math.max(xmax, x1);
                        ymin = Math.min(ymin, y1);
                        ymax = Math.max(ymax, y1);
                        externalEdges.add(new V2D_LineSegment(points[i - 1], points[i], oom, rm));
                    }
                    V2D_ConvexHull ch = new V2D_ConvexHull(oom, rm, points);
//                V2D_Polygon polygon = new V2D_Polygon(ch);
//                V2D_Polygon polygon = new V2D_Polygon(ch, externalEdges,
//                        externalHoles, internalEdges, internalHoles);
                    V2D_Polygon polygon = new V2D_Polygon(ch, externalEdges,
                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                    //System.out.println(polygon.toString());
                    polygons.add(polygon);
                }
                data = in.readNBytes(4);
                if (data.length == 0) {
                    break;
                }
            }
            in.close();
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
