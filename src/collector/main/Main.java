package collector.main;

import collector.engine.Loader;
import collector.engine.WindowsManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
	
	public static Parent root;
	public static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {	
		Loader.load();
		WindowsManager.displayLoginWindow();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	

}
