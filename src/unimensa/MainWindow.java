package unimensa;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindow extends Application {
	Functionality func;
	int connected;
	GridPane grid;
	ColumnConstraints cc;
	Scene scene;
	Text scenetitle;
	Text dbText;
	RadioButton dbChose;
	RadioButton dbChose2;
	ToggleGroup group;
	TextField otherDBURI;
	Label userName;
	TextField userTextField;
	Label pw;
	PasswordField pwBox;
	Button btn;
	HBox hbBtn;
	Text failedLogin;
	ArrayList<Object> loginItems = new ArrayList<Object>();
	
	/*
	 * main class, creates window with all sub-windows no functionalities like
	 * add data, remove data, etc... respect indentation o ve ammazzo scuoiati
	 * let's take the standard to comment what we add and alert everyone when
	 * you add a class and why pls <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3 <3
	 * <3 <3 <3
	 */
	public void start(Stage primaryStage) {
		func = new Functionality();
		
		primaryStage.setTitle("Login");
		primaryStage.setMinHeight(325);
		primaryStage.setMinWidth(500);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:resources/icon/icon.png"));
		primaryStage.show();

		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		cc = new ColumnConstraints();
		cc.setPercentWidth(50);
		grid.getColumnConstraints().add(cc);

		scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);

		scenetitle = new Text("Login to DB");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		dbText = new Text("Chose the DB you want to log in to");
		dbText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		grid.add(dbText, 0, 1);
		
		failedLogin = new Text();
		failedLogin.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		failedLogin.setFill(Color.RED);

		dbChose = new RadioButton("UniMensaDB");
		grid.add(dbChose, 0, 2);
		dbChose2 = new RadioButton("Other DB");
		grid.add(dbChose2, 1, 2);
		group = new ToggleGroup();
		dbChose.setToggleGroup(group);
		dbChose.setSelected(true);
		dbChose2.setToggleGroup(group);

		otherDBURI = new TextField("Example: jdbc:postgresql://address:port/db");
		userName = new Label("Username:");
		grid.add(userName, 0, 4);
		userTextField = new TextField();
		grid.add(userTextField, 1, 4);
		pw = new Label("Password:");
		grid.add(pw, 0, 5);
		pwBox = new PasswordField();
		grid.add(pwBox, 1, 5);

		btn = new Button("Sign in");
		btn.setDefaultButton(true);
		hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 7);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
                    if (dbChose2.isSelected()){
                    	grid.add(otherDBURI, 0, 3);
                    }
                    else
                        grid.getChildren().remove(otherDBURI);
            }
        });
		
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (dbChose.isSelected()) {
					connected = func.connect(userTextField.getText(), pwBox.getText());
				} else {
					connected = func.setConnection(otherDBURI.getText(), userTextField.getText(), pwBox.getText());
				}
				if (connected==-1){
					failedLogin.setText("Wrong credentials or DB offline/non existent");
					grid.add(failedLogin, 0, 7);
				}
				else if (connected==-2){
					failedLogin.setText("Drivers for PostgreSQL missing");
					grid.add(failedLogin, 0, 7);
				}
				else{
					MetaData.username = userTextField.getText();
					MetaData.password = pwBox.getText();
					primaryStage.close();
					LoginActivity hi = new LoginActivity();
					MetaData.func=func;
					hi.start();
				}
					
			}
		});
	}
	public Functionality getFunctionality(){
		func = new Functionality();
		func.connect(MetaData.username, MetaData.password);
		return func;
	}



	/*
	  public static void main(String[] args) { 
		  // Some rows of testing the Functionality class, pretty straightforward
		  Functionality func = new Functionality(); func.connect("username", "password");
		  // substitute with real UName & PW
		  //String[] values = { "Bolzano", "131", "145" };
		  //String[] values2 = { "Bressanone", "98", "70" };
		  //func.insert("unimensa", "location, tables, capacity", values);
		  //func.insert("unimensa", "location, tables, capacity", values2);
		  //func.update("unimensa", "location", "Bressanone", "capacity", "101");
		  // func.delete("unimensa", "location", "Bolzano");
		  // func.deleteAll("unimensa");
		  String[][] readValues = func.read("unimensa", "location, tables, capacity");
		  System.out.println("\nPrinting values:");
		  // SUPER IMPORTANT - Keep this for loop as for a printer in the correct
		  // format of Functionality.read method
		  for (int l = 0; l < readValues.length; l++) {
			  for (int i = 0; i <readValues[l].length; i++) {
				  if (i > (readValues[l].length - 1) || (i -(readValues[l].length - 1)) % readValues[l].length == 0)
				  {
					  System.out.println(readValues[l][i]);
				  }
				  else {
					  System.out.print(readValues[l][i] + ", ");
				  }
			  }
		  }
	  }*/
}
