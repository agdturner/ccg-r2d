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
import uk.ac.leeds.ccg.r2d.d.entities.PolygonDouble;
import uk.ac.leeds.ccg.r2d.d.entities.TriangleDouble;
import uk.ac.leeds.ccg.r2d.grids.Colour_MapDouble;
import uk.ac.leeds.ccg.r2d.io.IO;
import uk.ac.leeds.ccg.stats.range.Stats_RangeDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_ConvexHullDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_FiniteGeometryDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegmentDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PointDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_PolygonDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_RectangleDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_TriangleDouble;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_VectorDouble;

public class RenderImageDouble {

    /**
     * Universe.
     */
    UniverseDouble universe;

    /**
     * The window onto the universe to render.
     */
    V2D_RectangleDouble window;

    /**
     * The window grid/screen.
     */
    Grids_GridDouble grid;

    /**
     * For storing axes.
     */
    AxesDouble axes;

    /**
     * Lower left corner point of screen.
     */
    V2D_PointDouble p;

    /**
     * The pqr of window.
     */
    V2D_TriangleDouble pqr;

    /**
     * The pq of window.
     */
    V2D_LineSegmentDouble pq;

    /**
     * The rsp of window.
     */
    V2D_TriangleDouble rsp;

    /**
     * The rs of window.
     */
    V2D_LineSegmentDouble rs;

    /**
     * The pixel height pq vector of window.
     */
    V2D_VectorDouble pqv;

    /**
     * The pixel width qrv vector of window.
     */
    V2D_VectorDouble qrv;

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
     * If true then polygons are drawn.
     */
    boolean drawPolygons;

    /**
     * Create a new instance.
     *
     * @param universe The universe.
     * @param window The window onto the universe to render.
     */
    public RenderImageDouble(UniverseDouble universe,
            V2D_RectangleDouble window, int nrows, int ncols, double epsilon,
            int oom, RoundingMode rm, boolean drawAxes, Grids_GridDouble grid,
            ArrayList<Colour_MapDouble> gridCMs, boolean drawTriangles,
            boolean drawCircumcircles, boolean drawPolygons) {
        this.universe = universe;
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
        this.drawPolygons = drawPolygons;
    }

