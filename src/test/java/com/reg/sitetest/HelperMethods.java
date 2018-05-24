
package com.reg.sitetest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class HelperMethods {

	public static String screenshot(WebDriver driver, String path) {

		String dateName = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());

		TakesScreenshot ts = (TakesScreenshot) driver;

		File source = ts.getScreenshotAs(OutputType.FILE);

		String destination = path + "/ScreenShots/Pass" + dateName + ".png";

		File finalDestination = new File(destination);

		try {
			FileHandler.copy(source, finalDestination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return path + "/ScreenShots/Pass" + dateName + ".png";
	}

}
