import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

public class TestGooglePage {
    private WebDriver driver;

    public static final String GOOGLE_URL = "https://www.google.com";

    @BeforeClass
    private void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterClass
    private void tearDown() {
        driver.close();
    }

    @Test(description = "Check user can navigate to google.com")
    private void checkGoogleHomePage() {
        driver.get(GOOGLE_URL);
        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Google']")).isDisplayed());
    }

    @Test(description = "Check searches for 'Guma Turbo' returns results")
    private void checkSearchesForGumaTurbo() {
        driver.get(GOOGLE_URL);

        WebElement searchInput = driver.findElement(By.xpath("//input[@name='q']"));
        searchInput.sendKeys("Guma Turbo");
        searchInput.sendKeys(Keys.ENTER);

        WebElement searchResultsStats = driver.findElement(By.id("result-stats"));
        int searchResults = Integer.parseInt(searchResultsStats.getText().replaceAll("\\D+",""));

        List<WebElement> searchResultsItems = driver.findElements(By.xpath("//h3"));

        Assert.assertTrue(searchResults > 0);
        Assert.assertTrue(searchResultsItems.size() > 0);
    }

    @Test(description = "Check that no results are returned on custom non sense search")
    private void checkSearchReturnsNoResultsForRandomStrings() {
        driver.get(GOOGLE_URL);

        String text = String.format("thisWillDefinitelyNotReturnAnyResultsOnGoogleTrustMeToday:%s", new Date().getTime());

        WebElement searchInput = driver.findElement(By.xpath("//input[@name='q']"));
        searchInput.sendKeys(text);
        searchInput.sendKeys(Keys.ENTER);

        List<WebElement> searchResultsItems = driver.findElements(By.xpath("//h3"));

        Assert.assertEquals(searchResultsItems.size(), 0);
    }
}
