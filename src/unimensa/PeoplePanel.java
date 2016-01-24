package unimensa;

import java.util.Arrays;

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

public class PeoplePanel{
	private VBox vbox;
	private VBox tableVBox;
	private ComboBox<String> knownQueries;
	private StackPane root;
	private String[][] dataArray;
	private ObservableList<String[]> data;
	private String[] titles;
	
	/**
	 * @return
	 */
	public VBox start(){
		vbox = new VBox();
		System.out.println("fajshdfkl");
		tableVBox = new VBox();
		tableVBox.setSpacing(5);
		tableVBox.setPadding(new Insets(10, 0, 0, 10));
		
		knownQueries = new ComboBox<String>();
		knownQueries.setPromptText("Select something you want to see");
		knownQueries.getItems().add("UniMensa Table");
		knownQueries.getItems().add("People Table");
		knownQueries.getItems().add("test Table");
		
		knownQueries.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				tableVBox.getChildren().clear();
				root = new StackPane();
				if (knownQueries.getSelectionModel().getSelectedIndex() == 0) {
					dataArray = MetaData.func.read("unimensa", "location, tables, capacity");
					data = FXCollections.observableArrayList();
					titles = new String[] { "location", "tables", "capacity" };
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 1) {
					dataArray = MetaData.func.read("people", "role, location, discount");
					data = FXCollections.observableArrayList();
					titles = new String[] { "people", "role", "location", "discount" };
				}
				if (knownQueries.getSelectionModel().getSelectedIndex() == 2) {
					dataArray = MetaData.func.read("people", "role, location, discount");
					data = FXCollections.observableArrayList();
					titles = new String[] { "people", "role", "location", "discount" };
				}
				data.add(titles);
				data.addAll(Arrays.asList(dataArray));
				data.remove(0);
				TableView<String[]> table = new TableView<>();
				for (int i = 0; i < dataArray[0].length; i++) {
					TableColumn tc = new TableColumn(titles[i]);
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
