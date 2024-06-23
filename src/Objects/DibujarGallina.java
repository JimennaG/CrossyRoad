
package Objects;

import Utl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class DibujarGallina {
    private BufferedImage image;
    private int dx = 0, dy = 0;
    private int dxAnimacion = 0, dyAnimacion = 0;
    private double sxAnimacion = 0, syAnimacion = 0;
    private double sx = 1, sy = 1;
    private double theta = 0;
    public Color color = Color.red;

    private PointXYZDouble vectorRotacion = new PointXYZDouble(0, 0, 0);
    private PointXYZDouble vectorTraslacion = new PointXYZDouble(0, 0, 0);
    private PointXYZDouble vectorEscalacion = new PointXYZDouble(1, 1, 1);
    private PointXYZInt vectorProyeccion = new PointXYZInt(0, 0, 0);


    public DibujarGallina(JFrame frame) {
        resetImage(frame);
    }

    public void putPixel(int x, int y) {
        if ((x < 0 || x >= image.getWidth()) || (y < 0 || y >= image.getHeight()))
            return;
        image.setRGB(x, y, color.getRGB());
    }

    public Color getPixel(int x, int y) {
        if ((x < 0 || x >= image.getWidth()) || (y < 0 || y >= image.getHeight()))
            return null;
        return new Color(image.getRGB(x, y));
    }

    public void lineaEcuacion(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        boolean avanzaY = dy > dx;
        if (avanzaY) {
            int temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        double pendiente = (double) (y2 - y1) / (x2 - x1);
        double b = y1 - pendiente * x1;
        for (int x = x1; x <= x2; x++) {
            int y = (int) (pendiente * x + b);
            if (avanzaY) {
                putPixel(y, x);
            } else {
                putPixel(x, y);
            }
        }
    }

    public void lineaDDA(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int pasos = Math.max(Math.abs(dx), Math.abs(dy));
        double xInc = (double) dx / pasos;
        double yInc = (double) dy / pasos;
        double x = x1, y = y1;
        putPixel((int) Math.round(x), (int) Math.round(y));
        for (int i = 0; i <= pasos; i++) {
            x += xInc;
            y += yInc;
            putPixel((int) Math.round(x), (int) Math.round(y));
        }
    }

    public void lineaBresenham(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        boolean avanzaY = dy > dx;
        if (avanzaY) {
            int temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        dx = x2 - x1;
        dy = Math.abs(y2 - y1);
        int A = 2 * dy, B = 2 * dy - 2 * dx;
        int p = 2 * dy - dx;
        int y = y1;
        int sumaY = y1 < y2 ? 1 : -1;
        for (int x = x1; x <= x2; x++) {
            if (p < 0) {
                p += A;
            } else {
                y += sumaY;
                p += B;
            }
            if (avanzaY) {
                putPixel(y, x);
            } else {
                putPixel(x, y);
            }
        }

    }

    public void lineaPuntoMedio(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        boolean avanzaY = dy > dx;

        if (avanzaY) {
            int temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }

        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        dx = x2 - x1;
        dy = Math.abs(y2 - y1);


        int dosDy = 2 * dy;
        int dosDymenosDx = 2 * (dy - dx);
        int p = 2 * dy - dx;

        int y = y1;
        for (int x = x1; x <= x2; x++) {
            if (avanzaY) {
                putPixel(y, x);
            } else {
                putPixel(x, y);
            }
            if (p > 0) {
                p += dosDymenosDx;
                y += (y2 > y1 ? 1 : -1);
            } else {
                p += dosDy;
            }
        }
    }

    // línea con transformaciones aplicadas
    public void linea(int x1, int y1, int x2, int y2) {

        PointXYInt pos1 = calcularTransformaciones(new PointXYInt(x1, y1));
        PointXYInt pos2 = calcularTransformaciones(new PointXYInt(x2, y2));
        x1 = pos1.x;
        y1 = pos1.y;

        x2 = pos2.x;
        y2 = pos2.y;
        lineaPuntoMedio(x1, y1, x2, y2);
    }

    public void rectangulo(int x1, int y1, int x2, int y2) {
        linea(x1, y1, x2, y1);
        linea(x2, y1, x2, y2);
        linea(x2, y2, x1, y2);
        linea(x1, y2, x1, y1);
    }

    public void circulo1(int xc, int yc, int r) {
        for (int xPos = xc - r; xPos <= xc + r; xPos++) {
            double yPos = (double) (yc + Math.sqrt(Math.pow(r, 2) - Math.pow(xPos - xc, 2)));
            putPixel(xPos, (int) Math.round(yPos));
        }
        for (int xPos = xc - r; xPos <= xc + r; xPos++) {
            double yPos = (double) (yc - Math.sqrt(Math.pow(r, 2) - Math.pow(xPos - xc, 2)));
            putPixel(xPos, (int) Math.round(yPos));
        }
    }

    public void circuloPolar(int xc, int yc, int r) {
        for (double angulo = 0; angulo <= Math.PI / 2; angulo += 0.01) {
            double xPos = (double) (xc + r * Math.sin(angulo));
            double yPos = (double) (xc + r * Math.cos(angulo));
            putPixel((int) xPos, (int) yPos);
        }
        for (double angulo = (double) (Math.PI / 2); angulo <= Math.PI; angulo += 0.01) {
            double xPos = (double) (xc + r * Math.sin(angulo));
            double yPos = (double) (xc + r * Math.cos(angulo));
            putPixel((int) xPos, (int) yPos);
        }
        for (double angulo = (double) Math.PI; angulo <= Math.PI * (3 / 2); angulo += 0.01) {
            double xPos = (double) (xc + r * Math.sin(angulo));
            double yPos = (double) (xc + r * Math.cos(angulo));
            putPixel((int) xPos, (int) yPos);
        }
        for (double angulo = (double) (Math.PI * (3 / 2)); angulo <= 2 * Math.PI; angulo += 0.01) {
            double xPos = (double) (xc + r * Math.sin(angulo));
            double yPos = (double) (xc + r * Math.cos(angulo));
            putPixel((int) xPos, (int) yPos);
        }

    }

    public void circuloPuntoMedio(int xc, int yc, double R) {
        PointXYInt pos = calcularTraslacion(new PointXYInt(xc, yc));
        xc = pos.x;
        yc = pos.y;
//        putPixel(0, (int) R);
        int xk = -1;
        int yk = (int) R;
        double pk = (double) (5 / 4) - R;
        while (xk <= yk) {
            xk += 1;
            if (pk < 0) {
                putPixel(xk + xc, yk + yc);
                pk = pk + 2 * xk + 3;
            } else {
                yk -= 1;
                putPixel(xk + xc, yk + yc);
                pk = pk + 2 * xk - 2 * yk + 5;
            }
            // simetria
            putPixel(xk + xc, -yk + yc);
            putPixel(-xk + xc, yk + yc);
            putPixel(-xk + xc, -yk + yc);
            putPixel(yk + xc, xk + yc);
            putPixel(-yk + xc, xk + yc);
            putPixel(yk + xc, -xk + yc);
            putPixel(-yk + xc, -xk + yc);
        }
    }

    public void elipse(int xc, int yc, int rx, int ry) {
        PointXYDouble centro = calularRotacion(new PointXYDouble(xc, yc));
        xc = (int) centro.x;
        yc = (int) centro.y;

        int x1 = xc - rx;
        int y1 = yc - ry;
        int x2 = xc + rx;
        int y2 = yc + ry;
        PointXYDouble pos1 = calcularTraslacion(calcularEscalacion(new PointXYDouble(x1, y1)));
        PointXYDouble pos2 = calcularTraslacion(calcularEscalacion(new PointXYDouble(x2, y2)));
        x1 = (int) pos1.x;
        y1 = (int) pos1.y;

        x2 = (int) pos2.x;
        y2 = (int) pos2.y;

        xc = (x1 + x2) / 2;
        yc = (y1 + y2) / 2;
        rx = (x2 - x1) / 2;
        ry = (y2 - y1) / 2;


        for (double angulo = 0; angulo <= 2 * Math.PI; angulo += 0.005) {
            double xPos = (double) (xc + rx * Math.cos(theta) * Math.cos(angulo) - ry * Math.sin(theta) * Math.sin(angulo));
            double yPos = (double) (yc + rx * Math.sin(theta) * Math.cos(angulo) + ry * Math.cos(theta) * Math.sin(angulo));

            putPixel((int) xPos, (int) yPos);
        }
    }

    public void floodFill(int x, int y) {
        Color colorRelleno=color;
        PointXYInt pos1 = calcularTransformaciones(new PointXYInt(x, y));
        x = pos1.x;
        y = pos1.y;
        Color colorObjetivo = getPixel(x, y);

        if ((x < 0 || x >= image.getWidth()) || (y < 0 || y >= image.getHeight()))
            return;
        if (colorObjetivo.equals(colorRelleno)) {
            return;
        }

        Stack<PointXYInt> pila = new Stack<>();
        pila.push(new PointXYInt(x, y));

        while (!pila.isEmpty()) {
            PointXYInt p = pila.pop();
            int xPos = p.x;
            int yPos = p.y;
            putPixel(xPos, yPos);

            if (xPos > 0 && getPixel(xPos - 1, yPos).equals(colorObjetivo)) {
                pila.push(new PointXYInt(xPos - 1, yPos));
            }
            if (xPos < image.getWidth() - 1 && getPixel(xPos + 1, yPos).equals(colorObjetivo)) {
                pila.push(new PointXYInt(xPos + 1, yPos));
            }
            if (yPos > 0 && getPixel(xPos, yPos - 1).equals(colorObjetivo)) {
                pila.push(new PointXYInt(xPos, yPos - 1));
            }
            if (yPos < image.getHeight() - 1 && getPixel(xPos, yPos + 1).equals(colorObjetivo)) {
                pila.push(new PointXYInt(xPos, yPos + 1));
            }
        }
    }

    public void scanLineFill(int x, int y) {
        Color colorRelleno = color;
        PointXYInt pos1 = calcularTransformaciones(new PointXYInt(x, y));
        x = pos1.x;
        y = pos1.y;
        Color colorObjetivo = getPixel(x, y);
        if (colorObjetivo == null) {
            return;
        }
        if (!colorObjetivo.equals(colorRelleno)) {
            Stack<PointXYInt> pila = new Stack<PointXYInt>();
            pila.push(new PointXYInt(x, y));

            while (!pila.empty()) {
                PointXYInt p = pila.pop();

                if (getPixel(p.x, p.y).equals(colorObjetivo)) {
                    int xIzq = p.x;
                    int xDer = p.x;

                    while (xIzq >= 0 && getPixel(xIzq, p.y).equals(colorObjetivo)) {
                        xIzq--;
                    }

                    while (xDer < image.getWidth() && getPixel(xDer, p.y).equals(colorObjetivo)) {
                        xDer++;
                    }

                    for (int i = xIzq + 1; i < xDer; i++) {
                        putPixel(i, p.y);
                    }

                    for (int i = xIzq + 1; i < xDer; i++) {
                        if (p.y > 0 && getPixel(i, p.y - 1).equals(colorObjetivo)) {
                            pila.push(new PointXYInt(i, p.y - 1));
                        }
                    }

                    for (int i = xIzq + 1; i < xDer; i++) {
                        if (p.y < image.getHeight() - 1 && getPixel(i, p.y + 1).equals(colorObjetivo)) {
                            pila.push(new PointXYInt(i, p.y + 1));
                        }
                    }
                }
            }
        }
    }

    private PointXYDouble calcularEscalacion(PointXYDouble origen) {
        double[][] matrizOrigen = {{origen.x, origen.y, 1}};
        double[][] matrizTransformacion = {
                {
                        sx * (sxAnimacion + 1),
                        0,
                        0
                },
                {
                        0,
                        sy * (syAnimacion + 1),
                        0
                },
                {
                        0,
                        0,
                        1
                }
        };

        double[][] resultado = multiplicarMatriz(matrizOrigen, matrizTransformacion);
        return new PointXYDouble((int) resultado[0][0], (int) resultado[0][1]);
    }

    private PointXYDouble calcularTraslacion(PointXYDouble origen) {
        double[][] matrizOrigen = {{origen.x, origen.y, 1}};
        double[][] matrizTransformacion = {
                {
                        1,
                        0,
                        0
                },
                {
                        0,
                        1,
                        0
                },
                {
                        dx + dxAnimacion,
                        dy + dyAnimacion,
                        1
                }
        };

        double[][] resultado = multiplicarMatriz(matrizOrigen, matrizTransformacion);
        return new PointXYDouble(resultado[0][0], resultado[0][1]);
    }

    private PointXYInt calcularTraslacion(PointXYInt origen) {
        int[][] matrizOrigen = {{origen.x, origen.y, 1}};
        int[][] matrizTransformacion = {
                {
                        1,
                        0,
                        0
                },
                {
                        0,
                        1,
                        0
                },
                {
                        dx + dxAnimacion,
                        dy + dyAnimacion,
                        1
                }
        };

        int[][] resultado = multiplicarMatriz(matrizOrigen, matrizTransformacion);
        return new PointXYInt(resultado[0][0], resultado[0][1]);
    }

    private PointXYDouble calularRotacion(PointXYDouble origen) {
        double[][] matrizOrigen = {{origen.x, origen.y, 1}};
        double[][] matrizTransformacion = {
                {
                        (double) Math.cos(theta),
                        (double) Math.sin(theta),
                        0
                },
                {
                        (double) -Math.sin(theta),
                        (double) Math.cos(theta),
                        0
                },
                {
                        0,
                        0,
                        1
                }
        };

        double[][] resultado = multiplicarMatriz(matrizOrigen, matrizTransformacion);
        return new PointXYDouble(resultado[0][0], resultado[0][1]);
    }

    public void curva1(int x, int y, int ancho, int alto, int pasos) {
        PointXYInt puntoAnterior = new PointXYInt(x, y - (int) (Math.sin(0) * 200));
        for (double i = 0; i < Math.PI; i += Math.PI / pasos) {

            PointXYInt puntoNuevo = new PointXYInt(x + (int) (i * ancho), y - (int) (Math.sin(i) * alto));
            circuloPuntoMedio(puntoNuevo.x, puntoNuevo.y, 2);
            lineaBresenham(puntoAnterior.x, puntoAnterior.y, puntoNuevo.x, puntoNuevo.y);
            puntoAnterior = puntoNuevo;
        }
    }

    public void curvaHumo(int x, int y, int ancho, int alto, int paso) {
        PointXYInt puntoAnterior = new PointXYInt(x + (int) (0 * Math.cos(4 * 0) * ancho), y - (int) (0 * alto));
        for (double i = 0; i < 2 * Math.PI; i += Math.PI / 60) {

            PointXYInt puntoNuevo = new PointXYInt(x + (int) (i * Math.cos(4 * i + (paso / 10.0)) * ancho), y - (int) (i * alto));

            putPixel(puntoNuevo.x, puntoNuevo.y);
            lineaBresenham(puntoAnterior.x, puntoAnterior.y, puntoNuevo.x, puntoNuevo.y);
            puntoAnterior = puntoNuevo;
        }
    }

    public void curvaFlor(int xPos, int yPos, int ancho, int alto) {
        Double xI = Math.cos(0) + (1.0 / 2) * Math.cos(7 * 0) + (1.0 / 3) * Math.sin(17 * 0);
        Double yI = Math.sin(0) + (1.0 / 2) * Math.sin(7 * 0) + (1.0 / 3) * Math.cos(17 * 0);
        PointXYInt puntoAnterior = new PointXYInt((int) (xPos + xI * ancho), (int) (yPos + yI * alto));
        for (double i = 0; i < 2 * Math.PI; i += Math.PI / 400) {

            Double x = Math.cos(i) + (1.0 / 2) * Math.cos(7 * i) + (1.0 / 3) * Math.sin(17 * i);
            Double y = Math.sin(i) + (1.0 / 3) * Math.sin(7 * i) + (1.0 / 3) * Math.cos(17 * i);
            PointXYInt puntoNuevo = new PointXYInt((int) (xPos + x * ancho), (int) (yPos + y * alto));

            // System.out.println(puntoNuevo.x + "," + puntoNuevo.y);
            // putPixel(puntoNuevo.x, puntoNuevo.y);
            lineaBresenham(puntoAnterior.x, puntoAnterior.y, puntoNuevo.x, puntoNuevo.y);
            puntoAnterior = puntoNuevo;
        }
    }

    public void curvaSol(int xPos, int yPos, int ancho, int alto) {
        PointXYInt puntoAnterior = null;
        for (double i = 0; i <= 14 * Math.PI; i += Math.PI / 100) {

            Double x = 17 * Math.cos(i) + (7) * Math.cos((17.0 / 7) * i);
            Double y = 17 * Math.sin(i) - (7) * Math.sin((17.0 / 7) * i);
            PointXYInt puntoNuevo = new PointXYInt((int) (xPos + x * ancho), (int) (yPos + y * alto));

            // putPixel(puntoNuevo.x, puntoNuevo.y);
            if (puntoAnterior != null)
                lineaBresenham(puntoAnterior.x, puntoAnterior.y, puntoNuevo.x, puntoNuevo.y);
            puntoAnterior = puntoNuevo;
        }
    }

    public void curvaParametrica1(int xPos, int yPos, int ancho, int alto) {
        PointXYInt puntoAnterior = null;
        for (double i = 0; i <= 14 * Math.PI; i += Math.PI / 100) {

            Double x = i - 3 * Math.sin(i);
            Double y = 4 - 3 * Math.cos(i);
            PointXYInt puntoNuevo = new PointXYInt((int) (xPos + x * ancho), (int) (yPos + y * alto));

            // putPixel(puntoNuevo.x, puntoNuevo.y);
            if (puntoAnterior != null)
                lineaBresenham(puntoAnterior.x, puntoAnterior.y, puntoNuevo.x, puntoNuevo.y);
            puntoAnterior = puntoNuevo;
        }
    }

    public void curvaInfinito(int xPos, int yPos, int ancho) {
        PointXYInt puntoAnterior = null;
        for (double i = 0; i <= 2 * Math.PI; i += Math.PI / 100) {

            Double x = ancho * Math.sin(i) / (1 + Math.pow(Math.cos(i), 2));
            Double y = ancho * Math.sin(i) * Math.cos(i) / (1 + Math.pow(Math.cos(i), 2));
            PointXYInt puntoNuevo = new PointXYInt((int) (xPos + x), (int) (yPos + y));
            // putPixel(puntoNuevo.x, puntoNuevo.y);
            if (puntoAnterior != null)
                lineaBresenham(puntoAnterior.x, puntoAnterior.y, puntoNuevo.x, puntoNuevo.y);
            puntoAnterior = puntoNuevo;
        }
    }

    public void malla(int x1, int y1, int x2, int y2) {
        for (double x = x1; x <= x2; x += 4) {
            lineaBresenham((int) x, y1, (int) x, y2);
        }
        for (double y = y1; y <= y2; y += 10) {
            lineaBresenham(x1, (int) y, x2, (int) y);
        }
    }

    public void triangulo(int x1, int y1, int x2, int y2, int x3, int y3) {
        linea(x1, y1, x2, y2);
        linea(x2, y2, x3, y3);
        linea(x3, y3, x1, y1);
    }

    public void cuadrado(int x, int y, int k) {
        rectangulo(x, y, x + k, y + k);
    }

    public void poligono(int[] puntosX, int[] puntosY) {
        int longitud = puntosX.length;
        for (int i = 0; i < longitud - 1; i++) {
            linea(puntosX[i], puntosY[i], puntosX[i + 1], puntosY[i + 1]);
        }
        linea(puntosX[longitud - 1], puntosY[longitud - 1], puntosX[0], puntosY[0]);
    }

    public void rectanguloRelleno(int x1, int y1, int x2, int y2) {
        PointXYDouble pos1 = calcularTraslacion(calcularEscalacion(new PointXYDouble(x1, y1)));
        PointXYDouble pos2 = calcularTraslacion(calcularEscalacion(new PointXYDouble(x2, y2)));
        for (int y = (int) pos1.y; y <= pos2.y; y++) {
            for (int x = (int) pos1.x; x <= pos2.x; x++) {
                putPixel(x, y);
            }
        }
    }

    public void reiniciarTransformaciones() {
        sx = 1;
        sy = 1;
        dx = 0;
        dy = 0;
        theta = 0;
        dxAnimacion = 0;
        dyAnimacion = 0;

    }

    public void reiniciarRotacion() {
        theta = 0;
    }

    public void cuboParalelo(int x, int y, int z, int ancho) {
        PointXYZDouble vector = new PointXYZDouble(1, 0.5, 2);

        PointXYZInt[] puntos = new PointXYZInt[8];
        puntos[0] = new PointXYZInt(x, y, z);
        puntos[1] = new PointXYZInt(x + ancho, y, z);
        puntos[2] = new PointXYZInt(x + ancho, y + ancho, z);
        puntos[3] = new PointXYZInt(x, y + ancho, z);
        puntos[4] = new PointXYZInt(x, y, z + ancho);
        puntos[5] = new PointXYZInt(x + ancho, y, z + ancho);
        puntos[6] = new PointXYZInt(x + ancho, y + ancho, z + ancho);
        puntos[7] = new PointXYZInt(x, y + ancho, z + ancho);

        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = proyectarParalela(puntos[i], vector);
        }


        // Dibujar las aristas
        linea(puntos[0].x, puntos[0].y, puntos[1].x, puntos[1].y);
        linea(puntos[1].x, puntos[1].y, puntos[2].x, puntos[2].y);
        linea(puntos[2].x, puntos[2].y, puntos[3].x, puntos[3].y);
        linea(puntos[3].x, puntos[3].y, puntos[0].x, puntos[0].y);

        linea(puntos[4].x, puntos[4].y, puntos[5].x, puntos[5].y);
        linea(puntos[5].x, puntos[5].y, puntos[6].x, puntos[6].y);
        linea(puntos[6].x, puntos[6].y, puntos[7].x, puntos[7].y);
        linea(puntos[7].x, puntos[7].y, puntos[4].x, puntos[4].y);

        linea(puntos[0].x, puntos[0].y, puntos[4].x, puntos[4].y);
        linea(puntos[1].x, puntos[1].y, puntos[5].x, puntos[5].y);
        linea(puntos[2].x, puntos[2].y, puntos[6].x, puntos[6].y);
        linea(puntos[3].x, puntos[3].y, puntos[7].x, puntos[7].y);
    }

    public void tetris(int x, int y, int z, int ancho, double angulo) {
        PointXYZDouble vector = new PointXYZDouble(0, 0, 0);

        PointXYZInt[] puntos = new PointXYZInt[16];
        puntos[0] = new PointXYZInt(x, y, z);
        puntos[1] = new PointXYZInt(x + ancho, y, z);
        puntos[2] = new PointXYZInt(x + ancho, y + ancho, z);
        puntos[3] = new PointXYZInt(x + ancho * 2, y + ancho, z);
        puntos[4] = new PointXYZInt(x + ancho * 2, y + ancho * 2, z);
        puntos[5] = new PointXYZInt(x + ancho, y + ancho * 2, z);
        puntos[6] = new PointXYZInt(x + ancho, y + ancho * 3, z);
        puntos[7] = new PointXYZInt(x, y + ancho * 3, z);

        puntos[8] = new PointXYZInt(x, y, z + ancho);
        puntos[9] = new PointXYZInt(x + ancho, y, z + ancho);
        puntos[10] = new PointXYZInt(x + ancho, y + ancho, z + ancho);
        puntos[11] = new PointXYZInt(x + ancho * 2, y + ancho, z + ancho);
        puntos[12] = new PointXYZInt(x + ancho * 2, y + ancho * 2, z + ancho);
        puntos[13] = new PointXYZInt(x + ancho, y + ancho * 2, z + ancho);
        puntos[14] = new PointXYZInt(x + ancho, y + ancho * 3, z + ancho);
        puntos[15] = new PointXYZInt(x, y + ancho * 3, z + ancho);

        // Rotate around the y-axis
        double cosTheta = Math.cos(angulo);
        double sinTheta = Math.sin(angulo);
        for (int i = 0; i < puntos.length; i++) {
            PointXYZInt punto = puntos[i];
            double rotatedX = punto.x * cosTheta + punto.z * sinTheta;
            double rotatedZ = -punto.x * sinTheta + punto.z * cosTheta;
            puntos[i] = new PointXYZInt((int) rotatedX, punto.y, (int) rotatedZ);
        }


        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = proyectarParalela(puntos[i], vector);
        }


        linea(puntos[0].x, puntos[0].y, puntos[1].x, puntos[1].y);
        linea(puntos[1].x, puntos[1].y, puntos[2].x, puntos[2].y);
        linea(puntos[2].x, puntos[2].y, puntos[3].x, puntos[3].y);
        linea(puntos[3].x, puntos[3].y, puntos[4].x, puntos[4].y);
        linea(puntos[4].x, puntos[4].y, puntos[5].x, puntos[5].y);
        linea(puntos[5].x, puntos[5].y, puntos[6].x, puntos[6].y);
        linea(puntos[6].x, puntos[6].y, puntos[7].x, puntos[7].y);
        linea(puntos[7].x, puntos[7].y, puntos[0].x, puntos[0].y);


        linea(puntos[8].x, puntos[8].y, puntos[9].x, puntos[9].y);
        linea(puntos[9].x, puntos[9].y, puntos[10].x, puntos[10].y);
        linea(puntos[10].x, puntos[10].y, puntos[11].x, puntos[11].y);
        linea(puntos[11].x, puntos[11].y, puntos[12].x, puntos[12].y);
        linea(puntos[12].x, puntos[12].y, puntos[13].x, puntos[13].y);
        linea(puntos[13].x, puntos[13].y, puntos[14].x, puntos[14].y);
        linea(puntos[14].x, puntos[14].y, puntos[15].x, puntos[15].y);
        linea(puntos[15].x, puntos[15].y, puntos[8].x, puntos[8].y);


        linea(puntos[0].x, puntos[0].y, puntos[8].x, puntos[8].y);
        linea(puntos[1].x, puntos[1].y, puntos[9].x, puntos[9].y);
        linea(puntos[2].x, puntos[2].y, puntos[10].x, puntos[10].y);
        linea(puntos[3].x, puntos[3].y, puntos[11].x, puntos[11].y);
        linea(puntos[4].x, puntos[4].y, puntos[12].x, puntos[12].y);
        linea(puntos[5].x, puntos[5].y, puntos[13].x, puntos[13].y);
        linea(puntos[6].x, puntos[6].y, puntos[14].x, puntos[14].y);
        linea(puntos[7].x, puntos[7].y, puntos[15].x, puntos[15].y);
    }

    public PointXYZInt proyectarParalela(PointXYZInt punto, PointXYZDouble vector) {
        double u;
        if (vector.z == 0) {
            u = 0;
        } else {
            u = (double) (-punto.z / vector.z);
        }
        int x = (int) (punto.x + vector.x * u);
        int y = (int) (punto.y + vector.y * u);
        return new PointXYZInt(x, y, 0);
    }

    public PointXYInt proyectarPerspectiva(PointXYZDouble punto) {
        double u;
        if ((punto.z - vectorProyeccion.z) == 0) {
            u = 0;
        } else {
            u = (double) (-vectorProyeccion.z / (punto.z - vectorProyeccion.z));
        }
        double x = (vectorProyeccion.x + ((double) punto.x - (double) vectorProyeccion.x) * u);

        double y = (vectorProyeccion.y + ((double) punto.y - (double) vectorProyeccion.y) * u);

        return new PointXYInt((int) x, (int) y);
    }

    public void cuboPerspectiva(int x, int y, int z, int ancho, int alto, int largo) {

        PointXYZDouble[] puntos = new PointXYZDouble[8];

        puntos[0] = new PointXYZDouble(x - ancho / 2, y - alto / 2, z - largo / 2);
        puntos[1] = new PointXYZDouble(x + ancho / 2, y - alto / 2, z - largo / 2);
        puntos[2] = new PointXYZDouble(x + ancho / 2, y + alto / 2, z - largo / 2);
        puntos[3] = new PointXYZDouble(x - ancho / 2, y + alto / 2, z - largo / 2);
        puntos[4] = new PointXYZDouble(x - ancho / 2, y - alto / 2, z + largo / 2);
        puntos[5] = new PointXYZDouble(x + ancho / 2, y - alto / 2, z + largo / 2);
        puntos[6] = new PointXYZDouble(x + ancho / 2, y + alto / 2, z + largo / 2);
        puntos[7] = new PointXYZDouble(x - ancho / 2, y + alto / 2, z + largo / 2);

        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = calcularTransformaciones3D(puntos[i]);
        }


        PointXYInt[] puntosFinales = new PointXYInt[14];
        for (int i = 0; i < puntos.length; i++) {
            puntosFinales[i] = proyectarPerspectiva(puntos[i]);
        }


        // Dibujar las aristas
        linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[1].x, puntosFinales[1].y);
        linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[2].x, puntosFinales[2].y);
        linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[3].x, puntosFinales[3].y);
        linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[0].x, puntosFinales[0].y);

        linea(puntosFinales[4].x, puntosFinales[4].y, puntosFinales[5].x, puntosFinales[5].y);
        linea(puntosFinales[5].x, puntosFinales[5].y, puntosFinales[6].x, puntosFinales[6].y);
        linea(puntosFinales[6].x, puntosFinales[6].y, puntosFinales[7].x, puntosFinales[7].y);
        linea(puntosFinales[7].x, puntosFinales[7].y, puntosFinales[4].x, puntosFinales[4].y);

        linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[4].x, puntosFinales[4].y);
        linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[5].x, puntosFinales[5].y);
        linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[6].x, puntosFinales[6].y);
        linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[7].x, puntosFinales[7].y);

    }

    public void cuboPerspectivaFill(int x, int y, int z, int ancho, int alto, int largo) {

        PointXYZDouble[] puntos = new PointXYZDouble[14];

        puntos[0] = new PointXYZDouble(x - ancho / 2, y - alto / 2, z - largo / 2);
        puntos[1] = new PointXYZDouble(x + ancho / 2, y - alto / 2, z - largo / 2);
        puntos[2] = new PointXYZDouble(x + ancho / 2, y + alto / 2, z - largo / 2);
        puntos[3] = new PointXYZDouble(x - ancho / 2, y + alto / 2, z - largo / 2);
        puntos[4] = new PointXYZDouble(x - ancho / 2, y - alto / 2, z + largo / 2);
        puntos[5] = new PointXYZDouble(x + ancho / 2, y - alto / 2, z + largo / 2);
        puntos[6] = new PointXYZDouble(x + ancho / 2, y + alto / 2, z + largo / 2);
        puntos[7] = new PointXYZDouble(x - ancho / 2, y + alto / 2, z + largo / 2);

        puntos[8] = new PointXYZDouble(x - ancho / 2, 0, 0);
        puntos[9] = new PointXYZDouble(0, 0, z - largo / 2);
        puntos[10] = new PointXYZDouble(0, y - alto / 2, 0);

        puntos[11] = new PointXYZDouble(x + ancho / 2, 0, 0);
        puntos[12] = new PointXYZDouble(0, 0, z + largo / 2);
        puntos[13] = new PointXYZDouble(0, y + alto / 2, 0);

        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = calcularTransformaciones3D(puntos[i]);
        }
        PointXYZDouble[] puntosLinea = puntos.clone();


        double zMenor = Double.MAX_VALUE;
        int indexMenor = -1;
        for (int i = 0; i < 8; i++) {
            if (puntos[i].z < zMenor) {
                zMenor = puntos[i].z;
                indexMenor = i;
            }
        }
        puntos[indexMenor] = null;

        PointXYInt[] puntosFinales = new PointXYInt[14];
        for (int i = 0; i < 14; i++) {
            if (indexMenor != i)
                puntosFinales[i] = proyectarPerspectiva(puntos[i]);
        }


        color = Color.ORANGE;
        // Dibujar las aristas
        if (!(puntos[0] == null || puntos[1] == null))
            linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[1].x, puntosFinales[1].y);
        if (!(puntos[1] == null || puntos[2] == null))
            linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[2].x, puntosFinales[2].y);
        if (!(puntos[2] == null || puntos[3] == null))
            linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[3].x, puntosFinales[3].y);
        if (!(puntos[3] == null || puntos[0] == null))
            linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[0].x, puntosFinales[0].y);

        if (!(puntos[4] == null || puntos[5] == null))
            linea(puntosFinales[4].x, puntosFinales[4].y, puntosFinales[5].x, puntosFinales[5].y);
        if (!(puntos[5] == null || puntos[6] == null))
            linea(puntosFinales[5].x, puntosFinales[5].y, puntosFinales[6].x, puntosFinales[6].y);
        if (!(puntos[6] == null || puntos[7] == null))
            linea(puntosFinales[6].x, puntosFinales[6].y, puntosFinales[7].x, puntosFinales[7].y);
        if (!(puntos[7] == null || puntos[4] == null))
            linea(puntosFinales[7].x, puntosFinales[7].y, puntosFinales[4].x, puntosFinales[4].y);

        if (!(puntos[0] == null || puntos[4] == null))
            linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[4].x, puntosFinales[4].y);
        if (!(puntos[1] == null || puntos[5] == null))
            linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[5].x, puntosFinales[5].y);
        if (!(puntos[2] == null || puntos[6] == null))
            linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[6].x, puntosFinales[6].y);
        if (!(puntos[3] == null || puntos[7] == null))
            linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[7].x, puntosFinales[7].y);

        color = Color.ORANGE;
        floodFill(puntosFinales[8].x, puntosFinales[8].y);
        floodFill(puntosFinales[9].x, puntosFinales[9].y);
        floodFill(puntosFinales[10].x, puntosFinales[10].y);
        floodFill(puntosFinales[11].x, puntosFinales[11].y);
        floodFill(puntosFinales[12].x, puntosFinales[12].y);
        floodFill(puntosFinales[13].x, puntosFinales[13].y);


        color = Color.RED;
        if (!(puntos[0] == null || puntos[1] == null))
            linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[1].x, puntosFinales[1].y);
        if (!(puntos[1] == null || puntos[2] == null))
            linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[2].x, puntosFinales[2].y);
        if (!(puntos[2] == null || puntos[3] == null))
            linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[3].x, puntosFinales[3].y);
        if (!(puntos[3] == null || puntos[0] == null))
            linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[0].x, puntosFinales[0].y);

        if (!(puntos[4] == null || puntos[5] == null))
            linea(puntosFinales[4].x, puntosFinales[4].y, puntosFinales[5].x, puntosFinales[5].y);
        if (!(puntos[5] == null || puntos[6] == null))
            linea(puntosFinales[5].x, puntosFinales[5].y, puntosFinales[6].x, puntosFinales[6].y);
        if (!(puntos[6] == null || puntos[7] == null))
            linea(puntosFinales[6].x, puntosFinales[6].y, puntosFinales[7].x, puntosFinales[7].y);
        if (!(puntos[7] == null || puntos[4] == null))
            linea(puntosFinales[7].x, puntosFinales[7].y, puntosFinales[4].x, puntosFinales[4].y);

        if (!(puntos[0] == null || puntos[4] == null))
            linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[4].x, puntosFinales[4].y);
        if (!(puntos[1] == null || puntos[5] == null))
            linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[5].x, puntosFinales[5].y);
        if (!(puntos[2] == null || puntos[6] == null))
            linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[6].x, puntosFinales[6].y);
        if (!(puntos[3] == null || puntos[7] == null))
            linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[7].x, puntosFinales[7].y);


    }

    public void linea3D(int x1, int y1, int z1, int x2, int y2, int z2) {
        PointXYZDouble[] puntos = new PointXYZDouble[2];

        puntos[0] = new PointXYZDouble(x1, y1, z1);
        puntos[1] = new PointXYZDouble(x2, y2, z2);
        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = calcularTransformaciones3D(puntos[i]);
        }

        PointXYInt[] puntosFinales = new PointXYInt[2];
        for (int i = 0; i < puntos.length; i++) {
            puntosFinales[i] = proyectarPerspectiva(puntos[i]);
        }
        linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[1].x, puntosFinales[1].y);
    }

    private PointXYZDouble calcularTransformaciones3D(PointXYZDouble punto) {
        punto = traslacion3D(punto);
        punto = rotacion3D(punto);
        punto = escalacion3D(punto);
        return punto;
    }

    public void curvaReloj(int xPos, int yPos, int zPos, int ancho) {
        PointXYInt[][] puntos = new PointXYInt[21][21];
        int cordX = 0;
        int cordY = 0;
        for (double t = 0; t <= 2 * Math.PI; t += Math.PI / 10) {
            cordY = 0;
            for (double r = 0; r <= 2 * Math.PI; r += Math.PI / 10) {

                double x = (2 + Math.cos(t)) * Math.cos(r);
                double y = (2 + Math.cos(t)) * Math.sin(r);
                double z = t;
                PointXYZDouble punto3D = new PointXYZDouble(x, y, z);
                punto3D = calcularTransformaciones3D(punto3D);
                PointXYInt puntoNuevo = proyectarPerspectiva(punto3D);
                puntos[cordX][cordY] = puntoNuevo;
                cordY++;
            }
            cordX++;
        }
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                PointXYInt punto = puntos[x][y];

                if (x != 20) {
                    PointXYInt puntoDerecha = puntos[x + 1][y];
                    linea(punto.x, punto.y, puntoDerecha.x, puntoDerecha.y);
                }
                if (y != 20) {
                    PointXYInt puntoArriba = puntos[x][y + 1];
                    linea(punto.x, punto.y, puntoArriba.x, puntoArriba.y);
                }
            }
        }
    }

    public void cubo(int x, int y, int z, int ancho, int alto, int largo) {

        PointXYZDouble[] vertices = new PointXYZDouble[8];

        vertices[0] = new PointXYZDouble(x - ancho / 2, y - alto / 2, z - largo / 2);
        vertices[1] = new PointXYZDouble(x + ancho / 2, y - alto / 2, z - largo / 2);
        vertices[2] = new PointXYZDouble(x + ancho / 2, y + alto / 2, z - largo / 2);
        vertices[3] = new PointXYZDouble(x - ancho / 2, y + alto / 2, z - largo / 2);
        vertices[4] = new PointXYZDouble(x - ancho / 2, y - alto / 2, z + largo / 2);
        vertices[5] = new PointXYZDouble(x + ancho / 2, y - alto / 2, z + largo / 2);
        vertices[6] = new PointXYZDouble(x + ancho / 2, y + alto / 2, z + largo / 2);
        vertices[7] = new PointXYZDouble(x - ancho / 2, y + alto / 2, z + largo / 2);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = calcularTransformaciones3D(vertices[i]);
        }

        Face[] faces = new Face[6];

        // Face 1: Front
        PointXYZDouble[] face1Vertices = {vertices[0], vertices[3], vertices[2], vertices[1]};
        faces[0] = new Face(face1Vertices);

        // Face 2: Back
        PointXYZDouble[] face2Vertices = {vertices[4], vertices[5], vertices[6], vertices[7]};
        faces[1] = new Face(face2Vertices);

        // Face 3: Top
        PointXYZDouble[] face3Vertices = {vertices[3], vertices[7], vertices[6], vertices[2]};
        faces[2] = new Face(face3Vertices);

        // Face 4: Bottom
        PointXYZDouble[] face4Vertices = {vertices[0], vertices[1], vertices[5], vertices[4]};
        faces[3] = new Face(face4Vertices);

        // Face 5: Left
        PointXYZDouble[] face5Vertices = {vertices[0], vertices[4], vertices[7], vertices[3]};
        faces[4] = new Face(face5Vertices);

        // Face 6: Right
        PointXYZDouble[] face6Vertices = {vertices[1], vertices[2], vertices[6], vertices[5]};
        faces[5] = new Face(face6Vertices);


        drawFaces(faces);
    }

    public void plano(int x, int y, int z, int ancho, int largo) {
        PointXYZDouble[] vertices = new PointXYZDouble[4];

        vertices[0] = new PointXYZDouble(x - ancho / 2, y, z - largo / 2);
        vertices[1] = new PointXYZDouble(x + ancho / 2, y, z - largo / 2);
        vertices[2] = new PointXYZDouble(x + ancho / 2, y, z + largo / 2);
        vertices[3] = new PointXYZDouble(x - ancho / 2, y, z + largo / 2);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = calcularTransformaciones3D(vertices[i]);
        }
        Face[] face = {new Face(vertices)};
        drawFaces(face);

    }

    public void plano(PointXYZInt punto1, PointXYZInt punto2, PointXYZInt punto3, PointXYZInt punto4) {
        PointXYZDouble[] vertices = new PointXYZDouble[4];

        vertices[0] = new PointXYZDouble(punto1.x, punto1.y, punto1.z);
        vertices[1] = new PointXYZDouble(punto2.x, punto2.y, punto2.z);
        vertices[2] = new PointXYZDouble(punto3.x, punto3.y, punto3.z);
        vertices[3] = new PointXYZDouble(punto4.x, punto4.y, punto4.z);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = calcularTransformaciones3D(vertices[i]);
        }
        Face[] face = {new Face(vertices)};
        drawFaces(face);

    }

    private void drawFaces(Face[] faces) {

        PointXYZDouble cameraPosition = new PointXYZDouble(vectorProyeccion.x, vectorProyeccion.y, -vectorProyeccion.z);

        Color colorGuardado = color;
        for (int i = 0; i < faces.length; i++) {
            PointXYZDouble normal = faces[i].getNormal();


            PointXYZDouble direction = new PointXYZDouble(
                    normal.x - cameraPosition.x,
                    normal.y - cameraPosition.y,
                    normal.z - cameraPosition.z
            );

            double dotProduct = normal.dotProduct(direction);
            if (dotProduct > 0) {
                Face currentFace = faces[i];

                PointXYInt[] puntosFinales = new PointXYInt[4];
                for (int j = 0; j < 4; j++) {
                    puntosFinales[j] = proyectarPerspectiva(currentFace.vertices[j]);
                }

                // calcular el angulo en radianes
                double angle = Math.acos(dotProduct / -vectorProyeccion.z);

                double angleDegrees = Math.toDegrees(angle);
                int redCara = (int) Math.max(0, Math.min(255, color.getRed() - angleDegrees * 0.7));
                int greenCara = (int) Math.max(0, Math.min(255, color.getGreen() - angleDegrees * 0.7));
                int blueCara = (int) Math.max(0, Math.min(255, color.getBlue() - angleDegrees * 0.7));

                color = new Color(redCara, greenCara, blueCara);
                linea(puntosFinales[0].x, puntosFinales[0].y, puntosFinales[1].x, puntosFinales[1].y);
                linea(puntosFinales[1].x, puntosFinales[1].y, puntosFinales[2].x, puntosFinales[2].y);
                linea(puntosFinales[2].x, puntosFinales[2].y, puntosFinales[3].x, puntosFinales[3].y);
                linea(puntosFinales[3].x, puntosFinales[3].y, puntosFinales[0].x, puntosFinales[0].y);


                PointXYZDouble centro = currentFace.calculateCenterPoint();
                PointXYInt centro2d = proyectarPerspectiva(centro);

                scanLineFill(centro2d.x, centro2d.y);
            }

            color = colorGuardado;
        }
    }

    private PointXYZDouble rotacion3D(PointXYZDouble origen) {

        double[][] matrizOrigen = {{origen.x, origen.y, origen.z, 1}};
        // Convert angles to radians
        double radianX = vectorRotacion.x;
        double radianY = vectorRotacion.y;
        double radianZ = vectorRotacion.z;

        // Create the rotation matrix
        double[][] matrizTransformacion = {
                {Math.cos(radianY) * Math.cos(radianZ), -Math.cos(radianX) * Math.sin(radianZ) + Math.sin(radianX) * Math.sin(radianY) * Math.cos(radianZ), Math.sin(radianX) * Math.sin(radianZ) + Math.cos(radianX) * Math.sin(radianY) * Math.cos(radianZ), 0},
                {Math.cos(radianY) * Math.sin(radianZ), Math.cos(radianX) * Math.cos(radianZ) + Math.sin(radianX) * Math.sin(radianY) * Math.sin(radianZ), -Math.sin(radianX) * Math.cos(radianZ) + Math.cos(radianX) * Math.sin(radianY) * Math.sin(radianZ), 0},
                {-Math.sin(radianY), Math.sin(radianX) * Math.cos(radianY), Math.cos(radianX) * Math.cos(radianY), 0},
                {0, 0, 0, 1}
        };

        double[][] resultado = multiplicarMatriz4x4(matrizOrigen, matrizTransformacion);

        return new PointXYZDouble(resultado[0][0], resultado[0][1], resultado[0][2]);
    }

    private PointXYZDouble traslacion3D(PointXYZDouble origen) {

        double[][] matrizOrigen = {{origen.x, origen.y, origen.z, 1}};
        // Create the rotation matrix
        double[][] matrizTransformacion = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {vectorTraslacion.x, vectorTraslacion.y, vectorTraslacion.z, 1}
        };
        double[][] resultado = multiplicarMatriz4x4(matrizOrigen, matrizTransformacion);

        return new PointXYZDouble(resultado[0][0], resultado[0][1], resultado[0][2]);
    }

    private PointXYZDouble escalacion3D(PointXYZDouble origen) {

        double[][] matrizOrigen = {{origen.x, origen.y, origen.z, 1}};
        // Create the rotation matrix
        double[][] matrizTransformacion = {
                {vectorEscalacion.x, 0, 0, 0},
                {0, vectorEscalacion.y, 0, 0},
                {0, 0, vectorEscalacion.z, 0},
                {0, 0, 0, 1}
        };
        double[][] resultado = multiplicarMatriz4x4(matrizOrigen, matrizTransformacion);

        return new PointXYZDouble(resultado[0][0], resultado[0][1], resultado[0][2]);
    }

    private PointXYInt calcularTransformaciones(PointXYInt puntoOrigen) {
        PointXYDouble punto = new PointXYDouble(puntoOrigen.x, puntoOrigen.y);
        PointXYDouble puntoDestino = calcularTraslacion(calcularEscalacion(calularRotacion(punto)));
        return new PointXYInt((int) puntoDestino.x, (int) puntoDestino.y);
    }

    private double[][] multiplicarMatriz(double[][] matrizOrigen, double[][] matrizTransformacion) {
        double[][] resultado = new double[1][3];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    resultado[i][j] += matrizOrigen[i][k] * matrizTransformacion[k][j];
                }
            }
        }
        return resultado;
    }

    private double[][] multiplicarMatriz4x4(double[][] matrizOrigen, double[][] matrizTransformacion) {
        int filasOrigen = matrizOrigen.length;
        int columnasOrigen = matrizOrigen[0].length;
        int columnasTransformacion = matrizTransformacion[0].length;

        double[][] resultado = new double[filasOrigen][columnasTransformacion];

        for (int i = 0; i < filasOrigen; i++) {
            for (int j = 0; j < columnasTransformacion; j++) {
                for (int k = 0; k < columnasOrigen; k++) {
                    resultado[i][j] += matrizOrigen[i][k] * matrizTransformacion[k][j];
                }
            }
        }

        return resultado;
    }

    private int[][] multiplicarMatriz(int[][] matrizOrigen, int[][] matrizTransformacion) {
        int[][] resultado = new int[1][3];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    resultado[i][j] += matrizOrigen[i][k] * matrizTransformacion[k][j];
                }
            }
        }
        return resultado;
    }

    public void traslacion(int dxNuevo, int dyNuevo) {
        dx = dxNuevo;
        dy = dyNuevo;
    }

    public void traslacionAnimacion(int dxNuevo, int dyNuevo) {
        dxAnimacion = dxNuevo;
        dyAnimacion = dyNuevo;
    }

    public void escalacion(double sxNuevo, double syNuevo) {
        sx = sxNuevo;
        sy = syNuevo;
    }

    public void escalacionAnimacion(double sxNuevo, double syNuevo) {
        sxAnimacion = sxNuevo;
        syAnimacion = syNuevo;
    }

    public void rotacion(double anguloNuevo) {
        theta = anguloNuevo;
    }

    public void setVectorProyeccion(PointXYZInt vectorProyeccion) {
        this.vectorProyeccion = vectorProyeccion;
    }

    public void setVectorRotacion(double x, double y, double z) {
        this.vectorRotacion = new PointXYZDouble(x, y, z);
    }

    public void setVectorEscalacion(double x, double y, double z) {
        this.vectorEscalacion = new PointXYZDouble(x, y, z);
    }

    public void setVectorTraslacion(double x, double y, double z) {
        this.vectorTraslacion = new PointXYZDouble(x, y, z);
    }

    public void drawToBuffer(Image buffer, JFrame frame) {
        buffer.getGraphics().drawImage(image, 0, 0, frame);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void resetImage(JFrame ventana) {
        this.image = new BufferedImage(ventana.getWidth(), ventana.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

}
