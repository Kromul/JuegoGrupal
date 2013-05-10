/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CreacionMapas;

import Character.ComportamientoMostrar;
import Character.ControlPersonaje;
import Character.Personaje;
import Libreria3D.MiLibreria3D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    // Personaje
    public Personaje personaje = new Personaje();
    public ControlPersonaje control = new ControlPersonaje(personaje);
    public ComportamientoMostrar mostrar = new ComportamientoMostrar(personaje);
    // Constantes
    final String NO_EXISTE = "archivo no existente";
    final Float ESPACIO_Z = 2.0f; // espacio en el eje z entre los objetos

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

        // Creamos el mundo 3D
        rootBG.addChild(crearMundo());

        //Añadimos el personaje
        rootBG.addChild(MiLibreria3D.trasladarEstatico(personaje.getTG(), new Vector3f(-2, 0, -2)));

        //Añadimos el control del personaje
        rootBG.addChild(control);

        //Añadimos el mostrar
        rootBG.addChild(mostrar);

        // Elementos por defecto de la escena
        String rutaFondo = System.getProperty("user.dir") + "/" + "src/resources/texturas/textura_cielo.jpg";
        String rutaSonido = "file://localhost/" + System.getProperty("user.dir") + "/" + "src/resources/sonido/magic_bells.wav";
        String rutaSuelo = System.getProperty("user.dir") + "/" + "src/resources/texturas/textura_hielo.jpg";
        MiLibreria3D.setBackground(rootBG, rutaFondo, this, 1);
        MiLibreria3D.addSound(universo, rootBG, rutaSonido);
        try {
            rootBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, -1.0f, 0.0f),
                    MiLibreria3D.tipoFigura.rectangulo, 20.0f, 1.0f, 20.0f,
                    MiLibreria3D.getTexture(rutaSuelo, this),
                    null));
        } catch (Exception ex) {
            Logger.getLogger(BigMind.class.getName()).log(Level.SEVERE, null, ex);
        }
        rootBG.addChild(MiLibreria3D.CrearEjesCoordenada());
        rootBG.addChild(MiLibreria3D.getDefaultIlumination());

        return rootBG;
    }

    public String leerEscena(String escena, String archivo) {
        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "//" + "src//" + archivo);
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

    private BranchGroup crearMundo() {
        BranchGroup mundoBG = new BranchGroup();
        try {
            // Leemos el fichero de texto a partir del cual leemos la escena
            String escena = "";
            escena = escena + leerEscena(escena, "resources//escenarios//Arboles.txt");

            // Leer informacion de OBJ
            String info_obj = "";
            info_obj = info_obj + leerEscena(escena, "CreacionMapas//info_obj.txt");

            StringTokenizer str = new StringTokenizer(escena, "\t");

            // Configuramos los rectangulos que crearan la escena
            float ancho = 0.5f;
            float largo = 0.5f;
            float alto = 0.1f;
            float posInicialX = 0.0f;
            float posInicialY = 0.0f;
            float posInicialZ = 0.0f;
            float posSiguienteX = posInicialX;
            float posSiguienteY = posInicialY;
            float posSiguienteZ = posInicialZ;
            MiLibreria3D.tipoFigura tipoFigura = MiLibreria3D.tipoFigura.rectangulo;
            String urlTexturaMuro = System.getProperty("user.dir") + "//" + "src//resources//textura_muro.jpg";

            // Creamos el escenario

            while (str.hasMoreTokens()) {
                String elemento = str.nextToken();
                System.out.println(elemento);
                if (elemento.contains("final")) {
                    posSiguienteX = posInicialX;
                    posSiguienteZ = posSiguienteZ + ESPACIO_Z;
                } else if (elemento.contains("suelo")) {
                    posSiguienteX = posSiguienteX + ancho * 2;
                } else if (elemento.contains("muro")) {
                    posSiguienteX = posSiguienteX + ancho * 2;
                    mundoBG.addChild(MiLibreria3D.crear(new Vector3f(posSiguienteX, posSiguienteY, posSiguienteZ),
                            tipoFigura, ancho, alto, largo,
                            MiLibreria3D.getTexture(urlTexturaMuro, this),
                            ""));
                } else if (elemento.contains("obj")) {
                    // Una vez que sabemos que es un OBJ vemos en que carpeta esta
                    // y que archivo debemos de coger
                    String carpeta = NO_EXISTE;
                    String archivo = NO_EXISTE;
                    if (elemento.contains("natur")) {
                        carpeta = "naturaleza";
                        if (elemento.contains("asteroid")) {
                            archivo = "asteroid";
                        }
                    } else if (elemento.contains("ataq")) {
                        carpeta = "ataques";
                    } else if (elemento.contains("edif")) {
                        carpeta = "edificios";
                        if (elemento.contains("fence")) {
                            archivo = "fence";
                        }
                    }

                    // Conseguimos la escala
                    float escala = (float) elemento.charAt(elemento.lastIndexOf("e") + 1) - 48;
                    // Buscamos cual es la posicion de inicio del objeto en el archivo info_obj.txt
                    Vector3f posicion = new Vector3f();
                    StringTokenizer str_info = new StringTokenizer(info_obj, "\t");
                    String elemento_info = "";
                    boolean encontradoPosicion = false;
                    while (str_info.hasMoreTokens() && !encontradoPosicion) {
                        elemento_info = str_info.nextToken();
                        if (elemento_info.equalsIgnoreCase(archivo)) {
                            encontradoPosicion = true;
                        }
                    }

                    if (!encontradoPosicion || carpeta.equalsIgnoreCase(NO_EXISTE) || archivo.equalsIgnoreCase(NO_EXISTE)) {
                        throw new IllegalArgumentException("La carpeta/archivo OBJ señalado no existe");
                    } else {
                        System.out.println(escala);
                        posSiguienteX = posSiguienteX + (Float.parseFloat(str_info.nextToken()) * escala) * 2;
                        posSiguienteY = Float.parseFloat(str_info.nextToken()) * escala;

                        posicion = new Vector3f(posSiguienteX, posSiguienteY, posSiguienteZ + Float.parseFloat(str_info.nextToken()) * escala);

                        System.out.println(posSiguienteX);
                        System.out.println(posSiguienteY);
                        System.out.println(posSiguienteZ);
                    }

                    System.out.println(carpeta);
                    System.out.println(archivo);
                    System.out.println(posSiguienteY);

                    // Lo introducimos dentro del arbol y lo trasladamos al lugar correcto
                    mundoBG.addChild(MiLibreria3D.crear(new Vector3f(posSiguienteX, posSiguienteY, posSiguienteZ),
                            MiLibreria3D.tipoFigura.objetoOBJ, escala, null, null,
                            null,
                            System.getProperty("user.dir") + "/" + "src/resources/objetosOBJ/" + carpeta + "/" + archivo + ".obj"));


                }
            }

            // Situamos el objeto OBJ
//            String objetoURL = System.getProperty("user.dir") + "/" + "src/resources/objetosOBJ/edificios/city.obj";
//            rootBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, 7.25f, 0.0f),
//                    MiLibreria3D.tipoFigura.objetoOBJ, 20.0f, null, null,
//                    null,
//                    objetoURL));

            String objetoURL2 = System.getProperty("user.dir") + "/" + "src/resources/objetosOBJ/naturaleza/asteroid.obj";
            float escala = 1;
//            mundoBG.addChild(MiLibreria3D.crear(new Vector3f(.90f*escala, 0.55f*escala, 0.60f*escala),
//                    MiLibreria3D.tipoFigura.objetoOBJ, escala, null, null,
//                    null,
//                    objetoURL2));

            String urlFuego = System.getProperty("user.dir") + "//" + "src//resources//texturas//textura_fuego.jpg";
            // Situamos una esfera
//            mundoBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, 0.75f, 0.0f),
//                    MiLibreria3D.tipoFigura.esfera, 0.25f, null, null,
//                    MiLibreria3D.getTexture(urlFuego, this),
//                    null));

            // Situamos un cilindro
//            mundoBG.addChild(MiLibreria3D.crear(new Vector3f(0.0f, 1.5f, 0.0f),
//                    MiLibreria3D.tipoFigura.cilindro, 0.25f, 0.25f, null,
//                    MiLibreria3D.getTexture(urlFuego, this),
//                    null));

        } catch (Exception ex) {
            Logger.getLogger(BigMind.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mundoBG;
    }
}
