package collector.windows;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import collector.engine.WindowsManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AboutWindow implements Initializable {
	
	//====================================//
	//          FXML COMPONENSTS          //
	//====================================//

	@FXML Button okButton;
	
	@FXML Hyperlink mailLink;
		
	//====================================//
	//      FXML COMPONENSTS ACTIONS      //
	//====================================//
	
	
	@FXML public void handleOkButton() {
		System.out.println("OK Button clicked");
		WindowsManager.aboutStage.close();
	}
	
	@FXML public void handleGoToLink() {
		if (Desktop.isDesktopSupported()) {
		    Desktop desktop = Desktop.getDesktop();
		    if (desktop.isSupported(Desktop.Action.MAIL)) {
		        URI mailto;
				try {
					mailto = new URI("mailto:mondher.bouazizi@gmail.com");
					desktop.mail(mailto);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		    }
		}
	}
	
	
	//====================================//
	//           INITIALIZATION           //
	//====================================//

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
