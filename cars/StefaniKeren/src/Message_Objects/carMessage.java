package Message_Objects;

import java.util.ArrayList;

public class carMessage implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int carIndex;
	private int colorIndex;
	private double speed;
	private int raduis;
	private boolean firstTime; //
	private int raceCounter;
	private String type;
	private String shape;
	private ArrayList<String[]> newRaces;
	
	public carMessage(int carIndex,int color,double speed,int raduis,String type,String shape,boolean firstTime,int raceCounter)
	{
		this.carIndex = carIndex;
		this.colorIndex = color;
		this.speed =speed;
		this.raduis = raduis;
		this.type = type;
		this.shape = shape;
		this.firstTime = firstTime;
		this.raceCounter = raceCounter;
		
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

	public int getCarIndex() {
		return carIndex;
	}

	public void setCarIndex(int carIndex) {
		this.carIndex = carIndex;
	}

	public int getColorIndex() {
		return colorIndex;
	}

	public void setColorIndex(int color) {
		this.colorIndex = color;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getRaduis() {
		return raduis;
	}

	public void setRaduis(int raduis) {
		this.raduis = raduis;
	}

	public boolean getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public int getRaceCounter() {
		return raceCounter;
	}
	
	

	

	public void setRaceCounter(int raceCounter) {
		this.raceCounter = raceCounter;
	}
	
	
	
	

}
