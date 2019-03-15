package isen.java2.model.vcard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

/**
 * @author Clara Bomy
 *
 *         This class implements a contact vcard manager, which allows us 
 *         to export or import our contacts
 *         
 */
public class ContactVcardManager {

	private static final String CONTACT_EXPORT = "contacts_export"; //name of the directory where will be stored exported contacts
	private static final String CONTACT_IMPORT = "contacts_import";	//name of the directory where will be stored imported contacts
	private ContactDao contactDao = new ContactDao();
	
	protected Path root;
	protected Path contactsExportDir;
	protected Path contactsImportDir;

	
	/**
	 * Main constructor. We deal with all the directory creations here, not in
	 * the setters. Thus, when the constructor returns, we are sure that the
	 * object is usable
	 * 
	 * @param root
	 * @throws IOException
	 */
	public ContactVcardManager(String root) throws IOException {
		// This is a simple example of nullsafe processing : check that your
		// public interface is called with legit arguments
		if (root == null || !Files.exists(Paths.get(root))) {
			// this exception has many advantages : it is runtime, thus the
			// caller does not have to explicitly deal with it (no useless
			// try/catch to implement), it is meaningful just by it's name (far
			// more than inadvertently throwing NPE), and with a simple message,
			// it can tell exactly what argument is bad and why.
			throw new IllegalArgumentException("You have to provide a root directory to process");
		}
		
		this.root = Paths.get(root); 
		this.contactsExportDir = this.root.resolve(ContactVcardManager.CONTACT_EXPORT);
		if (Files.notExists(this.contactsExportDir )) {
			this.prepareDirectory("", this.contactsExportDir);
		}
		
		this.contactsImportDir = this.root.resolve(ContactVcardManager.CONTACT_IMPORT);
		if (Files.notExists(this.contactsImportDir )) {
			this.prepareDirectory("", this.contactsImportDir);
		}
		
	}
	
	/**
	 * This method prepares a directory : it creates the path to write on the
	 * drive, and then writes it actually
	 * 
	 * @param newDir
	 *            the name of the new directory to create
	 * @param base
	 *            the path to the base root where to create the new directory
	 * @return the path to the newly created directory
	 * @throws IOException
	 */
	public Path prepareDirectory(String newDir, Path base) throws IOException {
		Path newPath = base.resolve(newDir);
		if (Files.notExists(newPath, LinkOption.NOFOLLOW_LINKS)) { 
			Files.createDirectory(newPath);
		}
		return newPath;
	}
	
	
	/**
	 * This method is used to export a contact
	 * 
	 * @param contact
	 * @throws IOException
	 */
	public void exportContact(Contact contact) throws IOException {
		
		String filename = contact.getFirstname() + " " + contact.getLastname();
		
		//We write the information of the contact in the vcard file. Depending of the information provided, 
		//the content of the file will be different
		try (BufferedWriter br = Files.newBufferedWriter(this.contactsExportDir.resolve(filename), StandardCharsets.UTF_8);) {
			br.write("BEGIN:VCARD\nVERSION:2.1\n");
			br.write("FN:" + filename + "\n");
			br.write("N:" + contact.getLastname() + ";" + contact.getFirstname() + ";;;\n");			
			
			if (!contact.getNickname().contentEquals("")) {
				br.write("NICKNAME:" + contact.getNickname() + "\n");
			}
			
			br.write("CATEGORIES:" + contact.getCategory().getName() + "\n");
			br.write("TEL;CELL:" + contact.getPhone() + "\n");
			
			//Each element of the address is separed with '&&' caracter 
			if (!contact.getAddress().equals("&&&&&&") && !contact.getAddress().equals("")) {
				String address = contact.getAddress() + "&&/";
				String[] addressParts = address.split("&&");
				String street = addressParts[0]; 
				String postalCode = addressParts[1];
				String town = addressParts[2];
				String country = addressParts[3];
				
				br.write("ADR;HOME:;;" + street + ";" + town + ";;" + postalCode + ";" + country + "\n");
			}
			
			if (!contact.getMail().equals("")) {
				br.write("EMAIL;INTERNET:" + contact.getMail() + "\n");
			}
			
			if (contact.getBirthdate() != null) {
				br.write("BDAY:" + contact.getBirthdate().toString() + "\n");
			}
			
			if (!contact.getNotes().equals("")) {
				String notes = contact.getNotes().replace("\n", "\\n");
				br.write("NOTE:" + notes + "\n");
			}
			
			br.write("END:VCARD\n");
		}
	}
	
