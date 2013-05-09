/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Character;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Alex
 */
public class ComportamientoMostrar extends Behavior {

    //Constantes
    private final float VEL_ANDAR = 0.005f;
    private final float VEL_CORRER = 0.02f;
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
        Vector3f nuevaPosicion = new Vector3f(0f, 0f, 0f);
        if (personaje.getAdelante() || personaje.getAtras() || personaje.getIzquierda() || personaje.getDerecha()) {
            if (personaje.getDerecha()) {
                nuevaPosicion = new Vector3f(0f, VEL_CORRER, 0f);
                personaje.rotarDerecha();
            }
            if (personaje.getIzquierda()) {
                nuevaPosicion = new Vector3f(0f, VEL_CORRER, 0f);
                personaje.rotarIzquierda();
            }
            if (personaje.getAdelante()) {
                nuevaPosicion = new Vector3f(0f, VEL_CORRER, 0f);
                personaje.rotarAdelante();
            }
            if (personaje.getAtras()) {
                nuevaPosicion = new Vector3f(0f, VEL_CORRER, 0f);
                personaje.rotarAtras();
            }
            System.out.println("Posicion: " + personaje.getPosicion());
            (new Thread() {
                public void run() {
                    personaje.correr();
                }
            }).start();
            personaje.desplazar(nuevaPosicion);
        } else {
            if (personaje.getAndando()) {
                personaje.correr();
            }
        }
    }
}
