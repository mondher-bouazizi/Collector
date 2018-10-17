package collector.windows;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import collector.engine.Constants;
import collector.engine.Loader;
import collector.engine.WindowsManager;
import collector.extra.windows.AlertBox;
import collector.items.User;
import collector.security.Security;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateUserWindow implements Initializable {

	private enum ErrorCode {
		login, password, consumer_key, consumer_secret, access_token, access_token_secret, good_parameters
	}
	
	// ====================================//
	//           FXML COMPONENSTS          //
	// ====================================//
	
	@FXML TextField username;
	@FXML PasswordField password;

	@FXML TextField consumerKey;
	@FXML TextField consumerSecret;
	@FXML TextField accessToken;
	@FXML TextField accessTokenSecret;

	@FXML Button createButton;
	@FXML Button cancelButton;
	@FXML Button helpButton;
	
	
	// ====================================//
	//       FXML COMPONENSTS ACTIONS      //
	// ====================================//

	@FXML public void handleCreate() {
		System.out.println("Create User Button clicked");

		if (isGoodParameters().equals(ErrorCode.login)) {
			AlertBox.display("Error", "The unsername must be at least 6 characters-long!", "OK");
		} else if (isGoodParameters().equals(ErrorCode.password)) {
			AlertBox.display("Error",
					"The password must contain a letters, a number and a symbol!", "OK");
		} else if (isGoodParameters().equals(ErrorCode.consumer_key)) {
			AlertBox.display("Error", "The Consumer Key entered is invalid!", "OK");
		} else if (isGoodParameters().equals(ErrorCode.consumer_secret)) {
			AlertBox.display("Error", "The Consumer Key Secret is invalid!", "OK");
		} else if (isGoodParameters().equals(ErrorCode.access_token)) {
			AlertBox.display("Error", "The Access Token is invalid!", "OK");
		} else if (isGoodParameters().equals(ErrorCode.access_token_secret)) {
			AlertBox.display("Error", "The Access Token Secret is invalid!", "OK");
		} else {
			String usr = username.getText();
			String pswd = Security.MD5(password.getText());
			String key = consumerKey.getText();
			String secret = consumerKey.getText();
			String token = accessToken.getText();
			String tokenSecret = accessTokenSecret.getText();
			User user = new User(usr, pswd, key, secret, token, tokenSecret);
			if (Loader.getUsers().containsKey(usr)) {
				AlertBox.display("Username exists already", "The username you entered exists already. Please choose a different username", "OK");
			} else {
				Loader.addUser(user);
				AlertBox.display("User Create Successfully", "User created successfully, you can now login using the credentials you entered!", "OK");
				WindowsManager.createUserStage.close();
			}
		}
	}

	@FXML public void handleCancel() {
		System.out.println("Cancel Button clicked");
		WindowsManager.createUserStage.close();
	}

	@FXML public void handleHelp() {
		System.out.println("Help Button clicked");
		if (Desktop.isDesktopSupported()) {
			try {
				File myFile = new File(Constants.helpPdf);
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
				// no application registered for PDFs
			}
		}
	}
	
	
	// ====================================//
	//            INITIALIZATION           //
	// ====================================//

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO add if needed
	}

	
	// ====================================//
	//           PRIVATE METHODS           //
	// ====================================//
	
	/**
	 * Check if the different fields are fine and respect the rules
	 * 
	 * @return
	 */
	private ErrorCode isGoodParameters() {
		// Username
		if (username.getText() == null || username.getText().length() < 6) {
			return ErrorCode.login;
		}

		// Password
		String pswd = password.getText();
		if (pswd.replaceAll("[^A-Za-z]", "").equals("") || pswd.replaceAll("[^0-9]", "").equals("") || pswd.replaceAll("[a-zA-Z0-9]", "").equals("")) {
			return ErrorCode.password;
		}

		// Consumer Key
		if (consumerKey.getText().equals("")) {
			return ErrorCode.consumer_key;
		}

		// Consumer Secret
		if (consumerSecret.getText().equals("")) {
			return ErrorCode.consumer_secret;
		}

		// Access Token
		if (accessToken.getText().equals("")) {
			return ErrorCode.access_token;
		}

		// Access Token Secret
		if (accessTokenSecret.getText().equals("")) {
			return ErrorCode.access_token_secret;
		}

		// Everything is fine
		return ErrorCode.good_parameters;

	}

}