	/**
	 * This method is used to export all the contacts 
	 * 
	 * @param contactsToExport
	 * @throws IOException
	 */
	public void exportAllContacts(List<Contact> contactsToExport) throws IOException {
		contactsToExport =  this.contactDao.listAllContacts();
		
		for (Contact contact : contactsToExport) {
			exportContact(contact);
		}
	}
	
	/**
	 * This method is used to import a contact 
	 * 
	 * @param filename
	 * @throws IOException
	 * @throws NotEnoughDataException (if there is no lastname, firstname or phone in the vcard file)
	 */
	private void importContact(String filename) throws IOException, NotEnoughDataException {
		String firstname = "";
		String lastname = "";
		String nickname = "";
		String address = "";
		LocalDate birthdate = null;
		Category category = new Category("Sans Catégorie");
		String mail = "";
		String phone = "";
		String notes = "";
		
		// We create a map to store all the information of the contact to import 
		Map<String, String> fileContent = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(this.contactsImportDir.resolve(filename), StandardCharsets.UTF_8);) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] content = line.split(":");
				if (content.length == 2) {
					fileContent.put(content[0], content[1]);
				}
			}
			
			if (!fileContent.containsKey("N") || !fileContent.containsKey("TEL;CELL")) {
				throw new NotEnoughDataException();
			}

			String[] fullName = fileContent.get("N").split(";");
			lastname = fullName[0];
			firstname = fullName[1];
			phone = fileContent.get("TEL;CELL");
			
			if (fileContent.containsKey("NICKNAME")) {
				nickname = fileContent.get("NICKNAME");
			}
			
			if (fileContent.containsKey("CATEGORIES")) {
				category.setName(fileContent.get("CATEGORIES"));
			}
			
			if (fileContent.containsKey("ADR;HOME")) {
				fileContent.put("ADR;HOME", fileContent.get("ADR;HOME") + ";");
				String[] fullAddress =  fileContent.get("ADR;HOME").split(";");
				address = fullAddress[2] + "&&" + fullAddress[5] + "&&" + fullAddress[3] + "&&" + fullAddress[6]; 
			}
			
			if (fileContent.containsKey("EMAIL;INTERNET")) {
				mail = fileContent.get("EMAIL;INTERNET"); 
			}
			
			if (fileContent.containsKey("BDAY")) {
				birthdate = LocalDate.parse(fileContent.get("BDAY"));
			}
			
			if (fileContent.containsKey("NOTE")) {
				notes = fileContent.get("NOTE").replace("\\n", "\n");
			}
			
			// With all these information, we create a new contact and add it in the DB
			Contact importedContact = new Contact(lastname, firstname, nickname, address, birthdate, category, mail, phone, notes);
			this.contactDao.addContact(importedContact);
			
		}
	}
	
	/**
	 * This method is used to import all the vcard files present in the directory where will be stored imported contacts
	 * 
	 * @throws IOException
	 * @throws NotEnoughDataException
	 */
	public void importAllContacts() throws IOException, NotEnoughDataException {
		try (DirectoryStream<Path> contactsToImport = Files.newDirectoryStream(this.contactsImportDir)) {
			
			// We go throw the directory and import the contacts one by one
			for(Path contact : contactsToImport) {
				String filename = contact.getFileName().toString();
				importContact(filename);
				
				// Once the contact is imported, we deleted the considered vcard file
				Files.delete(contact);
			}
		}	
	}
			
}
	
