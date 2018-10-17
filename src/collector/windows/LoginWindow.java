package collector.windows;

import java.net.URL;
import java.util.ResourceBundle;

import collector.engine.Loader;
import collector.engine.Parameters;
import collector.engine.WindowsManager;
import collector.extra.windows.AlertBox;
import collector.security.Security;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginWindow implements Initializable {
	
	//====================================//
	//          FXML COMPONENSTS          //
	//====================================//
	
	@FXML TextField username;
	@FXML PasswordField password;
	
	@FXML Button loginButton;
	@FXML Button createUserButton;
	
	
	//====================================//
	//      FXML COMPONENSTS ACTIONS      //
	//====================================//
	
	@FXML public void handleLogin() {
		System.out.println("Login Button clicked");
		if (Loader.getUsers().containsKey(username.getText())) {
			String usr = username.getText();
			String pswd = Security.MD5(password.getText());
			if (Loader.getUsers().get(usr).getPasswordHash().equals(pswd)) {
				Parameters.setCurrentUser(Loader.getUsers().get(usr));
				WindowsManager.displayConfigurationWindow();
				WindowsManager.loginStage.close();
			} else {
				AlertBox.display("Wrong username or password", "The username and/or the password you entered are wrong. Please try again", "OK");
			}
		} else {
			AlertBox.display("Wrong username", "The username you entered is not registered. Please check again or create a new user if you have nor registered yet.", "OK");
		}
	}
	
	@FXML public void handleCreateUser() {
		System.out.println("Create User Button clicked");
		WindowsManager.displayCreateUserWindow();
	}
	
	
	//====================================//
	//           INITIALIZATION           //
	//====================================//

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        // Keys pressing
		username.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					keyEvent.consume();
					loginButton.fire();
				}
			}
		});
		
		password.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					keyEvent.consume();
					loginButton.fire();
				}
			}
		});
		
		loginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					keyEvent.consume();
					loginButton.fire();
				}
			}
		});
	}

}
