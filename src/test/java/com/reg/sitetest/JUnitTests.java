package com.reg.sitetest;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
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

public class JUnitTests {
	
	/////////////////////////// Attributes ///////////////////////////
	
	private WebDriver driver;
	private SitePOM sitePom;
	private Actions action;
	private WebDriverWait wait;
	private ExtentTest test;
	private static ExtentReports report = new ExtentReports(Constants.PATH +"Report.html", true);

	
	/////////////////////////// Surrounding-the-test methods ///////////////////////////
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
		driver.quit();
	}
	
	
	@AfterClass
	public static void teardownFull() {
		report.flush();
	}
	
	/////////////////////////// Tests ///////////////////////////
	
	@Test
	public void workingLink() throws InterruptedException {
		
		test = report.startTest("Testing main site navigation working");
		String expected = "https://vehicleenquiry.service.gov.uk/";
		
		driver.get(Constants.WEBSITEURL);

		sitePom.startIt(action, wait, expected);
		
		String actual = driver.getCurrentUrl();
		
		if (!actual.equals(expected)) {
			String imagePath = HelperMethods.screenshot(driver, Constants.PATH);
			test.log(LogStatus.INFO, "Screenshot has been captured" + test.addScreenCapture(imagePath));
			test.log(LogStatus.FAIL, "Did not reach required URL");
			report.endTest(test);
			report.flush();
		}
		
		assertEquals(expected, actual);
		test.log(LogStatus.PASS, "Navigation passed");
		
		report.endTest(test);
	}
	
	@Test
	public void testingData() {
		
		driver.get(Constants.SEARCHURL);
		
		for (int i = 1; i < ExcelUtils.getExcelWSheet().getPhysicalNumberOfRows(); i++) {
			test = report.startTest("Excel Data Test: " + i);
			test.log(LogStatus.INFO, "Set up Excel Utils path - Opened file stream");
		
			sitePom.submitReg(action, ExcelUtils.getCellData(i, 0), wait);
			test.log(LogStatus.INFO, "Submitted reg number");
			
			String imagePath = HelperMethods.screenshot(driver, Constants.PATH);
			test.log(LogStatus.INFO, "Screenshot has been captured" + test.addScreenCapture(imagePath));
			
			String expected = ExcelUtils.getCellData(i, 1);
			String actual = sitePom.getMake();
			
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected make of " + ExcelUtils.getCellData(i, 1));
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);
			}
			assertEquals(actual, expected);
			
			actual = sitePom.getColor();
			expected = ExcelUtils.getCellData(i, 2);
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected color of " + ExcelUtils.getCellData(i, 2));
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 3);
			}
			assertEquals(actual, expected);
			
			ExcelUtils.setCellData("Pass", i, 3);
			test.log(LogStatus.PASS, "Row " + i + " Passed");
			
			report.endTest(test);		
			sitePom.startAgain(action);
		}
	}


}
