# [ccg-r2d](https://github.com/agdturner/ccg-r2d)
A Java library for rendering 2D Euclidean spatial data built on [ccg-v2d](https://github.com/agdturner/ccg-v2d) and [ccg-grids](https://github.com/agdturner/ccg-grids). There are implementations for double precision and for precision accurate to a specified order of magnitude (which the user can specify).

The first examples are rendering triangles, grids behind triangles, and for showing that the algorithms for intersecting triangles work.

This library has utility for portraying 2D Euclidean spatial data. It is used to help develop [ccg-v2d](https://github.com/agdturner/ccg-v2d) as visualising processed data helps to show that algoithms, like intersection algorithms are working. This helps also in developing [ccg-v3d](https://github.com/agdturner/ccg-v3d) and [ccg-grids](https://github.com/agdturner/ccg-grids) for processing 3D Euclidean spatial data, and [ccg-r3d](https://github.com/agdturner/ccg-r3d) for rendering 3D spatial data. As you may appreciate, 3D Euclidean spatial data rendering is a lot harder than 2D Euclidean spatial data rendering, and getting a simpler and related implementation working can help.


One triangle:
<img alt="One triangle" src="data/output/test/test0.png" />
Three rotated overlapping large triangles:
<img alt="Three rotated overlapping large triangles" src="data/output/test/test1.png" />
Multiple small rotated triangles some overlapping:
<img alt="Multiple small rotated triangles some overlapping" src="data/output/test/test2.png" />
Triangle rotated 48 times with increasing angle:
<img alt="Triangle rotated 48 times with increasing angle" src="data/output/test/test3.png" />
Triangle rotated a bit, then the result rotated a bit 48 times:
<img alt="Triangle rotated a bit, then the result rotated a bit 48 times" src="data/output/test/test4.png" />

A triangle on a couple of randomly coloured grids:
<img alt="A triangle on a couple of randomly coloured grids" src="data/output/test/test0_grid.png" />

Two rotated triangles with a two triangle intersection
<img alt="A first rendering of a triangle on a couple of randomly coloured grids" src="data/output/test/test6.png" />
Two rotated triangles with a four triangle intersection:
<img alt="Two rotated triangles with a four triangle intersection" src="data/output/test/test7.png" />
