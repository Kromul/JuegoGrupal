package Character;

import CreacionMapas.BigMind;
import Libreria3D.MiLibreria3D;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    BigMind mundo;
    Personaje personaje;
    TransformGroup TG_personaje;
    WakeupOnElapsedFrames framewake = new WakeupOnElapsedFrames(0);

    public ComportamientoMostrar(BigMind mundo) {
        this.mundo = mundo;
        personaje = mundo.personaje;
        TG_personaje = personaje.getTG();
        setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
    }

    @Override
    public void initialize() {
        wakeupOn(framewake);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        actualizarCamara();
        actualizarPersonaje();

        wakeupOn(framewake);
    }

    private void actualizarPersonaje() {
        if (!personaje.getAtacando()) {
            if (personaje.getAtacar()) {
                personaje.atacar();
                (new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1300);
                            personaje.setAtacando(false);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ComportamientoMostrar.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
            } else if (personaje.getAdelante() || personaje.getAtras() || personaje.getIzquierda() || personaje.getDerecha()) {
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
                //System.out.println("Posicion: " + personaje.getPosicion());
                //System.out.println("Apuntando a: " + personaje.getPuntoDireccion());
                //System.out.println("");
                (new Thread() {
                    public void run() {
                        personaje.moverse();
                    }
                }).start();
                personaje.desplazar(nuevaPosicion);
            } else {
                if (personaje.getAndando()) {
                    personaje.moverse();
                }
            }
        }
    }

    private void actualizarCamara() {
        Point3d objetivo = new Point3d(personaje.getPosicion().getX(), personaje.getPosicion().getY(), personaje.getPosicion().getZ());
        Point3d posicion = new Point3d(personaje.getPosicion().getX(), personaje.getPosicion().getY() + 5, personaje.getPosicion().getZ() + 10.3);
        mundo.colocarCamaraDinamico(posicion, objetivo);
    }
}
