package crossyroad;

import Utl.PointXY;
import Utl.PointXYZ;
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
    
    private PointXYZ translate(PointXYZ point) {
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
        return new PointXYZ(x, y, z);
    }

    private PointXYZ scale(PointXYZ point, double scaleX, double scaleY, double scaleZ) {
        double x = point.x * scaleX;
        double y = point.y * scaleY;
        double z = point.z * scaleZ;
        return new PointXYZ(x, y, z);
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
    
    private PointXYZ calculatePoint(double u, double v) {
        double x = (C + A * Math.cos(v)) * Math.cos(u);
        double y = (C + A * Math.cos(v)) * Math.sin(u);
        double z = A * Math.sin(v);
        return new PointXYZ(x, y, z);
    }

    private PointXYZ rotate(PointXYZ point, double angleX, double angleY, double angleZ) {
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

    private PointXY applyPerspective(PointXYZ point) {
        double scaleFactor = 1 / (1 + point.z * PERSPECTIVE_FACTOR);
        double x = point.x * scaleFactor + buffer.getWidth() / 2.0;
        double y = point.y * scaleFactor + buffer.getWidth() / 2.0;
        return new PointXY(x, y);
    }
}
