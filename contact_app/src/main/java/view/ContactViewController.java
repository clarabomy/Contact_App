package view;

import java.io.IOException;

import controllers.QuizApp;
import isen.java2.app.ContactApp;
import isen.java2.model.db.entities.Contact;
import isen.quiz.service.StageService;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ContactViewController extends ListCell<Contact> {
	@FXML
	private Label label1;
	
	@FXML
	private Label label2;
	
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
			
			label2.setText(contact.getFirstname());
//			System.out.println(label1);
			label1.setText(contact.getLastname());
//			number.setText(contact.getPhone());
			setText(null);
			setGraphic(gridPane);
			
			
		}
	}
}
