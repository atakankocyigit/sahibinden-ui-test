package com.sahibinden.pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePageObject {

	protected WebDriver driver;
	protected Logger log;
	
	protected BasePageObject(WebDriver driver, Logger log) {
		this.driver=driver;
		this.log=log;
	}
	
	protected WebElement find(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}	
	
	//finding another element over a previously found element.
	protected WebElement findWithElement(WebElement element, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		return wait.until(ExpectedConditions.visibilityOf(element.findElement(locator)));
	}

	protected List<WebElement> findAllElements(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}
	
	protected boolean isElementVisible(WebElement element, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
	}

	protected boolean isElementVisibleByLocator(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
	}
	protected void clickByLocator(By locator, int timeout) {		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	protected void clickByElement(WebElement element, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.visibilityOf(element)).click();;
	}
	
	protected void scrollUntilElementVisible(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
	}
	
	protected void sendKeys(By locator, String value, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).sendKeys(value);
	}
	
	protected WebElement selectListElement(By locator, int choosenElementNumber) {
		List<WebElement> elements= findAllElements(locator, 5);
		if(elements.size() >= choosenElementNumber && choosenElementNumber > 0) {
			scrollUntilElementVisible(elements.get(choosenElementNumber-1));
			log.info("Click: " + elements.get(choosenElementNumber-1).getText());
			clickByElement(elements.get(choosenElementNumber-1), 5);
		}
		else 
			throw new IndexOutOfBoundsException("You choose number: " + choosenElementNumber + " but there are  "+ elements.size()+ " elements");
		return elements.get(choosenElementNumber-1);
	}
}
