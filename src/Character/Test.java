/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Character;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
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

    public Test() {
        Canvas3D zonaDibujo = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        universo = new SimpleUniverse(zonaDibujo);
        universo.getViewingPlatform().setNominalViewingTransform();
        getContentPane().add(zonaDibujo);
        BranchGroup escena = crearEscena();
        escena.compile();
        universo.addBranchGraph(escena);

        //Añadimos movimiento con ratón
        OrbitBehavior B = new OrbitBehavior(zonaDibujo);
        B.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        universo.getViewingPlatform().setViewPlatformBehavior(B);
    }

    public BranchGroup crearEscena() {
        BranchGroup objRoot = new BranchGroup();
        //Añadimos la luz direccional
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

        return objRoot;
    }

    public void run() {
        mostrar.run();
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setTitle("Test");
        test.setSize(800, 600);
        test.setVisible(true);
        test.run();
    }
}
