/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Character;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Alex
 */
public class ComportamientoMostrar extends Behavior {

    //Constantes
    private final float VEL_ANDAR_V = 0.005f;
    private final float VEL_ANDAR_H = 0.003f;
    private final float VEL_CORRER_V = 0.012f;
    private final float VEL_CORRER_H = 0.01f;
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
        Transform3D t3dNuevaPersonaje = new Transform3D();

        if (personaje.getAdelante() || personaje.getAtras() || personaje.getIzquierda() || personaje.getDerecha()) {
            
            (new Thread() {
                public void run() {
                    personaje.correr();
                }
            }).start();
            
            if (personaje.getDerecha()) {
                t3dNuevaPersonaje.set(new Vector3f(0f, 0f, VEL_CORRER_H));
            }
            if (personaje.getIzquierda()) {
                t3dNuevaPersonaje.set(new Vector3f(0f, 0f, VEL_CORRER_H));
            }
            if (personaje.getAdelante()) {
                t3dNuevaPersonaje.set(new Vector3f(0f, 0f, VEL_CORRER_V));
            }
            if (personaje.getAtras()) {
                t3dNuevaPersonaje.set(new Vector3f(0f, 0f, VEL_CORRER_V));
            }
        } else {
            if (personaje.getAndando()) {
                personaje.correr();
            }
        }
        Transform3D t3dPersonaje = new Transform3D();
        TG_personaje.getTransform(t3dPersonaje);
        t3dPersonaje.mul(t3dNuevaPersonaje);
        TG_personaje.setTransform(t3dPersonaje);
        Vector3d nuevaPosicion = new Vector3d();
        t3dNuevaPersonaje.get(nuevaPosicion);
    }
}
