# [ccg-r2d](https://github.com/agdturner/ccg-r2d)
A Java library for rendering 2D Euclidean spatial data using [ccg-v2d](https://github.com/agdturner/ccg-v2d) for vector (point, line, polygon) data and [ccg-grids](https://github.com/agdturner/ccg-grids) for raster data.

Visualising geometry helps develop [ccg-v2d](https://github.com/agdturner/ccg-v2d).  This 2D Euclidean geometry helps develop 3D geometry libraries: [ccg-v3d](https://github.com/agdturner/ccg-v3d) and [ccg-r3d](https://github.com/agdturner/ccg-r3d). Some example static renderings of data are shown below.

[GSHHS](https://www.ngdc.noaa.gov/mgg/shorelines/data/gshhg/latest/) low resolution land/sea polygons, 825 x 2000:

<img alt="GSHHS c Global 825 x 2000" src="data/output/test/gshhs_g_polygons3_nrows825_ncols2000.png" />

## Triangle rotations, colours and circumcircles

Three rotated overlapping large triangles (one colour for the triangle and a different colour for each respective edge):

<img alt="Three rotated overlapping large triangles" src="data/output/test/test1.png" />

Multiple small rotated triangles some overlapping with their circumcircle outlines drawn in white (one colour for all triangles and a different colour for each respective edge:

<img alt="Multiple small rotated triangles some overlapping with their circumcircle outlines drawn in white" src="data/output/test/test2.png" />

Triangle rotated 48 times with increasing angle (the last triangle draw has just one colour for it's edge):

<img alt="Triangle rotated 48 times with increasing angle" src="data/output/test/test3.png" />

Triangle rotated a bit, then the result rotated a bit - 48 times (the last triangle has just one colour for it's edge):

<img alt="Triangle rotated a bit, then the result rotated a bit - 48 times" src="data/output/test/test4.png" />

## With grids

A triangle on a couple of randomly coloured grids (one grid underlies the other, the top grid is located in the upper right of the image):

<img alt="A triangle on a couple of randomly coloured grids" src="data/output/test/test0_grid.png" />

## Triangle intersections

Two rotated triangles with a two triangle intersection:

<img alt="A first rendering of a triangle on a couple of randomly coloured grids" src="data/output/test/test6.png" />

Two rotated triangles with a four triangle intersection:

<img alt="Two rotated triangles with a four triangle intersection" src="data/output/test/test7.png" />

## Polygon

Polygon with a polygon internal hole. The external edge of the polygon is coloured red and the edge of the hole is coloured blue:

<img alt="Polygon that is not a convex hull with a darker outline and with a polygonal hole with the external edge coloured red and the internal edge coloured blue" src="data/output/test/test_polygons2_nrows150_ncols150.png" />

### [GSHHS](https://www.ngdc.noaa.gov/mgg/shorelines/data/gshhg/latest/) low resolution land/sea polygons

#### Global

Heirarchy ignored revealing the convex hulls with darker shade for edges:

<img alt="GSHHS c global with convex hull darker gray edges" src="data/output/test/gshhs_g_polygons3_ch.png" />

Without some of antarctica revealing holes:

165 x 400:

<img alt="GSHHS c Global 165 x 400" src="data/output/test/gshhs_g_polygons3_nrows165_ncols400.png" />

660 x 1600:

<img alt="GSHHS c Global 660 x 1600" src="data/output/test/gshhs_g_polygons3_nrows660_ncols1600.png" />

825 x 2000:

<img alt="GSHHS c Global 825 x 2000" src="data/output/test/gshhs_g_polygons3_nrows825_ncols2000.png" />

#### Great Britain

15 x 14:

<img alt="GSHHS c Great Britain 15 x 14" src="data/output/test/gshhs_gb_polygons3_nrows15_ncols14.png" />

30 x 28:

<img alt="GSHHS c Great Britain 30 x 28" src="data/output/test/gshhs_gb_polygons3_nrows30_ncols28.png" />

45 x 42:

<img alt="GSHHS c Great Britain 45 x 42" src="data/output/test/gshhs_gb_polygons3_nrows45_ncols42.png" />

60 x 56:

<img alt="GSHHS c Great Britain 60 x 56" src="data/output/test/gshhs_gb_polygons3_nrows60_ncols56.png" />

75 x 70:

<img alt="GSHHS c Great Britain 75 x 70" src="data/output/test/gshhs_gb_polygons3_nrows75_ncols70.png" />

90 x 84:

<img alt="GSHHS c Great Britain 90 x 84" src="data/output/test/gshhs_gb_polygons3_nrows90_ncols84.png" />

105 x 98:

<img alt="GSHHS c Great Britain 105 x 98" src="data/output/test/gshhs_gb_polygons3_nrows105_ncols98.png" />

120 x 112:

<img alt="GSHHS c Great Britain 120 x 112" src="data/output/test/gshhs_gb_polygons3_nrows120_ncols112.png" />

135 x 126:

<img alt="GSHHS c Great Britain 135 x 126" src="data/output/test/gshhs_gb_polygons3_nrows135_ncols126.png" />

150 x 140:

<img alt="GSHHS c Great Britain 150 x 140" src="data/output/test/gshhs_gb_polygons3_nrows150_ncols140.png" />

## Dependencies
- [Java SE 21](https://en.wikipedia.org/wiki/Java_version_history#Java_SE_21)
- Mainly [ccg-v2d](https://github.com/agdturner/ccg-v2d) for vectors and [ccg-grids](https://github.com/agdturner/ccg-grids) for rasters. Both of thesehave few light weight dependencies.
- Please see the [POM](https://github.com/agdturner/ccg-r2d/blob/master/pom.xml) for details.

## Development plans/ideas
- Calculate and show some example polygon-polygon intersections.
- Create some animations with geometries moving relative to others. 
- Generate some more example renderings of geographical data:
  - [Global Self-Consistent Hierarchical High-Resolution Shoreline (GSHHS) data](https://www.ngdc.noaa.gov/mgg/shorelines/data/gshhg/latest/)
    - [GSHHG-GMT](https://github.com/GenericMappingTools/gshhg-gmt)
  - Surface elevation
  - Land cover
  - Temperature
  - Rainfall
  - National boundaries and human population
- Make a versioned release on Maven Central.
- Investigate ways to speed up rendering.
- Community development:
  - Raise awareness
  - Develop use cases
  - Reach out to developers of ([GMT](https://github.com/GenericMappingTools/gmt), [Apache SIS](https://github.com/apache/sis), [JTS](https://github.com/locationtech/jts)).

## Contributing
- Thanks for thinking about this.
- For a collaborative project, there should be a Code of Conduct and Contributor Guide based on something like this: [Open Source Guide](https://opensource.guide/)
- Meanwhile if you want instructions or there is an issue, please report this in the usual way :)

## LICENCE
- [APACHE LICENSE, VERSION 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- Other licences are possible!

## Acknowledgements and thanks
- The [University of Leeds](http://www.leeds.ac.uk) has indirectly supported this work by employing me over the years and encouraging me to develop the skills necessary to produce this library.
- Thank you Eric for the [BigMath](https://github.com/eobermuhlner/big-math) library.
