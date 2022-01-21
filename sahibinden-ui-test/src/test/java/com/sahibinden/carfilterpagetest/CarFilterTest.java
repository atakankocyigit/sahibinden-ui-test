package com.sahibinden.carfilterpagetest;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.sahibinden.base.TestUtilities;
import com.sahibinden.pages.CarFilterPage;
import com.sahibinden.pages.CarPage;
import com.sahibinden.pages.HomePage;

public class CarFilterTest extends TestUtilities{
	
	@Test()
	public void carFilterTest() {
		
		log.info("Start filter car test.");
		HomePage homePageInstance = new HomePage(driver, log);
		homePageInstance.openHomePage();
		homePageInstance.acceptCookie();
		homePageInstance.scrollToCarButton();
		Assert.assertTrue(homePageInstance.isButtonVÝsible(), "Car button not visible. Scroll failed.");
		
		CarPage carPageInstance =  homePageInstance.switchToCarPage();
		Assert.assertTrue(carPageInstance.isCarPageOpened(), "Car Page not opened.");
		
		CarFilterPage carFilterPageInstance = carPageInstance.chooseCarBrand(5);
		Assert.assertTrue(carFilterPageInstance.isCarFilterPageOpened(), "Car Page not opened.");
		
		Map<String,Object> data = getData();
		carFilterPageInstance.fillTheValuesAndSearch(data);
		
		Assert.assertTrue(carFilterPageInstance.isFilterBarColorTrue(), "actual color is false");
		Assert.assertTrue(carFilterPageInstance.isFilterBarValuesTrue("Km", data), "Km value is not in the interval");
		Assert.assertTrue(carFilterPageInstance.isFilterBarValuesTrue("Year", data), "Year value is not in the interval");
		Assert.assertTrue(carFilterPageInstance.isFilterBarValuesTrue("Price", data), "Price value is not in the interval");

		Assert.assertTrue(carFilterPageInstance.filteredCarsIsTrue(), "One of the values is not in the range!");
		log.info("carFilterTest successfully completed.");
	}
}
