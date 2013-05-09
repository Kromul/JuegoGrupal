/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CreacionMapas;

import Libreria3D.MiLibreria3D;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author papa
 */
public class BigMind extends JFrame {

    SimpleUniverse universo;

    public BigMind() {
        Container GranPanel = getContentPane();
        JPanel Controles = new JPanel(new GridLayout(1, 4));
        Canvas3D dibujo3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        GranPanel.add(dibujo3D, BorderLayout.NORTH);
        GranPanel.add(Controles, BorderLayout.SOUTH);
        dibujo3D.setPreferredSize(new Dimension(780, 580));
        universo = new SimpleUniverse(dibujo3D);
        BranchGroup escena = crearEscena();
        escena.compile();
        //This moves the ViewPlatform back a bit so objects can be viewed.
        universo.getViewingPlatform().setNominalViewingTransform();
        universo.addBranchGraph(escena);
        pack();
        setVisible(true);

        MiLibreria3D.addMovimientoCamara(universo, dibujo3D);
        MiLibreria3D.colocarCamara(universo, new Point3d(-20, 10, -20), new Point3d(5, 0, 5));
    }

    BranchGroup crearEscena() {
        BranchGroup rootBG = new BranchGroup();
        BranchGroup escenaBG = new BranchGroup();
        BranchGroup elefanteBG = new BranchGroup();
        try {
            String escena = "";
            escena = escena + leerArchivo(escena, "EscenaBasica.txt");
            StringTokenizer str = new StringTokenizer(escena, "\t");

            // Configuramos los rectangulos que crearan la escena
            float ancho = 0.5f;
            float largo = 0.5f;
            float alto = 0.1f;
            float posInicialX = 0.0f;
            float posInicialY = 0.0f;
            float posInicialZ = 0.0f;
            float posSiguienteX = 0.0f;
            float posSiguienteY = alto;
            float posSiguienteZ = 0.0f;
            MiLibreria3D.tipoFigura tipoFigura = MiLibreria3D.tipoFigura.rectangulo;
            String urlTexturaMuro = System.getProperty("user.dir") + "//" + "src//resources//textura_muro.jpg";

            // Creamos el escenario
            while (str.hasMoreTokens()) {
                String elemento = str.nextToken();
                System.out.println(elemento);
                if (elemento.contains("final")) {
                    posSiguienteX = posInicialX;
                    posSiguienteZ = posSiguienteZ + largo * 2;
                } else if (elemento.contains("suelo")) {
                    posSiguienteX = posSiguienteX + ancho * 2;
                } else if (elemento.contains("muro")) {
                    posSiguienteX = posSiguienteX + ancho * 2;

                    escenaBG.addChild(MiLibreria3D.crear(new Vector3f(posSiguienteX, posSiguienteY, posSiguienteZ),
                            tipoFigura, ancho, alto, largo,
                            MiLibreria3D.getTexture(urlTexturaMuro, this),
                            ""));
                }
            }
            rootBG.addChild(escenaBG);

            // Situamos el elefante
            String elefanteURL = System.getProperty("user.dir") + "/" + "src/resources/objetosOBJ/elephav.obj";
            rootBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, 0.15f, 0.0f),
                    MiLibreria3D.tipoFigura.objetoOBJ, 0.25f, null, null,
                    null,
                    elefanteURL));

            String urlFuego = System.getProperty("user.dir") + "//" + "src//resources//textura_fuego.jpg";
            // Situamos una esfera
            rootBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, 0.75f, 0.0f),
                    MiLibreria3D.tipoFigura.esfera, 0.25f, null, null,
                    MiLibreria3D.getTexture(urlFuego, this),
                    null));

            // Situamos un cilindro
            rootBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, 1.5f, 0.0f),
                    MiLibreria3D.tipoFigura.cilindro, 0.25f, 0.25f, null,
                    MiLibreria3D.getTexture(urlFuego, this),
                    null));

        } catch (Exception ex) {
            Logger.getLogger(BigMind.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Elementos por defecto de la escena
        String rutaFondo = System.getProperty("user.dir") + "/" + "src/resources/textura_cielo.jpg";
        String rutaSonido = "file://localhost/" + System.getProperty("user.dir") + "/" + "src/resources/magic_bells.wav";
        MiLibreria3D.setBackground(rootBG, rutaFondo, this, 1);
        MiLibreria3D.addSound(universo, rootBG, rutaSonido);
        rootBG.addChild(MiLibreria3D.CrearSuelo());
        rootBG.addChild(MiLibreria3D.CrearEjesCoordenada());
        rootBG.addChild(MiLibreria3D.getDefaultIlumination());

        return rootBG;
    }

    public String leerArchivo(String escena, String archivo) {
        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "//" + "src//resources//escenarios//" + archivo);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            // Leer el archivo linea por linea
            while ((strLinea = buffer.readLine()) != null) {
                // Imprimimos la línea por pantalla
                //System.out.println (strLinea);
                escena = escena + strLinea;
            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
        return escena;
    }
}
