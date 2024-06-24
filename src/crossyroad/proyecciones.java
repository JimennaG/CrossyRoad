package crossyroad;

public class proyecciones {

    // Método para la proyección ortogonal
     public int[] proyeccionOrtogonal(int x, int y, int z) {
        return new int[]{x, y};
    }
    
       public int[] proyeccionIsometrica(int x, int y, int z) {
        // Ajuste para una inclinación menor
        int xIso = (int) ((x - y) * Math.cos(Math.PI / 12));
        int yIso = (int) ((x + y) * Math.sin(Math.PI / 12) - z);
        return new int[]{xIso, yIso};
    }

    // Método para la proyección oblicua
    public int[] proyeccionOblicua(int x, int y, int z) {
        double angulo = Math.toRadians(45); // Ángulo de 45 grados para la proyección oblicua
        double l = 0.5; // Factor de escala para la proyección oblicua
        int xProyectado = (int) (x + l * z * Math.cos(angulo));
        int yProyectado = (int) (y + l * z * Math.sin(angulo));
        System.out.println("X: "+xProyectado);
        int[] puntoProyectado = {xProyectado, yProyectado};
        return puntoProyectado;
    }
    
    public int[][] proyeccionPerspectiva(int [][] puntosIniciales, int Zc, int Xc, int Yc){
        int u = 0;
        int[][] puntosFinales = new int[8][2];

        for (int i = 0; i < 8; i++) {
            u = (int) Math.round(- Zc / (puntosIniciales[i][2] - Zc));
            System.out.println("Valor de U: "+u);
            int x = Xc + ((puntosIniciales[i][0] - Xc) * u);
            int y = Yc + ((puntosIniciales[i][1] - Yc) * u);
            
            puntosFinales[i][0] = (int) Math.round(x);
            System.out.println("Valor de X: "+ Xc +" + (("+puntosIniciales[i][0]+" - "+Xc+") * "+u+") = "+x);
            puntosFinales[i][1] = (int) Math.round(y);
            System.out.println("Valor de Y: "+ Yc +" + (("+puntosIniciales[i][1]+" - "+Yc+") * "+u+") = "+y);
            System.out.println("--------------------------------");
        }
        return puntosFinales;
    }
}
