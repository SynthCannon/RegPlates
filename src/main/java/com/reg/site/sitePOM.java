package com.reg.site;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SitePOM {
	
	@FindBy(xpath = "//*[@id=\"get-started\"]/a")
	private WebElement startNowBtn;
	
	@FindBy(xpath = "//*[@id=\"content\"]/form/div/div/div[2]/fieldset/div")
	private WebElement form;
	
	@FindBy(xpath = "//*[@id=\"content\"]/form/div/div/div[2]/fieldset/button")
	private WebElement continueBtn1;
	
	@FindBy(xpath = "//*[@id=\"pr3\"]/div/ul/li[2]/span[2]/strong")
	private WebElement makeLbl;
	
	@FindBy(xpath = "//*[@id=\"pr3\"]/div/ul/li[3]/span[2]/strong")
	private WebElement colorLbl;
	
	@FindBy(id = "Correct_False")
	private WebElement noRadioBtn;
	
	@FindBy(xpath = "//*[@id=\"pr3\"]/div/button")
	private WebElement continueBtn2;

	public void startIt(WebDriverWait wait, String expected) throws TimeoutException{
		startNowBtn.click();
		
		wait.until(ExpectedConditions.urlContains(expected));
	}
	
	public void submitReg(Actions action, String regNum, WebDriverWait wait) throws TimeoutException{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"content\"]/form/div/div/div[2]/fieldset/button")));
		
		action.click(form).sendKeys(regNum).click(continueBtn1).perform();
	}

	public String getColor() {
		return colorLbl.getText();
	}
	
	public String getMake(WebDriverWait wait) throws TimeoutException{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pr3\"]/div/ul/li[2]/span[2]/strong")));
		
		return makeLbl.getText();
	}
	
	public void startAgain() {
		noRadioBtn.click();
		continueBtn2.click();
	}
	
}
