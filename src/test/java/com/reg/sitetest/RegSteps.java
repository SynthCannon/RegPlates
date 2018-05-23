package com.reg.sitetest;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.reg.site.Constants;
import com.reg.site.ExcelUtils;
import com.reg.site.SitePOM;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class RegSteps {
	
	private WebDriver driver;
	private ExtentReports report;
	private ExtentTest test;
	private Actions action;
	private XSSFWorkbook workbook;
	private SitePOM sitePom;
	
	//@Parameters
	//public static List<Integer[]> data() {
	
	
	
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVERPATH);
		driver = new ChromeDriver();
		driver.manage().window();
		
		action = new Actions(driver);
		
		report = new ExtentReports(Constants.PATH +"Report.html", true);
		
		sitePom = PageFactory.initElements(driver, SitePOM.class);
		
		ExcelUtils.setExcelFile(Constants.PATH + Constants.TESTDATA, 0);
		
		
	}
	

	@After
	public void teardown() {
		driver.quit();
		report.endTest(test);
		report.flush();
		// Need to quit report?
	}
	
	
//	@AfterClass
//	public static void teardownFull() {
//		report.endTest(test);
//		report.flush();
//	}
	
	@Ignore
	@Test
	public void workingLink() {
		test = report.startTest("Testing main site navigation working");
		
		driver.get(Constants.WEBSITEURL);

		sitePom.startIt(action);

		
		String expected = "https://vehicleenquiry.service.gov.uk/";
		String actual = driver.getCurrentUrl();
		assertEquals(expected, actual);
		
		report.endTest(test);
	}
	
	@Test
	public void testingData() throws InterruptedException {
		
		driver.get(Constants.SEARCHURL);
		
		for (int i = 1; i < ExcelUtils.getExcelWSheet().getPhysicalNumberOfRows(); i++) {
			test = report.startTest("Excel Data Test: " + i);
			test.log(LogStatus.INFO, "Set up Excel Utils path - Opened file stream");
		
			sitePom.submitReg(action, ExcelUtils.getCellData(i, 0));
			test.log(LogStatus.INFO, "Submitted reg number");

			Thread.sleep(2000);
			
			String actual = sitePom.getColor();
			String expected = ExcelUtils.getCellData(i, 1);
			if (!actual.equals(expected)) {
				test.log(LogStatus.FAIL, "Did not recieve expected color");
				report.endTest(test);
				report.flush();
				ExcelUtils.setCellData("Fail", i, 2);
			}
			assertEquals(actual, expected);
			
			ExcelUtils.setCellData("Pass", i, 2);
			test.log(LogStatus.PASS, "Row " + i + " Passed");
			
			
		}
	}


}
