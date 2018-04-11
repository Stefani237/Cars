package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import Message_Objects.Gamble;
import Message_Objects.Gambler;
import Message_Objects.GamblerMessage;
import Message_Objects.NewCarDetails;
import Message_Objects.Race;
import Message_Objects.carMessage;

/**
 * This model class presents the server. It manages the memory actions: gets
 * information from the dataBase and sends it to the clients. In addition, it
 * updates and inserts information to the database.
 * 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class Model {
	private final int SIGN_IN = 1;
	private final int GAMBLER_MESSAGE = 0;
	
	private final int SUCCESS_MESSAGE = 1;
	private final int FAILED_MESSAGE = 0;

	private final int NUM_OF_CARS = 5;

	// RADIUS SIZE:
	private final int MINI = 5;
	private final int REGULAR = 10;
	private final int LARGE = 15;

	// CAR TYPES:
	private int[] radius = { MINI, REGULAR, LARGE };
	private String[] type = { "AUDI", "JAGUAR", "BMW", "MERCEDES", "PORCHE", "MASERATI", "LEXUS", "CADILLAC" };
	private String[] shapes = { "SPORT", "JET", "CABRIOLET" };
	private String[] songs = { "1.mp3", "2.mp3", "3.mp3" };
	private String start_sound = "4.mp3";
	private String end_sound = "5.mp3";

	private final int SPEED_CYCLE_COUNT = 4;

	private double betsAmount;

	private ServerSocket serverSocketRace;
	private Statement stmt;
	private boolean firstConnection = true;
	private ArrayList<Race> avaliableRaces;

	private ArrayList<String[]> raceHistory = new ArrayList<String[]>();
	private ArrayList<String[]> queryData;
	private ArrayList<String[]> newRaces = new ArrayList<String[]>();
	private ArrayList<String[]> carRaceHistory = new ArrayList<String[]>();
	private ArrayList<String[]> newGambles = new ArrayList<String[]>();
	private ArrayList<String[]> gamblesHistory = new ArrayList<String[]>();
	private ArrayList<String[]> allGamblesHistory = new ArrayList<String[]>();
	private ArrayList<String[]> gamblersProfit = new ArrayList<String[]>();
	private ArrayList<String[]> sys_Profit = new ArrayList<String[]>();
	private ArrayList<String[]> carOpenRaces;

	private static int raceOn = 1; // holds the race number of the race that is
									// about to start

	private final int GAMBLER_REQUEST = 0;
	private final int RACE_REQUEST = 1;
	private final String URL = "C:/Users/Keren/workspace/FastAndFerious/src";

	private static Lock lock = new ReentrantLock(); // Create a lock
	private static Lock lock2 = new ReentrantLock(); // Create a lock

	private CarLog log;
	private int nextRace;
	private static int gambleId;
	private Connection connection;
	private HashMap<Integer, Stream> hmap = new HashMap<Integer, Stream>();

	public CarLog getLog() {
		return log;
	}

	/* the server will send details to the controller and will update it */

	/*
	 * the server will need to check if the controller is from the gambler type
	 * or from the race type
	 */

	private static int raceCounter = 0; // counting the amount of races

	private Socket socketRace;
	private Socket socketGambler;
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
			Color.VIOLET, Color.GRAY, Color.CORAL };
	private String colorNames[] = { "RED", "AQUA", "BLUE", "GREEN", "YELLOW", "ORANGE", "PINK", "VIOLET", "GRAY",
			"CORAL" };

	public Model() {

		this.log = new CarLog();

		new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {

				/** cannot close the system till all races are on **/

				Platform.exit();
				System.exit(0);
			}
		};

		new Thread(() -> {

			try {

				serverSocketRace = new ServerSocket(8000);

				// ImportMySQLDB init = new ImportMySQLDB();
				// init.clear();
				initializeDB();
				initGamblesTable();
				initCarOpenRacesTable();
				initOpenRacesTable();

				// gambleId = getGambleId();

				// updateGambleStatus();

				while (true) {
					socketRace = serverSocketRace.accept();

					System.out.println("server accepted socket");

					new Thread(new HandleAClient(socketRace)).start();
				}

			}

			catch (SocketException ex) {
				try {
					serverSocketRace.close();
				} catch (IOException e) {
				}

			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}).start();

	}

	private void initializeDB() {
		try { // Load the JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver loaded");
			// Establish a connection
			connection = DriverManager.getConnection("jdbc:mysql://localhost/fastAndFurious?useSSL=false", "scott",
					"tiger");
			System.out.println("Database connected");
			// Create a statement
			stmt = connection.createStatement();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initOpenRacesTable() {

		String queary = "delete from OpenRaces";
		try {
			stmt.execute(queary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initCarOpenRacesTable() {

		String queary = "delete from CarOpenRaces";
		try {
			stmt.execute(queary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initGamblesTable() {

		String queary = "delete from Gamble";
		try {
			stmt.execute(queary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** this function will find if Person excists in the Person table **/

	private Gambler findGambler(String id, int windowType) {

		Gambler gambler = null;

		String searchQuery = "select * from Person" + " where Person.personId = '" + id + "'";

		try {
			ResultSet resultSet = stmt.executeQuery(searchQuery);

			/*
			 * int columnsCount = resultSet.getMetaData().getColumnCount();
			 * for(int i=0; i<columnsCount; i++) { gambler.setId(id); }
			 */

			while (resultSet.next()) {
				gambler = new Gambler(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), windowType);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gambler;
	}

	/** returns all gambles from specific race **/

	private ArrayList<Gamble> getRaceGambles(int raceId, ArrayList<Gamble> raceGambles) {

		Gamble gamble = null;
		int car;

		String searchQuery = "select * from Gamble" + " where Gamble.raceId = '" + raceId + "'";

		try {
			ResultSet resultSet = stmt.executeQuery(searchQuery);

			/*
			 * int columnsCount = resultSet.getMetaData().getColumnCount();
			 * for(int i=0; i<columnsCount; i++) { gambler.setId(id); }
			 */

			while (resultSet.next()) {
				car = resultSet.getInt(3);
				gamble = new Gamble(resultSet.getString(1), resultSet.getInt(2), null, resultSet.getDouble(4),
						resultSet.getDouble(5));

				gamble.setTransferedCars(car);
				raceGambles.add(gamble);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return raceGambles;
	}

	private void InsertManagers() {

		try {

			String quaryMenager1 = "INSERT INTO Person (personId,personName,personPassword,cardNumber,userPermissions) VALUES ('"
					+ "111111111" + "','" + "Keren" + "','" + "keren1234" + "','" + "123456" + "','" + "M" + "')";

			stmt.execute(quaryMenager1);

			String quaryMenager2 = "INSERT INTO Person (personId,personName,personPassword,cardNumber,userPermissions) VALUES ('"
					+ "222222222" + "','" + "Stefani" + "','" + "stefani1234" + "','" + "654321" + "','" + "M" + "')";

			stmt.execute(quaryMenager2);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void InsertClients(Gambler gambler) {

		try {

			String quary = "INSERT INTO Person (personId,personName,personPassword,cardNumber,userPermissions) VALUES ('"
					+ gambler.getId() + "','" + gambler.getName() + "','" + gambler.getPassword() + "','"
					+ gambler.getCardNumber() + "','" + gambler.getPermissionType() + "')";

			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void InsertCar(NewCarDetails car, int raceNumber, int index, String startTime, boolean isOpen) {

		String quary = " ";

		try {

			if (isOpen == false) {
				quary = "INSERT INTO CarRacesHistory (carId,raceId,startTime,carType,carColor,carSize,carShape) VALUES ('"
						+ index + "','" + raceNumber + "','" + startTime + "','" + car.getType() + "','"
						+ colorNames[car.getColorIndex()] + "','" + car.getRadius() + "','" + car.getShape() + "')";

			}

			else if (isOpen == true) {

				quary = "INSERT INTO CarOpenRaces (carId,raceId,carType,carColor,carSize,carShape) VALUES ('" + index
						+ "','" + raceNumber + "','" + car.getType() + "','" + colorNames[car.getColorIndex()] + "','"
						+ car.getRadius() + "','" + car.getShape() + "')";

			}

			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void InsertOpenRace(int raceNumber) {

		try {

			int results = 0;

			String quary = "INSERT INTO OpenRaces (raceId,startTime,betsAmount,raceStatus,raceComments) VALUES ('"
					+ raceNumber + "','" + "0000-00-00 00:00:00" + "','" + betsAmount + "','" + "OPEN" + "','"
					+ "Not enough bets " + "')";

			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void InsertTerminateRace(int raceNumber, String StartTime, String EndTime, int results, double betsAmount) {

		try {

			// int results = 0;

			String quary = "INSERT INTO RacesHistory (raceId, startTime, endTime, results, betsAmount) VALUES ('"
					+ raceNumber + "','" + StartTime + "','" + EndTime + "','" + results + "','" + betsAmount + "')";

			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * private void InsertRace(int raceNumber) {
	 * 
	 * try {
	 * 
	 * int results = 0;
	 * 
	 * String quary =
	 * "INSERT INTO Race (raceId,startTime,endTime,results,betsAmount,raceStatus,raceComments) VALUES ('"
	 * + raceNumber + "','" + "0000-00-00 00:00:00" + "','" +
	 * "0000-00-00 00:00:00" + "','" + results + "','" + betsAmount + "','" +
	 * "OPEN" + "','" + "Not enough bets " + "')";
	 * 
	 * stmt.execute(quary);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); } }
	 */

	private void InsertGambles(Gamble gamble) {

		try {

			for (int i = 0; i < gamble.getSelectedCars().size(); i++) {
				String quary = "INSERT INTO gamble (personId,raceId,carId,sumOfMoney,profit) VALUES ('"
						+ gamble.getPersonId() + "','" + gamble.getRaceId() + "','" + gamble.getSelectedCars().get(i)
						+ "','" + gamble.getSumOfMoney() + "','" + gamble.getProfit() + "')";

				stmt.execute(quary);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void InsertGamblesHistory(String startTime, double totalSum, ArrayList<Gamble> raceGambles) { // change
																											// it
																											// to
																											// be
																											// with
																											// batch
																											// --
																											// array
																											// of
																											// gambles

		try {

			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fastAndFurious?useSSL=false", "scott",
					"tiger");

			if (conn != null) {
				try {

					// Create statement object
					Statement statement = conn.createStatement();

					// Set auto-commit to false
					conn.setAutoCommit(false);

					for (int i = 0; i < raceGambles.size(); i++) {
						Gamble gamble = raceGambles.get(i);
						String quary = "insert into GamblesHistory(raceStartTime ,personId , raceId , carId , sumOfMoney , profit ,totalSumOnWinningCar ) values('"
								+ startTime + "','" + gamble.getPersonId() + "','" + gamble.getRaceId() + "','"
								+ gamble.getTransferedCars() + "','" + gamble.getSumOfMoney() + "','"
								+ gamble.getProfit() + "','" + totalSum + "')";
						statement.addBatch(quary);
					}

					statement.executeBatch();
					conn.commit();
					statement.close();
					conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void deleteCars(int raceNumber) {

		String queary = "delete from CarOpenRaces where raceId = '" + raceNumber + "'";
		try {
			stmt.execute(queary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	private double getBetsSumOnCar(int raceNumber, int carId) {

		double sum = 0;

		String query = "SELECT SUM(gamble.sumOfMoney+gamble.profit) AS totalBetsSum from gamble where gamble.raceId = '"
				+ raceNumber + "' and gamble.carId = '" + carId + "'";
		try {
			ResultSet resultSet = stmt.executeQuery(query);
			if (resultSet.next() == true) {
				sum = resultSet.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sum;
	}

	private void getNewReadyRace() {

		int readyRace = 0;

		// String getRaceQuery = "SELECT COUNT(Gamble.carId) AS NumOfCars,
		// Gamble.raceId FROM Gamble GROUP BY Gamble.raceID";

		String getRaceQuery = "SELECT COUNT(a.carId) AS NumOfCars, a.raceId FROM(SELECT DISTINCT gamble.raceId, gamble.carId from gamble) AS A GROUP BY a.raceID";

		try {

			ResultSet resultSet1 = stmt.executeQuery(getRaceQuery);

			if (resultSet1 == null) {
				readyRace = 0;
			}

			else {
				while (resultSet1.next()) {

					if (resultSet1.getInt(1) >= 3) {

						for (int i = 0; i < avaliableRaces.size(); i++) {
							readyRace = resultSet1.getInt(2);
							if (avaliableRaces.get(i).getRaceNumber() == readyRace) {

								avaliableRaces.get(i).setStatus("READY");

							}
						}
					}

				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return readyRace;
	}

	

	private int getNextRace() {

		int nextRace = 0;
		double amount = 0;

		for (int i = 0; i < avaliableRaces.size(); i++) {

			if (avaliableRaces.get(i).getStatus() == "READY" && avaliableRaces.get(i).getAmount() > amount) {

				nextRace = avaliableRaces.get(i).getRaceNumber();
				amount = avaliableRaces.get(i).getAmount();

			}

		}

		return nextRace;
	}

	
	private ArrayList<String[]> getQueryData(String query) {

		queryData = new ArrayList<String[]>();

		try {
			ResultSet rs = stmt.executeQuery(query);
			int columnsCount = rs.getMetaData().getColumnCount();

			/** insert columns labels **/

			String[] items = new String[columnsCount];

			for (int i = 1; i <= columnsCount; i++) {

				items[i - 1] = rs.getMetaData().getColumnLabel(i);
			}

			queryData.add(items);

			while (rs.next()) {
				String[] dataItems = new String[columnsCount];
				for (int i = 1; i <= columnsCount; i++) {

					dataItems[i - 1] = rs.getString(i);

				}

				queryData.add(dataItems);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return queryData;
	}

	private String showRaceHistory() {

		String HistoryQuery = "select CarRacesHistory.*, raceshistory.endTime , raceshistory.results, raceshistory.betsAmount,gamble.personId "
				+ "from CarRacesHistory,raceshistory,gamble "
				+ "where  raceshistory.raceId = gamble.raceId and CarRacesHistory.carId = gamble.carId and CarRacesHistory.raceId = raceshistory.raceId "
				+ "order by CarRacesHistory.startTime desc";

		return HistoryQuery;

	}

	private String getRaceHistoryCars() {

		String query = "SELECT raceshistory.*, carraceshistory.carId,carraceshistory.carColor, carraceshistory.carShape, carraceshistory.carType "
				+ "from raceshistory,carraceshistory " + "where raceshistory.raceId = carraceshistory.raceId";

		return query;
	}

	private String getGmblesNewBets(String gambler_id) {

		String query = "select gamble.raceId , gamble.carId, gamble.sumOfMoney as remaining_amount, (gamble. sumOfMoney+gamble.profit) as bet_amount, caropenraces.carColor, caropenraces.carShape, caropenraces.carSize, caropenraces.carType "
				+ "from gamble,caropenraces where gamble.personId ='" + gambler_id
				+ "' and gamble.raceId = caropenraces.raceId and gamble.carId = caropenraces.carId";

		return query;
	}

	private String getRacesCurrentState() {

		String query = "select openraces.raceId, openraces.raceStatus,openraces.betsAmount , openraces.raceComments , caropenraces.carId, caropenraces.carColor,caropenraces.carSize, caropenraces.carShape, caropenraces.carType "
				+ "from openraces, caropenraces "
				+ "where openraces.raceId = caropenraces.raceId and (openraces.raceStatus = 'OPEN' OR openraces.raceStatus = 'READY') order by openraces.raceId";

		return query;
	}

	private String getGamblesHistory(String gambler_id) {

		String query = "SELECT person.personId, person.personName, GamblesHistory.raceId, raceshistory.startTime, raceshistory.endTime,gambleshistory.carId,GamblesHistory.sumOfMoney, "
				+ "if(GamblesHistory.carId = raceshistory.results, (((GamblesHistory.sumOfMoney+GamblesHistory.profit)*raceshistory.betsAmount)/(GamblesHistory.totalSumOnWinningCar)),0) as gamblerProfit "
				+ "from person,GamblesHistory,raceshistory "
				+ "where GamblesHistory.raceId = raceshistory.raceId and person.personId = GamblesHistory.personId and gambleshistory.personId = '"
				+ gambler_id + "'" + "order by raceshistory.startTime";

		return query;
	}

	private String getAllGamblesHistory() {

		String query = "SELECT person.personId, person.personName, GamblesHistory.raceId, raceshistory.startTime, raceshistory.endTime,gambleshistory.carId,GamblesHistory.sumOfMoney,"
				+ "if(GamblesHistory.carId = raceshistory.results, (((GamblesHistory.sumOfMoney+GamblesHistory.profit)*raceshistory.betsAmount)/(GamblesHistory.totalSumOnWinningCar)),0) "
				+ "as gamblerProfit from person,GamblesHistory,raceshistory "
				+ "where GamblesHistory.raceId = raceshistory.raceId and person.personId = GamblesHistory.personId order by raceshistory.startTime";

		return query;
	}

	private String getGamblersProfit() {

		String query = "select a.personId, a.personName,sum(a.gamblerProfit) as totalProfit "
				+ "from (SELECT person.personId, person.personName, GamblesHistory.raceId, raceshistory.startTime, raceshistory.endTime,gambleshistory.carId,GamblesHistory.sumOfMoney, "
				+ "if(GamblesHistory.carId = raceshistory.results, (((GamblesHistory.sumOfMoney+GamblesHistory.profit)*raceshistory.betsAmount)/(GamblesHistory.totalSumOnWinningCar)),0) "
				+ "as gamblerProfit from person,GamblesHistory,raceshistory where "
				+ "GamblesHistory.raceId = raceshistory.raceId and person.personId = GamblesHistory.personId order by raceshistory.startTime) as a group by a.personId";

		return query;
	}

	private String getSys_Profit() {

		String query = "SELECT a.raceId, a.startTime, a.endTime, sum(raceshistory.betsAmount+gambleshistory.profit-a.gamblerProfit) as sys_profit "
				+ "from(SELECT person.personId, person.personName, GamblesHistory.raceId, raceshistory.startTime, raceshistory.endTime,gambleshistory.carId,GamblesHistory.sumOfMoney, "
				+ "if(GamblesHistory.carId = raceshistory.results, (((GamblesHistory.sumOfMoney+GamblesHistory.profit)*raceshistory.betsAmount)/(GamblesHistory.totalSumOnWinningCar)),0) as gamblerProfit "
				+ "from person,GamblesHistory,raceshistory where GamblesHistory.raceId = raceshistory.raceId and person.personId = GamblesHistory.personId order by raceshistory.startTime) as a, raceshistory, gambleshistory "
				+ "group by a.startTime";

		return query;
	}

	private void DeleteGamble(int raceId) {

		try {

			String quary = "DELETE FROM Gamble WHERE raceId = '" + raceId + "'";
			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void DeleteRace(int raceId) {

		try {

			String quary = "DELETE FROM openraces WHERE raceId = '" + raceId + "'";
			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void updateRaceAmount(int raceId, double betsAmount) {

		String quary = "UPDATE OpenRaces SET betsAmount = '" + betsAmount + "'  WHERE raceId = '" + raceId + "'";

		try {

			stmt.execute(quary);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateRace(int raceId, String status, double betsAmount, String time, String comment) {

		String quary;

		try {

			if (status == "ON") {

				/*
				 * String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				 * .format(Calendar.getInstance().getTime());
				 */
				quary = "UPDATE OpenRaces SET startTime = '" + time + "' , betsAmount = '" + betsAmount
						+ "' , raceStatus ='" + status + "' ,raceComments = '" + comment + "'  WHERE raceId = '"
						+ raceId + "'";
			}

			else { // READY

				quary = "UPDATE OpenRaces SET betsAmount = '" + betsAmount + "' , raceStatus ='" + status
						+ "' ,raceComments = '" + comment + "'  WHERE raceId = '" + raceId + "'";
			}

			stmt.execute(quary);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void updateGambleStatus() {

		String quary;

		try {

			
			if (gambleId != 0) {
				quary = "UPDATE gamble SET raceStatus = 'prformed' ";

				stmt.execute(quary);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static synchronized void increaseCount() {
		raceCounter++;
	}

	public static synchronized int increaseRaceOn() {
		return raceOn++;
	}

	public static synchronized void increaseGamblesCount() {
		gambleId++;
	}

	/*
	 * public int getRaceCounter() { return raceCounter; }
	 */

	class HandleAClient extends Thread {

		private Socket socket;
		private Random rand;
		private final double MAX_SPEED = 10;
		private final int MAX_RADIUS = 10;
		int raceNumber;

		private ObjectInputStream in;
		private ObjectOutputStream sendToRace;
		private ObjectOutputStream sendToGambler;

		private carMessage message;
		Timer timer = new Timer();

		/** race veriables **/

		private double speed;
		private int colorIndex;
		private int radiusIndex;
		private int typeIndex;
		private int shapeIndex;
		private Race race;
		private NewCarDetails[] carsDetails;

		/** gambler client's veriables **/

		private Gambler gambler;
		private Gamble gamble;
		// private ArrayList<Race> avaliableRaces;
		private int readyRace;
		private Stream connectToClient;
		private ObjectOutputStream writeSpeed;

		public HandleAClient(Socket socket) {
			super();
			this.socket = socket;
			betsAmount = 0;

			/**
			 * insert the managers to the database if it's the first connection
			 * and if the managers are not exists in the data base
			 **/

			if (firstConnection) {
				// initializeDB();
				if (findGambler("111111111", SIGN_IN) == null && findGambler("222222222", SIGN_IN) == null) {
					InsertManagers();
				}
				System.out.println("managers inserted successfully");
				firstConnection = false;
				avaliableRaces = new ArrayList<Race>();
			}

		}

		/* this function ruffle initial values */

		public synchronized void randomValues(int raceNumber) {

			// String timeStamp = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss").format(Calendar.getInstance().getTime());

			InsertOpenRace(raceNumber);

			for (int i = 1; i <= NUM_OF_CARS; i++) {

				colorIndex = (int) (Math.random() * colors.length);
				speed = 0;
				radiusIndex = (int) (Math.random() * radius.length);
				typeIndex = (int) (Math.random() * type.length);
				shapeIndex = (int) (Math.random() * shapes.length);

				carsDetails[i - 1] = new NewCarDetails(i, speed, colorIndex, radius[radiusIndex], type[typeIndex],
						shapes[shapeIndex], 0);

				InsertCar(carsDetails[i - 1], raceNumber, i, " ", true);
				System.out.println("car inserted successfully");

			}

			Race race = new Race(carsDetails, raceNumber, "OPEN");
			avaliableRaces.add(race);
			System.out.println("race inserted successfully");

		}

		/* this function ruffle speed */

		public void randomSpeed(int raceNumber, Stream connection, double betsAmount, String startTime) {

			int count = 0;
			sendToGambler = connection.getSt();

			// new Thread(() -> {
			int songIndex = (int) (Math.random() * 3);
			MediaPlayer mp_start = new MediaPlayer(new Media(getClass().getResource("/mp3/" + start_sound).toString()));
			MediaPlayer mp_end = new MediaPlayer(new Media(getClass().getResource("/mp3/" + end_sound).toString()));
			MediaPlayer mp = new MediaPlayer(new Media(getClass().getResource("/mp3/" + songs[songIndex]).toString()));
			mp_start.play();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mp.play();
			
			while (count <= SPEED_CYCLE_COUNT) {
				double newSpeed;
				System.out.println(" count = " + count);
				
				
				//resultsRaces.clear();
				//resultsRaces.addAll(getQueryData(getRacesCurrentState()));
					
			

				try {

					// ObjectOutputStream newSpeedMessage = new
					// ObjectOutputStream(socket.getOutputStream());
					for (int i = 0; i < NUM_OF_CARS; i++) {

						newSpeed = Math.random() * MAX_SPEED + 1;
						carMessage message = new carMessage(i, 0, newSpeed, 0, " ", " ", false, raceNumber);
						sendToGambler.writeObject(message);
						sendToGambler.flush();
						System.out.println("i = " + i);

					}

					if (count == 4) {
						DataInputStream resultMessage;
                        
						resultMessage = new DataInputStream(connection.getSocket().getInputStream());
						String results = resultMessage.readUTF();
						int winningCar = resultMessage.readInt();
						System.out.println("server : " + results);

						/** send the results to the log line by line **/

						Scanner scanner = new Scanner(results);
						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							Platform.runLater(new Runnable() {
								@Override
								public void run() {

									log.printMsg(line);
								}
							});

							// process the line
						}

						scanner.close();

						String timeStampEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(Calendar.getInstance().getTime());
						
						sleep(2000);
						
						mp_end.play();

						sleep(60000);

						InsertTerminateRace(raceNumber, startTime, timeStampEnd, winningCar, betsAmount);
						ArrayList<Gamble> raceGambles = new ArrayList<>();
						getRaceGambles(raceNumber, raceGambles);
						double winningBetsSum = getBetsSumOnCar(raceNumber, winningCar);
						DeleteGamble(raceNumber);
						deleteCars(raceNumber);
						DeleteRace(raceNumber);
						InsertGamblesHistory(startTime, winningBetsSum, raceGambles);
						hmap.remove(raceNumber);
						
						

						break;

					}
					sleep(30000);
					
					System.out.println(" 5 second pass");
					count++;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}

			}

		}

		public void HandleGamble(Gambler gambler) throws Exception {

			Race race = null;
			ArrayList<Integer> gamblesId = new ArrayList<Integer>();
			

			System.out.println("server : the avaliable races are : ");
			for (int i = 0; i < avaliableRaces.size(); i++) {
				System.out.println("race number : " + i + " \n " + avaliableRaces.get(i));
			}

			System.out.println(" server : gambler is : " + gambler.toString());

			raceHistory.addAll(getQueryData(showRaceHistory()));
			carRaceHistory.addAll(getQueryData(getRaceHistoryCars()));
			newGambles.addAll(getQueryData(getGmblesNewBets(gambler.getId())));
			newRaces.addAll(getQueryData(getRacesCurrentState()));
			gamblesHistory.addAll(getQueryData(getGamblesHistory(gambler.getId())));
			allGamblesHistory.addAll(getQueryData(getAllGamblesHistory()));
			gamblersProfit.addAll(getQueryData(getGamblersProfit()));
			sys_Profit.addAll(getQueryData(getSys_Profit()));
			//resultsRaces.clear();
			//resultsRaces.addAll(getQueryData(getRacesCurrentState()));

			GamblerMessage message = new GamblerMessage(avaliableRaces, gambler, GAMBLER_MESSAGE, 0, 0, raceHistory,
					carRaceHistory, gamblesHistory, newGambles, newRaces, allGamblesHistory, gamblersProfit,sys_Profit);

			sendToGambler.writeObject(message);
			sendToGambler.flush();

			raceHistory.clear();
			carRaceHistory.clear();
			newGambles.clear();
			newRaces.clear();
			gamblesHistory.clear();
			allGamblesHistory.clear();
			gamblersProfit.clear();
			sys_Profit.clear();

			gamble = (Gamble) in.readObject();

			System.out.println("selected car is : " + gamble.getRaceId());

			InsertGambles(gamble);

			for (int i = 0; i < avaliableRaces.size(); i++) {

				if (avaliableRaces.get(i).getRaceNumber() == gamble.getRaceId()) {

					avaliableRaces.get(i)
							.setAmount((gamble.getSumOfMoney() + gamble.getProfit()) * gamble.getSelectedCars().size());
					System.out.println("race : " + avaliableRaces.get(i).getRaceNumber() + " amount "
							+ avaliableRaces.get(i).getAmount());

					updateRaceAmount(avaliableRaces.get(i).getRaceNumber(), avaliableRaces.get(i).getAmount());
				}

			}

			getNewReadyRace();

			for (int i = 0; i < avaliableRaces.size(); i++) {

				if (avaliableRaces.get(i).getStatus() == "READY") {
					updateRace(avaliableRaces.get(i).getRaceNumber(), "READY", avaliableRaces.get(i).getAmount(),
							"0000-00-00 00:00:00", "ready to start");
				}

			}

			// if(nextRace!=0){

			lock2.lock();
			try {
				/*
				 * if(readyRaces.size()==1){
				 * 
				 * nextRace = readyRaces.get(0);
				 * System.out.println("next race: " +nextRace); } else { /*
				 * for(int i=0; i<readyRaces.size();i++) {
				 * updateRaceStatus(readyRaces.get(i), "READY");
				 * 
				 * }
				 */
				/*
				 * nextRace = getNextRace(); System.out.println("next race: "
				 * +nextRace); }
				 */

				// updateRaceTime(nextRace, "ON", timeStampStart, betsAmount, 0,
				// "started");

				// updateRaceStatus(nextRace, "ON");

				nextRace = getNextRace();
				if (nextRace != 0) {
					System.out.println("next race: " + nextRace);
					String timeStampStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());

					System.out.println("------ timeStampStart: " + timeStampStart + "----");

					for (int j = 0; j < avaliableRaces.size(); j++) {

						if (avaliableRaces.get(j).getRaceNumber() == nextRace) {
							betsAmount = avaliableRaces.get(j).getAmount();
							System.out.println("race number : " + avaliableRaces.get(j).getRaceNumber()
									+ " bets amount : " + betsAmount);
							updateRace(nextRace, "ON", betsAmount, timeStampStart, "started");

							race = avaliableRaces.get(j);

							avaliableRaces.remove(j);
						}

					}
					connectToClient = hmap.get(nextRace);
					//resultsRaces.clear();
					//resultsRaces.addAll(getQueryData(getRacesCurrentState()));

					

					randomSpeed(nextRace, connectToClient, betsAmount, timeStampStart);
					for (int i = 0; i < NUM_OF_CARS; i++) {

						InsertCar(race.getCar(i), race.getRaceNumber(), (i + 1), timeStampStart, false);
					}

					
				}
			}

			finally {
				lock2.unlock();

			}

		}

		public synchronized void run() {

			int clientType = -1;

			/** get number of races **/

			try {
				DataInputStream readClientType = new DataInputStream(socket.getInputStream());
				// while(clientType==-1)
				// {
				clientType = readClientType.readInt();
				// }
				System.out.println("server: client type " + clientType);

			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			if (clientType == RACE_REQUEST) {
				lock.lock(); // Acquire the lock
				try {

					// raceNumber = getNumberOfRace() + 1;
					increaseCount();
					raceNumber = raceCounter;

					carsDetails = new NewCarDetails[NUM_OF_CARS];
					try {

						sendToRace = new ObjectOutputStream(socket.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					System.out.println(" reace counter = " + raceNumber);
					randomValues(raceNumber);
					

					for (int i = 1; i <= NUM_OF_CARS; i++) {

						carMessage message = new carMessage(i, carsDetails[i - 1].getColorIndex(),
								carsDetails[i - 1].getSpeed(), carsDetails[i - 1].getRadius(),
								carsDetails[i - 1].getType(), carsDetails[i - 1].getShape(), true, raceNumber);
						try {
							sendToRace.writeObject(message);
							sendToRace.flush();

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					hmap.put(raceNumber, new Stream(socket, sendToRace));
					
					

				}

				finally {
					lock.unlock();
				} // Release the lock
				
				

			}

			else {

				try {

					/** initialize data base for the first time **/

					/** maybe gambles array will required **/

					in = new ObjectInputStream(socket.getInputStream());
					sendToGambler = new ObjectOutputStream(socket.getOutputStream());
					System.out.println("server : new gambler request");
					DataOutputStream success_mess = new DataOutputStream(socket.getOutputStream());

					while (true) {
						gambler = (Gambler) in.readObject();
						Gambler foundGambler = findGambler(gambler.getId(), gambler.getWindowType());

						if (foundGambler == null) {
							if (gambler.getWindowType() == SIGN_IN) {

								
								success_mess.writeInt(SUCCESS_MESSAGE);
								InsertClients(gambler);
								System.out.println("client inserted succesfully");
								try {
									HandleGamble(gambler);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							else { // Log In
								
								success_mess.writeInt(FAILED_MESSAGE);
							}

						} else { // client found

							if (gambler.getWindowType() == SIGN_IN) {

								success_mess.writeInt(FAILED_MESSAGE);
							}

							else {
								System.out.println("client found ");
								success_mess.writeInt(SUCCESS_MESSAGE);
								Gambler gambler = foundGambler;
								try {
									HandleGamble(gambler);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
