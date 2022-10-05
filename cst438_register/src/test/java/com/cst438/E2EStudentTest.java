package com.cst438;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
 
public class E2EStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION 
                          = "C:/chromedriver_win32/chromedriver.exe";
	public static final String URL = "http://localhost:3000";
	public static final String ALIAS_NAME = "test";
	public static final int SLEEP_DURATION = 1000; // 1 second.
 
	@Test
	public void playGame() throws Exception {
 
		// set the driver location and start driver
		//@formatter:off
		//
		// browser	property name 				Java Driver Class
		// -------  ------------------------    ----------------------
		// Edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		// Chrome   webdriver.chrome.driver     ChromeDriver
		//
		//@formatter:on
 //
		//TODO update the property name for your browser 
		System.setProperty("webdriver.chrome.driver",
                     CHROME_DRIVER_FILE_LOCATION);
		//TODO update the class ChromeDriver()  for your browser
		WebDriver driver = new ChromeDriver();
		
		try {
			WebElement we;
			
			driver.get(URL);
			// must have a short wait to allow time for the page to download 
			Thread.sleep(SLEEP_DURATION);
 
			// find and click the Add Student route button
			we = driver.findElement(By.id("addStudentButton"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// find and click the Add Student pop-up button
			we = driver.findElement(By.id("addStudentPopup"));
			we.click();
			Thread.sleep(SLEEP_DURATION);

			// enter email for test
			String emailAttempt = "bbird@csumb.edu";
			we = driver.findElement(By.name("email"));
			we.sendKeys(emailAttempt);
			
			// enter name for test
			String nameAttempt = "Big Bird";
			we = driver.findElement(By.name("name"));
			we.sendKeys(nameAttempt);
			
			// find and click the submit button
			we = driver.findElement(By.id("Add"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// find and click the home button
			we = driver.findElement(By.id("home"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
			
		} finally {
			driver.quit();
		}
	}
}
