package isen.java2.app;

import java.io.IOException;
import java.time.LocalDate;

import isen.java2.model.services.StageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Mathilde Christiaens
 *
 *         this is the main class of our application
 *         
 */
public class ContactApp extends Application
{
    public static void main( String[] args )
    {
    	launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {		
		primaryStage.setTitle("Homescreen");
		StageService.getInstance().setPrimaryStage(primaryStage);
		primaryStage.getIcons().add(new Image("file:src/main/resources/images/icon.png"));

		showHomeScreen();
		
	}
	
	public void showHomeScreen() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ContactApp.class.getResource("/view/GlobalView.fxml"));
		try {
			AnchorPane rootLayout = loader.load();
			Scene scene = new Scene(rootLayout);
			StageService.getInstance().getPrimaryStage().setScene(scene);
			StageService.getInstance().getPrimaryStage().show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
