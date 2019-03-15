package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.util.Callback;
import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import isen.java2.model.vcard.ContactVcardManager;
import isen.java2.model.vcard.NotEnoughDataException;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/***
 * 
 * @author Mathilde
 * The class that controls the GlobalView of our application
 */
public class GlobalViewController {
	@FXML
	private AnchorPane homeScreenAnchorPane;
	
	@FXML
	private AnchorPane detailedView;
	
	@FXML
	private ListView<Contact> listView;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private Button addButtonHS, exportAllVcard;
	
	@FXML
	private Button supprButtonHS;
	
	@FXML
	private ChoiceBox<String> groupFilter;
	
	@FXML
	private Button findButton;
	
	
	private ContactVcardManager cVcard;
	private ObservableList<Contact> observableContacts;
	private ObservableList<String> obsvCat;
	private CVOnClickController controllerOnClick;
	public Contact contactClick;
	private ContactDao dao = new ContactDao();
	private CategoryDao catDao = new CategoryDao();
	
	/**
	 * Initialize the class by creating the observaleArrayLists that will populate the ListView (list of contacts from database) and the choiceBox (list of categories from the database)
	 * We also add an non existing category "tout afficher" to display every contact
	 */
	public GlobalViewController() {
		observableContacts = FXCollections.observableArrayList();
		dao.listAllContacts().forEach( e-> observableContacts.add(e));
		try {
			cVcard = new ContactVcardManager("../");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		obsvCat = FXCollections.observableArrayList();
		catDao.getCategoryList().forEach(e -> obsvCat.add(e.getName()));
		obsvCat.add("Tout afficher");
	}


	/**
	 * Initialize all the elements in the view :
	 * Set the items of the database with the observablaeArrayList and set the cells with a CellFactory by calling the ContactViewController
	 * Set the items of the choicebox with the other observableArrayList
	 * Set a Listener on the selected item of the ListView so it launches the showContactDetails and change the value of the contactClick by the value of the selectionned contact
	 * Set a Listener to the searchbar so when the text chnages the search is launched instantly
	 * Set a Listener to the choicebox so when you click on it and changes its value it displays only the contacts filtered by the categroy you chose ("tout afficher" displays every contacts)
	 * Also add the detailedView of the contacts on the children of the anchor pane so it is on the right of the split pane
	 */
	@FXML
	public void initialize() {
		listView.setItems(observableContacts);
		listView.setCellFactory(new Callback<ListView<Contact>, javafx.scene.control.ListCell<Contact>>()
        {

			@Override
			public ListCell<Contact> call(ListView<Contact> arg0) {
				return new ContactViewController();
			}
        });
		
		groupFilter.getItems().addAll(obsvCat);
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {

			@Override
			public void changed(ObservableValue<? extends Contact> arg0, Contact arg1, Contact arg2) {
				contactClick = arg2;
				showContactDetails(arg2);
			}
			
		});
		
		searchBar.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				handleSearchButton();
			}
			
		});
		groupFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				listView.getItems().removeAll(observableContacts);
				if (!arg2.equals("Tout afficher")) {
					listView.getSelectionModel().clearSelection();
					listView.getItems().addAll(dao.listContactsByCategory(arg2));
					listView.refresh();
				}
				else {
					listView.getSelectionModel().clearSelection();
					listView.getItems().addAll(dao.listAllContacts());
					listView.refresh();
					
				}
			}
			
		});
		
		
		FXMLLoader loader = new  FXMLLoader();
		loader.setLocation(ContactApp.class.getResource("/view/ContactViewOnClick.fxml"));
		try {
			AnchorPane pane = loader.load();
			detailedView.getChildren().add(pane);
			AnchorPane.setBottomAnchor(pane, (double) 0);
		    AnchorPane.setTopAnchor(pane, (double)0);
		    AnchorPane.setLeftAnchor(pane, (double) 0);
		    AnchorPane.setRightAnchor(pane, (double) 0);
		    detailedView.getChildren().setAll(pane);
			detailedView.setVisible(false);
		}
		catch (IOException e ) {
			e.printStackTrace();
		}
		
		controllerOnClick = loader.getController();
		
		
		listView.refresh();
		listView.getSelectionModel().clearSelection();
		
	}
	
	/**
	 * Sets the visibility to false of the detailed view if there is no selectionned contact
	 * Otherwise sets it to true and calls the method setText of the controller of the detailedView
	 * @param contact
	 */
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
	
	
	 /**
	 * Loads the Add view if someone clicks on add (without any parameter for the controller)
	 */
	@FXML
	    private void handleNewButton() {
		 	FXMLLoader loader = new  FXMLLoader();
			loader.setLocation(ContactApp.class.getResource("/view/AddView.fxml"));
			try {
				homeScreenAnchorPane = loader.load();
				Scene scene = new Scene(homeScreenAnchorPane);
				StageService.getInstance().getPrimaryStage().setScene(scene);
				StageService.getInstance().getPrimaryStage().show();
				
			}
			catch (IOException e ) {
				e.printStackTrace();
			}
	    }
	 
	 
	 /**
	 * If a contact is selected load the addView with the setText method of its controller so it appears as a modification view
	 * Otherwise does nothing
	 */
	@FXML
	 private void handleChangeButton() {
		 if (!listView.getSelectionModel().isEmpty()) {
			 FXMLLoader loader = new  FXMLLoader();
			 loader.setLocation(ContactApp.class.getResource("/view/AddView.fxml"));
			 
			 
				try {
					homeScreenAnchorPane = loader.load();
					Scene scene = new Scene(homeScreenAnchorPane);
					StageService.getInstance().getPrimaryStage().setScene(scene);
					StageService.getInstance().getPrimaryStage().show();
					AddViewController controller = loader.getController();
					controller.setText(contactClick);
						
				}
				catch (IOException e ) {
					e.printStackTrace();
				}
		 }
	 }
	 
	 /**
	 * If a contact is selected, deletes it from the database and refresh the listView. 
	 * Otherwise does nothing.
	 */
	@FXML
	 private void handleSupprButton() {
		 if (!listView.getSelectionModel().isEmpty()) {
			 dao.deleteContact(contactClick.getId());
			 listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
			 System.out.println(listView.getItems());
			 this.listView.refresh();
			 listView.getSelectionModel().clearSelection();
		 }
	 }
	 
	 
	 /**
	 * Launches the search from the button and is also called when the textfield from the searchbar changes. 
	 * It removes every contacts from the ListView and adds only the ones with that are returned by the search.
	 */
	@FXML
	 private void handleSearchButton() {
		 listView.getSelectionModel().clearSelection();
		 listView.getItems().removeAll(observableContacts);
		 listView.getItems().addAll(dao.searchContact(this.searchBar.getText()));
		 listView.refresh();
	 }

	 /**
	 * When you click on the Vcard options, import all vCard, import all contacts from the appropriate folder and refreshes the view right after. 
	 */
	@FXML
		private void handleImportButton(){
			try {
				cVcard.importAllContacts();
			} catch (NotEnoughDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listView.getItems().clear();
			observableContacts.clear();
			listView.getItems().addAll(dao.listAllContacts());
			listView.refresh();
		}
	 
	
	 /**
	 * When you click on the Vcard options, export all vCard, export all contacts from the appropriate folder and refreshes the view right after. 
	 */
	@FXML
	 private void handleExportAllButton() {
		 try {
			cVcard.exportAllContacts(observableContacts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 
	/**
	 * Export the selected contact only if there is a selected contact.
	 * @throws IOException
	 */
	@FXML
	private void handleVcardExport() throws IOException {
		 if (!listView.getSelectionModel().isEmpty()) {
			ContactVcardManager cVcard = new ContactVcardManager("../");
			cVcard.exportContact(contactClick);
		 }
	}
	 
	 /**
	 * When you click on Category options, "ajouter une nouvelle catégorie", loads the addCategory view.
	 */
	@FXML
	 private void handleNewCat() {
		 FXMLLoader loader = new  FXMLLoader();
		 loader.setLocation(ContactApp.class.getResource("/view/AddCategoryView.fxml"));
	 
		 try {
				homeScreenAnchorPane = loader.load();
				Scene scene = new Scene(homeScreenAnchorPane);
				StageService.getInstance().getPrimaryStage().setScene(scene);
				StageService.getInstance().getPrimaryStage().show();
//				AddViewController controller = loader.getController();
					
			}
			catch (IOException e ) {
				e.printStackTrace();
			}
			
	 }
		
	
	

}
