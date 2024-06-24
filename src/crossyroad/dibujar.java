package crossyroad;

import Utl.*;
import Utl.PointXYZDouble;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class dibujar extends JPanel {
    
    private Timer timer;
    private static final double A = 50;
    private static final double C = 100;
    private static final int NUM_U = 30;
    private static final int NUM_V = 30;
    private static final double PERSPECTIVE_FACTOR = 1 / 400.0;
    private double rotationAngleX = 0;
    private double rotationAngleY = 0;
    private double rotationAngleZ = 0;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private double scaleZ = 1.0;
    private double dx;
    private double dy;
    private double dz;
    private double scaleIncrement = 0.01; 

    private ArrayList<int[]> listaDePuntos;
    private ArrayList<Color> listaDeColores;
    private BufferedImage buffer;
    

    private String message = null;  // Agrega esto para almacenar el mensaje
    private long messageEndTime = -1;  // Duración durante la cual el mensaje debe mostrarse

    public dibujar() {
        listaDePuntos = new ArrayList<>();
        listaDeColores = new ArrayList<>();
        buffer = new BufferedImage(600, 1080, BufferedImage.TYPE_INT_ARGB);
    }

    public void addPunto(List<Point> puntos, Color color) {
        for (Point p : puntos) {
            drawPointInBuffer(p.x, p.y, color); // Dibujar el punto en el buffer directamente
        }
    }

    public void putPixel(int x, int y, Color c) {
        buffer.setRGB(x, y, c.getRGB());
    }

    private void drawPointInBuffer(int x, int y, Color color) {
        Graphics2D g2d = buffer.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(x, y, 1, 1);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
        if (message != null && System.currentTimeMillis() < messageEndTime) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(message, 50, 250);
        } else if (System.currentTimeMillis() >= messageEndTime && message != null) {
            message = null;
            repaint();
        }
        Graphics2D g2d = buffer.createGraphics();
        clearBuffer(g2d);
    }

    private void clearBuffer(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g2d.setComposite(AlphaComposite.SrcOver);
    }

    public void rellenarTriangulo(List<Point> puntosRellenar, Color color) {
        Map<Integer, List<Integer>> map = new HashMap<>();

        for (Point p : puntosRellenar) {
            if (!map.containsKey(p.y)) {
                map.put(p.y, new ArrayList<>());
            }
            map.get(p.y).add(p.x);
        }

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            List<Integer> xs = entry.getValue();
            Collections.sort(xs);
            int y = entry.getKey();
            for (int x = xs.get(0); x <= xs.get(xs.size() - 1); x++) {
                putPixel(x, y, color);
            }
        }
    }

    public void setMessage(String msg, long duration) {
        this.message = msg;
        this.messageEndTime = System.currentTimeMillis() + duration;
        repaint();
    }
    
        // Método para rellenar la cara
    public void fillPolygon(List<Point> points, Color color) {
        // Encuentra los límites del polígono
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : points) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        // Recorrido por cada línea horizontal entre minY y maxY
        for (int y = minY; y <= maxY; y++) {
            List<Integer> nodeX = new ArrayList<>();
            // Encuentra puntos de intersección
            for (int i = 0; i < points.size(); i++) {
                Point p1 = points.get(i);
                Point p2 = points.get((i + 1) % points.size()); // Wrap around

                // Checa si la línea actual interseca con el segmento
                if ((p1.y < y && p2.y >= y) || (p2.y < y && p1.y >= y)) {
                    int intersectX = p1.x + (y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y);
                    nodeX.add(intersectX);
                }
            }

            // Ordena las intersecciones
            Collections.sort(nodeX);

            // Llena entre pares de intersecciones
            for (int i = 0; i < nodeX.size() - 1; i += 2) {
                if (nodeX.get(i) != nodeX.get(i + 1)) { // Ignora líneas de un solo punto
                    for (int x = nodeX.get(i); x <= nodeX.get(i + 1); x++) {
                        putPixel(x, y, color);
                    }
                }
            }
        }
    }
    
    private PointXYZDouble translate(PointXYZDouble point) {
        double[] vector = { point.x, point.y, point.z, 1.0 };

        double[][] matrix = {
                { dx, 0.0, 0.0, 0.0 },
                { 0.0, dy, 0.0, 0.0 },
                { 0.0, 0.0, dz, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        double[] result = new double[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        double x = point.x + dx;
        double y = point.y + dy;
        double z = point.z + dz;
        return new PointXYZDouble(x, y, z);
    }

    private PointXYZDouble scale(PointXYZDouble point, double scaleX, double scaleY, double scaleZ) {
        double x = point.x * scaleX;
        double y = point.y * scaleY;
        double z = point.z * scaleZ;
        return new PointXYZDouble(x, y, z);
    }

    private void startRotationAndTranslation() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                rotationAngleZ += 0.01;

                scaleX += scaleIncrement;
                scaleY += scaleIncrement;
                scaleZ += scaleIncrement;

                dx += 1;
                dy += 1;
                dz += 1;

                if (scaleX > 2.0 || scaleX < 0.5) {
                    scaleIncrement = -scaleIncrement;
                }

                repaint();
            }
        }, 0, 30);
    }

    private void stopRotation() {
        if (timer != null) {
            timer.cancel();
        }
    }
    
    private PointXYZDouble calculatePoint(double u, double v) {
        double x = (C + A * Math.cos(v)) * Math.cos(u);
        double y = (C + A * Math.cos(v)) * Math.sin(u);
        double z = A * Math.sin(v);
        return new PointXYZDouble(x, y, z);
    }

    private PointXYZDouble rotate(PointXYZDouble point, double angleX, double angleY, double angleZ) {
        double cosX = Math.cos(angleX);
        double sinX = Math.sin(angleX);
        double cosY = Math.cos(angleY);
        double sinY = Math.sin(angleY);
        double cosZ = Math.cos(angleZ);
        double sinZ = Math.sin(angleZ);

        double y = point.y * cosX - point.z * sinX;
        double z = point.y * sinX + point.z * cosX;
        point.y = y;
        point.z = z;

        double x = point.x * cosY + point.z * sinY;
        z = -point.x * sinY + point.z * cosY;
        point.x = x;
        point.z = z;

        x = point.x * cosZ - point.y * sinZ;
        y = point.x * sinZ + point.y * cosZ;
        point.x = x;
        point.y = y;

        return point;
    }

    private PointXYDouble applyPerspective(PointXYZDouble point) {
        double scaleFactor = 1 / (1 + point.z * PERSPECTIVE_FACTOR);
        double x = point.x * scaleFactor + buffer.getWidth() / 2.0;
        double y = point.y * scaleFactor + buffer.getWidth() / 2.0;
        return new PointXYDouble(x, y);
    }
    
    
    
 
