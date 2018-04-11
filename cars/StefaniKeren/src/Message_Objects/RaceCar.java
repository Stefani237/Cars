package Message_Objects;
import javafx.scene.paint.Color;

public class RaceCar
{

	private int raceNumber;
	private double speed;
	private String color;
	private int radius;
	private String type;
	private String shape;
	
	public RaceCar(int raceNumber,double speed, String color,int radius,String type, String shape){
		
		this.raceNumber = raceNumber;
		this.speed = speed;
		this.color = color;
		this.radius = radius;
		this.type = type;
		this.shape = shape;
	}

	public int getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}

/*	public int getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(int carNumber) {
		this.carNumber = carNumber;
	}*/

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	
	
	
	
	
	
	
}

