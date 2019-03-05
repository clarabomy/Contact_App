package view;

import java.io.IOException;

import isen.java2.app.ContactApp;
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
	private Label nameSurname;
	
	@FXML
	private Label email;
	
	@FXML
	private Label number;
	
	@FXML
	private GridPane gridPane;
	
	public FXMLLoader loader;
	
	@Override
	protected void updateItem(Contact contact, boolean empty) {
		super.updateItem(contact, empty);
		
		if (contact!=null){
			if (loader == null) {
				loader = new FXMLLoader();
				loader.setLocation(ContactApp.class.getResource("/view/ContactView.fxml"));
				loader.setController(this);
				try {
					loader.load();
				}
				catch (IOException e ) {
					e.printStackTrace();
				}
			}
			
			nameSurname.setText(contact.getFirstname()+ " "+ contact.getLastname());
			System.out.println(nameSurname);
			email.setText(contact.getMail());
			number.setText(contact.getPhone());
			setText(null);
			setGraphic(gridPane);
			
		}
	}
}
