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
    //Atributos
    private Scene personaje;
    private AnimationBehavior animacion;
    private TransformGroup tgPersonaje;
    private Point3f posicion;
    public boolean adelante, atras, izquierda, derecha;
    private boolean andando = false;

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
            tgPersonaje.addChild(bgPersonaje);
            //Inicialización de los atributos
            posicion = new Point3f(0, 0, 0);
            adelante = atras = izquierda = derecha = false;
        } catch (MalformedURLException ex) {
        } catch (FileNotFoundException ex) {
        } catch (IncorrectFormatException ex) {
        } catch (ParsingErrorException ex) {
        }
    }

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

    public String getDireccion() {
        String direccion = null;
        if (adelante) {
            direccion = "adelante";
        } else if (atras) {
            direccion = "atras";
        } else if (izquierda) {
            direccion = "izquierda";
        } else if (derecha) {
            direccion = "derecha";
        }
        return direccion;
    }

    public void andar() {
        if (!andando) {
            animacion.playAnimation(ACCION_CORRER, true);
            andando = true;
        }
    }
}
