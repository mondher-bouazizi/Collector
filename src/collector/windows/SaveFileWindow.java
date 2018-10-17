package collector.windows;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import collector.engine.Collector;
import collector.engine.Parameters;
import collector.engine.WindowsManager;
import collector.extra.windows.ConfirmBox;
import collector.io.Reader;
import collector.items.Tweet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SaveFileWindow implements Initializable {
	
	// =====================================//
	//            FXML Components           //
	// =====================================//
	
	@FXML TextField outputFile;
	
	@FXML Label fileStatus;
	@FXML Label tweetsToCollect;
	@FXML Label tweetsCollected;
	
	@FXML HBox tableLayout;
	
	@FXML Button outputFileSelect;
	@FXML Button startButton;
	@FXML Button stopButton;
	@FXML Button mainButton;
	@FXML Button exitButton;
	@FXML Button aboutButton;
	
	
	// =====================================//
	//          Non-FXML Components         //
	// =====================================//
	
	private static TableView<Tweet> table;
	
	private static TableColumn<Tweet, String> tweetId;
	private static TableColumn<Tweet, String> date;
	private static TableColumn<Tweet, String> username;
	private static TableColumn<Tweet, String> message;
		
	private static Thread background;
	
	
	// =====================================//
	//        FXML Components Actions       //
	// =====================================//
	
	@FXML public void handleOutputFileSelect() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text file", "*.txt"), new ExtensionFilter("All files", "*.*"));
		fileChooser.setTitle("Please specify which file you want to open");
		try {
			String fileToSave = fileChooser.showSaveDialog(WindowsManager.displayOnScreenStage).getPath();
			outputFile.setText(fileToSave);
		} catch (NullPointerException exepction) {
			System.out.println(exepction.getMessage());
		}
	}
	
	@FXML public void handleStartButton() {
		Parameters.setStop(false);
		if (isGoodParameters()) {
			startAction();
			startButton.setDisable(true);
			outputFileSelect.setDisable(true);
			outputFile.setDisable(true);
		}
	}
	
	@FXML public void handleStopButton() {
		Parameters.setStop(true);
	}
	
	@FXML public void handleMainButton() {
		boolean go = false;
		if (Parameters.isStop()) {
			go =true;
		} else {
			if (ConfirmBox.display("Stop and go to main menu?", "Are you sure you want to stop the collection of tweets and go to the main menu?")) {
				go = true;
			}
		}
		
		if (go) {
			Parameters.setStop(true);
			WindowsManager.displayConfigurationWindow();
			WindowsManager.saveToFileStage.close();
		}

		
	}
	
	@FXML public void handleExitButton() {
		if (ConfirmBox.display("Exit?", "Are you sure you want to exit?")) {
			System.exit(0);
		}
	}
	
	@FXML public void handleAboutButton() {
		WindowsManager.displayAboutWindow();
	}
	

	// =====================================//
	//            INITIALIZATION            //
	// =====================================//
	
	@SuppressWarnings("unchecked")
	/**
	 * Initialize the scene
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Initialize the count
		tweetsToCollect.setText(Parameters.getNumberOfTweetsToCollect() + "");
		tweetsCollected.setText(0 + "");
		tweetsCollected.setTextFill(Color.GREEN);
			
		// Initialize the table	
		table = new TableView<>();
		tableLayout.getChildren().add(table);
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		table.setEditable(false);
		
		tweetId = new TableColumn<>("Tweet ID");
		tweetId.setPrefWidth(123);
		tweetId.setStyle( "-fx-alignment: CENTER;");
		tweetId.setEditable(false);
		tweetId.setCellValueFactory(new PropertyValueFactory<Tweet, String>("id"));
		
		date = new TableColumn<>("Date");
		date.setPrefWidth(89);
		date.setStyle( "-fx-alignment: CENTER;");
		date.setEditable(false);
		date.setCellValueFactory(new PropertyValueFactory<Tweet, String>("time"));
		
		username = new TableColumn<>("Username");
		username.setPrefWidth(89);
		username.setStyle( "-fx-alignment: CENTER-LEFT;");
		username.setEditable(false);
		username.setCellValueFactory(new PropertyValueFactory<Tweet, String>("username"));
		
		message = new TableColumn<>("Tweet Message");
		message.setPrefWidth(389);
		message.setStyle( "-fx-alignment: CENTER-LEFT;");
		message.setEditable(false);
		message.setCellValueFactory(new PropertyValueFactory<Tweet, String>("message"));
		
		table.getColumns().addAll(tweetId, date, username, message);
		
		// Table update
		Parameters.getCountOfTweets().addListener((observable, oldValue, newValue) -> {
			if (newValue!=null) {
				if (!newValue.equals(oldValue)) {
					int tweetIndex = newValue.intValue() - 1;
						
					javafx.application.Platform.runLater( () -> {
						table.getItems().add(Parameters.getTweets().get(tweetIndex));
						table.refresh();
						tweetsCollected.setText(newValue.intValue() + "");
					});
				}
			}
			
		});
		
		// File status
		fileStatus.setText("No file");
		outputFile.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals("")) {
				fileStatus.setText("No file");
				fileStatus.setTextFill(Color.BLACK);
			} else {
				if(Reader.isValidDirectory(new File(newValue).getParent())) {
					System.out.println("Valid file");
					fileStatus.setText("Valid File!");
					fileStatus.setTextFill(Color.GREEN);
				} else {
					System.out.println("Non Valid file");
					fileStatus.setText("Non Valid File!");
					fileStatus.setTextFill(Color.RED);
					
				}
			}
		});
		
	}
	
	
	// =====================================//
	//            PRIVATE METHODS           //
	// =====================================//
	
	/**
	 * Start the collection of Tweets
	 */
	private void startAction() {
		
		Parameters.setOutputFile(outputFile.getText());
		
		Runnable task = new Runnable() {
			public void run() {
				Collector.collectAndSave();
			}
		};

		background = new Thread(task);
		background.setDaemon(true);
		background.start();
	}
	
	/**
	 * Check if the number of tweets to collect is fine and whether at least one keyword is specified
	 * @return
	 */
	private boolean isGoodParameters() {
		if (outputFile.getText()==null || outputFile.getText().equals("")) {
			return false;
		} else if (Reader.isValidDirectory(new File(outputFile.getText()).getParent())) {
			return true;
		}
		return false;
	}
	


}
