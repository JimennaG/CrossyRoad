package crossyroad;

import Objects.DibujarGallina;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;

public class main3D extends JFrame {

    // COLORES
    Color verdeFuerte = new Color(103, 147, 44);
    Color verdeClaro = new Color(125, 175, 55);
    Color azulFuerte = new Color(96, 182, 215);
    Color azulClaro = new Color(114, 216, 255);
    Color grisFuerte = new Color(84, 92, 95);
    Color grisClaro = new Color(100, 100, 100);

    private proyecciones proye;
    private LineaBresenham lineaBresenham;
    private final Canvas pixel;
    private int withPantalla = 500, heightPantalla = 650;

    private List<Plataforma> plataformas;
    private cositas decoraciones;
    private Random random;

    public main3D() {
        pixel = new Canvas();
        lineaBresenham = new LineaBresenham();
        proye = new proyecciones();
        plataformas = new CopyOnWriteArrayList<>();
        decoraciones = new cositas(proye, lineaBresenham);
        random = new Random();

        setTitle("PROYECTO 3D");
        setLayout(new BorderLayout());
        add(pixel, BorderLayout.CENTER);
        setSize(withPantalla, heightPantalla);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Inicializar las plataformas de manera que cubran todo el panel
        int altura = 160; // Altura de cada plataforma
        int separacion = 70; // Espacio entre plataformas
        int numPlataformas = (heightPantalla / (altura + separacion)) + 150; // Número de plataformas para cubrir el panel

        for (int i = 0; i < numPlataformas; i++) {
            Color colorFuerte;
            Color colorClaro;
            if (i % 3 == 0) {
                colorFuerte = verdeFuerte;
                colorClaro = verdeClaro;
            } else if (i % 3 == 1) {
                colorFuerte = azulFuerte;
                colorClaro = azulClaro;
            } else {
                colorFuerte = grisFuerte;
                colorClaro = grisClaro;
            }
            agregarPlataforma(colorFuerte, colorClaro, i * (altura + separacion));
        }

        // Configurar el temporizador para actualizar la animación
        Timer timer = new Timer(20, e -> {
            for (Plataforma plataforma : plataformas) {
                plataforma.desplazar();
            }
            pixel.repaint();
        });
        timer.start();
    }

    private void agregarPlataforma(Color colorFuerte, Color colorClaro, int yInicio) {
        Plataforma plataforma = new Plataforma(colorFuerte, colorClaro, yInicio);
        plataformas.add(plataforma);
    }

    private class Canvas extends JPanel {

        private BufferedImage buffer;

        public Canvas() {
            buffer = new BufferedImage(withPantalla, heightPantalla, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = buffer.createGraphics();
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, withPantalla, heightPantalla);
            for (Plataforma plataforma : plataformas) {
                plataforma.render(g2d);
            }
            g.drawImage(buffer, 0, 0, null);
        }

        public void putPixel(int x, int y, Color color) {
            Graphics g = buffer.getGraphics();
            if (g != null) {
                g.setColor(color);
                g.fillRect(x, y, 1, 1);
            }
        }

        public void fillPolygon(List<Point> points, Color color) {
            int n = points.size();
            int[] xPoints = new int[n];
            int[] yPoints = new int[n];
            for (int i = 0; i < n; i++) {
                xPoints[i] = points.get(i).x;
                yPoints[i] = points.get(i).y;
            }
            Graphics g = buffer.getGraphics();
            if (g != null) {
                g.setColor(color);
                g.fillPolygon(xPoints, yPoints, n);
            }
        }
    }

    private class Plataforma {

        private final Color colorFuerte;
        private final Color colorClaro;
        private int yInicio;
        private int desplazamiento = 0;
        private int altura = 160; // Definir la altura de la plataforma
        private int separacion = 70; // Espacio entre plataformas
        private List<int[]> posicionesArboles; // Lista de posiciones para los árboles
        private List<Carrito> carritos; // Lista de carritos en la plataforma
        private List<Trailer> trailers; // Lista de trailers en la plataforma
        private List<int[]> posicionesTroncos; // Lista de posiciones para los troncos

        public Plataforma(Color colorFuerte, Color colorClaro, int yInicio) {
            this.colorFuerte = colorFuerte;
            this.colorClaro = colorClaro;
            this.yInicio = yInicio;
            if (colorFuerte.equals(verdeFuerte)) {
                inicializarArboles();
            }
            if (colorFuerte.equals(grisFuerte)) {
                inicializarVehiculos();
            }
            if (colorFuerte.equals(azulFuerte)) {
                inicializarTroncos();
            }
        }

        private void inicializarArboles() {
            posicionesArboles = decoraciones.generarPosicionesAleatorias(0, altura); // Generar posiciones aleatorias en toda la altura de la plataforma
        }

        private void inicializarVehiculos() {
            carritos = new ArrayList<>();
            trailers = new ArrayList<>();
            int numVehiculos = 3; // Número de vehículos en la plataforma
            for (int i = 0; i < numVehiculos; i++) {
                int x = withPantalla + 800; // Posición inicial del carrito
                if (Math.random() < 0.5) {
                    carritos.add(new Carrito(x, 50, false));
                } else {
                    trailers.add(new Trailer(x, 50, false));
                }
            }
        }

        private void inicializarTroncos() {
            posicionesTroncos = decoraciones.generarPosicionesAleatoriasTroncos(30, altura - 60, 2 + random.nextInt(2));
        }

