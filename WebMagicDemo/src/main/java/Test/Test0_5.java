package Test;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 马蜂窝旅游游记爬取
 * @author Mr_Zhang
 *
 */
public class Test0_5 implements PageProcessor {
	static int a = 0;
	public static final String URL_POST = "http://www\\.mafengwo\\.cn/i/[0-9]{6,8}\\.html";
	//public static final String URL_LIST = "https://www\\.cnblogs\\.com/justcooooode/\\d";
	
	private Site site = Site
			.me()
			.setRetryTimes(3)
			.setSleepTime(3000)
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
			;

    public Site getSite() {
        return site;
    }

    public void process(Page page) {
    	//判断列表页中的详情页的地址，过滤正则表达式
        if (page.getUrl().regex(URL_POST).match()) {
            System.out.println("详情页 ");
            //如果是详情页，获取页面信息
            a++;
            page.putField("title 爬取了"+a,
                    page.getHtml().xpath("//*[@id='_j_cover_box']/div[3]/div[2]/div/h1/text()").toString());
        
        	} else {
        	System.out.println("列表页开始");
        	
        	//定位列表中的详情页地址 
        	page.addTargetRequests(
                    page.getHtml().xpath("//*[@class='post-list']/ul/li/div/a/@href").all());
        	
        	System.out.println("URL "+page.getHtml().xpath("//*[@class='post-list']/ul/li/div/a/@href"));
        	//System.out.println(page.getHtml().toString());
        	
        	//System.out.println("翻页 "+page.getHtml().xpath("//*[@class='page-hotel']").toString());
        	System.out.println("列表页结束");
        	
        	List<String> list = new ArrayList<String>();
        	for(int i=2;i<10;i++){
        		list.add("/yj/21536/1-0-"+i+".html");
        	}
        	//定位翻页的url
        	page.addTargetRequests(list);
        }
    }

    public static void main(String[] args) {
        Spider.create(new Test0_5()).addUrl("http://www.mafengwo.cn/yj/21536")
                .addPipeline(new ConsolePipeline()).thread(5).run();
    }
}
