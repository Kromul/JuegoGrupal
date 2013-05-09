package Character;

import Libreria3D.MiLibreria3D;
import Libreria3D.MiLibreria3D.Direccion;
import Libreria3D.MiLibreria3D.tipoTransformacion;
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
import javax.vecmath.Vector3f;
import net.sf.nwn.loader.AnimationBehavior;
import net.sf.nwn.loader.NWNLoader;

/**
 * @author Alex
 */
public class Personaje {

    //Constantes
    private final String RUTA = ("file://localhost/" + System.getProperty("user.dir") + "/src/resources/objetosMDL/");
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
    private Direccion direccion;
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
            rotar(tipoTransformacion.enX, -90);
            escalar((float) 0.3);
            tgPersonaje.addChild(bgPersonaje);
            //Inicialización de los atributos
            posicion = new Point3f(0, 0, 0);
            direccion = Direccion.adelante;
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

    public Point3f getPosicion() {
        return posicion;
    }

    //Transformaciones
    public void rotarIzquierda() {
        if (direccion.equals(Direccion.derecha)) {
            rotar(tipoTransformacion.enZ, 180);
        } else if (direccion.equals(Direccion.adelante)) {
            rotar(tipoTransformacion.enZ, 90);
        } else if (direccion.equals(Direccion.atras)) {
            rotar(tipoTransformacion.enZ, -90);
        }
        direccion = Direccion.izquierda;
    }

    public void rotarDerecha() {
        if (direccion.equals(Direccion.izquierda)) {
            rotar(tipoTransformacion.enZ, 180);
        } else if (direccion.equals(Direccion.adelante)) {
            rotar(tipoTransformacion.enZ, -90);
        } else if (direccion.equals(Direccion.atras)) {
            rotar(tipoTransformacion.enZ, 90);
        }
        direccion = Direccion.derecha;
    }

    public void rotarAdelante() {
        if (direccion.equals(Direccion.derecha)) {
            rotar(tipoTransformacion.enZ, 90);
        } else if (direccion.equals(Direccion.izquierda)) {
            rotar(tipoTransformacion.enZ, -90);
        } else if (direccion.equals(Direccion.atras)) {
            rotar(tipoTransformacion.enZ, 180);
        }
        direccion = Direccion.adelante;
    }

    public void rotarAtras() {
        if (direccion.equals(Direccion.derecha)) {
            rotar(tipoTransformacion.enZ, -90);
        } else if (direccion.equals(Direccion.izquierda)) {
            rotar(tipoTransformacion.enZ, 90);
        } else if (direccion.equals(Direccion.adelante)) {
            rotar(tipoTransformacion.enZ, 180);
        }
        direccion = Direccion.atras;
    }

    public void escalar(float escala) {
        Transform3D nueva = MiLibreria3D.escalarDinamico(escala);
        Transform3D actual = new Transform3D();
        tgPersonaje.getTransform(actual);
        actual.mul(nueva);
        tgPersonaje.setTransform(actual);
    }

    public void desplazar(Vector3f distancia) {
        Transform3D nueva = MiLibreria3D.trasladarDinamico(distancia);
        Transform3D actual = new Transform3D();
        tgPersonaje.getTransform(actual);
        actual.mul(nueva);
        tgPersonaje.setTransform(actual);
        if (direccion.equals(Direccion.adelante)) {
            posicion.setZ(posicion.getZ() - distancia.getY());
        } else if (direccion.equals(Direccion.atras)) {
            posicion.setZ(posicion.getZ() + distancia.getY());
        } else if (direccion.equals(Direccion.izquierda)) {
            posicion.setX(posicion.getX() - distancia.getY());
        } else if (direccion.equals(Direccion.derecha)) {
            posicion.setX(posicion.getX() + distancia.getY());
        }
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

    //Métodos auxiliares
    private void rotar(tipoTransformacion eje, float angulo) {
        Transform3D nueva = MiLibreria3D.rotarDinamico(eje, angulo);
        Transform3D actual = new Transform3D();
        tgPersonaje.getTransform(actual);
        actual.mul(nueva);
        tgPersonaje.setTransform(actual);
    }
}
