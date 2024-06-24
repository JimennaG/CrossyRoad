package crossyroad;

import Objects.*;
import Utl.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class main3D extends JFrame implements Runnable {

    // COLORES
    Color azulBajito = new Color(25, 181, 244);
    Color azul = new Color(0, 66, 255);
    Color rojo = new Color(255, 0, 0);
    Color verde = new Color(98, 220, 24);
    Color verdeFuerte = new Color(57, 220, 24);
    Color gris = new Color(155, 155, 155);
    Color negro = new Color(0, 0, 0);
    Color cafe = new Color(200, 111, 10);
    // CLASES
    private proyecciones proye;
    private LineaBresenham lineaBresenham;
    private final dibujar pixel;

    int withPantalla = 550, heightPantalla = 650;
    
    //Para dibujar gallina
    private final Thread hilo;
    private final AnimadorGallina animador;

    main3D() {
        pixel = new dibujar();
        lineaBresenham = new LineaBresenham();
        proye = new proyecciones();
        setTitle("PROYECTO 3D");
        setLayout(new BorderLayout());
        add(pixel, BorderLayout.CENTER);
        setSize(withPantalla, heightPantalla);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        animador = new AnimadorGallina();
        hilo = new Thread(this);
        hilo.start();
        
//        Thread thread = new Thread(this);
//        thread.start();
    }

    public void seccion_1() {
        int[][] puntosIniciales = {
            {2, 458, 50}, // A
            {531, 458, 50}, // B
            {2, 608, 50}, // C
            {531, 608, 50}, // D

            {30, 528, 150}, // E
            {531, 528, 150}, // F
            {30, 608, 150}, // G
            {531, 608, 150}, // H
        };

        int[][] puntosProyectadosOrtogonal = new int[8][2];

        // Aplicar proyección oblicua
        for (int i = 0; i < 8; i++) {
            puntosProyectadosOrtogonal[i] = proye.proyeccionOrtogonal(puntosIniciales[i][0], puntosIniciales[i][1], puntosIniciales[i][2]);
        } 
        List<Point> CaraGeneral = new ArrayList<>();
        List<Point> Cara1 = new ArrayList<>();
        List<Point> Cara2 = new ArrayList<>();
        List<Point> Cara3 = new ArrayList<>();
        // ------------------------------------------------------------------------------------------------------------------------------------
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1])); // A - B
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1])); // A - C
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1])); // A - E
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // C - H
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1])); // E - G
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[5][0], puntosProyectadosOrtogonal[5][1])); // E - F
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // B - H
        // ------------------------------------------------------------------------------------------------------------------------------------
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1]));
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (int i = puntosProyectadosOrtogonal[0][1]; i < puntosProyectadosOrtogonal[3][1]; i++) {
            Cara2.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], i, puntosProyectadosOrtogonal[1][0], i));
        }
        for (int i = puntosProyectadosOrtogonal[4][1]; i < puntosProyectadosOrtogonal[6][1]; i++) {
            Cara3.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], i, puntosProyectadosOrtogonal[5][0], i));
        }
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (Point punto : Cara2) {
            pixel.putPixel(punto.x, punto.y, verdeFuerte);
        }
        pixel.fillPolygon(Cara1, cafe);
        for (Point punto : Cara3) {
            pixel.putPixel(punto.x, punto.y, cafe);
        }
        for (Point punto : CaraGeneral) {
            pixel.putPixel(punto.x, punto.y, negro);
        }
    }

    public void seccion_2() {
        int[][] puntosIniciales = {
            {2, 308, 50}, // A
            {531, 308, 50}, // B
            {2, 456, 50}, // C
            {531, 456, 50}, // D

            {30, 378, 150}, // E
            {531, 378, 150}, // F
            {30, 456, 150}, // G
            {531, 456, 150}, // H
        };

        int[][] puntosProyectadosOrtogonal = new int[8][2];

        // Aplicar proyección oblicua
        for (int i = 0; i < 8; i++) {
            puntosProyectadosOrtogonal[i] = proye.proyeccionOrtogonal(puntosIniciales[i][0], puntosIniciales[i][1], puntosIniciales[i][2]);
        }
        List<Point> CaraGeneral = new ArrayList<>();
        List<Point> Cara1 = new ArrayList<>();
        List<Point> Cara2 = new ArrayList<>();
        List<Point> Cara3 = new ArrayList<>();
        // ------------------------------------------------------------------------------------------------------------------------------------
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1])); // A - B
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1])); // A - C
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1])); // A - E
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // C - H
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1])); // E - G
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[5][0], puntosProyectadosOrtogonal[5][1])); // E - F
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // B - H
        // ------------------------------------------------------------------------------------------------------------------------------------
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1]));
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (int i = puntosProyectadosOrtogonal[0][1]; i < puntosProyectadosOrtogonal[3][1]; i++) {
            Cara2.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], i, puntosProyectadosOrtogonal[1][0], i));
        }
        for (int i = puntosProyectadosOrtogonal[4][1]; i < puntosProyectadosOrtogonal[6][1]; i++) {
            Cara3.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], i, puntosProyectadosOrtogonal[5][0], i));
        }
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (Point punto : Cara2) {
            pixel.putPixel(punto.x, punto.y, azul);
        }
        pixel.fillPolygon(Cara1, azulBajito);
        for (Point punto : Cara3) {
            pixel.putPixel(punto.x, punto.y, azulBajito);
        }
        for (Point punto : CaraGeneral) {
            pixel.putPixel(punto.x, punto.y, negro);
        }
    }

    public void seccion_3() {
        int[][] puntosIniciales = {
            {2, 156, 50}, // A
            {531, 156, 50}, // B
            {2, 306, 50}, // C
            {531, 306, 50}, // D

            {30, 226, 150}, // E
            {531, 226, 150}, // F
            {30, 306, 150}, // G
            {531, 306, 150}, // H
        };

        int[][] puntosProyectadosOrtogonal = new int[8][2];

        // Aplicar proyección oblicua
        for (int i = 0; i < 8; i++) {
            puntosProyectadosOrtogonal[i] = proye.proyeccionOrtogonal(puntosIniciales[i][0], puntosIniciales[i][1], puntosIniciales[i][2]);
        }
        List<Point> CaraGeneral = new ArrayList<>();
        List<Point> Cara1 = new ArrayList<>();
        List<Point> Cara2 = new ArrayList<>();
        List<Point> Cara3 = new ArrayList<>();
        // ------------------------------------------------------------------------------------------------------------------------------------
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1])); // A - B
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1])); // A - C
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1])); // A - E
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // C - H
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1])); // E - G
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[5][0], puntosProyectadosOrtogonal[5][1])); // E - F
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // B - H
        // ------------------------------------------------------------------------------------------------------------------------------------
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1]));
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (int i = puntosProyectadosOrtogonal[0][1]; i < puntosProyectadosOrtogonal[3][1]; i++) {
            Cara2.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], i, puntosProyectadosOrtogonal[1][0], i));
        }
        for (int i = puntosProyectadosOrtogonal[4][1]; i < puntosProyectadosOrtogonal[6][1]; i++) {
            Cara3.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], i, puntosProyectadosOrtogonal[5][0], i));
        }
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (Point punto : Cara2) {
            pixel.putPixel(punto.x, punto.y, verdeFuerte);
        }
        pixel.fillPolygon(Cara1, cafe);
        for (Point punto : Cara3) {
            pixel.putPixel(punto.x, punto.y, cafe);
        }
        for (Point punto : CaraGeneral) {
            pixel.putPixel(punto.x, punto.y, negro);
        }
    }

    public void seccion_4() {
        int[][] puntosIniciales = {
            {2, 6, 50}, // A
            {531, 6, 50}, // B
            {2, 154, 50}, // C
            {531, 154, 50}, // D

            {30, 76, 150}, // E
            {531, 76, 150}, // F
            {30, 154, 150}, // G
            {531, 154, 150}, // H
        };

        int[][] puntosProyectadosOrtogonal = new int[8][2];

        // Aplicar proyección oblicua
        for (int i = 0; i < 8; i++) {
            puntosProyectadosOrtogonal[i] = proye.proyeccionOrtogonal(puntosIniciales[i][0], puntosIniciales[i][1], puntosIniciales[i][2]);
        }
        List<Point> CaraGeneral = new ArrayList<>();
        List<Point> Cara1 = new ArrayList<>();
        List<Point> Cara2 = new ArrayList<>();
        List<Point> Cara3 = new ArrayList<>();
        // ------------------------------------------------------------------------------------------------------------------------------------
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1])); // A - B
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1])); // A - C
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1])); // A - E
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // C - H
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1])); // E - G
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[5][0], puntosProyectadosOrtogonal[5][1])); // E - F
        CaraGeneral.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[1][0], puntosProyectadosOrtogonal[1][1], puntosProyectadosOrtogonal[7][0], puntosProyectadosOrtogonal[7][1])); // B - H
        // ------------------------------------------------------------------------------------------------------------------------------------
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1], puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], puntosProyectadosOrtogonal[4][1], puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[6][0], puntosProyectadosOrtogonal[6][1], puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1]));
        Cara1.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[2][0], puntosProyectadosOrtogonal[2][1], puntosProyectadosOrtogonal[0][0], puntosProyectadosOrtogonal[0][1]));
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (int i = puntosProyectadosOrtogonal[0][1]; i < puntosProyectadosOrtogonal[3][1]; i++) {
            Cara2.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[0][0], i, puntosProyectadosOrtogonal[1][0], i));
        }
        for (int i = puntosProyectadosOrtogonal[4][1]; i < puntosProyectadosOrtogonal[6][1]; i++) {
            Cara3.addAll(lineaBresenham.calcularLineaBresenham(puntosProyectadosOrtogonal[4][0], i, puntosProyectadosOrtogonal[5][0], i));
        }
        // ------------------------------------------------------------------------------------------------------------------------------------
        for (Point punto : Cara2) {
            pixel.putPixel(punto.x, punto.y, azul);
        }
        pixel.fillPolygon(Cara1, azulBajito);
        for (Point punto : Cara3) {
            pixel.putPixel(punto.x, punto.y, azulBajito);
        }
        for (Point punto : CaraGeneral) {
            pixel.putPixel(punto.x, punto.y, negro);
        }
    }
    
    public void gallina(){
        
        
    }

    public static void main(String[] args) {
        new main3D();
    }

    @Override
    public void run() {
        animador.setInicioAplicacion(System.currentTimeMillis());
        while (true) {
            try {
                repaint();
                hilo.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("error");
            }
        }
    }

    public void update(Graphics g) {
        Image buffer = createImage(getWidth(), getHeight());

        DibujarGallina d= new DibujarGallina(this);
        animador.setDibujar(d);
        
        

        // escena 1 (gallina)
        if(animador.tiempoActual<=15000) {
        //if(animador.tiempoActual<=1000) {
            animador.nuevaAnimacion();
            d.color=Color.decode("#76C1FF");
            d.rectanguloRelleno(0, 0, 550, 650);
            d.drawToBuffer(buffer, this);
//            animador.nuevaAnimacion();
            //animador.traslacion(300, 300, 0, 10000);
            //animador.traslacion(-300, -200, 10000, 15000);
            //d.elipse(0, 0, 40, 40);
            //d.floodFill(0, 0);

            animador.nuevaAnimacion();
            d.traslacion(getWidth()/2,getHeight()/2);
            d.setVectorProyeccion(new PointXYZInt(40,40,-10000));
            animador.rotacionY3D(0,10,0000,15000);
            //animador.rotacionY3D(0,10,0000,1000);

            d.resetImage(this);
            gallina(d);
            d.drawToBuffer(buffer, this);
        }
        // escena 2 (animacion 3D)
        if(animador.tiempoActual>15000 && animador.tiempoActual<60000) {
        //if(animador.tiempoActual>1000 && animador.tiempoActual<60000) {
            animador.nuevaAnimacion();
            
            DibujarGallina dMiniGallina = new DibujarGallina(pixel);
            animador.setDibujar(dMiniGallina);
            dMiniGallina.traslacion(getWidth()/2,getHeight()/2);
            dMiniGallina.setVectorProyeccion(new PointXYZInt(40,40,-10000));
            dMiniGallina.setVectorRotacion(-0.4, 9, 0);
            
            seccion_1();
            seccion_2();
            seccion_3();
            seccion_4();
            miniGallina(dMiniGallina);
            dMiniGallina.drawToBufferPanel(buffer, pixel);
            pixel.repaint();
        }

        g.drawImage(buffer, 0, 0, this);
    }

    public void gallina(DibujarGallina d){
        d.color=Color.white;
        d.cubo(0,0,0,100,200,120);
        d.color = Color.RED;
        d.cubo(0,-120,0,50,30,75);
        d.cubo(0,-25,-75,25,25,20); //pico
        d.color = Color.decode("#FFA500");
        d.cubo(25, 170, 0, 25, 30, 50);//pie
        d.cubo(-25, 170, 0, 25, 30, 50);//pie
        d.cubo(25, 130, 0, 20, 50, 20);//pata
        d.cubo(-25, 130, 0, 20, 50, 20);//pata
        d.cubo(0,-50,-80,25,25,30);//pico
        d.color = Color.BLACK;//ojos
        d.cubo(50, -70, -20, 5, 15, 15);
        d.cubo(-50, -70, -20, 5, 15, 15);
    }
    
    public void miniGallina(DibujarGallina d){
        d.color=Color.white;
        d.cubo(0,200,0,50,50,60);
        d.color = Color.RED;
        d.cubo(0,165,0,25,25,37);
        d.color = Color.decode("#FFA500");
        d.cubo(13, 260, 0, 13, 15, 25);//pie
        d.cubo(-13, 260, 0, 13, 15, 25);//pie
        d.cubo(13, 240, 0, 10, 25, 10);//pata
        d.cubo(-13, 240, 0, 10, 25, 10);//pata
        d.color = Color.BLACK;//ojos
        d.cubo(25, 190, -10, 3, 8, 8);
        d.cubo(-25, 190, -10, 3, 8, 8);
        
    }
    
    public void paint(Graphics g) {
        update(g);
    }

}
