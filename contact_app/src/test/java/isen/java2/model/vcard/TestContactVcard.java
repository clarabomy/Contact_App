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

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContactVcard {

	@BeforeClass
	public static void Init() throws IOException {
		Files.createDirectory(Paths.get("D:\\tmp1"));
		Files.createDirectory(Paths.get("D:\\tmp2"));
		Files.createDirectory(Paths.get("D:\\tmp2\\contacts_export"));
		Files.createDirectory(Paths.get("D:\\tmp3"));
		Files.createDirectory(Paths.get("D:\\tmp3\\contacts_import"));
		Files.createDirectory(Paths.get("D:\\tmp1e"));
		Files.createDirectory(Paths.get("D:\\tmp2e"));
		Files.createDirectory(Paths.get("D:\\tmp3e"));
	}
	
	@Test
	public void SimpleTestContactVcard() throws IOException {
		String root1 = "D:\\tmp1";
		ContactVcard Vcard1 = new ContactVcard(root1);
		assertThat(Vcard1.root.toString()).isEqualTo(root1);
		assertThat(Vcard1.contactsExportDir).exists();
		assertThat(Vcard1.contactsImportDir).exists();
		
		String root2 = "D:\\tmp2";
		ContactVcard Vcard2 = new ContactVcard(root2);
		assertThat(Vcard2.root.toString()).isEqualTo(root2);
		assertThat(Vcard2.contactsExportDir).exists();
		assertThat(Vcard2.contactsImportDir).exists();
		
		String root3 = "D:\\tmp3";
		ContactVcard Vcard3 = new ContactVcard(root3);
		assertThat(Vcard3.root.toString()).isEqualTo(root3);
		assertThat(Vcard3.contactsExportDir).exists();
		assertThat(Vcard3.contactsImportDir).exists();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestContactVcardWithRootNull() throws IOException {
		String root = null;
		ContactVcard Vcard = new ContactVcard(root);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestContactVcardWithRooThatDoesNotExist() throws IOException {
		String root = "D:\\WrongDirectory";
		ContactVcard Vcard = new ContactVcard(root);
	}
	
	@Test
	public void TestExportContact() throws IOException {
		String root = "D:\\tmp1e";
		ContactVcard Vcard = new ContactVcard(root);
		Category category = new Category(1,"name");
		Contact contact = new Contact(1,"Jarosset","Corentin","cocobergine","digue&&lille&&59800&&france",LocalDate.of(1997, Month.NOVEMBER, 4), category,"cocobergine@nulos.fr","0655447788","commentaire");
		Vcard.exportContact(contact);
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
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void TestExportContactWithWrongAdress() throws IOException {
		String root = "D:\\tmp2e";
		ContactVcard Vcard = new ContactVcard(root);
		Category category = new Category(1,"name");
		Contact contact = new Contact(1,"Christiaens","Mathilde","titilde","madeleine",LocalDate.of(1997, Month.MAY, 5), category,"titilde@nulos.fr","0655447788","commentaire");
		Vcard.exportContact(contact);
	}
	
	@Test
	public void TestExportContactWithVariableEqualToNull() throws IOException {
		String root = "D:\\tmp3e";
		ContactVcard Vcard = new ContactVcard(root);
		Category category = new Category(1,"name");
		Contact contact1 = new Contact(1,"Christiaens1","Mathilde","","madeleine&&lille&&59800&&france",LocalDate.of(1997, Month.MAY, 5), category,"titilde@nulos.fr","0655447788","commentaire");
		Vcard.exportContact(contact1);
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
		Contact contact2 = new Contact(1,"Christiaens2","Mathilde","titilde","madeleine&&lille&&59800&&france",LocalDate.of(1997, Month.MAY, 5), category,"","0655447788","commentaire");
		Vcard.exportContact(contact2);
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
		Contact contact3 = new Contact(1,"Christiaens3","Mathilde","titilde","",LocalDate.of(1997, Month.MAY, 5), category,"titilde@nulos.fr","0655447788","commentaire");
		Vcard.exportContact(contact3);
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
	}
	
	
	@AfterClass
	public static void Destroy() throws IOException {
		FileUtils.deleteDirectory(new File("D:\\tmp1"));
		FileUtils.deleteDirectory(new File("D:\\tmp2"));
		FileUtils.deleteDirectory(new File("D:\\tmp3"));
		//Files.delete(Paths.get("D:\\tmp1e\\contacts_export\\Corentin Jarosset"));
		//FileUtils.deleteDirectory(new File("D:\\tmp1e"));
		//Files.delete(Paths.get("D:\\tmp2e\\contacts_export\\Mathilde Christiaens"));
		//FileUtils.deleteDirectory(new File("D:\\tmp2e"));
		//Files.delete(Paths.get("D:\\tmp3e\\contacts_export\\Mathilde Christiaens1"));
		//Files.delete(Paths.get("D:\\tmp3e\\contacts_export\\Mathilde Christiaens2"));
		//Files.delete(Paths.get("D:\\tmp3e\\contacts_export\\Mathilde Christiaens3"));
		//FileUtils.deleteDirectory(new File("D:\\tmp3e"));
	}
}