package com.reg.site;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class SitePOM {
	
	@FindBy(xpath = "//*[@id=\"get-started\"]/a")
	private WebElement startNowBtn;
	
	@FindBy(xpath = "//*[@id=\"content\"]/form/div/div/div[2]/fieldset/button")
	private WebElement continueBtn1;
	
	@FindBy(xpath = "//*[@id=\"pr3\"]/div/ul/li[3]/span[2]/strong")
	private WebElement colorLbl;
	
	@FindBy(id = "Correct_False")
	private WebElement noRadioBtn;
	
	@FindBy(xpath = "//*[@id=\"pr3\"]/div/button")
	private WebElement continueBtn2;

	public void startIt(Actions action) {
		action.click(startNowBtn).perform();
	}
	
	public void submitReg(Actions action, String regNum) {
		action.moveToElement(continueBtn1).moveByOffset(0, -70).click().sendKeys(regNum).click(continueBtn1).perform();
	}

	public String getColor() {
		return colorLbl.getText();
	}
	
	public void startAgain(Actions action) {
		action.click(noRadioBtn).click(continueBtn2).perform();
	}
	
}
