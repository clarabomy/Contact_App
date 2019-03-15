package isen.java2.model.vcard;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactVcardTestCase {

	private ContactVcard contactVcard;
	// You can adapt the path on your filesystem here
	private String rootDir = "/src/test/ressources/vcardTest/";
	
	@Before
	public void init() throws IOException {		
		// Creating the test directory
		Path rootDirPath = Paths.get(rootDir);
		Files.createDirectories(rootDirPath);
		contactVcard = new ContactVcard(rootDir);
		Files.createDirectory(Paths.get("D:\\tmp2\\contacts_export")); 
		Files.createDirectory(Paths.get("tmp3")); 
		Files.createDirectory(Paths.get("D:\\tmp3\\contacts_import")); 
		Files.createDirectory(Paths.get("D:\\tmp1e")); 
		Files.createDirectory(Paths.get("D:\\tmp2e")); 
		Files.createDirectory(Paths.get("D:\\tmp3e")); 
	}
	
	@Test
	public void simpleTestContactVcard() throws IOException {
		int test = 1;
	}
}