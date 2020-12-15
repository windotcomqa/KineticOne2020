package com.res_keywordEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.dotcom.keyword.base.Base;
import com.dotcom.keyword.engine.WriteExcel;
import com.dotcom.testcases.ResKickStart;

public class ResNewKeywordEngin extends ResKickStart {

	public WebDriver driver;
	public String TestCaseID;
	public Properties prop;
	public Base base;
	public WriteExcel wc;
	public WebElement element;
	public LinksObjectRepository links;
	public ReadLinks read;
	public WriteLinksStatus status;
	public BrokenLinks brok;
	public ResLinksRepository reslink;
	
	public static Workbook book;
	public static org.apache.poi.ss.usermodel.Sheet sheet;
	// reports
	public final String SCENARIO_SHEET_PATH = "C:\\Users\\asmakhatoon.l\\eclipse-workspace\\DotComKeyword\\src\\main\\java\\com\\dotcom\\keyword\\scenarios\\RESEXCEL.xlsx";

	// Master sheet test plan
	public void readExecution() throws Throwable {

		File f = new File("C:\\Users\\asmakhatoon.l\\eclipse-workspace\\DotComKeyword\\src\\main\\java\\com\\dotcom\\keyword\\scenarios\\RESEXCEL.xlsx");
		FileInputStream fin = new FileInputStream(f);
		Workbook wb = new XSSFWorkbook(fin);
		Sheet sheet = wb.getSheet("ResMasterSheet");
		int TotalRowCount = sheet.getPhysicalNumberOfRows();
		String TC = String.valueOf(TotalRowCount);
		System.out.println("Total Plan Count is:- " + TC);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			System.out.println("ResMasterSheet Value of i is:- " + i);
			Row headrow = sheet.getRow(i);
			String headname = headrow.getCell(3).getStringCellValue();
			System.out.println(headname);
			System.out.println("MasterSheet Value of Flag is:- " + headname);
			if (headname.equals("Yes")) {
				TestCaseID = headrow.getCell(1).getStringCellValue();
				startExecution(TestCaseID);
				/*
				 * base.wait(driver, "//p[text()='Order Number']/following-sibling::p");
				 * WebElement orderID =
				 * driver.findElement(By.xpath("//p[text()='OrderNumber']/following-sibling::p")
				 * ); wc.updateExcel(i, 4, orderID.getText());
				 * System.out.println("Your order has been placed");
				 * System.out.print("Your order ID is " + orderID.getText());
				 * System.out.println(i); System.out.println(TestCaseID);
				 */
				TestCaseID = "";
			}
			System.out.println("MasterSheet Value of SheetName is:- " + TestCaseID);
		}

	}
	
	
	// Start Execution
	public void startExecution(String sheetName) throws Throwable

	{
		reportTestScenarios(sheetName);
		FileInputStream file = null;
		try {
			file = new FileInputStream(SCENARIO_SHEET_PATH);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		try {
			book = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		sheet = book.getSheet(sheetName);

		int k = 0;
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {

			System.out.println("Value of the (Index) is- " + i);

			String LocatorName = sheet.getRow(i).getCell(k + 1).toString().trim();
			String action = sheet.getRow(i).getCell(k + 2).toString().trim();
			String value = sheet.getRow(i).getCell(k + 3).toString().trim();

			System.out.println("Value of the (LocatorName) is- " + LocatorName);
			System.out.println("Value of the (action) is- " + action);
			System.out.println("Value of the (value) is- " + value);

			if (LocatorName.isEmpty() || LocatorName.equals(null)) {
				String val = base.getMapData(LocatorName);
				System.out.println("Value of the keyword (Key Data) is- " + val);

			}
			
			// Visit Residential
            if (action.equalsIgnoreCase("Visit Residential")) {
                  try {
                         String linkData = links.getLinkData(LocatorName);
                         base.click_element(driver, linkData);
                         Thread.sleep(15000);
                         reportStep("PASS", "Navigated to the website");
                  } catch (Exception e) {
                         reportStep("FAIL", "Please ckeck your URL");
                         e.printStackTrace();
                  }
            }

            if (action.equalsIgnoreCase("openBrowser")) {
                  try

                  {
                         base = new Base();
                         prop = base.init_properties();
                         if (value.isEmpty() || value.equals("NA")) {
                               driver = base.init_driver(prop.getProperty("browser"));

                         } else {
                               driver = base.init_driver(value);

                         }
                         reportStep("PASS", "Browser has been launched");
                  } catch (Exception e) {
                         System.out.println(e.getMessage());
                         reportStep("FAIL", "Browser has not launched");

                  }
            }

            else if (action.equalsIgnoreCase("navigateURL")) {
                  try {
                         base.navigateUrl(driver, value);
                         System.out.println("URL launched");
                         reportStep("PASS", "Browser has not launched");

                  } catch (Exception e) {
                         System.out.println(e.getMessage());
                         reportStep("FAIL", "Browser has not launched");

                  }
            }

            // Click element

            else if (action != null && action.equalsIgnoreCase("click_element")) {

                  try {
                         String val = base.getMapData(LocatorName);

                         base.click_element(driver, val);

                         System.out.println(value);
                         reportStep("PASS", "click element Done");

                  } catch (Exception e) {

                         System.out.println(e.getMessage());
                         reportStep("FAIL", "click element NOTDone");
                  }
            }

            // provideAddress

            else if (action != null && action.equalsIgnoreCase("provideAddress")) {
                  try {

                         String val10 = links.getLinkData(LocatorName);
                         String[] arr_locator = val10.split("%");
                         String[] arr_value = value.split(",");
                         int loc = 0;
                         int data = 0;

                         do {

                               if (loc == 0 && data == 0) {
                                      System.out.println("PROVIDE ADDRESS");
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      loc++;
                                      System.out.println("Street address" + arr_value[data]);
                                      base.mousehover(driver, arr_locator[loc]);
                                      loc++;
                                      data++;
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("City" + arr_value[data]);
                                      loc++;
                                      data++;
                                      base.click_element(driver, arr_locator[loc]);
                                      String stateLoc = "//a[text()='" + arr_value[data] + "']";
                                      base.scroll(driver, stateLoc);
                                      base.click_element(driver, stateLoc);
                                      System.out.println("State" + arr_value[data]);
                                      loc++;
                                      data++;
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      loc++;
                                      System.out.println("Zip Code" + arr_value[data]);
                                      base.mousehover(driver, arr_locator[loc]);
                                      Thread.sleep(5000);
                                      System.out.println("Address has been Entered successfully");
                                      System.out.println(
                                                   "----ReportDebugging--Calling reportStep--Check Desc =>" + "Just Address Entered");
                                      reportStep("PASS", "Address is entered");
                                      break;

                               } else {
                                      System.out.println("Please enter the Valid address");
                                      reportStep("FAIL", "Address is not entered");
                                      break;
                               }
                         } while (loc == 6);

                  } catch (Exception e) {
                         System.out.println(e.getMessage());
                         reportStep("FAIL", "Address is not entered");
                  }

            }

            // Checkout

            else if (action != null && action.equalsIgnoreCase("CheckOutPageWithInstallationDate")) {
                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val16 = base.getMapData(LocatorName);
                         String[] arr_locator = val16.split("%");
                         String[] arr_value = value.split(",");

                         int loc = 0;
                         int data = 0;

                         do {
                               System.out.println("You have been moved to checkout page");
                               base.wait(driver, arr_locator[loc]);

                               if (loc == 0 && data == 0) {
                                      System.out.println("Entering Details");
                                      // first name
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("First Name:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // last name
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Last Name:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // phone number
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Phone Number:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // email id
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Email Id:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // business name
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Business Name:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // tax id
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Tax Id:" + arr_value[data]);
                                      loc++;

                                      // installation date
                                      base.click_element(driver, arr_locator[loc]);
                                      loc++;

                                      // select date
                                      base.click_element(driver, arr_locator[loc]);
                                      loc++;
                                      data++;
                                      // Driving Directions
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      loc++;
                                      // scroll terms and conditions
                                      base.scroll(driver, arr_locator[loc]);
                                      base.click_element(driver, arr_locator[loc]);
                                      loc++;
                                      // Place order
                                      // base.click_element(driver, arr_locator[loc]);
                                      // loc++;
                                      Thread.sleep(10000);
                                      System.out.println("Completed your Purchase");
                                      reportStep("PASS", "Completed your Purchase");
                                      break;

                               }
                         } while (loc == 10);

                  } catch (Exception e) {
                         System.out.println(e.getMessage());
                         reportStep("FAIL", "NOT YET Completed your Purchase");

                  }

            }

            // Summary checkout

            else if (action != null && action.equalsIgnoreCase("SummaryCheckout")) {
                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         System.out.println("You've entered sumarry page");
                         String val17 = base.getMapData(LocatorName);
                         base.wait(driver, val17);
                         System.out.println("You have moving to checkout");
                         base.click_element(driver, val17);
                         reportStep("FAIL", "Address is not entered");
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }

            }

            // Checkout without Installation date

            else if (action != null && action.equalsIgnoreCase("CheckOutPageWithOutInstallationDate")) {
                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val16 = base.getMapData(LocatorName);
                         String[] arr_locator = val16.split("%");
                         String[] arr_value = value.split(",");

                         int loc = 0;
                         int data = 0;

                         do {
                               System.out.println("You have been moved to checkout page");
                               base.wait(driver, arr_locator[loc]);

                               if (loc == 0 && data == 0) {
                                      System.out.println("Entering Details");
                                      // first name
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("First Name:" + arr_value[data]);

                                      loc++;
                                      data++;
                                      // last name
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Last Name:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // phone number
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Phone Number:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // email id
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Email Id:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // business name
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Business Name:" + arr_value[data]);
                                      loc++;
                                      data++;
                                      // tax id
                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                      System.out.println("Tax Id:" + arr_value[data]);
                                      loc++;
                                      // data++;
                                      // scroll terms and conditions
                                      base.scroll(driver, arr_locator[loc]);
                                      base.click_element(driver, arr_locator[loc]);
                                      loc++;
                                      // Place order mandatory
                                      // base.click_element(driver, arr_locator[loc]);
                                      // loc++;
                                      Thread.sleep(10000);
                                      System.out.println("Completed your Purchase");
                                      reportStep("PASS", "Completed your Purchase");

                                      break;

                               }
                         } while (loc == 10);

                  } catch (Exception e) {
                         System.out.println(e.getMessage());
                         reportStep("FAIL", "Completed your Purchase");
                  }

            }

            // Close browser

            else if (action != null && action.equalsIgnoreCase("closeBrowser")) {
                  try {

                         driver.quit();
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }
            }

            // Geolocation

            else if (action != null && action.equalsIgnoreCase("geoLocation")) {
                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String linkData = links.getLinkData(LocatorName);
                         
                         System.out.println("val value" +linkData);
                         base.wait(driver, linkData);
                         base.click_element(driver, linkData);
                         
                         
                         //String[] arr_locator = val17.split("%");
                         //int loc = 0;
//                       do {
//                             base.wait(driver, arr_locator[loc]);
//                              base.click_element(driver, arr_locator[loc]);
//                             // base.wait(driver, arr_locator[loc]);
//                             loc++;
//                              base.click_element(driver, arr_locator[loc]);
//                             reportStep("PASS", "CLICK HERE TO ENTER YOUR ADDRESS");
//                             break;
//                       } while (loc == 1);

                  } catch (Exception e) {
                         System.out.println(e.getMessage());
                         reportStep("FAIL", "CLICK HERE TO ENTER YOUR ADDRESS");

                  }
            }

            // MYwin login
            else if (action != null && action.equalsIgnoreCase("EnterCrendentials")) {
                  try {
                         System.out.println("MyWIN Menu Validation For Residential");
                         String linkData = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData.split("%");
                         String[] arr_value = value.split(",");
                         int loc = 0;
                         int data = 0;
                         base.click_element(driver, arr_locator[loc]);
                         if (loc == 0 && data == 0) {
                                System.out.println("USERNAME " + arr_value[data]);
                               // base.wait(driver, (arr_locator[loc]));

                               System.out.println("Enter your credentials");
                               loc++;
                               base.wait(driver, arr_locator[loc]);
                               base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                               loc++;
                               data++;
                                System.out.println("PASSWORD " + arr_value[data]);
                               base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                               loc++;
                                base.click_element(driver, arr_locator[loc]);
                               System.out.println("Log in Successfully");
                               // validation
                               String actualUrl = "https://www.windstream.com/#/";
                               String expectedUrl = driver.getCurrentUrl();

                               if (actualUrl.equalsIgnoreCase(expectedUrl)) {
                                      System.out.println("Test passed");
                                      reportStep("PASS", "MyWin logged in");
                               } else {
                                      System.out.println("Test failed");
                                      System.out.println("please enter the valid credentials");
                                      reportStep("FAIL", "please enter the valid credentials");
                               }
                         }

                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                         reportStep("FAIL", "please enter the valid credentials");
                  }

            }

            // Res my win

            else if (action != null && action.equalsIgnoreCase("MyWinMenu")) {
                  try {
                         String linkData = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData.split("%");
                         String[] arr_value = value.split(",");
                         int loc = 0;
                         int data = 0;
                         System.out.println(arr_locator[loc]);
                         // int data = 0;
                         System.out.println("hai inside my win");
                         WebElement account = driver.findElement(By.xpath(arr_locator[loc]));
                         System.out.println(account);
                         Thread.sleep(5000);
                         loc++;
                         System.out.println("next line");
                         Actions builder = new Actions(driver);
                         Thread.sleep(5000);
                         WebElement account1 = driver.findElement(By.xpath(arr_locator[loc]));
                         builder.moveToElement(account1).perform();
                         // Thread.sleep(5000);
                         List<WebElement> elements = account.findElements(By.tagName("a"));

                         int size = elements.size();
                         System.out.println(size);
                         String ParentWindowHandle = driver.getWindowHandle();
                         // Thread.sleep(5000);;
                         JavascriptExecutor js = (JavascriptExecutor) driver;
                         for (int j = 0; j < size; j++) {
                                System.out.println(".........Inside For loop..........");
                               System.out.println("List of sub-menus is: " + elements.get(j));
                               String hrefLink = elements.get(j).getAttribute("href");
                               System.out.println("Href Value is " + hrefLink);
                               Thread.sleep(5000);
                                js.executeScript("window.open('" + hrefLink + "','_blank');");

                         }
                         // Thread.sleep(5000);
                         System.out.println("Control Came out of the For Loop : ======>");
                         Set<String> allWindowHandles = driver.getWindowHandles();
                         int handleCount = 0;
                         Thread.sleep(20000);
                         System.out.println("My Parent Window handle - > " + ParentWindowHandle);
                         driver.switchTo().window(ParentWindowHandle);

                         List<String> list = new ArrayList<String>();
                         for (String Invhandle : allWindowHandles) {
                               handleCount++;
                                System.out.println("Currently Window handle before IF matches is  - > " + Invhandle
                                            + "Handler Count is: " + handleCount);
                               if (!ParentWindowHandle.equals(Invhandle)) {
                                      list.add(Invhandle);
                               }
                         }

                         int tempH, valueiter;
                         String expectedURL = reslink.resLinkData(value);
                         // String[] arr_locator = val10.split("%");
                         // String[] arr_value = value.split(",");
                         // int loc = 0;
                         // String expectedURL = smblink.getLinkData(value);
                         System.out.println("values from excel" + value);
                         System.out.println("Expected url from excel" + expectedURL);
                         String[] splittedvalues = expectedURL.split("@");
                         /*
                         * int data=0;
                         * 
                          * System.out.println(splittedvalues[data]); data++;
                         * System.out.println(splittedvalues[data]); data++;
                         * System.out.println(splittedvalues[data]); data++;
                         */
                         // System.out.println(splittedvalues[data]);
                         for (tempH = list.size() - 1, valueiter = 0; tempH >= 0
                                      && valueiter <= splittedvalues.length; tempH--, valueiter++) {
                                driver.switchTo().window(list.get(tempH));

                                System.out.println("Control Came inside IF Loop after when PH not-matches : ======>");
                               String childURl = driver.getCurrentUrl();
                               if (splittedvalues[valueiter].contains(childURl)) {
                                      System.out.println("Expected URL " + splittedvalues[valueiter]);
                                      System.out.println("Actual URL from site" + childURl);
                                      System.out.println("URL status is PASS");
                               } else {
                                      System.out.println("Expected URL " + splittedvalues[valueiter]);
                                      System.out.println("Actual URL from site" + childURl);
                                      System.out.println("URL status is FAIL");
                               }

                               driver.close();
                         }
                         driver.switchTo().window(ParentWindowHandle);
                         reportStep("PASS", "MyWin URls Has been checked");

                  } catch (Exception e) {
                         reportStep("FAIL", "please enter the valid credentials");

                         e.printStackTrace();
                  }

            }
            // form validation
            else if (action != null && action.equalsIgnoreCase("form")) {
                  try {
                         String sryadd = links.getLinkData(LocatorName);
                         WebElement findElement = driver.findElement(By.xpath(sryadd));
                         String text = findElement.getText();
                         System.out.println("findelement gettext " + text);
                         if (text.equalsIgnoreCase(value)) {
                               System.out.println("form is Valid");
                               reportStep("PASS", "Form Validation Done");
                         } else {
                               System.out.println("not valid address");
                               reportStep("FAIL", "please enter the valid credentials");
                         }
                  } catch (Exception e) {
                         reportStep("FAIL", "please enter the valid credentials");

                         e.printStackTrace();
                  }

            }
            // Primary Links

            if (action.equalsIgnoreCase("PrimaryLinks")) {
                  try {
                         String linkData = read.getMapData(value);
                         System.out.println("The value is" + value);
                         System.out.println("check the link" + linkData);
                         driver.get(linkData);
                         int status = 1;
                         brok.brokenlink(driver, value, i);
                         reportStep("PASS", "PrimaryLinks Validation Done");
                  } catch (Exception e) {
                         // TODO Auto-generated catch block
                         reportStep("FAIL", "please enter the valid credentials");
                         e.printStackTrace();
                  }

            }
            // Search

            if (action.equalsIgnoreCase("Search")) {
                  try {
                         String linkData1 = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData1.split("%");
                         String[] arr_value = value.split("@");
                         int loc = 0;
                         int data = 0;
                         System.out.println(arr_value[data]);
                         base.wait(driver, arr_locator[loc]);
                         // clicking on top search/support to provide search data
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         base.wait(driver, arr_locator[loc]);
                         // providing search data
                         base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         // clicking on search icon
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         System.out.println(arr_value[data]);
                         base.wait(driver, arr_locator[loc]);
                         // verify search page count title
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         data++;

                         // verifing the url match
                         String currentUrl = driver.getCurrentUrl();
                         System.out.println("URL from site " + currentUrl);

                         boolean equals = currentUrl.equals(arr_value[data]);
                         System.out.println("Given url matches " + equals);
                         if (equals == true) {

                               System.out.println("top search URL matched");
                         } else {
                               System.out.println("top search URL does not match");
                         }

                         // checking first URL
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         Thread.sleep(5000);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         driver.navigate().back();

                         // checking first read more
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         driver.navigate().back();

                         // checking second URL
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         driver.navigate().back();

                         // checking second read more
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         driver.navigate().back();

                         // footer2
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);

                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // next
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // last
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // Back
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // First
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         Thread.sleep(10000);
                         reportStep("PASS", "Search Validation Done");
                  } catch (Exception e) {
                         // TODO Auto-generated catch block
                         reportStep("FAIL", "please enter the valid Input");
                         e.printStackTrace();
                  }

            }
            // Verifying Support Back Functionality

            if (action.equalsIgnoreCase("SupportBack")) {
                  try {
                         String linkData = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData.split("%");
                         String[] arr_value = value.split("@");
                         int loc = 0;
                         int data = 0;
                         System.out.println(arr_value[data]);
                         base.wait(driver, arr_locator[loc]);
                         // clicking on support to provide search data
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         base.wait(driver, arr_locator[loc]);
                         // providing search data
                         base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         // clicking on search icon
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         // providing search data
                         base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         // clicking on search icon
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         base.wait(driver, arr_locator[loc]);
                         base.click_element(driver, arr_locator[loc]);
                         // verifying back to support in breadcrumps
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         // verifying Digital tv in breadcrumps
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         // verifying Kinetic tv in breadcrumps
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         // click on digital tv
                         loc++;
                         base.click_element(driver, arr_locator[loc]);
                         // verify digital tv
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         // verify and click back to support
                         loc++;
                         data++;
                         base.click_element(driver, arr_locator[loc]);
                         // verify support page title
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         reportStep("PASS", "Support Search valdiation done");
                  } catch (Exception e) {
                         reportStep("FAIL", "please enter the valid Input");
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                  }
            }

            // Moving to support article search

            if (action.equalsIgnoreCase("SupportArticle")) {
                  try {
                         String linkData = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData.split("%");
                         String[] arr_value = value.split("@");
                         int loc = 0;
                         int data = 0;
                         System.out.println(arr_value[data]);
                         base.wait(driver, arr_locator[loc]);
                         // clicking on support to provide search data
                         base.click_element(driver, arr_locator[loc]);
                         // moving to support page and clicking my account article
                         loc++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         // provide search data and click on search
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         base.click_element(driver, arr_locator[loc]);
                         // verify search page count title
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         data++;
                         // verify support article URL
                         String currentUrl = driver.getCurrentUrl();
                         System.out.println("URL from site " + currentUrl);

                         boolean equals = currentUrl.equals(arr_value[data]);
                         System.out.println("Given url matches " + equals);
                         if (equals == true) {

                               System.out.println("top search URL matched");
                         } else {
                               System.out.println("top search URL does not match");
                         }
                         // verify back to support present
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         // verify and click on first link
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         // verify breadcrumps
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // providing search in support article result page
                         loc++;
                         data++;
                         base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         base.click_element(driver, arr_locator[loc]);

                         // footer2
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);

                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // next
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // last
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // Back
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);

                         // First
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         reportStep("PASS", "Support Artical search validation done");
                         Thread.sleep(10000);
                  } catch (Exception e) {
                         reportStep("FAIL", "please enter the valid Input");
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                  }

            }

            // invalid search
            if (action.equalsIgnoreCase("InvalidSearch")) {
                  try {
                         String linkData = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData.split("%");
                         String[] arr_value = value.split("@");
                         int loc = 0;
                         int data = 0;
                         System.out.println(arr_value[data]);
                         base.wait(driver, arr_locator[loc]);
                         // clicking on top search to provide search data
                         base.click_element(driver, arr_locator[loc]);
                         loc++;
                         base.wait(driver, arr_locator[loc]);
                         base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         base.click_element(driver, arr_locator[loc]);
                         // verify search page count title
                         loc++;
                         data++;
                         base.wait(driver, arr_locator[loc]);
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         loc++;
                         data++;
                         base.verifytext(driver, arr_locator[loc], arr_value[data]);
                         data++;
                         // verify support article URL
                         String currentUrl = driver.getCurrentUrl();
                         System.out.println("URL from site " + currentUrl);

                         boolean equals = currentUrl.equals(arr_value[data]);
                         System.out.println("Given url matches " + equals);
                         if (equals == true) {

                               System.out.println("top search URL matched");
                         } else {
                               System.out.println("top search URL does not match");
                         }
                         reportStep("PASS", "Invalid search validation done");
                  } catch (Exception e) {
                         reportStep("FAIL", "please enter the valid Input");
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                  }

            }
            // header products and support

            if (action.equalsIgnoreCase("HeaderProductsSupport")) {

                  try {
                         String linkData = links.getLinkData(LocatorName);
                         String[] arr_locator = linkData.split("%");
                         int loc = 0;
                         WebElement account = driver.findElement(By.xpath(arr_locator[loc]));
                         loc++;
                         // Thread.sleep(5000);
                         Actions builder = new Actions(driver);
                         Thread.sleep(5000);
                         WebElement account1 = driver.findElement(By.xpath(arr_locator[loc]));
                         builder.moveToElement(account1).perform();
                         // Thread.sleep(5000);
                         List<WebElement> elements = account.findElements(By.tagName("a"));

                         int size = elements.size();
                         System.out.println(size);
                         String ParentWindowHandle = driver.getWindowHandle();
                         // Thread.sleep(5000);;
                         JavascriptExecutor js = (JavascriptExecutor) driver;
                         for (int j = 0; j < size; j++) {
                                System.out.println(".........Inside For loop..........");
                               System.out.println("List of sub-menus is: " + elements.get(j));
                               String hrefLink = elements.get(j).getAttribute("href");
                               System.out.println("Href Value is " + hrefLink);
                               Thread.sleep(5000);
                                js.executeScript("window.open('" + hrefLink + "','_blank');");

                         }
                         // Thread.sleep(5000);
                         System.out.println("Control Came out of the For Loop : ======>");
                         Set<String> allWindowHandles = driver.getWindowHandles();
                         int handleCount = 0;
                         Thread.sleep(5000);
                         System.out.println("My Parent Window handle - > " + ParentWindowHandle);
                         driver.switchTo().window(ParentWindowHandle);

                         List<String> list = new ArrayList<String>();
                         for (String Invhandle : allWindowHandles) {
                               handleCount++;
                                System.out.println("Currently Window handle before IF matches is  - > " + Invhandle
                                            + "Handler Count is: " + handleCount);
                               if (!ParentWindowHandle.equals(Invhandle)) {
                                      list.add(Invhandle);
                               }
                         }

                         int tempH, valueiter;
                         String expectedURL = reslink.resLinkData(value);
                         System.out.println("values from excel" + value);
                         String[] splittedvalues = expectedURL.split("@");
                         for (tempH = list.size() - 1, valueiter = 0; tempH >= 0
                                      && valueiter <= splittedvalues.length; tempH--, valueiter++) {
                                driver.switchTo().window(list.get(tempH));

                                System.out.println("Control Came inside IF Loop after when PH not-matches : ======>");
                               String childURl = driver.getCurrentUrl();
                               if (splittedvalues[valueiter].equals(childURl)) {
                                      System.out.println("Expected URL " + splittedvalues[valueiter]);
                                      System.out.println("Actual URL from site" + childURl);
                                      System.out.println("URL status is PASS");
                               } else {
                                      System.out.println("Expected URL " + splittedvalues[valueiter]);
                                      System.out.println("Actual URL from site" + childURl);
                                      System.out.println("URL status is FAIL");
                               }

                               driver.close();
                         }
                         driver.switchTo().window(ParentWindowHandle);
                         reportStep("PASS", "Header Product Search validation done");

                  } catch (Exception e) {
                         reportStep("FAIL", "please enter the valid Input");
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                  }

            }

            // RES FOOTER CODE
            if (action.equalsIgnoreCase("RESFooter")) {
                  String linkData = links.getLinkData(LocatorName);
                  WebElement link = driver.findElement(By.xpath(linkData));
                  List<WebElement> elements = link.findElements(By.tagName("a"));
                  int size = elements.size();
                  System.out.println(size);
                  String ParentWindowHandle = driver.getWindowHandle();
                  for (int z = 0; z < size; z++) {
                         String keys = Keys.chord(Keys.CONTROL, Keys.ENTER);
                         Thread.sleep(3000);
                         elements.get(z).sendKeys(keys);

                  }
                  Thread.sleep(5000);
                  System.out.println("Control Came out of the For Loop : ======>");
                  Set<String> allWindowHandles = driver.getWindowHandles();
                  int handleCount = 0;
                  Thread.sleep(20000);
                  System.out.println("My Parent Window handle - > " + ParentWindowHandle);
                   driver.switchTo().window(ParentWindowHandle);

                  List<String> list = new ArrayList<String>();
                  for (String Invhandle : allWindowHandles) {
                         handleCount++;
                         System.out.println("Currently Window handle before IF matches is  - > " + Invhandle
                                      + "Handler Count is: " + handleCount);
                         if (!ParentWindowHandle.equals(Invhandle)) {
                               list.add(Invhandle);
                         }
                  }

                  int tempH, valueiter;
                  String expectedURL = reslink.resLinkData(value);
                  System.out.println("values from excel" + value);
                  String[] splittedvalues = expectedURL.split("@");
                  for (tempH = list.size() - 1, valueiter = 0; tempH >= 0
                               && valueiter <= splittedvalues.length; tempH--, valueiter++) {
                         driver.switchTo().window(list.get(tempH));

                         System.out.println("Control Came inside IF Loop after when PH not-matches : ======>");
                         System.out.println("splitted values" + splittedvalues[valueiter]);
                         String childURl = driver.getCurrentUrl();
                         if (splittedvalues[valueiter].equals(childURl)) {
                                System.out.println("Expected URL " + splittedvalues[valueiter]);
                                System.out.println("Actual URL from site" + childURl);
                               System.out.println("URL status is PASS");
                         } else {
                                System.out.println("Expected URL " + splittedvalues[valueiter]);
                                System.out.println("Actual URL from site" + childURl);
                               System.out.println("URL status is FAIL");
                         }

                         URL u = new URL(splittedvalues[valueiter]);
                         HttpURLConnection hc = (HttpURLConnection) u.openConnection();
                         hc.setRequestMethod("HEAD");
                         hc.connect();
                         int rc = hc.getResponseCode();
                         System.out.println(rc);
                         String rm = hc.getResponseMessage();
                         if (rc == 200) {
                                System.out.println(childURl + "is valid");

                         } else {
                                System.out.println(childURl + "is a Broken link");

                         }
                         driver.close();
                  }
                   driver.switchTo().window(ParentWindowHandle);

            }

            else if (action != null && action.equalsIgnoreCase("microProvideAddress")) {

                  try {

                         String micro = links.getLinkData(LocatorName);

                         String[] arr_locator = micro.split("%");

                         String[] arr_value = value.split(",");

                         int loc = 0;

                         int data = 0;

                         do {

                               if (loc == 0 && data == 0) {

                                      System.out.println("PROVIDE ADDRESS");

                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);

                                      loc++;

                                      System.out.println("Street address" + arr_value[data]);

                                      base.mousehover(driver, arr_locator[loc]);

                                      loc++;

                                      data++;

                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);

                                      System.out.println("City" + arr_value[data]);

                                      loc++;

                                      data++;

                                      base.click_element(driver, arr_locator[loc]);

                                      String stateLoc = "//a[text()='" + arr_value[data] + "']";

                                      base.scroll(driver, stateLoc);

                                      base.click_element(driver, stateLoc);

                                      System.out.println("State" + arr_value[data]);

                                      loc++;

                                      data++;

                                      base.SendKeys(driver, arr_locator[loc], arr_value[data]);

                                      loc++;

                                      System.out.println("Zip Code" + arr_value[data]);

                                      base.mousehover(driver, arr_locator[loc]);

                                      Thread.sleep(5000);

                                      System.out.println("Address has been Entered successfully");

                                      System.out.println(

                                                   "----ReportDebugging--Calling reportStep--Check Desc =>" + "Just Address Entered");

                                      reportStep("PASS", "Address is entered");

                                      break;

                               } else {

                                      System.out.println("Please enter the Valid address");

                                      reportStep("FAIL", "Address is not entered");

                                      break;

                               }

                         } while (loc == 6);

                  } catch (Exception e) {

                         System.out.println(e.getMessage());

                         reportStep("FAIL", "Address is not entered");

                  }

            }
            // Live chat RES
           /* else if (action != null && action.equalsIgnoreCase("LiveChatRES")) {

                  String chat = reschatlink.getRESChatData(value);
                  System.out.println("chat " + chat);
                  driver.get(chat);

                  *//*** Getting EST time ***//*
                   TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));

                  SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");

                  Date time = new Date();

                  String time1 = formatter.format(time);

                  System.out.println(time1);
                  int hours = time.getHours();

                  System.out.println(hours);

                  // Check for Business hours three tile
                  if ((hours >= 8) && (hours <= 18)) {
                         String greybarornot = grey.greyBarOrNot();
                         System.out.println("grey bar value is " + greybarornot);

                         if (greybarornot.equalsIgnoreCase("NO")) {

                               WebElement BeginChatText = driver.findElement(By.xpath("(//a[text()='Chat Now'])[1]"));
                               String bushr = BeginChatText.getText();
                                System.out.println(bushr);
                               String BeginChatActualText = "Chat now";
                               System.out.println("the value of" + BeginChatActualText);
                               WebElement BusHoursText = driver.findElement(By.xpath("(//div[@class='card-body'])[6]/p[1]"));
                               String bushrText = BeginChatText.getText();
                                System.out.println(bushrText);
                               String ChatActualText = "Get help fast and easy in real time from a knowledgeable representative.";
                               System.out.println("the value of" + ChatActualText);
                               if (bushr.equalsIgnoreCase(BeginChatActualText) || bushrText.contains(ChatActualText)) {
                                      System.out.println("Live chat cta is visible");
                                      BeginChatText.click();

                                      String parent = driver.getWindowHandle();

                                      Set<String> wind = driver.getWindowHandles();

                                      for (String windowHandle : wind) {
                                            if (!(windowHandle.equals(parent))) {
                                                   driver.switchTo().window(windowHandle);

                                                   Thread.sleep(5000);

                                                   String cta = driver.getCurrentUrl();

                                                   if (cta.contains("kineticcommunities")) {
                                                          System.out.println("kineticcommunities is passed");
                                                          // CustomKeywords.'chatkey.chat.title'(i, 13, 'passed')
                                                          // live.title(i, 13, 'passed')
                                                   } else {
                                                          System.out.println("kineticcommunities is failed");

                                                          // CustomKeywords.'chatkey.chat.title'(i, 13, 'Failed')
                                                          // live.title(i, 13, 'Failed')
                                                   }

                                                   driver.close();

                                                   driver.switchTo().window(parent);
                                            }
//                                          else{
                                            // System.out.println("windowhandles failed");
//                                          }
                                      }

                               } else {
                                      System.out.println("Not valid");
                               }
                         }

                  }

                  // non business hours
                  else {

                         // grey bar
                         WebElement BeginChatText = driver.findElement(By.xpath("//a[text()='Chat ']"));
                         String bushr = BeginChatText.getText();
                         System.out.println(bushr);
                         String BeginChatActualText = "Chat";
                         System.out.println("the value of" + BeginChatActualText);
                         WebElement BusHoursText = driver.findElement(By.xpath("//div[@class='title']"));
                         String bushrText = BeginChatText.getText();
                         System.out.println(bushrText);
                         String ChatActualText = "Still need help ? We're here to assist you.";
                         System.out.println("the value of" + ChatActualText);
                         if (bushr.equalsIgnoreCase(BeginChatActualText) || bushrText.contains(ChatActualText)) {
                               System.out.println("Live chat cta is visible in grey bar");
                               BeginChatText.click();

                               String parent = driver.getWindowHandle();

                               Set<String> wind = driver.getWindowHandles();

                               for (String windowHandle : wind) {
                                      if (!(windowHandle.equals(parent))) {
                                             driver.switchTo().window(windowHandle);

                                             Thread.sleep(5000);

                                            String cta = driver.getCurrentUrl();

                                            if (cta.contains("contact-us")) {
                                                   System.out.println("contact-us Page is opened");

                                            } else {
                                                   System.out.println("contact-us Page is not opened");

                                            }

                                             driver.close();

                                             driver.switchTo().window(parent);
                                      }

                               }

                         } else {
                               System.out.println("Not valid");
                         }

                  }

                 

            }
*/
            // SELECT CORE PRODUCT
            else if (action != null && action.equalsIgnoreCase("selectCoreProduct")) {
                  try {
                         String val11 = links.getLinkData(LocatorName);
                         String[] arr_locator = val11.split("%");
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         int loc1 = 0;
                         do {

                                System.out.println("SELECTING CORE PRODUCT");
                               if (loc1 == 0 && value.equalsIgnoreCase("Internet + Voice"))

                               {
                                      try {
                                            String Title = driver.getTitle();
                                             System.out.println(Title);
                                             base.wait(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Internet + Voice Product");
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 5;
                                             base.click_element(driver, arr_locator[loc1]);
                                             reportStep("PASS", "Internet and voice has been selected");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "Internet and voice hasn't been selected");

                                             e.printStackTrace();
                                      }
                               }
                               
                               // Internet
                               else if (value.equalsIgnoreCase("Internet")) {
                                      try {
                                            loc1 = 1;
                                             base.wait(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Internet");
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 5;
                                             base.click_element(driver, arr_locator[loc1]);
                                             reportStep("PASS", "Internet has been selected");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "Internet has been selected");
                                             e.printStackTrace();
                                      }
                               }
                               // Voice
                               else if (value.equalsIgnoreCase("Voice")) {
                                      try {
                                            loc1 = 2;
                                             base.wait(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Voice");
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 5;
                                             base.click_element(driver, arr_locator[loc1]);
                                             reportStep("PASS", "Voice has been selected");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "Voice hasn't been selected");
                                             e.printStackTrace();
                                      }
                               }
                               // I+V+TV
                               else if (value.equalsIgnoreCase("Internet + Voice + TV")) {
                                      try {
                                            loc1 = 3;
                                             base.wait(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Voice");
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 5;
                                             base.click_element(driver, arr_locator[loc1]);
                                             reportStep("PASS", "Voice has been selected");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "Voice hasn't been selected");
                                             e.printStackTrace();
                                      }
                               }
                               
                               // I+TV
                               else if (value.equalsIgnoreCase("Internet + TV")) {
                                      try {
                                            loc1 = 4;
                                             base.wait(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Internet + TV");
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 5;
                                             base.click_element(driver, arr_locator[loc1]);
                                             reportStep("PASS", "Internet + TV has been selected");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "Internet + TV hasn't been selected");
                                             e.printStackTrace();
                                      }
                               }

                         } while (loc1 == 4);
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }
            }

            // Speed tile
            else if (action != null && action.equalsIgnoreCase("selectInternetSpeed")) {

                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         System.out.println("SELECTING INTERNET SPEED");
                         String val12 = links.getLinkData(LocatorName);
                         String[] arr_locator = val12.split("%");
                         String[] arr_value = value.split(",");

                         int loc1 = 0;
                         int data1 = 0;
                         data1++;
                         Double.toString(data1);
                         base.wait(driver, arr_locator[loc1]);
                         String tile200 = "//span[text()='" + arr_value[data1] + "']";
                         System.out.println("speed "+tile200);
                         base.wait(driver, arr_locator[loc1]);
                         base.click_element(driver, tile200);
                         base.wait(driver, arr_locator[loc1]);
                         base.click_element(driver, arr_locator[loc1]);
                         System.out.println("You have selected " + arr_value[data1] + "speed");
                         reportStep("PASS", "You have selected the internet speed");
                  } catch (Exception e) {
                         reportStep("FAIL", "You haven't selected the internet speed");
                         System.out.println(e.getMessage());

                  }
            }

            // selectInternetModem
            else if (action != null && action.equalsIgnoreCase("selectInternetModem")) {
                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val13 = links.getLinkData(LocatorName);
                         String[] arr_locator = val13.split("%");

                         int loc1 = 0;
                         do {

                                System.out.println("SELECTING INTERNET MODEM");
                               if (loc1 == 0 && value.equalsIgnoreCase("Kinetic Gateway"))

                               {
                                      try {
                                            // base.wait(driver, arr_locator[loc1]);
                                            // base.wait(driver, arr_locator[loc1]);

                                             base.click_element(driver, arr_locator[loc1]);
                                            // base.wait(driver, arr_locator[loc1]);

                                            loc1 = 2;
                                             base.scroll(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                            loc1++;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected rental modem");
                                             reportStep("PASS", "You have selected rental modem");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected rental modem");
                                             e.printStackTrace();
                                      }
                               } else if (value.equalsIgnoreCase("Bring Your Own Modem")) {
                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 1;
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 2;
                                             base.click_element(driver, arr_locator[loc1]);
                                            loc1++;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected own modem");
                                             reportStep("PASS", "You have selected own modem");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected own modem");
                                             e.printStackTrace();
                                      }
                               }

                         } while (loc1 == 2);
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }
            }

            else if (action != null && action.equalsIgnoreCase("selectVoicePhoneNumber")) {

                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val15 = links.getLinkData(LocatorName);
                         String[] arr_locator = val15.split("%");

                         int loc1 = 0;
                         do {

                                System.out.println("SELECTING PHONE NUMBER");
                               if (loc1 == 0 && value.equalsIgnoreCase("Non-Published Number"))

                               {
                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);

                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 2;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected new phone number");
                                             reportStep("PASS", "You have selected new phone number");
                                            //break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected new phone number");
                                             e.printStackTrace();
                                      }
                               } else if (value.equalsIgnoreCase("Published Number")) {
                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 1;
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 2;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Existing Phone Number");
                                             reportStep("PASS", "You have selected Existing Phone Number");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have selected Existing Phone Number");
                                             e.printStackTrace();
                                      }
                               }

                         } while (loc1 == 2);
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }

            }
            
            //voice single tile
            else if (action != null && action.equalsIgnoreCase("selectVoiceSingleTile")) {

                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val14 = links.getLinkData(LocatorName);
                       //  String[] arr_locator = val14.split("%");

                         int loc1 = 0;
                         base.wait(driver, val14);
                         base.click_element(driver, val14);
                         
                               
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }

            }
            
            
            //voice two tiles
            else if (action != null && action.equalsIgnoreCase("selectVoiceTile")) {

                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val14 = links.getLinkData(LocatorName);
                         String[] arr_locator = val14.split("%");

                         int loc1 = 0;

                         do {

                                System.out.println("SELECTING VOICE");
                               if (loc1 == 0 && value.equalsIgnoreCase("Unlimited Phone + Voicemail"))

                               {
                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1++;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Voice Business lines");
                                             reportStep("PASS", "You have selected Voice Business lines");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have selected Voice Business lines");
                                             e.printStackTrace();
                                      }
                               } 
                               
                               else if (value.equalsIgnoreCase("Unlimited Phone")) {

                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 1;
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 2;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Existing Phone Number");
                                             reportStep("PASS", "You have selected Existing Phone Number");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have selected Existing Phone Number");
                                             e.printStackTrace();
                                      }

                               }

                         } while (loc1 == 1);
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }

            }
            // TV
            
            else if (action != null && action.equalsIgnoreCase("selectTVTile")) {
                  System.out.println("before try SELECTING tv");
                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val14 = links.getLinkData(LocatorName);
                         String[] arr_locator = val14.split("%");

                         int loc1 = 0;

                         do {

                                System.out.println("SELECTING tv");
                               if (loc1 == 0 && value.equalsIgnoreCase("Kinetic TV Local"))

                               {
                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 3;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Voice Business lines");
                                             reportStep("PASS", "You have selected Voice Business lines");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have selected Voice Business lines");
                                             e.printStackTrace();
                                      }
                               } else if (value.equalsIgnoreCase("Kinetic TV Select")) {

                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 1;
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 3;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Existing Phone Number");
                                             reportStep("PASS", "You have selected Existing Phone Number");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have selected Existing Phone Number");
                                             e.printStackTrace();
                                      }

                               } else if (value.equalsIgnoreCase("Kinetic TV Preferred")) {

                                      try {
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 1;
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             base.wait(driver, arr_locator[loc1]);
                                            loc1 = 3;
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected Existing Phone Number");
                                             reportStep("PASS", "You have selected Existing Phone Number");
                                            break;
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have selected Existing Phone Number");
                                             e.printStackTrace();
                                      }

                               }

                         } while (loc1 == 3);
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }

            }

            // TV addon page 1
            else if (action != null && action.equalsIgnoreCase("Entertainment Package")) {

                  try {
                         String Page_Title = driver.getTitle();
                         System.out.println("PAGE TITLE" + Page_Title);
                         String val14 = links.getLinkData(LocatorName);
                         String[] arr_locator = val14.split("%");
                         String[] arr_value = value.split(",");
                         int loc1 = 0, data = 0;

                         do {
                               if (loc1 == 0 && data == 0) {
                                      System.out.println("SELECTING Entertainment Package");
                                      if (arr_value[data].equalsIgnoreCase("Streaming")) {
                                            data++;
                                            
                                            if (loc1 == 0 && arr_value[data].equalsIgnoreCase("Kinetic TV 300 Hours of DVR"))

                                            {
                                                   try {
                                                          // base.wait(driver, arr_locator[loc1]);
                                                          // base.wait(driver, arr_locator[loc1]);
                                                          base.click_element(driver, arr_locator[loc1]);
                                                          // base.wait(driver, arr_locator[loc1]);

                                                          System.out.println("Kinetic TV 300 Hours of DVR");
                                                          reportStep("PASS", "You have selected Kinetic TV 300 Hours of DVR");
                                                   
                                                   } catch (Exception e) {
                                                          reportStep("FAIL", "You have not selected Kinetic TV 300 Hours of DVR");
                                                          e.printStackTrace();
                                                   }
                                            }

                                            // cloud dvr hours
                                            else if (arr_value[data].equalsIgnoreCase("Kinetic TV 200 Hours of DVR")) {

                                                   try {
                                                          // base.wait(driver, arr_locator[loc1]);
                                                          loc1 = 1;

                                                          base.click_element(driver, arr_locator[loc1]);
                                                          Thread.sleep(3000);

                                                          System.out.println("Kinetic TV 200 Hours of DVR");
                                                          reportStep("PASS", "You have selected Kinetic TV 200 Hours of DVR");
                                                          
                                                   } catch (Exception e) {
                                                          reportStep("FAIL", "You have not selected Kinetic TV 200 Hours of DVR");
                                                          e.printStackTrace();
                                                   }

                                            }

                                            else if (arr_value[data].equalsIgnoreCase("Kinetic TV 100 Hours of DVR")) {

                                                   try {
                                                          // base.wait(driver, arr_locator[loc1]);
                                                          loc1 = 2;

                                                          base.click_element(driver, arr_locator[loc1]);
                                                          Thread.sleep(3000);

//                                                                System.out.println("data value" +data);
                                                          System.out.println("Kinetic TV 100 Hours of DVR");
                                                          reportStep("PASS", "You have selected Kinetic TV 100 Hours of DVR");
                                                          
                                                   } catch (Exception e) {
                                                          reportStep("FAIL", "You have not selected Kinetic TV 100 Hours of DVR");
                                                          e.printStackTrace();
                                                   }

                                            } else if (arr_value[data].equalsIgnoreCase("50 Cloud DVR Hours")) {

                                                   try {
                                                          // base.wait(driver, arr_locator[loc1]);
                                                          loc1 = 3;

                                                          base.click_element(driver, arr_locator[loc1]);
                                                          Thread.sleep(3000);

                                                          System.out.println("50 Cloud DVR Hours");
                                                          reportStep("PASS", "You have selected 50 Cloud DVR Hours");
                                                   
                                                   } catch (Exception e) {
                                                          reportStep("FAIL", "You have not selected 50 Cloud DVR Hours");
                                                          e.printStackTrace();
                                                   }

                                            }
                                      }
                               }
                               System.out.println("out of streams");
                                             data++;
                                             if(arr_value[data].equalsIgnoreCase("No of streams")) {
                                             //total number of streams
                                             data++;
                                      if (arr_value[data].equalsIgnoreCase("5 Total Streams")) {
                                             System.out.println("entered total streams");
                                            try {

                                                   // base.wait(driver, arr_locator[loc1]);
                                                   loc1 = 4;
                                                   base.scroll(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("5 Total Streams");
                                                   reportStep("PASS", "You have selected 5 Total Streams");
                                                   
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected 5 Total Streams");
                                                    e.printStackTrace();
                                            }

                                      } else if (arr_value[data].equalsIgnoreCase("4 Total Streams")) {

                                            try {
                                                   System.out.println("entered into total streams");
                                                   // base.wait(driver, arr_locator[loc1]);
                                                   loc1 = 5;
                                                   base.scroll(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("4 Total Streams");
                                                   reportStep("PASS", "You have selected 4 Total Streams");
                                                   
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected 4 Total Streams");
                                                   e.printStackTrace();
                                            }

                                      } else if (arr_value[data].equalsIgnoreCase("3 Streaming on Multiple Devices")) {

                                            try {
                                                   // base.wait(driver, arr_locator[loc1]);
                                                   loc1 = 6;
                                                   base.scroll(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("3 Streaming on Multiple Devices");
                                                   reportStep("PASS", "You have selected 3 Streaming on Multiple Devices");
                                            
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected 3 Streaming on Multiple Devices");
                                                   e.printStackTrace();
                                            }

                                      }
                                             }
                                             data++;
                    if(arr_value[data].equalsIgnoreCase("channels")) {
                         data++;

                                      // channel addons
                                      if (arr_value[data].equalsIgnoreCase("STARZ")) {

                                            try {
                                            
                                                   loc1 = 7;
                                                   base.wait(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("STARZ");
                                                   reportStep("PASS", "You have selected STARZ");
                                            
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected STARZ");
                                                   e.printStackTrace();
                                            }

                                      } else if (arr_value[data].equalsIgnoreCase("Kinetic TV Sports and Entertainment")) {

                                            try {
                                                   // 
                                                   loc1 = 8;
                                                   base.wait(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("Kinetic TV Sports and Entertainment");
                                                   reportStep("PASS", "You have selected Kinetic TV Sports and Entertainment");
                                            
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected Kinetic TV Sports and Entertainment");
                                                   e.printStackTrace();
                                            }

                                      } else if (arr_value[data].equalsIgnoreCase("ENCORE")) {

                                            try {
                                                   // 
                                                   loc1 = 9;
                                                   base.wait(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("ENCORE");
                                                   reportStep("PASS", "ENCORE");
                               
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected ENCORE");
                                                   e.printStackTrace();
                                            }

                                      }
                                      if (arr_value[data].equalsIgnoreCase("PXL")) {

                                            try {
                                                   //
                                                   loc1 = 10;
                                                   base.wait(driver, arr_locator[loc1]);
                                                   base.click_element(driver, arr_locator[loc1]);

                                                   System.out.println("PXL");
                                                   reportStep("PASS", "You have selected PXL");
                                      
                                            } catch (Exception e) {
                                                   reportStep("FAIL", "You have not selected PXL");
                                                   e.printStackTrace();
                                            }

                                      }
}
                                      // click on continue
                                      loc1 = 11;
                                      base.click_element(driver, arr_locator[loc1]);

                               

                         } while (loc1 == 11);
                  } catch (Exception e) {
                         System.out.println(e.getMessage());

                  }

            }
            
            
            
            // TV addon page 2
                               else if (action != null && action.equalsIgnoreCase("Entertainment Device")) {

                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                            String val14 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val14.split("%");
                                            String[] arr_value = value.split(",");
                                            int loc1 = 0, data = 0;

                                            do {
                                                   if (loc1 == 0 && data == 0) {
                                                          System.out.println("SELECTING Entertainment Device");
                                                          if (arr_value[data].equalsIgnoreCase("Streaming Device")) {
                                                                data++;
                                                                
                                                                if (loc1 == 0 && arr_value[data].equalsIgnoreCase("Kinetic TV Streaming Device"))

                                                                {
                                                                       try {
                                                                             // base.wait(driver, arr_locator[loc1]);
                                                                             // base.wait(driver, arr_locator[loc1]);
                                                                             base.click_element(driver, arr_locator[loc1]);
                                                                             // base.wait(driver, arr_locator[loc1]);

                                                                             System.out.println("Kinetic TV Streaming Device");
                                                                             reportStep("PASS", "You have selected Kinetic TV Streaming Device");
                                                                       
                                                                       } catch (Exception e) {
                                                                             reportStep("FAIL", "You have not selected Kinetic TV Streaming Device");
                                                                             e.printStackTrace();
                                                                       }
                                                                }

                                                                // Rent Your Streaming Device
                                                                else if (arr_value[data].equalsIgnoreCase("Rent Your Streaming Device")) {

                                                                       try {
                                                                             base.wait(driver, arr_locator[loc1]);
                                                                             loc1 = 1;
                                                                             base.scroll(driver, arr_locator[loc1]);
                                                                             base.click_element(driver, arr_locator[loc1]);
                                                                             Thread.sleep(3000);

                                                                             System.out.println("Rent Your Streaming Device");
                                                                             reportStep("PASS", "You have selected Rent Your Streaming Device");
                                                                             
                                                                       } catch (Exception e) {
                                                                             reportStep("FAIL", "You have not selected Rent Your Streaming Device");
                                                                             e.printStackTrace();
                                                                       }

                                                                }

                                                                else if (arr_value[data].equalsIgnoreCase("Bring Your Own Device")) {

                                                                       try {
                                                                             // base.wait(driver, arr_locator[loc1]);
                                                                             loc1 = 2;

                                                                             base.click_element(driver, arr_locator[loc1]);
                                                                             Thread.sleep(3000);

//                                                                                  System.out.println("data value" +data);
                                                                             System.out.println("Bring Your Own Device");
                                                                             reportStep("PASS", "You have selected Bring Your Own Device");
                                                                             break;
                                                                       } catch (Exception e) {
                                                                             reportStep("FAIL", "You have not selected Bring Your Own Device");
                                                                             e.printStackTrace();
                                                                       }

                                                                } 
                                                          }
                                                   }
                                                   System.out.println("out of streams");
                                                                 data++;
                                                                 if(arr_value[data].equalsIgnoreCase("TV Installation")) {
                                                                 //total number of streams
                                                                        data++;
                                                          if (arr_value[data].equalsIgnoreCase("Self-TV Installation")) {
                                                                System.out.println("Self-TV Installation");
                                                                try {

                                                                       // base.wait(driver, arr_locator[loc1]);
                                                                       loc1 = 3;
                                                                       base.scroll(driver, arr_locator[loc1]);
                                                                       base.click_element(driver, arr_locator[loc1]);

                                                                       System.out.println("Self-TV Installation");
                                                                       reportStep("PASS", "You have selected Self-TV Installation");
                                                                       
                                                                } catch (Exception e) {
                                                                       reportStep("FAIL", "You have not selected Self-TV Installation");
                                                                       e.printStackTrace();
                                                                }

                                                          } else if (arr_value[data].equalsIgnoreCase("Free Professional TV Installation")) {

                                                                try {
                                                                       System.out.println("Free Professional TV Installation");
                                                                       // base.wait(driver, arr_locator[loc1]);
                                                                       loc1 = 4;
                                                                       base.scroll(driver, arr_locator[loc1]);
                                                                       base.click_element(driver, arr_locator[loc1]);

                                                                       System.out.println("Free Professional TV Installation");
                                                                       reportStep("PASS", "You have selected Free Professional TV Installation");
                                                                       
                                                                } catch (Exception e) {
                                                                       reportStep("FAIL", "You have not selected Free Professional TV Installation");
                                                                       e.printStackTrace();
                                                                }

                                                          } 
                                          }

                                                          
                                                          // click on continue
                                                          loc1 = 5;
                                                          base.click_element(driver, arr_locator[loc1]);

                                                   

                                            } while (loc1 == 5);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());

                                      }

                               }
            
            //Kinetic One Gold Speed tile
                               else if (action != null && action.equalsIgnoreCase("KineticOneGoldSpeed")) {

                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                             System.out.println("SELECTING INTERNET SPEED");
                                            String val12 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val12.split("%");
                                            String[] arr_value = value.split(",");

                                            int loc1 = 0;
                                            int data1 = 0;
                                            data1++;
                                             Double.toString(data1);                                                 
                                            
                                             base.wait(driver, arr_locator[loc1]);
                                            
                                            String KineticTile = "//span[contains(text(),'"+arr_value[data1]+"')]/parent::h1/parent::div/p[text()='Kinetic One Gold']";
//                                          String tile200 = "//span[text()='" + arr_value[data1] + "']";
                                             System.out.println("speed "+KineticTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, KineticTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected " + arr_value[data1] + "speed");
                                             reportStep("PASS", "You have selected the internet speed");
                                            
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected the internet speed");
                                             System.out.println(e.getMessage());

                                      }
                               }
            
            //Kinetic One Silver Speed tile with arrow
                               else if (action != null && action.equalsIgnoreCase("KineticOneSilverSpeed")) {

                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                             System.out.println("SELECTING INTERNET SPEED");
                                            String val12 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val12.split("%");
                                            String[] arr_value = value.split(",");

                                            int loc1 = 0;
                                            int data1 = 0;
                                            data1++;
                                             Double.toString(data1);                                                 
                                            
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             Thread.sleep(3000);
                                             base.click_element(driver, arr_locator[loc1]);
                                             Thread.sleep(3000);
                                             base.click_element(driver, arr_locator[loc1]);
                                            
                                            loc1++;
                                            
                                            String KineticTile = "//span[contains(text(),'"+arr_value[data1]+"')]/parent::h1/parent::div/p[text()='Kinetic One Silver']";
//                                          String tile200 = "//span[text()='" + arr_value[data1] + "']";
                                             System.out.println("speed "+KineticTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, KineticTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected " + arr_value[data1] + "speed");
                                             reportStep("PASS", "You have selected the internet speed");
                                            
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected the internet speed");
                                             System.out.println(e.getMessage());

                                      }
                               }
            
            
            //Kinetic One Silver Speed tile without arrow
                               else if (action != null && action.equalsIgnoreCase("KineticOneSilver")) {

                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                             System.out.println("SELECTING INTERNET SPEED");
                                            String val12 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val12.split("%");
                                            String[] arr_value = value.split(",");

                                            int loc1 = 0;
                                            int data1 = 0;
                                            data1++;
                                             Double.toString(data1);                                                 
                                            
                                             base.wait(driver, arr_locator[loc1]);
                                            
                                            
                                            
                                            String KineticTile = "//span[contains(text(),'"+arr_value[data1]+"')]/parent::h1/parent::div/p[text()='Kinetic One Silver']";
//                                          String tile200 = "//span[text()='" + arr_value[data1] + "']";
                                             System.out.println("speed "+KineticTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, KineticTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected " + arr_value[data1] + "speed");
                                             reportStep("PASS", "You have selected the internet speed");
                                            
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected the internet speed");
                                             System.out.println(e.getMessage());

                                      }
                               }
            
            //Kinetic One Modem tile
                               else if (action != null && action.equalsIgnoreCase("KineticOneModem")) {

                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                             System.out.println("SELECTING INTERNET SPEED");
                                            String val12 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val12.split("%");
                                            String[] arr_value = value.split(",");

                                            int loc1 = 0;
                                            int data1 = 0;
                                            
                                             Double.toString(data1);                                                 
                                            
                                             base.wait(driver, arr_locator[loc1]);
                                            
                                            
                                            
                                            String modemTile = "//span[text()='Choose Your Equipment']/parent::p/following-sibling::div/div/label/div//div/p[text()='"+arr_value[data1]+"']";
//                                          String tile200 = "//span[text()='" + arr_value[data1] + "']";
                                             System.out.println("speed "+modemTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, modemTile);
                                            
                                            data1++;
                                            String SecurityTile = "//span[text()='Security Plan ']/parent::p/following-sibling::div/div/label/div//div/p[text()='"+arr_value[data1]+"']";
//                                          String tile200 = "//span[text()='" + arr_value[data1] + "']";
                                             System.out.println("speed "+SecurityTile);
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, SecurityTile);
                                            
                                             base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                                             System.out.println("You have selected " + arr_value[data1] + "speed");
                                             reportStep("PASS", "You have selected the internet speed");
                                            
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You haven't selected the internet speed");
                                             System.out.println(e.getMessage());

                                      }
                               }
            
            
            // Summary checkout

                               else if (action != null && action.equalsIgnoreCase("SummaryCheckout")) {
                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                             System.out.println("You've entered sumarry page");
                                            String val12 = links.getLinkData(LocatorName);
                                             base.wait(driver, val12);
                                             System.out.println("You have moving to checkout");
                                             base.click_element(driver, val12);
                                             reportStep("PASS", "You have moving to checkout");
                                      } catch (Exception e) {
                                             reportStep("FAIL", "You have moving to checkout");
                                             System.out.println(e.getMessage());

                                      }

                               }

            // Checkout

                               else if (action != null && action.equalsIgnoreCase("CheckOut")) {
                                      try {
                                            String Page_Title = driver.getTitle();
                                             System.out.println("PAGE TITLE" + Page_Title);
                                            String val12 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val12.split("%");
                                            String[] arr_value = value.split(",");

                                            int loc = 0;
                                            int data = 0;

                                            do {
                                                   System.out.println("You have been moved to checkout page");
                                                   base.wait(driver, arr_locator[loc]);

                                                   if (loc == 0 && data == 0) {
                                                          try {
                                                                System.out.println("Entering Details");
                                                                // first name
                                                                base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                                System.out.println("First Name:" + arr_value[data]);
                                                                loc++;
                                                                data++;
                                                                // last name
                                                                base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                                System.out.println("Last Name:" + arr_value[data]);
                                                                loc++;
                                                                data++;
                                                                // phone number
                                                                base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                                System.out.println("Phone Number:" + arr_value[data]);
                                                                loc++;
                                                                data++;
                                                                // email id
                                                                base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                                System.out.println("Email Id:" + arr_value[data]);
                                                                loc++;
                                                                data++;
                                                                // ssn number
                                                                base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                                System.out.println("Business Name:" + arr_value[data]);
                                                                
                                                                loc++;
                                                                Thread.sleep(25000);
                                                                // installation date
                                                                base.click_element(driver, arr_locator[loc]);
                                                                loc++;

                                                                // select date
                                                                base.click_element(driver, arr_locator[loc]);
                                                                loc++;
                                                                
                                                                // autopay
                                                                base.click_element(driver, arr_locator[loc]);
                                                                loc++;
                                                                // scroll terms and conditions
                                                                base.scroll(driver, arr_locator[loc]);
                                                                base.click_element(driver, arr_locator[loc]);
                                                                loc++;
                                                                // Place order
                                                                // base.click_element(driver, arr_locator[loc]);
                                                                // loc++;
                                                                Thread.sleep(10000);
                                                                System.out.println("Completed your Purchase");
                                                                reportStep("PASS", "Completed your Purchase");
                                                                
                                                                break;
                                                          } catch (Exception e) {
                                                                reportStep("FAIL", "Completed your Purchase");
                                                                e.printStackTrace();
                                                          }

                                                   }
                                            } while (loc == 10);

                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());

                                      }

                               }
                               
            //KineticStandaloneInternet
            
                               else if (action != null && action.equalsIgnoreCase("KineticStandaloneInternet")) {

                                      
                                            String val12 = links.getLinkData(LocatorName);
                                            String[] arr_locator = val12.split("%");
                                            String[] arr_value = value.split(",");

                                            int loc1 = 0;
                                            
                                          base.wait(driver, arr_locator[loc1]);
                                             base.click_element(driver, arr_locator[loc1]);
                               
                               }

         // InternetCartContainer
                               else if (action != null && action.equalsIgnoreCase("InternetCartContainer")) {
                                      try {
                                             System.out.println("You've entered Internet Cart Container");
                                            String val17 = base.getMapData(LocatorName);
                                            String[] arr_locator = val17.split("%");
                                            String[] arr_value = value.split(",");
                                            int loc = 0;
                                            int data = 0;
                                            do {

                                                   if (loc == 0 && data == 0) {
                                                          base.wait(driver, arr_locator[loc]);
                                                          Thread.sleep(10000);
                                                          base.mousehover(driver, arr_locator[loc]);
                                                          
                                                          System.out.println("Value given by user " + arr_value[data]);

                                                          // Internet speed
                                                          WebElement internetSpeed = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println(internetSpeed);
                                                          String internetSpeedText = internetSpeed.getText();
                                                          System.out.println(internetSpeedText);
                                                          data++;
                                                          if (internetSpeedText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement SpeedPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                                String SpeedPriceText = SpeedPrice.getText();

                                                                if (SpeedPriceText.equals(arr_value[data])) {
                                                                       System.out.println("Speed and pricing has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("Speed and pricing  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }

                                                          // modem
                                                          data++;
                                                          WebElement modemType = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println("modemtype " +modemType);
                                                          String modemText = modemType.getText();
                                                          System.out.println("modem "+modemText);
                                                          
                                                          if (modemText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement modemPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                                String modemPriceText = modemPrice.getText();

                                                                if (modemPriceText.equals(arr_value[data])) {
                                                                       System.out.println("modemPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("modemPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          data++;
                                                          // Security plan
                                                          WebElement securityPlan = driver
                                                                       .findElement(By.xpath("//span[text()='"+arr_value[data]+"']"));
                                                          System.out.println(securityPlan);
                                                          base.scroll(driver, "//span[text()='"+arr_value[data]+"']");
                                                          String securityPlanText = securityPlan.getText();
                                                          System.out.println(securityPlanText);
                                                          data++;
                                                          if (securityPlanText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement securityPlanPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                                String securityPlanPriceText = securityPlanPrice.getText();
                                                                data++;
                                                                if (securityPlanPriceText.equals(arr_value[data])) {
                                                                       System.out.println("securityPlanPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("securityPlanPriceText is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          System.out.println("closing cart container");
                                                          loc++;
                                                          base.mousehover(driver, arr_locator[loc]);
                                                   }
                                            } while (loc == 1);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());
                                             reportStep("Fail", "Not ENTER YOUR USE OWN MODEM RENTAL " + e);

                                      }

                               }
            
            
            // VoiceCartContainer
                               else if (action != null && action.equalsIgnoreCase("VoiceCartContainer")) {
                                      try {
                                             System.out.println("You've entered Internet Cart Container");
                                            String val17 = base.getMapData(LocatorName);
//                                          String[] arr_locator = val17.split("%");
                                            String[] arr_value = value.split(",");
//                                          int loc = 0;
                                            int data = 0;
                                            do {

                                                   if (data == 0) {
//                                                        base.wait(driver, arr_locator[loc]);
//                                                        base.mousehover(driver, arr_locator[loc]);
                                                          Thread.sleep(2000);
                                                          System.out.println("Value given by user " + arr_value[data]);
                                                          
                                                          // voice tile
                                                   
                                                          WebElement voice = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          
                                                          System.out.println(voice);
                                                          String voiceText = voice.getText();
                                                          System.out.println(voiceText);
                                                          data++;
                                                          if (voiceText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement voicePrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                                String voicePriceText = voicePrice.getText();

                                                                if (voicePriceText.equals(arr_value[data])) {
                                                                       System.out.println("voicePrice has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("voicePrice  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }

                                                          // phone number
                                                          data++;
                                                          WebElement phoneNumber = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println("phoneNumber " +phoneNumber);
                                                          String phoneNumberText = phoneNumber.getText();
                                                          System.out.println("phoneNumber "+phoneNumberText);
                                                          
                                                          if (phoneNumberText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement phoneNumberPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                                String phoneNumberPriceText = phoneNumberPrice.getText();

                                                                if (phoneNumberPriceText.equals(arr_value[data])) {
                                                                       System.out.println("phoneNumber has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("phoneNumber  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          break;
                                                   }
                                            } while (data == 4);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());
                                             reportStep("Fail", "Not ENTER YOUR USE OWN MODEM RENTAL " + e);

                                      }

                               }

            // tvCartContainer
                               else if (action != null && action.equalsIgnoreCase("tvCartContainer")) {
                                      try {
                                             System.out.println("You've entered Internet Cart Container");
                                            String val17 = base.getMapData(LocatorName);
//                                          String[] arr_locator = val17.split("%");
                                            String[] arr_value = value.split(",");
                                            int data = 0;
                                            do {

                                                   if (data == 0) {
//                                                        base.wait(driver, arr_locator[loc]);
//                                                        base.mousehover(driver, arr_locator[loc]);
                                                          Thread.sleep(2000);
//                                                        JavascriptExecutor js = (JavascriptExecutor) driver;
//                                                        js.executeScript("scroll(0, 750);");
//                                                        
//                                                        base.mousehover(driver, "(//span[text()='FREE'])[2]");
//                                                        base.mousehover(driver, "(//span[text()='FREE'])[2]");
//                                                        base.mousehover(driver, "//div[@id='product-list-section']");
                                                          System.out.println("Value given by user " + arr_value[data]);
                                                          
                                                          // tv tile
                                                   
                                                          WebElement entertainmentPackage = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          
                                                          System.out.println(entertainmentPackage);
                                                          String entertainmentPackageText = entertainmentPackage.getText();
                                                          System.out.println(entertainmentPackageText);
                                                   
                                                          if (entertainmentPackageText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement entertainmentPackagePrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                                String entertainmentPackagePriceText = entertainmentPackagePrice.getText();

                                                                if (entertainmentPackagePriceText.equals(arr_value[data])) {
                                                                       System.out.println("entertainmentPackagePriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("entertainmentPackagePriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }

                                                          // cloud dvr hours
                                                          data++;
                                                          WebElement dvr = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println("dvrText " +dvr);
                                                          String dvrText = dvr.getText();
                                                          System.out.println("dvrText "+dvrText);
                                                          
                                                          if (dvrText.equals(arr_value[data])) {
                                                                
                                                                WebElement dvrPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]/parent::li/span[2]"));
                                                                data++;
                                                                String dvrPriceText = dvrPrice.getText();
                                                                
                                                                
                                                                if (dvrPriceText.equals(arr_value[data])) {
                                                                       System.out.println("dvrPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("dvrPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                   //Total streams
                                                          
                                                          data++;
                                                          WebElement streams = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"             '])[1]"));
                                                          System.out.println("streams " +streams);
                                                          String streamsText = streams.getText();
                                                          System.out.println("streamsText "+streamsText);
                                                          
                                                          if (streamsText.equals(arr_value[data])) {
                                                                
                                                                WebElement streamsPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"             '])[1]/parent::li/span[2]"));
                                                                data++;
                                                                String streamsPriceText = streamsPrice.getText();
                                                                
                                                                
                                                                if (streamsPriceText.equals(arr_value[data])) {
                                                                       System.out.println("streamsPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("streamsPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          
                                                          //channels
                                                          
                                                          data++;
                                                          base.mousehover(driver, "(//span[text()='FREE'])[2]");
                                                          base.mousehover(driver, "(//span[text()='FREE'])[2]");
                                                          Thread.sleep(3000);
                                                          WebElement channels = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println("channels " +channels);
                                                          String channelsText = channels.getText();
                                                          System.out.println("channelsText "+channelsText);
                                                          
                                                          if (channelsText.equals(arr_value[data])) {
                                                                
                                                                WebElement channelsTextPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]/parent::li/span[2]"));
                                                                data++;
                                                                String channelsTextPriceText = channelsTextPrice.getText();
                                                                
                                                                
                                                                if (channelsTextPriceText.equals(arr_value[data])) {
                                                                       System.out.println("channelsTextPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("channelsTextPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          //Streaming Device
                                                          
                                                          data++;
                                                          WebElement streamingDevice = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println("streamingDevice " +streamingDevice);
                                                          String streamingDeviceText = streamingDevice.getText();
                                                          System.out.println("streamingDeviceText "+streamingDeviceText);
                                                          
                                                          if (streamingDeviceText.equals(arr_value[data])) {
                                                                
                                                                WebElement streamingDeviceTextPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]/parent::li/span[2]"));
                                                                data++;
                                                                String streamingDevicePriceText = streamingDeviceTextPrice.getText();
                                                                
                                                                
                                                                if (streamingDevicePriceText.equals(arr_value[data])) {
                                                                       System.out.println("streamingDevicePriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("streamingDevicePriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          //tvInstllation
                                                          
                                                          data++;
                                                          WebElement tvInstllation = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]"));
                                                          System.out.println("tvInstllationText " +tvInstllation);
                                                          String tvInstllationText = tvInstllation.getText();
                                                          System.out.println("tvInstllationText "+tvInstllationText);
                                                          
                                                          if (tvInstllationText.equals(arr_value[data])) {
                                                                
                                                                WebElement tvInstllationTextPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[1]/parent::li/span[2]"));
                                                                data++;
                                                                String tvInstllationPriceText = tvInstllationTextPrice.getText();
                                                                
                                                                
                                                                if (tvInstllationPriceText.equals(arr_value[data])) {
                                                                       System.out.println("tvInstllationPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("tvInstllationPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          
                                                          break;
                                                          
                                                          
                                                   }
                                            } while (data == 11);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());
                                             reportStep("Fail", "Not ENTER YOUR USE OWN MODEM RENTAL " + e);

                                      }

                               }

            // InternetSummary
                               else if (action != null && action.equalsIgnoreCase("InternetSummaryValidation")) {
                                      try {
                                             System.out.println("You've entered Summary Page");
                                            String val17 = base.getMapData(LocatorName);
//                                          String[] arr_locator = val17.split("%");
                                            String[] arr_value = value.split(",");
//                                          int loc = 0;
                                            int data = 0;
                                            do {

                                                   if (data == 0) {
//                                                        base.wait(driver, arr_locator[loc]);
                                                          Thread.sleep(5000);
//                                                        base.mousehover(driver, arr_locator[loc]);
                                                          
                                                          System.out.println("Value given by user " + arr_value[data]);
                                                          
                                                          // Internet speed
                                                          base.scroll(driver, "(//span[text()='"+arr_value[data]+"'])[2]");
                                                          Thread.sleep(5000);
                                                          WebElement internetSpeed = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println(internetSpeed);
                                                          String internetSpeedText = internetSpeed.getText();
                                                          System.out.println(internetSpeedText);
                                                          data++;
                                                          if (internetSpeedText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement SpeedPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                                String SpeedPriceText = SpeedPrice.getText();

                                                                if (SpeedPriceText.equals(arr_value[data])) {
                                                                       System.out.println("Speed and pricing has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("Speed and pricing  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }

                                                          // modem
                                                          data++;
                                                          WebElement modemType = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println("modemtype " +modemType);
                                                          String modemText = modemType.getText();
                                                          System.out.println("modem "+modemText);
                                                          
                                                          if (modemText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement modemPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                                String modemPriceText = modemPrice.getText();

                                                                if (modemPriceText.equals(arr_value[data])) {
                                                                       System.out.println("modemPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("modemPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          data++;
                                                          // Security plan
                                                          WebElement securityPlan = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println(securityPlan);
                                                          base.scroll(driver, "//span[text()='"+arr_value[data]+"']");
                                                          String securityPlanText = securityPlan.getText();
                                                          System.out.println(securityPlanText);
                                                          data++;
                                                          if (securityPlanText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement securityPlanPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                                String securityPlanPriceText = securityPlanPrice.getText();
                                                                data++;
                                                                if (securityPlanPriceText.equals(arr_value[data])) {
                                                                       System.out.println("securityPlanPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("securityPlanPriceText is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          break;
                                                   }
                                            } while (data == 8);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());
                                             reportStep("Fail", "Not ENTER YOUR USE OWN MODEM RENTAL " + e);

                                      }

                               }
            
            // VoiceSummary
                               else if (action != null && action.equalsIgnoreCase("VoiceSummaryValidation")) {
                                      try {
                                             System.out.println("You've entered Voice Summary Page");
                                            String val17 = base.getMapData(LocatorName);
//                                          String[] arr_locator = val17.split("%");
                                            String[] arr_value = value.split(",");
//                                          int loc = 0;
                                            int data = 0;
                                            do {

                                                   if (data == 0) {
//                                                        base.wait(driver, arr_locator[loc]);
//                                                        base.mousehover(driver, arr_locator[loc]);
                                                          Thread.sleep(2000);
                                                          System.out.println("Value given by user " + arr_value[data]);
                                                          
                                                          // voice tile
                                                   
                                                          WebElement voice = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          
                                                          System.out.println(voice);
                                                          String voiceText = voice.getText();
                                                          System.out.println(voiceText);
                                                          data++;
                                                          if (voiceText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement voicePrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                                String voicePriceText = voicePrice.getText();

                                                                if (voicePriceText.equals(arr_value[data])) {
                                                                       System.out.println("voicePrice has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("voicePrice  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }

                                                          // phone number
                                                          data++;
                                                          
                                                          base.scroll(driver, "(//span[text()='"+arr_value[data]+"'])[2]");
                                                          WebElement phoneNumber = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println("phoneNumber " +phoneNumber);
                                                          String phoneNumberText = phoneNumber.getText();
                                                          System.out.println("phoneNumber "+phoneNumberText);
                                                          
                                                          if (phoneNumberText.equals(arr_value[data])) {
                                                                data++;
                                                                WebElement phoneNumberPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[3]"));
                                                                String phoneNumberPriceText = phoneNumberPrice.getText();

                                                                if (phoneNumberPriceText.equals(arr_value[data])) {
                                                                       System.out.println("phoneNumber has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("phoneNumber  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          break;
                                                   }
                                            } while (data == 4);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());
                                             reportStep("Fail", "Not ENTER YOUR USE OWN MODEM RENTAL " + e);

                                      }

                               }
            // tvSummary
                               else if (action != null && action.equalsIgnoreCase("tvSummaryValidation")) {
                                      try {
                                             System.out.println("You've entered Summary Page");
                                            String val17 = base.getMapData(LocatorName);
//                                          String[] arr_locator = val17.split("%");
                                            String[] arr_value = value.split(",");
                                            int data = 0;
                                            do {

                                                   if (data == 0) {
//                                                        base.wait(driver, arr_locator[loc]);
//                                                        base.mousehover(driver, arr_locator[loc]);
                                                          Thread.sleep(2000);
//                                                        JavascriptExecutor js = (JavascriptExecutor) driver;
//                                                        js.executeScript("scroll(0, 750);");
//                                                        
//                                                        base.mousehover(driver, "(//span[text()='FREE'])[2]");
//                                                        base.mousehover(driver, "(//span[text()='FREE'])[2]");
//                                                        base.mousehover(driver, "//div[@id='product-list-section']");
                                                          System.out.println("Value given by user " + arr_value[data]);
                                                          
                                                          // tv tile
                                                   
                                                          WebElement entertainmentPackage = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          
                                                          System.out.println(entertainmentPackage);
                                                          String entertainmentPackageText = entertainmentPackage.getText();
                                                          System.out.println(entertainmentPackageText);
                                                   
                                                          if (entertainmentPackageText.equals(arr_value[data])) {
                                                                data++;
                                                                base.scroll(driver, "(//span[text()='"+arr_value[data]+"'])[2]");
                                                                WebElement entertainmentPackagePrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                                String entertainmentPackagePriceText = entertainmentPackagePrice.getText();

                                                                if (entertainmentPackagePriceText.equals(arr_value[data])) {
                                                                       System.out.println("entertainmentPackagePriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("entertainmentPackagePriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }

                                                          // cloud dvr hours
                                                          data++;
                                                          WebElement dvr = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println("dvrText " +dvr);
                                                          String dvrText = dvr.getText();
                                                          System.out.println("dvrText "+dvrText);
                                                          
                                                          if (dvrText.equals(arr_value[data])) {
                                                                
                                                                WebElement dvrPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]/parent::li/span[2]"));
                                                                data++;
                                                                String dvrPriceText = dvrPrice.getText();
                                                                
                                                                
                                                                if (dvrPriceText.equals(arr_value[data])) {
                                                                       System.out.println("dvrPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("dvrPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                   //Total streams
                                                          
                                                          data++;
                                                          WebElement streams = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"             '])[2]"));
                                                          System.out.println("streams " +streams);
                                                          String streamsText = streams.getText();
                                                          System.out.println("streamsText "+streamsText);
                                                          
                                                          if (streamsText.equals(arr_value[data])) {
                                                                
                                                                WebElement streamsPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"             '])[2]/parent::li/span[2]"));
                                                                data++;
                                                                String streamsPriceText = streamsPrice.getText();
                                                                
                                                                
                                                                if (streamsPriceText.equals(arr_value[data])) {
                                                                       System.out.println("streamsPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("streamsPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          
                                                          //channels
                                                          
                                                          data++;
//                                                        base.mousehover(driver, "(//span[text()='FREE'])[2]");
//                                                        base.mousehover(driver, "(//span[text()='FREE'])[2]");
                                                          Thread.sleep(3000);
                                                          base.scroll(driver, "(//span[text()='"+arr_value[data]+"'])[2]");
                                                          WebElement channels = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println("channels " +channels);
                                                          String channelsText = channels.getText();
                                                          System.out.println("channelsText "+channelsText);
                                                          
                                                          if (channelsText.equals(arr_value[data])) {
                                                                
                                                                WebElement channelsTextPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]/parent::li/span[2]"));
                                                                data++;
                                                                String channelsTextPriceText = channelsTextPrice.getText();
                                                                
                                                                
                                                                if (channelsTextPriceText.equals(arr_value[data])) {
                                                                       System.out.println("channelsTextPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("channelsTextPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          //Streaming Device
                                                          
                                                          data++;
                                                          WebElement streamingDevice = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println("streamingDevice " +streamingDevice);
                                                          String streamingDeviceText = streamingDevice.getText();
                                                          System.out.println("streamingDeviceText "+streamingDeviceText);
                                                          
                                                          if (streamingDeviceText.equals(arr_value[data])) {
                                                                
                                                                WebElement streamingDeviceTextPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]/parent::li/span[2]"));
                                                                data++;
                                                                String streamingDevicePriceText = streamingDeviceTextPrice.getText();
                                                                
                                                                
                                                                if (streamingDevicePriceText.equals(arr_value[data])) {
                                                                       System.out.println("streamingDevicePriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("streamingDevicePriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          //tvInstllation
                                                          
                                                          data++;
                                                          WebElement tvInstllation = driver
                                                                       .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]"));
                                                          System.out.println("tvInstllationText " +tvInstllation);
                                                          String tvInstllationText = tvInstllation.getText();
                                                          System.out.println("tvInstllationText "+tvInstllationText);
                                                          
                                                          if (tvInstllationText.equals(arr_value[data])) {
                                                                
                                                                WebElement tvInstllationTextPrice = driver
                                                                              .findElement(By.xpath("(//span[text()='"+arr_value[data]+"'])[2]/parent::li/span[2]"));
                                                                data++;
                                                                String tvInstllationPriceText = tvInstllationTextPrice.getText();
                                                                
                                                                
                                                                if (tvInstllationPriceText.equals(arr_value[data])) {
                                                                       System.out.println("tvInstllationPriceText has been validated");
                                                                       reportStep("PASS", "ENTER YOUR INTERNETCARTCONTAINER");
                                                                } else {
                                                                       System.out.println("tvInstllationPriceText  is not validated");
                                                                       reportStep("FAIL", "NOT ENTER YOUR INTERNETCARTCONTAINER");

                                                                }
                                                          }
                                                          
                                                          
                                                          break;
                                                          
                                                          
                                                   }
                                            } while (data == 11);
                                      } catch (Exception e) {
                                             System.out.println(e.getMessage());
                                             reportStep("Fail", "Not ENTER YOUR USE OWN MODEM RENTAL " + e);

                                      }

                               }

            // TestEnviLogin
                               else if (action != null && action.equalsIgnoreCase("TestEnvi_Login")) {
                                     try {
                                            System.out.println("TestEnvi_Login");
                                          
                                            String username = links.getLinkData(LocatorName);
                                            String[] arr_locator = username.split("%");
                                            String[] arr_value = value.split(",");
                                            int loc = 0;
                                            int data = 0;
                                            base.click_element(driver, arr_locator[loc]);
                                            if (loc == 0 && data == 0) {
                                                   System.out.println("USERNAME " + arr_value[data]);
                                               

                                                  System.out.println("Enter your credentials");
                                            
                                                  base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                  loc++;
                                                  data++;
                                                   System.out.println("PASSWORD " + arr_value[data]);
                                                  base.SendKeys(driver, arr_locator[loc], arr_value[data]);
                                                  loc++;
                                                   base.click_element(driver, arr_locator[loc]);
                                                  System.out.println("Log in Successfully");
                                               
                                            }
                                            
                                     } catch (Exception e) {
                                            System.out.println(e.getMessage());

                                            reportStep("FAIL", "please enter the valid credentials");
                                     }

                               }


			
			// default
		}
	}
}
