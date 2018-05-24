package com.reg.sitetest;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.reg.site.Constants;
import com.reg.site.ExcelUtils;
import com.reg.site.SitePOM;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CucumberTestsSteps {

	/////////////////////////// Attributes ///////////////////////////

	private WebDriver driver;
	private SitePOM sitePom;
	private Actions action;
	private WebDriverWait wait;
	private ExtentTest test;
	private ExtentReports report;
	

	/////////////////////////// Surrounding the test methods ///////////////////////////
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVERPATH);
		driver = new ChromeDriver();
		driver.manage().window();

		sitePom = PageFactory.initElements(driver, SitePOM.class);
		action = new Actions(driver);
		wait = new WebDriverWait(driver, 10);

		ExcelUtils.setExcelFile(Constants.PATH + Constants.TESTDATA, 0);
	}

	@After
	public void teardown() {
		report.flush();
		driver.quit();
	}

	/////////////////////////// Scenario 1 ///////////////////////////
	
	@Given("^I want to reach the search page from the provided URL$")
	public void i_want_to_reach_the_search_page_from_the_provided_URL() throws Throwable {
		report = new ExtentReports(Constants.PATH + "\\Reports\\Navigation Report.html", true);
		driver.get(Constants.WEBSITEURL);
	}

	@When("^I click the search button$")
	public void i_click_the_search_button() throws Throwable {
		test = report.startTest("Testing main site navigation working");
		
		try {
			sitePom.startIt(action, wait, "https://vehicleenquiry.service.gov.uk/");
		} catch (TimeoutException e) {
			test.log(LogStatus.FAIL, "Did not reach any URL");
			return;
		}
		
	}

	@Then("^I reach the search page$")
	public void i_reach_the_search_page() throws Throwable {
		String actual = driver.getCurrentUrl();
		String expected = "https://vehicleenquiry.service.gov.uk/";
		
		test.log(LogStatus.INFO, "Screenshot has been captured" + test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));
		
		if (!actual.equals(expected)) {
			test.log(LogStatus.FAIL, "Did not reach required URL");
		} else {
			test.log(LogStatus.PASS, "Navigation passed");
		}
		
		report.endTest(test);
	}
	
	/////////////////////////// Scenario 2 ///////////////////////////
	
	@Given("^I want to look up various number plates$")
	public void i_want_to_look_up_various_number_plates() throws Throwable {
		report = new ExtentReports(Constants.PATH + "\\Reports\\Data Search Report.html", true);
		driver.get(Constants.SEARCHURL);
	}

	@When("^I enter these number plates$")
	public void i_enter_these_number_plates() throws Throwable {
		ExcelUtils.setExcelFile(Constants.PATH + Constants.TESTDATA, 0);
	}

	@Then("^I find the correct information$")
	public void i_find_the_correct_information() throws Throwable {
		
			
		for (int i = 1; i < ExcelUtils.getExcelWSheet().getPhysicalNumberOfRows(); i++) {
			test = report.startTest("Excel Data Test: " + i);
			test.log(LogStatus.INFO, "Set up Excel Utils path - Opened file stream");
		
			try {
				sitePom.submitReg(action, ExcelUtils.getCellData(i, 0), wait);
			} catch (TimeoutException e) {
				test.log(LogStatus.INFO, "Screenshot has been captured" + test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));
				test.log(LogStatus.FAIL, "Did not find elements in page");
				return;
			}
			
			test.log(LogStatus.INFO, "Submitted reg number");
			String actual = "";	
			try {
				actual = sitePom.getMake(wait);
			} catch (TimeoutException e) {
				test.log(LogStatus.INFO, "Screenshot has been captured" + test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));
				test.log(LogStatus.FAIL, "Did not find number plate in database");
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);
				
				return;
			}
			
			test.log(LogStatus.INFO, "Screenshot has been captured" + test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));
			
			String expected = ExcelUtils.getCellData(i, 1);	
			
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected make of " + ExcelUtils.getCellData(i, 1));
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);
				
				return;
			}
			
			actual = sitePom.getColor();
			expected = ExcelUtils.getCellData(i, 2);
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected color of " + ExcelUtils.getCellData(i, 2));
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);
				
				return;
			}
			
			ExcelUtils.setCellData("Pass", i, 3);
			test.log(LogStatus.PASS, "Row " + i + " Passed");
			
			report.endTest(test);		
			sitePom.startAgain(action);
		}

	}

}
