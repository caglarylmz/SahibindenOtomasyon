import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IlanOlustur {
    private ChromeDriver driver;
    private NgWebDriver ngDriver;
    static  String jsonFilePath = "ilanlar/";
    static String urlIlanlar ="https://banaozel.sahibinden.com/ilanlarim";
    private JSONObject advertJson;

    public IlanOlustur(ChromeDriver driver, NgWebDriver ngDriver) throws InterruptedException {
        this.driver = driver;
        this.ngDriver = ngDriver;
        //this.advertJson = getJsonFile();

        //Click "Yeni İlan"
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/header/div/ul/li[6]/a")).click();
        Thread.sleep(3000);
       // List<String> kategoriSecimListesi = getBreadCrumbCategories();
        getAdvertFolderNames();
        //getAdvertFoldersFileNames();
    }


    public List<String> getBreadCrumbCategories(){
        List<WebElement> kategoriSecimiElements = driver.findElements(ByAngular.repeater("enumValue in kategoriSecimiElements.enumValues"));

        List<String> kategoriSecimiListesi = new ArrayList<String>();

        for (int i = 0; i <kategoriSecimiElements.size() ; i++) {
            kategoriSecimiListesi.add(kategoriSecimiElements.get(i).getText());
        }


        return kategoriSecimiListesi;
    }

    public JSONObject getJsonFile(String jsonFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(jsonFilePath));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line=br.readLine())!=null){
            sb.append(line + "\n");

        }

        JSONObject advertJson = new JSONObject(sb.toString());
        return advertJson;
    }

    public  Map<String ,List<String>> getAdvertFolderNames(){
        File adverts = new File("ilanlar");
        File[] advertFolders = adverts.listFiles();
        List<String > advertFolderNames = new ArrayList<String>();
        List<String>  advertFolderPathes = new ArrayList<String>();
        Map<String ,List<String>> map = new HashMap<String, List<String>>();

        for (int i = 0; i <advertFolders.length ; i++) {
            advertFolderPathes.add(advertFolders[i].getPath());
            System.out.println(advertFolders[i].getPath());

            File[] advertFolderFiles = advertFolders[i].listFiles();
            List<String> advertFilePathes = new ArrayList<String>();

            for (int j = 0; j <advertFolderFiles.length ; j++) {
                advertFilePathes.add(advertFolderFiles[j].getPath());
                System.out.println(advertFilePathes.get(j));
            }
            advertFolderNames.add(advertFolderPathes.get(i).substring(advertFolderPathes.get(i).indexOf("\\")+1));
            System.out.println(advertFolderNames.get(i));
            map.put(advertFolderNames.get(i),advertFilePathes);


        }

        System.out.println(map.get("707880025").get(0));


        return map;
    }






    //Todo: İlanlar yeniden açılacak
    //Todo: İlanlar klasör altındaki dosyaları listele
    //Todo: json aç ve resimlerin path'ini listele
    //Todo: ngWebDriver araştır angular için


}
