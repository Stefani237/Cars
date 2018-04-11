package Message_Objects;
import java.io.Serializable;
import java.util.ArrayList;

public class Gamble implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String personId;
	private int raceId;
	private ArrayList<Integer> selectedCars;
	private int transferCar;
	private double sumOfMoney;
	private double profit;
	
	public Gamble (String personId,int raceId,ArrayList<Integer> selectedCars,double sumOfMoney,double profit){
		
      
      this.personId = personId;
      this.raceId = raceId;
      this.selectedCars = selectedCars;
      this.sumOfMoney = sumOfMoney;
      this.profit = profit;
      this.transferCar =0 ;
      
}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	

	public double getSumOfMoney() {
		return sumOfMoney;
	}

	public void setSumOfMoney(double sumOfMoney) {
		this.sumOfMoney = sumOfMoney;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public ArrayList<Integer> getSelectedCars() {
		return selectedCars;
	}

	public void setSelectedCars(ArrayList<Integer> selectedCars) {
		this.selectedCars = selectedCars;
	}
	
	public void setTransferedCars(int car) {
		this.transferCar = car;
	}
	
	public int getTransferedCars() {
		return transferCar;
	}

	@Override
	public String toString() {
		
		String str = " ";
		
		str = str + "personId= " + personId + ", raceId=" + raceId + " sumOfMoney=" + sumOfMoney + ", profit=" + profit + " \n";
		
		str = str + " Array : \n";
		
		for(int i=0; i<selectedCars.size();i++){
			
			str = str + " " + selectedCars.get(i) + "\n";
		}
		
		return str;
		
	}

	
	
	
	
}
