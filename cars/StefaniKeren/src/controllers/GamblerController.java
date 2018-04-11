package controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Model.Model;
import UI.GamblerView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Message_Objects.Gamble;
import Message_Objects.Gambler;
import Message_Objects.GamblerMessage;
import Message_Objects.Race;

/**
 * This Gambler Controller class gets information from the server and updates
 * the GamblerViews.
 * 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class GamblerController {

	private final int SIGN_IN = 1;
	private final int LOG_IN = 0;
	private final int CLIENT_REQUEST = 0;
	private final int GAMBLER_MESSAGE = 0;
	private final int SUCCESS_MESSAGE = 1;
	private final int FAILED_MESSAGE = 0;

	private Model model;
	private GamblerView gamblerView;
	private int raceCounter;
	private Stage stg;
	private Stage stage;
	private Gambler gambler;
	private ObjectInputStream fromModel;
	private ObjectOutputStream toModel;

	public GamblerController(Model model, GamblerView gamblerView, int raceCounter, Stage stage) {

		System.out.println("i am a new controller");

		this.model = model;
		this.gamblerView = gamblerView;
		this.raceCounter = raceCounter;
		this.stage = stage;

		new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {

				Platform.exit();
				System.exit(0);

			}
		};

		new Thread(() -> {

			connectToServer();
		}).start();
	}

	public void connectToServer() {

		Socket socket;
		ArrayList<Race> raceList = null;
		ArrayList<String[]> raceHistory = null;
		ArrayList<String[]> carRaceHistory = null;
		ArrayList<String[]> gamblesHistory = null;
		ArrayList<String[]> newGambles = null;
		ArrayList<String[]> newRaces = null;
		ArrayList<String[]> allGamblesHistory = null;
		ArrayList<String[]> gamblersProfit = null;
		ArrayList<String[]> sys_Profit = null;
		GamblerMessage message = null;

		try {
			socket = new Socket("localhost", 8000);
			DataOutputStream clientType = new DataOutputStream(socket.getOutputStream());
			clientType.writeInt(CLIENT_REQUEST);

			/**
			 * this code section waits until client will write all the fields
			 * and press ok
			 **/

			System.out.println("before loop");

			while (gamblerView.getGambler() == null) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			int ok_Message = FAILED_MESSAGE;
			DataInputStream error_Mess = new DataInputStream(socket.getInputStream());

			toModel = new ObjectOutputStream(socket.getOutputStream());
			fromModel = new ObjectInputStream(socket.getInputStream());

			gambler = gamblerView.getGambler();

			System.out.println("after loop");

			toModel.writeObject(gambler);
			toModel.flush();

			ok_Message = error_Mess.readInt();

			if (ok_Message == FAILED_MESSAGE) {
				if (gambler.getWindowType() == LOG_IN) {

					Platform.runLater(() -> {
						gamblerView.error_Message("you enter wrong information");
					});

					Platform.runLater(() -> {

						stg.close();
					});

				}

				else {

					Platform.runLater(() -> {
						gamblerView.error_Message(" client is allready exists in the system");
					});

					Platform.runLater(() -> {

						stg.close();
					});

				}

			}

			else {

				while (message == null) {
					message = (GamblerMessage) fromModel.readObject();
					if (message.getMessageType() == GAMBLER_MESSAGE) {
						raceList = message.getRaceList();
						gambler = message.getGambler();
						raceHistory = message.getRaceHistory();
						carRaceHistory = message.getcarRaceHistory();
						gamblesHistory = message.getgamblesHistory();
						newGambles = message.getnewGambles();
						newRaces = message.getnewRaces();

						allGamblesHistory = message.getallGamblesHistory();
						gamblersProfit = message.getGamblersProfit();
						sys_Profit = message.getSys_Profit();

					}
				}

				/** getting data from the server **/

				/** create new gamble frame with the recieved data **/

				gamblerView.createGambleWindow(raceList, gambler, newGambles, newRaces, carRaceHistory, raceHistory,
						gamblesHistory, allGamblesHistory, gamblersProfit, sys_Profit);

				Platform.runLater(() -> {
					stg.close();
				});

				while (gamblerView.getGamble() == null) {
					try {

						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				Gamble gamble = gamblerView.getGamble();

				toModel.writeObject(gamble);
				toModel.flush();
				Platform.runLater(() -> {

					System.out.println("controller close window");
					gamblerView.getStg().close();
				});
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setOwnerStage(Stage stg) {
		this.stg = stg;
	}
}
