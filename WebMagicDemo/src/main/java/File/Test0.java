package File;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
public class Test0 implements PageProcessor {
	static int a = 0;
	String path = "D:/data";
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
            
            //将文章存到本地文件中
            String filename = page.getHtml().xpath("//*[@id='_j_cover_box']/div[3]/div[2]/div/h1/text()").toString();
            File f1 = new File(path+"/"+filename+".txt"); 
    		if (!f1.exists()) {
    			try {
					f1.createNewFile();
					
					FileWriter writer = null;
					writer = new FileWriter(f1,true);
					System.out.println("文本 "+page.getHtml().xpath("//*[@class='view clearfix']/tidyText()").toString());
					writer.append(page.getHtml().xpath("//*[@class='view clearfix']/tidyText()").toString());
				    writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 创建目标文件
            } 
        	
        } else {
        	System.out.println("列表页开始");
        	
        	//定位列表中的详情页地址 
        	page.addTargetRequests(
                    page.getHtml().xpath("//*[@class='post-list']/ul/li/div/a/@href").all());
        	
        	System.out.println("URL "+page.getHtml().xpath("//*[@class='post-list']/ul/li/div/a/@href"));
        	
        	//System.out.println("翻页 "+page.getHtml().xpath("//*[@class='page-hotel']").toString());
        	System.out.println("列表页结束");
        	
        	List<String> list = new ArrayList<String>();
        	for(int i=2;i<10;i++){
        		list.add("/yj/21536/1-0-"+i+".html");
        	}
        	//将翻页的地址放入待爬取页面
        	page.addTargetRequests(list);
        }
    }

    public static void main(String[] args) {
        Spider.create(new Test0()).addUrl("http://www.mafengwo.cn/yj/21536")
                .addPipeline(new ConsolePipeline()).thread(5).run();
    }
}
