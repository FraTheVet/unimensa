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

		return scene;
	}
}