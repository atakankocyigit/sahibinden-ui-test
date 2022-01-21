package com.sahibinden.loginpagetest;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.sahibinden.base.BaseTest;
import com.sahibinden.pages.HomePage;
import com.sahibinden.pages.LoginPage;

public class NegativeLoginTest extends BaseTest{
	
	@Parameters({ "username", "password"})
	@Test
	public void negativeLoginTest(String username, String password) {
		log.info("Start negative login test");
		HomePage homePageInstance = new HomePage(driver, log);
		homePageInstance.openHomePage();
		homePageInstance.acceptCookie();
		
		LoginPage loginPageInstance =  homePageInstance.switchToLoginPage();
		Assert.assertTrue(loginPageInstance.isLoginPageOpened(), "Login Page not opened.");
		
		loginPageInstance.negativeLogin(username, password);
		boolean isErrorMessageAppear = loginPageInstance.isErrorMessageAppear();
		Assert.assertTrue(isErrorMessageAppear, "Negative Login Test failed! Error message didn't appear.");
		log.info("NegativeLogin test successfully completed.");
	}
}
