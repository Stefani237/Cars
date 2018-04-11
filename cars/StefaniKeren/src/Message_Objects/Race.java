package Message_Objects;
import java.io.Serializable;
import java.util.Arrays;

public class Race implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private final int NUM_OF_CARS = 5;
	private  double betsAmount  ;
	int raceNumber;
	private String status;
	
	private NewCarDetails[] cars = new NewCarDetails[NUM_OF_CARS];

	public Race(NewCarDetails[] cars,int raceNumber,String status) {
		
		this.cars = cars;
		this.raceNumber = raceNumber;
		this.status = status;
		this.betsAmount = 0;
		
		
	}

	public NewCarDetails getCar(int i) {
		return cars[i];
	}

	public void setCar(NewCarDetails car, int i) {
		this.cars[i] = car;
	}

	public int getRaceNumber() {
		return raceNumber;
	}
	
	public void setAmount(double amount){
		
		this.betsAmount = betsAmount+amount;
	}
	
public void setStatus(String status){
		
		this.status= status;
	}

public String getStatus(){
	
	return status;
}
	
	
public double getAmount(){
		
		return betsAmount;
	}
	

	public int getNUM_OF_CARS() {
		return NUM_OF_CARS;
	}

	@Override
	public String toString() {
		return "Race [NUM_OF_CARS=" + NUM_OF_CARS + ", raceNumber=" + raceNumber + ", cars=" + Arrays.toString(cars)
				+ "]";
	}

	
	
	

}
