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
import isen.java2.model.vcard.ContactVcard;
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

public class GlobalVueController {
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
	
	
	private ContactVcard cVcard;
	private ObservableList<Contact> observableContacts;
	private ObservableList<String> obsvCat;
	private CVOnClickController controllerOnClick;
	public Contact contactClick;
	private ContactDao dao = new ContactDao();
	private CategoryDao catDao = new CategoryDao();
	
	public GlobalVueController() {
		
		observableContacts = FXCollections.observableArrayList();
		dao.listAllContacts().forEach( e-> observableContacts.add(e));
		try {
			cVcard = new ContactVcard("../");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		obsvCat = FXCollections.observableArrayList();
		catDao.getCategoryList().forEach(e -> obsvCat.add(e.getName()));
		obsvCat.add("Tout afficher");
	}


	@FXML
	public void initialize() {
		
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
		
		groupFilter.getItems().addAll(obsvCat);
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {

			@Override
			public void changed(ObservableValue<? extends Contact> arg0, Contact arg1, Contact arg2) {
				// TODO Auto-generated method stub
				contactClick = arg2;
				showContactDetails(arg2);
			}
			
		});
		
		searchBar.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				handleSearchButton();
			}
			
		});
		groupFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
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
	 
	 
	 @FXML
	 private void handleSearchButton() {
		 listView.getSelectionModel().clearSelection();
		 listView.getItems().removeAll(observableContacts);
		 listView.getItems().addAll(dao.searchContact(this.searchBar.getText()));
		 listView.refresh();
	 }

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
	 
	 @FXML
	 private void handleExportAllButton() {
		 try {
			cVcard.exportAllContacts(observableContacts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 
	 @FXML
		private void handleVcardExport() throws IOException {
		 if (!listView.getSelectionModel().isEmpty()) {
			ContactVcard cVcard = new ContactVcard("../");
			cVcard.exportContact(contactClick);
		 }
		}
	 
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
