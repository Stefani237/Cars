package UI;
import java.util.ArrayList;

import Model.Model;
import controllers.RaceController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** 
 * This CreateRaceWindow class creates and open new race window. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class CreateRaceWindow {

	private final int FIRST_TIME_NUM_OF_RACES = 3;

	private ArrayList<RaceController> raceControllerList;
	private ArrayList<RaceView> raceViewList;
	private Stage stage;
	private int raceCounter = 0;
	private Model model;

	public CreateRaceWindow(Stage stage, Model model) {
		raceControllerList = new ArrayList<RaceController>();
		raceViewList = new ArrayList<RaceView>();
		this.stage = stage;
		this.model = model;
		createNewWindow(stage,FIRST_TIME_NUM_OF_RACES);

	}

	public void createNewWindow(Stage stage,int iterations) {

		// Model model = new Model(raceCounter);

		/* in every button press we create new controller and new view */
		/*
		 * therefore- in the feature we may have to create two types of
		 * controller and view, one for the gambler and one for the race
		 */
		for (int i = 0; i < iterations; i++) {

			raceCounter++;
			RaceView raceView = new RaceView(raceCounter);
			RaceController raceController = new RaceController(model, raceView, raceCounter,this,stage);
			raceView.setModel(model, raceController.getCars());
			raceViewList.add(raceView);
			raceControllerList.add(raceController);
			// model.setControllerList(controllerList);
			// model.setViewList(viewList);
			
			if(iterations == 3)
			{
				Stage stg = new Stage();
				Scene scene = new Scene(raceView.getBorderPane(), 750, 500);
				PerspectiveCamera camera = new PerspectiveCamera(true);
				  
				  camera.setTranslateZ(-960);
				  camera.setTranslateY(245);
				  camera.setTranslateX(380);
				  camera.setNearClip(0.1);
				  camera.setFarClip(3000.0);
				  camera.setFieldOfView(30);
				  scene.setCamera(camera);
				raceController.setOwnerStage(stg);
				raceView.createAllTimelines();
				stg.setScene(scene);

				stg.setTitle("CarRaceView" + raceCounter);

				stg.setAlwaysOnTop(true);
				stg.show();
				
				scene.widthProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { // TODO
																															// Auto-generated
																															// method
																															// stub
						raceView.setCarPanesMaxWidth(newValue.doubleValue());
					}
				});
				
			}
			
			else
			{
			
			Platform.runLater(() -> {
				Stage stg = new Stage();
				Scene scene = new Scene(raceView.getBorderPane(), 750, 500);
				PerspectiveCamera camera = new PerspectiveCamera(true);
				  
				  camera.setTranslateZ(-960);
				  camera.setTranslateY(245);
				  camera.setTranslateX(380);
				  camera.setNearClip(0.1);
				  camera.setFarClip(3000.0);
				  camera.setFieldOfView(30);
				  scene.setCamera(camera);
				raceController.setOwnerStage(stg);
				raceView.createAllTimelines();
				stg.setScene(scene);

				stg.setTitle("CarRaceView" + raceCounter);

				stg.setAlwaysOnTop(true);
				stg.show();
				
				
				
				scene.widthProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { // TODO
																															// Auto-generated
																															// method
																															// stub
						raceView.setCarPanesMaxWidth(newValue.doubleValue());
					}
				});
		});
			
			
			
			
		}
		}

		/*
		 * we should create in the feature two controller and view list, one for
		 * the gambler and one for the race
		 */

	}
}
