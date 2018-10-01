package Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 携程旅游游记爬取
 */
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
/**
 * 携程旅游 游记爬取
 * @author Mr_Zhang
 *
 */
public class Test1 implements PageProcessor{
	static int a = 0;
	//有的符号需要转译
	// http://you.ctrip.com/TravelSite/Home/IndexTravelListHtml?p=13&Idea=0&Type=100&Plate=0
	public static final String URL_LIST = "http://you\\.ctrip\\.com/TravelSite/Home/IndexTravelListHtml\\?p=\\d{1,3}&Idea=0&Type=100&Plate=0";
    public static final String URL_POST = "http://you\\.ctrip\\.com/travels/\\w+/[0-9]{7}\\.html";
    public static final String URL_LIST1 = "http://you\\.ctrip\\.com/travels/";
    
    private Site site = Site
            .me()
            .setDomain("you.ctrip.com")
            .setSleepTime(3000)
            .setUserAgent(
            		"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
    public void process(Page page) {
    
        //判断是否是文章页的链接
        if (page.getUrl().regex(URL_POST).match()) {
        	System.out.println("详情页");//   
        	//如果是文章页，直接爬取数据 
        	a++;            									//ctd_head_con cf   ctd_content
        	page.putField("title1 爬取"+a, page.getHtml().xpath("//*[@class='ctd_head_con cf']/h1/text()"));
            
        }else if(page.getUrl().regex(URL_LIST).match()){
        	System.out.println("JS 获得的页面");
        	
        	page.addTargetRequests(
                    page.getHtml().xpath("/html/body/div/a/@href").all());
        }
        else if(page.getUrl().regex(URL_LIST1).match()){
        	//通过xPath来定位页面列表中文章的url
        	System.out.println("列表页");
        	//找到列表页中的详情页地址，并加入待爬取队列
        	page.addTargetRequests(
                    page.getHtml().xpath("//*[@id='photo-fall']/div/div[2]/a[2]/@href").all());
            // 定位翻页的url，并将其加入带爬取页面
        	List<String> list = new ArrayList<String>();
        	for(int i=0;i<20;i++){
        		list.add("http://you.ctrip.com/TravelSite/Home/IndexTravelListHtml?p="+i+"&Idea=0&Type=100&Plate=0");
        	}
        	page.addTargetRequests(list);
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
        Spider.create(new Test1()).addUrl("http://you.ctrip.com/travels/")
        .thread(5)
        .run();
        System.out.println("一共爬取了"+a+"条数据");
    }

}
