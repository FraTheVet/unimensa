package unimensa;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PeoplePanel {
	private VBox vbox;
	private VBox tableVBox;
	private ComboBox<String> knownQueries;
	private StackPane root;
	private String[][] dataArray;
	private ObservableList<String[]> data;
	private Calendar calendar;
	private int night;
	private String ingredient;

	public VBox start() {
		calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		vbox = new VBox();
		tableVBox = new VBox();
		tableVBox.setSpacing(5);
		tableVBox.setPadding(new Insets(10, 0, 0, 10));

		knownQueries = new ComboBox<String>();
		knownQueries.setPromptText("Select something you want to see");
		knownQueries.getItems().add("Mensa details");
		knownQueries.getItems().add("Prices");
		knownQueries.getItems().add("Day Dishes");
		knownQueries.getItems().add("Week Dishes");
		knownQueries.getItems().add("Ingredients of a specific plate");
		knownQueries.getItems().add("Distributors of a specific plate");

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
					dataArray = MetaData.func.read("menuprice", new String[] { "discount",
							"fullmenu", "lightmenu", "extralightmenu" });
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 2) {
					try {
						night = Integer.parseInt(JOptionPane
								.showInputDialog("Type 0 for Lunch or 1 for Dinner"));
						if (night != 0 && night != 1) {
							JOptionPane.showMessageDialog(null, "Input not valid, default 0 set");
							night = 0;
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Input not valid, default 0 set");
						night = 0;
					}

					dataArray = MetaData.func
							.read("select dish.name from dish, dailymenu, contains where dish.id = contains.iddish and dailymenu.id = contains.iddaily and dailymenu.dow = "
									+ calendar.DAY_OF_WEEK
									+ " and dailymenu.wom = "
									+ calendar.WEEK_OF_MONTH + " and dailymenu.night = " + night);
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 3) {
					dataArray = MetaData.func
							.read("select dailymenu.dow, dailymenu.night, dish.name from dish, dailymenu, contains where dish.id = contains.iddish and dailymenu.id = contains.iddaily and dailymenu.wom = "
									+ calendar.WEEK_OF_MONTH);
					data = FXCollections.observableArrayList();
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 4) {
					ingredient = JOptionPane
							.showInputDialog("Which dish do you want to get the ingredients of?");
					ingredient = ingredient.toLowerCase();
					dataArray = MetaData.func
							.read("select distinct ingredient.name from distributor, dish, ingredient, made, provided where ingredient.id = made.iding and dish.id = made.iddish and dish.name = '"
									+ ingredient + "'");
					data = FXCollections.observableArrayList();
					if (dataArray.length < 2) {
						JOptionPane.showMessageDialog(null, "No data found for ingredient "
								+ ingredient + ".\n\n Showing nothing.");
					}
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 5) {
					ingredient = JOptionPane
							.showInputDialog("Which dish do you want to get the distributors of?");
					ingredient = ingredient.toLowerCase();
					dataArray = MetaData.func
							.read("select distinct ingredient.name, distributor.id, distributor.typeofprovision from distributor, dish, ingredient, made, provided where ingredient.id = made.iding and dish.id = made.iddish and ingredient.id = provided.ingredientid and distributor.id = provided.distributorid and dish.name = '"
									+ ingredient + "'");
					data = FXCollections.observableArrayList();
					if (dataArray.length < 2) {
						JOptionPane.showMessageDialog(null, "No data found for ingredient "
								+ ingredient + ".\n\n Showing nothing.");
					}
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
		vbox.getChildren().add(knownQueries);
		vbox.getChildren().add(tableVBox);
		return vbox;
	}
}
