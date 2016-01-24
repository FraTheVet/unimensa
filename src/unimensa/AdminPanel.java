package unimensa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AdminPanel {
	private VBox vbox;
	private Label id;
	private Label pw;
	private TextField idTextField;
	private PasswordField pwTextField;
	private Text adminText;
	private HBox hbLogin;
	private Button login;
	private Text failedLogin;
	
	public VBox start(){
		vbox = new VBox();
		adminText = new Text("Chose the DB you want to log in to");
		adminText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		vbox.getChildren().add(adminText);
		
		id = new Label("Username:");
		vbox.getChildren().add(id);
		idTextField = new TextField();
		vbox.getChildren().add(idTextField);
		pw = new Label("Password:");
		vbox.getChildren().add(pw);
		pwTextField = new PasswordField();
		vbox.getChildren().add(pwTextField);
		
		failedLogin = new Text("You failed the log in");
		failedLogin.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		failedLogin.setFill(Color.RED);
		
		login = new Button("Sign in");
		login.setDefaultButton(true);
		hbLogin = new HBox(10);
		hbLogin.setAlignment(Pos.BOTTOM_RIGHT);
		hbLogin.getChildren().add(login);
		vbox.getChildren().add(hbLogin);
		
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String[][] staff = MetaData.func.read("employee", new String[]{"id", "surname"});
				String[] credentials = new String[] { idTextField.getText(), pwTextField.getText() };
				boolean logged = false;
				for (String[] memberCredentials : staff) {
					if (memberCredentials[0].equals(credentials[0])
							&& memberCredentials[1].equals(credentials[1])) {
						logged = true;
						break;
					}
				}
				if (logged){
					System.out.println("Successfully logged in to the Admin Panel");
					vbox.getChildren().remove(1);
				}
				else{
					vbox.getChildren().add(failedLogin);
				}
			}
		});
				
		return vbox;
	}
}
