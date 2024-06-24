package crossyroad;

import Objects.AnimadorGallina;
import Objects.DibujarGallina;
import Utl.PointXYZInt;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;

public class PolloPrincipal extends JFrame implements Runnable {

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

    public PolloPrincipal() {
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
    }

    public void gallina() {
    }

    @Override
    public void run() {
        animador.setInicioAplicacion(System.currentTimeMillis());
        while (true) {
            try {
                repaint();
                hilo.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("error");
            }
        }
    }

    public void update(Graphics g) {
        Image buffer = createImage(getWidth(), getHeight());

        DibujarGallina d = new DibujarGallina(this);
        animador.setDibujar(d);

        if (animador.tiempoActual <= 30000) {
            animador.nuevaAnimacion();
            d.color = Color.decode("#76C1FF");
            d.rectanguloRelleno(0, 0, 550, 650);
            d.drawToBuffer(buffer, this);
            animador.nuevaAnimacion();

            animador.nuevaAnimacion();
            d.traslacion(getWidth() / 2, getHeight() / 2);
            d.setVectorProyeccion(new PointXYZInt(40, 40, -10000));
            animador.rotacionY3D(0, 10, 0000, 30000);
            //animador.rotacionY3D(0,10,0000,1000);

            d.resetImage(this);
            gallina(d);
            d.drawToBuffer(buffer, this);
        }

        // escena 2 (animacion 3D)
        // if (animador.tiempoActual > 15000 && animador.tiempoActual < 60000) {
        //     animador.nuevaAnimacion();
        //     DibujarGallina dMiniGallina = new DibujarGallina(pixel);
        //     animador.setDibujar(dMiniGallina);
        //     dMiniGallina.traslacion(getWidth() / 2, getHeight() / 2);
        //     dMiniGallina.setVectorProyeccion(new PointXYZInt(40, 40, -10000));
        //     dMiniGallina.setVectorRotacion(-0.4, 9, 0);
        //     miniGallina(dMiniGallina);
        //     dMiniGallina.drawToBufferPanel(buffer, pixel);
        //     pixel.repaint();
        // }
        g.drawImage(buffer, 0, 0, this);
    }

    public void gallina(DibujarGallina d) {
        d.color = Color.white;
        d.cubo(0, 0, 0, 100, 200, 120);
        d.color = Color.RED;
        d.cubo(0, -120, 0, 50, 30, 75);
        d.cubo(0, -25, -75, 25, 25, 20); //pico
        d.color = Color.decode("#FFA500");
        d.cubo(25, 170, 0, 25, 30, 50);//pie
        d.cubo(-25, 170, 0, 25, 30, 50);//pie
        d.cubo(25, 130, 0, 20, 50, 20);//pata
        d.cubo(-25, 130, 0, 20, 50, 20);//pata
        d.cubo(0, -50, -80, 25, 25, 30);//pico
        d.color = Color.BLACK;//ojos
        d.cubo(50, -70, -20, 5, 15, 15);
        d.cubo(-50, -70, -20, 5, 15, 15);
    }

    public void paint(Graphics g) {
        update(g);
    }
}
