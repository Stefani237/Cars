

import java.util.ArrayList;

import Model.CarLog;
import Model.ImportMySQLDB;
import Model.Model;
import UI.CreateRaceWindow;
import UI.GamblerView;
import UI.RaceView;
import controllers.GamblerController;
import controllers.RaceController;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CarRaceMVC extends Application {
	private final int SIGN_IN = 1;
	private final int LOG_IN = 0;

	private boolean firstTime = true;
	private int loop;
	private Button btnLogIn = new Button("Log In");
	private ArrayList<RaceController> raceControllerList;
	private ArrayList<RaceView> raceViewList;
	private Button btnSignIn = new Button("Sign In");

	private ArrayList<GamblerController> gamblerControllerList;
	private ArrayList<GamblerView> gamblerViewList;
	private ArrayList<Model> modelList;
	private int raceCounter = 0;
	private CarLog log;
	private Model model;

	@Override
	public void start(Stage primaryStage) {
		BorderPane pane = new BorderPane();

		ImageView image = new ImageView("images/fast2.jpg");
		pane.setCenter(image);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(btnLogIn, btnSignIn);
		buttons.setAlignment(Pos.CENTER);

		buttons.setMargin(btnLogIn, new Insets(2, 10, 2, 4));
		buttons.setMargin(btnSignIn, new Insets(2, 4, 2, 0));
		pane.setBottom(buttons);
		pane.setStyle("-fx-background-color: none");
		Scene scene = new Scene(pane, 450, 300);
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setTitle("CarRaceMVC"); // Set the stage title
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					Platform.exit();
					System.exit(0);

				} catch (Exception e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		gamblerControllerList = new ArrayList<GamblerController>();
		gamblerViewList = new ArrayList<GamblerView>();

		model = new Model();
		CreateRaceWindow raceWindow = new CreateRaceWindow(primaryStage, model);

		primaryStage.show(); // Display the stage
		primaryStage.setAlwaysOnTop(true);

		btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createGamblerWindow(primaryStage, LOG_IN);
			}
		});

		btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createGamblerWindow(primaryStage, SIGN_IN);
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void createGamblerWindow(Stage primaryStage, int windowType) {

		Scene scene;

		GamblerView gamblerView = new GamblerView(windowType);

		GamblerController gamblerController = new GamblerController(model, gamblerView, raceCounter, primaryStage);
		gamblerView.setModel(model);
		gamblerViewList.add(gamblerView);
		gamblerControllerList.add(gamblerController);

		Stage stg = new Stage();
		gamblerController.setOwnerStage(stg);

		if (windowType == LOG_IN) {
			scene = new Scene(gamblerView.getPane(), 350, 300);
			stg.setTitle("Log In");
		} else {
			scene = new Scene(gamblerView.getPane(), 350, 400);
			stg.setTitle("Sign In");
		}

		stg.setScene(scene);

		stg.setAlwaysOnTop(true);
		stg.show();

	}

}