    /**
     * Edit to add/remove grid background and render different triangles.
     */
    public static void main(String[] args) {
        Path inDataDir = Paths.get("data", "input");
        Path outDataDir = Paths.get("data", "output", "d");
        Path dir = Paths.get(outDataDir.toString(), "test");
        //boolean drawAxes = true;
        boolean drawAxes = false;
        int oom = -6;
        RoundingMode rm = RoundingMode.HALF_UP;
        //double epsilon = 1d / 10000000d;
        //double epsilon = 1d / 100000000000d;
        double epsilon = 1d / 10000d;
        //double epsilon = 0d;
        int nrows = 150; //150;
        int ncols = 150; //150;
        // Init universe
        long m = 75;         // multiplication factor
        V2D_VectorDouble offset = V2D_VectorDouble.ZERO;
        V2D_PointDouble lb = new V2D_PointDouble(offset, new V2D_VectorDouble(-1 * m, -1 * m));
        V2D_PointDouble lt = new V2D_PointDouble(offset, new V2D_VectorDouble(-1 * m, 1 * m));
        V2D_PointDouble rt = new V2D_PointDouble(offset, new V2D_VectorDouble(1 * m, 1 * m));
        V2D_PointDouble rb = new V2D_PointDouble(offset, new V2D_VectorDouble(1 * m, -1 * m));
        V2D_RectangleDouble window = new V2D_RectangleDouble(lb, lt, rt, rb);
        UniverseDouble universe = new UniverseDouble(window.getEnvelope());
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
        int tt = 2;
        switch (tt) {
            case 0 ->
                addTriangles0(universe, epsilon);
            case 1 ->
                addTriangles1(universe, epsilon);
            case 2 ->
                addTriangles2(universe, epsilon);
            case 3 ->
                addTriangles3(universe, epsilon);
            case 4 ->
                addTriangles4(universe, epsilon);
            case 5 ->
                addTriangles5(universe, epsilon);
            case 6 ->
                addTriangles6(universe, epsilon);
            case 7 ->
                addTriangles7(universe, epsilon);
        }
        // Add polygons
        boolean drawPolygons = true;
        //boolean drawPolygons = false;
        int pp = 2;
        switch (pp) {
            case 0 ->
                addPolygons0(universe, epsilon);
            case 1 ->
                addPolygons1(universe, epsilon);
            case 2 ->
                addPolygons2(universe, epsilon);
        }

        // Draw circumcircles
        //boolean drawCircumcircles = false;
        boolean drawCircumcircles = true;
        // Render
        RenderImageDouble ri = new RenderImageDouble(universe, window, nrows,
                ncols, epsilon, oom, rm, drawAxes, grid, gridCMs, drawTriangles,
                drawCircumcircles, drawPolygons);
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

    public static Colour_MapDouble addGrid1(Grids_GridDoubleFactory gdf,
            UniverseDouble universe, int nrows, int ncols) {
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

    public static Colour_MapDouble addGrid2(Grids_GridDoubleFactory gdf, UniverseDouble universe, int nrows, int ncols) {
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
    public static void addTriangles0(UniverseDouble universe, double epsilon) {
        Color color = Color.gray;
        Color colorEdge = Color.blue;
        V2D_PointDouble p = new V2D_PointDouble(-50d, -50d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 50d);
        V2D_PointDouble r = new V2D_PointDouble(50d, -50d);
        V2D_TriangleDouble t0 = new V2D_TriangleDouble(p, q, r);
        //universe.addTriangle(t0);
        universe.addTriangle(t0, color, colorEdge);
    }

    /**
     * Three rotated overlapping large triangles.
     */
    public static void addTriangles1(UniverseDouble universe, double epsilon) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        // 0
        V2D_PointDouble p = new V2D_PointDouble(-50d, -50d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 50d);
        V2D_PointDouble r = new V2D_PointDouble(50d, -50d);
        V2D_TriangleDouble t0 = new V2D_TriangleDouble(p, q, r);
        //universe.addTriangle(t0);
        universe.addTriangle(t0, color, colorPQ, colorQR, colorRP);
        double theta;
        V2D_PointDouble origin = new V2D_PointDouble(0d, 0d);
        // 1
        theta = Math.PI;
        V2D_TriangleDouble t1 = t0.rotate(origin, theta, epsilon);
        //universe.addTriangle(t1);
        universe.addTriangle(t1, color, colorPQ, colorQR, colorRP);
        // 2
        theta = Math.PI / 2d;
        V2D_TriangleDouble t2 = t0.rotate(p, theta, epsilon);
        //universe.addTriangle(t2);
        universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
        // 3
        theta = 3d * Math.PI / 2d;
        V2D_TriangleDouble t3 = t0.rotate(origin, theta, epsilon);
        //universe.addTriangle(t3);
        universe.addTriangle(t3, color, colorPQ, colorQR, colorRP);
    }

    /**
     * Multiple small rotated triangles some overlapping.
     */
    public static void addTriangles2(UniverseDouble universe, double epsilon) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        // 0
        V2D_PointDouble p = new V2D_PointDouble(-10d, -10d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 10d);
        V2D_PointDouble r = new V2D_PointDouble(10d, -10d);
        V2D_TriangleDouble t0 = new V2D_TriangleDouble(p, q, r);
        universe.addTriangle(t0, color, colorPQ, colorQR, colorRP);
        double theta;
        V2D_PointDouble origin = new V2D_PointDouble(0d, 0d);
        // 1
        theta = Math.PI;
        V2D_TriangleDouble t1 = t0.rotate(origin, theta, epsilon);
        universe.addTriangle(t1, color, colorPQ, colorQR, colorRP);
        // 2
        theta = Math.PI / 2d;
        V2D_TriangleDouble t2 = t0.rotate(p, theta, epsilon);
        universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
        // 3
        theta = 3d * Math.PI / 2d;
        V2D_TriangleDouble t3 = t2.rotate(origin, theta, epsilon);
        universe.addTriangle(t3, color, colorPQ, colorQR, colorRP);
        // 4
        theta = Math.PI / 2d;
        V2D_TriangleDouble t4 = t2.rotate(t2.getR(), theta, epsilon);
        universe.addTriangle(t4, color, colorPQ, colorQR, colorRP);
        // 5
        V2D_TriangleDouble t5 = t4.rotate(t4.getR(), theta, epsilon);
        universe.addTriangle(t5, color, colorPQ, colorQR, colorRP);
    }

    /**
     * Triangle rotated 48 times with increasing angle.
     */
    public static void addTriangles3(UniverseDouble universe, double epsilon) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        V2D_PointDouble p = new V2D_PointDouble(-20d, -20d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 20d);
        V2D_PointDouble r = new V2D_PointDouble(20d, -20d);
        V2D_TriangleDouble t0 = new V2D_TriangleDouble(p, q, r);
        universe.addTriangle(t0);
        V2D_TriangleDouble t1 = t0;
        V2D_TriangleDouble t2 = null;
        double theta = Math.PI / 12d;
        for (int i = 1; i <= 48; i++) {
            t2 = t1.rotate(t0.getR(), theta * (double) i, epsilon);
            universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
            t1 = t2;
        }
        universe.addTriangle(t2);
    }

