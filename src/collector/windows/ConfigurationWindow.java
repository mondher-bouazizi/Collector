package collector.windows;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

import collector.engine.Parameters;
import collector.engine.WindowsManager;
import collector.extra.windows.AlertBox;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class ConfigurationWindow implements Initializable {
	
	//====================================//
	//            ENUMERATIONS            //
	//====================================//
	
	private enum ErrorCode {
		big_number,
		no_number,
		no_keyword,
		action,
		fine
	}
	
	//====================================//
	//        NON-FXML COMPONENTS         //
	//====================================//
	
	private static TreeMap<String, HBox> keywordsLabels;
	private static IntegerProperty countOfKeywords;
	private static GridPane keywordsInnerLayout;
	
	//====================================//
	//          FXML COMPONENTS           //
	//====================================//

	@FXML TextField numberOfTweets;
	@FXML TextField keyword;
	
	@FXML Button addButton;
	@FXML Button clearButton;
	@FXML Button startButton;
	
	@FXML ToggleGroup saveGroup;
	@FXML RadioButton saveToFile;
	@FXML RadioButton displayOnScreen;
	
	@FXML VBox keywordsLayout;
	
	@FXML ChoiceBox<String> languageChoiceBox;
	
	
	//====================================//
	//       FXML COMPONENTS ACTIONS      //
	//====================================//
	
	@FXML public void handleAddButton() {
		System.out.println("Add Button clicked");
		if(!keyword.getText().equals("") && !keywordsLabels.containsKey(keyword.getText().toLowerCase())) {
			HBox labelToAdd = createKeyWord(keyword.getText().toLowerCase());
			keywordsLabels.put(keyword.getText().toLowerCase(), labelToAdd);
			countOfKeywords.set(countOfKeywords.intValue() + 1);
			keyword.clear();
			System.out.println("class added!");
		}
	}
	
	@FXML public void handleClearButton() {
		System.out.println("Clear Button clicked");
		keywordsLabels.clear();
		countOfKeywords.set(0);
	}
	
	@FXML public void handleStartButton() {
		System.out.println("Clear Button clicked");
		if (isGoodParameters().equals(ErrorCode.big_number)) {
			AlertBox.display("Too big number", "Please choose a number of tweets to collect that is less than 100,000", "OK");
		} else if (isGoodParameters().equals(ErrorCode.no_number)) {
			AlertBox.display("Specify the number of tweets to collect", "Please Specify the number of tweets to collect", "OK");
		} else if (isGoodParameters().equals(ErrorCode.no_keyword)) {
			AlertBox.display("Specify your keyword(s)", "Please choose at least one keyword to collect the tweets", "OK");
		} else if (isGoodParameters().equals(ErrorCode.action)) {
			AlertBox.display("Choose what to do with the collected tweets", "Please choose whether you want to save the tweets in a file or display them", "OK");
		} else if (isGoodParameters().equals(ErrorCode.fine)) {
			saveParameters();
			if (saveToFile.isSelected()) {
				WindowsManager.saveToFileWindow();
				WindowsManager.configurationStage.close();
			} else if (displayOnScreen.isSelected()) {
				WindowsManager.displayOnScreenWindow();
				WindowsManager.configurationStage.close();
			}
		}
		
		
	}
	
	
	//====================================//
	//           INITIALIZATION           //
	//====================================//

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Initialize language ChoiceBox
		languageChoiceBox.getItems().addAll("all", "English","French");
		languageChoiceBox.getSelectionModel().select("all");
		
		// Initializes the parameters
		Parameters.setTweets(new ArrayList<>());
		Parameters.setCountOfTweets(new SimpleIntegerProperty(0));
		Parameters.setActiveTweet(null);
		Parameters.setKeywords(new ArrayList<>());
		
		// Related to the number of tweets
		numberOfTweets.textProperty().addListener((observable, oldValue, newValue) -> {
			if(!newValue.equals(oldValue)) {
				numberOfTweets.setText(numberOfTweets.getText().replaceAll("[^0-9]", ""));
			}
			
		});

		// Related to the keywords
		keywordsLabels = new TreeMap<>();
		countOfKeywords = new SimpleIntegerProperty(0);
		
		keywordsInnerLayout = new GridPane();
		keywordsInnerLayout.setMinWidth(480);
		keywordsInnerLayout.setMaxWidth(480);
		keywordsInnerLayout.setMinHeight(100);
		keywordsInnerLayout.setMaxWidth(100);
		keywordsInnerLayout.setHgap(2);
		keywordsInnerLayout.setVgap(2);
		final int numCols = 4 ;
        final int numRows = 4 ;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            keywordsInnerLayout.getColumnConstraints().add(colConst);
        }
        
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            keywordsInnerLayout.getRowConstraints().add(rowConst);         
        }
        
        keywordsLayout.getChildren().add(keywordsInnerLayout);
		
		// Listener to che change of count of key words entered
        ReadOnlyIntegerProperty.readOnlyIntegerProperty(countOfKeywords).addListener((v, oldValue, newValue) -> {
        	rearrangeKeywords(keywordsInnerLayout, 4, 4, keywordsLabels);
			if (!newValue.equals(16)) {
				addButton.setDisable(false);
			} else {
				addButton.setDisable(true);
			}
			System.out.println("oldValue:" + oldValue + ", newValue = " + newValue);
		});
        
        // Keys pressing
        keyword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					keyEvent.consume();
					addButton.fire();
				}
			}
		});
	}
	
	
	//====================================//
	//           PRIVATE METHODS          //
	//====================================//
	
	/**
	 * Creates the label that will be added to the keywords
	 * @param keyword
	 * @return
	 */
	private static HBox createKeyWord(String keyword) {
		Button closeButton = new Button("X");
		closeButton.setStyle("-fx-font-size: 5pt; -fx-text-fill:red;");
		Label label = new Label(keyword);
		label.setGraphic(closeButton);
		label.setContentDisplay(ContentDisplay.RIGHT);

		HBox keywordRemoveLayout = new HBox(label);
		keywordRemoveLayout.setAlignment(Pos.CENTER);
		keywordRemoveLayout.setStyle("-fx-padding: 1;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 1;" +
                "-fx-border-insets: 1;" + 
                "-fx-border-radius: 1;" + 
                "-fx-border-color: gray;");
		closeButton.setOnAction(event -> {
			keywordRemoveLayout.getChildren().remove(label);
			keywordsLabels.remove(keyword);
			countOfKeywords.set(countOfKeywords.get()-1);
		});
		return keywordRemoveLayout;
	}
	
	/**
	 * Rearrange the keywords in the grid (after adding one keyword)
	 * @param layout
	 * @param numberOfColumns
	 * @param NumberOfLines
	 * @param kwLabels
	 */
	private static void rearrangeKeywords(GridPane layout, int numberOfColumns, int NumberOfLines, TreeMap<String, HBox> kwLabels) {
		
		int counter = 0;
		Integer i = 0;
		Integer j = 0;
		
		layout.getChildren().clear();
		
		for (String key : kwLabels.keySet()) {
			
			layout.add(kwLabels.get(key), i, j);
			
			counter++;
			i++;
			if (i%(Integer)numberOfColumns==0) {
				i = 0;
				j++;
			}
			if (counter == numberOfColumns * NumberOfLines) {
				break;
			}
 		}
	}
	
	/**
	 * Checks whether all the parameters were entered correctly
	 * @return
	 */
	private ErrorCode isGoodParameters() {
		// Too big number
		if (numberOfTweets.getText()!=null && !numberOfTweets.getText().equals("")) {
			if (Integer.parseInt(numberOfTweets.getText())>100000) {
				return ErrorCode.big_number;
			}
		}
		
		// No number
		if (numberOfTweets.getText().equals("") || Integer.parseInt(numberOfTweets.getText())==0) {
			return ErrorCode.no_number;
		}
		
		// No keyword
		if (keywordsLabels.isEmpty()) {
			return ErrorCode.no_keyword;
		}
		
		// No action chosen
		if (!saveToFile.isSelected() && !displayOnScreen.isSelected()) {
			return ErrorCode.action;
		}
		
		// Everything is fine
		return ErrorCode.fine;
	}
	
	/**
	 * Save the parameters [Number of tweets and keywords]
	 */
	private void saveParameters() {
		ArrayList<String> keywords = new ArrayList<>();
		for (String cla : keywordsLabels.keySet()) {
			keywords.add(cla);
		}
		Parameters.setKeywords(keywords);
		Parameters.setNumberOfTweetsToCollect(Integer.parseInt(numberOfTweets.getText()));
		Parameters.setLanguage(languageChoiceBox.getSelectionModel().getSelectedItem());
	}
	

}
