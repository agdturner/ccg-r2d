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
package uk.ac.leeds.ccg.r2d;

import ch.obermuhlner.math.big.BigRational;
import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.MemoryImageSource;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import uk.ac.leeds.ccg.math.arithmetic.Math_BigDecimal;
import uk.ac.leeds.ccg.math.arithmetic.Math_BigRational;
import uk.ac.leeds.ccg.math.arithmetic.Math_Integer;
import uk.ac.leeds.ccg.r2d.entities.Polygon;
import uk.ac.leeds.ccg.r2d.entities.Triangle;
import uk.ac.leeds.ccg.r2d.grids.Colour_MapDouble;
import uk.ac.leeds.ccg.r2d.io.IO;
import uk.ac.leeds.ccg.stats.range.Stats_RangeDouble;
import uk.ac.leeds.ccg.v2d.geometry.V2D_ConvexHull;
import uk.ac.leeds.ccg.v2d.geometry.V2D_FiniteGeometry;
import uk.ac.leeds.ccg.v2d.geometry.V2D_LineSegment;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Point;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Polygon;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Rectangle;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Triangle;
import uk.ac.leeds.ccg.v2d.geometry.V2D_Vector;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;

public class RenderImage {

    /**
     * Universe.
     */
    Universe universe;

    /**
     * The window onto the universe to render.
     */
    V2D_Rectangle window;

    /**
     * The window grid/screen.
     */
    Grids_GridDouble grid;

    /**
     * For storing axes.
     */
    Axes axes;

    /**
     * Lower left corner point of screen.
     */
    V2D_Point p;

    /**
     * The pqr of window.
     */
    V2D_Triangle pqr;

    /**
     * The pq of window.
     */
    V2D_LineSegment pq;

    /**
     * The rsp of window.
     */
    V2D_Triangle rsp;

    /**
     * The rs of window.
     */
    V2D_LineSegment rs;

    /**
     * The pixel height pq vector of window.
     */
    V2D_Vector pqv;

    /**
     * The pixel width qrv vector of window.
     */
    V2D_Vector qrv;

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
     * The Order of Magnitude for the precision.
     */
    int oom;

    /**
     * The RoundingMode for any rounding.
     */
    RoundingMode rm;

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
     * If true then polygons are drawn.
     */
    boolean drawPolygons;
    
    /**
     * Create a new instance.
     *
     * @param universe The universe.
     * @param window The window onto the universe to render.
     */
    public RenderImage(Universe universe,
            V2D_Rectangle window, int nrows, int ncols, int oom, RoundingMode rm,
            boolean drawAxes, Grids_GridDouble grid,
            ArrayList<Colour_MapDouble> gridCMs, boolean drawTriangles,
            boolean drawCircumcircles, boolean drawPolygons) {
        this.universe = universe;
        this.window = window;
        this.pqr = window.getPQR();
        this.rsp = window.getRSP();
        this.pq = pqr.getPQ(oom, rm);
        this.pqv = pq.l.v.divide(BigRational.valueOf(nrows), oom, rm);
        this.p = window.getPQR().getP();
        this.rs = rsp.getQR(oom, rm);
        this.qrv = pqr.getQR(oom, rm).l.v.divide(BigRational.valueOf(ncols), oom, rm);
        this.nrows = nrows;
        this.ncols = ncols;
        this.oom = oom;
        this.rm = rm;
        this.pixelSize = rs.getLength(oom, rm).getSqrt(oom, rm).divide(ncols);
        this.drawAxes = drawAxes;
        this.grid = grid;
        this.gridCMs = gridCMs;
        this.drawTriangles = drawTriangles;
        this.drawCircumcircles = drawCircumcircles;
        this.drawPolygons = drawPolygons;
    }

