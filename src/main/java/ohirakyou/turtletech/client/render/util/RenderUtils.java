package ohirakyou.turtletech.client.render.util;

import net.minecraft.client.renderer.VertexBuffer;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.util.MathUtils;

abstract public class RenderUtils {
    public static void addCube(final VertexBuffer vertexBuffer, final TexturedCubeMap cubeMap, final Size3D size) {
        addCube(vertexBuffer, cubeMap, size, new RelativePosition(), false);
    }
    public static void addCube(final VertexBuffer vertexBuffer, final TexturedCubeMap cubeMap, final Size3D size, boolean unlit) {
        addCube(vertexBuffer, cubeMap, size, new RelativePosition(), unlit);
    }

    public static void addCube(final VertexBuffer vertexBuffer, final TexturedCubeMap cubeMap,
                               final Size3D size, final RelativePosition offset) {
        addCube(vertexBuffer, cubeMap, size, offset, false);
    }

    /**
     * Forms a textured cube from quads and adds it to the vertex buffer.
     *
     * @param vertexBuffer  the buffer responsible for all of the cube's vertices
     * @param cubeMap  contains the UV coordinates for each face. Use null tiles to disable individual faces.
     * @param size  the cube's world dimensions, used for positioning vertices
     * @param offset  the cube's relative position
     * @param unlit  if true, cube will be lightmapped
     */
    public static void addCube(final VertexBuffer vertexBuffer, final TexturedCubeMap cubeMap,
                               final Size3D size, final RelativePosition offset, boolean unlit) {
        Size3D halfSize = size.getHalfSize();

        // Front (-z, north)  Subtracting length is normal here, because -z is forward
        if (cubeMap.northTile != null) {
            createVertex(vertexBuffer, halfSize.width + offset.rightward, halfSize.height + offset.upward, -halfSize.length - offset.forward, cubeMap.northTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward, -halfSize.height + offset.upward, -halfSize.length - offset.forward, cubeMap.northTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, -halfSize.width + offset.rightward, -halfSize.height + offset.upward, -halfSize.length - offset.forward, cubeMap.northTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, -halfSize.width + offset.rightward, halfSize.height + offset.upward, -halfSize.length - offset.forward, cubeMap.northTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);
        }

        // Back (+z, south)
        if (cubeMap.southTile != null) {
            createVertex(vertexBuffer,-halfSize.width + offset.rightward, halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.southTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer,-halfSize.width + offset.rightward,-halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.southTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward,-halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.southTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward, halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.southTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);
        }

        // Right (-x, west)
        if (cubeMap.westTile != null) {
            createVertex(vertexBuffer,-halfSize.width + offset.rightward, halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.westTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer,-halfSize.width + offset.rightward,-halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.westTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer,-halfSize.width + offset.rightward,-halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.westTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer,-halfSize.width + offset.rightward, halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.westTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);
        }

        // Left (+x, east)
        if (cubeMap.eastTile != null) {
            createVertex(vertexBuffer, halfSize.width + offset.rightward, halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.eastTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward,-halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.eastTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward,-halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.eastTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward, halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.eastTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);
        }

        // Top (+y, up)
        if (cubeMap.upTile != null) {
            createVertex(vertexBuffer,-halfSize.width + offset.rightward, halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.upTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer,-halfSize.width + offset.rightward, halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.upTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward, halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.upTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward, halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.upTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);
        }

        // Bottom (-y, down)
        if (cubeMap.downTile != null) {
            createVertex(vertexBuffer,-halfSize.width + offset.rightward,-halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.downTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer,-halfSize.width + offset.rightward,-halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.downTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward,-halfSize.height + offset.upward,-halfSize.length - offset.forward, cubeMap.downTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);
            createVertex(vertexBuffer, halfSize.width + offset.rightward,-halfSize.height + offset.upward, halfSize.length - offset.forward, cubeMap.downTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);
        }
    }

    /**
     * Adds a beam with the same start and end radius
     *
     * @see #addBeam(VertexBuffer, RelativePosition, float, float, float, int, float, TextureTile, boolean)
     */
    public static void addBeam(VertexBuffer vertexBuffer, RelativePosition start,
                               float length, float radius, int faces, float roll, TextureTile textureTile, boolean unlit) {
        addBeam(vertexBuffer, start, length, radius, radius, faces, roll, textureTile, unlit);
    }

