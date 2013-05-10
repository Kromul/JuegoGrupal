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
    boolean cambioLibre = true;
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
                            if (ek.getKeyChar() == 'f') {
                                personaje.setAtacar(true);
                            }
                            if (!personaje.getAtacar()) {
                                if (ek.getKeyCode() == KeyEvent.VK_SHIFT) {
                                    if (cambioLibre) {
                                        if (personaje.getCorriendo()) {
                                            personaje.setCorriendo(false);
                                        } else {
                                            personaje.setCorriendo(true);
                                        }
                                        cambioLibre = false;
                                    }
                                } else if (ek.getKeyChar() == 'w') {
                                    personaje.setAdelante(true);
                                    if (personaje.getDerecha()) {
                                        personaje.setAdDer(true);
                                    } else if (personaje.getIzquierda()) {
                                        personaje.setAdIzq(true);
                                    }
                                } else if (ek.getKeyChar() == 's') {
                                    personaje.setAtras(true);
                                    if (personaje.getDerecha()) {
                                        personaje.setAtDer(true);
                                    } else if (personaje.getIzquierda()) {
                                        personaje.setAtIzq(true);
                                    }
                                } else if (ek.getKeyChar() == 'a') {
                                    personaje.setIzquierda(true);
                                    if (personaje.getAdelante()) {
                                        personaje.setAdIzq(true);
                                    } else if (personaje.getAtras()) {
                                        personaje.setAtIzq(true);
                                    }
                                } else if (ek.getKeyChar() == 'd') {
                                    personaje.setDerecha(true);
                                    if (personaje.getAdelante()) {
                                        personaje.setAdDer(true);
                                    } else if (personaje.getAtras()) {
                                        personaje.setAtDer(true);
                                    }
                                }
                            }
                        } else if (ek.getID() == KeyEvent.KEY_RELEASED) {
                            if (ek.getKeyCode() == KeyEvent.VK_SHIFT) {
                                cambioLibre = true;
                            } else if (ek.getKeyChar() == 'w') {
                                personaje.setAdelante(false);
                                if (personaje.getAdDer()) {
                                    personaje.setAdDer(false);
                                } else if (personaje.getAdIzq()) {
                                    personaje.setAdIzq(false);
                                }
                            } else if (ek.getKeyChar()
                                    == 's') {
                                personaje.setAtras(false);
                                if (personaje.getAtDer()) {
                                    personaje.setAtDer(false);
                                } else if (personaje.getAtIzq()) {
                                    personaje.setAtIzq(false);
                                }
                            } else if (ek.getKeyChar() == 'a') {
                                personaje.setIzquierda(false);
                                if (personaje.getAdIzq()) {
                                    personaje.setAdIzq(false);
                                } else if (personaje.getAtIzq()) {
                                    personaje.setAtIzq(false);
                                }
                            } else if (ek.getKeyChar() == 'd') {
                                personaje.setDerecha(false);
                                if (personaje.getAdDer()) {
                                    personaje.setAdDer(false);
                                } else if (personaje.getAtDer()) {
                                    personaje.setAtDer(false);
                                }
                            } else if (ek.getKeyChar() == 'f') {
                                personaje.setAtacar(false);
                            }
                        }
                    }
                }
                wakeupOn(keepUpCondition);
            }
        }
    }
}