//    private void DrawCube(int x, int y, int z, int ancho, int alto, int largo, Color color)
//    {
//        PointXYZInt vector = new PointXYZInt(-2,-1,6);
//        
//        ArrayList<PointXYZInt> pointsXYZ = new ArrayList<>();
//        pointsXYZ.add(new PointXYZInt(50, 150, 150)); // A
//        pointsXYZ.add(new PointXYZInt(150, 150, 150)); // B
//        pointsXYZ.add(new PointXYZInt(50, 250, 150)); // C
//        pointsXYZ.add(new PointXYZInt(150, 250, 150)); // D
//
//        pointsXYZ.add(new PointXYZInt(50, 150, 250)); // E
//        pointsXYZ.add(new PointXYZInt(150, 150, 250)); // F
//        pointsXYZ.add(new PointXYZInt(50, 250, 250)); // G
//        pointsXYZ.add(new PointXYZInt(150, 250, 250)); // H
//        
//        for (int i = 0; i < pointsXYZ.size(); i++)
//        {
//            float u =  (float) (-1 * pointsXYZ.get(i).z) / vector.z;
//            float x = pointsXYZ.get(i).x + (vector.x * u);
//            float y = pointsXYZ.get(i).y + (vector.y * u);
//
//            pointsXYInt.add(new PointXYInt((int) x, (int) y));
//        }
//        for (PointXY point: pointsXY)
//        {
//            PutPixel(point.x, point.y, MyColor.myYellow);
//            System.out.println("x:" + point.x + " y:" + point.y);
//        }
//
//        LineaBresenham lineAB = new LineaBresenham(pointsXY.get(0), pointsXY.get(1));
//        LineaBresenham lineAC = new LineaBresenham(pointsXY.get(0), pointsXY.get(2));
//        LineaBresenham lineAE = new LineaBresenham(pointsXY.get(0), pointsXY.get(4));
//
//        LineaBresenham lineBD = new LineaBresenham(pointsXY.get(1), pointsXY.get(3));
//        LineaBresenham lineBF = new LineaBresenham(pointsXY.get(1), pointsXY.get(5));
//
//        LineaBresenham lineEG = new LineaBresenham(pointsXY.get(4), pointsXY.get(6));
//        LineaBresenham lineEF = new LineaBresenham(pointsXY.get(4), pointsXY.get(5));
//
//        LineaBresenham lineCG = new LineaBresenham(pointsXY.get(2), pointsXY.get(6));
//        LineaBresenham lineCD = new LineaBresenham(pointsXY.get(2), pointsXY.get(3));
//
//        LineaBresenham lineHG = new LineaBresenham(pointsXY.get(7), pointsXY.get(6));
//        LineaBresenham lineHF = new LineaBresenham(pointsXY.get(7), pointsXY.get(5));
//        LineaBresenham lineHD = new LineaBresenham(pointsXY.get(7), pointsXY.get(3));
//
//        ArrayList<PointXYInt> points = lineAB.LineToPointsXY();
//        points.addAll(lineCG.LineToPointsXY());
//        points.addAll(lineBF.LineToPointsXY());
//        points.addAll(lineHF.LineToPointsXY());
//        points.addAll(lineHG.LineToPointsXY());
//        points.addAll(lineAC.LineToPointsXY());
//
//        for (PointXY point: points)
//            PutPixel(point.x, point.y, MyColor.myYellow);
//        Rellenar(110, 266, MyColor.myYellow, MyColor.myYellow);
////
//        points.addAll(lineBD.LineToPointsXY());
//        points.addAll(lineHD.LineToPointsXY());
//        points.addAll(lineEG.LineToPointsXY());
//        points.addAll(lineAE.LineToPointsXY());
//        points.addAll(lineEF.LineToPointsXY());
//        points.addAll(lineCD.LineToPointsXY());
//
//        for (PointXYInt point: points)
//            PutPixel(point.x, point.y, MyColor.cabinBlack2);
//    }
//
//    public void Rellenar (int x, int y,Color color, Color figura) {
//        int limiteIzquierdo = x;
//        int limiteDerecho = x;
//
//        while (getPixel(limiteIzquierdo - 1, y) != figura.getRGB()) {
//            limiteIzquierdo--;
//        }
//        while (getPixel(limiteDerecho + 1, y) != figura.getRGB()) {
//            limiteDerecho++;
//        }
//
//        for (int i = limiteIzquierdo; i <= limiteDerecho; i++) {
//            PutPixel(i, y, color);
//        }
//
//        for (int i = limiteIzquierdo; i <= limiteDerecho; i++) {
//            if (getPixel(i , y - 1) != figura.getRGB() && getPixel(i , y - 1) != color.getRGB()) {
//                Rellenar(i, y - 1,color,figura);
//            }
//            if (getPixel(i , y + 1) != figura.getRGB() && getPixel(i , y + 1) != color.getRGB()) {
//                Rellenar(i, y + 1,color,figura);
//            }
//        }
//    }
//    
//    public int getPixel(int x, int y) { return buffer.getRGB(x, y); }
}
