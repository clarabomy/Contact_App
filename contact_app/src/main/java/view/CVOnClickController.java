package view;

import java.io.IOException;

import isen.java2.model.db.entities.Contact;
import isen.java2.model.vcard.ContactVcard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class CVOnClickController {
	
	@FXML
	private Button exportVCard;
	
	@FXML
	private Label title;
	
	@FXML
	private Text detailedViewNumber;
	
	@FXML
	private Text detailedViewEmail;
	
	@FXML
	private Text detailedViewBirthDate;
	
	@FXML
	private Text detailedViewGroup;
	
	@FXML
	private Text detailedViewAddress;
	
	@FXML
	private Text detailedViewNotes;
	
	private Contact detailedContact;
	
	@FXML
	public void setText(Contact contact) {
		detailedContact = contact;
		if(contact.getNickname()==null||contact.getNickname()=="") {
			title.setText(contact.getLastname().toUpperCase()+ " "+ contact.getFirstname());
		}
		else {
			title.setText(contact.getLastname().toUpperCase()+ " "+ contact.getFirstname()+" ("+contact.getNickname()+")");
		}
		
		detailedViewNumber.setText(contact.getPhone());
		detailedViewBirthDate.setText(contact.getBirthdate()==null?"":contact.getBirthdate().toString());
		detailedViewEmail.setText(contact.getMail()==null?"":contact.getMail());
		detailedViewGroup.setText(contact.getCategory().getName()==null?"":contact.getCategory().getName());
		detailedViewNotes.setText(contact.getNotes()==null?"":contact.getNotes());
		
		if (contact.getAddress()!=null) {
			String address = contact.getAddress().replace("&&", " ");
			detailedViewAddress.setText(address);
		}
		
	}
	
	@FXML
	private void handleVcardExport() throws IOException {
		ContactVcard cVcard = new ContactVcard("D:/ISEN/Cours/M1/Projets/Contact_App/contact_app/src/main");
		cVcard.exportContact(detailedContact);
	}
	
	
	

}
