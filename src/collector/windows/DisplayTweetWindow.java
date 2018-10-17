package collector.windows;

import java.net.URL;
import java.util.ResourceBundle;

import collector.engine.Parameters;
import collector.engine.WindowsManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DisplayTweetWindow implements Initializable {
	
	//====================================//
	//          FXML COMPONENSTS          //
	//====================================//

	@FXML Button okButton;
	
	@FXML Label tweetID;
	@FXML Label date;
	@FXML Label username;
	@FXML Label userTiwtterId;
	@FXML Label userLocation;
	@FXML Label message;
	
	//====================================//
	//      FXML COMPONENSTS ACTIONS      //
	//====================================//
	
	
	@FXML public void handleOkButton() {
		System.out.println("OK Button clicked");
		WindowsManager.displayTweetStage.close();
	}
	
	
	//====================================//
	//           INITIALIZATION           //
	//====================================//

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if (Parameters.getActiveTweet()!=null) {
			tweetID.setText(Parameters.getActiveTweet().getId());
			date.setText(Parameters.getActiveTweet().getTime());
			username.setText(Parameters.getActiveTweet().getUsername());
			userTiwtterId.setText(Parameters.getActiveTweet().getUser());
			userLocation.setText(Parameters.getActiveTweet().getLocation());
			message.setText(Parameters.getActiveTweet().getMessage());
		}
		
        // Keys pressing
		okButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					keyEvent.consume();
					okButton.fire();
				}
			}
		});

	}

}
