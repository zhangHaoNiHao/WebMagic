package Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Demo6_1 {

	public static void main(String[] args) {
        // 第一步： 设置chromedriver地址。一定要指定驱动的位置。
        System.setProperty("webdriver.chrome.driver",
        		"C:/Users/Mr_Zhang/AppData/Local/Google/Chrome/Application/chromedriver.exe");
        // 第二步：初始化驱动
        WebDriver driver = new ChromeDriver();
        // 第三步：获取目标网页
        driver.get("https://www.mafengwo.cn/yj/21536/");
        // 第四步：解析。以下就可以进行解了。使用webMagic、jsoup等进行必要的解析。
        System.out.println("Page title is: " + driver.getTitle());
        //System.out.println("Page title is: " + driver.getPageSource());
        System.out.println("CurrentUrl : " + driver.getCurrentUrl());
    }
}
