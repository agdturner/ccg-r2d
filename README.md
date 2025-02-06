# [ccg-r2d](https://github.com/agdturner/ccg-r2d)
A Java library for rendering 2D Euclidean spatial data using [ccg-v2d](https://github.com/agdturner/ccg-v2d) for vectors and [ccg-grids](https://github.com/agdturner/ccg-grids) for rasters.

Example static renderings of data are shown below.

This library is being used to help develop [ccg-v2d](https://github.com/agdturner/ccg-v2d). Visualising things like calculated intersections helps to show that geometrical algorithms work.

Developing 2D spatial data functionaility also helps with developing 3D spatial data functionality. If you are interested in processing 3D Euclidean spatial data then maybe also have a look at: [ccg-v3d](https://github.com/agdturner/ccg-v3d) and [ccg-r3d](https://github.com/agdturner/ccg-r3d).

## Triangle rotations, colours and circumcircles
* One triangle (one colour for the triangle and another for it's edge):
<img alt="One triangle" src="data/output/test/test0.png" />
* Three rotated overlapping large triangles (one colour for the triangle and a different colour for each respective edge):
<img alt="Three rotated overlapping large triangles" src="data/output/test/test1.png" />
* Multiple small rotated triangles some overlapping with their circumcircle outlines drawn in white (one colour for all triangles and generally a different colour for each respective edge):
<img alt="Multiple small rotated triangles some overlapping with their circumcircle outlines drawn in white" src="data/output/test/test2.png" />
* Triangle rotated 48 times with increasing angle (the last triangle draw has just one colour for it's edge):
<img alt="Triangle rotated 48 times with increasing angle" src="data/output/test/test3.png" />
* Triangle rotated a bit, then the result rotated a bit 48 times (the last triangle has just one colour for it's edge):
<img alt="Triangle rotated a bit, then the result rotated a bit 48 times" src="data/output/test/test4.png" />

## With grids
* A triangle on a couple of randomly coloured grids:
<img alt="A triangle on a couple of randomly coloured grids" src="data/output/test/test0_grid.png" />

## Triangle intersections
* Two rotated triangles with a two triangle intersection
<img alt="A first rendering of a triangle on a couple of randomly coloured grids" src="data/output/test/test6.png" />
Two rotated triangles with a four triangle intersection:
<img alt="Two rotated triangles with a four triangle intersection" src="data/output/test/test7.png" />

## Polygon
* Polygon that is not a convex hull with a darker outline
<img alt="Polygon that is not a convex hull with a darker outline" src="data/output/test/test_polygons0.png" />
* Polygon that is not a convex hull with a darker outline and with a polygonal hole
<img alt="Polygon that is not a convex hull with a darker outline" src="data/output/test/test_polygons2.png" />
  
## Development plans/ideas
- Add classes for polygons. 
- Add functionality for cutting/clipping.
- Add functionality for merging.
- Generate some example renderings of geographical data.
- Make a versioned release on Maven Central.
- Investigate ways to speed up rendering.

## Development history
- The code was originally developed for academic research projects.

## Contributions welcome
- Please submit issues and initiate discussions about collaboration to help develop the code.

## LICENSE
- [APACHE LICENSE, VERSION 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Acknowledgements and thanks
- The [University of Leeds](http://www.leeds.ac.uk) and externally funded research grants have supported the development of this library.
- Thank you Eric for the [BigMath](https://github.com/eobermuhlner/big-math) library.
