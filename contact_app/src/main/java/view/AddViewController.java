package view;

import java.io.IOException;
import java.util.ArrayList;

import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private TextField addNom, ville, cp, numRue, addPrenom, addNotes, addTelephone, addEmail, addSurnom, pays;
	
	@FXML
	private ChoiceBox addGroupe;
	
	@FXML
	private DatePicker addBirthDate;
	
	@FXML
	private Button validateButton, backToGlobalVue;
	
	private ObservableList<String> obsvList;
	
	@FXML
	public void initialize() {
		
		CategoryDao catd = new CategoryDao();
		obsvList = FXCollections.observableArrayList();
		catd.getCategoryList().forEach(e ->{ 
			obsvList.add(e.getName());
			System.out.println(e.getName());});
		addGroupe.getItems().addAll(obsvList);
		addGroupe.getSelectionModel().select(0);
	}
	
	@FXML
	public void setText(Contact contact) {
		System.out.println(addNom);
		addNom.setText(contact.getLastname()==null?"":contact.getLastname());
		addPrenom.setText(contact.getFirstname()==null?"":contact.getFirstname());
		addNotes.setText(contact.getNotes()==null?"":contact.getNotes());
		addTelephone.setText(contact.getPhone()==null?"":contact.getPhone());
		addEmail.setText(contact.getMail()==null?"":contact.getMail());
		addSurnom.setText(contact.getNickname()==null?"":contact.getNickname());
		addGroupe.getSelectionModel().select(contact.getCategory().getId());
		addBirthDate.setValue(contact.getBirthdate());
		if (contact.getAddress()!= null || contact.getAddress()!= "") {
			String[] address = contact.getAddress().split("&&");
			ArrayList<TextField> addressTF = new ArrayList<TextField>();
			addressTF.add(numRue);
			addressTF.add(ville);
			addressTF.add(cp);
			addressTF.add(pays);
			for (int i =0; i<address.length; i++) {
				addressTF.get(i).setText(address[i]);
			}
		}
		
		
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
			Category category = cdao.getCategory("Sans catégorie");
			if (addGroupe.getValue().toString()!="" || addGroupe.getValue()!=null) {
				category = cdao.getCategory(addGroupe.getValue().toString());
				
			}
			
			String address = numRue.getText()+"&&"+ville.getText()+"&&"+cp.getText()+"&&"+pays.getText();
			
			Contact contact = new Contact(addNom.getText(), addPrenom.getText(), addSurnom.getText(), address, addBirthDate.getValue(), category, addEmail.getText(), addTelephone.getText(), addNotes.getText()  );
			
			ContactDao contDao = new ContactDao();
			contDao.addContact(contact);
			
			handleReturnButton();
			
		}
		
	}
	
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
	
}
