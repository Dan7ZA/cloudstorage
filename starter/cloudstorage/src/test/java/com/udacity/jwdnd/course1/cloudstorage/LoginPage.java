package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    @FindBy(css="#inputUsername")
    private WebElement usernameField;

    @FindBy(css="#inputPassword")
    private WebElement passwordField;

    @FindBy(css="#submit-button")
    private WebElement submitButton;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void waitForPageLoad(WebDriver driver, WebElement element) throws InterruptedException {
        //WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait.until(webDriver -> webDriver.findElement(By.id(elementId)));
        Thread.sleep(2000);
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(element));
        Thread.sleep(2000);
    }

    public void login(String username, String password) {
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
        this.submitButton.click();
    }

    public Boolean isLoaded(WebDriver driver){
        try{
            waitForPageLoad(driver, usernameField);
        }
        catch (InterruptedException e){
            return false;
        }
      return true;
    }

}
