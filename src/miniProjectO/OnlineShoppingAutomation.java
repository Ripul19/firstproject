package miniProjectO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OnlineShoppingAutomation
{
	
	public static WebDriver driver;
	public static String baseURL="http://www.flipkart.com";
	public static int rowCount;

	public static String[] text=new String[10];
	public static String browser;

	//Reading the Excel File-----------------------------------------------------------------------
	public static void readFile() throws IOException 
	{
		FileInputStream readFile = new FileInputStream("Excel\\BrowserData.xlsx");		
		XSSFWorkbook workbook=new XSSFWorkbook(readFile);
		XSSFSheet sheet = workbook.getSheet("Sheet1");
		rowCount = sheet.getLastRowNum();

		Row row;
		Cell cell;
		int index =0;
		
		Iterator<Row> rowIterator = sheet.iterator();
		{
			while(rowIterator.hasNext())
			{
				row = rowIterator.next();
				
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext())
				{
					cell = cellIterator.next();
					
					DataFormatter formatter = new DataFormatter();
					text[index] = formatter.formatCellValue(cell);
					index++;
					
				}
			}
		}
		workbook.close();
	}
	
	//selecting the browser-------------------------------------------------------------------------
	public static void selectBrowser(String browser)throws InterruptedException 
	{
		// Launching Chrome browser.
		if(browser.equalsIgnoreCase("chrome")) 
		{
			System.out.println("Launching Chrome Browser");
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		//launching firefox browser
		else if(browser.equalsIgnoreCase("firefox")) 
		{
			System.out.println("Launching Firefox Browser");
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}
		
	}
	
	//going to flipkart.com---------------------------------------------------------------------------
	public static void toUrl() throws InterruptedException 
	{
		driver.get(baseURL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		Thread.sleep(2000);
	}
	
	//login at flipkart--------------------------------------------------------------------------------
	public static void loginFlipkart() 
	{
		
		driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/form/div[1]/input")).sendKeys("#login id");
		driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/form/div[2]/input")).sendKeys("#pass");
		driver.findElement(By.xpath("//button/span[text()='Login']")).click();
	}
	
	//main driver code--------------------------------------------------------------------------------------
	public static void addToCart() throws InterruptedException 
	{ 
		Thread.sleep(3000);
		//search
		driver.findElement(By.xpath("//input[@placeholder='Search for products, brands and more']")).sendKeys("Home appliances");
		WebElement searchResult = driver.findElement(By.xpath("//button"));
		
		searchResult.click();
		driver.navigate().refresh();

		WebDriverWait wait = new WebDriverWait(driver, 30);

		//selecting 1st item
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='container']/div/div[3]/div[1]/div[2]/div[2]/div/div[1]/div/a[2]"))).click();
		Thread.sleep(2000);
		
		//to change tabs
		Set<String> windowIDs =driver.getWindowHandles();
		Iterator<String> itr=windowIDs.iterator();
		String firstpageId=itr.next();
		String secondpageId=itr.next();
		driver.switchTo().window(secondpageId);
		
		//adding selecting item to cart and getting its price 
		driver.navigate().refresh();
		String strI1= driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[2]/div/div[3]/div[1]/div/div[1]")).getText();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[1]/div[2]/div/ul/li[1]/button"))).click();
		Thread.sleep(2000); 
		
		String strTA1=driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div[1]/div/div/div[1]/div[5]/div/span/div/div/span")).getText();
		System.out.println("Total Price from cart: "+ strTA1);
		Thread.sleep(1000);
		
		driver.close();
		Thread.sleep(1000);
		driver.switchTo().window(firstpageId);
		
		//selecting 2nd item
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[2]/div/div[2]/div/a[2]"))).click();

		//to switch tabs
		windowIDs=driver.getWindowHandles();
		itr=windowIDs.iterator();
		firstpageId=itr.next();
		secondpageId=itr.next();
		
		driver.switchTo().window(secondpageId);
		Thread.sleep(3000);
		
		//adding 2nd item to cart and getting its price
		String strI2=driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[2]/div/div[3]/div[1]/div/div[1]")).getText();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[3]/div[1]/div[1]/div[2]/div/ul/li[1]/button"))).click();

		Thread.sleep(2000);
		
		//getting total price from cart
		String strTA= driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[2]/div/div[1]/div[2]/div[1]/div/div/div[1]/div[5]/div/span/div/div/span")).getText();
		System.out.println("Revised Order Amount: "+ strTA);
		Thread.sleep(2000);
		
		removeItems();
		
		//to removing UTF-8 characters (calling charRemoveAt function)
		String strTI1=charRemoveAt(strI1);
		String strTI2=charRemoveAt(strI2);
		String strTTA=charRemoveAt(strTA);
		
		//converting to integers
		int I1=Integer.parseInt(strTI1);
		int I2=Integer.parseInt(strTI2);
		int FTA= Integer.parseInt(strTTA);
		
		//adding the 2 integers- (calling add function)
		int FI=add(I2,I1);      

		//validating-checkpoint
		if(FI==FTA) 
		{
			System.out.println("Checkpoint: Validate");
		}
		else 
		{
			System.out.println("Checkpoint: Not Validate");
		}
		Thread.sleep(1000);
	}
	
	//Removing the items-------------------------------------------------------------------------------
	public static void removeItems() throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.findElement(By.xpath("//div[text()=\"Remove\"]")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div[2]"))).click();
		Thread.sleep(2000);
		driver.navigate().refresh();
		
		JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement Element = driver.findElement(By.xpath("//div[text()=\"Remove\"]"));

        //This will scroll the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", Element);
		
		driver.findElement(By.xpath("//div[text()=\"Remove\"]")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div[2]"))).click();
		
		

	}
	
	//closing the browser-----------------------------------------------------------------------------
	public static void closeBrowser() 
	{ 
		driver.close();
		driver.quit();
	}
    
	//removing UTF-8 characters------------------------------------------------------------------------
	public static String charRemoveAt(String str) 
	{ 
        String resultStr="";
        for (int a=0;a<str.length();a++)  
        {  
        	//comparing alphabets with their corresponding ASCII value  
              if (str.charAt(a)>=48 && str.charAt(a)<=57)        
              {   
            	//adding characters into empty string 
                  resultStr =resultStr+str.charAt(a);                
              }
       } 
        return resultStr;
     }  
	
	//method for addition---------------------------------------------------------------------------------------
    public static int add(int a, int b) 
    { 
    	return a+b;
    }
    
	//main
	public static void main(String[]args) throws InterruptedException, IOException
	{
		readFile();
		for(int index=0;index<=rowCount;index++)
		{
			selectBrowser(text[index]);
			toUrl();
			loginFlipkart();
			addToCart();
			//removeItems();
			closeBrowser();	
		}
	}

}