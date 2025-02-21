# [ccg-r2d](https://github.com/agdturner/ccg-r2d)
A Java library for rendering 2D Euclidean spatial data using [ccg-v2d](https://github.com/agdturner/ccg-v2d) for vectors and [ccg-grids](https://github.com/agdturner/ccg-grids) for rasters.

This library is being used to help develop [ccg-v2d](https://github.com/agdturner/ccg-v2d). Visualising inputs and outputs of geometrical operationa helps to show that algorithms work. The development of the 2D spatial data libraries helps develop these 3D spatial data libraries: [ccg-v3d](https://github.com/agdturner/ccg-v3d) and [ccg-r3d](https://github.com/agdturner/ccg-r3d). Some example static renderings of data are shown below.

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

Polygon that is not a convex hull with a darker outline. The external holes (parts between the convex hull and the polygon edge are all convex hulls):

<img alt="Polygon that is not a convex hull with a darker outline" src="data/output/test/test_polygons0.png" />

Polygon that is not a convex hull with an external hole that is not a convex hull, but a polygon:

<img alt="Polygon that is not a convex hull with a darker outline" src="data/output/test/test_polygons1.png" />

Polygon with a polygon internal hole. The external edge of the polygon is coloured red and the edge of the hole is coloured blue (Currently there is an issue with this as there are some pixels coloured grey on the wron side of the blue!):

<img alt="Polygon that is not a convex hull with a darker outline and with a polygonal hole with the external edge coloured red and the internal edge coloured blue" src="data/output/test/test_polygons2.png" />

GSHHS low resolution land/sea polygons. The heirarchy is ignored. The image shows lighter for the convex hull and darker for the external edges:

<img alt="GSHHS with light gray for the convex hull and darker gray for the external edges" src="data/output/test/test_polygons3_ch.png" />

GSHHS low resolution land/sea polygons. (This is shifted a bit compared with the image showing the convex hulls):

<img alt="GSHHS with light gray for the convex hull and darker gray for the external edges" src="data/output/test/test_polygons3_nrows165_ncols400.png" />

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
  - Reach out to GeoSpatial librabry developers ([Apache SIS](https://github.com/apache/sis), [JTS](https://github.com/locationtech/jts), [GeoTools](https://github.com/geotools/geotools)).

## Contributing
- Thanks for thinking about this.
- If this is to form into a collaborative project, it could do with a code of conduct and contributor guidelines based on something like this: [Open Source Guide](https://opensource.guide/) 

## LICENCE
- [APACHE LICENSE, VERSION 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- If you want this licensed differently, then this could be considered...

## Acknowledgements and thanks
- The [University of Leeds](http://www.leeds.ac.uk) is my employer and I have used work time to help develop some of the code in this repository and in libraries dependencies.
- Thank you Eric for the [BigMath](https://github.com/eobermuhlner/big-math) library.