    /**
     * Triangle rotated a bit, then the result rotated a bit 48 times.
     */
    public static void addTriangles4(UniverseDouble universe, double epsilon) {
        Color color = Color.gray;
        //Color colorEdge = Color.blue;
        Color colorPQ = Color.blue;
        Color colorQR = Color.green;
        Color colorRP = Color.red;
        V2D_PointDouble p = new V2D_PointDouble(-20d, -20d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 20d);
        V2D_PointDouble r = new V2D_PointDouble(20d, -20d);
        V2D_TriangleDouble t0 = new V2D_TriangleDouble(p, q, r);
        universe.addTriangle(t0);
        V2D_TriangleDouble t1 = t0;
        V2D_TriangleDouble t2 = null;
        double theta = Math.PI / 12d;
        for (int i = 1; i <= 48; i++) {
            t2 = t1.rotate(t0.getR(), theta, epsilon);
            universe.addTriangle(t2, color, colorPQ, colorQR, colorRP);
            t0 = t1;
            t1 = t2;
        }
        universe.addTriangle(t2);
    }

    /**
     * Apply lots of rotations to rotate a triangle back to original position.
     */
    public static void addTriangles5(UniverseDouble universe, double epsilon) {
        V2D_PointDouble p = new V2D_PointDouble(-20d, -20d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 20d);
        V2D_PointDouble r = new V2D_PointDouble(20d, -20d);
        V2D_TriangleDouble t0 = new V2D_TriangleDouble(p, q, r);
        universe.addTriangle(t0);
        V2D_TriangleDouble t1 = t0;
        V2D_TriangleDouble t2 = null;
        double n = 1000d;
        //double n = 10000000d;
        double theta = Math.PI / (12d * n);
        for (int i = 1; i <= 48 * n; i++) {
            t2 = t1.rotate(t0.getR(), theta, epsilon);
            //universe.addTriangle(t2);
            t0 = t1;
            t1 = t2;
        }
        universe.addTriangle(t2);
    }

