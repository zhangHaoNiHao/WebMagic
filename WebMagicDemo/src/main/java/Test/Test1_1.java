package Test;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 携程旅游游记爬取
 * @author Mr_Zhang
 *
 */
public class Test1_1 implements PageProcessor {
	
	static int a = 0;
	//有的符号需要转译
	// http://you.ctrip.com/TravelSite/Home/IndexTravelListHtml?p=13&Idea=0&Type=100&Plate=0
	public static final String URL_LIST = "http://you\\.ctrip\\.com/TravelSite/Home/IndexTravelListHtml\\?p=\\d{1,3}&Idea=0&Type=100&Plate=0";
    public static final String URL_POST = "http://you\\.ctrip\\.com/travels/\\w+/[0-9]{7}\\.html";
    public static final String URL_LIST1 = "http://you\\.ctrip\\.com/travels";
    
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
        	String title = "";
        	title = page.getHtml().xpath("//*[@class='ctd_head_con cf']/h1/text()").toString();
        	if(title == null){
        		title = page.getHtml().xpath("//*[@class='ctd_head_left']/h2/text()").toString();
        	}
        	page.putField("title1 爬取"+a, title);
            
        }else if(page.getUrl().regex(URL_LIST).match()){
        	page.addTargetRequests(
                    page.getHtml().xpath("/html/body/div/a/@href").all());
        }
        
        else{
        	System.out.println("列表页");
        	page.addTargetRequests(
                    page.getHtml().xpath("//*[@id='photo-fall']/div/div[2]/a[2]/@href").all());
        	
        	List<String> list = new ArrayList<String>();
        	for(int i=0;i<20;i++){
        		list.add("http://you.ctrip.com/TravelSite/Home/IndexTravelListHtml?p="+i+"&Idea=0&Type=100&Plate=0");
        	}
        	page.addTargetRequests(list);
        	
        	
        	
        	/*//如果是js列表页面
        	if(page.getUrl().regex(URL_LIST).match()){
        		
        	}else{//如果是普通列表页面
        		page.addTargetRequests(
                        page.getHtml().xpath("//*[@id='photo-fall']/div/div[2]/a[2]/@href").all());
        	}
        	*/
        	
        	
        }
    }
    
    public Site getSite() {
        return site;
    }
    public static void main(String[] args) {
        Spider.create(new Test1_1()).addUrl("http://you.ctrip.com/travels/")
        .thread(5)
        .run();
        System.out.println("一共爬取了"+a+"条数据");
    }

}