    /**
     * Edit to add/remove grid background and render different triangles.
     */
    public static void main(String[] args) {
        Path inDataDir = Paths.get("data", "input");
        Path outDataDir = Paths.get("data", "output");
        Path dir = Paths.get(outDataDir.toString(), "test");
        //boolean drawAxes = true;
        boolean drawAxes = false;
        int oom = -6;
        RoundingMode rm = RoundingMode.HALF_UP;
        //double epsilon = 0d;
        int nrows = 150;
        int ncols = 150;
        // Init universe
        long m = 75;         // multiplication factor
        V2D_Vector offset = V2D_Vector.ZERO;
        V2D_Point lb = new V2D_Point(offset, new V2D_Vector(-1 * m, -1 * m));
        V2D_Point lt = new V2D_Point(offset, new V2D_Vector(-1 * m, 1 * m));
        V2D_Point rt = new V2D_Point(offset, new V2D_Vector(1 * m, 1 * m));
        V2D_Point rb = new V2D_Point(offset, new V2D_Vector(1 * m, -1 * m));
        V2D_Rectangle window = new V2D_Rectangle(lb, lt, rt, rb, oom, rm);
        Universe universe = new Universe(window.getEnvelope(oom, rm));
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
            BigRational xMin = BigRational.valueOf(-ncols / 2d);
            BigRational xMax = BigRational.valueOf(ncols / 2d);
            BigRational yMin = BigRational.valueOf(-nrows / 2d);
            BigRational yMax = BigRational.valueOf(nrows / 2d);
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
        Math_BigDecimal bd = new Math_BigDecimal(100);
        int tt = 2;
        switch (tt) {
            case 0 ->
                addTriangles0(universe, bd, oom, rm);
            case 1 ->
                addTriangles1(universe, bd, oom, rm);
            case 2 ->
                addTriangles2(universe, bd, oom, rm);
            case 3 ->
                addTriangles3(universe, bd, oom, rm); // 10 minutes
            case 4 ->
                addTriangles4(universe, bd, oom, rm); // 8 mins
            case 5 ->
                addTriangles5(universe, bd, oom, rm); // 2 minutes.
            case 6 ->
                addTriangles6(universe, bd, oom, rm); // 1 minute
            case 7 ->
                addTriangles7(universe, bd, oom, rm); // 1 minute
        }
        // Add polygons
        boolean drawPolygons = true;
        //boolean drawPolygons = false;
        int pp = 0;
        switch (pp) {
            case 0 ->
                addPolygons0(universe, oom, rm);
        }
        
        // Draw circumcircles
        //boolean drawCircumcircles = false;
        boolean drawCircumcircles = true;
        // Render
        RenderImage ri = new RenderImage(universe, window, nrows, ncols, oom, 
                rm, drawAxes, grid, gridCMs, drawTriangles, drawCircumcircles, 
                drawPolygons);
        String fname = "test";
        if (drawTriangles) {
            fname += "_triangles" + tt;
        }
        if (drawPolygons) {
            fname += "_polygons" + pp;
        }
        if (addGrid) {
            fname += "_grid";
        }
        ri.output = Paths.get(dir.toString(), fname + ".png");
        System.out.println(ri.output.toString());
        ri.run();
    }

