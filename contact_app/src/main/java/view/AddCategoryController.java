package view;

import java.io.IOException;

import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.services.StageService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddCategoryController {
	@FXML
	private TextField newCat;
	
	private CategoryDao catDao= new CategoryDao();
	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	
	@FXML
	private void handleReturnButton() {
		/***
		 * Same thing than for the AddView
		 * If you click on the "retour" button it just loads the GlobalView
		 */
		FXMLLoader loader = new  FXMLLoader();
		loader.setLocation(ContactApp.class.getResource("/view/GlobalVue.fxml"));
		try {
			AnchorPane homeScreenAnchorPane = loader.load();
			Scene scene = new Scene(homeScreenAnchorPane);
			StageService.getInstance().getPrimaryStage().setScene(scene);
			StageService.getInstance().getPrimaryStage().show();
			
		}
		catch (IOException e ) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void hanldeValidateButton() {
		/**
		 * If the text Field of the new Category is null : send an alert to the user
		 * If the name chosen for the category already exists in the database : send an alert
		 * Otherwise add the category after passing the text through capitalize and add the category to the database 
		 */
		if (newCat.getText().equals("") || newCat.getText()==null) {
			alert.setTitle("Nom de catégorie vide");
			alert.setContentText("Veuillez entrer un nom pour la catégorie.");
			alert.setHeaderText(null);
			alert.show();
		}
		else {
			if (catDao.getCategory(newCat.getText())==null) {
				catDao.addCategory(capitalize(newCat.getText()));
				handleReturnButton();
			}
			else {
				alert.setTitle("Catégorie déjà existante");
				alert.setContentText("Vous ne pouvez pas ajouter une catégorie déjà existante.");
				alert.setHeaderText(null);
				alert.show();
			}
		}
		
	}
	
	public String capitalize(String str)
	{
		/***
		 * Code that permits to make the first letter upper case and the other lower 
		 */
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
