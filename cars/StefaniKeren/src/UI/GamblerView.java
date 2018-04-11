package UI;


import java.util.ArrayList;



import Model.Model;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import Message_Objects.Gamble;
import Message_Objects.Gambler;
import Message_Objects.Race;
import Message_Objects.RaceCar;

/** 
 * This RaceView class represents the gambler user's interface. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class GamblerView {
	private final int SIGN_IN = 1;
	private final int LOG_IN = 0;
	private final int NUM_OF_CARS = 5;
	private final String PERMISSION_TYPE = "C";
	private final double SYS_DEDUCATION = 0.05;
	private final double REMAIN_AMOUNT = 0.95;
	private Model model;
	private BorderPane border_pane;
	private GridPane grid, cars_grid;
	private CarPane[] carPanes = new CarPane[NUM_OF_CARS];
	private Label lbl1, lbl2, lbl3, lbl4;
	private Slider slRadius;
	private TextField txt1, txt2, txt3, txt4;
	private PasswordField passwordTxt;
	private ComboBox<String> colorComBox, carIdComBox;
	private ObservableList<String> items_color, items_car;
	private Button btOK, btSignIn, btSubmit, btRaceHistory, btGambles,btSysGamblers,btSysRaces;
	private int raceCounter;
	private int windowType;
	private Gambler gambler;
	private int chosenRace = 0;
	private CheckBox[] cars;
	private CheckBox cb;
	private Gamble gamble;
	private int j;
	private Stage stg, frameStg;
	private ListView<Integer> tfCars = new ListView<Integer>();
	private TableView<String[]> raceHistorytable = new TableView<>();
	private TableView<String[]> carsHistorytable = new TableView<>();
	private TableView<String[]> gamblesHistorytable = new TableView<>();
	private TableView<String[]> newGamblestable = new TableView<>();
	private TableView<String[]> newRacetable = new TableView<>();
	private TableView<String[]> allGamblestable = new TableView<>();
	private TableView<String[]> gamblersProfittable = new TableView<>();
	private TableView<String[]> sys_Profittable = new TableView<>();
	private ObservableList<Integer> carItems;
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
			Color.VIOLET, Color.GRAY, Color.CORAL };
	String colorNames[] = { "RED", "AQUA", "BLUE", "GREEN", "YELLOW", "ORANGE", "PINK", "VIOLET", "GRAY", "CORAL" };

	/* the car panes manage all the car events */

	public GamblerView(int type) {

		if (type == LOG_IN) {
			createLogInGrid();
		}

		else {
			createSignInGrid();
		}

	}

	// initialize all the events

	public void setModel(Model myModel) {
		model = myModel;

	}

	public Model getModel(Model myModel) {
		return model;
	}

	public void createGamblerCard() {
		cars_grid = new GridPane();

		for (int i = 0; i < carPanes.length; i++) {
			carPanes[i] = new CarPane();
			// carPanes[i].setVisible(false);

			cars_grid.add(carPanes[i], 0, i);

		}

		cars_grid.setStyle("-fx-background-color: beige");
		cars_grid.setGridLinesVisible(true);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(100);
		cars_grid.getColumnConstraints().add(column);
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(33);

		for (int i = 0; i < carPanes.length; i++) {
			cars_grid.getRowConstraints().add(row);

		}

	}

	/* all the cars information: speed, radius and so on */

	public void createSignInGrid() {

		grid = new GridPane();
		border_pane = new BorderPane();
		btOK = new Button("OK");
		// btOK.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		lbl1 = new Label("ID: ");
		lbl2 = new Label("Name: ");
		lbl3 = new Label("Password: ");
		lbl4 = new Label("Card Number: ");

		txt1 = new TextField();
		txt2 = new TextField();
		txt3 = new TextField();
		txt4 = new TextField();

		grid.add(lbl1, 0, 0);
		grid.add(lbl2, 0, 1);
		grid.add(lbl3, 0, 2);
		grid.add(lbl4, 0, 3);
		grid.add(txt1, 1, 0);
		grid.add(txt2, 1, 1);
		grid.add(txt3, 1, 2);
		grid.add(txt4, 1, 3);
		// grid.add(btOK, 2, 1);

		border_pane.setTop(grid);
		ImageView image = new ImageView("images/Las-Vegas.png");
		border_pane.setCenter(image);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(btOK);
		buttons.setAlignment(Pos.CENTER);

		buttons.setMargin(btOK, new Insets(0, 0, 4, 0));
		border_pane.setBottom(buttons);

		windowType = SIGN_IN;

		btOK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String id = txt1.getText();
				String name = txt2.getText();
				String password = txt3.getText();
				String cardNum = txt4.getText();

				gambler = new Gambler(id, name, password, cardNum, PERMISSION_TYPE, windowType);

			}
		});

	}

	public Gambler getGambler() {
		return gambler;
	}

	public void createLogInGrid() {

		grid = new GridPane();
		border_pane = new BorderPane();
		btOK = new Button("OK");

		lbl1 = new Label("ID: ");
		lbl2 = new Label("Password: ");

		txt1 = new TextField();
		passwordTxt = new PasswordField();

		grid.add(lbl1, 0, 0);
		grid.add(lbl2, 0, 1);
		grid.add(txt1, 1, 0);
		grid.add(passwordTxt, 1, 1);

		border_pane.setTop(grid);
		ImageView image = new ImageView("images/new-york-city.jpg");
		border_pane.setCenter(image);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(btOK);
		buttons.setAlignment(Pos.CENTER);

		buttons.setMargin(btOK, new Insets(0, 0, 4, 0));
		border_pane.setBottom(buttons);

		windowType = LOG_IN;

		btOK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String id = txt1.getText();
				String password = passwordTxt.getText();
				gambler = new Gambler(id, "", password, "", PERMISSION_TYPE, windowType);

			}
		});

	}

	public void createGambleWindow(ArrayList<Race> raceList, Gambler gambler, ArrayList<String[]> newGambles,
			ArrayList<String[]> newRaces, ArrayList<String[]> carsHistory, ArrayList<String[]> raceHistory, ArrayList<String[]> gamblesHistory,
			ArrayList<String[]> allGamblesHistory, ArrayList<String[]> gamblersProfit,ArrayList<String[]> sys_Profit) {

		// double amount;

		ArrayList<Integer> selectedCars = new ArrayList<>();
		ObservableList<RaceCar> data;
		tfCars.setEditable(false);
		tfCars.setPrefHeight(50);
		tfCars.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		VBox gamblerDetails = new VBox();

		grid = new GridPane();
		border_pane = new BorderPane();
		VBox centerBorder = new VBox();
		btSubmit = new Button("Let's bet!");// btOK.setMaxSize(Double.MAX_VALUE,
		btRaceHistory = new Button("race History");
		btGambles = new Button("show gambles");
		btSysGamblers = new Button("gamblers information");
		btSysRaces = new Button("races profits");

		Label welcome = new Label("Hello " + gambler.getName() + "!");
		welcome.setTextFill(Color.DARKGREEN);
		welcome.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		Label idLabel = new Label("ID: " + gambler.getId());
		idLabel.setTextFill(Color.DARKGREEN);
		idLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		Label cardNum = new Label("Card number: " + gambler.getCardNumber());
		cardNum.setTextFill(Color.DARKGREEN);
		cardNum.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		Label space1 = new Label();

		gamblerDetails.getChildren().addAll(welcome, idLabel, cardNum, space1);

		lbl1 = new Label("Select race: ");
		lbl1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		lbl2 = new Label("Select cars: ");
		lbl2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		lbl3 = new Label("Amount: ");
		lbl3.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		ComboBox<Integer> combo = new ComboBox<>();

		ArrayList<Integer> raceOptions = new ArrayList<Integer>();
		for (int i = 0; i < raceList.size(); i++) {
			raceOptions.add(raceList.get(i).getRaceNumber());

		}

		System.out.println("The raceList : ");
		for (int i = 0; i < raceList.size(); i++) {

			System.out.println(raceList.get(i));
		}

		ObservableList<Integer> raceNumbers = FXCollections.observableArrayList(raceOptions);

		System.out.println("The list options : ");
		for (int i = 0; i < raceNumbers.size(); i++) {

			System.out.println(raceNumbers.get(i));
		}

		combo.getItems().addAll(raceNumbers);

		combo.setOnAction(e -> setChosenNumber(combo.getValue(), raceList, selectedCars));

		VBox scroll = new VBox();

		createQueryTable(newRaces, newRacetable);

		scroll.getChildren().add(newRacetable);

		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setPrefSize(500, 200);
		scrollpane.setContent(scroll);

		txt3 = new TextField();

		grid.add(lbl1, 0, 0);
		grid.add(lbl2, 0, 1);
		grid.add(lbl3, 0, 2);
		grid.add(combo, 1, 0);

		grid.add(tfCars, 1, 1);
		grid.add(txt3, 1, 2);

		Label space2 = new Label();
		centerBorder.getChildren().addAll(grid, space2, scrollpane);

		border_pane.setTop(gamblerDetails);

		border_pane.setCenter(centerBorder);
		HBox button = new HBox();
		button.getChildren().addAll(btSubmit, btRaceHistory, btGambles,btSysGamblers,btSysRaces);
		button.setAlignment(Pos.CENTER);

		border_pane.setBottom(button);

		carItems = FXCollections.observableArrayList();
		tfCars.setItems(carItems);
		tfCars.setOrientation(Orientation.HORIZONTAL);

		for (int i = 0; i < NUM_OF_CARS; i++) {
			carItems.add(i + 1);
		}

		btGambles.setOnAction(e -> {

			frameStg = new Stage();
			createDataFrame(newGambles, gamblesHistory, newGamblestable, gamblesHistorytable, frameStg,
					"gambles details", "gambles on terminate races: ", "new gambles:");

		});
		
		btSysGamblers.setOnAction(e -> {

			frameStg = new Stage();
			createDataFrame(gamblersProfit ,allGamblesHistory ,gamblersProfittable , allGamblestable, frameStg,
					"gamblers statistic", "gamblers profit:", "all gamblers history");

		});
		
		btSysRaces.setOnAction(e -> {

			frameStg = new Stage();
			raceStatisticsFrame(sys_Profit  ,sys_Profittable , frameStg,"race profit:");

		});
		
		
			

		btSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/** add code for handeling with empty input **/

				try {
					double amount = Double.parseDouble(txt3.getText());
					for (int i = 0; i < selectedCars.size(); i++)
						System.out.println("selected check box item : " + selectedCars.get(i));
					ObservableList<Integer> selected = tfCars.getSelectionModel().getSelectedItems();

					for (Integer value : selected) {

						selectedCars.add(value);
					}
					gamble = new Gamble(gambler.getId(), chosenRace, selectedCars, amount * REMAIN_AMOUNT,
							amount * SYS_DEDUCATION); // the server will give
														// the gamble id by
														// query

				} catch (NumberFormatException e) {
					
					
					error_Message("Please enter amount of money");
				}

			}
		});

		btRaceHistory.setOnAction(e -> {

			frameStg = new Stage();
			createDataFrame(carsHistory ,raceHistory ,carsHistorytable , raceHistorytable, frameStg,
					"race history:", "participate cars history:", "race history:");

		});

		Platform.runLater(() -> {

			stg = new Stage();

			Scene scene = new Scene(border_pane, 500, 400);
			stg.setTitle("Gamble");

			stg.setScene(scene);

			stg.setAlwaysOnTop(true);
			stg.show();

		});

	}

	public void createDataFrame(ArrayList<String[]> arr1, ArrayList<String[]> arr2, TableView<String[]> table1,
			TableView<String[]> table2, Stage frameStg, String title, String lblText1, String lblText2) {

		createQueryTable(arr1, table1);

		createQueryTable(arr2, table2);
		
		Label lblA = new Label (lblText1);
		Label lblB = new Label (lblText2);

		lblA.setTextFill(Color.BLACK);
		lblA.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		lblB.setTextFill(Color.BLACK);
		lblB.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		VBox box = new VBox();

		

		VBox scroll = new VBox();

		scroll.getChildren().add(table1);

		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setPrefSize(500, 200);
		scrollpane.setContent(scroll);

		VBox scroll2 = new VBox();

		scroll2.getChildren().add(table2);

		ScrollPane scrollpane2 = new ScrollPane();
		scrollpane2.setFitToWidth(true);
		scrollpane2.setFitToHeight(true);
		scrollpane2.setPrefSize(500, 200);
		scrollpane2.setContent(scroll2);

		box.getChildren().addAll(lblB,  scrollpane,  lblA, scrollpane2);

		Platform.runLater(() -> {

			Scene scene = new Scene(box, 500, 450);
			frameStg.setTitle(title);

			frameStg.setScene(scene);

			frameStg.setAlwaysOnTop(true);
			frameStg.show();

		});

	}
	
	public void raceStatisticsFrame(ArrayList<String[]>sys_Profit  ,TableView<String[]> sys_Profittable , Stage frameStg,
			String title){
		
		createQueryTable(sys_Profit, sys_Profittable);
		
		Label raceProfitslbl = new Label("system profits from races");

		raceProfitslbl.setTextFill(Color.BLACK);
		raceProfitslbl.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		

		VBox box = new VBox();

		

		VBox scroll = new VBox();

		scroll.getChildren().add(sys_Profittable);

		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setPrefSize(500, 200);
		scrollpane.setContent(scroll);

		

		box.getChildren().addAll(raceProfitslbl,  scrollpane);

		Platform.runLater(() -> {

			Scene scene = new Scene(box, 500, 450);
			frameStg.setTitle(title);

			frameStg.setScene(scene);

			frameStg.setAlwaysOnTop(true);
			frameStg.show();

		});
		
		
	}

	/** creates table that presents the raceHistory **/

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

	public BorderPane getPane() {
		return border_pane;
	}

	public Pane getCarPane1(int i) {
		return carPanes[i];
	}

	public TextField getTxt1() {
		return txt1;
	}

	public TextField getTxt2() {
		return txt2;
	}

	public Button getOKButton() {
		return btOK;
	}

	public Button getSignInButton() {
		return btSignIn;
	}

	public Button getSubmitButton() {
		return btSubmit;
	}

	public Button getGamblesButton() {
		return btGambles;
	}

	public void setChosenNumber(int index, ArrayList<Race> raceList, ArrayList<Integer> selectedCars) {

		// int i;
		chosenRace = index;
		System.out.println("function set chosen number " + chosenRace + " was chosen");

		for (int i = 0; i < selectedCars.size(); i++) {

			System.out.println("function : selected check box item : " + selectedCars.get(i));

		}

	}

	public Gamble getGamble() {

		return gamble;
	}

	public Stage getStg() {
		return stg;
	}
	
	public void error_Message(String msg){
		
		Stage stage = new Stage();
		new ErrorMessage(stage, msg).show();;
		stage.setAlwaysOnTop(true);
	}

}
