package seleniumProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.io.FileUtils;

public class TaceTAPractice {

	public static void main(String[] args) {
		TaceTAPractice taPractice = new TaceTAPractice();
		taPractice.getTA125Stats();
	}

	public void getTA125Stats() {

		WebDriver driver;
		try {
			driver = invokeBrowser(Constants.XPath.TAUrl);	
		} catch(Exception e) {
			System.out.println("Somemthing went wrong with invoking the browser, error:" + e.getLocalizedMessage());
			return;
		}
		
		System.out.println("Tapping on TA125");
		driver.findElement(By.xpath(Constants.XPath.TALinkButton)).click();
		
		System.out.println("Tapping on More About button");
		driver.findElement(By.xpath(Constants.XPath.TAMoreAboutButton)).click();

		makeBrowserSleep();
		
		System.out.println("Tapping on Index Components");
		driver.findElement(By.xpath(Constants.XPath.TAIndexComponents)).click();
		
		makeBrowserSleep();
		
		List<CompanyData> companies = new ArrayList<CompanyData>();
		
		System.out.println("Generating companies into objects");
		companies = listGenerator(companies, driver, 1);
		
		System.out.println("Generated "+ companies.size() + " Companies");
		System.out.println("Sorting companies");
		Collections.sort(companies);

		try {
			writeTableIntoFileInText(companies);
			System.out.println("Writing data into file has been done");
		} catch (IOException e) {
			System.out.println("Something went bad in saving companies data into text file: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		// Part 2 
		lookForCompany(companies, driver);

	}
	
	private void lookForCompany(List<CompanyData> companies, WebDriver driver) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("What do you want todo:");
			System.out.println("Press 1 to Choose a company");
			System.out.println("Press Any other number to terminate the program");
			String firstUserChoice = scanner.nextLine();  // Read user input
			Integer convertedUserChoice = 0;
			try {
				convertedUserChoice = Integer.valueOf(firstUserChoice);
			} catch (NumberFormatException numberException) {
				numberException.printStackTrace();
				System.out.println("Please choose a valid number");
				continue;
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("Please choose a valid number");
				continue;
			}
			
			if (convertedUserChoice != 1) {
				System.out.println("Bye Bye");
				break;
			}
			
		    System.out.println("Enter Company name:");
		    String companyName = scanner.nextLine();  // Read user input
		    System.out.println("Looking for the following company name: " + companyName);
		    
		    CompanyData chosenCompany = null;
			for (CompanyData company : companies) {
//				System.out.println("Comparing chosenCompany: " + companyName + " to:" + company.name);
				if (company.name.equals(companyName)) {
					chosenCompany = company;
					break;
				}
			}
			
			if (chosenCompany == null) {
				System.out.println("Invalid company name, going back to the menu");
				continue;
			}
			
			System.out.println("Navigating to company url");
			driver.get(chosenCompany.companyUrl);
			
			System.out.println("Taking screen shot");
			takeScreenShot(driver, chosenCompany);
						
		}
		scanner.close();
	}
	
	private void takeScreenShot(WebDriver driver, CompanyData company) {
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);

//		NOVOLOG_2021-10-10 23/10/37.951
		File destFile = new File(company.name + "_" + timestamp.toString().replace("/", "").replace("-", "").replace(" ", "").replace(".", "") + ".jpg");
        
        //Copy file at destination
        try {
        	// import org.apache.commons.io.FileUtils
			FileUtils.copyFile(srcFile, destFile);
			System.out.println("Saved succesfully into:" + destFile.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("error in saving into file");
			e.printStackTrace();
		}

	}
	
	private void writeTableIntoFileInText(List<CompanyData> companies) throws IOException {
		String tableStr = "";
		for (CompanyData company : companies) {
			tableStr += company.toString() + "\n";
		}
		this.writeToDisk(Constants.Disk.FileNameTableListCompanies, tableStr);
	}


