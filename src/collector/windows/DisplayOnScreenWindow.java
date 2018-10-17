package collector.windows;

import java.net.URL;
import java.util.ResourceBundle;

import collector.engine.Collector;
import collector.engine.Parameters;
import collector.engine.WindowsManager;
import collector.extra.windows.ConfirmBox;
import collector.io.Writer;
import collector.items.Tweet;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DisplayOnScreenWindow implements Initializable {
	
	// =====================================//
	//            FXML Components           //
	// =====================================//
		
	@FXML HBox tableLayout;
	
	@FXML Button startButton;
	@FXML Button saveAll;
	@FXML Button displayDetailsButton;
	@FXML Button stopButton;
	@FXML Button mainButton;
	@FXML Button exitButton;
	@FXML Button aboutButton;
	
	@FXML Label tweetsCollected;
	@FXML Label tweetsToCollect;
	
	
	// =====================================//
	//          Non-FXML Components         //
	// =====================================//
	
	private static TableView<Tweet> table;
	
	private static TableColumn<Tweet, String> tweetId;
	private static TableColumn<Tweet, String> date;
	private static TableColumn<Tweet, String> username;
	private static TableColumn<Tweet, String> message;
	private static TableColumn<Tweet, String> userLocation;
		
	private static Thread background;
	
	// =====================================//
	//        FXML Components Actions       //
	// =====================================//

	@FXML public void handleStartButton() {
		Parameters.setStop(false);
		startAction();
		startButton.setDisable(true);
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
			WindowsManager.displayOnScreenStage.close();
		}

		
	}
	
	@FXML public void handleDisplayDetailsButton() {
		if (table.selectionModelProperty().getValue()!=null && !table.selectionModelProperty().get().isEmpty()) {
			displaySelectedTweet();
		}
	}
	
	@FXML public void handleSaveAll() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text file", "*.txt"), new ExtensionFilter("All files", "*.*"));
		fileChooser.setTitle("Please specify which file you want to open");
		try {
			String fileToSave = fileChooser.showSaveDialog(WindowsManager.displayOnScreenStage).getPath();
			Writer.saveTweets(fileToSave, Parameters.getTweets());
		} catch (NullPointerException exepction) {
			System.out.println(exepction.getMessage());
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
	
	/**
	 * Initialize the scene
	 */
	@SuppressWarnings("unchecked")
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
		
		userLocation = new TableColumn<>("Location");
		userLocation.setPrefWidth(59);
		userLocation.setStyle( "-fx-alignment: CENTER;");
		userLocation.setEditable(false);
		userLocation.setCellValueFactory(new PropertyValueFactory<Tweet, String>("location"));
		
		table.getColumns().addAll(tweetId, date, username, message, userLocation);
		
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
		
	}
	
	
	// =====================================//
	//            PRIVATE METHODS           //
	// =====================================//
	
	/**
	 * Start the collection of Tweets
	 */
	private static void startAction() {

		Runnable task = new Runnable() {
			public void run() {
				Collector.collectAndDisplay();
			}
		};

		background = new Thread(task);
		background.setDaemon(true);
		background.start();
	}
	
	/**
	 * Display the selected tweet
	 */
	private static void displaySelectedTweet() {
		Parameters.setActiveTweet(table.selectionModelProperty().getValue().getSelectedItem());
		WindowsManager.displayTweetWindow();
	}
}
