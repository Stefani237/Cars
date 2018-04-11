package UI;




import java.util.ArrayList;

import Model.Model;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import Message_Objects.NewCarDetails;

/** 
 * This RaceView class represents the race user's interface. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class RaceView {
	private final int NUM_OF_CARS = 5;
	private Model model;
	private Pane pane;
	private BorderPane border_pane;
	private GridPane details_grid, cars_grid;
	private CarPane[] carPanes = new CarPane[NUM_OF_CARS];
	private Label lbl1, lbl2, lbl3,lbl4,lbl5,winLbl;
	private Slider slRadius;
	private TextField spd_txt1, spd_txt2, spd_txt3;
	private ComboBox<String> colorComBox, carIdComBox;
	private ObservableList<String> items_color, items_car;
	private Button btn;
	private int raceCounter;
	private Stage res_Stage;
	private TableView<String[]> resultsTable = new TableView<>();
	
	//private ObservableList<String[]> data = FXCollections.observableArrayList();
	


	/* the car panes manage all the car events */

	
	public RaceView(int raceCounter) {
		this.raceCounter = raceCounter;
		
		border_pane = new BorderPane();
		//data.clear();
		
			
			createCarsGrid();
			
			
	
		
		//createDetailsGrid();
		//border_pane.setTop(details_grid);
		
		
		border_pane.setCenter(cars_grid);
	}

	// initialize all the events

	public void setModel(Model myModel, Car[] cars) {
		model = myModel;
		if (model != null) {

			for (int i = 0; i < carPanes.length; i++) {
				carPanes[i].setCarModel(cars[i]);

			}

		}
	}

	public Model getModel(Model myModel) {
		return model;
	}

	
	public void createCarsGrid() {
		cars_grid = new GridPane();

		for (int i = 0; i < carPanes.length; i++) {
			carPanes[i] = new CarPane();
			// carPanes[i].setVisible(false);

			cars_grid.add(carPanes[i], 0, i);

		}

		cars_grid.setStyle("-fx-background-color: white");
		cars_grid.setGridLinesVisible(true);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(100);
		cars_grid.getColumnConstraints().add(column);
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(18);

		for (int i = 0; i < carPanes.length; i++) {
			cars_grid.getRowConstraints().add(row);

		}

	}

	/* creates animation to each car pane */

	public void createAllTimelines() {
	
		for (int i = 0; i < carPanes.length; i++) {
			carPanes[i].createTimeline();
			

		}

	}

	/* all the cars information: speed, radius and so on */

	public void createDetailsGrid(NewCarDetails[] results) {
		Pane pane = new Pane();
		
		createResultsTable( results, resultsTable );
		
		Label lblA = new Label ("the results are:");
		Label lblB = new Label ("coming up races:");

		lblA.setTextFill(Color.BLACK);
		lblA.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		lblB.setTextFill(Color.BLACK);
		lblB.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		VBox box = new VBox();

		

		VBox scroll = new VBox();

		scroll.getChildren().add(resultsTable);

		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setPrefSize(500, 200);
		scrollpane.setContent(scroll);

		VBox scroll2 = new VBox();

		

		box.getChildren().addAll(lblA,  scrollpane);
		
		Platform.runLater(() -> {

		    res_Stage = new Stage();
			Scene scene = new Scene(box, 500, 450);
			res_Stage.setTitle("results:");

			res_Stage.setScene(scene);

			res_Stage.setAlwaysOnTop(true);
			res_Stage.show();

		});
		
	}
	
	public void createResultsTable(NewCarDetails[] results, TableView<String[]> resultsTable  ){
		

		ArrayList<String[]> resultsList = new ArrayList<String[]>();
		
		String [] item = new String [3];
		
		
		
		 item [0] = "place";
		 item [1] = "car number";
		 item [2] = "distance";
		 
		 resultsList.add(item);

			for (int i = 0; i < results.length ; i++) {
				
				String [] items = new String [3];

				items[0] = "# "+(i+1);
				items[1] = " car "+(results[i].getCarId()+1);
				items[2] = " "+results[i].getDistance();
				
				System.out.println("results information");
				
				for(int j=0 ; j<3; j++){
					
					System.out.print(items[j]+" ");
				}
				
				System.out.println();
				
				resultsList.add(items);
				
				createQueryTable(resultsList, resultsTable);
				
				
			}
			// Close the connection

		

	}
	
	
	public void createQueryTable(ArrayList<String[]> arr, TableView<String[]> table) {

		table.getColumns().clear();
		// ObservableList<ObservableList> data =
		table.setItems(FXCollections.observableArrayList());
		try {
			// ResultSetMetaData rsMetaData = (ResultSetMetaData)
			// rs.getMetaData();

			for (int i = 1; i <= arr.get(0).length; i++) {
				final int columnNum = i - 1;
				TableColumn<String[], String> Column = new TableColumn<String[], String>(arr.get(0)[columnNum]);
				Column.setMinWidth(100);
				Column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[columnNum]));
				table.getColumns().add(Column);
			}

			for (int i = 0; i < arr.size(); i++) {

				for (int j = 0; j < arr.get(i).length; j++) {

					System.out.print(arr.get(i)[j] + " ");
				}

				System.out.println();

			}

			for (int i = 1; i < arr.size(); i++) {

				table.getItems().add(arr.get(i));

			}
			// Close the connection

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}

	}

	public BorderPane getBorderPane() {
		return border_pane;
	}

	public GridPane getDetailsGrid() {
		return details_grid;
	}

	public GridPane getCarsGrid() {
		return cars_grid;
	}

	/* change the carPane size */

	public void setCarPanesMaxWidth(double newWidth) {
		for (int i = 0; i < carPanes.length; i++) {
			carPanes[i].setMaxWidth(newWidth);

		}

	}

	public Pane getCarPane1(int i) {
		return carPanes[i];
	}

	public TextField getSpeedTxt1() {
		return spd_txt1;
	}

	public TextField getSpeedTxt2() {
		return spd_txt2;
	}

	public TextField getSpeedTxt3() {
		return spd_txt3;
	}

	public ObservableList<String> getItemsCar() {
		return items_car;
	}

	public ObservableList<String> getItemsColor() {
		return items_color;
	}

	public ComboBox<String> getColorComBox() {
		return colorComBox;
	}

	public ComboBox<String> getCarIdComBox() {
		return carIdComBox;
	}

	public Button getColorButton() {
		return btn;
	}

	public Slider getRadSlider() {
		return slRadius;
	}

	public Stage getRes_Stage() {
		return res_Stage;
	}
	
	


}
