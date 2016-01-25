package unimensa;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.VBox;

public class LoginActivity {

	private Scene scene;
	private TabPane tabs;
	private Tab people;
	public static Tab admin;
	private VBox peopleVBox;
	private VBox adminVBox;
//	private ComboBox<String> knownQueries;
//	private String[][] dataArray;
//	private StackPane root;
//	private ObservableList<String[]> data;
//	private String[] titles;
//	private Text adminText;
//	private Text failedLogin;
//	private Label id;
//	private Label pw;
//	private TextField idTextField;
//	private TextField pwTextField;
//	private Button login;
//	private HBox hbLogin;
//	private GridPane grid;
//	private ColumnConstraints cc;
//	private MainWindow accessFunc;
	
	public Scene start() {
		tabs = new TabPane();
		scene = new Scene(tabs, 500, 600);

		people = new Tab("Student/Professor/academic Staff");
		admin = new Tab("Administrator");
		tabs.getTabs().add(people);
		tabs.getTabs().add(admin);
		tabs.setPadding(new Insets(10, 10, 10, 10));
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		PeoplePanel peoplepanel = new PeoplePanel();
		peopleVBox = peoplepanel.start();
		
		AdminPanel adminpanel = new AdminPanel();
		adminVBox = adminpanel.start();
		
		peopleVBox.setPadding(new Insets(10, 5, 10, 5));
		adminVBox.setPadding(new Insets(10, 5, 10, 5));
		people.setContent(peopleVBox);
		admin.setContent(adminVBox);

//		VBox tableVBox = new VBox();
//		tableVBox.setSpacing(5);
//		tableVBox.setPadding(new Insets(10, 0, 0, 10));

//		knownQueries = new ComboBox<String>();
//		knownQueries.setPromptText("Select something you want to see");
//		knownQueries.getItems().add("UniMensa Table");
//		knownQueries.getItems().add("People Table");
//		knownQueries.getItems().add("test Table");

//		adminText = new Text("Chose the DB you want to log in to");
//		adminText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
//		adminVBox.getChildren().add(adminText);
//
//		failedLogin = new Text();
//		failedLogin.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
//		failedLogin.setFill(Color.RED);
//
//		id = new Label("Username:");
//		adminVBox.getChildren().add(id);
//		idTextField = new TextField();
//		adminVBox.getChildren().add(idTextField);
//		pw = new Label("Password:");
//		adminVBox.getChildren().add(pw);
//		pwTextField = new PasswordField();
//		adminVBox.getChildren().add(pwTextField);
//
//		login = new Button("Sign in");
//		login.setDefaultButton(true);
//		hbLogin = new HBox(10);
//		hbLogin.setAlignment(Pos.BOTTOM_RIGHT);
//		hbLogin.getChildren().add(login);
//		adminVBox.getChildren().add(hbLogin);
//		
//		login.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				String[][] staff = MetaData.func.read("employee", new String[]{"id", "surname"});
//				String[] credentials = new String[] { idTextField.getText(), pwTextField.getText() };
//				boolean logged = false;
//				for (String[] memberCredentials : staff) {
//					if (memberCredentials[0].equals(credentials[0])
//							&& memberCredentials[1].equals(credentials[1])) {
//						logged = true;
//						break;
//					}
//				}
//				if (logged){
//					System.out.println("Successfully logged in to the Admin Panel");
//				}
//				else{
//					System.out.println("Wrong Username/Password, try again.");
//				}
//			}
//		});
		//peopleVBox.getChildren().add(/*knownQueries*/ peoplepanel.start());
		//peopleVBox.getChildren().add(tableVBox);

//		knownQueries.valueProperty().addListener(new ChangeListener<String>() {
//			@Override
//			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
//				tableVBox.getChildren().clear();
//				root = new StackPane();
//				if (knownQueries.getSelectionModel().getSelectedIndex() == 0) {
//					dataArray = MetaData.func.read("unimensa", "location, tables, capacity");
//					data = FXCollections.observableArrayList();
//					titles = new String[] { "location", "tables", "capacity" };
//				}
//				if (knownQueries.getSelectionModel().getSelectedIndex() == 1) {
//					dataArray = MetaData.func.read("people", "role, location, discount");
//					data = FXCollections.observableArrayList();
//					titles = new String[] { "people", "role", "location", "discount" };
//				}
//				if (knownQueries.getSelectionModel().getSelectedIndex() == 2) {
//					dataArray = MetaData.func.read("people", "role, location, discount");
//					data = FXCollections.observableArrayList();
//					titles = new String[] { "people", "role", "location", "discount" };
//				}
//				data.add(titles);
//				data.addAll(Arrays.asList(dataArray));
//				data.remove(0);
//				TableView<String[]> table = new TableView<>();
//				for (int i = 0; i < dataArray[0].length; i++) {
//					TableColumn tc = new TableColumn(titles[i]);
//					final int colNo = i;
//					tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
//						@Override
//						public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
//							return new SimpleStringProperty((p.getValue()[colNo]));
//						}
//					});
//					tc.setPrefWidth(90);
//					table.getColumns().add(tc);
//				}
//				table.setItems(data);
//				root.getChildren().add(table);
//				tableVBox.getChildren().add(root);
//			}
//		});
		return scene;
	}
}