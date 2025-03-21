/*
 * Copyright 2021 Centre for Computational Geography.
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

import ch.obermuhlner.math.big.BigRational;
import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.MemoryImageSource;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.grids.d2.chunk.d.Grids_ChunkDoubleFactoryArray;
import uk.ac.leeds.ccg.grids.d2.chunk.d.Grids_ChunkDoubleFactorySinglet;
import uk.ac.leeds.ccg.grids.d2.grid.Grids_Dimensions;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDoubleFactory;
import uk.ac.leeds.ccg.io.IO_Cache;
import uk.ac.leeds.ccg.math.arithmetic.Math_Integer;
import uk.ac.leeds.ccg.r2d.d.entities.Polygon_d;
import uk.ac.leeds.ccg.r2d.d.entities.PolygonNoInternalHoles_d;
import uk.ac.leeds.ccg.r2d.d.entities.Triangle_t;
import uk.ac.leeds.ccg.r2d.grids.Colour_MapDouble;
import uk.ac.leeds.ccg.r2d.io.GSHHGDouble;
import uk.ac.leeds.ccg.r2d.io.IO;
import uk.ac.leeds.ccg.stats.range.Stats_RangeDouble;
import uk.ac.leeds.ccg.v2d.core.d.V2D_Environment_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_ConvexArea_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_FiniteGeometry_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegment_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Point_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Polygon_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonNoInternalHoles_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Rectangle_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Triangle_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Vector_d;

public class RenderImage_d {

    /**
     * Universe.
     */
    Universe_d universe;

    /**
     * The V2D_Environment_d
     */
    V2D_Environment_d env;

    /**
     * The window onto the universe to render.
     */
    V2D_Rectangle_d window;

    /**
     * The window grid/screen.
     */
    Grids_GridDouble grid;

    /**
     * For storing axes.
     */
    Axes_d axes;

    /**
     * Lower left corner point of screen.
     */
    V2D_Point_d p;

    /**
     * The pqr of window.
     */
    V2D_Triangle_d pqr;

    /**
     * The pq of window.
     */
    V2D_LineSegment_d pq;

    /**
     * The rsp of window.
     */
    V2D_Triangle_d rsp;

    /**
     * The rs of window.
     */
    V2D_LineSegment_d rs;

    /**
     * The pixel height pq vector of window.
     */
    V2D_Vector_d pqv;

    /**
     * The pixel width qrv vector of window.
     */
    V2D_Vector_d qrv;

    /**
     * The grids colour maps.
     */
    public ArrayList<Colour_MapDouble> gridCMs;

    /**
     * nrows
     */
    int nrows;

    /**
     * ncols
     */
    int ncols;

    /**
     * epsilon
     */
    double epsilon;

    /**
     * Path to output file.
     */
    Path output;

    /**
     * pixelSize
     */
    BigRational pixelSize;

    /**
     * If true then axes are drawn.
     */
    boolean drawAxes;

    /**
     * If true then triangles are drawn.
     */
    boolean drawTriangles;

    /**
     * If true then circumcircles are drawn for all triangles.
     */
    boolean drawCircumcircles;

    /**
     * If true then polygons with no internal holes are drawn.
     */
    boolean drawPolygonsNoInternalHoles;

    /**
     * If true then polygons are drawn.
     */
    boolean drawPolygons;

    /**
     * Create a new instance.
     *
     * @param universe The universe.
     * @param window The window onto the universe to render.
     */
    public RenderImage_d(Universe_d universe, V2D_Environment_d env,
            V2D_Rectangle_d window, int nrows, int ncols, double epsilon,
            boolean drawAxes, Grids_GridDouble grid,
            ArrayList<Colour_MapDouble> gridCMs, boolean drawTriangles,
            boolean drawCircumcircles, boolean drawPolygonsNoInternalHoles,
            boolean drawPolygons) {
        this.universe = universe;
        this.env = env;
        this.window = window;
        this.pqr = window.getPQR();
        this.rsp = window.getRSP();
        this.pq = pqr.getPQ();
        this.pqv = pq.l.v.divide((double) nrows);
        this.p = window.getPQR().getP();
        this.rs = rsp.getQR();
        this.qrv = pqr.getQR().l.v.divide((double) ncols);
        this.nrows = nrows;
        this.ncols = ncols;
        this.epsilon = epsilon;
        this.pixelSize = BigRational.valueOf(rs.getLength()).divide(ncols);
        this.drawAxes = drawAxes;
        this.grid = grid;
        this.gridCMs = gridCMs;
        this.drawTriangles = drawTriangles;
        this.drawCircumcircles = drawCircumcircles;
        this.drawPolygonsNoInternalHoles = drawPolygonsNoInternalHoles;
        this.drawPolygons = drawPolygons;
    }

    /**
     * The main method. Edit to configure details.
     *
     * @param args The arguments:
     * <ul>
     * <li>args[0] "d"</li>
     * <li>args[1] path to the data directory</li>
     * <li>args[2] the filename for the gshhs data set ("gshhs_c", "gshhs_l,
     * "gshhs_i", "gshhs_h", "gshhs_f")</li>
     * <li>args[3] the scale</li>
     * <li>args[4] the place ("ga", "g", "gb", "iom")</li>
     * </ul>
     */
    public static void main(String[] args) {
        String directory;
        if (args.length > 0) {
            directory = args[1];
        } else {
            directory = ".";
        }
        Path inDataDir = Paths.get(directory, "data", "input");
        Path outDataDir = Paths.get(directory, "data", "output", "d");
        Path dir = Paths.get(outDataDir.toString(), "test");
        //boolean drawAxes = true;
        boolean drawAxes = false;
        int oom = -6;
        RoundingMode rm = RoundingMode.HALF_UP;
        //double epsilon = 1d / 10000000d;
        //double epsilon = 1d / 100000000000d;
        double epsilon = 1d / 10000d;
        //double epsilon = 0d;
        V2D_Environment_d env = new V2D_Environment_d(epsilon);
        //boolean gshhs = false;
        boolean gshhs = true;
        String name;
        String gshhs_name;
        if (args.length > 1) {
            gshhs_name = args[2];
        } else {
            //gshhs_name = "gshhs_c";
            //gshhs_name = "gshhs_l";
            //gshhs_name = "gshhs_i";
            //gshhs_name = "gshhs_h";
            gshhs_name = "gshhs_f";
        }
        int nrows;
        int ncols;
        int scale;
        if (!gshhs) {
            scale = 1;
            nrows = 150 * scale;
            ncols = 150 * scale;
            name = "test";
        } else {
            if (args.length > 2) {
                scale = Integer.valueOf(args[3]);
            } else {
                scale = 1;
            }
            if (args.length > 4) {
                if (args[4].equalsIgnoreCase("ga")) {
                    // Global all         
                    nrows = 180 * scale;
                    ncols = 540 * scale;
                } else if (args[4].equalsIgnoreCase("g")) {
                    // Global less far south          
                    nrows = 165 * scale;
                    ncols = 400 * scale;
                } else if (args[4].equalsIgnoreCase("gb")) {
                    // GB
                    nrows = 15 * scale;
                    ncols = 14 * scale;
                } else {
                    //args[4].equalsIgnoreCase("iom")
                    // IOM
                    nrows = 40 * scale;
                    ncols = 53 * scale;
                }
                name = gshhs_name + "_" + args[4];
            } else {
                nrows = 40 * scale;
                ncols = 53 * scale;
                name = gshhs_name + "_iom";
            }
        }
        int nrowsd2 = nrows / 2;
        int ncolsd2 = ncols / 2;
        int ncolsd3 = ncols / 3;
        int ncolssncolsd3 = ncols - ncolsd3;
        // Init universe
        V2D_Vector_d offset = V2D_Vector_d.ZERO;
        double xmin;
        double xmax;
        double ymin = -nrowsd2;
        double ymax = nrowsd2;
        if (!gshhs) {
            xmin = -ncolsd2;
            xmax = ncolsd2;
        } else {
            if (args.length > 4) {
                if (args[4].equalsIgnoreCase("ga")) {
                    // Global all          
                    xmin = -ncolsd3;
                    xmax = ncolssncolsd3;
                    ymin = -75 * scale;
                    ymax = 90 * scale;
                } else if (args[4].equalsIgnoreCase("g")) {
                    // Global less far south         
                    xmin = -20 * scale;
                    xmax = 380 * scale;
                } else if (args[4].equalsIgnoreCase("gb")) {
                    // GB
                    ymin = 47 * scale;
                    ymax = 62 * scale;
                    xmin = -10 * scale;
                    xmax = 4 * scale;
                } else {
                    //args[4].equalsIgnoreCase("iom")
                    // IOM
                    ymin = 54.03d * scale;
                    ymax = 54.43d * scale;
                    xmin = (360d - 4.82) * scale;
                    xmax = (360d - 4.29) * scale;
                }
            } else {
                // IOM
                ymin = 54.03d * scale;
                ymax = 54.43d * scale;
                xmin = (360d - 4.82) * scale;
                xmax = (360d - 4.29) * scale;
            }
        }
        V2D_Point_d lb = new V2D_Point_d(env, offset, new V2D_Vector_d(xmin, ymin));
        V2D_Point_d lt = new V2D_Point_d(env, offset, new V2D_Vector_d(xmin, ymax));
        V2D_Point_d rt = new V2D_Point_d(env, offset, new V2D_Vector_d(xmax, ymax));
        V2D_Point_d rb = new V2D_Point_d(env, offset, new V2D_Vector_d(xmax, ymin));
        V2D_Rectangle_d window = new V2D_Rectangle_d(lb, lt, rt, rb);
        Universe_d universe = new Universe_d(window.getAABB());
        ArrayList<Colour_MapDouble> gridCMs = new ArrayList<>();

        boolean addGrid = false;
        //boolean addGrid = true;
        // Add grids
        Grids_GridDouble grid = null; // Grid for the window/screen.
        try {
            Grids_Environment ge = new Grids_Environment(new Generic_Environment(new Generic_Defaults(Paths.get("data"))));
            Path gDir = Paths.get("data", "grids");
            IO_Cache ioc = new IO_Cache(gDir, "V2D_Grids");
            Grids_ChunkDoubleFactoryArray dgcdf = new Grids_ChunkDoubleFactoryArray();
            Grids_ChunkDoubleFactorySinglet gcdf = new Grids_ChunkDoubleFactorySinglet(0d);
            Grids_GridDoubleFactory gdf = new Grids_GridDoubleFactory(ge, ioc, gcdf, dgcdf, ncols, ncols);
            BigRational xMin = BigRational.valueOf(xmin);
            BigRational xMax = BigRational.valueOf(xmax);
            BigRational yMin = BigRational.valueOf(ymin);
            BigRational yMax = BigRational.valueOf(ymax);
            BigRational cellsize = BigRational.valueOf(1);
            Grids_Dimensions dimensions = new Grids_Dimensions(xMin, xMax, yMin, yMax, cellsize);
            grid = gdf.create(nrows, ncols, dimensions);
            if (addGrid) {
                // Add grid 1
                gridCMs.add(addGrid1(gdf, universe, nrows, ncols));
                // Add grid 2
                gridCMs.add(addGrid2(gdf, universe, nrows / 2, ncols / 2));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        // Add triangles
        //boolean drawTriangles = true;
        boolean drawTriangles = false;
        int tt = 2;
        switch (tt) {
            case 0 ->
                addTriangles0(universe, env);
            case 1 ->
                addTriangles1(universe, env);
            case 2 ->
                addTriangles2(universe, env);
            case 3 ->
                addTriangles3(universe, env);
            case 4 ->
                addTriangles4(universe, env);
            case 5 ->
                addTriangles5(universe, env);
            case 6 ->
                addTriangles6(universe, env);
            case 7 ->
                addTriangles7(universe, env);
        }
        // Add polygons
        boolean drawPolygons = true;
        //boolean drawPolygons = false;
        boolean drawPolygonsNoInternalHoles = true;
        int pp;
        if (gshhs) {
            pp = 3;
        } else {
            pp = 4;
        }
        switch (pp) {
            case 0 ->
                addPolygons0(universe, env, epsilon);
            case 1 ->
                addPolygons1(universe, env, epsilon);
            case 2 ->
                addPolygons2(universe, env, epsilon);
            case 3 ->
                addPolygons3(universe, env, gshhs_name, scale, epsilon);
            case 4 ->
                addPolygons4(universe, env, epsilon);
        }

        // Draw circumcircles
        //boolean drawCircumcircles = false;
        boolean drawCircumcircles = true;
        // Render
        RenderImage_d ri = new RenderImage_d(universe, env, window, nrows,
                ncols, epsilon, drawAxes, grid, gridCMs, drawTriangles,
                drawCircumcircles, drawPolygonsNoInternalHoles, drawPolygons);
        String fname = name;
        if (drawTriangles) {
            fname += "_triangles" + tt;
        }
        if (drawPolygons) {
            if (!gshhs) {
                fname += "_polygons" + pp;
            }
        }
        if (addGrid) {
            fname += "_grid";
        }
        ri.output = Paths.get(dir.toString(), fname + "_nrows" + nrows + "_ncols" + ncols + ".png");
        System.out.println(ri.output.toString());
        ri.run();
    }

    public static Colour_MapDouble addGrid1(Grids_GridDoubleFactory gdf,
            Universe_d universe, int nrows, int ncols) {
        double n = nrows * ncols;
        Colour_MapDouble cm = new Colour_MapDouble();
        //range
        Stats_RangeDouble range0 = new Stats_RangeDouble(0d, n / 3d);
        cm.addRange(range0, Color.yellow);
        Stats_RangeDouble range1 = new Stats_RangeDouble(n / 3d, 2d * n / 3d);
        cm.addRange(range1, Color.orange);
        Stats_RangeDouble range2 = new Stats_RangeDouble(2d * n / 3d, n + 1);
        cm.addRange(range2, Color.red);
        Grids_GridDouble grid = null;
        try {
            BigRational xMin = BigRational.valueOf(-ncols / 2d);
            BigRational xMax = BigRational.valueOf(ncols / 2d);
            BigRational yMin = BigRational.valueOf(-nrows / 2d);
            BigRational yMax = BigRational.valueOf(nrows / 2d);
            BigRational cellsize = BigRational.valueOf(1);
            Grids_Dimensions dimensions = new Grids_Dimensions(xMin, xMax, yMin, yMax, cellsize);
            grid = gdf.create(nrows, ncols, dimensions);
            Random r = new Random(0);
            for (long row = 0L; row < nrows; row++) {
                for (long col = 0L; col < ncols; col++) {
                    grid.addToCell(row, col, r.nextDouble(0d, n));
                    //grid.addToCell(row, col, row * ncols + col);
                    //grid.setCell(row, col, row * ncols + col);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        universe.addGrid(grid);
        return cm;
    }

    public static Colour_MapDouble addGrid2(Grids_GridDoubleFactory gdf, Universe_d universe, int nrows, int ncols) {
        Colour_MapDouble cm = new Colour_MapDouble();
        double n = nrows * ncols;
        Stats_RangeDouble range0 = new Stats_RangeDouble(0, n / 2d);
        cm.addRange(range0, Color.yellow);
        Stats_RangeDouble range1 = new Stats_RangeDouble(n / 2d, n + 1);
        cm.addRange(range1, Color.black);
        Grids_GridDouble grid = null;
        try {
            BigRational xMin = BigRational.valueOf(0);
            BigRational xMax = BigRational.valueOf(ncols);
            BigRational yMin = BigRational.valueOf(0);
            BigRational yMax = BigRational.valueOf(nrows);
            BigRational cellsize = BigRational.valueOf(1);
            Grids_Dimensions dimensions = new Grids_Dimensions(xMin, xMax, yMin, yMax, cellsize);
            grid = gdf.create(nrows, ncols, dimensions);
            Random r = new Random(0);
            for (long row = 0L; row < nrows; row++) {
                for (long col = 0L; col < ncols; col++) {
                    grid.addToCell(row, col, r.nextDouble(0d, n));
                    //grid.addToCell(row, col, row * ncols + col);
                    //grid.setCell(row, col, row * ncols + col);
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        universe.addGrid(grid);
        return cm;
    }

    /**
     * One triangle.
     */
    public static void addTriangles0(Universe_d universe, V2D_Environment_d env) {
        Color color = Color.gray;
        Color colorEdge = Color.blue;
        V2D_Point_d p = new V2D_Point_d(env, -50d, -50d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 50d);
        V2D_Point_d r = new V2D_Point_d(env, 50d, -50d);
        V2D_Triangle_d t0 = new V2D_Triangle_d(p, q, r);
        //universe.addTriangle(t0);
        universe.addTriangle(t0, color, colorEdge);
    }

    /**
     * Three rotated overlapping large triangles.
     */
    public static void addTriangles1(Universe_d universe, V2D_Environment_d env) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        // 0
        V2D_Point_d p = new V2D_Point_d(env, -50d, -50d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 50d);
        V2D_Point_d r = new V2D_Point_d(env, 50d, -50d);
        V2D_Triangle_d t0 = new V2D_Triangle_d(p, q, r);
        //universe.addTriangle(t0);
        universe.addTriangle(t0, color, colorPQ, colorQR, colorRP);
        double theta;
        V2D_Point_d origin = new V2D_Point_d(env, 0d, 0d);
        // 1
        theta = Math.PI;
        V2D_Triangle_d t1 = t0.rotate(origin, theta);
        //universe.addTriangle(t1);
        universe.addTriangle(t1, color, colorPQ, colorQR, colorRP);
        // 2
        theta = Math.PI / 2d;
        V2D_Triangle_d t2 = t0.rotate(p, theta);
        //universe.addTriangle(t2);
        universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
        // 3
        theta = 3d * Math.PI / 2d;
        V2D_Triangle_d t3 = t0.rotate(origin, theta);
        //universe.addTriangle(t3);
        universe.addTriangle(t3, color, colorPQ, colorQR, colorRP);
    }

    /**
     * Multiple small rotated triangles some overlapping.
     */
    public static void addTriangles2(Universe_d universe, V2D_Environment_d env) {
        double epsilon = env.epsilon;
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        // 0
        V2D_Point_d p = new V2D_Point_d(env, -10d, -10d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 10d);
        V2D_Point_d r = new V2D_Point_d(env, 10d, -10d);
        V2D_Triangle_d t0 = new V2D_Triangle_d(p, q, r);
        universe.addTriangle(t0, color, colorPQ, colorQR, colorRP);
        double theta;
        V2D_Point_d origin = new V2D_Point_d(env, 0d, 0d);
        // 1
        theta = Math.PI;
        V2D_Triangle_d t1 = t0.rotate(origin, theta);
        universe.addTriangle(t1, color, colorPQ, colorQR, colorRP);
        // 2
        theta = Math.PI / 2d;
        V2D_Triangle_d t2 = t0.rotate(p, theta);
        universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
        // 3
        theta = 3d * Math.PI / 2d;
        V2D_Triangle_d t3 = t2.rotate(origin, theta);
        universe.addTriangle(t3, color, colorPQ, colorQR, colorRP);
        // 4
        theta = Math.PI / 2d;
        V2D_Triangle_d t4 = t2.rotate(t2.getR(), theta);
        universe.addTriangle(t4, color, colorPQ, colorQR, colorRP);
        // 5
        V2D_Triangle_d t5 = t4.rotate(t4.getR(), theta);
        universe.addTriangle(t5, color, colorPQ, colorQR, colorRP);
    }

    /**
     * Triangle rotated 48 times with increasing angle.
     */
    public static void addTriangles3(Universe_d universe, V2D_Environment_d env) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        V2D_Point_d p = new V2D_Point_d(env, -20d, -20d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 20d);
        V2D_Point_d r = new V2D_Point_d(env, 20d, -20d);
        V2D_Triangle_d t0 = new V2D_Triangle_d(p, q, r);
        universe.addTriangle(t0);
        V2D_Triangle_d t1 = t0;
        V2D_Triangle_d t2 = null;
        double theta = Math.PI / 12d;
        for (int i = 1; i <= 48; i++) {
            t2 = t1.rotate(t0.getR(), theta * (double) i);
            universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
            t1 = t2;
        }
        universe.addTriangle(t2);
    }

    /**
     * Triangle rotated a bit, then the result rotated a bit 48 times.
     */
    public static void addTriangles4(Universe_d universe, V2D_Environment_d env) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        V2D_Point_d p = new V2D_Point_d(env, -20d, -20d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 20d);
        V2D_Point_d r = new V2D_Point_d(env, 20d, -20d);
        V2D_Triangle_d t0 = new V2D_Triangle_d(p, q, r);
        universe.addTriangle(t0);
        V2D_Triangle_d t1 = t0;
        V2D_Triangle_d t2 = null;
        double theta = Math.PI / 12d;
        for (int i = 1; i <= 48; i++) {
            t2 = t1.rotate(t0.getR(), theta);
            universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
            t0 = t1;
            t1 = t2;
        }
        universe.addTriangle(t2);
    }

    /**
     * Apply lots of rotations to rotate a triangle back to original position.
     */
    public static void addTriangles5(Universe_d universe,
            V2D_Environment_d env) {
        V2D_Point_d p = new V2D_Point_d(env, -20d, -20d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 20d);
        V2D_Point_d r = new V2D_Point_d(env, 20d, -20d);
        V2D_Triangle_d t0 = new V2D_Triangle_d(p, q, r);
        universe.addTriangle(t0);
        V2D_Triangle_d t1 = t0;
        V2D_Triangle_d t2 = null;
        double n = 1000d;
        //double n = 10000000d;
        double theta = Math.PI / (12d * n);
        for (int i = 1; i <= 48 * n; i++) {
            t2 = t1.rotate(t0.getR(), theta);
            //universe.addTriangle(t2);
            t0 = t1;
            t1 = t2;
        }
        universe.addTriangle(t2);
    }

    /**
     * Adds two triangles and intersects these adding the triangular
     * intersecting parts that form a diamond.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addTriangles6(Universe_d universe, V2D_Environment_d env) {
        // 0
        V2D_Point_d p = new V2D_Point_d(env, -50d, -50d);
        V2D_Point_d q = new V2D_Point_d(env, 0d, 50d);
        V2D_Point_d r = new V2D_Point_d(env, 50d, -50d);
        Triangle_t t0 = universe.addTriangle(new V2D_Triangle_d(p, q, r));
        double theta;
        V2D_Point_d origin = new V2D_Point_d(env, 0d, 0d);
        // 1
        //theta = Math.PI;
        theta = Math.PI / 2d;
        Triangle_t t1 = universe.addTriangle(t0.triangle.rotate(origin, theta));
        // Calculate the intersection
        V2D_FiniteGeometry_d gi = t0.triangle.getIntersect(t1.triangle, env.epsilon);
        ArrayList<V2D_Triangle_d> git = ((V2D_ConvexArea_d) gi).getTriangles();
        for (int i = 0; i < git.size(); i++) {
            Triangle_t t = universe.addTriangle(git.get(i));
            t.setColor(Color.yellow);
        }
    }

    /**
     * Adds two triangles and intersects these adding the triangular
     * intersecting parts that form a hexagon.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addTriangles7(Universe_d universe, V2D_Environment_d env) {
        V2D_Point_d origin = new V2D_Point_d(env, 0d, 0d);
        Triangle_t t0 = universe.addTriangle(new V2D_Triangle_d(
                new V2D_Point_d(env, -30d, -30d),
                new V2D_Point_d(env, 0d, 60d),
                new V2D_Point_d(env, 30d, -30d)));
        double theta = Math.PI;
        Triangle_t t1 = universe.addTriangle(t0.triangle.rotate(origin, theta));
        // Calculate the intersection
        V2D_FiniteGeometry_d gi = t0.triangle.getIntersect(t1.triangle, env.epsilon);
        ArrayList<V2D_Triangle_d> git = ((V2D_ConvexArea_d) gi).getTriangles();
        for (int i = 0; i < git.size(); i++) {
            Triangle_t t = universe.addTriangle(git.get(i));
            t.setColor(Color.yellow);
        }
    }

    /**
     * One polygon that is not a convex hull.
     *
     * @param universe
     * @param epsilon The tolerance within which two vectors are regarded as
     * equal.
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons0(Universe_d universe, V2D_Environment_d env, double epsilon) {
        V2D_Point_d[] pts = new V2D_Point_d[8];
        pts[0] = new V2D_Point_d(env, -30d, -30d);
        pts[1] = new V2D_Point_d(env, -20d, 0d);
        pts[2] = new V2D_Point_d(env, -30d, 30d);
        pts[3] = new V2D_Point_d(env, 0d, 20d);
        pts[4] = new V2D_Point_d(env, 30d, 30d);
        pts[5] = new V2D_Point_d(env, 20d, 0d);
        pts[6] = new V2D_Point_d(env, 30d, -30d);
        pts[7] = new V2D_Point_d(env, 0d, -20d);
        V2D_PolygonNoInternalHoles_d polygon = new V2D_PolygonNoInternalHoles_d(pts, epsilon);
        universe.addPolygonNoInternalHoles(polygon);
    }

    /**
     * One polygon that is not a convex hull and that has other holes.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons1(Universe_d universe, V2D_Environment_d env, double epsilon) {
        ArrayList<V2D_Point_d> pts = new ArrayList<>();
        pts.add(new V2D_Point_d(env, -30d, -30d));
        pts.add(new V2D_Point_d(env, -25d, -20d));
        pts.add(new V2D_Point_d(env, -20d, -15d));
        pts.add(new V2D_Point_d(env, -24d, -10d));
        pts.add(new V2D_Point_d(env, -20d, 0d));
        pts.add(new V2D_Point_d(env, -24d, 10d));
        pts.add(new V2D_Point_d(env, -20d, 15d));
        pts.add(new V2D_Point_d(env, -25d, 20d));
        pts.add(new V2D_Point_d(env, -30d, 30d));
        pts.add(new V2D_Point_d(env, -20d, 25d));
        pts.add(new V2D_Point_d(env, -15d, 20d));
        pts.add(new V2D_Point_d(env, -10d, 24d));
        pts.add(new V2D_Point_d(env, 0d, 20d));
        pts.add(new V2D_Point_d(env, 10d, 24d));
        pts.add(new V2D_Point_d(env, 15d, 20d));
        pts.add(new V2D_Point_d(env, 20d, 25d));
        pts.add(new V2D_Point_d(env, 30d, 30d));
        pts.add(new V2D_Point_d(env, 25d, 20d));
        pts.add(new V2D_Point_d(env, 20d, 15d));
        pts.add(new V2D_Point_d(env, 24d, 10d));
        pts.add(new V2D_Point_d(env, 20d, 0d));
        pts.add(new V2D_Point_d(env, 24d, -10d));
        pts.add(new V2D_Point_d(env, 20d, -15d));
        pts.add(new V2D_Point_d(env, 25d, -20d));
        pts.add(new V2D_Point_d(env, 30d, -30d));
        pts.add(new V2D_Point_d(env, 20d, -25d));
        pts.add(new V2D_Point_d(env, 15d, -20d));
        pts.add(new V2D_Point_d(env, 10d, -24d));
        pts.add(new V2D_Point_d(env, 0d, -20d));
        pts.add(new V2D_Point_d(env, -10d, -24d));
        pts.add(new V2D_Point_d(env, -15d, -20d));
        pts.add(new V2D_Point_d(env, -20d, -25d));
        V2D_PolygonNoInternalHoles_d polygon;
        polygon = new V2D_PolygonNoInternalHoles_d(
                pts.toArray(V2D_Point_d[]::new), epsilon);
        universe.addPolygonNoInternalHoles(polygon);
    }

    /**
     * One polygon that is not a convex hull and that has other holes.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons2(Universe_d universe,
            V2D_Environment_d env, double epsilon) {
        // Outer points
        ArrayList<V2D_Point_d> pts = new ArrayList<>();
        pts.add(new V2D_Point_d(env, -30d, -30d));
        pts.add(new V2D_Point_d(env, -25d, -20d));
        pts.add(new V2D_Point_d(env, -20d, -15d));
        pts.add(new V2D_Point_d(env, -24d, -10d));
        pts.add(new V2D_Point_d(env, -20d, 0d));
        pts.add(new V2D_Point_d(env, -24d, 10d));
        pts.add(new V2D_Point_d(env, -20d, 15d));
        pts.add(new V2D_Point_d(env, -25d, 20d));
        pts.add(new V2D_Point_d(env, -30d, 30d));
        pts.add(new V2D_Point_d(env, -20d, 25d));
        pts.add(new V2D_Point_d(env, -15d, 20d));
        pts.add(new V2D_Point_d(env, -10d, 24d));
        pts.add(new V2D_Point_d(env, 0d, 20d));
        pts.add(new V2D_Point_d(env, 10d, 24d));
        pts.add(new V2D_Point_d(env, 15d, 20d));
        pts.add(new V2D_Point_d(env, 20d, 25d));
        pts.add(new V2D_Point_d(env, 30d, 30d));
        pts.add(new V2D_Point_d(env, 25d, 20d));
        pts.add(new V2D_Point_d(env, 20d, 15d));
        pts.add(new V2D_Point_d(env, 24d, 10d));
        pts.add(new V2D_Point_d(env, 20d, 0d));
        pts.add(new V2D_Point_d(env, 24d, -10d));
        pts.add(new V2D_Point_d(env, 20d, -15d));
        pts.add(new V2D_Point_d(env, 25d, -20d));
        pts.add(new V2D_Point_d(env, 30d, -30d));
        pts.add(new V2D_Point_d(env, 20d, -25d));
        pts.add(new V2D_Point_d(env, 15d, -20d));
        pts.add(new V2D_Point_d(env, 10d, -24d));
        pts.add(new V2D_Point_d(env, 0d, -20d));
        pts.add(new V2D_Point_d(env, -10d, -24d));
        pts.add(new V2D_Point_d(env, -15d, -20d));
        pts.add(new V2D_Point_d(env, -20d, -25d));
        // Internal Holes
        HashMap<Integer, V2D_PolygonNoInternalHoles_d> internalHoles = new HashMap<>();
        // Hole Points
        ArrayList<V2D_Point_d> hole_pts = new ArrayList<>();
        hole_pts.add(new V2D_Point_d(env, -15d, -15d));
        hole_pts.add(new V2D_Point_d(env, -12.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, -10d, -7.5d));
        hole_pts.add(new V2D_Point_d(env, -12d, -5d));
        hole_pts.add(new V2D_Point_d(env, -10d, 0d));
        hole_pts.add(new V2D_Point_d(env, -12d, 5d));
        hole_pts.add(new V2D_Point_d(env, -10d, 7.5d));
        hole_pts.add(new V2D_Point_d(env, -12.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, -15d, 15d));
        hole_pts.add(new V2D_Point_d(env, -10d, 12.5d));
        hole_pts.add(new V2D_Point_d(env, -7.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, -5d, 12d));
        hole_pts.add(new V2D_Point_d(env, 0d, 10d));
        hole_pts.add(new V2D_Point_d(env, 5d, 12d));
        hole_pts.add(new V2D_Point_d(env, 7.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, 10d, 12.5d));
        hole_pts.add(new V2D_Point_d(env, 15d, 15d));
        hole_pts.add(new V2D_Point_d(env, 12.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, 10d, 7.5d));
        hole_pts.add(new V2D_Point_d(env, 12d, 5d));
        hole_pts.add(new V2D_Point_d(env, 10d, 0d));
        hole_pts.add(new V2D_Point_d(env, 12d, -5d));
        hole_pts.add(new V2D_Point_d(env, 10d, -7.5d));
        hole_pts.add(new V2D_Point_d(env, 12.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, 15d, -15d));
        hole_pts.add(new V2D_Point_d(env, 10d, -12.5d));
        hole_pts.add(new V2D_Point_d(env, 7.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, 5d, -12d));
        hole_pts.add(new V2D_Point_d(env, 0d, -10d));
        hole_pts.add(new V2D_Point_d(env, -5d, -12d));
        hole_pts.add(new V2D_Point_d(env, -7.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, -10d, -12.5d));
        internalHoles.put(internalHoles.size(),
                new V2D_PolygonNoInternalHoles_d(
                        hole_pts.toArray(V2D_Point_d[]::new), epsilon));
        V2D_Polygon_d polygon = new V2D_Polygon_d(
                new V2D_PolygonNoInternalHoles_d(
                        pts.toArray(V2D_Point_d[]::new), epsilon),
                internalHoles, epsilon);
        universe.addPolygon(polygon, Color.lightGray, Color.red, Color.blue);
    }

    public static void addPolygons3(Universe_d universe,
            V2D_Environment_d env, String gshhs_name, int scale, double epsilon) {
        Path outDataDir = Paths.get("data", "input", "gshhg-bin-2.3.7");
        Path filepath = Paths.get(outDataDir.toString(), gshhs_name + ".b");
        V2D_Point_d[] points = null;
        GSHHGDouble gshhg = new GSHHGDouble(filepath, env, scale, epsilon);
        HashMap<Integer, V2D_Polygon_d> polygons = gshhg.polygons;
        for (V2D_Polygon_d p : polygons.values()) {
            universe.addPolygon(p);
        }
    }

    /**
     * One polygon that is not a convex hull and that has other holes.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons4(Universe_d universe,
            V2D_Environment_d env, double epsilon) {
        // Outer points
        ArrayList<V2D_Point_d> pts = new ArrayList<>();
        pts.add(new V2D_Point_d(env, -30d, -30d));
        pts.add(new V2D_Point_d(env, -25d, -20d));
        pts.add(new V2D_Point_d(env, -20d, -15d));
        pts.add(new V2D_Point_d(env, -24d, -10d));
        pts.add(new V2D_Point_d(env, -20d, 0d));
        pts.add(new V2D_Point_d(env, -24d, 10d));
        pts.add(new V2D_Point_d(env, -20d, 15d));
        pts.add(new V2D_Point_d(env, -25d, 20d));
        pts.add(new V2D_Point_d(env, -30d, 30d));
        pts.add(new V2D_Point_d(env, -20d, 25d));
        pts.add(new V2D_Point_d(env, -15d, 20d));
        pts.add(new V2D_Point_d(env, -10d, 24d));
        pts.add(new V2D_Point_d(env, 0d, 20d));
        pts.add(new V2D_Point_d(env, 10d, 24d));
        pts.add(new V2D_Point_d(env, 15d, 20d));
        pts.add(new V2D_Point_d(env, 20d, 25d));
        pts.add(new V2D_Point_d(env, 30d, 30d));
        pts.add(new V2D_Point_d(env, 25d, 20d));
        pts.add(new V2D_Point_d(env, 20d, 15d));
        pts.add(new V2D_Point_d(env, 24d, 10d));
        pts.add(new V2D_Point_d(env, 20d, 0d));
        pts.add(new V2D_Point_d(env, 24d, -10d));
        pts.add(new V2D_Point_d(env, 20d, -15d));
        pts.add(new V2D_Point_d(env, 25d, -20d));
        pts.add(new V2D_Point_d(env, 30d, -30d));
        pts.add(new V2D_Point_d(env, 20d, -25d));
        pts.add(new V2D_Point_d(env, 15d, -20d));
        pts.add(new V2D_Point_d(env, 10d, -24d));
        pts.add(new V2D_Point_d(env, 0d, -20d));
        pts.add(new V2D_Point_d(env, -10d, -24d));
        pts.add(new V2D_Point_d(env, -15d, -20d));
        pts.add(new V2D_Point_d(env, -20d, -25d));
        // Internal Holes
        HashMap<Integer, V2D_PolygonNoInternalHoles_d> internalHoles = new HashMap<>();
        // Hole Points
        ArrayList<V2D_Point_d> hole_pts = new ArrayList<>();
        hole_pts.add(new V2D_Point_d(env, -15d, -15d));
        hole_pts.add(new V2D_Point_d(env, -12.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, -10d, -7.5d));
        hole_pts.add(new V2D_Point_d(env, -12d, -5d));
        hole_pts.add(new V2D_Point_d(env, -10d, 0d));
        hole_pts.add(new V2D_Point_d(env, -12d, 5d));
        hole_pts.add(new V2D_Point_d(env, -10d, 7.5d));
        hole_pts.add(new V2D_Point_d(env, -12.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, -15d, 15d));
        hole_pts.add(new V2D_Point_d(env, -10d, 12.5d));
        hole_pts.add(new V2D_Point_d(env, -7.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, -5d, 12d));
        hole_pts.add(new V2D_Point_d(env, 0d, 10d));
        hole_pts.add(new V2D_Point_d(env, 5d, 12d));
        hole_pts.add(new V2D_Point_d(env, 7.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, 10d, 12.5d));
        hole_pts.add(new V2D_Point_d(env, 15d, 15d));
        hole_pts.add(new V2D_Point_d(env, 12.5d, 10d));
        hole_pts.add(new V2D_Point_d(env, 10d, 7.5d));
        hole_pts.add(new V2D_Point_d(env, 12d, 5d));
        hole_pts.add(new V2D_Point_d(env, 10d, 0d));
        hole_pts.add(new V2D_Point_d(env, 12d, -5d));
        hole_pts.add(new V2D_Point_d(env, 10d, -7.5d));
        hole_pts.add(new V2D_Point_d(env, 12.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, 15d, -15d));
        hole_pts.add(new V2D_Point_d(env, 10d, -12.5d));
        hole_pts.add(new V2D_Point_d(env, 7.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, 5d, -12d));
        hole_pts.add(new V2D_Point_d(env, 0d, -10d));
        hole_pts.add(new V2D_Point_d(env, -5d, -12d));
        hole_pts.add(new V2D_Point_d(env, -7.5d, -10d));
        hole_pts.add(new V2D_Point_d(env, -10d, -12.5d));
        internalHoles.put(internalHoles.size(),
                new V2D_PolygonNoInternalHoles_d(
                        hole_pts.toArray(V2D_Point_d[]::new), epsilon));
        V2D_Polygon_d polygon = new V2D_Polygon_d(
                new V2D_PolygonNoInternalHoles_d(
                        pts.toArray(V2D_Point_d[]::new), epsilon),
                internalHoles, epsilon);
        universe.addPolygon(polygon, Color.lightGray, Color.red, Color.blue);
    }

    /**
     * The process for rendering and image.
     *
     * @throws Exception
     */
    public void run() {
        int[] pix = render();
        if (drawAxes) {
            axes = new Axes_d(env, universe.envelope);
            renderLine(axes.xAxis, Color.blue, pix);
            renderLine(axes.yAxis, Color.red, pix);
        }
        MemoryImageSource m = new MemoryImageSource(ncols, nrows, pix, 0, ncols);
        Panel panel = new Panel();
        Image image = panel.createImage(m);
        IO.imageToFile(image, "png", output);
        System.out.println("Rendered");
    }

    /**
     * Creates an image map of the universe.
     */
    int[] render() {
        int n = ncols * nrows;
        int[] pix = new int[n];

        // Render grids
        ArrayList<Grids_GridDouble> grids = universe.grids;
        for (int i = 0; i < grids.size(); i++) {
            Grids_GridDouble grid = grids.get(i);
            Colour_MapDouble cm = gridCMs.get(i);
            long nrows = grid.getNRows();
            long ncols = grid.getNCols();
            for (long row = 0L; row < nrows; row++) {
                BigRational y = grid.getCellY(row);
                int gridRow = (int) this.grid.getRow(y);
                for (long col = 0L; col < ncols; col++) {
                    BigRational x = grid.getCellX(col);
                    if (this.grid.isInGrid(x, y)) {
                        int gridCol = (int) this.grid.getCol(x);
                        try {
                            Color color = cm.getColour(grid.getCell(x, y));
                            render(pix, gridRow, gridCol, color);
                        } catch (Exception e) {
                            System.err.print(e.getMessage());
                        }
                    }
                }
            }
        }

        // Render triangles
        if (drawTriangles) {
            ArrayList<Triangle_t> ts = universe.triangles;
            for (int i = 0; i < ts.size(); i++) {
                renderTriangle(ts.get(i), pix);
            }
        }

        // Render PolygonsNoInternalHoles
        if (drawPolygonsNoInternalHoles) {
            ArrayList<PolygonNoInternalHoles_d> ps = universe.pnih;
            for (int i = 0; i < ps.size(); i++) {
                renderPolygonNoInternalHoles(ps.get(i), pix, epsilon);
            }
        }

        // Render polygons
        if (drawPolygons) {
            ArrayList<Polygon_d> ps = universe.polygons;
            for (int i = 0; i < ps.size(); i++) {
                renderPolygon(ps.get(i), pix);
            }
        }

        return pix;
    }

    /**
     * For rendering a point on the image. Points may be obscured by other
     * rendered entities. The rendering order determines what is visible.
     *
     * @param epsilon The tolerance for intersection.
     * @param p The point to render.
     * @param pl The plane of the point to render. The normal is the vector to
     * this.
     * @param pix The image.
     * @return pix
     */
    public void renderPoint(V2D_Point_d p, Color c, int[] pix) {
        int row = getRow(p);
        int col = getCol(p);
        render(pix, row, col, c);
    }

    private void render(int[] pix, int r, int c, Color color) {
        r = nrows - r - 1;
        int in = (r * ncols) + c;
        if (!(in < 0 || in >= pix.length)) {
            pix[in] = color.getRGB();
        }
    }

    /**
     * Calculate and return the row index of the screen that p is on.
     *
     * @param p A point on the screen.
     * @param qr The line segment that is the bottom of the screen.
     * @return The row index of the screen for the point p.
     */
    protected int getRow(V2D_Point_d p) {
        BigRational d = BigRational.valueOf(rs.getDistance(p, epsilon));
        return (d.divide(pixelSize)).intValue();
    }

    /**
     * Calculate and return the column index of the screen that p is on.
     *
     * @param p A point on the screen.
     * @return The column index of the screen for the point p.
     */
    protected int getCol(V2D_Point_d p) {
        BigRational d = BigRational.valueOf(pq.getDistance(p, epsilon));
        return (d.divide(pixelSize)).intValue();
    }

    /**
     * For rendering a line on the image. Lines may be obscured by other
     * rendered entities. The rendering order determines what is visible.
     *
     * @param l The line to render.
     * @param pix The image.
     */
    public void renderLine(V2D_LineSegment_d l, Color color, int[] pix) {
        V2D_Point_d lp = l.getP();
        // Calculate the min and max row and col.
        int minr = getRow(lp);
        int minc = getCol(lp);
        int maxr = minr;
        int maxc = minc;
        V2D_Point_d lq = l.getQ();
        int rq = getRow(lq);
        int cq = getCol(lq);
        minr = Math.min(minr, rq);
        minc = Math.min(minc, cq);
        maxr = Math.max(maxr, rq);
        maxc = Math.max(maxc, cq);
        if (minr < 0) {
            minr = 0;
        }
        if (minc < 0) {
            minc = 0;
        }
        if (maxr >= nrows) {
            maxr = nrows - 1;
        }
        if (maxc >= ncols) {
            maxc = ncols - 1;
        }
        for (int r = minr; r <= maxr; r++) {
            for (int c = minc; c <= maxc; c++) {
                V2D_Rectangle_d pixel = getPixel(r, c);
                V2D_FiniteGeometry_d pil = pixel.getIntersect(l, epsilon);
                if (pil != null) {
                    render(pix, r, c, color);
                }
            }
        }
    }

    /**
     * For rendering a triangle on the image. Triangles may be obscured by other
     * rendered entities. The rendering order determines what is visible.
     *
     * @param l The line to render.
     * @param pix The image.
     */
    public void renderTriangle(Triangle_t triangle, int[] pix) {
        V2D_Triangle_d t = triangle.triangle;
        // Circumcircles
        if (drawCircumcircles) {
            V2D_Point_d circumcentre = t.getCircumcenter();
            double radius = circumcentre.getDistance(t.getP());
            drawCircle(pix, circumcentre, radius, Color.white);
        }
        V2D_Point_d tp = t.getP();
        // Calculate the min and max row and col.
        int rp = getRow(tp);
        int cp = getCol(tp);
        V2D_Point_d tq = t.getQ();
        int rq = getRow(tq);
        int cq = getCol(tq);
        V2D_Point_d tr = t.getR();
        int rr = getRow(tr);
        int cr = getCol(tr);
        int minr = Math_Integer.min(rp, rq, rr);
        int minc = Math_Integer.min(cp, cq, cr);
        int maxr = Math_Integer.max(rp, rq, rr);
        int maxc = Math_Integer.max(cp, cq, cr);
        if (minr < 0) {
            minr = 0;
        }
        if (minc < 0) {
            minc = 0;
        }
        if (maxr >= nrows) {
            maxr = nrows - 1;
        }
        if (maxc >= ncols) {
            maxc = ncols - 1;
        }
        for (int r = minr; r <= maxr; r++) {
            for (int c = minc; c <= maxc; c++) {
                V2D_Rectangle_d pixel = getPixel(r, c);
                V2D_FiniteGeometry_d pit = pixel.getIntersect(t, epsilon);
                if (pit != null) {
                    render(pix, r, c, triangle.color);
                    // Edge
                    /**
                     * There is probably a faster way to render the edge by only
                     * getting intersections for more restricted rows and
                     * columns.
                     */
                    // PQ
                    V2D_FiniteGeometry_d pipq = pixel.getIntersect(t.getPQ(), epsilon);
                    if (pipq != null) {
                        render(pix, r, c, triangle.getColorPQ());
                    }
                    // QR
                    V2D_FiniteGeometry_d piqr = pixel.getIntersect(t.getQR(), epsilon);
                    if (piqr != null) {
                        render(pix, r, c, triangle.getColorQR());
                    }
                    // RP
                    V2D_FiniteGeometry_d pirp = pixel.getIntersect(t.getRP(), epsilon);
                    if (pirp != null) {
                        render(pix, r, c, triangle.getColorRP());
                    }
                }
            }
        }
    }

    /**
     * For rendering a triangle on the image. Triangles may be obscured by other
     * rendered entities. The rendering order determines what is visible.
     *
     * @param l The polygon to render.
     * @param pix The image.
     * @param epsilon The tolerance within which two vectors are regarded as
     * equal.
     */
    public void renderPolygonNoInternalHoles(PolygonNoInternalHoles_d polygon, int[] pix, double epsilon) {
        V2D_PolygonNoInternalHoles_d poly = polygon.polygon;
        V2D_ConvexArea_d ch = poly.getConvexArea(epsilon);
        HashMap<Integer, V2D_LineSegment_d> externalEdges = poly.getEdges();
        V2D_LineSegment_d[] externalEdgesArray = new V2D_LineSegment_d[externalEdges.size()];
        for (int i = 0; i < externalEdgesArray.length; i++) {
            externalEdgesArray[i] = externalEdges.get(i);
        }
        V2D_Point_d[] ePs = ch.getPointsArray();
        // Calculate the min and max row and col.
        int minr = getRow(ePs[0]);
        int maxr = getRow(ePs[0]);
        int minc = getCol(ePs[0]);
        int maxc = getCol(ePs[0]);
        for (int i = 1; i < ePs.length; i++) {
            minr = Math.min(minr, getRow(ePs[i]));
            maxr = Math.max(maxr, getRow(ePs[i]));
            minc = Math.min(minc, getCol(ePs[i]));
            maxc = Math.max(maxc, getCol(ePs[i]));
        }
        if (minr < 0) {
            minr = 0;
        }
        if (minc < 0) {
            minc = 0;
        }
        if (maxr >= nrows) {
            maxr = nrows - 1;
        }
        if (maxc >= ncols) {
            maxc = ncols - 1;
        }
        for (int r = minr; r <= maxr; r++) {

            if (r == 53) {
                int debug = 1;
            }

            for (int c = minc; c <= maxc; c++) {

                if (c == 85) {
                    int debug = 1;
                }
                V2D_Rectangle_d pixel = getPixel(r, c);
                if (ch.intersects(pixel, epsilon)) {
                    if (poly.intersects(pixel, epsilon)) {
                        render(pix, r, c, polygon.color);
                    }
                    if (pixel.intersects(epsilon, externalEdgesArray)) {
                        render(pix, r, c, polygon.getColorExternalEdge());
                    }
                }
            }
        }
    }

    /**
     * For rendering a triangle on the image. Triangles may be obscured by other
     * rendered entities. The rendering order determines what is visible.
     *
     * @param l The polygon to render.
     * @param pix The image.
     */
    public void renderPolygon(Polygon_d polygon, int[] pix) {
        V2D_Polygon_d poly = polygon.polygon;
        V2D_ConvexArea_d ch = poly.getConvexArea(epsilon);
        HashMap<Integer, V2D_LineSegment_d> edges = poly.getEdges();
        //V2D_LineSegmentDouble[] externalEdgesArray = new V2D_LineSegment_d[externalEdges.size()];
        //for (int i = 0; i < externalEdgesArray.length; i++) {
        //    externalEdgesArray[i] = externalEdges.get(i);
        //}
        HashMap<Integer, V2D_PolygonNoInternalHoles_d> internalHoles = poly.internalHoles;
        V2D_Point_d[] ePs = ch.getPointsArray();
        // Calculate the min and max row and col.
        int minr = getRow(ePs[0]);
        int maxr = getRow(ePs[0]);
        int minc = getCol(ePs[0]);
        int maxc = getCol(ePs[0]);
        for (int i = 1; i < ePs.length; i++) {
            minr = Math.min(minr, getRow(ePs[i]));
            maxr = Math.max(maxr, getRow(ePs[i]));
            minc = Math.min(minc, getCol(ePs[i]));
            maxc = Math.max(maxc, getCol(ePs[i]));
        }
        if (minr < 0) {
            minr = 0;
        }
        if (minc < 0) {
            minc = 0;
        }
        if (maxr >= nrows) {
            maxr = nrows - 1;
        }
        if (maxc >= ncols) {
            maxc = ncols - 1;
        }
        for (int r = minr; r <= maxr; r++) {

            if (r == 75) {
                int debug = 1;
            }

            for (int c = minc; c <= maxc; c++) {

                if (c == 52) {
                    int debug = 1;
                }

                V2D_Rectangle_d pixel = getPixel(r, c);
                if (ch.intersects(pixel, epsilon)) {
                    if (poly.intersects(pixel, epsilon)) {
                        render(pix, r, c, polygon.color);
                    }
                    if (pixel.intersects(epsilon, edges.values())) {
                        render(pix, r, c, polygon.getColorExternalEdge());
                    }
                    for (var x : internalHoles.values()) {
                        if (pixel.intersects(epsilon, x.getEdges().values())) {
                            render(pix, r, c, polygon.getColorInternalEdge());
                        }
                    }
                }
            }
        }
    }

    /**
     * @param row The row index for the pixel returned.
     * @param col The column index for the pixel returned.
     * @return The pixel rectangle.
     */
    public V2D_Rectangle_d getPixel(int row, int col) {
        // p
        V2D_Point_d pP = new V2D_Point_d(p);
        pP.translate(pqv.multiply(row).add(qrv.multiply(col)));
        // q
        V2D_Point_d pQ = new V2D_Point_d(p);
        pQ.translate(pqv.multiply(row + 1).add(qrv.multiply(col)));
        // r
        V2D_Point_d pR = new V2D_Point_d(p);
        pR.translate(pqv.multiply(row + 1).add(qrv.multiply(col + 1)));
        // s
        V2D_Point_d pS = new V2D_Point_d(p);
        pS.translate(pqv.multiply(row).add(qrv.multiply(col + 1)));
        return new V2D_Rectangle_d(pP, pQ, pR, pS);
    }

    /**
     * https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
     * https://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm#Java
     *
     * @param pix The image.
     * @param centre The circle centre.
     * @param radius The circle radius.
     * @param color The colour of the circle.
     */
    private void drawCircle(int[] pix, V2D_Point_d centre, double radius,
            Color color) {
        int d = (5 - (int) radius * 4) / 4;
        int x = 0;
        int y = (int) radius;
        int centreY = getCol(centre);
        int centreX = getRow(centre);
        do {
            render(pix, centreX + x, centreY + y, color);
            render(pix, centreX + x, centreY - y, color);
            render(pix, centreX - x, centreY + y, color);
            render(pix, centreX - x, centreY - y, color);
            render(pix, centreX + y, centreY + x, color);
            render(pix, centreX + y, centreY - x, color);
            render(pix, centreX - y, centreY + x, color);
            render(pix, centreX - y, centreY - x, color);
            if (d < 0) {
                d += 2 * x + 1;
            } else {
                d += 2 * (x - y) + 1;
                y--;
            }
            x++;
        } while (x <= y);
    }
}
