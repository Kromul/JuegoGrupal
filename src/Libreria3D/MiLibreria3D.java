package Libreria3D;
import java.awt.Component;
import java.io.File;
import java.net.URL;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BackgroundSound;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.MediaContainer;
import javax.media.j3d.Node;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;


public class MiLibreria3D {
	
	public static enum tipoTransformacion{enX, enY, enZ};
        public static enum tipoFigura{rectangulo, circulo, cilindro, objetoOBJ};
        
        
        
	static tipoTransformacion transformacion;
	
	public static Color3f rojo = new Color3f(1.0f, 0.0f, 0.0f);
	public static Color3f verde = new Color3f(0.0f, 1.0f, 0.0f);
	public static Color3f azul = new Color3f(0.0f, 0.0f, 1.0f);
	public static Color3f amarillo = new Color3f(1.0f, 1.0f, 0.0f);
	public static Color3f cian = new Color3f(0.0f, 1.0f, 1.0f);
	public static Color3f violeta = new Color3f(1.0f, 0.0f, 1.0f);
	
	
	/**
	 * Devuelve la apariencia con el (r,g,b) indicado y el ColoringAttributes
	 * @param r
	 * @param g
	 * @param b
	 * @param fastest
	 * @return
	 */
	public static Appearance crearApariencia(float r, float g, float b, int fastest) {
		// Apariencia del objeto
		ColoringAttributes colores;
		colores = new ColoringAttributes(r, g, b, fastest);
		Appearance apariencia;
		apariencia = new Appearance ();
		apariencia.setColoringAttributes(colores);
		return apariencia;
	}
	
