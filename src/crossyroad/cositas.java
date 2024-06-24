package crossyroad;

import Objects.AnimadorGallina;
import Objects.DibujarGallina;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class cositas {

    private AnimadorGallina animador;
    private proyecciones proye;
    private LineaBresenham lineaBresenham;
    private Random random;

    public cositas(proyecciones proye, LineaBresenham lineaBresenham) {
        this.proye = proye;
        this.lineaBresenham = lineaBresenham;
        this.random = new Random();
    }

    public void dibujarArbol(Graphics g, int x, int y, int z) {
        // Coordenadas del tronco y copa del árbol en 3D
        int[][] tronco = {
            {x, y, z},
            {x + 20, y, z},
            {x + 20, y + 20, z},
            {x, y + 20, z},
            {x, y, z + 40},
            {x + 20, y, z + 40},
            {x + 20, y + 20, z + 40},
            {x, y + 20, z + 40}
        };

        int[][] copa = {
            {x - 20, y - 20, z + 40},
            {x + 40, y - 20, z + 40},
            {x + 40, y + 40, z + 40},
            {x - 20, y + 40, z + 40},
            {x - 20, y - 20, z + 80},
            {x + 40, y - 20, z + 80},
            {x + 40, y + 40, z + 80},
            {x - 20, y + 40, z + 80}
        };

        int[][] troncoProyectado = new int[8][2];
        int[][] copaProyectado = new int[8][2];

        for (int i = 0; i < 8; i++) {
            troncoProyectado[i] = proye.proyeccionIsometrica(tronco[i][0], tronco[i][1], tronco[i][2]);
            copaProyectado[i] = proye.proyeccionIsometrica(copa[i][0], copa[i][1], copa[i][2]);
        }

        g.setColor(new Color(102, 51, 0)); // Color del tronco
        rellenarCubo(g, troncoProyectado, new Color(102, 51, 0));

        g.setColor(new Color(34, 139, 34)); // Color de la copa
        rellenarCubo(g, copaProyectado, new Color(34, 139, 34));
    }

    public List<int[]> generarPosicionesAleatorias(int yInicio, int altura) {
        List<int[]> posicionesArboles = new ArrayList<>();
        int numArboles = random.nextInt(7) + 2; // Número aleatorio de árboles entre 2 y 5
        int xArbol = random.nextInt(450) + 950;
        for (int i = 0; i < numArboles; i++) {
            int x = random.nextInt(xArbol); // Posición aleatoria en el eje x dentro de la plataforma
            int y = random.nextInt(altura) + yInicio; // Posición aleatoria en el eje y dentro de la plataforma
            posicionesArboles.add(new int[]{x, y});
        }
        return posicionesArboles;
    }

    public void dibujarCarritoDerecha(Graphics g, int x, int y, int z) {
        // Coordenadas del cuerpo del coche en 3D
        int[][] cuerpo = {
            {x, y, z},
            {x + 60, y, z},
            {x + 60, y + 20, z},
            {x, y + 20, z},
            {x, y, z + 20},
            {x + 60, y, z + 20},
            {x + 60, y + 20, z + 20},
            {x, y + 20, z + 20}
        };

        // Coordenadas de la cabina del coche en 3D
        int[][] cabina = {
            {x + 20, y, z + 20},
            {x + 40, y, z + 20},
            {x + 40, y + 20, z + 20},
            {x + 20, y + 20, z + 20},
            {x + 20, y, z + 40},
            {x + 40, y, z + 40},
            {x + 40, y + 20, z + 40},
            {x + 20, y + 20, z + 40}
        };

        // Coordenadas de las ruedas del coche en 3D
        int[][] rueda1 = {
            {x + 5, y + 20, z},
            {x + 15, y + 20, z},
            {x + 15, y + 30, z},
            {x + 5, y + 30, z},
            {x + 5, y + 20, z + 10},
            {x + 15, y + 20, z + 10},
            {x + 15, y + 30, z + 10},
            {x + 5, y + 30, z + 10}
        };

        int[][] rueda2 = {
            {x + 45, y + 20, z},
            {x + 55, y + 20, z},
            {x + 55, y + 30, z},
            {x + 45, y + 30, z},
            {x + 45, y + 20, z + 10},
            {x + 55, y + 20, z + 10},
            {x + 55, y + 30, z + 10},
            {x + 45, y + 30, z + 10}
        };

        // Coordenadas de las ventanas del coche en 3D
        int[][] ventana1 = {
            {x + 20, y, z + 20},
            {x + 40, y, z + 20},
            {x + 40, y + 10, z + 20},
            {x + 20, y + 10, z + 20},
            {x + 20, y, z + 30},
            {x + 40, y, z + 30},
            {x + 40, y + 10, z + 30},
            {x + 20, y + 10, z + 30}
        };

        int[][] cuerpoProyectado = new int[8][2];
        int[][] cabinaProyectado = new int[8][2];
        int[][] rueda1Proyectado = new int[8][2];
        int[][] rueda2Proyectado = new int[8][2];

        for (int i = 0; i < 8; i++) {
            cuerpoProyectado[i] = proye.proyeccionIsometrica(cuerpo[i][0], cuerpo[i][1], cuerpo[i][2]);
            cabinaProyectado[i] = proye.proyeccionIsometrica(cabina[i][0], cabina[i][1], cabina[i][2]);
            rueda1Proyectado[i] = proye.proyeccionIsometrica(rueda1[i][0], rueda1[i][1], rueda1[i][2]);
            rueda2Proyectado[i] = proye.proyeccionIsometrica(rueda2[i][0], rueda2[i][1], rueda2[i][2]);
        }

        g.setColor(new Color(0, 128, 0)); // Color del cuerpo
        rellenarCubo(g, cuerpoProyectado, new Color(0, 128, 0));

        g.setColor(Color.WHITE); // Color de la cabina
        rellenarCubo(g, cabinaProyectado, Color.WHITE);

        g.setColor(Color.BLACK); // Color de las ruedas
        rellenarCubo(g, rueda1Proyectado, Color.BLACK);
        rellenarCubo(g, rueda2Proyectado, Color.BLACK);
    }

    public void dibujarCarritoIzquierda(Graphics g, int x, int y, int z) {
        // Coordenadas del cuerpo del coche en 3D
        int[][] cuerpo = {
            {x, y, z},
            {x + 60, y, z},
            {x + 60, y + 20, z},
            {x, y + 20, z},
            {x, y, z + 20},
            {x + 60, y, z + 20},
            {x + 60, y + 20, z + 20},
            {x, y + 20, z + 20}
        };

        // Coordenadas de la cabina del coche en 3D
        int[][] cabina = {
            {x + 20, y, z + 20},
            {x + 40, y, z + 20},
            {x + 40, y + 20, z + 20},
            {x + 20, y + 20, z + 20},
            {x + 20, y, z + 40},
            {x + 40, y, z + 40},
            {x + 40, y + 20, z + 40},
            {x + 20, y + 20, z + 40}
        };

        // Coordenadas de las ruedas del coche en 3D
        int[][] rueda1 = {
            {x + 5, y + 20, z},
            {x + 15, y + 20, z},
            {x + 15, y + 30, z},
            {x + 5, y + 30, z},
            {x + 5, y + 20, z + 10},
            {x + 15, y + 20, z + 10},
            {x + 15, y + 30, z + 10},
            {x + 5, y + 30, z + 10}
        };

        int[][] rueda2 = {
            {x + 45, y + 20, z},
            {x + 55, y + 20, z},
            {x + 55, y + 30, z},
            {x + 45, y + 30, z},
            {x + 45, y + 20, z + 10},
            {x + 55, y + 20, z + 10},
            {x + 55, y + 30, z + 10},
            {x + 45, y + 30, z + 10}
        };

        // Coordenadas de las ventanas del coche en 3D
        int[][] ventana1 = {
            {x + 20, y, z + 20},
            {x + 40, y, z + 20},
            {x + 40, y + 10, z + 20},
            {x + 20, y + 10, z + 20},
            {x + 20, y, z + 30},
            {x + 40, y, z + 30},
            {x + 40, y + 10, z + 30},
            {x + 20, y + 10, z + 30}
        };

        int[][] cuerpoProyectado = new int[8][2];
        int[][] cabinaProyectado = new int[8][2];
        int[][] rueda1Proyectado = new int[8][2];
        int[][] rueda2Proyectado = new int[8][2];
        int[][] ventana1Proyectado = new int[8][2];

        for (int i = 0; i < 8; i++) {
            cuerpoProyectado[i] = proye.proyeccionIsometrica(cuerpo[i][0], cuerpo[i][1], cuerpo[i][2]);
            cabinaProyectado[i] = proye.proyeccionIsometrica(cabina[i][0], cabina[i][1], cabina[i][2]);
            rueda1Proyectado[i] = proye.proyeccionIsometrica(rueda1[i][0], rueda1[i][1], rueda1[i][2]);
            rueda2Proyectado[i] = proye.proyeccionIsometrica(rueda2[i][0], rueda2[i][1], rueda2[i][2]);
            ventana1Proyectado[i] = proye.proyeccionIsometrica(ventana1[i][0], ventana1[i][1], ventana1[i][2]);
        }

        g.setColor(new Color(0, 128, 0)); // Color del cuerpo
        rellenarCubo(g, cuerpoProyectado, new Color(0, 128, 0));

        g.setColor(Color.WHITE); // Color de la cabina
        rellenarCubo(g, cabinaProyectado, Color.WHITE);

        g.setColor(Color.BLACK); // Color de las ruedas
        rellenarCubo(g, rueda1Proyectado, Color.BLACK);
        rellenarCubo(g, rueda2Proyectado, Color.BLACK);

        g.setColor(Color.CYAN); // Color de las ventanas
        rellenarCubo(g, ventana1Proyectado, Color.CYAN);
    }

    public void dibujarTronco(Graphics g, int x, int y, int z) {
        // Coordenadas del tronco en 3D
        int[][] tronco = {
            {x, y, z},
            {x + 150, y, z},
            {x + 150, y + 30, z},
            {x, y + 30, z},
            {x, y, z + 20},
            {x + 150, y, z + 20},
            {x + 150, y + 30, z + 20},
            {x, y + 30, z + 20}
        };

        int[][] troncoProyectado = new int[8][2];

        for (int i = 0; i < 8; i++) {
            troncoProyectado[i] = proye.proyeccionIsometrica(tronco[i][0], tronco[i][1], tronco[i][2]);
        }

        g.setColor(new Color(139, 69, 19)); // Color del tronco
        rellenarCubo(g, troncoProyectado, new Color(139, 69, 19));
    }

    public void dibujarTrailerIzquierda(Graphics g, int x, int y, int z) {
        Color rojo = new Color(255, 0, 0);
        Color blanco = new Color(255, 255, 255);
        Color negro = new Color(0, 0, 0);

        int largoCabina = 40;
        int anchoCabina = 30;
        int altoCabina = 30;

        int largoRemolque = 100;
        int anchoRemolque = 30;
        int altoRemolque = 50;

        int tamañoRueda = 10;

        // Cabina
        int[][] cabina = {
            {x, y, z},
            {x + largoCabina, y, z},
            {x + largoCabina, y + anchoCabina, z},
            {x, y + anchoCabina, z},
            {x, y, z + altoCabina},
            {x + largoCabina, y, z + altoCabina},
            {x + largoCabina, y + anchoCabina, z + altoCabina},
            {x, y + anchoCabina, z + altoCabina}
        };

        // Remolque
        int remolqueX = x + largoCabina; // Ajuste para conectar directamente con la cabina
        int remolqueY = y;
        int remolqueZ = z;
        int[][] remolque = {
            {remolqueX, remolqueY, remolqueZ},
            {remolqueX + largoRemolque, remolqueY, remolqueZ},
            {remolqueX + largoRemolque, remolqueY + anchoRemolque, remolqueZ},
            {remolqueX, remolqueY + anchoRemolque, remolqueZ},
            {remolqueX, remolqueY, remolqueZ + altoRemolque},
            {remolqueX + largoRemolque, remolqueY, remolqueZ + altoRemolque},
            {remolqueX + largoRemolque, remolqueY + anchoRemolque, remolqueZ + altoRemolque},
            {remolqueX, remolqueY + anchoRemolque, remolqueZ + altoRemolque}
        };

        // Ruedas delanteras
        int ruedaDelanteraX = x + 10;
        int ruedaDelanteraY = y + anchoCabina;
        int ruedaDelanteraZ = z;
        int[][] ruedaDelantera = {
            {ruedaDelanteraX, ruedaDelanteraY, ruedaDelanteraZ},
            {ruedaDelanteraX + tamañoRueda, ruedaDelanteraY, ruedaDelanteraZ},
            {ruedaDelanteraX + tamañoRueda, ruedaDelanteraY + tamañoRueda, ruedaDelanteraZ},
            {ruedaDelanteraX, ruedaDelanteraY + tamañoRueda, ruedaDelanteraZ},
            {ruedaDelanteraX, ruedaDelanteraY, ruedaDelanteraZ + tamañoRueda},
            {ruedaDelanteraX + tamañoRueda, ruedaDelanteraY, ruedaDelanteraZ + tamañoRueda},
            {ruedaDelanteraX + tamañoRueda, ruedaDelanteraY + tamañoRueda, ruedaDelanteraZ + tamañoRueda},
            {ruedaDelanteraX, ruedaDelanteraY + tamañoRueda, ruedaDelanteraZ + tamañoRueda}
        };

        // Ruedas traseras
        int ruedaTraseraX = remolqueX + largoRemolque - 20;
        int ruedaTraseraY = remolqueY + anchoRemolque;
        int ruedaTraseraZ = z;
        int[][] ruedaTrasera = {
            {ruedaTraseraX, ruedaTraseraY, ruedaTraseraZ},
            {ruedaTraseraX + tamañoRueda, ruedaTraseraY, ruedaTraseraZ},
            {ruedaTraseraX + tamañoRueda, ruedaTraseraY + tamañoRueda, ruedaTraseraZ},
            {ruedaTraseraX, ruedaTraseraY + tamañoRueda, ruedaTraseraZ},
            {ruedaTraseraX, ruedaTraseraY, ruedaTraseraZ + tamañoRueda},
            {ruedaTraseraX + tamañoRueda, ruedaTraseraY, ruedaTraseraZ + tamañoRueda},
            {ruedaTraseraX + tamañoRueda, ruedaTraseraY + tamañoRueda, ruedaTraseraZ + tamañoRueda},
            {ruedaTraseraX, ruedaTraseraY + tamañoRueda, ruedaTraseraZ + tamañoRueda}
        };

        int[][] cabinaProyectado = new int[8][2];
        int[][] remolqueProyectado = new int[8][2];
        int[][] ruedaDelanteraProyectado = new int[8][2];
        int[][] ruedaTraseraProyectado = new int[8][2];

        for (int i = 0; i < 8; i++) {
            cabinaProyectado[i] = proye.proyeccionIsometrica(cabina[i][0], cabina[i][1], cabina[i][2]);
            remolqueProyectado[i] = proye.proyeccionIsometrica(remolque[i][0], remolque[i][1], remolque[i][2]);
            ruedaDelanteraProyectado[i] = proye.proyeccionIsometrica(ruedaDelantera[i][0], ruedaDelantera[i][1], ruedaDelantera[i][2]);
            ruedaTraseraProyectado[i] = proye.proyeccionIsometrica(ruedaTrasera[i][0], ruedaTrasera[i][1], ruedaTrasera[i][2]);
        }

        g.setColor(rojo); // Color de la cabina
        rellenarCubo(g, cabinaProyectado, rojo);

        g.setColor(blanco); // Color del remolque
        rellenarCubo(g, remolqueProyectado, blanco);

        g.setColor(negro); // Color de las ruedas
        rellenarCubo(g, ruedaDelanteraProyectado, negro);
        rellenarCubo(g, ruedaTraseraProyectado, negro);
    }

    

    private void rellenarCubo(Graphics g, int[][] puntos, Color color) {
        g.setColor(color);
        // Cara superior
        int[] xPoints = {puntos[0][0], puntos[1][0], puntos[2][0], puntos[3][0]};
        int[] yPoints = {puntos[0][1], puntos[1][1], puntos[2][1], puntos[3][1]};
        g.fillPolygon(xPoints, yPoints, 4);

        // Cara inferior
        xPoints = new int[]{puntos[4][0], puntos[5][0], puntos[6][0], puntos[7][0]};
        yPoints = new int[]{puntos[4][1], puntos[5][1], puntos[6][1], puntos[7][1]};
        g.fillPolygon(xPoints, yPoints, 4);

        // Caras laterales
        for (int i = 0; i < 4; i++) {
            xPoints = new int[]{puntos[i][0], puntos[(i + 1) % 4][0], puntos[(i + 1) % 4 + 4][0], puntos[i + 4][0]};
            yPoints = new int[]{puntos[i][1], puntos[(i + 1) % 4][1], puntos[(i + 1) % 4 + 4][1], puntos[i + 4][1]};
            g.fillPolygon(xPoints, yPoints, 4);
        }
    }

    public void dibujarGallina(Graphics g, int x, int y, int z) {
        Color blanco = new Color(255, 255, 255);
        Color naranja = new Color(255, 153, 51);
        Color rojo = new Color(255, 51, 51);
        Color negro = new Color(0, 0, 0);
        Color rosado = new Color(255, 105, 180);

        int tamañoCuerpo = 20;
        int tamañoCabeza = 15;
        int tamañoPico = 7;
        int tamañoCresta = 5;
        int tamañoPata = 5;
        int tamañoAla = 7;

        // Cuerpo
        int[][] cuerpo = {
            {x, y, z},
            {x + tamañoCuerpo, y, z},
            {x + tamañoCuerpo, y + tamañoCuerpo, z},
            {x, y + tamañoCuerpo, z},
            {x, y, z + tamañoCuerpo},
            {x + tamañoCuerpo, y, z + tamañoCuerpo},
            {x + tamañoCuerpo, y + tamañoCuerpo, z + tamañoCuerpo},
            {x, y + tamañoCuerpo, z + tamañoCuerpo}
        };

        // Cabeza
        int cabezaX = x - 10;
        int cabezaY = y - 10;
        int[][] cabeza = {
            {cabezaX, cabezaY, z + tamañoCuerpo - 5},
            {cabezaX + tamañoCabeza, cabezaY, z + tamañoCuerpo - 5},
            {cabezaX + tamañoCabeza, cabezaY + tamañoCabeza, z + tamañoCuerpo - 5},
            {cabezaX, cabezaY + tamañoCabeza, z + tamañoCuerpo - 5},
            {cabezaX, cabezaY, z + tamañoCuerpo + tamañoCabeza - 5},
            {cabezaX + tamañoCabeza, cabezaY, z + tamañoCuerpo + tamañoCabeza - 5},
            {cabezaX + tamañoCabeza, cabezaY + tamañoCabeza, z + tamañoCuerpo + tamañoCabeza - 5},
            {cabezaX, cabezaY + tamañoCabeza, z + tamañoCuerpo + tamañoCabeza - 5}
        };

        // Pico
        int picoX = cabezaX;
        int picoY = cabezaY + 10;
        int[][] pico = {
            {picoX, picoY, z + tamañoCuerpo + tamañoCabeza - 15},
            {picoX + tamañoPico, picoY, z + tamañoCuerpo + tamañoCabeza - 15},
            {picoX + tamañoPico, picoY + tamañoPico, z + tamañoCuerpo + tamañoCabeza - 15},
            {picoX, picoY + tamañoPico, z + tamañoCuerpo + tamañoCabeza - 15},
            {picoX, picoY, z + tamañoCuerpo + tamañoCabeza + tamañoPico - 15},
            {picoX + tamañoPico, picoY, z + tamañoCuerpo + tamañoCabeza + tamañoPico - 15},
            {picoX + tamañoPico, picoY + tamañoPico, z + tamañoCuerpo + tamañoCabeza + tamañoPico - 15},
            {picoX, picoY + tamañoPico, z + tamañoCuerpo + tamañoCabeza + tamañoPico - 15}
        };

        // Cresta
        int crestaX = cabezaX + 10;
        int crestaY = cabezaY + 10;
        int[][] cresta = {
            {crestaX, crestaY, z + tamañoCuerpo + tamañoCabeza},
            {crestaX + tamañoCresta, crestaY, z + tamañoCuerpo + tamañoCabeza},
            {crestaX + tamañoCresta, crestaY + tamañoCresta, z + tamañoCuerpo + tamañoCabeza},
            {crestaX, crestaY + tamañoCresta, z + tamañoCuerpo + tamañoCabeza},
            {crestaX, crestaY, z + tamañoCuerpo + tamañoCabeza + tamañoCresta},
            {crestaX + tamañoCresta, crestaY, z + tamañoCuerpo + tamañoCabeza + tamañoCresta},
            {crestaX + tamañoCresta, crestaY + tamañoCresta, z + tamañoCuerpo + tamañoCabeza + tamañoCresta},
            {crestaX, crestaY + tamañoCresta, z + tamañoCuerpo + tamañoCabeza + tamañoCresta}
        };

        // Pata izquierda
        int pataY = y + tamañoCuerpo;
        int[][] pataIzquierda = {
            {x + tamañoCuerpo / 4 - tamañoPata / 2, pataY, z},
            {x + tamañoCuerpo / 4 + tamañoPata / 2, pataY, z},
            {x + tamañoCuerpo / 4 + tamañoPata / 2, pataY + tamañoPata, z},
            {x + tamañoCuerpo / 4 - tamañoPata / 2, pataY + tamañoPata, z},
            {x + tamañoCuerpo / 4 - tamañoPata / 2, pataY, z + tamañoPata},
            {x + tamañoCuerpo / 4 + tamañoPata / 2, pataY, z + tamañoPata},
            {x + tamañoCuerpo / 4 + tamañoPata / 2, pataY + tamañoPata, z + tamañoPata},
            {x + tamañoCuerpo / 4 - tamañoPata / 2, pataY + tamañoPata, z + tamañoPata}
        };

        // Pata derecha
        int[][] pataDerecha = {
            {x + 3 * tamañoCuerpo / 4 - tamañoPata / 2, pataY, z},
            {x + 3 * tamañoCuerpo / 4 + tamañoPata / 2, pataY, z},
            {x + 3 * tamañoCuerpo / 4 + tamañoPata / 2, pataY + tamañoPata, z},
            {x + 3 * tamañoCuerpo / 4 - tamañoPata / 2, pataY + tamañoPata, z},
            {x + 3 * tamañoCuerpo / 4 - tamañoPata / 2, pataY, z + tamañoPata},
            {x + 3 * tamañoCuerpo / 4 + tamañoPata / 2, pataY, z + tamañoPata},
            {x + 3 * tamañoCuerpo / 4 + tamañoPata / 2, pataY + tamañoPata, z + tamañoPata},
            {x + 3 * tamañoCuerpo / 4 - tamañoPata / 2, pataY + tamañoPata, z + tamañoPata}
        };

        // Ojo
        int ojoX = cabezaX + 13;
        int ojoY = cabezaY + 10;
        int[][] ojo = {
            {ojoX, ojoY, z + tamañoCuerpo + tamañoCabeza - 10},
            {ojoX + tamañoPico / 2, ojoY, z + tamañoCuerpo + tamañoCabeza - 10},
            {ojoX + tamañoPico / 2, ojoY + tamañoPico / 2, z + tamañoCuerpo + tamañoCabeza - 10},
            {ojoX, ojoY + tamañoPico / 2, z + tamañoCuerpo + tamañoCabeza - 10},
            {ojoX, ojoY, z + tamañoCuerpo + tamañoCabeza + tamañoPico / 2 - 10},
            {ojoX + tamañoPico / 2, ojoY, z + tamañoCuerpo + tamañoCabeza + tamañoPico / 2 - 10},
            {ojoX + tamañoPico / 2, ojoY + tamañoPico / 2, z + tamañoCuerpo + tamañoCabeza + tamañoPico / 2 - 10},
            {ojoX, ojoY + tamañoPico / 2, z + tamañoCuerpo + tamañoCabeza + tamañoPico / 2 - 10}
        };

        // Ala
        int alaX = x + tamañoCuerpo / 2 - tamañoAla / 2;
        int alaY = y + tamañoCuerpo / 2;
        int[][] ala = {
            {alaX, alaY, z + tamañoCuerpo / 3},
            {alaX + tamañoAla, alaY, z + tamañoCuerpo / 3},
            {alaX + tamañoAla, alaY + tamañoAla, z + tamañoCuerpo / 3},
            {alaX, alaY + tamañoAla, z + tamañoCuerpo / 3},
            {alaX, alaY, z + tamañoCuerpo / 3 + tamañoAla},
            {alaX + tamañoAla, alaY, z + tamañoCuerpo / 3 + tamañoAla},
            {alaX + tamañoAla, alaY + tamañoAla, z + tamañoCuerpo / 3 + tamañoAla},
            {alaX, alaY + tamañoAla, z + tamañoCuerpo / 3 + tamañoAla}
        };

        int[][][] partes = {cuerpo, cabeza, pico, cresta, pataIzquierda, pataDerecha, ojo, ala};
        Color[] colores = {blanco, blanco, naranja, rosado, naranja, naranja, negro, blanco};

        for (int i = 0; i < partes.length; i++) {
            int[][] parte = partes[i];
            Color color = colores[i];
            int[][] parteProyectada = new int[8][2];

            for (int j = 0; j < 8; j++) {
                parteProyectada[j] = proye.proyeccionIsometrica(parte[j][0], parte[j][1], parte[j][2]);
            }

            rellenarCubo(g, parteProyectada, color);
        }
    }

    public List<int[]> generarPosicionesAleatoriasTroncos(int yInicio, int altura, int numTroncos) {
        List<int[]> posicionesTroncos = new ArrayList<>();

        // Asegurar que haya un tronco al inicio de cada plataforma
        posicionesTroncos.add(new int[]{50, yInicio + 60});

        for (int i = 1; i < numTroncos; i++) {
            int x = random.nextInt(350) + 10; // Posición aleatoria en el eje x dentro de la plataforma
            int yMax = altura - 120;
            if (yMax > 0) {
                int y = random.nextInt(yMax) + yInicio + 60; // Ajuste en el eje y para la parte azul clara
                posicionesTroncos.add(new int[]{x, y});
            } else {
                // Si el valor de yMax no es positivo, coloca el tronco en la parte superior de la plataforma
                posicionesTroncos.add(new int[]{x, yInicio + 60});
            }
        }

        return posicionesTroncos;
    }
}
