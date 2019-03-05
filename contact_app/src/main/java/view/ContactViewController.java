package view;

import java.io.IOException;

import isen.java2.model.db.entities.Contact;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ContactViewController extends ListCell<Contact> {
	@FXML
	private Label NameSurname;
	
	@FXML
	private Label email;
	
	@FXML
	private Label number;
	
	@FXML
	private GridPane gridPane;
	
	private FXMLLoader loader;
	
	@Override
	protected void updateItem(Contact contact, boolean empty) {
		super.updateItem(contact, empty);
		
		if (empty || contact==null) {
			setText(null);
			setGraphic(null);
		}
		
		else {
			if (loader == null) {
				loader = new FXMLLoader(getClass().getResource("/view/ContactView.fxml"));
				loader.setController(this);
				
				try {
					loader.load();
				}
				catch (IOException e ) {
					e.printStackTrace();
				}
			}
			
			NameSurname.setText(contact.getFirstname()+ " "+ contact.getFirstname());
			email.setText(contact.getMail());
			number.setText(contact.getPhone());
			
			
			setText(null);
			setGraphic(gridPane);
			
		
		}
	}
}
