package UI;
import UI.CarEvents.eventType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/** 
 * This CarPane class implements information from the carEvents interface and it paints the cars. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class CarPane extends Pane implements CarEvents {
	class ColorEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setColor(car.getColor());
		}
	}

	class RadiusEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setRadius(car.getRadius());
		}
	}

	class SpeedEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setSpeed(car.getSpeed());
		}
	}
	
	class TypeEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setType(car.getType());
		}
	}
	
	class ShapeEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setShape(car.getShape());
		}
	}

	final int MOVE = 1;
	final int STOP = 0;
	private double xCoor;
	private double yCoor;
	private Timeline tl; // speed=setRate()
	private Color color = Color.ALICEBLUE;
	private int r;// radius
	private Car car;
	private String type;
	private String shape;
	private Box myBox;
	private PhongMaterial blackMaterial;
	

	public CarPane() {
		xCoor = 0;
		r = 5;
	}

	public void setCarModel(Car myCar) {
		car = myCar;
		if (car != null) {

			car.addEventHandler(new ColorEvent(), eventType.COLOR);
			car.addEventHandler(new RadiusEvent(), eventType.RADIUS);
			car.addEventHandler(new SpeedEvent(), eventType.SPEED);
			car.addEventHandler(new TypeEvent(), eventType.TYPE);
			car.addEventHandler(new ShapeEvent(), eventType.SHAPE);

		}
	}

	public Car getCarModel() {
		return car;
	}

	/* create timeLine calls to move car and it does the animation */

	public void moveCar(int n) {
		yCoor = getHeight();
		setMinSize(10 * r, 6 * r);
		if (xCoor > getWidth()) {
			xCoor = -10 * r;
		} else {
				xCoor += n;
				
		}
		
		PhongMaterial grayMaterial = new PhongMaterial();
		grayMaterial.setDiffuseColor(Color.DARKGREY);
		grayMaterial.setSpecularColor(Color.GRAY);
		
		PhongMaterial colorMaterial = new PhongMaterial();
		colorMaterial.setDiffuseColor(color.darker());
		colorMaterial.setSpecularColor(color.darker());
		
		Sphere wheel1 = new Sphere(7);
		Sphere wheel2 = new Sphere(7);
		Sphere wheel3 = new Sphere(7);
		Sphere wheel4 = new Sphere(7);
		
		 
		wheel1.setTranslateX(xCoor + 27);
		wheel1.setTranslateY(yCoor-1);
		wheel1.setTranslateZ(1);
		wheel1.setMaterial(grayMaterial);
		
		wheel2.setTranslateX(xCoor+8);
		wheel2.setTranslateY(yCoor-1 );
		wheel2.setTranslateZ(1);
		wheel2.setMaterial(grayMaterial);
		
		wheel3.setTranslateX(xCoor + 10 );
		wheel3.setTranslateY(yCoor-2  );
		wheel3.setTranslateZ(-15);
		wheel3.setMaterial(grayMaterial);
		
		wheel4.setTranslateX(xCoor + 29);
		wheel4.setTranslateY(yCoor -2);
		wheel4.setTranslateZ(-15);
		wheel4.setMaterial(grayMaterial);
		  
		  Cylinder myCylinder = new Cylinder(12,r+8);
		  myCylinder.setTranslateX(xCoor + 20);
		  myCylinder.setTranslateY(yCoor - 17-r-10 );
		  myCylinder.setTranslateZ(-10);
		  myCylinder.setMaterial(colorMaterial);
		  
		  Box myBox = new Box(40+r,r+10, 20);
		  myBox.setTranslateX(xCoor + 20);
		  myBox.setTranslateY(yCoor - 17);
		  myBox.setTranslateZ(-15);
		  myBox.setMaterial(colorMaterial);
		 // Rotate rx3 = new Rotate(0.0, 0.0, 0.0, 0.0, Rotate.X_AXIS);
		 // Rotate ry3 = new Rotate(0.0, 0.0, 0.0, 0.0, Rotate.Y_AXIS);
		//  Rotate rz3 = new Rotate(0.0, 0.0, 0.0, 0.0, Rotate.Z_AXIS);
		 // myBox.getTransforms().addAll(rx3 , ry3 , rz3);
		  
		// Draw the car
		/*Polygon polygon = new Polygon(xCoor, yCoor - r, xCoor, yCoor - 4 * r, xCoor + 2 * r, yCoor - 4 * r,
				xCoor + 4 * r, yCoor - 6 * r, xCoor + 6 * r, yCoor - 6 * r, xCoor + 8 * r, yCoor - 4 * r,
				xCoor + 10 * r, yCoor - 4 * r, xCoor + 10 * r, yCoor - r);
		polygon.setFill(color);
		polygon.setStroke(Color.BLACK);
		// Draw the wheels
		Circle wheel1 = new Circle(xCoor + r * 3, yCoor - r, r, Color.BLACK);
		Circle wheel2 = new Circle(xCoor + r * 7, yCoor - r, r, Color.BLACK);*/
		getChildren().clear();
		getChildren().addAll(myBox,myCylinder,wheel1,wheel4,wheel2,wheel3);
		
		
	}

	public void createTimeline() {
		EventHandler<ActionEvent> eventHandler = e -> {
			moveCar(MOVE); // move car pane according to limits
		};
		tl = new Timeline();
		tl.setCycleCount(1);
		KeyFrame kf = new KeyFrame(Duration.millis(50), eventHandler);
		tl.getKeyFrames().add(kf);
		tl.play();
	}

	public Timeline getTimeline() {
		return tl;
	}

	public void setColor(Color color) {
		this.color = color;
		if (car.getSpeed() == STOP)
			moveCar(STOP);
	}

	public void setRadius(int r) {
		this.r = r;
		if (car.getSpeed() == STOP)
			moveCar(STOP);
	}
	
	public void setType(String type) {
		this.type=type;
		
	}
	
	public void setShape(String shape) {
		this.shape=shape;
		
	}

	public void setSpeed(double speed) {
		if (speed == STOP) {
			if(tl != null)
				tl.stop();
		} else {
			if(tl != null)
			{
				tl.setRate(speed);
				tl.play();
			}
		}
	}

	public double getX() {
		return xCoor;
	}

	public double getY() {
		return yCoor;
	}
}
