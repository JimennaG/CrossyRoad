package crossyroad;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineaBresenham {

    // Constructor vacío, no se requiere inicialización adicional.
    public LineaBresenham() {
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
}
