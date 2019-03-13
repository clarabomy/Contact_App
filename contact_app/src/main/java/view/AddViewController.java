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
		else if(cp.getText()!=null && !cp.getText().equals("")) {
			try {
				int num = Integer.parseInt(cp.getText());
//				System.out.println(num);
				
			}catch(NumberFormatException e){
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un code postal valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
		}
		if(!ville.getText().equals("")) {
			String s=ville.getText().replaceAll("[^0-9]", "");
			if (s.length()>0 && Pattern.matches("[a-zA-Z]+",ville.getText())) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un nom de ville valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
			
		}
		if(!pays.getText().equals("")) {
			String s=pays.getText().replaceAll("[^0-9]", "");
			System.out.println(s);
			if (s.length()>0) {
				alert.setTitle("Formulaire mal rempli");
				alert.setContentText("Vous devez remplir un pays valide.");
				alert.setHeaderText(null);
				alert.show();
				formulaireValide=false;
			}
			
		}
		try {
			int num = Integer.parseInt(addTelephone.getText());
			
		}catch(NumberFormatException e){
			alert.setTitle("Formulaire mal rempli");
			alert.setContentText("Vous devez remplir un numéro de téléphone valide.");
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
			
			String address = numRue.getText()+"&&"+cp.getText()+"&&"+ville.getText()+"&&"+pays.getText();
			//System.out.println(address);
			//System.out.println(addBirthDate.getValue());
			Contact contact = new Contact(addNom.getText(), addPrenom.getText(), addSurnom.getText(), address, addBirthDate.getValue(), category, addEmail.getText(), addTelephone.getText(), addNotes.getText()  );
			//System.out.println(update);
			if((!this.update) && (contDao.existContact(contact)==0)) {
				contDao.addContact(contact);
			}
			else  {
				contact.setId(previousContact.getId());
				contDao.updateContact(contact);
			}
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
