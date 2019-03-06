package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.util.Callback;
import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class GlobalVueController implements Initializable{
	@FXML
	private AnchorPane homeScreenAnchorPane;
	
	@FXML
	private AnchorPane detailedView;
	
	@FXML
	private ListView<Contact> listView;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private Button addButtonHS;
	
	@FXML
	private Button supprButtonHS;
	
	@FXML
	private ChoiceBox<Category> groupFilter;
	
	@FXML
	private Button findButton;
	
	
	private ObservableList<Contact> observableContacts;
	private CVOnClickController controllerOnClick;
	
	
	public GlobalVueController() {
		
		observableContacts = FXCollections.observableArrayList();
		
		ContactDao daoContact = new ContactDao();
		daoContact.listAllContacts("").forEach( e-> observableContacts.add(e));
		
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		listView.setItems(observableContacts);
		listView.setCellFactory(new Callback<ListView<Contact>, javafx.scene.control.ListCell<Contact>>()
        {

			@Override
			public ListCell<Contact> call(ListView<Contact> arg0) {
				// TODO Auto-generated method stub
				return new ContactViewController();
			}
        });
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {

			@Override
			public void changed(ObservableValue<? extends Contact> arg0, Contact arg1, Contact arg2) {
				// TODO Auto-generated method stub
				showContactDetails(arg2);
			}
			
		});
		
		FXMLLoader loader = new  FXMLLoader();
		loader.setLocation(ContactApp.class.getResource("/view/ContactViewOnClick.fxml"));
		try {
			AnchorPane pane = loader.load();
			detailedView.getChildren().add(pane);
			detailedView.setVisible(false);
		}
		catch (IOException e ) {
			e.printStackTrace();
		}
		
		controllerOnClick = loader.getController();
		
		
		listView.refresh();
		listView.getSelectionModel().clearSelection();
		
	}
	
	@FXML
	private void showContactDetails(Contact contact) {
		if (contact==null) {
			detailedView.setVisible(false);
		}
		else {
			detailedView.setVisible(true);
			controllerOnClick.setText(contact);
		}
	}
	
	@FXML
	private void handleSaveButton() {
		
	}
	
	
	

}
