package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseURL;

	String username = "Dan";
	String password = "12345";
	Note note;
	Note editedNote;
	Credential credential;
	Credential editedCredential;

	@Autowired
	private CredentialService credentialService;


	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();

	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
		note = new Note("Note Title", "Note Description");
		editedNote = new Note("Edited Title","Edited Description");
		credential = new Credential("www.facebook.com","Username","Password");
		editedCredential = new Credential("www.facebook.com","Username Edited","Password Edited");
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getHomePageUnauthorised() {
		driver.get(baseURL + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUserSignupAndLogin() throws InterruptedException {

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		Assertions.assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		homePage.logout(driver);

		Assertions.assertTrue(driver.getCurrentUrl().equals(baseURL + "/login"));

		driver.get(baseURL + "/home");
		Thread.sleep(1000);
		loginPage.isLoaded(driver);

		Assertions.assertEquals("Login", driver.getTitle());

	}

	@Test
	public void createAndViewNote() throws InterruptedException {

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		HomePage homePage = new HomePage(driver);
		homePage.createNote(note, driver);

		Note savedNote = homePage.getSavedNote(note, driver);

		Assertions.assertEquals(note.getNoteTitle(), savedNote.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), savedNote.getNoteDescription());

		homePage.deleteNote(note, driver);
	}

	@Test
	public void editNote() throws InterruptedException {

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		HomePage homePage = new HomePage(driver);
		homePage.createNote(note, driver);

		Note savedNote = homePage.getSavedNote(note, driver);

		Assertions.assertEquals(note.getNoteTitle(), savedNote.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), savedNote.getNoteDescription());

		homePage.editNote(editedNote, driver);

		savedNote = homePage.getSavedNote(editedNote, driver);

		Assertions.assertEquals(editedNote.getNoteTitle(), savedNote.getNoteTitle());
		Assertions.assertEquals(editedNote.getNoteDescription(), savedNote.getNoteDescription());

		homePage.deleteNote(editedNote, driver);
	}

	@Test
	public void deleteNote() throws InterruptedException {

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		HomePage homePage = new HomePage(driver);
		homePage.createNote(note, driver);

		Note savedNote = homePage.getSavedNote(note, driver);

		Assertions.assertEquals(note.getNoteTitle(), savedNote.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), savedNote.getNoteDescription());

		homePage.deleteNote(note, driver);

		savedNote = homePage.getSavedNote(note, driver);

		Assertions.assertNull(savedNote.getNoteTitle());

	}

	@Test
	public void createAndViewCredential() throws InterruptedException {

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		HomePage homePage = new HomePage(driver);
		homePage.createCredential(credential, driver);

		Credential savedCredential = homePage.getSavedCredential(credential, driver);

		Assertions.assertEquals(credential.getUrl(), savedCredential.getUrl());
		Assertions.assertEquals(credential.getUsername(), savedCredential.getUsername());
		Assertions.assertEquals(credentialService.getCredentialById(1).getPassword(), savedCredential.getPassword());

		homePage.deleteCredential(credential, driver);
	}

	@Test
	public void editCredential() throws InterruptedException {

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		HomePage homePage = new HomePage(driver);
		homePage.createCredential(credential, driver);

		Credential savedCredential = homePage.getSavedCredential(credential, driver);

		Assertions.assertEquals(credential.getUrl(), savedCredential.getUrl());
		Assertions.assertEquals(credential.getUsername(), savedCredential.getUsername());
		Assertions.assertEquals(credentialService.getCredentialById(1).getPassword(), savedCredential.getPassword());

		Assertions.assertTrue(homePage.checkCredentialPasswordUnencrypted(credential, driver));

		homePage.editCredential(editedCredential, driver);

		savedCredential = homePage.getSavedCredential(credential, driver);

		Assertions.assertEquals(editedCredential.getUrl(), savedCredential.getUrl());
		Assertions.assertEquals(editedCredential.getUsername(), savedCredential.getUsername());
		Assertions.assertEquals(credentialService.getCredentialById(1).getPassword(), savedCredential.getPassword());

		homePage.deleteCredential(credential, driver);
	}

	@Test
	public void deleteCredential() throws InterruptedException {

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Dan", "Pitman", username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		HomePage homePage = new HomePage(driver);
		homePage.createCredential(credential, driver);

		Credential savedCredential = homePage.getSavedCredential(credential, driver);

		Assertions.assertEquals(credential.getUrl(), savedCredential.getUrl());

		homePage.deleteCredential(savedCredential, driver);

		Assertions.assertNull(homePage.getSavedCredential(credential, driver).getUrl());

	}


}
