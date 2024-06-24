package crossyroad;

import Utl.PointXYInt;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineaBresenham {
    
    public PointXYInt pointA, pointB;
    // Constructor vacío, no se requiere inicialización adicional.
    public LineaBresenham() {
    }
    
    public LineaBresenham(PointXYInt pointA, PointXYInt pointB)
    {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    // Método para calcular los puntos en una línea usando el algoritmo de Bresenham
    public static List<Point> calcularLineaBresenham(int x1, int y1, int x2, int y2) {
        List<Point> puntos = new ArrayList<>();  // Lista para almacenar los puntos en la línea
        int dx = Math.abs(x2 - x1);             // Diferencia en coordenadas x
        int dy = Math.abs(y2 - y1);             // Diferencia en coordenadas y
        int sx = (x1 < x2) ? 1 : -1;            // Dirección en x (1 o -1)
        int sy = (y1 < y2) ? 1 : -1;            // Dirección en y (1 o -1)
        int err = dx - dy;                      // Error inicial

        while (true) {
            puntos.add(new Point(x1, y1));      // Agregar el punto actual a la lista de puntos

            // Verificar si hemos llegado al punto final
            if (x1 == x2 && y1 == y2) {
                break;
            }

            int e2 = 2 * err;                   // Doble del error
            if (e2 > -dy) {                     // ¿El error es mayor que el componente y?
                err -= dy;                      // Reducir el error en el componente y
                x1 += sx;                       // Mover en la dirección de x
            }
            if (e2 < dx) {                      // ¿El error es menor que el componente x?
                err += dx;                      // Aumentar el error en el componente x
                y1 += sy;                       // Mover en la dirección de y
            }
        }
        return puntos;  // Devolver la lista de puntos que forman la línea
    }
    
    public ArrayList<PointXYInt> LineToPointsXY()
    {
        ArrayList<PointXYInt> points = new ArrayList<PointXYInt>();

        int xk = pointA.x, yk = pointA.y;
        int dx = pointB.x - pointA.x, dy = pointB.y - pointA.y;
        int incX = 1, incY = 1, incE, incNE, pk = 0;

        if (dx < 0)
        {
            dx = -dx;
            incX = -1;
        }
        if (dy < 0)
        {
            dy = -dy;
            incY = -1;
        }

        if (Math.abs(dx) > Math.abs(dy))
        {
            incE = 2 * dy;
            incNE = 2 * (dy - dx);

            while (xk != pointB.x)
            {
                points.add(new PointXYInt(xk, yk));
                xk += incX;
                if (2 * (pk + dy) < dx)
                    pk += incE;
                else
                {
                    pk += incNE;
                    yk += incY;
                }
            }
        }
        else
        {
            incE = 2 * dx;
            incNE = 2 * (dx - dy);
            while (yk != pointB.y)
            {
                points.add(new PointXYInt(xk, yk));
                yk += incY;
                if (2 * (pk + dx) < dy)
                    pk += incE;
                else
                {
                    pk += incNE;
                    xk += incX;
                }
            }
        }
        return points;
    }
}
