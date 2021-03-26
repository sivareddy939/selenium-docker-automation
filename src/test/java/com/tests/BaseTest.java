package com.tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {

    protected WebDriver driver;

    @BeforeTest
    public void setupDriver(ITestContext ctx) throws MalformedURLException {
        // BROWSER => chrome / firefox
        // HUB_HOST => localhost / 10.0.1.3 / hostname

        String host = "localhost";
        DesiredCapabilities dc;

        if(System.getProperty("BROWSER") != null &&
                System.getProperty("BROWSER").equalsIgnoreCase("firefox")){
            dc = DesiredCapabilities.firefox();
        }else{
            dc = DesiredCapabilities.chrome();
        }

        if(System.getProperty("HUB_HOST") != null){
            host = System.getProperty("HUB_HOST");
        }

        String testName = ctx.getCurrentXmlTest().getName();

        String completeUrl = "http://" + host + ":4444/wd/hub";
        dc.setCapability("name", testName);
        this.driver = new RemoteWebDriver(new URL(completeUrl), dc);
    }

    @AfterTest
    public void quitDriver(){
        this.driver.quit();
    }
    
    public boolean waitforpage(WebDriver driver) {
    	WebDriverWait wait = new WebDriverWait(driver, 30);
    	JavascriptExecutor js = (JavascriptExecutor)(driver);
    	// wait for jQuery to load
    	ExpectedCondition<Boolean> jquery = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				try {
				return ((Long)(js).executeScript("return jQuery.active")==0);
				}catch(Exception e) {
					return true;
				}
			}
		};
		// wait for Javascript to load
		ExpectedCondition<Boolean> jsload = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				
				return (js).executeScript("return document.readystate").toString().equals("complete");
			}
			
		};
		return wait.until(jquery) && wait.until(jsload);
    			
    }
    
    public void WaitForPageLoad(WebDriver driver) {
    	long timeouts =25000; //Time outs 25 seconds
    	
    	for(long i=1000;i<timeouts;i=i+1000) {
    		try {
    			Thread.sleep(i);
    			if(waitforpage(driver)) {
    				break;
    			}
    		}catch(InterruptedException e) {
    			String k =e.getMessage();
    			System.out.println(k);
    		}
    	}
    }



}