    /**
     * Adds two triangles and interects these adding the triangular intersecting
     * parts that form a diamond.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addTriangles6(UniverseDouble universe, double epsilon) {
        // 0
        V2D_PointDouble p = new V2D_PointDouble(-50d, -50d);
        V2D_PointDouble q = new V2D_PointDouble(0d, 50d);
        V2D_PointDouble r = new V2D_PointDouble(50d, -50d);
        TriangleDouble t0 = universe.addTriangle(new V2D_TriangleDouble(p, q, r));
        double theta;
        V2D_PointDouble origin = new V2D_PointDouble(0d, 0d);
        // 1
        //theta = Math.PI;
        theta = Math.PI / 2d;
        TriangleDouble t1 = universe.addTriangle(t0.triangle.rotate(origin, theta, epsilon));
        // Calculate the intersection
        V2D_FiniteGeometryDouble gi = t0.triangle.getIntersection(t1.triangle, epsilon);
        ArrayList<V2D_TriangleDouble> git = ((V2D_ConvexHullDouble) gi).getTriangles();
        for (int i = 0; i < git.size(); i++) {
            TriangleDouble t = universe.addTriangle(git.get(i));
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
    public static void addTriangles7(UniverseDouble universe, double epsilon) {
        V2D_PointDouble origin = new V2D_PointDouble(0d, 0d);
        TriangleDouble t0 = universe.addTriangle(new V2D_TriangleDouble(
                new V2D_PointDouble(-30d, -30d),
                new V2D_PointDouble(0d, 60d),
                new V2D_PointDouble(30d, -30d)));
        double theta = Math.PI;
        TriangleDouble t1 = universe.addTriangle(t0.triangle.rotate(origin, theta, epsilon));
        // Calculate the intersection
        V2D_FiniteGeometryDouble gi = t0.triangle.getIntersection(t1.triangle, epsilon);
        ArrayList<V2D_TriangleDouble> git = ((V2D_ConvexHullDouble) gi).getTriangles();
        for (int i = 0; i < git.size(); i++) {
            TriangleDouble t = universe.addTriangle(git.get(i));
            t.setColor(Color.yellow);
        }
    }

    /**
     * One polygon that is not a convex hull.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons0(UniverseDouble universe, double epsilon) {
        V2D_PointDouble a = new V2D_PointDouble(-30d, -30d);
        V2D_PointDouble b = new V2D_PointDouble(-20d, 0d);
        V2D_PointDouble c = new V2D_PointDouble(-30d, 30d);
        V2D_PointDouble d = new V2D_PointDouble(0d, 20d);
        V2D_PointDouble e = new V2D_PointDouble(30d, 30d);
        V2D_PointDouble f = new V2D_PointDouble(20d, 0d);
        V2D_PointDouble g = new V2D_PointDouble(30d, -30d);
        V2D_PointDouble h = new V2D_PointDouble(0d, -20d);
        V2D_ConvexHullDouble ch = new V2D_ConvexHullDouble(epsilon,
                a, b, c, d, e, f, g, h);
        ArrayList<V2D_LineSegmentDouble> edges = new ArrayList<>();
        edges.add(new V2D_LineSegmentDouble(a, b));
        edges.add(new V2D_LineSegmentDouble(b, c));
        edges.add(new V2D_LineSegmentDouble(c, d));
        edges.add(new V2D_LineSegmentDouble(d, e));
        edges.add(new V2D_LineSegmentDouble(e, f));
        edges.add(new V2D_LineSegmentDouble(f, g));
        edges.add(new V2D_LineSegmentDouble(g, h));
        edges.add(new V2D_LineSegmentDouble(h, a));
        ArrayList<V2D_PolygonDouble> holes = new ArrayList<>();
        holes.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, a, b, c)));
        holes.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, c, d, e)));
        holes.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, e, f, g)));
        holes.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, g, h, a)));
        V2D_PolygonDouble polygon = new V2D_PolygonDouble(ch, edges, holes);
        PolygonDouble p0 = universe.addPolygon(polygon);
    }

    /**
     * One polygon that is not a convex hull and that has other holes.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons1(UniverseDouble universe, double epsilon) {
        // Outer points
        V2D_PointDouble a = new V2D_PointDouble(-48d, -48d);
        V2D_PointDouble b = new V2D_PointDouble(-48d, 48d);
        V2D_PointDouble c = new V2D_PointDouble(0d, 36d);
        V2D_PointDouble d = new V2D_PointDouble(48d, 48d);
        V2D_PointDouble e = new V2D_PointDouble(48d, -48d);
        V2D_ConvexHullDouble ch = new V2D_ConvexHullDouble(epsilon,
                a, b, c, d, e);
        // Holes
        ArrayList<V2D_PolygonDouble> holes = new ArrayList<>();
        // Outer holes
        holes.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, b, c, d)));
        // Internal hole
        // Outer points of inner hole
        V2D_PointDouble f = new V2D_PointDouble(-24d, -24d);
        V2D_PointDouble g = new V2D_PointDouble(-24d, 24d);
        V2D_PointDouble h = new V2D_PointDouble(-16d, 24d);
        V2D_PointDouble i = new V2D_PointDouble(-8d, -2d);
        V2D_PointDouble j = new V2D_PointDouble(8d, -2d);
        V2D_PointDouble k = new V2D_PointDouble(16d, 24d);
        V2D_PointDouble l = new V2D_PointDouble(24d, 24d);
        V2D_PointDouble m = new V2D_PointDouble(24d, -24d);
        // Holes of inner hole
        ArrayList<V2D_PolygonDouble> holesOfHole = new ArrayList<>();
        holesOfHole.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, h, i, j, k)));
        V2D_PolygonDouble hole = new V2D_PolygonDouble(
                new V2D_ConvexHullDouble(epsilon, f, g, h, i, j, k, l, m), holesOfHole);
        holes.add(hole);
        // Edges
        // Outer edge
        ArrayList<V2D_LineSegmentDouble> edges = new ArrayList<>();
        edges.add(new V2D_LineSegmentDouble(a, b));
        edges.add(new V2D_LineSegmentDouble(b, c));
        edges.add(new V2D_LineSegmentDouble(c, d));
        edges.add(new V2D_LineSegmentDouble(d, e));
        edges.add(new V2D_LineSegmentDouble(e, a));
        // Inner edge
        edges.add(new V2D_LineSegmentDouble(f, g));
        edges.add(new V2D_LineSegmentDouble(g, h));
        edges.add(new V2D_LineSegmentDouble(h, i));
        edges.add(new V2D_LineSegmentDouble(i, j));
        edges.add(new V2D_LineSegmentDouble(j, k));
        edges.add(new V2D_LineSegmentDouble(k, l));
        edges.add(new V2D_LineSegmentDouble(l, m));
        edges.add(new V2D_LineSegmentDouble(m, f));
        V2D_PolygonDouble polygon = new V2D_PolygonDouble(ch, edges, holes);
        universe.addPolygon(polygon);
    }

    /**
     * One polygon that is not a convex hull and that has other holes.
     *
     * @param universe
     * @param epsilon
     * @return The ids of the original triangles that are intersected.
     */
    public static void addPolygons2(UniverseDouble universe, double epsilon) {
        // Outer points
        V2D_PointDouble a = new V2D_PointDouble(-30d, -30d);
        V2D_PointDouble b = new V2D_PointDouble(-20d, 0d);
        V2D_PointDouble c = new V2D_PointDouble(-30d, 30d);
        V2D_PointDouble d = new V2D_PointDouble(0d, 20d);
        V2D_PointDouble e = new V2D_PointDouble(30d, 30d);
        V2D_PointDouble f = new V2D_PointDouble(20d, 0d);
        V2D_PointDouble g = new V2D_PointDouble(30d, -30d);
        V2D_PointDouble h = new V2D_PointDouble(0d, -20d);
        V2D_ConvexHullDouble ch = new V2D_ConvexHullDouble(epsilon,
                a, b, c, d, e, f, g, h);
        // Holes
        ArrayList<V2D_PolygonDouble> externalHoles = new ArrayList<>();
        // Outer holes
        externalHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, a, b, c)));
        externalHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, c, d, e)));
        externalHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, e, f, g)));
        externalHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, g, h, a)));
        externalHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, g, h, a)));
        // Internal holes
        ArrayList<V2D_PolygonDouble> internalHoles = new ArrayList<>();
        // Outer points of inner hole
        V2D_PointDouble i = new V2D_PointDouble(-10d, 10d);
        V2D_PointDouble j = new V2D_PointDouble(3d, 2d);
        V2D_PointDouble k = new V2D_PointDouble(15d, 15d);
        V2D_PointDouble l = new V2D_PointDouble(12d, -13d);
        V2D_PointDouble m = new V2D_PointDouble(-6d, -4d);
        V2D_PointDouble n = new V2D_PointDouble(-10d, -15d);
        // Holes of inner hole
        ArrayList<V2D_PolygonDouble> holeHoles = new ArrayList<>();
        holeHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, i, j, k)));
        holeHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, l, m, n)));
        internalHoles.add(new V2D_PolygonDouble(new V2D_ConvexHullDouble(epsilon, i, j, k, l, m, n), holeHoles));
        // Edges
        // Outer edge
        ArrayList<V2D_LineSegmentDouble> externalEdges = new ArrayList<>();
        externalEdges.add(new V2D_LineSegmentDouble(a, b));
        externalEdges.add(new V2D_LineSegmentDouble(b, c));
        externalEdges.add(new V2D_LineSegmentDouble(c, d));
        externalEdges.add(new V2D_LineSegmentDouble(d, e));
        externalEdges.add(new V2D_LineSegmentDouble(e, f));
        externalEdges.add(new V2D_LineSegmentDouble(f, g));
        externalEdges.add(new V2D_LineSegmentDouble(g, h));
        externalEdges.add(new V2D_LineSegmentDouble(h, a));
        // Inner edge
        ArrayList<V2D_LineSegmentDouble> internalEdges = new ArrayList<>();
        internalEdges.add(new V2D_LineSegmentDouble(i, j));
        internalEdges.add(new V2D_LineSegmentDouble(j, k));
        internalEdges.add(new V2D_LineSegmentDouble(k, l));
        internalEdges.add(new V2D_LineSegmentDouble(l, m));
        internalEdges.add(new V2D_LineSegmentDouble(m, n));
        internalEdges.add(new V2D_LineSegmentDouble(n, i));
        V2D_PolygonDouble polygon = new V2D_PolygonDouble(ch, externalEdges, 
                externalHoles, internalEdges, internalHoles);
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
            axes = new AxesDouble(universe.envelope);
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
            ArrayList<TriangleDouble> ts = universe.triangles;
            for (int i = 0; i < ts.size(); i++) {
                renderTriangle(ts.get(i), pix);
            }
        }

        // Render polygons
        if (drawPolygons) {
            ArrayList<PolygonDouble> ts = universe.polygons;
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
    public void renderPoint(V2D_PointDouble p, Color c, int[] pix) {
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
    protected int getRow(V2D_PointDouble p) {
        BigRational d = BigRational.valueOf(rs.getDistance(p, epsilon));
        return (d.divide(pixelSize)).intValue();
    }

    /**
     * Calculate and return the column index of the screen that p is on.
     *
     * @param p A point on the screen.
     * @return The column index of the screen for the point p.
     */
    protected int getCol(V2D_PointDouble p) {
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
    public void renderLine(V2D_LineSegmentDouble l, Color color, int[] pix) {
        V2D_PointDouble lp = l.getP();
        // Calculate the min and max row and col.
        int minr = getRow(lp);
        int minc = getCol(lp);
        int maxr = minr;
        int maxc = minc;
        V2D_PointDouble lq = l.getQ();
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
                V2D_RectangleDouble pixel = getPixel(r, c);
                V2D_FiniteGeometryDouble pil = pixel.getIntersection(l, epsilon);
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
    public void renderTriangle(TriangleDouble triangle, int[] pix) {
        V2D_TriangleDouble t = triangle.triangle;
        // Circumcircles
        if (drawCircumcircles) {
            V2D_PointDouble circumcentre = t.getCircumcenter();
            double radius = circumcentre.getDistance(t.getP());
            drawCircle(pix, circumcentre, radius, Color.white);
        }
        V2D_PointDouble tp = t.getP();
        // Calculate the min and max row and col.
        int rp = getRow(tp);
        int cp = getCol(tp);
        V2D_PointDouble tq = t.getQ();
        int rq = getRow(tq);
        int cq = getCol(tq);
        V2D_PointDouble tr = t.getR();
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
                V2D_RectangleDouble pixel = getPixel(r, c);
                V2D_FiniteGeometryDouble pit = pixel.getIntersection(t, epsilon);
                if (pit != null) {
                    render(pix, r, c, triangle.color);
                    // Edge
                    /**
                     * There is probably a faster way to render the edge by only
                     * getting intersections for more restricted rows and
                     * columns.
                     */
                    // PQ
                    V2D_FiniteGeometryDouble pipq = pixel.getIntersection(t.getPQ(), epsilon);
                    if (pipq != null) {
                        render(pix, r, c, triangle.getColorPQ());
                    }
                    // QR
                    V2D_FiniteGeometryDouble piqr = pixel.getIntersection(t.getQR(), epsilon);
                    if (piqr != null) {
                        render(pix, r, c, triangle.getColorQR());
                    }
                    // RP
                    V2D_FiniteGeometryDouble pirp = pixel.getIntersection(t.getRP(), epsilon);
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
    public void renderPolygon(PolygonDouble polygon, int[] pix) {
        V2D_PolygonDouble poly = polygon.polygon;
        V2D_ConvexHullDouble ch = poly.getConvexHull(epsilon);
        ArrayList<V2D_LineSegmentDouble> externalEdges = poly.getExternalEdges();
        V2D_LineSegmentDouble[] externalEdgesArray = new V2D_LineSegmentDouble[externalEdges.size()];
        for (int i = 0; i < externalEdgesArray.length; i++) {
            externalEdgesArray[i] = externalEdges.get(i);
        }
        ArrayList<V2D_LineSegmentDouble> internalEdges = poly.getInternalEdges();
        V2D_LineSegmentDouble[] internalEdgesArray = new V2D_LineSegmentDouble[internalEdges.size()];
        for (int i = 0; i < internalEdgesArray.length; i++) {
            internalEdgesArray[i] = internalEdges.get(i);
        }
        V2D_PointDouble[] ePs = ch.getPoints();
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

            if (r == ((maxr - minr) / 2) + minr) {
                int debug = 1;
            }

            for (int c = minc; c <= maxc; c++) {

                if (c == ((maxc - minc) / 2) + minc) {
                    int debug = 1;
                }

                V2D_RectangleDouble pixel = getPixel(r, c);
                if (ch.isIntersectedBy(pixel, epsilon)) {
                    if (poly.isIntersectedBy(pixel, epsilon)) {
                        render(pix, r, c, polygon.color);
                    }
                    if (pixel.isIntersectedBy(epsilon, internalEdgesArray)) {
                        render(pix, r, c, polygon.getColorInternalEdge());
                    }
                    if (pixel.isIntersectedBy(epsilon, externalEdgesArray)) {
                        render(pix, r, c, polygon.getColorExternalEdge());
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
    public V2D_RectangleDouble getPixel(int row, int col) {
        // p
        V2D_PointDouble pP = new V2D_PointDouble(p);
        pP.translate(pqv.multiply(row).add(qrv.multiply(col)));
        // q
        V2D_PointDouble pQ = new V2D_PointDouble(p);
        pQ.translate(pqv.multiply(row + 1).add(qrv.multiply(col)));
        // r
        V2D_PointDouble pR = new V2D_PointDouble(p);
        pR.translate(pqv.multiply(row + 1).add(qrv.multiply(col + 1)));
        // s
        V2D_PointDouble pS = new V2D_PointDouble(p);
        pS.translate(pqv.multiply(row).add(qrv.multiply(col + 1)));
        return new V2D_RectangleDouble(pP, pQ, pR, pS);
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
    private void drawCircle(int[] pix, V2D_PointDouble centre, double radius,
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
