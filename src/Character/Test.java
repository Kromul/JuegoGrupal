package Character;

import Libreria3D.MiLibreria3D;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * @author Alex
 */
public final class Test extends JFrame {

    //Constantes
    public final Point3d POS_CAMARA = new Point3d(0d, 5d, 10.3d);
    //Atributos
    public SimpleUniverse universo;
    public Canvas3D zonaDibujo;
    public Personaje personaje = new Personaje();
    public ControlPersonaje control = new ControlPersonaje(personaje);
    public ComportamientoMostrar mostrar = new ComportamientoMostrar(personaje);
    public TransformGroup TGcamara = new TransformGroup();

    public Test() {
        Canvas3D zonaDibujo = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        universo = new SimpleUniverse(zonaDibujo);
        TGcamara = universo.getViewingPlatform().getViewPlatformTransform();
        MiLibreria3D.colocarCamara(universo, new Point3d(0, 2, 5), new Point3d(0, 0, 0));
        getContentPane().add(zonaDibujo);
        BranchGroup escena = crearEscena();
        escena.compile();
        universo.addBranchGraph(escena);

        //Añadimos movimiento con ratón 
        // - Alex este trozo de codigo es un metodo de
        // la libreria que se llama anadirMovimientoCamara.
        OrbitBehavior B = new OrbitBehavior(zonaDibujo);
        B.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        universo.getViewingPlatform().setViewPlatformBehavior(B);
    }

    private BranchGroup crearEscena() {
        BranchGroup objRoot = new BranchGroup();
        //Añadimos la luz direccional 
        // - Alex este metodo tambien esta en la libreria y se llama
        // getDefaultIlumination
        DirectionalLight LuzDireccional = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(1f, 0f, -1f));
        BoundingSphere limites2 = new BoundingSphere(new Point3d(-5, 0, 5), 100.0);
        LuzDireccional.setInfluencingBounds(limites2);
        objRoot.addChild(LuzDireccional);

        //Añadimos el personaje
        objRoot.addChild(personaje.getTG());

        //Añadimos el control del personaje
        objRoot.addChild(control);

        //Añadimos el mostrar
        objRoot.addChild(mostrar);

        //Añadimos los ejes
        objRoot.addChild(crearEjes());

        return objRoot;
    }

    // Alex este metodo tambien existe en la libreria, tiene el mismo nombre
    private BranchGroup crearEjes() {
        BranchGroup grupoEjes = new BranchGroup();

        //Creamos los ejes
        LineArray ejeX = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        LineArray ejeY = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        LineArray ejeZ = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        ejeX.setCoordinate(0, new Point3f(-1.0f, 0.0f, 0.0f));
        ejeX.setCoordinate(1, new Point3f(1.0f, 0.0f, 0.0f));
        ejeY.setCoordinate(0, new Point3f(0.0f, -1.0f, 0.0f));
        ejeY.setCoordinate(1, new Point3f(0.0f, 1.0f, 0.0f));
        ejeZ.setCoordinate(0, new Point3f(0.0f, 0.0f, -1.0f));
        ejeZ.setCoordinate(1, new Point3f(0.0f, 0.0f, 1.0f));

        //Les damos color
        Color3f rojo = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f verde = new Color3f(0.0f, 1.0f, 0.0f);
        ejeX.setColor(0, rojo);
        ejeX.setColor(1, verde);
        ejeY.setColor(0, rojo);
        ejeY.setColor(1, verde);
        ejeZ.setColor(0, rojo);
        ejeZ.setColor(1, verde);

        //Los añadimos al BG
        grupoEjes.addChild(new Shape3D(ejeX));
        grupoEjes.addChild(new Shape3D(ejeY));
        grupoEjes.addChild(new Shape3D(ejeZ));

        return grupoEjes;
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setTitle("Test Animación");
        test.setSize(800, 600);
        test.setVisible(true);
    }
}
