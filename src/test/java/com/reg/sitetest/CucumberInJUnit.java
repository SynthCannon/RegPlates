package com.reg.sitetest;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.reg.site.Constants;
import com.reg.site.ExcelUtils;
import com.reg.site.SitePOM;

public class CucumberInJUnit {
	
	/////////////////////////// Attributes ///////////////////////////
	
	private WebDriver driver;
	private SitePOM sitePom;
	private Actions action;
	private WebDriverWait wait;
	private ExtentTest test;
	private static ExtentReports report = new ExtentReports(Constants.PATH + Constants.REPORT_PATH + "report.html", true);
	
	
	/////////////////////////// Cucumber before/after method replacement methods ///////////////////////////
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", Constants.PATH + Constants.CHROME_DRIVER_PATH + Constants.CHROME_DRIVER);
		driver = new ChromeDriver();
		driver.manage().window();
		
		sitePom = PageFactory.initElements(driver, SitePOM.class);
		action = new Actions(driver);
		wait = new WebDriverWait(driver, 10);
		
		ExcelUtils.setExcelFile(Constants.PATH + Constants.TEST_DATA, 0);
	}
	
	@After
	public void teardown() {
		driver.quit();
	}
	
	@AfterClass
	public static void teardownFull() {
		report.flush();
	}
	
	/////////////////////////// Cucumber replacement tests ///////////////////////////
	
	@Test
	public void workingLink() throws InterruptedException {
		
		test = report.startTest("Testing main site navigation working");
		
		driver.get(Constants.WEBSITE_URL);

		try {
			sitePom.startIt(wait, Constants.ENQUIRY_URL);
		} catch (TimeoutException e) {
			test.log(LogStatus.FAIL, "Did not reach any URL");
			return;
		}
		
		String actual = driver.getCurrentUrl();
		String expected = Constants.ENQUIRY_URL;

		test.log(LogStatus.INFO, "Screenshot has been captured"
				+ test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));

		if (!actual.equals(expected)) {
			test.log(LogStatus.FAIL, "Did not reach required URL");
		} else {
			test.log(LogStatus.PASS, "Navigation passed");
		}
		
		assertEquals(actual,expected);
		
		report.endTest(test);
	}
	
	@Test
	public void testingData() {

		driver.get(Constants.SEARCH_URL);
		
		for (int i = 1; i < ExcelUtils.getExcelWSheet().getPhysicalNumberOfRows(); i++) {
			test = report.startTest("Excel Data Test: " + i);
			test.log(LogStatus.INFO, "Set up Excel Utils path - Opened file stream");

			try {
				sitePom.submitReg(action, ExcelUtils.getCellData(i, 0), wait);
			} catch (TimeoutException e) {
				test.log(LogStatus.INFO, "Screenshot has been captured"
						+ test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));
				test.log(LogStatus.FAIL, "Did not find elements in page");
				return;
			}

			test.log(LogStatus.INFO, "Submitted reg number");
			String actual = "";
			try {
				actual = sitePom.getMake(wait);
			} catch (TimeoutException e) {
				test.log(LogStatus.INFO, "Screenshot has been captured"
						+ test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));
				test.log(LogStatus.FAIL, "Did not find number plate in database");
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);

				return;
			}
			
			test.log(LogStatus.INFO, "Screenshot has been captured"
					+ test.addScreenCapture(HelperMethods.screenshot(driver, Constants.PATH)));

			String expected = ExcelUtils.getCellData(i, 1);
			
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected make of " + ExcelUtils.getCellData(i, 1));
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);

				return;
			}
			assertEquals(actual,expected);

			actual = sitePom.getColor();
			expected = ExcelUtils.getCellData(i, 2);
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected color of " + ExcelUtils.getCellData(i, 2));
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);

				return;
			}
			assertEquals(actual,expected);

			ExcelUtils.setCellData("Pass", i, 3);
			test.log(LogStatus.PASS, "Row " + i + " Passed");

			report.endTest(test);
			sitePom.startAgain();
		}
	}


}
