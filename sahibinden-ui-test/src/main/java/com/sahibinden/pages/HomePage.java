package com.sahibinden.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePageObject{
	
	private String pageUrl= "https://www.sahibinden.com/";
	private By cookieLocator= By.id("onetrust-accept-btn-handler");
	private By carButtonLocator= By.xpath("//a[@title='Otomobil']");
	private By signInButton = By.id("secure-login");
	
	public HomePage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public void openHomePage() {
		log.info("Open home page");
		driver.get(pageUrl);
		log.info("Page opened.");
	}
	
	public void acceptCookie() {
		if(isElementVisibleByLocator(cookieLocator, 5)) {
			clickByLocator(cookieLocator, 5);
			log.info("Accept cookies");
		}
	}
	
	public void scrollToCarButton() {
		WebElement carButton = find(carButtonLocator, 5);
		scrollUntilElementVisible(carButton);
	}
	
	public boolean isButtonVÝsible() {
		return isElementVisibleByLocator(carButtonLocator, 5);
	}
	
	public CarPage switchToCarPage() {
		log.info("Click to car button");
		clickByLocator(carButtonLocator, 5);
		return new CarPage(driver, log);
	}
	
	public LoginPage switchToLoginPage() {
		clickByLocator(signInButton, 5);
		return new LoginPage(driver, log);
	}
}
