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
    //public ComportamientoMostrar mostrar = new ComportamientoMostrar(this);
    // Constantes
    final String NO_EXISTE = "archivo no existente";
    final Float ESPACIO_Z = 3.0f; // espacio en el eje z entre los objetos
    // Escena
    String matrixScene[][];

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
        //rootBG.addChild(mostrar);

        // Elementos por defecto de la escena
        String rutaFondo = System.getProperty("user.dir") + "/" + "src/resources/texturas/textura_cielo.jpg";
        String rutaSonido = "file://localhost/" + System.getProperty("user.dir") + "/" + "src/resources/sonido/magic_bells.wav";
        String rutaSuelo = System.getProperty("user.dir") + "/" + "src/resources/texturas/textura_hielo.jpg";
        MiLibreria3D.setBackground(rootBG, rutaFondo, this, 1);
//        MiLibreria3D.addSound(universo, rootBG, rutaSonido);
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
            float posAnteriorX = posInicialX;
            float posAnteriorY = posInicialY;
            float posAnteriorZ = posInicialZ;
            float escala = 1;
            MiLibreria3D.tipoTrans[] transformacion = new MiLibreria3D.tipoTrans[3];
            float[] grados = new float[3];
            Float[] posManual = new Float[3];
            boolean esObjetoInicial = true; // esto nos sirve para colocar justo en (0,0,0) el comienzo del mapa
            MiLibreria3D.tipoFigura tipoFigura = MiLibreria3D.tipoFigura.rectangulo;
            String urlTexturaMuro = System.getProperty("user.dir") + "//" + "src//resources//textura_muro.jpg";

            // Creamos el escenario
            while (str.hasMoreTokens()) {
                String elemento = str.nextToken();
                if (elemento.contains("final")) {
                    posSiguienteX = posInicialX;
                    posSiguienteZ = posSiguienteZ + ESPACIO_Z;
                    esObjetoInicial = true;
                } else if (elemento.contains("espacio")) {
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
                        } else if (elemento.contains("palm")) {
                            archivo = "palm";
                        } else if (elemento.contains("tree")) {
                            archivo = "tree";
                        } else if (elemento.contains("tree_dry")) {
                            archivo = "tree_dry";
                        } else if (elemento.contains("tree_conifer")) {
                            archivo = "tree_conifer";
                        } else if (elemento.contains("druid_morning_star")) {
                            archivo = "druid_morning_star";
                        } else if (elemento.contains("elephant")) {
                            archivo = "elephant";
                        } else if (elemento.contains("turtle")) {
                            archivo = "turtle";
                        } else if (elemento.contains("mossy_rock")) {
                            archivo = "mossy_rock";
                        }
                    } else if (elemento.contains("ataq")) {
                        carpeta = "ataques";
                        if (elemento.contains("mine")) {
                            archivo = "mine";
                        } else if (elemento.contains("microbio")) {
//                            archivo = "microbio";
                        } else if (elemento.contains("war_axe")) {
                            archivo = "war_axe";
                        }
                    } else if (elemento.contains("edif")) {
                        carpeta = "edificios";
                        if (elemento.contains("house")) {
                            archivo = "house";
                        } else if (elemento.contains("fence")) {
                            archivo = "fence";
                        } else if (elemento.contains("granja")) {
                            archivo = "granja";
                        } else if (elemento.contains("granero")) {
                            archivo = "granero";
                        } else if (elemento.contains("rural_stall")) {
                            archivo = "rural_stall";
                        } else if (elemento.contains("bar_concreto2")) {
                            archivo = "bar_concreto2";
                        } else if (elemento.contains("bar_concreto")) {
                            archivo = "bar_concreto";
                        } else if (elemento.contains("brick_shader")) {
                            archivo = "brick_shader";
                        }
                    }

                    // Conseguimos la escala
                    escala = (float) elemento.charAt(elemento.lastIndexOf("e") + 1) - 48;

                    // Conseguimos la rotacion
                    if (elemento.contains("rotX")) {
                        transformacion[0] = MiLibreria3D.tipoTrans.enX;
                        grados[0] = Float.parseFloat(elemento.substring(elemento.indexOf("rotX") + 4, elemento.indexOf("/", elemento.indexOf("rotX") + 4)));
                    }
                    if (elemento.contains("rotY")) {
                        transformacion[1] = MiLibreria3D.tipoTrans.enY;
                        grados[1] = Float.parseFloat(elemento.substring(elemento.indexOf("rotY") + 4, elemento.indexOf("/", elemento.indexOf("rotY") + 4)));
                    }
                    if (elemento.contains("rotZ")) {
                        transformacion[2] = MiLibreria3D.tipoTrans.enZ;
                        grados[2] = Float.parseFloat(elemento.substring(elemento.indexOf("rotZ") + 4, elemento.indexOf("/", elemento.indexOf("rotZ") + 4)));
                    }

                    // Conseguimos la posicion en caso de qeu se vaya a realizar un posicionamiento manual
                    posManual = null;
                    if (elemento.contains("posX") || elemento.contains("posY") || elemento.contains("posZ")) {
                        posManual = new Float[3];
                        posManual[0] = posManual[1] = posManual[2] = 0.0f;
                        if (elemento.contains("posX")) {
                            posManual[0] = Float.parseFloat(elemento.substring(elemento.indexOf("posX") + 4, elemento.indexOf("/", elemento.indexOf("posX") + 4)));
                        }
                        if (elemento.contains("posY")) {
                            posManual[1] = Float.parseFloat(elemento.substring(elemento.indexOf("posY") + 4, elemento.indexOf("/", elemento.indexOf("posY") + 4)));
                        }
                        if (elemento.contains("posZ")) {
                            posManual[2] = Float.parseFloat(elemento.substring(elemento.indexOf("posZ") + 4, elemento.indexOf("/", elemento.indexOf("posZ") + 4)));
                        }
                    }

                    // Buscamos cual es la posicion de inicio del objeto en el archivo info_obj.txt
                    StringTokenizer str_info = new StringTokenizer(info_obj, "\t");
                    String elemento_info = "";
                    boolean encontradoPosicion = false;
                    while (str_info.hasMoreTokens() && !encontradoPosicion) {
                        elemento_info = str_info.nextToken();
                        if (elemento_info.equalsIgnoreCase(archivo)) {
                            encontradoPosicion = true;
                        }
                    }

                    // Si encontramos los datos que necesitamos para situar el objeto
                    // pasamos al else y sino decimos que algo ha ido mal al intentar
                    // encontrar estos datos en el fichero info_obj.txt
                    if (!encontradoPosicion || carpeta.equalsIgnoreCase(NO_EXISTE) || archivo.equalsIgnoreCase(NO_EXISTE)) {
                        try {
                            throw new IllegalArgumentException("La carpeta/archivo OBJ señalado no existe");
                        } catch (IllegalArgumentException e) {
                            System.err.println("No se encontro la carpeta, archivo o datos de " + elemento);
                        }
                    } else {
                        // Conseguimos el ancho, alto y largo del modelo
                        ancho = Float.parseFloat(str_info.nextToken());
                        alto = Float.parseFloat(str_info.nextToken());
                        largo = Float.parseFloat(str_info.nextToken());

                        // Colcamos el proximo objeto en su posicion
                        // cambiando la X y la Y ya que son las unicas variables
                        // que se ven afectadas al rellenar nuestro mapa de izquierda
                        // a derecha
                        if (esObjetoInicial) {
                            posAnteriorX = ancho * escala;
                            esObjetoInicial = false;
                        } else {
                            posAnteriorX = posSiguienteX + (ancho * escala);
                        }
                        posAnteriorY = alto * escala;

                        Vector3f posicion;
                        // Si el posicionamiento no es manual situamos el objeto en la posicion
                        // siguiente situandolo al lado del anterior objeto
                        if (posManual == null) {
                            posicion = new Vector3f(posAnteriorX, posAnteriorY, (posSiguienteZ + (largo * escala)));
                        } else {
                            posicion = new Vector3f(posManual[0] + (ancho * escala), posManual[1] + (alto * escala), posManual[2] + (largo * escala));
                        }

                        // Lo introducimos dentro del arbol y lo trasladamos al lugar correcto
                        mundoBG.addChild(MiLibreria3D.trasladarEstatico(
                                MiLibreria3D.rotarEstatico(
                                MiLibreria3D.rotarEstatico(
                                MiLibreria3D.rotarEstatico(
                                MiLibreria3D.crear(new Vector3f(0.0f, 0.0f, 0.0f),
                                MiLibreria3D.tipoFigura.objetoOBJ, escala, null, null,
                                null,
                                System.getProperty("user.dir") + "/" + "src/resources/objetosOBJ/" + carpeta + "/" + archivo + ".obj"),
                                grados[0], transformacion[0]),
                                grados[1], transformacion[1]),
                                grados[2], transformacion[2]),
                                posicion));


                        posSiguienteX = posAnteriorX + ancho * escala;
                        transformacion[0] = transformacion[1] = transformacion[2] = null;
                    }
                } else {
                    posSiguienteX = posSiguienteX + ancho * escala;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BigMind.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mundoBG;
    }
}
