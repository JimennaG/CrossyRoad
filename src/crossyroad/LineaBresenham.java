package crossyroad;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class LineaBresenham {
    public List<Point> calcularLineaBresenham(int x0, int y0, int x1, int y1) {
        List<Point> puntos = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            puntos.add(new Point(x0, y0));
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return puntos;
    }
}
