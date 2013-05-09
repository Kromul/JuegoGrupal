package Character;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;

/**
 * @author Alex
 */
public class ControlPersonaje extends javax.media.j3d.Behavior {

    Personaje personaje;
    TransformGroup tgPersonaje;
    WakeupOnAWTEvent presionada = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    WakeupOnAWTEvent liberada = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
    WakeupCondition keepUpCondition = null;
    WakeupCriterion[] continueArray = new WakeupCriterion[2];

    public ControlPersonaje(Personaje personaje) {
        this.personaje = personaje;
        tgPersonaje = personaje.getTG();
        continueArray[0] = liberada;
        continueArray[1] = presionada;
        keepUpCondition = new WakeupOr(continueArray);
        setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
    }

    @Override
    public void initialize() {
        wakeupOn(keepUpCondition);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion ster = (WakeupCriterion) criteria.nextElement();
            if (ster instanceof WakeupOnAWTEvent) {
                AWTEvent[] events = ((WakeupOnAWTEvent) ster).getAWTEvent();
                for (int n = 0; n < events.length; n++) {
                    if (events[n] instanceof KeyEvent) {
                        KeyEvent ek = (KeyEvent) events[n];
                        if (ek.getID() == KeyEvent.KEY_PRESSED) {
                            if (ek.getKeyChar()
                                    == 'w') {
                                personaje.setAdelante(true);
                            } else if (ek.getKeyChar() == 's') {
                                personaje.setAtras(true);
                            }
                            if (ek.getKeyChar() == 'a') {
                                personaje.setIzquierda(true);
                            } else if (ek.getKeyChar() == 'd') {
                                personaje.setDerecha(true);
                            }
                        } else if (ek.getID() == KeyEvent.KEY_RELEASED) {
                            if (ek.getKeyChar()
                                    == 'w') {
                                personaje.setAdelante(false);
                            } else if (ek.getKeyChar()
                                    == 's') {
                                personaje.setAtras(false);
                            } else if (ek.getKeyChar() == 'a') {
                                personaje.setIzquierda(false);
                            } else if (ek.getKeyChar() == 'd') {
                                personaje.setDerecha(false);
                            }
                        }
                    }
                }
                wakeupOn(keepUpCondition);
            }
        }
    }
}
