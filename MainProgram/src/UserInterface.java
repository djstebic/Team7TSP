
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
		Text printer = new Text();
		printer.setFont(Font.font("Courier New"));
		test.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Statistics test = new Statistics();
				String query = test.getAllPop();
				ResultSet result = test.runQuery(query, "Crystal Falls township");
				try {
					printer.setText(displayData(result));
					InteractWithDatabase st = new InteractWithDatabase();
					System.out.println(displayData(result));
							
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				try {
//					String output = null;
//					ResultSetMetaData rsmd = result.getMetaData();
//					int columnsNumber = rsmd.getColumnCount();
//					ObservableList<Stat> valueList = FXCollections.observableArrayList();
//					while (result.next()) {
//						for (int i = 1; i <= columnsNumber; i++) {
//							//String value = result.getString(i);
//							
//							//System.out.println(value);
//							valueList.add(new Stat(result.getString(i)));
//							String columnName = rsmd.getColumnName(i);
//
//							TableColumn<Stat, String> column = new TableColumn<Stat, String>(columnName);
//							column.setCellValueFactory(new PropertyValueFactory<Stat, String>("stat"));
//							table.setItems(valueList);
//							table.getColumns().addAll(column);
//
//						}
//					}
//				} catch (SQLException e) {
//
//					e.printStackTrace();
//				}

			}

		});
		//Go or Submit button
		Button go = new Button();
		go.setText("Go");
		go.setMinSize(35, 0);
		go.setTextAlignment(TextAlignment.CENTER);
		go.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String searchText = searchBox.getValue();
				searchBox.setValue("");
				System.out.println(searchText);
			}
		});

		// HBox at top of border pane
		HBox dropDownHBox = new HBox();
		dropDownHBox.getChildren().add(help);
		dropDownHBox.getChildren().add(searchBox);
		dropDownHBox.getChildren().add(go);
		dropDownHBox.setAlignment(Pos.CENTER);
		HBox.setMargin(searchBox, new Insets(10, 10, 10, 10));
		help.setAlignment(Pos.CENTER_LEFT);
		go.setAlignment(Pos.CENTER_RIGHT);
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
		Button population = new Button("Population");
		Button medAgeBySex = new Button("Median Age By Sex");
		//Button  = new Button("");
		//Button  = new Button("");
//		attributes.getChildren().addAll(table);
		attributes.getChildren().addAll(printer);
		attributes.setPadding(new Insets(5, 10, 0, 0));
		attributes.setSpacing(5);
		
		//Adds all attributes to the VBox
		attributes.getChildren().add(population);
		attributes.getChildren().add(medAgeBySex);

		// Sets things to areas of the border pane
		bp.setTop(dropDownHBox);
		bp.setLeft(attributes);
		bp.setBottom(bottom);
//		bp.setCenter(table);
		bp.setCenter(printer);
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
	
	public static String displayData(ResultSet toPrint) throws SQLException{
		ResultSetMetaData rsmd = toPrint.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		String out = "";
		for (int i = 1; i <= columnsNumber; i++) {
			out = out + rsmd.getColumnName(i).substring(0,((rsmd.getColumnName(i).length() < 10) ? rsmd.getColumnName(i).length() : 10));
			out = out + StringUtils.repeat(" ", 15 - rsmd.getColumnName(i).length());
		}
		out = out + "\n";
		while (toPrint.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				out = out + toPrint.getString(i).substring(0, ((toPrint.getString(i).length() < 10) ? toPrint.getString(i).length() : 10));
				out = out + StringUtils.repeat(" ", 15 - toPrint.getString(i).length());
			}
			out = out + "\n";
		}
		return out;
	}

	public static void main(String[] args) {
		Statistics test = new Statistics();
		try {
			displayData(test.runQuery(test.getAllPop(),"Crystal Falls township"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}

}
