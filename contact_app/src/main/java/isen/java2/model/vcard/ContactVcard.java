package isen.java2.model.vcard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;


public class ContactVcard {

	private static final String CONTACT_EXPORT = "contacts_export";
	private static final String CONTACT_IMPORT = "contacts_import";

	
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
		
		String address = contact.getAddress() + "&&/";
		System.out.println(address);
		String[] addressParts = address.split("&&");
		String street = addressParts[0]; 
		String town = addressParts[1];
		String postalCode = addressParts[2];
		String country = addressParts[3];
				
		try (BufferedWriter br = Files.newBufferedWriter(this.contactsExportDir.resolve(filename), StandardCharsets.UTF_8);) {
			br.write("BEGIN:VCARD\nVERSION:2.1");
			br.write("FN:" + filename);
			br.write("N:" + contact.getLastname() + ";" + contact.getFirstname() + ";;;");			
			
			if (contact.getNickname() != null) {
				br.write("NICKNAME:" + contact.getNickname());
			}
			
			br.write("CATEGORIES:" + contact.getCategory().getName());
			br.write("TEL;CELL:" + contact.getPhone());
			br.write("ADR;HOME:;;" + street + ";" + town + ";;" + postalCode + ";" + country);
			
			if (contact.getMail() != null) {
				br.write("EMAIL;INTERNET:" + contact.getMail());
			}
			
			if (contact.getBirthdateKnown()!=null) {
				br.write("BDAY:" + contact.getBirthdate().toString());
			}
			
			if (contact.getNotes() != null) {
				br.write("NOTE:" + contact.getNotes() == null? contact.getNotes() : "");
			}
			
			br.write("END:VCARD");
		}
	}
	
	public void importContact(String filename) throws IOException {
		String firstname = "";
		String lastname = "";
		String nickname = "";
		String address = "";
		LocalDate birthdate = LocalDate.of(2000, Month.JANUARY, 1);
		Boolean birthdateKnown = false;
		Category category = new Category("Sans Catégorie");
		String mail = "";
		String phone = "";
		String notes = "";
		
		
		Map<String, String> fileContent = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(this.contactsImportDir.resolve(filename), StandardCharsets.UTF_8);) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] content = line.split(":");
				fileContent.put(content[0], content[1]);
			}
			
			if (!fileContent.containsKey("N") || !fileContent.containsKey("TEL;CELL")) {
				//throw exception : not enough data
			}
			
			if (fileContent.containsKey("N")) {
				String[] fullName = fileContent.get("N").split(";");
				lastname = fullName[0];
				firstname = fullName[1];
			}
			
			if (fileContent.containsKey("NICKNAME")) {
				nickname = fileContent.get("NICKNAME");
			}
			
			if (fileContent.containsKey("CATEGORIES")) {
				category.setName(fileContent.get("CATEGORIES"));
			}
			
		}
	}
}
	
