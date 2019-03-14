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
import isen.java2.model.vcard.ContactVcard;
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
//		System.out.println(addNom);
		nveauContact.setText("Modification");
		addNom.setText(contact.getLastname()==null?"":contact.getLastname());
		addPrenom.setText(contact.getFirstname()==null?"":contact.getFirstname());
//		System.out.println(contact.getNotes());
//		addNotes.setText(contact.getNotes()==null?"":contact.getNotes());
		addNotes.setText(contact.getNotes());
		addTelephone.setText(contact.getPhone()==null?"":contact.getPhone());
		addEmail.setText(contact.getMail()==null?"":contact.getMail());
		addSurnom.setText(contact.getNickname()==null?"":contact.getNickname());
		addGroupe.getSelectionModel().select(contact.getCategory().getId()-1);
		addBirthDate.setValue(contact.getBirthdate());
//		System.out.println(contact.getAddress());
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
			Contact contact = new Contact(addNom.getText(), addPrenom.getText(), addSurnom.getText(), address, addBirthDate.getValue(), category, addEmail.getText(), addTelephone.getText(), addNotes.getText()  );
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
				alert.setContentText("Vous ne pouvez pas ajouter 2 contacts déjà existants. Veuillez sortir de la fenêtre d'ajout de contact et cliquer sur le bouton 'change'.");
				alert.setHeaderText(null);
				alert.show();
			}
			
			
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
