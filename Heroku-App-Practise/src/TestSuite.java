import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class TestSuite {

	public static void main(String[] args) throws InterruptedException {

		// Configurations.
		String URL = "https://the-internet.herokuapp.com/";
		String homePageTitle = "The Internet";

		// Setting up Chrome driver and basic
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get(URL);

		// Check if landed on the right URL
		if (!driver.getCurrentUrl().equals(URL)) {
			System.out.println("URL has been redirected, Please be cautious !\n Expected URL : " + URL
					+ ", Actual URL :" + driver.getCurrentUrl());
			driver.quit();
			System.exit(1);
		}

		// Check the title
		if (!driver.getTitle().equals(homePageTitle)) {
			System.out.println("Expected Title : " + homePageTitle + ", Actual Title :" + driver.getTitle());
			driver.quit();
			System.exit(1);
		}

		// Check the header text
		String headerText = driver.findElement(By.tagName("h1")).getText();
		if (!headerText.equals("Welcome to the-internet")) {
			System.out.println("Expected Header Text : Welcome to the-internet, Actual Header Text :" + headerText);
			driver.quit();
			System.exit(1);
		}

		// Check content header text
		String contentHeaderText = driver.findElement(By.tagName("h2")).getText();
		if (!contentHeaderText.equals("Available Examples")) {
			System.out.println("Expected Content Header Text : Welcome to the-internet, Actual Content Header Text :"
					+ contentHeaderText);
			driver.quit();
			System.exit(1);
		}

		// Collecting all the links to test
		List<String> functionNames = new ArrayList<String>();
		Map<String, WebElement> arguments = new HashMap<>();

		List<WebElement> links = driver.findElements(By.cssSelector("li a"));

		for (WebElement link : links) {
			String functionName = link.getText().replaceAll("[^a-zA-Z]", "_");
			functionNames.add(functionName);
			arguments.put(functionName, link);
		}

		// Dummy
		int count = 0;
		int expectedCount = 3;

		for (String name : functionNames) {
			try {
				Method method = TestSuite.class.getDeclaredMethod(name, WebElement.class, WebDriver.class);
				method.invoke(null, arguments.get(name), driver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			driver.navigate().back();
			if (count == expectedCount)
				break;
			count++;
		}
		Thread.sleep(3000);
		System.out.println("Test finised to success !!");
	}

	public static void A_B_Testing(WebElement link, WebDriver driver) {
		link.click();
		String headerText = driver.findElement(By.tagName("h3")).getText();
		if (!headerText.equals("A/B Test Control") && !headerText.equals("A/B Test Variation 1")) {
			System.out.println("Expected Header Text : A/B Test Control, Actual Header Text : " + headerText);
//			driver.quit();
			System.exit(1);
		}
	}

	public static void Add_Remove_Elements(WebElement link, WebDriver driver) {
		link.click();
		driver.findElement(By.xpath("//button[text()='Add Element']")).click();
		String addedBtnText = driver.findElement(By.cssSelector("button[onclick='deleteElement()']")).getText();
		if (!addedBtnText.equals("Delete")) {
			System.out.println("Expected Button Text : Delete, Actual Button Text :" + addedBtnText);
			driver.quit();
			System.exit(1);
		}
	}

	public static void Basic_Auth(WebElement link, WebDriver driver) throws InterruptedException {
		System.out.println("Skipping for now Basic_Auth()");
	}

	public static void Broken_Images(WebElement link, WebDriver driver) throws InterruptedException {
		Thread.sleep(5000);
		System.out.println(link.isDisplayed());
		List<WebElement> img = driver.findElements(By.tagName("img"));

		if (img.get(0).isDisplayed()) {
			driver.quit();
			System.exit(1);
			System.out.println("Image is not broken, please check website");
		}

		if (img.get(1).isDisplayed()) {
			driver.quit();
			System.exit(1);
			System.out.println("Image is not broken, please check website");
		}

		if (!img.get(1).isDisplayed()) {
			driver.quit();
			System.exit(1);
			System.out.println("Image is not broken, please check website");
		}
	}
}
