package File;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;

/**
 * 测试马蜂窝的动态加载页面
 * @author Mr_Zhang
 *
 */
public class Demo2 implements PageProcessor{

	
	public static final String URL_POST = "http://www\\.mafengwo\\.cn/i/[0-9]{6,8}\\.html";
	
	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site
            .me()
            .setDomain("you.ctrip.com")
            .setSleepTime(3000)
            .setUserAgent(
            		"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
    private static int count =0;


    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        //判断链接是否符合http://www.cnblogs.com/任意个数字字母-/p/7个数字.html格式
        if(page.getUrl().regex(URL_POST).match()){
        	count++;
        	page.putField("title 爬取了"+count,
                    page.getHtml().xpath("//*[@id='_j_cover_box']/div[3]/div[2]/div/h1/text()").toString());
        	String title = page.getHtml().xpath("//*[@id='_j_cover_box']/div[3]/div[2]/div/h1/text()").toString();
        	if(title == null){
        		title = ""+count;
        	}
        	//创建文件
        	System.out.println();
        	
        	
        	//加入满足条件的链接
            page.addTargetRequests(
                    page.getHtml().xpath("//*[@id=\"post_list\"]/div/div[@class='post_item_body']/h3/a/@href").all());
        }else{
            //获取页面需要的内容
            System.out.println("抓取的内容："+
                    page.getHtml().xpath("//*[@id=\"Header1_HeaderTitle\"]/text()").get()
            );
            count ++;
            
            List<String> list = new ArrayList<String>();
            // https://www.mafengwo.cn/note/ajax.php?act=getNoteDetailContentChunk&id=6749363&iid=191827858&seq=193652059&back=0
        	for(int i=0;i<20;i++){
        		list.add("https://www.mafengwo.cn/note/ajax.php?act=getNoteDetailContentChunk&id=6749363&iid=191827858&seq=193652059&back=0");
        	}
        	page.addTargetRequests(list);
        }
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new GithubRepoPageProcessor()).addUrl("https://www.mafengwo.cn/").thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了"+count+"条记录");
    }
}