    public static Colour_MapDouble addGrid1(Grids_GridDoubleFactory gdf, Universe universe, int nrows, int ncols) {
        double n = nrows * ncols;
        Colour_MapDouble cm = new Colour_MapDouble();
        //range
        Stats_RangeDouble range0 = new Stats_RangeDouble(0, n / 3d);
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

    public static Colour_MapDouble addGrid2(Grids_GridDoubleFactory gdf, Universe universe, int nrows, int ncols) {
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
    public static void addTriangles0(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        Color color = Color.gray;
        Color colorEdge = Color.blue;
        V2D_Point p = new V2D_Point(-50d, -50d);
        V2D_Point q = new V2D_Point(0d, 50d);
        V2D_Point r = new V2D_Point(50d, -50d);
        V2D_Triangle t0 = new V2D_Triangle(p, q, r, oom, rm);
        //universe.addTriangle(t0, oom);
        universe.addTriangle(t0, oom, rm, color, colorEdge);
        BigRational theta;
        V2D_Point origin = new V2D_Point(0d, 0d);
    }

    /**
     * Three rotated overlapping large triangles.
     */
    public static void addTriangles1(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        // 0
        V2D_Point p = new V2D_Point(-50d, -50d);
        V2D_Point q = new V2D_Point(0d, 50d);
        V2D_Point r = new V2D_Point(50d, -50d);
        V2D_Triangle t0 = new V2D_Triangle(p, q, r, oom, rm);
        universe.addTriangle(t0, oom, rm, color, colorPQ, colorQR, colorRP);
        BigRational theta;
        V2D_Point origin = new V2D_Point(0d, 0d);
        // 1
        theta = Math_BigRational.getPi(bd, oom - 2, rm);
        V2D_Triangle t1 = t0.rotate(origin, theta, bd, oom, rm);
        universe.addTriangle(t1, oom, rm, color, colorPQ, colorQR, colorRP);
        // 2
        theta = theta.divide(2);
        V2D_Triangle t2 = t0.rotate(p, theta, bd, oom, rm);
        universe.addTriangle(t2, oom, rm, color, colorPQ, colorQR, colorRP);
        // 3
        theta = theta.multiply(3);
        V2D_Triangle t3 = t0.rotate(origin, theta, bd, oom, rm);
        universe.addTriangle(t3, oom, rm, color, colorPQ, colorQR, colorRP);
    }

    /**
     * Multiple small rotated triangles some overlapping.
     */
    public static void addTriangles2(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        // 0
        V2D_Point p = new V2D_Point(-10d, -10d);
        V2D_Point q = new V2D_Point(0d, 10d);
        V2D_Point r = new V2D_Point(10d, -10d);
        V2D_Triangle t0 = new V2D_Triangle(p, q, r, oom, rm);
        universe.addTriangle(t0, oom, rm, color, colorPQ, colorQR, colorRP);
        BigRational theta;
        V2D_Point origin = new V2D_Point(0d, 0d);
        // 1
        theta = Math_BigRational.getPi(bd, oom - 2, rm);
        V2D_Triangle t1 = t0.rotate(origin, theta, bd, oom, rm);
        universe.addTriangle(t1, oom, rm, color, colorPQ, colorQR, colorRP);
        // 2
        theta = theta.divide(2);
        V2D_Triangle t2 = t0.rotate(p, theta, bd, oom, rm);
        universe.addTriangle(t2, oom, rm, color, colorPQ, colorQR, colorRP);
        // 3
        theta = theta.multiply(3);
        V2D_Triangle t3 = t2.rotate(origin, theta, bd, oom, rm);
        universe.addTriangle(t3, oom, rm, color, colorPQ, colorQR, colorRP);
        // 4
        theta = theta.divide(3);
        V2D_Triangle t4 = t2.rotate(t2.getR(), theta, bd, oom, rm);
        universe.addTriangle(t4, oom, rm, color, colorPQ, colorQR, colorRP);
        // 5
        V2D_Triangle t5 = t4.rotate(t4.getR(), theta, bd, oom, rm);
        universe.addTriangle(t5, oom, rm, color, colorPQ, colorQR, colorRP);
    }

    /**
     * Triangle rotated 48 times with increasing angle.
     */
    public static void addTriangles3(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        V2D_Point p = new V2D_Point(-20d, -20d);
        V2D_Point q = new V2D_Point(0d, 20d);
        V2D_Point r = new V2D_Point(20d, -20d);
        V2D_Triangle t0 = new V2D_Triangle(p, q, r, oom, rm);
        universe.addTriangle(t0, oom, rm);
        V2D_Triangle t1 = t0;
        V2D_Triangle t2 = null;
        BigRational theta = Math_BigRational.getPi(bd, oom - 2, rm).divide(12);
        for (int i = 1; i <= 48; i++) {
            t2 = t1.rotate(t0.getR(), theta.multiply(i), bd, oom, rm);
            universe.addTriangle(t2, oom, rm, color, colorPQ, colorQR, colorRP);
            t1 = t2;
        }
        universe.addTriangle(t2, oom, rm);
    }

    /**
     * Triangle rotated a bit, then the result rotated a bit 48 times.
     */
    public static void addTriangles4(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        V2D_Point p = new V2D_Point(-20d, -20d);
        V2D_Point q = new V2D_Point(0d, 20d);
        V2D_Point r = new V2D_Point(20d, -20d);
        V2D_Triangle t0 = new V2D_Triangle(p, q, r, oom, rm);
        universe.addTriangle(t0, oom, rm);
        V2D_Triangle t1 = t0;
        V2D_Triangle t2 = null;
        BigRational theta = Math_BigRational.getPi(bd, oom - 2, rm).divide(12);
        for (int i = 1; i <= 48; i++) {
            t2 = t1.rotate(t0.getR(), theta, bd, oom, rm);
            universe.addTriangle(t2, oom, rm, color, colorPQ, colorQR, colorRP);
            t0 = t1;
            t1 = t2;
        }
        universe.addTriangle(t2, oom, rm);
    }

    /**
     * Apply lots of rotations to rotate a triangle back to original position.
     */
    public static void addTriangles5(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        V2D_Point p = new V2D_Point(-20d, -20d);
        V2D_Point q = new V2D_Point(0d, 20d);
        V2D_Point r = new V2D_Point(20d, -20d);
        V2D_Triangle t0 = new V2D_Triangle(p, q, r, oom, rm);
        universe.addTriangle(t0, oom, rm);
        V2D_Triangle t1 = t0;
        V2D_Triangle t2 = null;
        int n = 1000;
        //int n = 1000000; // Takes too long. Might be useful for profiling/optimisation...
        BigRational theta = Math_BigRational.getPi(bd, oom - 2, rm).divide(12 * n);
        for (int i = 1; i <= 48 * n; i++) {
            t2 = t1.rotate(t0.getR(), theta, bd, oom, rm);
            //universe.addTriangle(t2, oom);
            t0 = t1;
            t1 = t2;
        }
        universe.addTriangle(t2, oom, rm);
    }

    /**
     * Adds two triangles and intersects these adding the triangular
     * intersecting parts. Two rotated triangles with a two triangle
     * intersection.
     */
    public static void addTriangles6(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        // 0
        V2D_Point p = new V2D_Point(-50d, -50d);
        V2D_Point q = new V2D_Point(0d, 50d);
        V2D_Point r = new V2D_Point(50d, -50d);
        Triangle t0 = universe.addTriangle(new V2D_Triangle(p, q, r, oom, rm), oom, rm);
        BigRational theta;
        V2D_Point origin = new V2D_Point(0d, 0d);
        // 1
        //theta = Math.PI;
        theta = Math_BigRational.getPi(bd, oom - 2, rm).divide(2);
        Triangle t1 = universe.addTriangle(t0.triangle.rotate(origin, theta, bd, oom, rm), oom, rm);
        // Calculate the intersection
        V2D_FiniteGeometry gi = t0.triangle.getIntersection(t1.triangle, oom, rm);
        ArrayList<V2D_Triangle> git = ((V2D_ConvexHull) gi).getTriangles(oom, rm);
        for (int i = 0; i < git.size(); i++) {
            Triangle t = universe.addTriangle(git.get(i), oom, rm);
            t.setColor(Color.yellow);
        }
    }

    /**
     * Adds two triangles and intersects these adding the triangular
     * intersecting parts. Two rotated triangles with a four triangle
     * intersection.
     */
    public static void addTriangles7(Universe universe, Math_BigDecimal bd, int oom, RoundingMode rm) {
        V2D_Point origin = new V2D_Point(0d, 0d);
        Triangle t0 = universe.addTriangle(new V2D_Triangle(
                new V2D_Point(-30d, -30d),
                new V2D_Point(0d, 60d),
                new V2D_Point(30d, -30d), oom, rm), oom, rm);
        BigRational theta = Math_BigRational.getPi(bd, oom - 2, rm);
        Triangle t1 = universe.addTriangle(t0.triangle.rotate(origin, theta, bd, oom, rm), oom, rm);
        // Calculate the intersection
        V2D_FiniteGeometry gi = t0.triangle.getIntersection(t1.triangle, oom, rm);
        ArrayList<V2D_Triangle> git = ((V2D_ConvexHull) gi).getTriangles(oom, rm);
        for (int i = 0; i < git.size(); i++) {
            Triangle t = universe.addTriangle(git.get(i), oom, rm);
            t.setColor(Color.yellow);
        }
    }
    
    /**
     * Adds two triangles and interects these adding the triangular intersecting
     * parts that form a hexagon.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons0(Universe universe, int oom, RoundingMode rm) {
        V2D_Point origin = new V2D_Point(0d, 0d);
        V2D_Point a = new V2D_Point(-30d, -30d);
        V2D_Point b = new V2D_Point(-20d, 0d);
        V2D_Point c = new V2D_Point(-30d, 30d);
        V2D_Point d = new V2D_Point(0d, 20d);
        V2D_Point e = new V2D_Point(30d, 30d);
        V2D_Point f = new V2D_Point(20d, 0d);
        V2D_Point g = new V2D_Point(30d, -30d);
        V2D_Point h = new V2D_Point(0d, -20d);
        V2D_ConvexHull ch = new V2D_ConvexHull(oom, rm,
                a, b, c, d, e, f, g, h);
        ArrayList<V2D_LineSegment> edges = new ArrayList<>();
        edges.add(new V2D_LineSegment(a, b, oom, rm));
        edges.add(new V2D_LineSegment(b, c, oom, rm));
        edges.add(new V2D_LineSegment(c, d, oom, rm));
        edges.add(new V2D_LineSegment(d, e, oom, rm));
        edges.add(new V2D_LineSegment(e, f, oom, rm));
        edges.add(new V2D_LineSegment(f, g, oom, rm));
        edges.add(new V2D_LineSegment(g, h, oom, rm));
        edges.add(new V2D_LineSegment(h, a, oom, rm));
        ArrayList<V2D_ConvexHull> holes = new ArrayList<>();
        holes.add(new V2D_ConvexHull(oom, rm, a, b, c));
        holes.add(new V2D_ConvexHull(oom, rm, c, d, e));
        holes.add(new V2D_ConvexHull(oom, rm, e, f, g));
        holes.add(new V2D_ConvexHull(oom, rm, g, h, a));
        V2D_Polygon polygon = new V2D_Polygon(ch, edges, holes);
        Polygon p0 = universe.addPolygon(polygon, oom, rm);
    }

    /**
     * The process for rendering and image.
     *
     * @throws Exception
     */
    public void run() {
        int[] pix = render();
        if (drawAxes) {
            axes = new Axes(universe.envelope, oom, rm);
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
        ArrayList<Triangle> ts = universe.triangles;
        for (int i = 0; i < ts.size(); i++) {
            renderTriangle(ts.get(i), pix);
        }
        }
        
        // Render polygons
        if (drawPolygons) {
            ArrayList<Polygon> ts = universe.polygons;
            for (int i = 0; i < ts.size(); i++) {
                renderPolygon(ts.get(i), pix);
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
    public void renderPoint(V2D_Point p, Color c, int[] pix) {
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
    protected int getRow(V2D_Point p) {
        BigRational d = rs.getDistance(p, oom, rm);
        return (d.divide(pixelSize)).intValue();
    }

    /**
     * Calculate and return the column index of the screen that p is on.
     *
     * @param p A point on the screen.
     * @return The column index of the screen for the point p.
     */
    protected int getCol(V2D_Point p) {
        BigRational d = pq.getDistance(p, oom, rm);
        return (d.divide(pixelSize)).intValue();
    }

    /**
     * For rendering a line on the image. Lines may be obscured by other
     * rendered entities. The rendering order determines what is visible.
     *
     * @param l The line to render.
     * @param pix The image.
     */
    public void renderLine(V2D_LineSegment l, Color color, int[] pix) {
        V2D_Point lp = l.getP();
        // Calculate the min and max row and col.
        int minr = getRow(lp);
        int minc = getCol(lp);
        int maxr = minr;
        int maxc = minc;
        V2D_Point lq = l.getQ(oom, rm);
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
                V2D_Rectangle pixel = getPixel(r, c);
                V2D_FiniteGeometry pil = pixel.getIntersection(l, oom, rm);
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
    public void renderTriangle(Triangle triangle, int[] pix) {
        V2D_Triangle t = triangle.triangle;
        V2D_Point tp = t.getP();
        // Circumcircles
        if (drawCircumcircles) {
            V2D_Point circumcentre = t.getCircumcenter(oom, rm);
            BigRational radius = circumcentre.getDistance(tp, oom, rm);
            drawCircle(pix, circumcentre, radius, Color.white);
        }
        // Calculate the min and max row and col.
        int rp = getRow(tp);
        int cp = getCol(tp);
        V2D_Point tq = t.getQ();
        int rq = getRow(tq);
        int cq = getCol(tq);
        V2D_Point tr = t.getR();
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
                V2D_Rectangle pixel = getPixel(r, c);
                V2D_FiniteGeometry pit = pixel.getIntersection(t, oom, rm);
                if (pit != null) {
                    render(pix, r, c, triangle.color);
                    // Edge
                    /**
                     * There is probably a faster way to render the edge by only
                     * getting intersections for more restricted rows and
                     * columns.
                     */
                    // PQ
                    V2D_FiniteGeometry pipq = pixel.getIntersection(t.getPQ(oom, rm), oom, rm);
                    if (pipq != null) {
                        render(pix, r, c, triangle.getColorPQ());
                    }
                    // QR
                    V2D_FiniteGeometry piqr = pixel.getIntersection(t.getQR(oom, rm), oom, rm);
                    if (piqr != null) {
                        render(pix, r, c, triangle.getColorQR());
                    }
                    // RP
                    V2D_FiniteGeometry pirp = pixel.getIntersection(t.getRP(oom, rm), oom, rm);
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
     */
    public void renderPolygon(Polygon polygon, int[] pix) {
        V2D_Polygon poly = polygon.polygon;
        V2D_ConvexHull ch = poly.getConvexHull(oom, rm);
        ArrayList<V2D_LineSegment> edges = poly.getEdges();
        V2D_LineSegment[] edgesArray = new V2D_LineSegment[edges.size()];
        for (int i = 0; i < edgesArray.length; i ++) {
            edgesArray[i] = edges.get(i);
        }
        //ArrayList<V2D_ConvexHullDouble> holes = poly.getHoles(epsilon);
        V2D_Point[] ePs = ch.getPoints(oom, rm);
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
            for (int c = minc; c <= maxc; c++) {
                V2D_Rectangle pixel = getPixel(r, c);
                if (ch.isIntersectedBy(pixel, oom, rm)) {
                    if (poly.isIntersectedBy(pixel, oom, rm)) {
                        render(pix, r, c, polygon.color);
                    } else {
                        if (pixel.isIntersectedBy(oom, rm, edgesArray)) {
                            render(pix, r, c, polygon.colorEdge);
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
    public V2D_Rectangle getPixel(int row, int col) {
        // p
        V2D_Point pP = new V2D_Point(p);
        pP.translate(pqv.multiply(row, oom, rm).add(qrv.multiply(col, oom, rm), oom, rm), oom, rm);
        // q
        V2D_Point pQ = new V2D_Point(p);
        pQ.translate(pqv.multiply(row + 1, oom, rm).add(qrv.multiply(col, oom, rm), oom, rm), oom, rm);
        // r
        V2D_Point pR = new V2D_Point(p);
        pR.translate(pqv.multiply(row + 1, oom, rm).add(qrv.multiply(col + 1, oom, rm), oom, rm), oom, rm);
        // s
        V2D_Point pS = new V2D_Point(p);
        pS.translate(pqv.multiply(row, oom, rm).add(qrv.multiply(col + 1, oom, rm), oom, rm), oom, rm);
        return new V2D_Rectangle(pP, pQ, pR, pS, oom, rm);
    }
    
    /**
     * https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
     * https://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm#Java
     * @param pix The image.
     * @param centre The circle centre.
     * @param radius The circle radius.
     * @param color The colour of the circle.
     */
    private void drawCircle(int[] pix, V2D_Point centre, BigRational radius, 
            Color color) {
        int y = radius.intValue();
        int d = (5 - y * 4) / 4;
        int x = 0;
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
