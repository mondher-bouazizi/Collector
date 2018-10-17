package collector.engine;

import java.io.IOException;

import collector.extra.windows.ConfirmBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WindowsManager {
	
	//====================================//
	//         STAGES AND PARENTS         //
	//====================================//
	
	public static Stage loginStage;
	public static Parent loginRoot;
	
	public static Stage createUserStage;
	public static Parent createUserRoot;
	
	public static Stage configurationStage;
	public static Parent configurationRoot;
	
	public static Stage saveToFileStage;
	public static Parent saveToFileRoot;
	
	public static Stage displayOnScreenStage;
	public static Parent displayOnScreenRoot;
	
	public static Stage displayTweetStage;
	public static Parent displayTweetRoot;
	
	public static Stage aboutStage;
	public static Parent aboutRoot;
	
	//====================================//
	//          WINDOWS DISPLAY           //
	//====================================//
	
	/**
	 * Handle the "Login" window
	 */
	public static void displayLoginWindow() {
		
		loginStage = new Stage();
		loginStage.setResizable(false);
		loginStage.getIcons().add(new Image("File:" + Constants.loginImage));
		loginStage.setTitle("Login");
		
		loginStage.setOnCloseRequest(e -> {
			e.consume();
			System.exit(0);
		});
		
		loginRoot = null;
		
		try {
			loginRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/LoginWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		loginStage.setScene(new Scene(loginRoot, 320, 480));
		loginStage.initModality(Modality.APPLICATION_MODAL);
		loginStage.show();
	}
	
	/**
	 * Handle the "Create User" window
	 */
	public static void displayCreateUserWindow() {
		
		createUserStage = new Stage();
		createUserStage.setResizable(false);
		createUserStage.getIcons().add(new Image("File:" + Constants.loginImage));
		createUserStage.setTitle("Create user");
		
		createUserRoot = null;
		
		try {
			createUserRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/CreateUserWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		createUserStage.setScene(new Scene(createUserRoot, 360, 480));
		createUserStage.initModality(Modality.APPLICATION_MODAL);
		createUserStage.show();
	}
	
	/**
	 * Handle the "Configuration" window
	 */
	public static void displayConfigurationWindow() {
		
		configurationStage = new Stage();
		configurationStage.setResizable(false);
		configurationStage.getIcons().add(new Image("File:" + Constants.settingsIcon));
		configurationStage.setTitle("Settings");
		
		configurationStage.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		
		configurationRoot = null;
		
		try {
			configurationRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/ConfigurationWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		configurationStage.setScene(new Scene(configurationRoot, 520, 640));
		configurationStage.initModality(Modality.APPLICATION_MODAL);
		configurationStage.show();
	}
	
	/**
	 * Handle the "Save To File" window
	 */
	public static void saveToFileWindow() {
		
		saveToFileStage = new Stage();
		saveToFileStage.setResizable(false);
		saveToFileStage.getIcons().add(new Image("File:" + Constants.TwitterIcon));
		saveToFileStage.setTitle("Twitter Collector");
		
		saveToFileStage.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		
		saveToFileRoot = null;
		
		try {
			saveToFileRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/SaveFileWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		saveToFileStage.setScene(new Scene(saveToFileRoot, 640, 480));
		// saveToFileStage.initModality(Modality.APPLICATION_MODAL);
		saveToFileStage.initModality(Modality.WINDOW_MODAL);
		saveToFileStage.show();
	}
	
	/**
	 * Handle the "Display on screen" window
	 */
	public static void displayOnScreenWindow() {
		
		displayOnScreenStage = new Stage();
		displayOnScreenStage.setResizable(false);
		displayOnScreenStage.getIcons().add(new Image("File:" + Constants.TwitterIcon));
		displayOnScreenStage.setTitle("Twitter Collector");
		
		displayOnScreenStage.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		
		displayOnScreenRoot = null;
		
		try {
			displayOnScreenRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/DisplayOnScreenWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		displayOnScreenStage.setScene(new Scene(displayOnScreenRoot, 800, 600));
		// displayOnScreenStage.initModality(Modality.APPLICATION_MODAL);
		displayOnScreenStage.initModality(Modality.WINDOW_MODAL);
		displayOnScreenStage.show();
	}
	
	/**
	 * Handle the "Display a Tweet" window
	 */
	public static void displayTweetWindow() {
		
		displayTweetStage = new Stage();
		displayTweetStage.setResizable(false);
		displayTweetStage.getIcons().add(new Image("File:" + Constants.TwitterIcon));
		displayTweetStage.setTitle("Tweet details");
		
		displayTweetRoot = null;
		
		try {
			displayTweetRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/DisplayTweetWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		displayTweetStage.setScene(new Scene(displayTweetRoot, 620, 240));
		displayTweetStage.initModality(Modality.APPLICATION_MODAL);
		displayTweetStage.show();
	}
	
	/**
	 * Handle the "Display on screen" window
	 */
	public static void displayAboutWindow() {
		
		aboutStage = new Stage();
		aboutStage.setResizable(false);
		aboutStage.getIcons().add(new Image("File:" + Constants.TwitterImage));
		aboutStage.setTitle("About...");
		
		aboutRoot = null;
		
		try {
			aboutRoot = FXMLLoader.load(WindowsManager.class.getResource("/collector/windows/AboutWindow.fxml"));
		} catch (IOException e) {
			System.out.println("Could not load the FXML file!!");
			e.printStackTrace();
		}
		aboutStage.setScene(new Scene(aboutRoot, 280, 480));
		aboutStage.initModality(Modality.APPLICATION_MODAL);
		aboutStage.show();
	}
	
	
	
	//====================================//
	//          PRIVATE METHODS           //
	//====================================//
	
	/**
	 * Handle the exit situations
	 */
	private static void closeProgram() {
			if (ConfirmBox.display( "Exit", "Are you sure you want to exis?")) {
				System.exit(0);
			}
	}

}
