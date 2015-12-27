package urban_robot_controller.procedures.object_recognition.local_sign_detection;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class Objekterkennung extends Observable implements Runnable {
	
	private Videoverarbeitung video;
	private double focalLength=-1;
	private int objectHeight = 86;  // größe des zu erkennenden Objekten in mm
	private double cameraHeight;
	private double cameraWidth;
	private CascadeClassifier detector;
	private MatOfRect signDetections;
	private Mat imageArray;
	private Thread t;
	private float distance;
	private ArrayList<Observer> observers = new ArrayList<Observer>(); 
	private Object[] dataObject = new Object[4];
	private Scalar colour;
	private String signType;
	
	public Objekterkennung (Videoverarbeitung video, String classifierPath, String signType, Scalar colour){
		super();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.video = video;
		signDetections = new MatOfRect();
		this.detector = new CascadeClassifier(classifierPath);
		this.signType = signType;
		this.colour = colour;
		this.imageArray = new Mat();
		//calculateFocalLengthUSB();
		//calculateFocalLengthRB();

	}
	
	public boolean checkSign(){
		while (this.imageArray.empty()){
			this.imageArray=video.getImageArray();
		}
		this.detector.detectMultiScale(this.imageArray, this.signDetections);
		boolean sign = !(signDetections.empty());
		return sign;		 
	}
	

	/*
	 * Berechnet die FocalLength für die Raspberry Kamera
	 * um die Distanz berechnen zu können
	 */
	public void calculateFocalLengthRB(){	
		this.cameraWidth = video.getImageArray().width();
		this.cameraHeight = video.getImageArray().height();
		
//		this.cameraWidth = video.getCameraResolution()[0]; //für angeschlossene Webcam
//		this.cameraHeight = video.getCameraResolution()[1];
				
		double ratio1 = 240.0 / this.cameraHeight;
		double ratio2 = 320.0 / this.cameraWidth;
		
		this.focalLength =  ( 3488.0 /  ((ratio1 + ratio2) / 2));
	}
	
	/*
	 * gibt die Distanz in Centimeter zurück
	 */
	public void calculateDistance(){
		if(focalLength==-1){// falls FocalLength noch nicht gesetzt wurde
			calculateFocalLengthRB();
		}
		float objectHeightInPixel = 1;
		for (Rect rect : this.signDetections.toArray()) {
			objectHeightInPixel = rect.height;
		}

		if (objectHeightInPixel == 1){
			this.distance = 0;
		}
		else {
			this.distance = (float) ((this.objectHeight * this.focalLength) / objectHeightInPixel); 
		}

		this.distance = this.distance/100;		// in centimeter zurückgeben
	}
	
	public ArrayList<Observer> getObservers() {  
        return observers;  
    }  
    public void setObservers(ArrayList<Observer> observers) {  
        this.observers = observers;  
    }
    
    public void signDetected() {    
            setChanged();
            notifyObservers(this);    
    }  
  
    public void notifyObservers(Observable observable) {
         for (Observer ob : observers) {
             ob.update(observable, this.dataObject);
      }  
  
    }  
  
    public void registerObserver(Observer observer) {  
         observers.add(observer);  
          
    }  
  
    public void removeObserver(Observer observer) {  
         observers.remove(observer);  
          
    } 
	
	
	
	public void start ()
	{
		if (t == null)
		{
			t = new Thread (this);
			t.start ();
		}
	}
	
	@Override
	public void run() {
		while(true){
			this.imageArray = video.getImageArray();
			if(checkSign()){
				calculateDistance();
				this.dataObject[0] = this.signType;
				this.dataObject[1] = this.distance;
				this.dataObject[2] = this.signDetections;
				this.dataObject[3] = this.colour;
				signDetected();
				
			}
		}
		
	}

}