	/**
	 * Crea una apariencia por defecto
	 * @return
	 */
	public static Appearance getDefaultAppearance() {
		// Apariencia del objeto
		ColoringAttributes colores;
		colores = new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.FASTEST);
		Appearance apariencia;
		apariencia = new Appearance ();
		apariencia.setColoringAttributes(colores);
		return apariencia;
	}
	
	/**
	 * Rota el objeto indicado en el eje X,Y,Z que se indica
	 * @param objeto
	 * @param grados
	 * @param tipoRot
	 * @return
	 */
	public static TransformGroup rotar(Node objeto,float grados, tipoTransformacion tipoRot){
		/* Transformacion a realizar sobre la esfera*/
		Transform3D rotacion = new Transform3D();
		//Datos de rotaci�n en X,Y,Z //Math.PI/4.0d); 
		if(tipoRot.equals(tipoTransformacion.enX)){rotacion.rotX(Math.toRadians(grados));}
		if(tipoRot.equals(tipoTransformacion.enY)){rotacion.rotY(Math.toRadians(grados));}
		if(tipoRot.equals(tipoTransformacion.enZ)){rotacion.rotZ(Math.toRadians(grados));}
		// Se asocia al objeto la transformacion
		TransformGroup objetoRotado = new TransformGroup(rotacion);
		objetoRotado.addChild(objeto);	

		return objetoRotado;
	}
	
	/**
	 * Rota el objeto indicado en el eje X,Y,Z que se indica
	 * @param objeto
	 * @param rotX
	 * @param rotY
	 * @param rotZ
	 * @return
	 */
	public static TransformGroup trasladar(Node objeto, Vector3f posicion){
		/* Transformacion a realizar sobre la esfera*/
		Transform3D traslacion = new Transform3D();
		//Datos de rotaci�n en X,Y,Z //Math.PI/4.0d); 
		traslacion.set(posicion);
		// Se asocia al objeto la transformacion
		TransformGroup objetoRotado = new TransformGroup(traslacion);
		objetoRotado.addChild(objeto);	

		return objetoRotado;
	}
	
	/**
	 * Devuelve un DirectionalLight
	 * @return
	 */
	public static DirectionalLight getDefaultIlumination(){
		DirectionalLight LuzDireccional = new DirectionalLight(new Color3f (1f, 1f, 1f), new Vector3f (1f, 0f, -1f));
		BoundingSphere limites= new BoundingSphere(new Point3d(-5 , 0, 5), 100.0); //Localizacion de fuente/paso de luz
		LuzDireccional.setInfluencingBounds (limites);
		return LuzDireccional;
	}

	/** Devuelve un Appearance aplicandole la caracteristica de material para lo cual
	 * hay que aplicar tambien luz direccional
	 * @return
	 */
	public static Appearance getDefaultMaterial(){
		Appearance apariencia = new Appearance();
		Color3f unColor = new Color3f(1.0f, 0.1f, 0.1f); //Crea un color rojizo para la esfera
		//Es necesario a�adir luz direccional.
		//Para m�s informaci�n, buscar : Understanding Lighting in Java 3D
		Material mat = new Material();
		mat.setDiffuseColor(unColor); //color cuando hay luz direccional
		mat.setSpecularColor(new Color3f (1f, 1f, 0f)); //color amarillo para el reflejo de luz
		mat.setShininess(128f); //brillo m�ximo de reflejo de luz
		apariencia.setMaterial(mat);
		
		return apariencia;
	}
	
	/** Devuelve un Appearance aplicandole la caracteristica de material para lo cual
	 * hay que aplicar tambien luz direccional con el (r,g,b) indicado
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static Appearance getDefaultMaterial(float r, float g, float b){
		Appearance apariencia = new Appearance();
		Color3f unColor = new Color3f(r,g,b); //Crea un color rojizo para la esfera
		//Es necesario a�adir luz direccional.
		//Para m�s informaci�n, buscar : Understanding Lighting in Java 3D
		Material mat = new Material();
		mat.setDiffuseColor(unColor); //color cuando hay luz direccional
		mat.setSpecularColor(new Color3f (1f, 1f, 0f)); //color amarillo para el reflejo de luz
		mat.setShininess(128f); //brillo m�ximo de reflejo de luz
		apariencia.setMaterial(mat);
		
		return apariencia;
	}
	
	/** Devuelve la ruta de la clase que se pasa como argumento
	 * @param clase
	 * @return
	 */
	public static String getRutaProyecto(Class clase){
		URL ruta = clase.getProtectionDomain().getCodeSource().getLocation(); // traigo dirreccion
		return ruta.toString();
	}
	
	/**
	 * Establece un comportamiendo para el objeto girando con respecto al eje y
	 * @param objTrans
	 * @return
	 */
	public static RotationInterpolator rotacion(TransformGroup objTrans) {
		// Crear un objeto Behavior que realizar� las operaciones
		// deseadas en la transformada especificada y a�adirlo a la escena
		Transform3D yAxis = new Transform3D();
		Alpha rotationAlpha = new Alpha(2, 3000);// si se pone -1 en el primer argumento gira indefinidamente
		RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI * 2.0f);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		rotator.setSchedulingBounds(bounds);
		
		return rotator;
	}
	
	/**
	 *  Retorna un nodo con los ejes de coordenadas situadas en el origen
	 * @return
	 */
	public static void CrearEjesCoordenada(BranchGroup rootBG) {
		  LineArray linea = new LineArray(6, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
		  float alcance = 100.0f;
		  // Eje x
		  linea.setCoordinate(0, new Point3f(-alcance, 0.0f, 0.0f));
		  linea.setCoordinate(1, new Point3f(alcance, 0.0f, 0.0f));
		  // Eje y
		  linea.setCoordinate(2, new Point3f(0.0f, -alcance, 0.0f));
		  linea.setCoordinate(3, new Point3f(0.0f, alcance, 0.0f));
		  // Eje z
		  linea.setCoordinate(4, new Point3f(0.0f, 0.0f, -alcance));
		  linea.setCoordinate(5, new Point3f(0.0f, 0.0f, alcance));
		  
		  linea.setColor(0, new Color3f(1.0f, 0.0f, 0.0f)); // Rojo
		  linea.setColor(1, new Color3f(1.0f, 1.0f, 0.0f)); // Amarillo
		  linea.setColor(2, new Color3f(0.0f, 1.0f, 0.0f)); // Verde
		  linea.setColor(3, new Color3f(0.0f, 1.0f, 1.0f)); // Ci�n
		  linea.setColor(4, new Color3f(0.0f, 0.0f, 1.0f)); // Azul
		  linea.setColor(5, new Color3f(1.0f, 0.0f, 1.0f)); // Violeta
		 
		  Shape3D forma = new Shape3D(linea);
		  rootBG.addChild(forma);
	}
	
	/**
	 * Dado un BranchGroup bajo el cual se encuentran los objetos que se quieren escalar
	 * este metodo devolvera un BranchGroup el cual cubre a al BG inicial y, evidentemente,
	 * a todos los objetos que este tenga por debajo suya escalandolos el valor indicado
	 * @param objRoot
	 * @return
	 */
	public static BranchGroup escalar(BranchGroup objRoot, double escala) {
		BranchGroup escaladoBG = new BranchGroup();
		Transform3D escalado = new Transform3D();
		escalado.setScale(escala);
		TransformGroup transform = new TransformGroup(escalado);
		transform.addChild(objRoot);
		escaladoBG.addChild(transform);
		return escaladoBG;
	}

	/**
	 * Permite que podamos mover la camara en el universo en el que nos encontramos
	 * @param universo
	 * @param zonaDibujo
	 */
	public static void addMovimientoCamara(SimpleUniverse universo, Canvas3D zonaDibujo) {
		// A�adimos el codigo para poder rotar nosotros mismos el objeto
		OrbitBehavior B = new OrbitBehavior(zonaDibujo);
		B.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0) );
		universo.getViewingPlatform().setViewPlatformBehavior(B);
	}
	
	/**
	 * Coloca la camara existente del universo en la posicion indicado y mirando hacia la posicion
	 * objetivo indicada
	 * @param universo
	 * @param posici�nCamara
	 * @param objetivoCamara
	 */
	public static void colocarCamara (SimpleUniverse universo, Point3d posicioonCamara, Point3d objetivoCamara){
		Point3d posicionCamara = new Point3d(posicioonCamara.x +0.001, posicioonCamara.y+0.001d, posicioonCamara.z+0.001);
		Transform3D datosConfiguracionCamara = new Transform3D();
		datosConfiguracionCamara.lookAt ( posicionCamara, objetivoCamara, new Vector3d (0.001, 1.001, 0.001));
		try{ datosConfiguracionCamara.invert();
		TransformGroup TGcamara = universo.getViewingPlatform().getViewPlatformTransform();
		TGcamara.setTransform ( datosConfiguracionCamara );
		} catch (Exception e){System.out.println(e.toString());}
	}
	
	/**
	 * Crea un suelo partiendo de la posicion (0,0,0)
	 * @param rootBG
	 */
	public static void CrearSuelo(BranchGroup rootBG) {
		int contadorTira[] = {3,3,3,3};
		TriangleStripArray cuadrado=new TriangleStripArray(12,TriangleArray.COORDINATES|TriangleArray.COLOR_3, contadorTira);
		float limite = 100.0f;
		float posicionSuelo = -0.0f;
		// Primera tira
		cuadrado.setCoordinate(0, new Point3f(-limite, posicionSuelo, 0.0f));
		cuadrado.setCoordinate(1, new Point3f(0.0f,posicionSuelo, -limite));
		cuadrado.setCoordinate(2, new Point3f(limite, posicionSuelo, 0.0f));

		cuadrado.setCoordinate(3, new Point3f(limite, posicionSuelo, 0.0f));
		cuadrado.setCoordinate(4, new Point3f(0.0f,posicionSuelo, limite));
		cuadrado.setCoordinate(5, new Point3f(-limite, posicionSuelo, 0.0f));
		
		cuadrado.setCoordinate(6, new Point3f(limite, posicionSuelo, 0.0f));
		cuadrado.setCoordinate(7, new Point3f(0.0f,posicionSuelo, -limite));
		cuadrado.setCoordinate(8, new Point3f(-limite, posicionSuelo, 0.0f));

		cuadrado.setCoordinate(9, new Point3f(-limite, posicionSuelo, 0.0f));
		cuadrado.setCoordinate(10, new Point3f(0.0f,posicionSuelo, limite));
		cuadrado.setCoordinate(11, new Point3f(limite, posicionSuelo, 0.0f));
		
		float r = 58/255f;
		float g = 173/255f;
		float b = 167/255f;
		Color3f verde = new Color3f(r, g, b); // verde
		cuadrado.setColor(0, verde); cuadrado.setColor(1, verde);
		cuadrado.setColor(2, verde);
		cuadrado.setColor(3, verde);  cuadrado.setColor(4, verde);
		cuadrado.setColor(5, verde);
		
		cuadrado.setColor(6, verde); cuadrado.setColor(7, verde);
		cuadrado.setColor(8, verde);
		cuadrado.setColor(9, verde);  cuadrado.setColor(10, verde);
		cuadrado.setColor(11, verde);
		
		Shape3D forma = new Shape3D(cuadrado);

		rootBG.addChild(forma);
	}

	/**
	 * Consigue la apariencia de la imagen o textura que se indique
	 * en el parametro de entrada URL
	 * @param url
	 * @return
	 */
	public static Appearance getTexture(String url, Component contexto) {
		Texture tex = new TextureLoader(url, contexto).getTexture();
		Appearance apariencia = new Appearance();
		apariencia.setTexture(tex);
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		apariencia.setTextureAttributes(texAttr);
		return apariencia;
	}
	
	/**
	 * Crea una caja con el ancho, alto, largo y apariencia especificada
	 * y lo situa su parte superior en el punto (0,0,0)
	 * @param apariencia
	 * @param ancho
	 * @param alto
	 * @param largo
	 * @return
	 */
	public static TransformGroup crearCaja(Appearance apariencia, float ancho, float alto, float largo) {		
		Box objeto = new Box (ancho, alto, largo, Box.GENERATE_TEXTURE_COORDS, apariencia);
		
		TransformGroup objetoTrasladado = MiLibreria3D.trasladar(objeto, new Vector3f(0.0f, -2*(alto), 0.0f));
		return objetoTrasladado;
	}
	
	
	public static void setBackground(BranchGroup rootBG, String url, Component context, int escala) {
		TextureLoader bgTexture = new TextureLoader(url, context);
		Background bg = new Background(bgTexture.getImage());
		BoundingSphere limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		bg.setApplicationBounds(limites);
		bg.setImageScaleMode(escala);
		BranchGroup backGeoBranch = new BranchGroup();
		bg.setGeometry(backGeoBranch);
		rootBG.addChild(bg);
	}

	public static void addSound(SimpleUniverse universo, BranchGroup rootBG, String ruta) {
		// Configuramos el universo para que pueda ejecutar sonido
		configurarUniverso(universo);
		
		// Le indicamos a la rama cual es el sonido a ejecutar
		anadirSonidoARama(rootBG, ruta);
	}
	
	private static void configurarUniverso(SimpleUniverse universo) {
		try{
			PhysicalEnvironment pe = universo.getViewer().getPhysicalEnvironment();
			JavaSoundMixer objetoMezcladorSonidos = new JavaSoundMixer(pe);
			pe.setAudioDevice(objetoMezcladorSonidos);
			objetoMezcladorSonidos.initialize();
			universo.getViewer().getView().setPhysicalEnvironment(pe);
		}catch (Exception e){System.out.println("problema de audio");}
	}

	private static void anadirSonidoARama (BranchGroup b,String soundFile) {
		//Create a media container to load the file
		MediaContainer droneContainer = new MediaContainer(soundFile);
		//Create the background sound from the media container
		BackgroundSound drone= new BackgroundSound(droneContainer, 1.0f);
		//Activate the sound
		Bounds limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0); 
		drone.setSchedulingBounds(limites);
		drone.setEnable(true);
		//Set the sound to loop forever
		drone.setLoop(BackgroundSound.INFINITE_LOOPS);
		b.addChild(drone);
	}

        public static BranchGroup crear(Vector3f posInicial, tipoFigura tipoFigura, float ancho, float alto, float largo, Appearance apariencia) throws Exception{
            BranchGroup rootBG = new BranchGroup();
            
            if(tipoFigura.equals(tipoFigura.rectangulo)){
                // Objeto sobre el que se realiza la transformacion
		Box cubo = new Box(ancho, alto, largo, apariencia);
                TransformGroup traslacion = MiLibreria3D.trasladar(cubo, posInicial);
                rootBG.addChild(traslacion);
            }else{
                throw new Exception("Error al crear la figura");
            }
            
            return rootBG;
        }

}
