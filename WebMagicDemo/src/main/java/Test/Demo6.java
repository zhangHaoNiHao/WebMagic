package Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 花瓣网抽取器。<br>
 * 使用Selenium做页面动态渲染。<br>
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午4:08 <br>
 * "C:/Users/Mr_Zhang/AppData/Local/Google/Chrome/Application/chromedriver"
 */
public class Demo6 implements PageProcessor {

    private Site site;

    
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("http://huaban\\.com/.*").all());
        if (page.getUrl().toString().contains("pins")) {
            page.putField("img", page.getHtml().xpath("//div[@class='image-holder']/a/img/@src").toString());
        } else {
            page.getResultItems().setSkip(true);
        }
    }

    
    public Site getSite() {
        if (null == site) {
            site = Site.me().setDomain("huaban.com").setSleepTime(0);
        }
        return site;
    }
//undefined
    public static void main(String[] args) {
        Spider.create(new Demo6()).thread(5)
                .addPipeline(new FilePipeline("/data/webmagic/test/"))
                .setDownloader(new SeleniumDownloader("C:/Users/Mr_Zhang/AppData/Local/Google/Chrome/Application/chromedriver.exe"))
                .addUrl("http://huaban.com/")
                .runAsync();
    }
}