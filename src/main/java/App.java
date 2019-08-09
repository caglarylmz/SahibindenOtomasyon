import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class App {
    static ChromeOptions options;
    static ChromeDriver driver;
    static NgWebDriver ngDriver;
    static IlanGetir ilanGetir;
    static  IlanOlustur ilanOlustur;
    static String urlIlanlar ="https://banaozel.sahibinden.com/ilanlarim";

    public static void main(String[] args) throws InterruptedException, IOException {
        prepareBrowser();
        openUrl(urlIlanlar);
        ilanGetir= new IlanGetir(driver,ngDriver);
       // ilanOlustur = new IlanOlustur(driver,ngDriver);

    }

    public static void prepareBrowser(){
        options= new ChromeOptions();
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

    }

    public static void openUrl(String url) throws InterruptedException {
        driver.navigate().to(url);
        Thread.sleep(3000);
    }



}