    /**
     * Adds a beam that goes forward from start.
     * <p>
     * Rotate the rendering transform to change the beam's direction.
     * Use the roll parameter to rotate the beam around its center by degrees.
     * By default, the beam's first point starts directly above the center, at 0/360.
     *
     * @param vertexBuffer  the buffer responsible for all of the beam's vertices
     * @param start  the center of the beam, at its start
     * @param length  distance between the start and end points of the beam
     * @param startRadius  half-width of the beam at its starting point
     * @param endRadius  half-width of the beam at its end point
     * @param faces  also the number of start and end circle vertices
     * @param roll  a value, auto-wrapped between 0 and 360, that can be used to rotate the beam
     * @param textureTile  beam UV coordinates, left being the start and right being the end of the beam
     * @param unlit  if true, beam is rendered lightmapped
     */
    public static void addBeam(
            VertexBuffer vertexBuffer, RelativePosition start, float length, float startRadius, float endRadius,
            int faces, float roll, TextureTile textureTile, boolean unlit) {

        float degreesPerPoint = 360f / faces;

        float[] x = new float[faces];
        float[] y = new float[faces];

        float[] xEnd = new float[faces];
        float[] yEnd = new float[faces];

        // x = radius * sin(degrees)
        // y = radius * cos(degrees)

        // Generate a circle of points on a 2D plane
        for (int i = 0; i < faces; i++) {
            float angle = MathUtils.wrapAngle360(i * degreesPerPoint + roll);

            x[i] = startRadius * (float)Math.sin(Math.toRadians(angle));
            y[i] = startRadius * (float)Math.cos(Math.toRadians(angle));
        }

        // Generate a second circle of points on a 2D plane in the distance
        if (startRadius == endRadius) {
            // Don't bother recalculating if the data will be the same
            xEnd = x;
            yEnd = y;
        } else {
            // Radii are different; calculate end points
            for (int i = 0; i < faces; i++) {
                float angle = MathUtils.wrapAngle360(i * degreesPerPoint + roll);

                xEnd[i] = endRadius * (float)Math.sin(Math.toRadians(angle));
                yEnd[i] = endRadius * (float)Math.cos(Math.toRadians(angle));
            }
        }

        // Connect each point to the next point. Then, connect those to the distant points to form a quad
        for (int iTop = 0; iTop < faces; iTop++) {
            int iBottom = iTop + 1;
            if (iBottom == x.length) { iBottom = 0; }

//             //Start
//            addVertex(vertexBuffer, x[iTop] + start.rightward, y[iTop] + start.upward, -start.forward, textureTile.getTopLeft());
//            addVertex(vertexBuffer, x[iBottom] + start.rightward, y[iBottom] + start.upward, -start.forward, textureTile.getBottomLeft());
//
//             //Target (note that forward is -z; subtracting length is normal)
//            addVertex(vertexBuffer, xEnd[iBottom] + start.rightward, yEnd[iBottom] + start.upward, -length - start.forward, textureTile.getBottomRight());
//            addVertex(vertexBuffer, xEnd[iTop] + start.rightward, yEnd[iTop] + start.upward, -length - start.forward, textureTile.getTopRight());


            // Start
            createVertex(vertexBuffer, x[iTop] + start.rightward, y[iTop] + start.upward, -start.forward, textureTile.getTopLeft());
            finalizeVertex(vertexBuffer, unlit);

            createVertex(vertexBuffer, x[iBottom] + start.rightward, y[iBottom] + start.upward, -start.forward, textureTile.getBottomLeft());
            finalizeVertex(vertexBuffer, unlit);

            // Target (note that forward is -z; subtracting length is normal)
            createVertex(vertexBuffer, xEnd[iBottom] + start.rightward, yEnd[iBottom] + start.upward, -length - start.forward, textureTile.getBottomRight());
            finalizeVertex(vertexBuffer, unlit);

            createVertex(vertexBuffer, xEnd[iTop] + start.rightward, yEnd[iTop] + start.upward, -length - start.forward, textureTile.getTopRight());
            finalizeVertex(vertexBuffer, unlit);


//            int lightValue = 15 << 20 | 15 << 4;
//            int lmapX = lightValue >> 16 & 0xFFFF;
//            int lmapY = lightValue & 0xFFFF;
//
//            //Start
//            createVertex(vertexBuffer, x[iTop] + start.rightward, y[iTop] + start.upward, -start.forward, textureTile.getTopLeft()).lightmap(lmapX, lmapY).color(1f, 1f, 1f, 1f).endVertex();
//            createVertex(vertexBuffer, x[iBottom] + start.rightward, y[iBottom] + start.upward, -start.forward, textureTile.getBottomLeft()).lightmap(lmapX, lmapY).color(1f, 1f, 1f, 1f).endVertex();
//
//             //Target (note that forward is -z; subtracting length is normal)
//            createVertex(vertexBuffer, xEnd[iBottom] + start.rightward, yEnd[iBottom] + start.upward, -length - start.forward, textureTile.getBottomRight()).lightmap(lmapX, lmapY).color(1f, 1f, 1f, 1f).endVertex();
//            createVertex(vertexBuffer, xEnd[iTop] + start.rightward, yEnd[iTop] + start.upward, -length - start.forward, textureTile.getTopRight()).lightmap(lmapX, lmapY).color(1f, 1f, 1f, 1f).endVertex();

        }

    }

    public static VertexBuffer finalizeVertex (VertexBuffer buffer, boolean unlit) {
        int lightValue = 15 << 20 | 15 << 4;
        int lmapX = lightValue >> 16 & 0xFFFF;
        int lmapY = lightValue & 0xFFFF;

        if (unlit) {
            buffer.lightmap(lmapX, lmapY).color(1f, 1f, 1f, 1f);
        }

        buffer.endVertex();

        return buffer;
    }


    /*
    public static void addQuad(final VertexBuffer vertexBuffer, final TextureTile tile, final Size3D size) {
        Size3D halfSize = size.getHalfSize();

        // Needs some directional info. Otherwise, this will only ever add a quad in a single orientation
//        addVertex(vertexBuffer, halfSize.width, halfSize.height, -halfSize.length, tile.getTopLeft());
//        addVertex(vertexBuffer, halfSize.width,-halfSize.height, -halfSize.length, tile.getBottomLeft());
//        addVertex(vertexBuffer,-halfSize.width,-halfSize.height, -halfSize.length, tile.getBottomRight());
//        addVertex(vertexBuffer,-halfSize.width, halfSize.height, -halfSize.length, tile.getTopRight());
    }
    */

    public static void addVertex(final VertexBuffer vertexBuffer, float x, float y, float z, TexturePoint texturePoint) {
        createVertex(vertexBuffer, x, y, z, texturePoint).endVertex();
    }

    public static VertexBuffer createVertex(final VertexBuffer vertexBuffer, float x, float y, float z, TexturePoint texturePoint) {
        return vertexBuffer.pos(x, y, z).tex(texturePoint.right, texturePoint.down);
    }
}
