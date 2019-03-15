package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import isen.java2.model.vcard.ContactVcardManager;
import isen.java2.model.vcard.NotEnoughDataException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/***
 * 
 * @author Mathilde
 * The class that controls the add/change a contact View
 *
 */

public class AddViewController {
	@FXML
	private TextField addNom, ville, cp, numRue, addPrenom, addTelephone, addEmail, addSurnom, pays;
	
	@FXML
	private TextArea addNotes;
	
	@FXML
	private Text nveauContact;
	
	@FXML
	private ChoiceBox addGroupe;
	
	@FXML
	private DatePicker addBirthDate;
	
	@FXML
	private Button validateButton, backToGlobalVue;
	
	private ObservableList<String> obsvList;
	private boolean update = false;
	private Contact previousContact;
	
	/**
	 * Initialize the view by adding the database and listing the categories for them to be in the choicebox.
	 */
	@FXML
	public void initialize() {		
		CategoryDao catd = new CategoryDao();
		obsvList = FXCollections.observableArrayList();
		catd.getCategoryList().forEach(e ->{obsvList.add(e.getName());});
		addGroupe.getItems().addAll(obsvList);
		addGroupe.getSelectionModel().select(0);
	}
	
	/**
	 * Function that permits to initialize the view with the valid informations when you want to change a contact. 
	 * It's only called by the GlobalView controller and only when you click on the button change.
	 * It also sets the update boolean to true so you the validate function knows if you're updating an existing contact or creating a new one.
	 * @param contact
	 */
	@FXML
	public void setText(Contact contact) {
		nveauContact.setText("Modification");
		addNom.setText(contact.getLastname()==null?"":contact.getLastname());
		addPrenom.setText(contact.getFirstname()==null?"":contact.getFirstname());
		addNotes.setText(contact.getNotes());
		addTelephone.setText(contact.getPhone()==null?"":contact.getPhone());
		addEmail.setText(contact.getMail()==null?"":contact.getMail());
		addSurnom.setText(contact.getNickname()==null?"":contact.getNickname());
		addGroupe.getSelectionModel().select(contact.getCategory().getId()-1);
		addBirthDate.setValue(contact.getBirthdate());

		if (contact.getAddress()!= null && contact.getAddress()!= "") {
			String[] address = contact.getAddress().split("&&");
			ArrayList<TextField> addressTF = new ArrayList<TextField>();
			addressTF.add(numRue);
			addressTF.add(cp);
			addressTF.add(ville);
			addressTF.add(pays);
			for (int i =0; i<address.length; i++) {
				addressTF.get(i).setText(address[i]);
			}
		}
		update=true;
		previousContact=contact;
		
	}
	
	
	/**
	 * Function that permits to handle the "valider" button. 
	 * First, it checks if the form is accurate or not. If it's not the function will do nothing. If it is it will do as follow:
	 * If the view was load because you wanted to change a contact, the function will update the contact in the database.
	 * If the view was load to create a new contact, the function will create the contact in the database. But only if the contact doesn't already exist.
	 * At the end, if the form is valid it will return to the global view of the contacts.
	 */
	@FXML
	private void handleValidateButton() {
		ContactDao contDao = new ContactDao();
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
		else if(!Pattern.matches("0[1-9][1-9]?([0-9]){8}", addTelephone.getText())) {
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez entrer un numéro de téléphone valide.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		else if(cp.getText()!=null && !cp.getText().equals("")) {
			if (cp.getText().contains("\"")||cp.getText().contains("&")||cp.getText().contains(";")) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un code postal valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
		}
		if(!addEmail.getText().equals("")) {
			if(!Pattern.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]{2,}\\.[a-z]{2,5}", addEmail.getText())) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir une adresse email valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
		}
		if(!ville.getText().equals("")) {
			boolean match = Pattern.matches("[a-zA-Z\u00C0-\u017F]+-?'?[a-zA-Z\u00C0-\u017F]*-?'?[a-zA-Z\u00C0-\u017F]*-?'?[a-zA-Z\u00C0-\u017F]*-?'?[a-zA-Z\u00C0-\u017F]*",ville.getText());
			if (!match) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un nom de ville valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
			
		}
		if(!pays.getText().equals("")) {
			if (!Pattern.matches("[a-zA-Z\u00C0-\u017F]+", pays.getText())) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un pays valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
			
		}
		if(!numRue.getText().equals("")) {
			if (numRue.getText().contains("\"")||numRue.getText().contains("&")||numRue.getText().contains(";")) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un numéro et une rue valides.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
		}
		if(addNom.getText().contains("\"")||addNom.getText().contains("&")||addNom.getText().contains(";")) {
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez remplir un nom valide.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		if(addPrenom.getText().contains("\"")||addPrenom.getText().contains("&")||addPrenom.getText().contains(";")) {
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez remplir un prénom valide.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		if(addSurnom.getText().contains("\"")||addSurnom.getText().contains("&")||addSurnom.getText().contains(";")) {
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez remplir un surnom valide.");
			alert.setHeaderText(null);
			alert.show();
			formulaireValide=false;
		}
		
		if(formulaireValide) {
			CategoryDao cdao = new CategoryDao();
			Category category = cdao.getCategory("Sans catégorie");
			if (addGroupe.getValue().toString()!="" || addGroupe.getValue()!=null) {
				category = cdao.getCategory(addGroupe.getValue().toString());
				
			}
			Contact existCont = new Contact(addNom.getText(), addPrenom.getText(), null, null);
			String address = numRue.getText()+"&&"+cp.getText()+"&&"+ville.getText()+"&&"+pays.getText();
			Contact contact = new Contact(capitalize(addNom.getText()), capitalize(addPrenom.getText()), addSurnom.getText().equals("")?addSurnom.getText():capitalize(addSurnom.getText()), address, addBirthDate.getValue(), category, addEmail.getText(), addTelephone.getText(), addNotes.getText()  );
			if((!this.update) && (contDao.existContact(contact)==0)) {
				contDao.addContact(contact);
				handleReturnButton();
			}
			else if(this.update)  {
				contact.setId(previousContact.getId());
				contDao.updateContact(contact);
				handleReturnButton();
			}
			else if(!this.update && contDao.existContact(existCont)!=0) {
				alert.setTitle("Contact déjà existant");
				alert.setContentText("Vous ne pouvez pas ajouter un contact déjà existant. Veuillez sortir de la fenêtre d'ajout de contact et cliquer sur le bouton 'change'.");
				alert.setHeaderText(null);
				alert.show();
			}
			
			
		}
		
	}
	
	/**
	 * Basic function that permits to return to the global view when we click on "retour" or when the form is valid and you clicked on "valider".
	 */
	@FXML
	private void handleReturnButton() {
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
	
	public String capitalize(String str)
	{
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	
	
}