        public void desplazar() {
            desplazamiento -= 2;
            if (yInicio + desplazamiento < -altura) {
                yInicio += (plataformas.size() * (altura + separacion));
                desplazamiento = 0;
                if (colorFuerte.equals(verdeFuerte)) {
                    inicializarArboles();
                }
                if (colorFuerte.equals(grisFuerte)) {
                    inicializarVehiculos();
                }
                if (colorFuerte.equals(azulFuerte)) {
                    inicializarTroncos();
                }
            }
        }

        public void render(Graphics g) {
            int largo = withPantalla + 1000;
            int grosor = 15;

            int[][] puntos = {
                {0, yInicio + desplazamiento, 0},
                {largo, yInicio + 2 + desplazamiento, 0},
                {largo, yInicio + altura + 2 + desplazamiento, 0},
                {0, yInicio + altura + desplazamiento, 0},
                {0, yInicio + desplazamiento, grosor},
                {largo, yInicio + 2 + desplazamiento, grosor},
                {largo, yInicio + altura + 2 + desplazamiento, grosor},
                {0, yInicio + altura + desplazamiento, grosor}
            };

            int[][] puntosProyectados = new int[8][2];

            for (int i = 0; i < 8; i++) {
                puntosProyectados[i] = proye.proyeccionIsometrica(puntos[i][0], puntos[i][1], puntos[i][2]);
            }

            List<Point> plataformaSuperior = new ArrayList<>();
            List<Point> plataformaInferior = new ArrayList<>();
            List<Point> bordeInferior1 = new ArrayList<>();
            List<Point> bordeInferior2 = new ArrayList<>();
            List<Point> bordeLateral1 = new ArrayList<>();
            List<Point> bordeLateral2 = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                plataformaSuperior.add(new Point(puntosProyectados[i][0], puntosProyectados[i][1]));
                plataformaInferior.add(new Point(puntosProyectados[i + 4][0], puntosProyectados[i + 4][1]));
            }

            bordeInferior1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectados[4][0], puntosProyectados[4][1], puntosProyectados[5][0], puntosProyectados[5][1]));
            bordeInferior2.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectados[6][0], puntosProyectados[6][1], puntosProyectados[7][0], puntosProyectados[7][1]));
            bordeLateral1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectados[0][0], puntosProyectados[0][1], puntosProyectados[4][0], puntosProyectados[4][1]));
            bordeLateral2.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectados[1][0], puntosProyectados[1][1], puntosProyectados[5][0], puntosProyectados[5][1]));

            pixel.fillPolygon(plataformaSuperior, colorFuerte);
            pixel.fillPolygon(plataformaInferior, colorClaro);

            // Dibujar bordes con validación de límites
            dibujarBorde(g, bordeInferior1, colorFuerte);
            dibujarBorde(g, bordeInferior2, colorFuerte);
            dibujarBorde(g, bordeLateral1, colorFuerte);
            dibujarBorde(g, bordeLateral2, colorFuerte);

            // Dibujar árboles en plataformas de pasto
            if (colorFuerte.equals(verdeFuerte)) {
                for (int[] pos : posicionesArboles) {
                    decoraciones.dibujarArbol(g, pos[0], pos[1] + yInicio + desplazamiento, 5);
                }
            }

            // Dibujar carritos o trailers en plataformas de concreto
            if (colorFuerte.equals(grisFuerte)) {
                for (Carrito carrito : carritos) {
                    carrito.mover();
                    if (carrito.getX() > -400) {
                        decoraciones.dibujarCarritoIzquierda(g, carrito.getX(), yInicio + desplazamiento + carrito.getY(), 0);
                    } else {
                        carrito.setX(withPantalla + 800);
                    }
                }
                for (Trailer trailer : trailers) {
                    trailer.mover();
                    if (trailer.getX() > -460) {
                        decoraciones.dibujarTrailerIzquierda(g, trailer.getX(), yInicio + desplazamiento + trailer.getY(), 0);
                    } else {
                        trailer.setX(withPantalla + 800);
                    }
                }
            }

            // Dibujar troncos en plataformas de agua
            if (colorFuerte.equals(azulFuerte)) {
                for (int[] pos : posicionesTroncos) {
                    int x = pos[0];
                    int y = yInicio + desplazamiento + pos[1];
                    if (y >= 0 && y < heightPantalla) {
                        decoraciones.dibujarTronco(g, x, y, 0);
                    }
                }
            }
            
            decoraciones.dibujarGallina(g, 400, 50, 30);
        }

        private void dibujarBorde(Graphics g, List<Point> puntos, Color color) {
            g.setColor(color);
            for (Point punto : puntos) {
                if (punto.x >= 0 && punto.x < withPantalla && punto.y >= 0 && punto.y < heightPantalla) {
                    pixel.putPixel(punto.x, punto.y, color);
                }
            }
        }
    }

    public class Carrito {

        private int x;
        private int y;
        private int velocidad = 4; // Velocidad del carrito

        public Carrito(int x, int y, boolean haciaDerecha) {
            this.x = x;
            this.y = y;
        }

        public void mover() {
            x -= velocidad;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public class Trailer {

        private int x;
        private int y;
        private int velocidad = 2; // Velocidad del tráiler

        public Trailer(int x, int y, boolean haciaDerecha) {
            this.x = x;
            this.y = y;
        }

        public void mover() {
            x -= velocidad;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static void main(String[] args) {
        new main3D();
    }
}
