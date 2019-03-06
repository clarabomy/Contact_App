package isen.java2.app;

import java.io.IOException;
import isen.java2.model.services.StageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class ContactApp extends Application
{
    public static void main( String[] args )
    {
    	launch(args);
    	String address = "&&&&&&";
		String[] addressParts = address.split("&&");
		String street = addressParts[0]; 
		String town = addressParts[1];
		String postalCode = addressParts[2];
		String country = addressParts[3];
		for (String item : addressParts) {
			System.out.println("'" + item+ "'");
		}
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		primaryStage.setTitle("Homescreen");
		StageService.getInstance().setPrimaryStage(primaryStage);
		showHomeScreen();
		
	}
	
	public void showHomeScreen() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ContactApp.class.getResource("/view/GlobalVue.fxml"));
//		System.out.println("bla");
		try {
			AnchorPane rootLayout = loader.load();
			Scene scene = new Scene(rootLayout);
			StageService.getInstance().getPrimaryStage().setScene(scene);
			StageService.getInstance().getPrimaryStage().show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
