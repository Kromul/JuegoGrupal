package Character;

import Libreria3D.MiLibreria3D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Point3d;

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
        MiLibreria3D.addMovimientoCamara(universo, zonaDibujo);
    }

    private BranchGroup crearEscena() {
        BranchGroup objRoot = new BranchGroup();
        //Añadimos la luz direccional 
        DirectionalLight luzDireccional = MiLibreria3D.getDefaultIlumination();
        objRoot.addChild(luzDireccional);

        //Añadimos el personaje
        objRoot.addChild(personaje.getTG());

        //Añadimos el control del personaje
        objRoot.addChild(control);

        //Añadimos el mostrar
        objRoot.addChild(mostrar);

        //Añadimos los ejes
        objRoot.addChild(MiLibreria3D.CrearEjesCoordenada());

        return objRoot;
    }
    
    public static void main(String[] args) {
        Test test = new Test();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setTitle("Test Animación");
        test.setSize(800, 600);
        test.setVisible(true);
    }
}
