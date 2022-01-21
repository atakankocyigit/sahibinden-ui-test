package com.sahibinden.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePageObject{
	
	private By loginButtonLocator= By.id("userLoginSubmitButton");
	private By usernameLocator= By.id("username");
	private By passwordLocator = By.id("password");
	private By errorMessageLocator = By.xpath("//span[@class='error']");
	
	public LoginPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public boolean isLoginPageOpened() {
		return isElementVisibleByLocator(loginButtonLocator, 5);
	}
	
	public void negativeLogin(String username, String password) {
		sendKeys(usernameLocator, username, 5);
		log.info("Username entered the following value: " + username);
		sendKeys(passwordLocator, password, 5);
		log.info("Password entered the following value: " + password);
		clickByLocator(loginButtonLocator, 5);
	}
	
	public boolean isErrorMessageAppear(){
		return isElementVisibleByLocator(errorMessageLocator, 5);
	}
}
