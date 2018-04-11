package Message_Objects;
import java.io.Serializable;

public class NewCarDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final double TIME_SECTIOM = 30;    // 30 seconds is 0.008 hours
	private int carId;
	private double speed;
	private double distance ;
	private int colorIndex;
	private int radius;
	private String type;
	private String shape;
	
	
	public NewCarDetails (int carId,double speed, int colorIndex,int radius,String type, String shape,double distance){
		
		this.carId = carId;
		this.speed = speed;
		this.distance = distance;
		this.colorIndex = colorIndex;
		this.radius = radius;
		this.type = type;
		this.shape = shape;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getColorIndex() {
		return colorIndex;
	}

	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
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
	
	

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}
	
	public void setDistamce(double speed){
		
		 this.distance = this.distance+TIME_SECTIOM*speed;
		
	}
	
	public double getDistance(){
		
		return distance;
	}

	@Override
	public String toString() {
		return "speed=" + speed + ", colorIndex=" + colorIndex + ", radius=" + radius + ", type=" + type
				+ ", shape=" + shape ;
	}
	
	

	
}
