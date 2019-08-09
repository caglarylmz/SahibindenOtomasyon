package Model;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class İlanCek {
   /* public İlanCek() {
        try {
            Document doc = Jsoup.connect("https://banaozel.sahibinden.com/ilanlarim").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    public static void main(String[] args) throws InterruptedException {
        ChromeDriver driver;
        NgWebDriver ngDriver;
        ChromeOptions options;
        options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/cagla/AppData/Local/Google/Chrome/User Data/Profile 1");
        options.addArguments("--profile-directory=Profile 1");
        options.addArguments("--disable-extension");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //driver.manage().window().maximize();
        ngDriver = new NgWebDriver(driver);
        ngDriver.waitForAngularRequestsToFinish();

        driver.navigate().to("https://banaozel.sahibinden.com/ilanlarim");
        Thread.sleep(3000);

        List<WebElement> ilanlar = driver.findElements(ByAngular.repeater("classified in classifieds"));

        for (int i = 0; i <ilanlar.size() ; i++) {
           List<WebElement> ilanLinkleri = ilanlar.get(i).findElements(ByAngular.binding("classified.title"));

            for (int j = 0; j < ilanLinkleri.size() ; j++) {
                System.out.println(ilanLinkleri.get(j).getAttribute("href"));

            }
        }



    }


}

