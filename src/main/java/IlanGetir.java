import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;


public class IlanGetir {
    private ChromeDriver driver;
    private NgWebDriver ngDriver;
    static String urlIlanlar ="https://banaozel.sahibinden.com/ilanlarim";

    public IlanGetir(ChromeDriver driver,NgWebDriver ngDriver) throws IOException, InterruptedException {
        this.driver=driver;
        this.ngDriver=ngDriver;

        createAdvertFolders();
        getAdvertDatas();

    }

    public boolean checkUrl(){
        String actualTitle = driver.getTitle();
        String expectedTitle = "Yayında Olan İlanlar";
        if (actualTitle.equalsIgnoreCase(expectedTitle))
            return true;
        else
            return false;
    }

    public int advertCount(){
        //ilan sayısı
        String ilanCount = driver.findElement(ByAngular.binding("classifiedsHolder.totalCount")).getText();
        System.out.println("ilan sayısı =" + ilanCount);
        return Integer.valueOf(ilanCount);
    }

    public void createAdvertFolders(){
        /**
         * ilan id'leri klasör açıyoruz. Bu alanda ilana ait datalar bir json içinde tutulacak. ayrıca resimler buraya indirilecek
         */
        //ilan idleri
        List<WebElement> ilanNumaraları = driver.findElements(ByAngular.binding("classified.id"));
        //id'ler ile klasör açılıyor
        for (int i = 0; i <ilanNumaraları.size() ; i++) {
            String fileName = ilanNumaraları.get(i).getText();

            File file = new File("ilanlar/"+fileName);
            file.mkdir();

        }
    }

    public void getAdvertDatas() throws InterruptedException, IOException {
        /**
         * Sırayla ilanların linkleri açılıyor, bilgiler çekiliyor ve ilan kapatılıyor
         */
        //ilan linkleri alıınıyor
        List<WebElement> advertLinks = driver.findElements(By.xpath("//li[@class='cl-image-column']/a"));
        for (int i = 0; i <advertLinks.size() ; i++) {
            System.out.println(advertLinks.get(i).getAttribute("href"));
        }

        for (int adverts = 0; adverts < advertLinks.size(); adverts++) {
            advertLinks = driver.findElements(By.xpath("//li[@class='cl-image-column']/a"));
            //ilanı açıyoruz
            driver.get(advertLinks.get(adverts).getAttribute("href"));

            /****
             *****ilana ait bilgileri getiriyoruz
             ****/

            //Kategorileri JSON olarak kaydediyoruz
            JSONObject advertJson = new JSONObject();
            JSONArray advertCategoriesJsonArray = new JSONArray();

            //ilan categorileri
            List<WebElement> breadCrumbCategory = driver.findElements(By.xpath("//div[@class='classifiedBreadCrumb']/ol/li/a"));
            for (int i = 0; i < breadCrumbCategory.size()-4; i++) {
                advertCategoriesJsonArray.put(breadCrumbCategory.get(i).getText());
            }
            advertJson.put("Categories", advertCategoriesJsonArray);
            //other data
            advertJson.put("Title",driver.findElement(By.xpath("//div[@class='classifiedDetailTitle']/h1")).getText());
            advertJson.put("Price",driver.findElement(By.xpath("//div[@class='classifiedInfo ']/h3")).getText().trim());
            advertJson.put(driver.findElement(By.xpath("//h3[@class='uiDetailTitle']/a")).getText(),
                    driver.findElement(By.xpath("//div[@class='uiBoxContainer']/p")).getText());

            List<WebElement> propertiesValue = driver.findElements(By.xpath("//div[@class='classifiedInfo ']/ul/li/span"));
            List<WebElement> propertiesKey = driver.findElements(By.xpath("//div[@class='classifiedInfo ']/ul/li/strong"));
            for (int i = 0; i < propertiesKey.size(); i++) {
                advertJson.put(propertiesKey.get(i).getText(),propertiesValue.get(i).getText());
            }

            /**
             * İlana ait resimleri listeleyip indiriyoruz
             */
            //resmi büyütüyoruz

            WebElement megaPhoto = driver.findElement(By.id("mega-foto"));
            if (megaPhoto.getAttribute("class").equalsIgnoreCase("megaPhotoLink megaPhoto")) {
                megaPhoto.click();
                Thread.sleep(3000);
                //resim sayısı alıyoruz
                String imgCountString = driver.findElement(By.xpath("//div[@class='megaPhotoFooterDesc']/span")).getText();
                //1/3 şeklinde bir sstring. Bize sadece kaç resim olduğu lazım
                int imgCount = Integer.valueOf(imgCountString.substring(imgCountString.indexOf("/") + 1));
                System.out.println("resim sayısı=" + imgCount);
                advertJson.put("Image Count", imgCount);

                //resim linklerini bir JsonArray olarak kaydediyoruz
                JSONArray imgLinksJsonArray = new JSONArray();
                for (int i = 0; i < imgCount; i++) {
                    imgLinksJsonArray.put(driver.findElement(By.xpath("//div[@class='megaPhotoImgContainer']/div/img")).getAttribute("src"));
                    //resim sayısı 2'den küçükse path değişiyor.
                    //Todo: 2 resim varken ve hiç resim yokke, path'in ne olduğunu görmemiz ve ona göre bir koşul yazmamız gerekebiir
                    if (imgCount > 1)
                        driver.findElement(By.xpath("//*[@id=\"megaPhotoBox\"]/div/div[1]/div/div/a[2]")).click();
                    Thread.sleep(2000);
                }
                advertJson.put("Images Links", imgLinksJsonArray);

                /**
                 * Resimleri ilanın klasörüne kaydediyoruz
                 */
                for (int i = 0; i < imgCount; i++) {
                    JSONArray imgUrlsJsonAray = (JSONArray) advertJson.get("Images Links");
                    System.out.println(imgUrlsJsonAray.getString(i));
                    URL imageURL = new URL(imgUrlsJsonAray.getString(i));
                    BufferedImage saveImage = ImageIO.read(imageURL);
                    ImageIO.write(saveImage, "png", new File("ilanlar/" + advertJson.get("İlan No") + "/" + i + ".png"));
                }
            }
            Thread.sleep(5000);

            /**
             *             Json dosyasını kaydediyoruz
             */
            FileWriter writer = new FileWriter("ilanlar/"+advertJson.get("İlan No")+"/"+"data.json");
            writer.write(advertJson.toString());
            writer.close();
            driver.navigate().to(urlIlanlar);
            Thread.sleep(3000);
        }
    }



}
