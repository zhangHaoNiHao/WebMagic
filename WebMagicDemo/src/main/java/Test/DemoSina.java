package Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 篮球新闻
 * @author Mr_Zhang
 *
 */
public class DemoSina implements PageProcessor{
	
	//有的符号需要转译
	//public static final String URL_LIST = "https://voice\\.hupu\\.com/nba/\\d";
    public static final String URL_POST = "https://voice\\.hupu\\.com/nba/[0-9]{7}\\.html";
    
    private Site site = Site
            .me()
            .setDomain("voice.hupu.com")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    public void process(Page page) {
    	//System.out.println("page "+page.toString());
        //判断是否是文章页的链接
        if (page.getUrl().regex(URL_POST).match()) {
        	
        	System.out.println("详情页");
        	//如果是文章页，直接爬取数据 /html/body/div[4]/div[1]/div[1]/h1
        	page.putField("title", page.getHtml().xpath("//div[@class='artical-title']/h1/text()"));
            
            
        } else {
        	//如果不满足文章页的正则，就说明这是一个列表页，
        	//此时要。
        	//如果是列表页，将列表页加入待下载的URL中
        	//page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all());
            //page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            
        	//已经得到要爬取url的list，把他们通过addTargetRequests方法加入到队列中即可。
        	//通过xPath来定位页面列表中文章的url
        	System.out.println("列表页");
        	page.addTargetRequests(
                    page.getHtml().xpath("/html/body/div[3]/div[1]/div[2]/ul/li/div[1]/h4/a/@href").all());
        	// /html/body/div[3]/div[1]/div[2]/ul/li[1]/div[1]/h4/a
        	// /html/body/div[3]/div[1]/div[2]/ul/li[2]/div[1]/h4/a
        	// /html/body/div[3]/div[1]/div[2]/ul/li[3]/div[1]/h4/a
        	// 定位翻页的url
        	/*
            page.addTargetRequests(
                    page.getHtml().xpath("/html/body/div[3]/div[1]/div[3]/a[@class='page-btn-prev']/@href").all());
            */
        }
    }
    
    public Site getSite() {
        return site;
    }
    public static void main(String[] args) {
        Spider.create(new DemoSina()).addUrl("https://voice.hupu.com/nba/")
                .run();
    }

}
