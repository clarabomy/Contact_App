package view;

import java.io.IOException;

import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import isen.java2.model.services.ViewService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddViewController {
	@FXML
	private TextField addNom, addAddress, addPrenom, addNotes, addTelephone, addEmail, addSurnom;
	
	@FXML
	private ChoiceBox<String> addGroupe;
	
	@FXML
	private DatePicker addBirthDate;
	
	@FXML
	private Button validateButton, backToGlobalVue;
	
	
	@FXML
	public void init() {
		
	}
	
	@FXML
	private void handleValidateButton() {
		boolean formulaireValide = true;
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initOwner(StageService.getInstance().getPrimaryStage());
		
		if (addNom.getText().equals("") || addNom==null) {
			alert.setTitle("Erreur formulaire");
			alert.setContentText("Vous devez obligatoirement remplir le champ Nom.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		else if (addPrenom.getText().equals("") || addPrenom==null) {
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez obligatoirement remplir le champ Prénom.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		else if (addTelephone.getText().equals("") || addTelephone==null) {
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez obligatoirement remplir le champ Téléphone.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		else{try {
			int num = Integer.parseInt(addTelephone.getText());
			
		}catch(NumberFormatException e){
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez remplir un numéro de téléphone valide.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}}
		
		if(formulaireValide) {
			CategoryDao cdao = new CategoryDao();
			Category category = null;
			if (addGroupe.getSelectionModel().getSelectedItem()!="" || addGroupe!=null) {
				category = cdao.getCategory(addGroupe.getSelectionModel().getSelectedItem());
				
			}
			
			Contact contact = new Contact(addNom.getText(), addPrenom.getText(), addSurnom.getText(), addAddress.getText(), addBirthDate.getValue(), category, addEmail.getText(), addTelephone.getText(), addNotes.getText()  );
			
			ContactDao contDao = new ContactDao();
			contDao.addContact(contact);
			
			FXMLLoader loader = new  FXMLLoader();
			loader.setLocation(ContactApp.class.getResource("/view/GlobalView.fxml"));
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
		
	}
	
}
