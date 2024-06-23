package Objects;

import Utl.PointXYZDouble;

public class Face {
    public PointXYZDouble[] vertices;
    private PointXYZDouble normal;

    public Face(PointXYZDouble[] vertices) {
        this.vertices = vertices;
        this.normal = calculateFaceNormal(this.vertices);
    }

    public PointXYZDouble getNormal() {
        return normal;
    }

    private PointXYZDouble calculateFaceNormal(PointXYZDouble[] faceVertices) {

        PointXYZDouble vector1 = new PointXYZDouble(
                faceVertices[1].getX() - faceVertices[0].getX(),
                faceVertices[1].getY() - faceVertices[0].getY(),
                faceVertices[1].getZ() - faceVertices[0].getZ()
        );
        PointXYZDouble vector2 = new PointXYZDouble(
                faceVertices[2].getX() - faceVertices[0].getX(),
                faceVertices[2].getY() - faceVertices[0].getY(),
                faceVertices[2].getZ() - faceVertices[0].getZ()
        );


        double nx = vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY();
        double ny = vector1.getZ() * vector2.getX() - vector1.getX() * vector2.getZ();
        double nz = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();


        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= length;
        ny /= length;
        nz /= length;


        return new PointXYZDouble(nx, ny, nz);
    }
    public PointXYZDouble calculateCenterPoint() {
        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;

        for (PointXYZDouble vertex : vertices) {
            sumX += vertex.getX();
            sumY += vertex.getY();
            sumZ += vertex.getZ();
        }

        int vertexCount = vertices.length;
        double centerX = sumX / vertexCount;
        double centerY = sumY / vertexCount;
        double centerZ = sumZ / vertexCount;

        return new PointXYZDouble(centerX, centerY, centerZ);
    }
}
