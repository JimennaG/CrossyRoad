package Main;

import crossyroad.PolloPrincipal;
import crossyroad.main3D;

public class Launcher {
    public static void main(String[] args) {
        // MUESTRA DE PERSONAJE
        PolloPrincipal polloFrame = new PolloPrincipal();
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // FINALIZACION DE PERSONAJE
        polloFrame.dispose();
        // INICIO PROGRAMA
        new main3D();
    }
}
