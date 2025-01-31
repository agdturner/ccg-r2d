# [ccg-r2d](https://github.com/agdturner/ccg-r2d)
A Java library for rendering 2D Euclidean spatial data using [ccg-v2d](https://github.com/agdturner/ccg-v2d) for vectors and [ccg-grids](https://github.com/agdturner/ccg-grids) for rasters.

Example static renderings of data are shown below.

This library is being used to help develop [ccg-v2d](https://github.com/agdturner/ccg-v2d) as visualising things like calculated intersections helps to show that geometrical algorithms work.

Developing 2D spatial data functionaility also helps with developing 3D spatial data functionality. If you are interested in processing 3D Euclidean spatial data then maybe also have a look at: [ccg-v3d](https://github.com/agdturner/ccg-v3d) and [ccg-r3d](https://github.com/agdturner/ccg-r3d).

## Rotations and colours
* One triangle (one colour for the triangle and another for it's edge):
<img alt="One triangle" src="data/output/test/test0.png" />
* Three rotated overlapping large triangles (one colour for the triangle and a different colour for each respective edge):
<img alt="Three rotated overlapping large triangles" src="data/output/test/test1.png" />
* Multiple small rotated triangles some overlapping:
<img alt="Multiple small rotated triangles some overlapping" src="data/output/test/test2.png" />
* Triangle rotated 48 times with increasing angle (the last triangle has just one colour for it's edge):
<img alt="Triangle rotated 48 times with increasing angle" src="data/output/test/test3.png" />
* Triangle rotated a bit, then the result rotated a bit 48 times (the last triangle has just one colour for it's edge):
<img alt="Triangle rotated a bit, then the result rotated a bit 48 times" src="data/output/test/test4.png" />

## With grids
* A triangle on a couple of randomly coloured grids:
<img alt="A triangle on a couple of randomly coloured grids" src="data/output/test/test0_grid.png" />

## Showing intersections
* Two rotated triangles with a two triangle intersection
<img alt="A first rendering of a triangle on a couple of randomly coloured grids" src="data/output/test/test6.png" />
Two rotated triangles with a four triangle intersection:
<img alt="Two rotated triangles with a four triangle intersection" src="data/output/test/test7.png" />

## Development plans/ideas
- Improve documentation.
- Generate some example renderings of geographical data.
- Make a versioned release on Maven Central.

## Development history
- The code was originally developed for academic research projects.

## Contributions welcome
- Please submit issues and initiate discussions about collaboration to help develop the code.

## LICENSE
- [APACHE LICENSE, VERSION 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Acknowledgements and thanks
- The [University of Leeds](http://www.leeds.ac.uk) and externally funded research grants have supported the development of this library.
- Thank you Eric for the [BigMath](https://github.com/eobermuhlner/big-math) library.
