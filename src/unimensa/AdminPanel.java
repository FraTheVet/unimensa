package unimensa;

import java.util.Arrays;

import javax.swing.JOptionPane;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;

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
	private VBox twBox;
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
	private VBox viewingbox;
	private VBox insertionbox;
	private VBox deletionbox;
	private VBox editingbox;
	private VBox replacementbox;
	private VBox insertionvbox;
	private VBox updateVBox;
	private String tableName = "";
	private String[] attributes = {};
	private VBox deleteVBox;
	private String key;
	private VBox customVBox;
	private VBox customvbox;

	public VBox start() {
		vbox = new VBox();
		adminText = new Text("Login to the DB Admin Panel");
		adminText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		vbox.getChildren().add(adminText);

		id = new Label("Username:");
		vbox.getChildren().add(id);
		idTextField = new TextField();
		idTextField.setPromptText("Example \"1234\"");
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
				String[][] staff = MetaData.func.read("employee", new String[] { "id", "surname" });
				String[] credentials = new String[] { idTextField.getText(),
						pwTextField.getText().toLowerCase() };
				logged = false;
				for (String[] memberCredentials : staff) {
					if (memberCredentials[0].equals(credentials[0])
							&& memberCredentials[1].toLowerCase().equals(credentials[1])) {
						logged = true;
						break;
					}
				}
				if (logged) {
					System.out.println("Successfully logged in to the Admin Panel");
					vbox = tabManagmentAdmin();
					update();
				} else {
					if (!vbox.getChildren().contains(failedLogin)) {
						vbox.getChildren().add(failedLogin);
					}
					pwTextField.setText("");
				}
			}
		});
		return vbox;
	}

	private VBox tabManagmentAdmin() {
		TabPane tabs = new TabPane();
		Tab viewingtab = new Tab("View Data");
		Tab insertiontab = new Tab("Insert Data");
		Tab deletiontab = new Tab("Delete Data");
		Tab editingtab = new Tab("Edit Data");
		Tab customtab = new Tab("Custom Write Query");
		tabs.getTabs().add(viewingtab);
		tabs.getTabs().add(insertiontab);
		tabs.getTabs().add(deletiontab);
		tabs.getTabs().add(editingtab);
		tabs.getTabs().add(customtab);
		tabs.setPadding(new Insets(10, 0, 10, 0));
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		viewingbox = tableViewAdmin();
		insertionbox = insertionViewAdmin();
		deletionbox = deleteViewAdmin();
		editingbox = updateViewAdmin();
		customvbox = customViewAdmin();
		replacementbox = new VBox();

		viewingtab.setContent(viewingbox);
		insertiontab.setContent(insertionbox);
		deletiontab.setContent(deletionbox);
		editingtab.setContent(editingbox);
		customtab.setContent(customvbox);

		viewingbox.setPrefWidth(500);
		viewingbox.setPrefHeight(500);

		replacementbox.getChildren().add(tabs);
		return replacementbox;
	}

	private VBox insertionViewAdmin() {
		insertionvbox = new VBox();
		insertionvbox.setPadding(new Insets(10, 0, 10, 0));
		insertionvbox.setSpacing(10);
		ComboBox<String> tablesdropdown = getDropDown();
		TextField inputvalues = new TextField();
		inputvalues.setPromptText("Please write the attributes");

		Button insertbutton = new Button();
		insertbutton.setText("Click Here to send the insertion");
		Label label = new Label();
		label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		insertionvbox.getChildren().add(tablesdropdown);
		insertionvbox.getChildren().add(inputvalues);
		insertionvbox.getChildren().add(label);
		insertionvbox.getChildren().add(insertbutton);

		insertbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String tablenameselected = tablesdropdown.getSelectionModel().getSelectedItem();
				int i = 0, count = 0;
				for (String retval : inputvalues.getText().split(",")) {
					count++;
				}

				String[] userinputattributes = new String[count];

				for (String retval : inputvalues.getText().split(",")) {
					userinputattributes[i] = retval;
					i++;
				}
				MetaData.func.insert(tablenameselected, attributes, userinputattributes);
			}
		});

		tablesdropdown.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				String tableName = "";
				attributes = null;
				String attributesstr = "Make the insertion look like this:\n";
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 0) {// unimensa
					tableName = MetaData.unimensa()[0];
					attributes = new String[MetaData.unimensa().length - 1];
					for (int a = 1; a < MetaData.unimensa().length; a++) {
						attributes[a - 1] = MetaData.unimensa()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 1) {// chef
					tableName = MetaData.chef()[0];
					attributes = new String[MetaData.chef().length - 1];
					for (int a = 1; a < MetaData.chef().length; a++) {
						attributes[a - 1] = MetaData.chef()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 2) {// dailymenu
					tableName = MetaData.dailymenu()[0];
					attributes = new String[MetaData.dailymenu().length - 1];
					for (int a = 1; a < MetaData.dailymenu().length; a++) {
						attributes[a - 1] = MetaData.dailymenu()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 3) {// dish
					tableName = MetaData.dish()[0];
					attributes = new String[MetaData.dish().length - 1];
					for (int a = 1; a < MetaData.dish().length; a++) {
						attributes[a - 1] = MetaData.dish()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 4) {// emplyee
					tableName = MetaData.employee()[0];
					attributes = new String[MetaData.employee().length - 1];
					for (int a = 1; a < MetaData.employee().length; a++) {
						attributes[a - 1] = MetaData.employee()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 5) {// ingredient
					tableName = MetaData.ingredient()[0];
					attributes = new String[MetaData.ingredient().length - 1];
					for (int a = 1; a < MetaData.ingredient().length; a++) {
						attributes[a - 1] = MetaData.ingredient()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 6) {// menuprice
					tableName = MetaData.menuprice()[0];
					attributes = new String[MetaData.menuprice().length - 1];
					for (int a = 1; a < MetaData.menuprice().length; a++) {
						attributes[a - 1] = MetaData.menuprice()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 7) {// staffmember
					tableName = MetaData.staffmember()[0];
					attributes = new String[MetaData.staffmember().length - 1];
					for (int a = 1; a < MetaData.staffmember().length; a++) {
						attributes[a - 1] = MetaData.staffmember()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 8) {// has
					tableName = MetaData.has()[0];
					attributes = new String[MetaData.has().length - 1];
					for (int a = 1; a < MetaData.has().length; a++) {
						attributes[a - 1] = MetaData.has()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 9) {// job
					tableName = MetaData.job()[0];
					attributes = new String[MetaData.job().length - 1];
					for (int a = 1; a < MetaData.job().length; a++) {
						attributes[a - 1] = MetaData.job()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 10) {// contains
					tableName = MetaData.contains()[0];
					attributes = new String[MetaData.contains().length - 1];
					for (int a = 1; a < MetaData.contains().length; a++) {
						attributes[a - 1] = MetaData.contains()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 11) {// provided
					tableName = MetaData.provided()[0];
					attributes = new String[MetaData.provided().length - 1];
					for (int a = 1; a < MetaData.provided().length; a++) {
						attributes[a - 1] = MetaData.provided()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 12) {// made
					tableName = MetaData.made()[0];
					attributes = new String[MetaData.made().length - 1];
					for (int a = 1; a < MetaData.made().length; a++) {
						attributes[a - 1] = MetaData.made()[a];
					}
				}
				for (int i = 0; i < attributes.length; i++) {
					attributesstr += attributes[i] + ", ";
				}
				label.setText(attributesstr.substring(0, attributesstr.length() - 2));
			}
		});

		return insertionvbox;
	}

	private VBox updateViewAdmin() {
		updateVBox = new VBox();
		updateVBox.setPadding(new Insets(10, 0, 10, 0));
		updateVBox.setSpacing(10);
		ComboBox<String> tablesdropdown = getDropDown();
		ComboBox<String> attributesDropDown = new ComboBox<String>();
		attributesDropDown.getItems().addAll(attributes);
		TextField updateValue = new TextField();
		updateValue.setPromptText("New attribute's value");
		TextField keyValue = new TextField();
		keyValue.setPromptText("Key value of the tuple you want to update");
		Button updateButton = new Button("Update Value");
		updateVBox.getChildren().add(tablesdropdown);
		updateVBox.getChildren().add(attributesDropDown);
		updateVBox.getChildren().add(updateValue);
		updateVBox.getChildren().add(keyValue);
		updateVBox.getChildren().add(updateButton);
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if ((!tableName.equals("")) && (!tableName.equals(null))
						&& (!attributes[0].equals("")) && (!attributes[0].equals(null))
						&& (!updateValue.getText().equals(""))
						&& (!updateValue.getText().equals(null))) {
					MetaData.func.update(tableName, attributes[0], keyValue.getText(),
							attributes[attributesDropDown.getSelectionModel().getSelectedIndex()],
							updateValue.getText());
				} else {
					JOptionPane.showMessageDialog(null,
							"All fields must be filled in order for the update function to work");
				}
			}
		});

		tablesdropdown.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 0) {// unimensa
					tableName = MetaData.unimensa()[0];
					attributes = new String[MetaData.unimensa().length - 1];
					for (int a = 1; a < MetaData.unimensa().length; a++) {
						attributes[a - 1] = MetaData.unimensa()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 1) {// chef
					tableName = MetaData.chef()[0];
					attributes = new String[MetaData.chef().length - 1];
					for (int a = 1; a < MetaData.chef().length; a++) {
						attributes[a - 1] = MetaData.chef()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 2) {// dailymenu
					tableName = MetaData.dailymenu()[0];
					attributes = new String[MetaData.dailymenu().length - 1];
					for (int a = 1; a < MetaData.dailymenu().length; a++) {
						attributes[a - 1] = MetaData.dailymenu()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 3) {// dish
					tableName = MetaData.dish()[0];
					attributes = new String[MetaData.dish().length - 1];
					for (int a = 1; a < MetaData.dish().length; a++) {
						attributes[a - 1] = MetaData.dish()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 4) {// emplyee
					tableName = MetaData.employee()[0];
					attributes = new String[MetaData.employee().length - 1];
					for (int a = 1; a < MetaData.employee().length; a++) {
						attributes[a - 1] = MetaData.employee()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 5) {// ingredient
					tableName = MetaData.ingredient()[0];
					attributes = new String[MetaData.ingredient().length - 1];
					for (int a = 1; a < MetaData.ingredient().length; a++) {
						attributes[a - 1] = MetaData.ingredient()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 6) {// menuprice
					tableName = MetaData.menuprice()[0];
					attributes = new String[MetaData.menuprice().length - 1];
					for (int a = 1; a < MetaData.menuprice().length; a++) {
						attributes[a - 1] = MetaData.menuprice()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 7) {// staffmember
					tableName = MetaData.staffmember()[0];
					attributes = new String[MetaData.staffmember().length - 1];
					for (int a = 1; a < MetaData.staffmember().length; a++) {
						attributes[a - 1] = MetaData.staffmember()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 8) {// has
					tableName = MetaData.has()[0];
					attributes = new String[MetaData.has().length - 1];
					for (int a = 1; a < MetaData.has().length; a++) {
						attributes[a - 1] = MetaData.has()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 9) {// job
					tableName = MetaData.job()[0];
					attributes = new String[MetaData.job().length - 1];
					for (int a = 1; a < MetaData.job().length; a++) {
						attributes[a - 1] = MetaData.job()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 10) {// contains
					tableName = MetaData.contains()[0];
					attributes = new String[MetaData.contains().length - 1];
					for (int a = 1; a < MetaData.contains().length; a++) {
						attributes[a - 1] = MetaData.contains()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 11) {// provided
					tableName = MetaData.provided()[0];
					attributes = new String[MetaData.provided().length - 1];
					for (int a = 1; a < MetaData.provided().length; a++) {
						attributes[a - 1] = MetaData.provided()[a];
					}
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 12) {// made
					tableName = MetaData.made()[0];
					attributes = new String[MetaData.made().length - 1];
					for (int a = 1; a < MetaData.made().length; a++) {
						attributes[a - 1] = MetaData.made()[a];
					}
				}
				attributesDropDown.getItems().clear();
				attributesDropDown.getItems().addAll(attributes);
			}
		});

		return updateVBox;
	}

	private VBox deleteViewAdmin() {
		deleteVBox = new VBox();
		deleteVBox.setPadding(new Insets(10, 0, 10, 0));
		deleteVBox.setSpacing(10);
		ComboBox<String> tablesdropdown = getDropDown();
		TextField keyValue = new TextField();
		keyValue.setPromptText("Key value of the tuple you want to delete");
		Button deleteButton = new Button("Delete Value");
		deleteVBox.getChildren().add(tablesdropdown);
		deleteVBox.getChildren().add(keyValue);
		deleteVBox.getChildren().add(deleteButton);
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if ((!tableName.equals("")) && (!tableName.equals(null)) && (!key.equals(""))
						&& (!key.equals(null)) && (!keyValue.getText().equals(""))
						&& (!keyValue.getText().equals(null))) {
					MetaData.func.delete(tableName, key, keyValue.getText());
				} else {
					JOptionPane.showMessageDialog(null,
							"All fields must be filled in order for the update function to work");
				}
			}
		});

		tablesdropdown.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 0) {// unimensa
					tableName = MetaData.unimensa()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 1) {// chef
					tableName = MetaData.chef()[0];
					key = MetaData.unimensa()[1];

				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 2) {// dailymenu
					tableName = MetaData.dailymenu()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 3) {// dish
					tableName = MetaData.dish()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 4) {// emplyee
					tableName = MetaData.employee()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 5) {// ingredient
					tableName = MetaData.ingredient()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 6) {// menuprice
					tableName = MetaData.menuprice()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 7) {// staffmember
					tableName = MetaData.staffmember()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 8) {// has
					tableName = MetaData.has()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 9) {// job
					tableName = MetaData.job()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 10) {// contains
					tableName = MetaData.contains()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 11) {// provided
					tableName = MetaData.provided()[0];
					key = MetaData.unimensa()[1];
				}
				if (tablesdropdown.getSelectionModel().getSelectedIndex() == 12) {// made
					tableName = MetaData.made()[0];
					key = MetaData.unimensa()[1];
				}
			}
		});

		return deleteVBox;
	}

	public VBox customViewAdmin() {
		customVBox = new VBox();
		customVBox.setPadding(new Insets(10, 0, 10, 0));
		customVBox.setSpacing(10);
		TextField customQuery = new TextField();
		customQuery.setPromptText("type your custom write query here");
		Button customButton = new Button("Send Write Query");
		customVBox.getChildren().add(customQuery);
		customVBox.getChildren().add(customButton);
		customButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (customQuery.getText().toUpperCase().contains("SELECT")) {
					JOptionPane.showMessageDialog(null,
							"This tab is just for writing purposes, put a write query");
				} else {
					MetaData.func.customSendQuery(customQuery.getText());
					customQuery.setText("");
				}
			}
		});
		return customVBox;
	}

	private ComboBox<String> getDropDown() {
		ComboBox<String> dropdown = new ComboBox<String>();
		dropdown.setPromptText("Select any table:");
		dropdown.getItems().addAll(
				new String[] { MetaData.unimensa()[0], MetaData.chef()[0], MetaData.dailymenu()[0],
						MetaData.dish()[0], MetaData.employee()[0], MetaData.ingredient()[0],
						MetaData.menuprice()[0], MetaData.staffmember()[0], MetaData.has()[0],
						MetaData.job()[0], MetaData.contains()[0], MetaData.provided()[0],
						MetaData.made()[0] });
		return dropdown;
	}

	// this one is finished
	private VBox tableViewAdmin() {
		twBox = new VBox();
		twBox.setPadding(new Insets(10, 0, 10, 0));

		knownQueries = new ComboBox<String>();
		knownQueries.setPromptText("See any of the available tables!");
		knownQueries.getItems().addAll(
				new String[] { "Unimensa Locations", "Dishes Offered", "University People",
						"Menu Prices", "Chef licence", "Staff roles",
						"Ingredients and Distributor", "All Employee Details" });

		tableVBox = new VBox();
		tableVBox.setSpacing(5);
		tableVBox.setPadding(new Insets(10, 0, 10, 0));

		twBox.getChildren().add(knownQueries);
		twBox.getChildren().add(tableVBox);

		knownQueries.valueProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				tableVBox.getChildren().clear();
				root = new StackPane();
				if (knownQueries.getSelectionModel().getSelectedIndex() == 0) {
					dataArray = MetaData.func.read("unimensa", new String[] { "location", "tables",
							"capacity" });
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 1) {
					dataArray = MetaData.func
							.read("dish", new String[] { "course", "name", "veg" });
					dataArray[0][2] = "vegetarian";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 2) {
					dataArray = MetaData.func.read("people", new String[] { "universityid", "role",
							"location", "discount" });
					dataArray[0][0] = "university ID";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 3) {
					dataArray = MetaData.func.read("menuprice", new String[] { "discount",
							"fullmenu", "lightmenu", "extralightmenu" });
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 4) {
					dataArray = MetaData.func.read("chef", new String[] { "id", "licenceno" });
					dataArray[0][1] = "licence number";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 5) {
					dataArray = MetaData.func.read("staffmember", new String[] { "id", "role" });
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 6) {
					dataArray = MetaData.func
							.read("select ingredient.name, ingredient.quantity, distributor.id, distributor.typeofprovision from ingredient, distributor, provided where ingredient.id = provided.ingredientid and provided.distributorid = distributor.id");
					dataArray[0][0] = "ingredient";
					dataArray[0][2] = "distributor id";
					dataArray[0][3] = "type of provision";
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 7) {
					dataArray = MetaData.func
							.read("select distinct employee.id, employee.contracttype, employee.firstname, employee.surname, chef.licenceno, staffmember.role, employee.dateofbirth, employee.address, employee.entryyear, employee.joblocation "
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
		// update();
		return twBox;
	}

	private void update() {
		LoginActivity.admin.setContent(vbox);
	}
}