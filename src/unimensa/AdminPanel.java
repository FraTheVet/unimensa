package unimensa;

import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class AdminPanel {
	private VBox vbox;
	private VBox tableVBox;
	private Label id;
	private Label pw;
	private TextField idTextField;
	private PasswordField pwTextField;
	private Text adminText;
	private HBox hbLogin;
	private Button login;
	private Text failedLogin;
	private boolean logged;
	private ComboBox<String> knownQueries;
	private StackPane root;
	private String[][] dataArray;
	private ObservableList<String[]> data;
	
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
				String[] credentials = new String[] { idTextField.getText(), pwTextField.getText().toLowerCase() };
				logged = false;
				for (String[] memberCredentials : staff) {
					if (memberCredentials[0].equals(credentials[0])
							&& memberCredentials[1].toLowerCase().equals(credentials[1])) {
						logged = true;
						break;
					}
				}
				if (logged){
					System.out.println("Successfully logged in to the Admin Panel");
					vbox = tabManagmentAdmin();
					update();
				}
				else{
					vbox.getChildren().add(failedLogin);
				}
			}
		});
		return vbox;
	}
	private VBox tabManagmentAdmin(){
		TabPane tabs = new TabPane();
		Tab viewingtab = new Tab("View Data");
		Tab insertiontab = new Tab("Insert Data");
		Tab deletiontab = new Tab("Delete Data");
		Tab editingtab = new Tab("Edit Data");
		tabs.getTabs().add(viewingtab);
		tabs.getTabs().add(insertiontab);
		tabs.getTabs().add(deletiontab);
		tabs.getTabs().add(editingtab);
		tabs.setPadding(new Insets(10,10,10,10));
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
			
		VBox viewingbox = tableViewAdmin();
		VBox insertionbox = new VBox();
		VBox deletionbox = new VBox();
		VBox editingbox = new VBox();
		VBox replacementbox = new VBox();
		
		viewingtab.setContent(viewingbox);
		insertiontab.setContent(insertionbox);
		deletiontab.setContent(deletionbox);
		editingtab.setContent(editingbox);
		
		replacementbox.getChildren().add(tabs);
		return replacementbox;
	}
	
	private VBox tableViewAdmin(){
		VBox vbox = new VBox();
		VBox insertionbox = new VBox();
		vbox.getChildren().add(insertionbox);
		
		knownQueries = new ComboBox<String>();
		knownQueries.setPromptText("See any of the available tables!");
		knownQueries.getItems().addAll(new String[]{"Unimensa Locations", "Dishes Offered", "University People", "Menu Prices","Chef licence",
				"Staff roles", "Ingredients and Distributor", "All Employee Details"});
		
		tableVBox = new VBox();
		tableVBox.setSpacing(5);
		tableVBox.setPadding(new Insets(10, 0, 0, 10));
				
		knownQueries.valueProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2){
				tableVBox.getChildren().clear();
				root = new StackPane();
				if (knownQueries.getSelectionModel().getSelectedIndex() == 0) {
					dataArray = MetaData.func.read("unimensa", new String[]{"location", "tables", "capacity"});
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 1) {
					dataArray = MetaData.func.read("dish", new String[]{"course", "name", "veg"});
					dataArray[0][2] = "vegetarian";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 2) {
					dataArray = MetaData.func.read("people", new String[]{"universityid","role","location","discount"});
					dataArray[0][0] = "university ID";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 3) {
					dataArray = MetaData.func.read("menuprice", new String[]{"discount","fullmenu","lightmenu","extralightmenu"});
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 4) {
					dataArray = MetaData.func.read("chef", new String[]{"id","licenceno"});
					dataArray[0][1] = "licence number";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 5) {
					dataArray = MetaData.func.read("staffmember", new String[]{"id","role"});
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 6) {
					dataArray = MetaData.func.read("select ingredient.name, ingredient.quantity, distributor.id, distributor.typeofprovision from ingredient, distributor, provided where ingredient.id = provided.ingredientid and provided.distributorid = distributor.id");
					dataArray[0][0]="ingredient";
					dataArray[0][2] = "distributor id";
					dataArray[0][3] = "type of provision";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 7) {
					dataArray = MetaData.func.read("select distinct employee.id, employee.contracttype, employee.firstname, employee.surname, chef.licenceno, staffmember.role, employee.dateofbirth, employee.address, employee.entryyear, employee.joblocation "
							+ "from (employee full join chef on employee.id = chef.id) full join staffmember on employee.id = staffmember.id");
					data = FXCollections.observableArrayList();
				}
				data.addAll(Arrays.asList(dataArray));
				data.remove(0);
				TableView<String[]> table = new TableView<>();
				for (int i = 0; i < dataArray[0].length; i++) {
					@SuppressWarnings("rawtypes")
					TableColumn tc = new TableColumn(dataArray[0][i]);
					final int colNo = i;
					tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
							return new SimpleStringProperty((p.getValue()[colNo]));
						}
					});
					tc.setPrefWidth(90);
					table.getColumns().add(tc);
				}
				table.setItems(data);
				root.getChildren().add(table);
				tableVBox.getChildren().add(root);
			}
		});
		insertionbox.getChildren().add(knownQueries);
		insertionbox.getChildren().add(tableVBox);
		return vbox;
	}
	
	private void update(){
		LoginActivity.admin.setContent(vbox);
	}
}
