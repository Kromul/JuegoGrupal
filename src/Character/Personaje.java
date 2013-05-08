/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Character;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;
import net.sf.nwn.loader.AnimationBehavior;
import net.sf.nwn.loader.NWNLoader;

/**
 *
 * @author Alex
 */
public class Personaje {

    //Constantes
    private final String RUTA = ("file://localhost/" + System.getProperty("user.dir") + "/");
    private final String MDL = "iron_golem.mdl";
    private final String ACCION_CORRER = "iron_golem:crun";
    private final String ACCION_ANDAR = "iron_golem:cwalk";
    private final String ACCION_ATACAR = "iron_golem:cstab";
    private final String ACCION_PAUSA = "iron_golem:cpause";
    //Atributos
    private Scene personaje;
    private AnimationBehavior animacion;
    private TransformGroup tgPersonaje;
    private Point3f posicion;
    private boolean adelante, atras, izquierda, derecha;
    private boolean andando;

    public Personaje() {
        try {
            //Inicialización del personaje
            NWNLoader nwn2 = new NWNLoader();
            nwn2.enableModelCache(true);
            personaje = nwn2.load(new URL(RUTA + MDL));
            BranchGroup bgPersonaje = personaje.getSceneGroup();
            animacion = (AnimationBehavior) personaje.getNamedObjects().get("AnimationBehavior");
            //Inicialización del transform group
            tgPersonaje = new TransformGroup();
            tgPersonaje.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tgPersonaje.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            rotar("x", -90);
            escalar((float) 0.3);
            tgPersonaje.addChild(bgPersonaje);
            //Inicialización de los atributos
            posicion = new Point3f(0, 0, 0);
            adelante = atras = izquierda = derecha = andando = false;
        } catch (MalformedURLException ex) {
        } catch (FileNotFoundException ex) {
        } catch (IncorrectFormatException ex) {
        } catch (ParsingErrorException ex) {
        }
    }

    //Get y set
    public TransformGroup getTG() {
        return tgPersonaje;
    }

    public void setAdelante(boolean estado) {
        adelante = estado;
    }

    public void setAtras(boolean estado) {
        atras = estado;
    }

    public void setIzquierda(boolean estado) {
        izquierda = estado;
    }

    public void setDerecha(boolean estado) {
        derecha = estado;
    }

    public boolean getAdelante() {
        return adelante;
    }

    public boolean getAtras() {
        return atras;
    }

    public boolean getIzquierda() {
        return izquierda;
    }

    public boolean getDerecha() {
        return derecha;
    }

    public boolean getAndando() {
        return andando;
    }

    //Transformaciones
    public void rotar(String eje, float angulo) {
        Transform3D nueva = rotarObjeto(eje, angulo);
        Transform3D actual = new Transform3D();
        tgPersonaje.getTransform(actual);
        actual.mul(nueva);
        tgPersonaje.setTransform(actual);
    }

    public void escalar(float escala) {
        Transform3D nueva = escalarObjeto(escala);
        Transform3D actual = new Transform3D();
        tgPersonaje.getTransform(actual);
        actual.mul(nueva);
        tgPersonaje.setTransform(actual);
    }

    //Animaciones
    public void correr() {
        if (!andando) {
            animacion.playAnimation(ACCION_CORRER, true);
            andando = true;
        }
        if (andando && !adelante && !atras && !izquierda && !derecha) {
            animacion.playDefaultAnimation();
            andando = false;
        }
    }

    //Métodos de librería
    private Transform3D rotarObjeto(String eje, float angulo) {
        angulo = convertir_A_Radianes(angulo);
        Transform3D rotarObj = new Transform3D();
        if (eje.toLowerCase().equals("x")) {
            rotarObj.rotX(angulo);
        } else if (eje.toLowerCase().equals("y")) {
            rotarObj.rotY(angulo);
        } else if (eje.toLowerCase().equals("z")) {
            rotarObj.rotZ(angulo);
        }
        return rotarObj;
    }

    private float convertir_A_Radianes(float angulo) {
        float resultado = angulo;
        resultado = resultado / 180;
        resultado = (float) (resultado * Math.PI);
        return resultado;
    }

    private Transform3D escalarObjeto(float escala) {
        Transform3D escalarObj = new Transform3D();
        escalarObj.setScale(escala);
        return escalarObj;
    }
}
