
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class UserInterface extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("BorderPane");
		
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10, 20, 10, 20));
		
		//Need to import township list if this is what we are doing
		ObservableList<String> townships = FXCollections.observableArrayList("One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten");
		
		final ComboBox<String> searchBox = new ComboBox<String>(townships); //Search Box
		searchBox.setEditable(true);							   //Allows box to be edited
		searchBox.setPrefWidth(1000);
		
		
		FilteredList<String> filteredSearch = new FilteredList<String>(townships, p -> true);
		
		searchBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = searchBox.getEditor();
            final String selected = searchBox.getSelectionModel().getSelectedItem();
		
            // This needs run on the GUI thread to avoid the error described
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
		
		
		HBox dropDownHBox = new HBox();
		dropDownHBox.getChildren().add(searchBox);
		dropDownHBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(searchBox, Priority.ALWAYS);
		
		bp.setTop(dropDownHBox);
		
		Scene scene = new Scene(bp, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
