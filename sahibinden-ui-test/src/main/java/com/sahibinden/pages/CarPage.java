package com.sahibinden.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CarPage extends BasePageObject{
	
	private By carsBrandLocator= By.xpath("//li/a/h2");
	public CarPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public CarFilterPage chooseCarBrand(int brandNumber) {
		selectListElement(carsBrandLocator, brandNumber);
		log.info("Click car brand: " + brandNumber);
		return new CarFilterPage(driver,log);
	}
	
	public boolean isCarPageOpened() {
		return isElementVisibleByLocator(carsBrandLocator, 5);
	}
}
