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
		if (newCat.getText().equals("") || newCat.getText()==null) {
			alert.setTitle("Nom de catégorie vide");
			alert.setContentText("Veuillez entrer un nom pour la catégorie.");
			alert.setHeaderText(null);
			alert.show();
		}
		else {
			if (catDao.getCategory(newCat.getText())==null) {
				catDao.addCategory(newCat.getText());
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
}
