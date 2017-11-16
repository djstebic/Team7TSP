
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;

public class UserInterface extends Application {
	boolean allPop = false;
	boolean medAge = false;
	
	
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
		help.setTextAlignment(TextAlignment.CENTER);
		help.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Help Dialog");
				alert.setHeaderText("Help Dialog");
				
//				alert.setContentText("This page is for help while operating the program.\n"
//						+ "-Select townships from the search bar at the top.");

				try {
					String content = readFile("ReadMe.txt", StandardCharsets.UTF_8);
					alert.setContentText(content);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				alert.setResizable(true);
				alert.getDialogPane().setPrefSize(1000, 480);
				
				alert.showAndWait();
			}
		});
		
		
		
		HBox testoutput = new HBox();
		TableView<List<Object>> table = new TableView<List<Object>>();
		table.setEditable(true);
		table.setColumnResizePolicy((param) -> true);
		table.setPlaceholder(new Label("Please select a filter and type in name."));
		
		// List of buttons on left side (need list of things we want)
		ToggleButton population = new ToggleButton("Population");
		ToggleButton medAgeBySex = new ToggleButton("Median Age By Sex");
		ToggleButton income =  new ToggleButton("Income");
		ToggleButton housing = new ToggleButton("Housing");
		
		Button graph = new Button();
		ArrayList<String> columnNamesData = new ArrayList<>();
		
		
		//Go or Submit button
		Button go = new Button();
		go.setText("Go");
		go.setMinSize(36, 0);
		go.setTextAlignment(TextAlignment.CENTER);
		go.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				table.getColumns().clear();
				String searchText = searchBox.getValue();
				searchBox.getEditor().clear();
				Label printer = new Label();
				printer.setFont(Font.font("Courier New"));
				Statistics test = new Statistics();
				
				//New query method.
				String select = "distinct Migeo2010.NAME as Name, (select NAME from Migeo2010 where COUNTY in (select distinct COUNTY from Migeo2010 where NAME= ) and SUMLEV = \"050\") as [County]";
				String from = "((((Migeo2010 inner join SF1_00004 on (Migeo2010.LOGRECNO = SF1_00004.LOGRECNO)) inner join Migeo on (Migeo2010.NAME = Migeo.NAME)) inner join Mi00006 on (Migeo.LOGRECNO = Mi00006.LOGRECNO)) inner join SF1_00003 on (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO)) inner join SF1_00005 on (Migeo2010.LOGRECNO = SF1_00005.LOGRECNO)";
				if(medAgeBySex.isSelected()) {
					select += test.getMedianAgebySex();
				}
				if(population.isSelected()) {
					select += test.getAllPop();
				}
				if(income.isSelected() ){
					select += test.getAggregateHouseholdIncome();
				}
				if(housing.isSelected()) {
					select += test.getTotalHouseholds();
				}
				
				
				String query = "SELECT " + select + " FROM " + from + "WHERE NAME =";
				
				/*Old query method.
				if(population.isSelected() && medAgeBySex.isSelected())
					query =  test.getPopandMedAge();
				else if(medAgeBySex.isSelected())
					query = test.getMedianAgebySex();
				else if(population.isSelected())
					query = test.getAllPop();
				*/
				try {
				Statistics stat = new Statistics();
				 Stat stats = stat.getAllData(query, searchText);
				 
				 for(int i = 0; i < stats.getNumColumns(); i++) {
					 TableColumn<List<Object>, Object> column = new TableColumn<>(stats.getColumnName(i));
					 int columnIndex = i;
					 column.setCellValueFactory(cellData ->
					 new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
					 table.setColumnResizePolicy((param) -> true);
					 table.getColumns().add(column);
					 columnNamesData.add(column.getText());
				 }
//				 table.getItems().setAll(stats.getStat());
				 table.getItems().addAll(stats.getStat());
				 
				} catch (SQLException e2) {
					e2.printStackTrace();
					table.setPlaceholder(new Label("Invalid Input or No Filter Selected"));
				}
				
				
			}
		});

		searchBox.setOnKeyPressed( e -> {
			if(e.getCode() == KeyCode.ENTER) {
				go.fire();
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
		bottom.setAlignment(Pos.CENTER);
		Button clear = new Button("Clear");
		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				table.getColumns().clear();
				table.getItems().clear();
				
			}
			
		});
		testoutput.setSpacing(10);
		testoutput.setAlignment(Pos.CENTER);
		testoutput.setDisable(true);

		// VBox to select what goes in table/graph
		VBox attributes = new VBox();
		

		
		//Button  = new Button("");
		//Button  = new Button("");
		//attributes.getChildren().addAll(table);
		attributes.setPadding(new Insets(5, 10, 0, 0));
		attributes.setSpacing(5);
		
		//Adds all attributes to the VBox
		attributes.getChildren().add(population);
		attributes.getChildren().add(medAgeBySex);
		attributes.getChildren().add(income);
		attributes.getChildren().add(housing);
		
		bottom.getChildren().add(graph);
		bottom.setAlignment(Pos.BOTTOM_LEFT);
		//bottom.getChildren().add(clear);

		
		//Middle VBox for table and clear button.
		VBox vTable = new VBox();
		HBox hTable = new HBox();
		StackPane stack = new StackPane();
		stack.getChildren().add(clear);
		stack.setAlignment(Pos.BOTTOM_CENTER);
		hTable.getChildren().add(stack);
		hTable.setAlignment(Pos.BOTTOM_CENTER);
		vTable.getChildren().addAll(table, hTable);
		vTable.setVgrow(table, Priority.ALWAYS);
		
		//Chart Stuffs
		
		graph.setText("Graph");
		graph.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent arg0) {
				CategoryAxis xAxis = new CategoryAxis();
				NumberAxis yAxis = new NumberAxis();
				BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
				bc.setTitle("Comparison Chart");
				xAxis.setLabel("City/Township");       
		        yAxis.setLabel("Value");
		        
		        ObservableList<List<Object>> tData = table.getItems();
		        ArrayList<Object> tList = new ArrayList<>();
		        //tData.forEach((t) -> {tList.add(t);});
		        tData.forEach((t) -> {
		        	List<Object> a = t;
		        	int counter = tData.indexOf(t);
		        	XYChart.Series<String, Number> series = new XYChart.Series<>();
	        		series.setName((String) a.get(0));
		        	for(int i = 2; i < a.size(); i++) {
		        		series.getData().add(new XYChart.Data(columnNamesData.get(i), a.get(i)));
		        	}
		        	bc.getData().addAll(series);
		        	});
		        
		       
				Scene scene = new Scene(bc, 800, 600);
				Stage stage = new Stage();
				stage.setScene(scene);
		        stage.show();
		        
			}
			
		});
		
        
       
		
		// Sets things to areas of the border pane
		bp.setTop(dropDownHBox);
		bp.setLeft(attributes);
		bp.setBottom(bottom);
		bp.setCenter(vTable);
		
		
		// add locations for buttons and graphs
		Scene scene = new Scene(bp, 1000, 500);
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
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static String displayData(ResultSet toPrint) throws SQLException{
		ResultSetMetaData rsmd = toPrint.getMetaData();
		boolean go1 = true;
		boolean go2 = true;
		int columnsNumber = rsmd.getColumnCount();
		String out = "";
		for (int i = 1; i <= columnsNumber; i++) {
			out = out + rsmd.getColumnLabel(i).substring(0,((rsmd.getColumnLabel(i).length() < 15) ? rsmd.getColumnLabel(i).length() : 15));
			out = out + StringUtils.repeat(" ", 15 - rsmd.getColumnLabel(i).length());
			if(i != columnsNumber)
				out = out + " |   ";

		}
		out = out + "\n";
		while (toPrint.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				out = out + toPrint.getString(i).substring(0, ((toPrint.getString(i).length() < 15) ? toPrint.getString(i).length() : 15));
				out = out + StringUtils.repeat(" ", 15 - toPrint.getString(i).length());
				if(i != columnsNumber)
					out = out + " |   ";
			}
			out = out + "\n";
		}
		return out;
	}

	public static void main(String[] args) {
		Statistics test = new Statistics();
		
		launch(args);
	}

}