	private Object readFromDisk(String filename) {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(filename));
			Object anyObject = in.readObject();
			in.close();
			return anyObject;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void writeToDisk(String filename, Object anyObject) throws IOException {
		// On Mac it's saving this to the current directory
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(anyObject);
		oos.close();
	}

	private WebDriver invokeBrowser(String url) throws Exception {
		// Driver property can be different from mac to mac
		System.setProperty("webdriver.chrome.driver","/Users/guycohen/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get(url);
		return driver;
	}

	private List<CompanyData> listGenerator(List<CompanyData> companies, WebDriver driver, int pageNumber) {
		
		List<WebElement> companyNameElement = driver.findElements(By.xpath(Constants.XPath.NameElement));
		Iterator<WebElement> companyNameItr= companyNameElement.iterator();
		List<WebElement> companyUrlElement = driver.findElements(By.xpath(Constants.XPath.URLElement));
		Iterator<WebElement> companyUrlItr = companyUrlElement.iterator();
		List<WebElement> symbolElement = driver.findElements(By.xpath(Constants.XPath.SymbolElement));
		Iterator<WebElement> symbolItr= symbolElement.iterator();
		List<WebElement> isinElement = driver.findElements(By.xpath("//tbody/tr/td[3]"));
		Iterator<WebElement> isinItr= isinElement.iterator();
		List<WebElement> lastRateElement = driver.findElements(By.xpath("//tbody/tr/td[4]"));
		Iterator<WebElement> lastRateItr= lastRateElement.iterator();
		List<WebElement> changeElement = driver.findElements(By.xpath("//tbody/tr/td[5]"));
		Iterator<WebElement> changeItr= changeElement.iterator();
		List<WebElement> turnoverElement = driver.findElements(By.xpath("//tbody/tr/td[6]"));
		Iterator<WebElement> turnoverItr= turnoverElement.iterator();
		List<WebElement> lastTradeElement = driver.findElements(By.xpath("//tbody/tr/td[7]"));
		Iterator<WebElement> lastTradeItr= lastTradeElement.iterator();
		List<WebElement> baseTradeElement = driver.findElements(By.xpath("//tbody/tr/td[8]"));
		Iterator<WebElement> baseTradeItr= baseTradeElement.iterator();
		List<WebElement> openingPriceElement = driver.findElements(By.xpath("//tbody/tr/td[9]"));
		Iterator<WebElement> openingPriceItr= openingPriceElement.iterator();
		int companyIndex = 1;
		while(companyNameItr.hasNext()) {
			System.out.println("Generating from page:" + pageNumber +  ", Company:" + companyIndex);
			companyIndex++;
			String companyName = companyNameItr.next().getText().replaceAll("\n", "");
			String companyUrl = companyUrlItr.next().getAttribute("href");
			String symbol = symbolItr.next().getText();
			String isin = isinItr.next().getText();
			String lastRate = lastRateItr.next().getText();
			String change = changeItr.next().getText();
			String turnover = turnoverItr.next().getText();
			Double turnoverConverted = 0.0;
			try {
				turnoverConverted =  Double.parseDouble(turnover.replaceAll(",", ""));	
			} catch(Exception e) {
				e.printStackTrace();
			}

			String lastTrade = lastTradeItr.next().getText();
			String baseTrade = baseTradeItr.next().getText();
			String openingPrice = openingPriceItr.next().getText().replaceAll("לינקים לפעולות שונות", "");
			CompanyData company = new CompanyData(companyName, 
					companyUrl,
					symbol, 
					isin,
					lastRate, 
					change, 
					turnoverConverted,
					lastTrade, 
					baseTrade, 
					openingPrice);
			companies.add(company);
		}

		makeBrowserSleep();

		try {
			driver.findElement(By.xpath(Constants.XPath.NextPage)).click();
			this.makeBrowserSleep();
			pageNumber++;
			return this.listGenerator(companies, driver, pageNumber);
		} catch (NoSuchElementException e) {
			System.out.println("We don't have any pages to navigate, we can return from here");
			return companies;
		} 
	}

	private void makeBrowserSleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

