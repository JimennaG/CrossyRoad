package crossyroad;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class dibujar extends JPanel {

    private BufferedImage buffer;

    public dibujar() {
        buffer = new BufferedImage(600, 650, BufferedImage.TYPE_INT_ARGB);
    }

    public void putPixel(int x, int y, Color c) {
        buffer.setRGB(x, y, c.getRGB());
    }

    public void drawPointInBuffer(int x, int y, Color color) {
        Graphics2D g2d = buffer.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(x, y, 1, 1);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }

    public void fillPolygon(List<Point> points, Color color) {
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
        }
        Graphics2D g2d = buffer.createGraphics();
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, points.size());
        g2d.dispose();
    }
}
