package Character;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 * @author Alex
 */
public class ComportamientoMostrar extends Behavior {

    //Constantes
    private final float VEL_ANDAR = 0.02f;
    private final float VEL_CORRER = 0.05f;
    //Atributos
    Personaje personaje;
    TransformGroup TG_personaje;
    WakeupOnElapsedFrames framewake = new WakeupOnElapsedFrames(0);

    public ComportamientoMostrar(Personaje personaje) {
        this.personaje = personaje;
        TG_personaje = personaje.getTG();
        setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
    }

    @Override
    public void initialize() {
        wakeupOn(framewake);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        actualizarPersonaje();

        wakeupOn(framewake);
    }

    private void actualizarPersonaje() {
        if (personaje.getAdelante() || personaje.getAtras() || personaje.getIzquierda() || personaje.getDerecha()) {
            Vector3f nuevaPosicion;
            if (personaje.getCorriendo()) {
                nuevaPosicion = new Vector3f(0f, VEL_CORRER, 0f);
            } else {
                nuevaPosicion = new Vector3f(0f, VEL_ANDAR, 0f);
            }
            if (personaje.getAdDer()) {
                personaje.rotarAdelanteDerecha();
            } else if (personaje.getAdIzq()) {
                personaje.rotarAdelanteIzquierda();
            } else if (personaje.getAtDer()) {
                personaje.rotarAtrasDerecha();
            } else if (personaje.getAtIzq()) {
                personaje.rotarAtrasIzquierda();
            } else {
                if (personaje.getDerecha()) {
                    personaje.rotarDerecha();
                }
                if (personaje.getIzquierda()) {
                    personaje.rotarIzquierda();
                }
                if (personaje.getAdelante()) {
                    personaje.rotarAdelante();
                }
                if (personaje.getAtras()) {
                    personaje.rotarAtras();
                }
            }
            System.out.println("Posicion actual: " + personaje.getPosicion());
            System.out.println("Apuntando: " + personaje.getPuntoDireccion());
            System.out.println();
            if (personaje.getCorriendo()) {
                (new Thread() {
                    public void run() {
                        personaje.correr();
                    }
                }).start();
            } else {
                (new Thread() {
                    public void run() {
                        personaje.andar();
                    }
                }).start();
            }
            personaje.desplazar(nuevaPosicion);
        } else {
            if (personaje.getAndando()) {
                personaje.andar();
            }
            if (personaje.getAtacar()) {
                personaje.atacar();
            }
        }
    }
}
