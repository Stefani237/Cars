package Model;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class CarLog extends Stage
{ private ScrollPane srcPane;
  private VBox vBoxInSrcPane;
  private BorderPane mainFrame;
  private Scene scene;
  public CarLog()
  {	srcPane = new ScrollPane();
	srcPane.setFitToHeight(true);
	srcPane.setFitToWidth(true);
	vBoxInSrcPane = new VBox(3);
	mainFrame = new BorderPane();
	scene = new Scene(mainFrame);
	srcPane.setContent(vBoxInSrcPane);
	mainFrame.setCenter(srcPane);
	setScene(scene);
	setTitle("Car Logger "+1);
	setX(0);
	setY(0);
	setHeight(700);
	setWidth(600);
	this.show();
  }
  
  /* prints the message in the log */
  
  public void printMsg(String str)
  {	Label action = new Label(str);
	vBoxInSrcPane.getChildren().add(action);
	srcPane.setVvalue(action.getScaleY() + action.getHeight());
  }
}