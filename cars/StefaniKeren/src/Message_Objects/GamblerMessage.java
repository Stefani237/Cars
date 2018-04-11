package Message_Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class GamblerMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Race> raceList;
	private ArrayList<String[]> raceHistory;
	private ArrayList<String[]> carRaceHistory;
	private ArrayList<String[]> newGambles;
	private ArrayList<String[]> newRaces;
	private ArrayList<String[]> gamblesHistory;
	private ArrayList<String[]> allGamblesHistory;
	private ArrayList<String[]> gamblersProfit;
	private ArrayList<String[]> sys_Profit;
	private Gambler gambler;
	private int messageType;
	private double speed;
	private int raceNumber;

	public GamblerMessage(ArrayList<Race> raceList, Gambler gambler, int messageType, double speed, int raceNumber,
			ArrayList<String[]> raceHistory, ArrayList<String[]> carRaceHistory, ArrayList<String[]> gamblesHistory,
			ArrayList<String[]> newGambles, ArrayList<String[]> newRaces, ArrayList<String[]> allGamblesHistory,
			ArrayList<String[]> gamblersProfit, ArrayList<String[]> sys_Profit) {

		this.raceList = raceList;
		this.gambler = gambler;
		this.messageType = messageType;
		this.speed = speed;
		this.raceHistory = raceHistory;
		this.carRaceHistory = carRaceHistory;
		this.newGambles = newGambles;
		this.newRaces = newRaces;
		this.gamblesHistory = gamblesHistory;
		this.allGamblesHistory = allGamblesHistory;
		this.gamblersProfit = gamblersProfit;
		this.sys_Profit = sys_Profit;
	}

	public ArrayList<Race> getRaceList() {
		return raceList;
	}

	public void setRaceList(ArrayList<Race> raceList) {
		this.raceList = raceList;
	}

	public Gambler getGambler() {
		return gambler;
	}

	public void setGambler(Gambler gambler) {
		this.gambler = gambler;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}

	public ArrayList<String[]> getRaceHistory() {
		return raceHistory;
	}

	public ArrayList<String[]> getcarRaceHistory() {
		return carRaceHistory;
	}

	public ArrayList<String[]> getgamblesHistory() {
		return gamblesHistory;
	}

	public ArrayList<String[]> getnewGambles() {
		return newGambles;
	}

	public ArrayList<String[]> getnewRaces() {
		return newRaces;
	}

	public ArrayList<String[]> getallGamblesHistory() {
		return allGamblesHistory;
	}

	public ArrayList<String[]> getGamblersProfit() {
		return gamblersProfit;
	}

	public ArrayList<String[]> getSys_Profit() {
		return sys_Profit;
	}
	
	

}
