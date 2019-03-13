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


public class ContactVcard {

	private static final String CONTACT_EXPORT = "contacts_export";
	private static final String CONTACT_IMPORT = "contacts_import";	
	private ContactDao contactDao = new ContactDao();
	
	protected Path root;
	protected Path contactsExportDir;
	protected Path contactsImportDir;

	public ContactVcard(String root) throws IOException {

		if (root == null) {
			throw new IllegalArgumentException("You have to provide a root directory to process");
		}
		
		this.root = Paths.get(root); 
		this.contactsExportDir = this.root.resolve(ContactVcard.CONTACT_EXPORT);
		if (Files.notExists(this.contactsExportDir )) {
			this.prepareDirectory("", this.contactsExportDir);
		}
		
		this.contactsImportDir = this.root.resolve(ContactVcard.CONTACT_IMPORT);
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
	
	/*public void moveFileToArchive(Path entry) throws IOException {
		Path target = this.contactsArchive.resolve(entry.getFileName());
		Files.move(entry, target, StandardCopyOption.REPLACE_EXISTING);
	}*/
	
	public void exportContact(Contact contact) throws IOException {
		
		String filename = contact.getFirstname() + " " + contact.getLastname();
		
				
		try (BufferedWriter br = Files.newBufferedWriter(this.contactsExportDir.resolve(filename), StandardCharsets.UTF_8);) {
			br.write("BEGIN:VCARD\nVERSION:2.1\n");
			br.write("FN:" + filename + "\n");
			br.write("N:" + contact.getLastname() + ";" + contact.getFirstname() + ";;;\n");			
			
			if (!contact.getNickname().contentEquals("")) {
				br.write("NICKNAME:" + contact.getNickname() + "\n");
			}
			
			br.write("CATEGORIES:" + contact.getCategory().getName() + "\n");
			br.write("TEL;CELL:" + contact.getPhone() + "\n");
			
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
				br.write("NOTE:" + contact.getNotes() + "\n");
			}
			
			br.write("END:VCARD\n");
		}
	}
	
	public void exportAllContacts(List<Contact> contactsToExport) throws IOException {
		contactsToExport =  this.contactDao.listAllContacts();
		
		for (Contact contact : contactsToExport) {
			exportContact(contact);
		}
	}
	
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
				fileContent.put("ADR;HOME", fileContent.get("ADR;HOME") + " ;");
				String[] fullAddress =  fileContent.get("ADR;HOME").split(";");
				address = fullAddress[2] + "&&" + fullAddress[3] + "&&" + fullAddress[5] + "&&" + fullAddress[6]; 
			}
			
			if (fileContent.containsKey("EMAIL;INTERNET")) {
				mail = fileContent.get("EMAIL;INTERNET"); 
			}
			
			if (fileContent.containsKey("BDAY")) {
				birthdate = LocalDate.parse(fileContent.get("BDAY"));
			}
			
			if (fileContent.containsKey("NOTE")) {
				notes = fileContent.get("NOTE");
			}
			
			Contact importedContact = new Contact(lastname, firstname, nickname, address, birthdate, category, mail, phone, notes);
			this.contactDao.addContact(importedContact);
			
		}
	}
	
	public void importAllContacts() throws IOException, NotEnoughDataException {
		try (DirectoryStream<Path> contactsToImport = Files.newDirectoryStream(this.contactsImportDir)) {
			for(Path contact : contactsToImport) {
				String filename = contact.getFileName().toString();
				System.out.println(filename.toString());
				importContact(filename);
				Files.delete(contact);
			}
		}	
	}
			
}
	
