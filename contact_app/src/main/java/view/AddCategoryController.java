package view;

import java.io.IOException;
import java.util.Optional;

import isen.java2.app.ContactApp;
import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;
import isen.java2.model.services.StageService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


/***
 * 
 * @author Mathilde
 * The class that controls the add a category view
 *
 */
public class AddCategoryController {
	@FXML
	private TextField newCat;
	
	@FXML 
	private ComboBox<String> boxCat;
	
	private int id =0;
	private CategoryDao catDao= new CategoryDao();
	private ObservableList<String> obsvCat;
	private Alert alert = new Alert(Alert.AlertType.INFORMATION);
	private Alert confirmer = new Alert(Alert.AlertType.CONFIRMATION);
//	/** not implemented
//	 * The constructor that initialize the comboBox
//	 */
//	public AddCategoryController() {
//		obsvCat = FXCollections.observableArrayList();
//		catDao.getCategoryList().forEach(e -> obsvCat.add(e.getName()));
//	}
	
//	/**
//	 * The initialization of the view not implemented
//	 */
//	@FXML
//	private void initialize() {
//		boxCat.getItems().addAll(obsvCat);
//		boxCat.setEditable(true);
//		boxCat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				if(catDao.getCategory(newValue)!=null) {
//					id=catDao.getCategory(newValue).getId();
//				}
//			}
//			
//		}
//		);
//	}
	
	/**
	 * Same thing than for the AddView
	 * If you click on the "retour" button it just loads the GlobalView
	 */
	@FXML
	private void handleReturnButton() {
		FXMLLoader loader = new  FXMLLoader();
		loader.setLocation(ContactApp.class.getResource("/view/GlobalView.fxml"));
		try {
			AnchorPane homeScreenAnchorPane = loader.load();
			Scene scene = new Scene(homeScreenAnchorPane);
			StageService.getInstance().getPrimaryStage().setScene(scene);
			StageService.getInstance().getPrimaryStage().show();
			
		}
		catch (IOException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * If the text Field of the new Category is null : send an alert to the user
	 * If the name chosen for the category already exists in the database : send an alert
	 * Otherwise add the category after passing the text through capitalize and add the category to the database 
	 */
	@FXML
	private void hanldeValidateButton() {
		if (newCat.getText().equals("") || newCat.getText()==null) {
			alert.setTitle("Nom de catégorie vide");
			alert.setContentText("Veuillez entrer un nom pour la catégorie.");
			alert.setHeaderText(null);
			alert.show();
		}
		else if(newCat.getText().contains(";")||newCat.getText().contains("&")||newCat.getText().contains("\"")) {
			alert.setTitle("Nom de catégorie invalide");
			alert.setContentText("Veuillez entrer un nom valide de catégorie.");
			alert.setHeaderText(null);
			alert.show();
		}
		else {
			if (catDao.getCategory(newCat.getText())==null) {
				catDao.addCategory(capitalize(newCat.getText()));
				handleReturnButton();
			}
			else {
				alert.setTitle("Catégorie déjà existante");
				alert.setContentText("Vous ne pouvez pas ajouter une catégorie déjà existante.");
				alert.setHeaderText(null);
				alert.show();
			}
		}
		
	}
	
	/**
	 * Take the changes that the user made on the current selected item (not implemented)
	 */
//	@FXML
//	public void handleChangeButton() {
//		if(boxCat.getSelectionModel().getSelectedItem().equals("")) {
//			alert.setTitle("Nom de catégorie vide");
//			alert.setContentText("Veuillez entrer un nom pour la catégorie.");
//			alert.setHeaderText(null);
//			alert.show();
//		}
//		else {
//			confirmer.setTitle("Attention : changement du nom d'un catégorie");
//			confirmer.setContentText("Vous êtes sur le point de modifier le nom d'une catégorie. Ce changement implique que TOUS les contacts qui font partis de cette catégorie chnageront également de catégorie. Êtes-vous sûr de vouloir continuer ?");
//			confirmer.setHeaderText(null);
//			Optional<ButtonType> result= confirmer.showAndWait();
//			if (result.get()==ButtonType.OK) {
//				catDao.updateCategory(new Category(id,boxCat.getSelectionModel().getSelectedItem()));
//				handleReturnButton();
//			}
//			else {
//				boxCat.getItems().clear();
//				boxCat.getItems().addAll(obsvCat);
//			}
//		}
//		
//		
//	}
	
	/**
	 * Code that permits to make the first letter upper case and the other lower 
	 * @param str
	 * @return String
	 */
	public String capitalize(String str)
	{
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
