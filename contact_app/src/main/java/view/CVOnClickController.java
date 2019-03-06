package view;

import isen.java2.model.db.entities.Contact;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CVOnClickController {
	
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
	
	
	public void setText(Contact contact) {
		detailedViewNumber.setText(contact.getPhone());
		detailedViewBirthDate.setText(contact.getBirthdate()==null?"":contact.getBirthdate().toString());
		detailedViewEmail.setText(contact.getMail()==null?"":contact.getMail());
		detailedViewGroup.setText(contact.getCategory().getName()==null?"":contact.getCategory().getName());
		detailedViewNotes.setText(contact.getNotes()==null?"":contact.getNotes());
		detailedViewAddress.setText(contact.getAddress()==null?"":contact.getAddress());
		
	}
	

}
