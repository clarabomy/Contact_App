package isen.java2.model.vcard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class ContactFilesStorage {

	private static final String CONTACT_DIR = "my_contacts";
	
	protected Path root;
	protected Path contactsArchive;

	public ContactFilesStorage(String root) throws IOException {

		if (root == null) {
			throw new IllegalArgumentException("You have to provide a root directory to process");
		}
		
		this.root = Paths.get(root); 
		this.contactsArchive = this.root.resolve(ContactFilesStorage.CONTACT_DIR);
		
		if (Files.notExists(this.contactsArchive)) {
			this.prepareDirectory("", this.contactsArchive);
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
	
	public void moveFileToArchive(Path entry) throws IOException {
		Path target = this.contactsArchive.resolve(entry.getFileName());
		Files.move(entry, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
}
