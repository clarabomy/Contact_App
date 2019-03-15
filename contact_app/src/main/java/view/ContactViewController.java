package view;

import java.io.IOException;
import isen.java2.app.ContactApp;
import isen.java2.model.db.entities.Contact;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

/***
 * 
 * @author Mathilde
 * The class that controls the cells of the LsitView of the globalView
 *
 */

public class ContactViewController extends ListCell<Contact> {
	@FXML
	private Label label1;
	
	@FXML
	private Label label2;
	
	@FXML
	private GridPane gridPane;
	
	public FXMLLoader loader;
	
	/* 
	 * Void that update the items of the listView in the GlobalView. 
	 * If the contact is null then it sets the text of the two labels at ""
	 * and if the contact exists it sets the labels at the value LastName and FirstName
	 * (non-Javadoc)
	 * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
	 */
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
			
			label2.setText(contact.getFirstname());
			label1.setText(contact.getLastname());
			setText(null);
			setGraphic(gridPane);
			
		}
		else if(loader!=null) {
			label2.setText("");
			label1.setText("");
		}
		
	}
}
