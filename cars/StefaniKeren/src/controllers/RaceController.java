package controllers;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Model.Model;
import UI.Car;
import UI.CarEvents;
import UI.CarPane;
import UI.CreateRaceWindow;
import UI.ErrorMessage;
import UI.RaceView;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import Message_Objects.NewCarDetails;
import Message_Objects.carMessage;

/** 
 * This RaceController class gets information from the server and updates the raceViews. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class RaceController implements CarEvents {
	private final int MAXSPEED = 200;
	private final int NUM_OF_CARS = 5;
	private final int SPEED_CYCLE_COUNT = 4;
	private final int ITERATION_COUNT = 1;
	private final int CLIENT_REQUEST = 1;
	//private final int EXIT_MESSAGE = 999;
	// private static Lock lock = new ReentrantLock();
	private Car[] cars;
	private final int CAR1_ID = 0;
	private final int CAR2_ID = 1;
	private final int CAR3_ID = 2;
	private int raceCounter;
	private double newSpeed;
	private Stage stg;
	private Model model;
	private RaceView view;
	private ObjectInputStream FromModel;
	private carMessage message;
	private CreateRaceWindow raceWindow;
	private Stage stage;
	private Socket socket;
	private boolean openRace = false;
	private NewCarDetails[] results = new NewCarDetails[NUM_OF_CARS];
	private ArrayList<String[]> newRaces;
	
	
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
			Color.VIOLET, Color.GRAY, Color.CORAL };

	public RaceController(Model model, RaceView view, int raceCounter, CreateRaceWindow raceWindow,Stage stage) {

		this.model = model;
		this.view = view;
		this.raceCounter = raceCounter;
		this.stage = stage;
		cars = new Car[NUM_OF_CARS];
		this.raceWindow = raceWindow;
		for (int i = 0; i < cars.length; i++) {

			cars[i] = new Car(i, raceCounter, model.getLog());
			

		}

		new Thread(() -> {
			connectToServer();
		}).start();
		
		/*stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				
				try {
					
					System.out.println(" before error request race number is "+raceCounter);
					DataOutputStream exit_message = new DataOutputStream(socket.getOutputStream());
					exit_message.writeInt(raceCounter);
					//raceWindow.createNewWindow(stage,ITERATION_COUNT);
					
					
					Platform.exit();
					System.exit(0);
					
					
					
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}
				

			}
		});*/
		


		

		/* new EventHandler<WindowEvent>() { 
			 
	     public void handle(WindowEvent event) {
	    	 
	    	/* try {
	    	 
				DataOutputStream exit_message = new DataOutputStream(socket.getOutputStream());
				exit_message.writeInt(raceCounter);
				raceWindow.createNewWindow(stage,ITERATION_COUNT);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    	 
		 
	    	/* Platform.exit();
			 System.exit(0);
	    	 
	    	 

		 
		  }
		 };*/
		
		
		
		
		 

	}

	public void setOwnerStage(Stage stg) {
		this.stg = stg;
		stg.setResizable(false);
		
		Platform.setImplicitExit(false);
		
		stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        event.consume();
		    }
		});
	}

	public void errorAlert(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stg);
		alert.setTitle("Error");
		alert.setContentText(msg);
		alert.show();
	}

	public void closeRace() {
		/*
		 * new EventHandler<WindowEvent>() { //public void handle(WindowEvent
		 * event) {
		 * 
		 * Platform.exit(); System.exit(0);
		 * 
		 * //} };
		 */
        
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
		delay.setOnFinished(event -> {stg.close();
		view.getRes_Stage().close();});
		delay.play();
	}

	public Car getCarById(int id, int raceCounter) {
		return cars[id];

	}

	public Car[] getCars() {
		return cars;
	}

	public void connectToServer() {

		//Socket socket;
		int raceOn = 0;
		ObjectInputStream speedFromModel = null;
		ObjectInputStream closeMessage = null;
		carMessage speedMessage;
		// for (int i = 0; i < cars.length; i++) {

		// cars[i].setSpeed(0, raceCounter);

		// }

		try {

			socket = new Socket("localhost", 8000);
			DataOutputStream clientType = new DataOutputStream(socket.getOutputStream());
			clientType.writeInt(CLIENT_REQUEST);

			FromModel = new ObjectInputStream(socket.getInputStream());

			// while (true) {

			for (int i = 0; i < cars.length; i++) {

				try {

					carMessage message = (carMessage) FromModel.readObject();

					if (message.getFirstTime() == true) {

						cars[i].setColor(colors[message.getColorIndex()], message.getRaceCounter());
						cars[i].setRadius(message.getRaduis(), message.getRaceCounter());
						cars[i].setSpeed(0, message.getRaceCounter());
						cars[i].setType(message.getType());
						cars[i].setShape(message.getShape());
						results[i] = new NewCarDetails(i, 0, message.getColorIndex(), cars[i].getRadius(), cars[i].getType(), cars[i].getShape(), 0);
						

					}

					/*
					 * if(FromModel.read()==-1){ break; }
					 */

				}

				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			int count = 0;
			
			
			for(int i=0;i<NUM_OF_CARS;i++){

				CarPane pane = (CarPane) view.getCarPane1(i);
				//if(pane != null)
					//if(pane.getTimeline() != null)
						pane.getTimeline().setCycleCount(Timeline.INDEFINITE);
			}
			

			

			
			while(true){
				
				//speedFromModel = new ObjectInputStream(socket.getInputStream());
			//	speedMessage = (carMessage)FromModel.readObject();
				
				
				
				//if(speedMessage.getMessageType()==SPEED_MESSAGE)
				//{
					//newSpeed = speedMessage.getSpeed();

			while (count <= SPEED_CYCLE_COUNT ) {
				
				//int raceOn = 0;
				System.out.println("race controller : new speed : "+newSpeed);
				
				System.out.println(" race number "+raceCounter+"is on loop");

				try {

					
					
					for (int i = 0; i < cars.length; i++) {
						carMessage message = (carMessage) FromModel.readObject();
						 raceOn = message.getRaceCounter();

						//newRaces = message.getNewRaces();


					//	if (raceCounter == message.getRaceCounter()) {
							cars[i].setSpeed(message.getSpeed(), raceOn);
							results[i].setDistamce(cars[i].getSpeed());
							
							//System.out.println("race controller : new speed : "+message.getSpeed());
						//	System.out.println("race controller : section time : "+results[i].getTimeS());
					//	}

					/*	else {
							cars[i].setSpeed(0, message.getRaceCounter());
						}*/
					}
					
				//	if (raceCounter == raceOn) {
					
					

					count++;
				//	}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					speedFromModel.close();
					e.printStackTrace();
				}
				
				if (openRace==false) {   //raceCounter == raceOn
					raceWindow.createNewWindow(stage,ITERATION_COUNT);
					openRace = true;
					
					}

			}

			for (int i = 0; i < cars.length; i++) {

				cars[i].setSpeed(0, raceCounter);

				
			}
			
			
			if (raceCounter == raceOn)
			{
			System.out.println("before sort");
			
			SortResults sort = new SortResults();
			sort.sort(results);
			
            String resultsMsg ="";
            
            resultsMsg+="\n";
            resultsMsg+="race : "+raceOn+ "\n";
			
			for(int i=0; i<cars.length; i++){
				
				resultsMsg+= "#" + (i+1) + ": car "+(results[i].getCarId()+1)+ " distance "+results[i].getDistance()+" km's \n";
			}
			
			
			        DataOutputStream resultMessage = new DataOutputStream(socket.getOutputStream());
	                resultMessage.writeUTF(resultsMsg);
	                resultMessage.writeInt((results[0].getCarId()+1));

			    	
	                view.createDetailsGrid(results);
			
			System.out.println(resultsMsg);
			
			Thread.sleep(60000);
			openRace = false;
			
			
			closeRace();
			break;
			}
			
			
			
			}
			//}
		}catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		/*
			 * catch (ClassNotFoundException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); }
			 */

	}
	

}