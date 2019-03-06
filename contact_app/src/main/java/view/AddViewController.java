package view;

import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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
			
		}
		
	}
	
}
