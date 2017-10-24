
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserInterface extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("BorderPane");

		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10, 20, 10, 20));

		// Need to import township list if this is what we are doing
		ObservableList<String> townships = townships();

		final ComboBox<String> searchBox = new ComboBox<String>(townships); // Search
																			// Box
		searchBox.setEditable(true); // Allows box to be edited
		searchBox.setPrefWidth(1000);

		FilteredList<String> filteredSearch = new FilteredList<String>(townships, p -> true);

		searchBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			final TextField editor = searchBox.getEditor();
			final String selected = searchBox.getSelectionModel().getSelectedItem();

			// This needs to run on the GUI thread to avoid the error described
			// here: https://bugs.openjdk.java.net/browse/JDK-8081700.
			Platform.runLater(() -> {
				// If the no item in the list is selected or the selected item
				// isn't equal to the current input, we refilter the list.
				if (selected == null || !selected.equals(editor.getText())) {
					filteredSearch.setPredicate(item -> {
						// We return true for any items that starts with the
						// same letters as the input. We use toUpperCase to
						// avoid case sensitivity.
						if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
							return true;
						} else {
							return false;
						}
					});
				}
			});
		});
		searchBox.setItems(filteredSearch);

		// Help button
		Button help = new Button();
		help.getStyleClass().add("button-blue");
		help.setText("?");
		help.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Help Dialog");
				alert.setHeaderText("Help Dialog");
				alert.setContentText("This page is for help while operating the program.\n"
						+ "-Select townships from the search bar at the top.");

				alert.showAndWait();
			}
		});
		
		
		HBox testoutput = new HBox();
		TableView<Stat> table = new TableView<Stat>();
		table.setEditable(true);
		
		// Test button for SQL
		Button test = new Button();
		test.setText("Test");
		test.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Statistics test = new Statistics();
				String query = test.getTotalPop();
				ResultSet result = test.runQuery(query, "Crysal Falls township");
				try {
					String output = null;
					ResultSetMetaData rsmd = result.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					ObservableList<Stat> valueList = FXCollections.observableArrayList();
					
					while (result.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							//String value = result.getString(i);
							
							//System.out.println(value);
							valueList.add(new Stat(result.getString(i)));
							String columnName = rsmd.getColumnName(i);

							TableColumn<Stat, String> column = new TableColumn<Stat, String>(columnName);
							column.setCellValueFactory(new PropertyValueFactory<Stat, String>("stat"));
							table.setItems(valueList);
							table.getColumns().addAll(column);

						}
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		});

		// HBox at top of border pane
		HBox dropDownHBox = new HBox();
		dropDownHBox.getChildren().add(searchBox);
		dropDownHBox.getChildren().add(help);
		dropDownHBox.setAlignment(Pos.CENTER);
		HBox.setMargin(searchBox, new Insets(0, 10, 0, 10));
		help.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(searchBox, Priority.ALWAYS);

		// HBox at bottom of border pane.
		HBox bottom = new HBox();
		bottom.getChildren().add(test);
		bottom.setAlignment(Pos.CENTER);

		// Output for testing
			//Label label1 = new Label("Output:");
			//testoutput.getChildren().addAll(label1);
		
		testoutput.setSpacing(10);
		testoutput.setAlignment(Pos.CENTER);
		testoutput.setDisable(true);

		// VBox to select what goes in table/graph
		VBox attributes = new VBox();
		
		// List of buttons on left side (need list of things we want)
		//Button att1 = new Button("att1");
		//attributes.getChildren().addAll(table);
		//attributes.setPadding(new Insets(5, 0, 0, 0));

		// Sets things to areas of the border pane
		bp.setTop(dropDownHBox);
		bp.setLeft(attributes);
		bp.setBottom(bottom);
		bp.setCenter(table);
		// add locations for buttons and graphs

		Scene scene = new Scene(bp, 500, 500);
		scene.getStylesheets().add("CSS.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private ObservableList<String> townships() {
		Statistics ins = new Statistics();
		ResultSet out = ins.getTownships();
		ObservableList<String> townships = FXCollections.observableArrayList();
		try {
			ResultSetMetaData rsmd = out.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (out.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					String name = out.getString(i);
					townships.add(name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return townships;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
