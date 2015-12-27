package urban_robot_controller.procedures.object_recognition.local_sign_detection;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import com.atul.JavaOpenCV.Imshow;


public class Videoverarbeitung implements Observer, Runnable{

	private VideoCapture vcam;
	private Mat imageArray;
	private Imshow imageViewer;
	private Thread t;
	private Object dataObject[];


	//private String srvAddress = "http://192.168.11.31:8080";
	private String srvAddress;

	public Videoverarbeitung (int videoDevice, String videoName){

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.vcam = new VideoCapture(videoDevice);
		this.imageViewer = new Imshow(videoName);
		this.imageArray = new Mat();
		
//	 	 vcam.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
//    	 vcam.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);
	}
	
	/*
	* Konstruktor mit IP Raspberry
	*/
	public Videoverarbeitung (String srvAddress, String videoName){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.srvAddress = srvAddress;
		this.imageViewer = new Imshow(videoName);
		this.imageArray = new Mat();
	}
	

	/*
	 * überprüft verfügbarkeit der Kamera/Bildes
	 */
	public void checkCam(){
		while (vcam.isOpened() == false) System.out.println("Fehler!");

		while (this.imageArray.empty()) {
			vcam.retrieve(imageArray);	
		}
	}
	
	/*
	 * Gibt die Auflösung (Höhe und Breite) der angeschlossenen Kamera zurück
	 */
	public double[] getCameraResolution(){
		double resolution[] = new double[2];
		resolution[0] = vcam.get(Highgui.CV_CAP_PROP_FRAME_WIDTH);
		resolution[1] = vcam.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT);
		return resolution;
	}

	public Mat getImageArray(){
		return this.imageArray;
	}

	public void enframeSign(){

		MatOfRect signDetections = (MatOfRect)this.dataObject[2];
		Scalar colour = (Scalar) this.dataObject[3];
		String signType = (String) this.dataObject[0];
		String distance = Integer.toString(((int)((float)this.dataObject[1])));
		for (Rect rect : signDetections.toArray()) {
			Core.rectangle(this.imageArray, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					colour, 5);
			Core.putText(this.imageArray, signType +" in: " + distance + "cm", new Point(rect.x-10,rect.y-10), 2, 0.5, colour);

		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.dataObject=(Object[]) arg1;
		enframeSign();

	}

	public void USBCam (){
		checkCam();
		while(true){

			vcam.retrieve(this.imageArray);
			this.imageViewer.showImage(this.imageArray);
		}
	}

	public void raspbarryCam(){
		URL url;
		try {
			url = new URL(this.srvAddress);
			HttpURLConnection urlconnection;
			urlconnection = (HttpURLConnection) url.openConnection();        
			//		    urlconnection.setDoInput(true);
			int responseCode=((HttpURLConnection)urlconnection).getResponseCode();    

			if ((responseCode == 200)) {
				synchronized(this){
					while(true){
						try {
							InputStream iStream = new BufferedInputStream(urlconnection.getInputStream());
							BufferedImage iBuff = ImageIO.read(iStream);
							if(iBuff!=null){
								byte[] data = ((DataBufferByte) iBuff.getRaster().getDataBuffer()).getData();
								Mat mat = new Mat(iBuff.getHeight(), iBuff.getWidth(), CvType.CV_8UC3);
								mat.put(0, 0, data);
								Mat dst = new Mat();
								Size size = new Size(800,600);
								//Size size = new Size(320, 240);
								Imgproc.resize(mat, dst, size);
								//								MatOfByte mem = new MatOfByte();
								//					            Highgui.imencode(".jpg", mat, mem);
								this.imageArray = dst;
								//					            ByteArrayInputStream in = new ByteArrayInputStream(mem.toArray());
								//							    camview.setImage(new javafx.scene.image.Image(in));
								this.imageViewer.showImage(this.imageArray);
								
								mat.release();

							}
						} catch (Exception e){
							continue;
						}
					}
				}	
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}


	@Override
	public void run() {
		if(srvAddress==null){
			USBCam();
		}
		else {
			raspbarryCam();
		}
	}

	public void start ()
	{
		if (t == null)
		{
			t = new Thread (this);
			t.start ();
		}
	}

}