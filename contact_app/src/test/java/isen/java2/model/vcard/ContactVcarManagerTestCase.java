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
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Corentin Jarosset
 * 
 *         Test class used to test the behavior of the ContactVcardManager Class. This
 *         class replaces the application class we used before, and becomes our
 *         entry point into the program
 */
public class ContactVcarManagerTestCase {
	private static String rootDir = "C:\\tmp\\java2\\testVcard\\"; 

	@BeforeClass
	public static void init() throws IOException {
		Files.createDirectory(Paths.get("C:\\tmp"));
		Files.createDirectory(Paths.get("C:\\tmp\\java2"));
		Files.createDirectory(Paths.get("C:\\tmp\\java2\\testVcard"));
		Files.createDirectory(Paths.get(rootDir+"tmp1"));
		Files.createDirectory(Paths.get(rootDir+"tmp2"));
		Files.createDirectory(Paths.get(rootDir+"tmp2\\contacts_export"));
		Files.createDirectory(Paths.get(rootDir+"tmp3"));
		Files.createDirectory(Paths.get(rootDir+"tmp3\\contacts_import"));
	}
	
	@Test
	public void simpleTestContactVcardManager() throws IOException {
		String root1 = rootDir+"tmp1";
		ContactVcardManager vcard1 = new ContactVcardManager(root1);
		assertThat(vcard1.root.toString()).isEqualTo(root1);
		assertThat(vcard1.contactsExportDir).exists();
		assertThat(vcard1.contactsImportDir).exists();
		
		String root2 = rootDir+"tmp2";
		ContactVcardManager vcard2 = new ContactVcardManager(root2);
		assertThat(vcard2.root.toString()).isEqualTo(root2);
		assertThat(vcard2.contactsExportDir).exists();
		assertThat(vcard2.contactsImportDir).exists();
		
		String root3 = rootDir+"tmp3";
		ContactVcardManager vcard3 = new ContactVcardManager(root3);
		assertThat(vcard3.root.toString()).isEqualTo(root3);
		assertThat(vcard3.contactsExportDir).exists();
		assertThat(vcard3.contactsImportDir).exists();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContactVcardManagerWithRootNull() throws IOException {
		String root = null;
		ContactVcardManager vcard = new ContactVcardManager(root);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContactVcardManagerWithRooThatDoesNotExist() throws IOException {
		String root = rootDir+"WrongDirectory";
		ContactVcardManager vcard = new ContactVcardManager(root);
	}
	
	@Test
	public void testExportContact() throws IOException {
		String root = rootDir+"tmp1";
		ContactVcardManager vcard = new ContactVcardManager(root);
		Category category = new Category(1,"name");
		Contact contact = new Contact(1,"Jarosset","Corentin","cocobergine","digue&&lille&&59800&&france",LocalDate.of(1997, Month.NOVEMBER, 4), category,"cocobergine@nulos.fr","0655447788","commentaire");
		vcard.exportContact(contact);
		BufferedReader br = new BufferedReader(new FileReader(new File(root+"\\contacts_export\\"+contact.getFirstname()+" "+contact.getLastname())));
		assertThat(br.readLine()).isEqualTo("BEGIN:VCARD");
		assertThat(br.readLine()).isEqualTo("VERSION:2.1");
		assertThat(br.readLine()).isEqualTo("FN:Corentin Jarosset");
		assertThat(br.readLine()).isEqualTo("N:Jarosset;Corentin;;;");
		assertThat(br.readLine()).isEqualTo("NICKNAME:cocobergine");
		assertThat(br.readLine()).isEqualTo("CATEGORIES:name");
		assertThat(br.readLine()).isEqualTo("TEL;CELL:0655447788");
		assertThat(br.readLine()).isEqualTo("ADR;HOME:;;digue;59800;;lille;france");
		assertThat(br.readLine()).isEqualTo("EMAIL;INTERNET:cocobergine@nulos.fr");
		assertThat(br.readLine()).isEqualTo("BDAY:1997-11-04");
		assertThat(br.readLine()).isEqualTo("NOTE:commentaire");
		assertThat(br.readLine()).isEqualTo("END:VCARD");
		br.close();
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testExportContactWithWrongAdress() throws IOException {
		String root = rootDir+"tmp2";
		ContactVcardManager vcard = new ContactVcardManager(root);
		Category category = new Category(1,"name");
		Contact contact = new Contact(1,"Christiaens","Mathilde","titilde","madeleine",LocalDate.of(1997, Month.MAY, 5), category,"titilde@nulos.fr","0655447788","commentaire");
		vcard.exportContact(contact);
	}
	
	@Test
	public void testExportContactWithVariableEqualToNull() throws IOException {
		String root = rootDir+"tmp3";
		ContactVcardManager vcard = new ContactVcardManager(root);
		Category category = new Category(1,"name");
		Contact contact1 = new Contact(1,"Christiaens1","Mathilde","","madeleine&&lille&&59800&&france",LocalDate.of(1997, Month.MAY, 5), category,"titilde@nulos.fr","0655447788","commentaire");
		vcard.exportContact(contact1);
		BufferedReader br = new BufferedReader(new FileReader(new File(root+"\\contacts_export\\"+contact1.getFirstname()+" "+contact1.getLastname())));
		assertThat(br.readLine()).isEqualTo("BEGIN:VCARD");
		assertThat(br.readLine()).isEqualTo("VERSION:2.1");
		assertThat(br.readLine()).isEqualTo("FN:Mathilde Christiaens1");
		assertThat(br.readLine()).isEqualTo("N:Christiaens1;Mathilde;;;");
		assertThat(br.readLine()).isEqualTo("CATEGORIES:name");
		assertThat(br.readLine()).isEqualTo("TEL;CELL:0655447788");
		assertThat(br.readLine()).isEqualTo("ADR;HOME:;;madeleine;59800;;lille;france");
		assertThat(br.readLine()).isEqualTo("EMAIL;INTERNET:titilde@nulos.fr");
		assertThat(br.readLine()).isEqualTo("BDAY:1997-05-05");
		assertThat(br.readLine()).isEqualTo("NOTE:commentaire");
		assertThat(br.readLine()).isEqualTo("END:VCARD");
		br.close();
		Contact contact2 = new Contact(1,"Christiaens2","Mathilde","titilde","madeleine&&lille&&59800&&france",LocalDate.of(1997, Month.MAY, 5), category,"","0655447788","commentaire");
		vcard.exportContact(contact2);
		br = new BufferedReader(new FileReader(new File(root+"\\contacts_export\\"+contact2.getFirstname()+" "+contact2.getLastname())));
		assertThat(br.readLine()).isEqualTo("BEGIN:VCARD");
		assertThat(br.readLine()).isEqualTo("VERSION:2.1");
		assertThat(br.readLine()).isEqualTo("FN:Mathilde Christiaens2");
		assertThat(br.readLine()).isEqualTo("N:Christiaens2;Mathilde;;;");
		assertThat(br.readLine()).isEqualTo("NICKNAME:titilde");
		assertThat(br.readLine()).isEqualTo("CATEGORIES:name");
		assertThat(br.readLine()).isEqualTo("TEL;CELL:0655447788");
		assertThat(br.readLine()).isEqualTo("ADR;HOME:;;madeleine;59800;;lille;france");
		assertThat(br.readLine()).isEqualTo("BDAY:1997-05-05");
		assertThat(br.readLine()).isEqualTo("NOTE:commentaire");
		assertThat(br.readLine()).isEqualTo("END:VCARD");
		br.close();
		Contact contact3 = new Contact(1,"Christiaens3","Mathilde","titilde","",LocalDate.of(1997, Month.MAY, 5), category,"titilde@nulos.fr","0655447788","commentaire");
		vcard.exportContact(contact3);
		br = new BufferedReader(new FileReader(new File(root+"\\contacts_export\\"+contact3.getFirstname()+" "+contact3.getLastname())));
		assertThat(br.readLine()).isEqualTo("BEGIN:VCARD");
		assertThat(br.readLine()).isEqualTo("VERSION:2.1");
		assertThat(br.readLine()).isEqualTo("FN:Mathilde Christiaens3");
		assertThat(br.readLine()).isEqualTo("N:Christiaens3;Mathilde;;;");
		assertThat(br.readLine()).isEqualTo("NICKNAME:titilde");
		assertThat(br.readLine()).isEqualTo("CATEGORIES:name");
		assertThat(br.readLine()).isEqualTo("TEL;CELL:0655447788");
		assertThat(br.readLine()).isEqualTo("EMAIL;INTERNET:titilde@nulos.fr");
		assertThat(br.readLine()).isEqualTo("BDAY:1997-05-05");
		assertThat(br.readLine()).isEqualTo("NOTE:commentaire");
		assertThat(br.readLine()).isEqualTo("END:VCARD");
		br.close();
	}
	
	
	@AfterClass
	public static void destroy() throws IOException {
		Files.delete(Paths.get(rootDir+"tmp1\\contacts_export\\Corentin Jarosset"));
		FileUtils.deleteDirectory(new File(rootDir+"tmp1"));
		Files.delete(Paths.get(rootDir+"tmp2\\contacts_export\\Mathilde Christiaens"));
		FileUtils.deleteDirectory(new File(rootDir+"tmp2"));
		Files.delete(Paths.get(rootDir+"tmp3\\contacts_export\\Mathilde Christiaens1"));
		Files.delete(Paths.get(rootDir+"tmp3\\contacts_export\\Mathilde Christiaens2"));
		Files.delete(Paths.get(rootDir+"tmp3\\contacts_export\\Mathilde Christiaens3"));
		FileUtils.deleteDirectory(new File(rootDir+"tmp3"));
	}
}