package view;

import isen.java2.model.db.entities.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
/***
 * 
 * @author Mathilde
 * The class that controls the detailed view of the contacts
 */

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
	private Label detailedViewAddress;
	
	@FXML
	private Label detailedViewNotes;
	
	private Contact detailedContact;
	
	/**
	 * Set the text of the detailed view so the selected contact is shown
	 * @param contact
	 */
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
			String address = contact.getAddress().replaceFirst("&&", "\n").replaceFirst("&&", " ").replaceFirst("&&", "\n");
			detailedViewAddress.setText(address);
		}
		
	}
	
	
	
	
	

}
