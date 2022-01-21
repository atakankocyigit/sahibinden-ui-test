package com.sahibinden.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CarFilterPage extends BasePageObject {
	
	//fields to enter filter values
	private Map<String, By> locators = Map.ofEntries(
		      Map.entry("minPrice", By.xpath("//div/input[@placeholder='min TL']")),
		      Map.entry("maxPrice", By.xpath("//div/input[@placeholder='max TL']")),
		      Map.entry("minYear", By.xpath("//input[@name='a5_min']")),
		      Map.entry("maxYear", By.xpath("//input[@name='a5_max']")),
		      Map.entry("minKm", By.xpath("//input[@name='a4_min']")),
		      Map.entry("maxKm", By.xpath("//input[@name='a4_max']")),
		      Map.entry("color", By.xpath("//div[@id='searchResultLeft-a3']/dl/dd/ul/div/div/li/div/a"))
	);
	
	//locators of values in the result bar after searching
	private Map<String, By> afterFilterLocators= Map.ofEntries(
			Map.entry("color", By.xpath("//a[@data-element='a3']")),
		      Map.entry("km", By.xpath("//a[@data-element='a4']")),
		      Map.entry("year", By.xpath("//a[@data-element='a5']")),
		      Map.entry("price", By.xpath("//a[@data-element='price']"))
	);
	
	//buttons to turn filtering fields on and off
	private By[] filterButtonLocators = new By[] {By.id("_cllpsID_a5"), By.id("_cllpsID_a4"), By.id("_cllpsID_a3"), By.id("_cllpsID_price")};
	
	//locator of the result field header
	private By resultAreaLocator = By.className("infoSearchResults");
	
	private By searchButton = By.xpath("//button[@class='btn btn-block search-submit']");
	
	//locators of the values of the cars in the list (year, km, color)
	private By resultListLocators = By.xpath("//td[@class='searchResultsAttributeValue']");
	//locators of the price of the cars in the list
	private By resultListPrice = By.xpath("//td[@class='searchResultsPriceValue']/div");
	//if an element is found, the locator required to find a parent element on that element.
	private By parentElement = By.xpath("./..");
	
	private String color;

	public CarFilterPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	//if the filter bar closed, click the filter button
	private void clickedFilterButton() {
		for(By filterButtonLocator: filterButtonLocators) {
			WebElement element = find(filterButtonLocator, 5);
			scrollUntilElementVisible(element);
			if(element.getAttribute("class").contains("collapseClosed"))
				clickByElement(findWithElement(element, parentElement), 5);
		}
	}

	//Fill price, km, year values
	private void fillFilterValuesWithData(String key, int value) {
		WebElement element = find(locators.get(key), 5);
		scrollUntilElementVisible(element);
		sendKeys(locators.get(key), Integer.toString(value), 5);
		log.info(key + ": " + value);
	}

	//Click selected color 
	private void selectColor(int colorNumber) {
		color = selectListElement(locators.get("color"), colorNumber).getAttribute("title");
		log.info("Click color: " + color);
	}
	
	//Are the values on the page correct after the filter values are entered?
	private String[] findFilterValuesOnPage(String key) {
		WebElement element = find(afterFilterLocators.get(key), 5);
		return element.getAttribute("title").replace(".", "").split(" ");
	}
	
	//Are the values of the listed vehicles correct?
	//The min max value may have been entered incorrectly by the user. Therefore, double-sided control is performed.
	private boolean areTheValuesInRange(String expectedValueMin, String expectedValueMax, WebElement actualValue, String key) {
		log.info("Expected "+ key +" values between: " + expectedValueMin + " and " + expectedValueMax);
		int actualValueInt = Integer.parseInt(actualValue.getText().toString().replaceAll("[^0-9]", ""));
		log.info("Actual " + key  + " value: " + actualValueInt);
		
		return (actualValueInt >= Integer.parseInt(expectedValueMin) && actualValueInt <= Integer.parseInt(expectedValueMax)) || (
				actualValueInt >= Integer.parseInt(expectedValueMax) && actualValueInt <= Integer.parseInt(expectedValueMin));
	}

	//is the page open?
	public boolean isCarFilterPageOpened() {
		return isElementVisibleByLocator(resultAreaLocator, 5);
	}

	//filter values are entered and the search button is pressed.
	public void fillTheValuesAndSearch(Map<String, Object> values) {
		clickedFilterButton();
		try {
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("color")) 
					selectColor((int) entry.getValue());
				else 
					fillFilterValuesWithData(entry.getKey(), (int) entry.getValue());
			}
			clickByLocator(searchButton, 5);
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
	}
	
	//are the values correct in the bar that opens after pressing the search button?
	public boolean isFilterBarValuesTrue(String key, Map<String, Object> values){
		Object min = values.get("min"+key);
		Object max = values.get("max"+key);
		
		WebElement element = find(afterFilterLocators.get(key.toLowerCase()), 5);
		log.info("Expected "+ key +": Between " + min + " and " + max);
		
		List<String> actualValues = new ArrayList<>(Arrays.asList(element.getAttribute("title").replace(".", "").split(" "))); 
		log.info("Actual "+ key+": " + actualValues.toString().replaceAll("[^0-9-]", ""));
		
		return actualValues.contains(min.toString()) && actualValues.contains(max.toString());
	}
	
	//is the color correct in the bar that opens after pressing the search button?
	public boolean isFilterBarColorTrue(){
		WebElement element = find(afterFilterLocators.get("color"), 5);
		log.info("Expected color: " + color);
		
		String actualColor = element.getAttribute("title").toLowerCase();
		log.info("Actual color: "+ actualColor);
		
		return element.getAttribute("title").toLowerCase().contains(color.toLowerCase());
	}

	//are the values of the vehicles listed in the correct range?
	public boolean filteredCarsIsTrue() {
		//the reason for splitting is after checking the accuracy of the values in the upper filter, the text there is taken. 
		//split the value, then the first value is saved as min and the second value as max.
		String[] km = findFilterValuesOnPage("km");
		String[] year = findFilterValuesOnPage("year");
		String[] price = findFilterValuesOnPage("price");
		
		List<WebElement> resultList = findAllElements(resultListLocators, 5);
		
		List<WebElement> resultPriceList = findAllElements(resultListPrice, 5);
		//checking values with a for loop of all cars
		//km, year and color are determined with the same locator. Therefore, the value of i is increased by 3 by 3. 
		//first value is year, second value is km, third value is color.
		for(int i = 0; i < resultList.size(); i+=filterButtonLocators.length-1) {
			if(!isElementVisible(resultList.get(i),5)) {
				log.info("Elements are invisible. Test fail.");
				return false;
			}
			else if(!areTheValuesInRange(year[0], year[2], resultList.get(i), "year") || //year range control
			   !areTheValuesInRange(km[0], km[2], resultList.get(i+1), "km") || //km range control
			   !areTheValuesInRange(price[0], price[2], resultPriceList.get(i/(filterButtonLocators.length-1)), "price") //price range control
			   ) {
				log.info("Values are not in range.");
				return false;
			}
			else if(!resultList.get(i+2).getText().strip().equalsIgnoreCase(color)) {
				log.info("Expected color: " + color + " but "
						+ "Actual color:" + resultList.get(i+2).getText().strip());
				return false;
			}
		}
		return true;
	}
}
