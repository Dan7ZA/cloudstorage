package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HomePage {

    private WebDriver driver;

    @FindBy(css="#logoutButton")
    private WebElement logoutButton;

    @FindBy(css="#nav-files-tab")
    private WebElement filesTab;

    @FindBy(css="#nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css="#nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(css="#new-note")
    private WebElement newNoteButton;

    @FindBy(css="#note-title")
    private WebElement noteTitleField;

    @FindBy(css="#note-description")
    private WebElement noteDescriptionField;

    @FindBy(css="#save-note")
    private WebElement noteSaveButton;

//    @FindBy(css="#userTable > tbody > tr > th")
//    private List<WebElement> savedNoteTitles;

    @FindBy(css=".noteTitle")
    private List<WebElement> savedNoteTitles;

    //#userTable > tbody > tr > th
    //#userTable > tbody > tr > td.noteDescription

    @FindBy(css=".noteDescription")
    private List<WebElement> savedNoteDescriptions;

/*    #userTable > tbody > tr > td:nth-child(1) > button
    #userTable > tbody > tr > td.noteDescription
    "[_noteid='1']"*/

    @FindBy(css=".edit-note-button")
    private List<WebElement> editNoteButton;

    @FindBy(css=".delete-note-button")
    private List<WebElement> deleteNoteButton;

    @FindBy(css="#new-credential")
    private WebElement newCredentialButton;

    @FindBy(css="#credential-url")
    private WebElement credentialUrlField;

    @FindBy(css="#credential-username")
    private WebElement credentialUsernameField;

    @FindBy(css="#credential-password")
    private WebElement credentialPasswordField;

    @FindBy(css="#save-credential")
    private WebElement credentialSaveButton;

    @FindBy(css=".credential-url")
    private List<WebElement> savedCredentialUrls;

    @FindBy(css=".credential-username")
    private List<WebElement> savedCredentialUsernames;

    @FindBy(css=".credential-password")
    private List<WebElement> savedCredentialPasswords;

    @FindBy(css=".edit-credential-button")
    private List<WebElement> editCredentialButton;

    @FindBy(css=".delete-credential-button")
    private List<WebElement> deleteCredentialButton;

    @FindBy(css="#cancel-credential")
    private WebElement credentialCancelButton;


    public void waitForPageLoad(WebDriver driver, WebElement element) throws InterruptedException {
        Thread.sleep(1000);
        new WebDriverWait(driver, 2).until(ExpectedConditions.elementToBeClickable(element));
        Thread.sleep(1000);
    }


    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout(WebDriver driver) throws InterruptedException {
        waitForPageLoad(driver, logoutButton);
        this.logoutButton.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.urlContains("login"));
        Thread.sleep(1000);
    }

    public void createNote(Note note, WebDriver driver) throws InterruptedException {
        waitForPageLoad(driver, logoutButton);
        this.notesTab.click();
        //Thread.sleep(2000);
        waitForPageLoad(driver, newNoteButton);
        this.newNoteButton.click();
        //Thread.sleep(2000);
        waitForPageLoad(driver, noteTitleField);
        this.noteTitleField.sendKeys(note.getNoteTitle());
        this.noteDescriptionField.sendKeys(note.getNoteDescription());
        //waitForPageLoad(driver, noteSaveButton);
        this.noteSaveButton.click();
    }

    public Note getSavedNote(Note note, WebDriver driver) throws InterruptedException {

        String savedNoteTitle;
        String savedNoteDescription;

        waitForPageLoad(driver, logoutButton);
        this.notesTab.click();
        //Thread.sleep(2000);
        waitForPageLoad(driver, newNoteButton);

        Note savedNote = new Note();

       try{
           savedNoteTitle = this.savedNoteTitles.get(0).getText();
           savedNoteDescription = this.savedNoteDescriptions.get(0).getText();
       } catch(IndexOutOfBoundsException e){
           return savedNote;
       }

        savedNote.setNoteTitle(savedNoteTitle);
        savedNote.setNoteDescription(savedNoteDescription);
        return savedNote;
    }

    public void editNote(Note note, WebDriver driver) throws InterruptedException {

        waitForPageLoad(driver, logoutButton);
        this.notesTab.click();
        waitForPageLoad(driver, newNoteButton);

        this.editNoteButton.get(0).click();
        waitForPageLoad(driver, noteTitleField);


        this.noteTitleField.clear();
        this.noteTitleField.sendKeys(note.getNoteTitle());
        this.noteDescriptionField.clear();
        this.noteDescriptionField.sendKeys(note.getNoteDescription());
        this.noteSaveButton.click();

    }

    public void deleteNote(Note note, WebDriver driver) throws InterruptedException {
        waitForPageLoad(driver, logoutButton);
        this.notesTab.click();
        //Thread.sleep(2000);
        waitForPageLoad(driver, newNoteButton);

        this.deleteNoteButton.get(0).click();
    }

    public void createCredential(Credential credential,WebDriver driver) throws InterruptedException {

        waitForPageLoad(driver, logoutButton);
        this.credentialsTab.click();
        //Thread.sleep(2000);
        waitForPageLoad(driver, newCredentialButton);
        this.newCredentialButton.click();
        //Thread.sleep(2000);
        waitForPageLoad(driver, credentialUrlField);

        this.credentialUrlField.sendKeys(credential.getUrl());
        this.credentialUsernameField.sendKeys(credential.getUsername());
        this.credentialPasswordField.sendKeys(credential.getPassword());
        this.credentialSaveButton.click();

    }

    public Credential getSavedCredential(Credential credential,WebDriver driver) throws InterruptedException {

        waitForPageLoad(driver, logoutButton);
        this.credentialsTab.click();
        waitForPageLoad(driver, newCredentialButton);

        Credential savedCredential = new Credential();

        try{
            URI uri = new URI(this.deleteCredentialButton.get(0).getAttribute("href"));
            String path = uri.getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            int id = Integer.parseInt(idStr);

            savedCredential.setCredentialId(id);
            savedCredential.setUrl(this.savedCredentialUrls.get(0).getText());
            savedCredential.setUsername(this.savedCredentialUsernames.get(0).getText());
            savedCredential.setPassword(this.savedCredentialPasswords.get(0).getText());

        } catch(IndexOutOfBoundsException e){
            return savedCredential;
        } catch (URISyntaxException e) {
            System.out.println("The system encountered an error" + e);
        }

        return savedCredential;

    }

    public Boolean checkCredentialPasswordUnencrypted(Credential credential, WebDriver driver) throws InterruptedException {

        waitForPageLoad(driver, logoutButton);
        this.credentialsTab.click();
        this.editCredentialButton.get(0).click();

        waitForPageLoad(driver, credentialUrlField);

        String unencryptedPassword = this.credentialPasswordField.getAttribute("value");
        this.credentialCancelButton.click();

        if(unencryptedPassword.equals(credential.getPassword())){
            return true;
        } else {
            return false;
        }

    }

    public void editCredential(Credential credential, WebDriver driver) throws InterruptedException {

        waitForPageLoad(driver, logoutButton);
        this.credentialsTab.click();
        this.editCredentialButton.get(0).click();

        waitForPageLoad(driver, credentialUrlField);

        this.credentialUrlField.clear();
        this.credentialUrlField.sendKeys(credential.getUrl());
        this.credentialUsernameField.clear();
        this.credentialUsernameField.sendKeys(credential.getUsername());
        this.credentialPasswordField.clear();
        this.credentialPasswordField.sendKeys(credential.getPassword());
        this.credentialSaveButton.click();
    }

    public void deleteCredential(Credential credential, WebDriver driver) throws InterruptedException {
        this.credentialsTab.click();
        this.deleteCredentialButton.get(0).click();
    }

}
