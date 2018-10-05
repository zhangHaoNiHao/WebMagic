package File;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
/**
 * 汽车之家 游记爬取
 * @author Mr_Zhang
 * 之前汽车之家的游记详情页面没有考虑是否是整个文章
 * 后来发现爬取得页面只是开头一部分，才发现还有一个扩展全文的按钮，点开这个按钮之后才是全文
 * 但是点开这个按钮，发现链接中有一串随机数字，然后找了半天终于在一个json数据中找到了，继续拼接链接
 * 多加了一个寻找详情页随机数的过程，最终还是将完整的详情页的地址找到了
 * 
 * 运行过程可能会报错，因为有的链接是广告，它的地址格式与详情页不同，不过没影响
 */
public class Test2 implements PageProcessor {
	static int a = 0;
	String path="D:/data/qiche";
	//完整页面的地址 https://you.autohome.com.cn/details/111947/fbd5670102140e5eb30c8e5cd7ce15ef?handleType=1
	public static final String URL_POST = "https://you\\.autohome\\.com\\.cn/details/[0-9]{5,10}/\\w+\\?handleType=1";
	
	// 获得随机数的地址 https://you.autohome.com.cn/details/getmenuinfo?tripId=111947
	public static final String URL_POST1 = "https://you\\.autohome\\.com\\.cn/details/getmenuinfo\\?tripId=[0-9]{5,10}";
	
	// 获得列表页的地址
	// https://you.autohome.com.cn/summary/getsearchresultlist?ps=20&pg=3&type=3&tagCode=&tagName=&sortType=3
	// https://you.autohome.com.cn/summary/getsearchresultlist?ps=20&pg=4&type=3&tagCode=&tagName=&sortType=3
	public static final String URL_LIST = "https://you\\.autohome\\.com\\.cn/summary/getsearchresultlist\\?ps=20&pg=\\d{1,4}&type=3&tagCode=&tagName=&sortType=3";
	
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
    	//判断待爬地址是否是详情页的地址
        if (page.getUrl().regex(URL_POST).match()) {
			System.out.println("完整详情页 ");
			a++;
			//如果是详情页， /html/body/div[7]/div[1]/div/div/div/div/span[2]
			page.putField("titles 爬取了 "+a,  // /html/body/div[7]/div[1]/div/div/div/div/span[2]
			        page.getHtml().xpath("/html/body/div[7]/div[1]/div/div/div/div/span[2]/text()").toString());
			String title = page.getHtml().xpath("/html/body/div[7]/div[1]/div/div/div/div/span[2]/text()").toString();
			if(title == null){
				title = page.getHtml().xpath("/html/body/div[7]/div[1]/div/div/div/div/span/text()").toString();
				if(title == null) {
					title = ""+a;
				}
			}
			String content = page.getHtml().xpath("//*[@class='main-container-left fl']//tidyText()").toString();
			
			//将文章存到本地文件中
			String filename = title;
			
			File f1 = new File(path+"/"+filename+".txt"); 
			if (!f1.exists()) {
				try {
					f1.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
					FileWriter fw = new FileWriter(f1,false);
				    BufferedWriter bw = new BufferedWriter(fw);
				    bw.write(content);
				    bw.flush();  
				    bw.close();
				    fw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
        }
        else if(page.getUrl().regex(URL_POST1).match()){
        	//如果是获取完整页面的随机数的值
        	List<String> urls = new JsonPathSelector("$.result[*].id").selectList(page.getRawText());
        	
        	String sui = urls.get(0);
        	System.out.println("每个页面随机数是 "+sui);
        	//拼接完整的详情页链接
        	String jsonPath = page.getUrl().toString();
        	String shu = jsonPath.split("=")[1]; 
        	// https://you.autohome.com.cn/details/112358/267e8d9437a9dbdcc1274e4c74f07827?handleType=1
        	String url = "https://you.autohome.com.cn/details/"+shu+"/"+sui+"?handleType=1";
        	
        	//将详情页加入带爬取队列
        	page.addTargetRequest(url);
        	
        }
        else if(page.getUrl().regex(URL_LIST).match()){
        	
        	//如果是翻页的链接，找到其中详情页的id,并拼接寻找完整详情页的随机值的地址
        	
        	List<String> list = new ArrayList<String>();
        	List<String> list2 = new ArrayList<String>();
        	
        	//参考博客：http://webmagic.io/docs/zh/posts/chx-cases/js-render-page.html
        	List<String> urls = new JsonPathSelector("$.result.hitlist[*].url").selectList(page.getRawText());
        	System.out.println("地址 "+urls.toString());
        	
        	if (CollectionUtils.isNotEmpty(urls)) {
        		
                for (String url : urls) {
                	System.out.println(url);
                	String tripId = url.split("/")[2];
                	System.out.println("游记id "+tripId);
                	//拼接成获取完整详情页随机数的链接
                	// https://you.autohome.com.cn/details/getmenuinfo?tripId=112358 
                	list2.add("https://you.autohome.com.cn/details/getmenuinfo?tripId="+tripId);
                }
            }
        	//将获取详情页随机数的链接加载到带爬取页面
            page.addTargetRequests(list2);
        }
        else {
        	System.out.println("列表页");
        	//定位列表中的详情页地址 /html/body/div[3]/div[1]/div[2]/ul/li/div[1]/h4/a/@href
        	/*
        	page.addTargetRequests(
                    page.getHtml().xpath("//*[@id='app']/div[2]/div[2]/div[2]/div[2]/div/div/a/@href").all());
            */
        	
        	// 拼接列表页的链接地址
        	//目前一共有2800页，一直循环到2800
        	List<String> list3 = new ArrayList<String>();
        	for(int i=0;i<5;i++){
        		list3.add("https://you.autohome.com.cn/summary/getsearchresultlist?ps=20&pg="+i+"&type=3&tagCode=&tagName=&sortType=3"); 
        	}
        	//将列表页地址加载到带爬取队列
            page.addTargetRequests(list3);
            
        }
    }

    public static void main(String[] args) {
        Spider.create(new Test2()).addUrl("https://you.autohome.com.cn/index/searchkeyword")
                .addPipeline(new ConsolePipeline())
                .thread(5)
                .run();
    }
}
