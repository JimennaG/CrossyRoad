package Objects;

public class AnimadorGallina {
    DibujarGallina dibujar;
    public long inicioAplicacion;
    public long tiempoActual;
    int dx = 0, dy = 0;
    double sx = 1, sy = 1;
    double theta = 0;
    private boolean isSuave = true;
    public double rotX3D=-0.2;

    public void traslacion(int dxDestino, int dyDestino, long inicioAnimacion, long finAnimacion) {
        if (tiempoActual < inicioAnimacion) {
            return;
        }

        if (tiempoActual <= finAnimacion) {
            long milisegundosAnimados = tiempoActual - inicioAnimacion;
            long duracionAnimacion = finAnimacion - inicioAnimacion;

            double tiempoAnimado = (double) milisegundosAnimados / duracionAnimacion;
            double porcentajeAnimacion = ease(tiempoAnimado);
            dx += dxDestino * porcentajeAnimacion;
            dy += dyDestino * porcentajeAnimacion;
        } else {
            dx += dxDestino;
            dy += dyDestino;
        }
        dibujar.traslacionAnimacion(dx, dy);
    }

    private double ease(double t) {
        if (isSuave)
            return 3 * t * t - 2 * t * t * t;
        else
            return t;
    }

    public void escalacion(double sxDestino, double syDestino, long inicioAnimacion, long finAnimacion) {
        if (tiempoActual < inicioAnimacion) {
            return;
        }

        if (tiempoActual <= finAnimacion) {
            long milisegundosAnimados = tiempoActual - inicioAnimacion;
            long duracionAnimacion = finAnimacion - inicioAnimacion;

            double tiempoAnimado = (double) milisegundosAnimados / duracionAnimacion;
            double porcentajeAnimacion = ease(tiempoAnimado);

            sx += sxDestino * porcentajeAnimacion;
            sy += syDestino * porcentajeAnimacion;
        } else {
            sx += sxDestino;
            sy += syDestino;
        }
        dibujar.escalacion((float) sx, (float) sy);
    }

    public void rotacion(double thetaDestino, long inicioAnimacion, long finAnimacion) {
        if (tiempoActual < inicioAnimacion) {
            return;
        }

        if (tiempoActual <= finAnimacion) {
            long milisegundosAnimados = tiempoActual - inicioAnimacion;
            long duracionAnimacion = finAnimacion - inicioAnimacion;
            double porcentajeAnimacion = (double) milisegundosAnimados / duracionAnimacion;

            dibujar.rotacion((float) (theta + thetaDestino * porcentajeAnimacion));
        } else {
            dibujar.rotacion((float) (theta + thetaDestino));
            theta += thetaDestino;
        }
    }
    public void rotacionY3D(double thetaInicio,double thetaDestino, long inicioAnimacion, long finAnimacion) {
        if (tiempoActual < inicioAnimacion) {
            return;
        }

        if (tiempoActual <= finAnimacion) {
            long milisegundosAnimados = tiempoActual - inicioAnimacion;
            long duracionAnimacion = finAnimacion - inicioAnimacion;

            double tiempoAnimado = (double) milisegundosAnimados / duracionAnimacion;
            double porcentajeAnimacion = ease(tiempoAnimado);

            dibujar.setVectorRotacion(rotX3D,(double) (thetaInicio + thetaDestino * porcentajeAnimacion),0);
        }else{
            dibujar.setVectorRotacion(rotX3D,(double) (thetaInicio + thetaDestino ),0);

        }
    }
    public void escalacion3D(double dInicio,double dDestino, long inicioAnimacion, long finAnimacion) {
        if (tiempoActual < inicioAnimacion) {
            return;
        }

        if (tiempoActual <= finAnimacion) {
            long milisegundosAnimados = tiempoActual - inicioAnimacion;
            long duracionAnimacion = finAnimacion - inicioAnimacion;

            double tiempoAnimado = (double) milisegundosAnimados / duracionAnimacion;
            double porcentajeAnimacion = ease(tiempoAnimado);
            double escala=(double) (dInicio + dDestino * porcentajeAnimacion);
            dibujar.setVectorEscalacion(escala,escala,escala);
        }else{
            double escala=(double) (dInicio + dDestino);
            dibujar.setVectorEscalacion(escala,escala,escala);

        }
    }

    public void setInicioAplicacion(long inicioAplicacion) {
        this.inicioAplicacion = inicioAplicacion;
    }

    public void setDibujar(DibujarGallina dibujar) {
        tiempoActual = System.currentTimeMillis() - inicioAplicacion;
        this.dibujar = dibujar;
        reiniciarTransformaciones();
    }

    private void reiniciarTransformaciones() {
        dx = 0;
        dy = 0;
        sx = 1;
        sy = 1;
        theta = 0;
        isSuave=true;
        dibujar.reiniciarTransformaciones();
    }
    public void reiniciarRotacion() {
        theta = 0;
        isSuave=true;
        dibujar.reiniciarRotacion();
    }

    public void nuevaAnimacion() {
        reiniciarTransformaciones();
    }

    public void animacionSuave(boolean isSuave) {
        this.isSuave = isSuave;
    }
}
