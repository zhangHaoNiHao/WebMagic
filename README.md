# WebMagic学习

## 遇到的问题  
1. Log4j错误
	![](./blog_images/1.png '描述')  

	解决：在src目录下添加配置文件 log4j.properties   
	    

		log4j.rootLogger=INFO, stdout, file
		log4j.logger.org.quartz=WARN, stdout
		log4j.appender.stdout=org.apache.log4j.ConsoleAppender
		log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
		log4j.appender.stdout.layout.ConversionPattern=%d{MM-dd HH:mm:ss}[%p]%m%n
		
		log4j.appender.file=org.apache.log4j.FileAppender
		log4j.appender.file.File=C:\\log4j\\webmagic\\webmagic.log
		log4j.appender.file.layout=org.apache.log4j.PatternLayout
		log4j.appender.file.layout.ConversionPattern=%n%-d{MM-dd HH:mm:ss}-%C.%M()%n[%p]%m%n  
		
2. 协议错误，有的网站需要的SSL协议比较高，尽量使用做高版本的jar包
	![](./blog_images/2.png '描述')
	 

		<dependency>
		    <groupId>us.codecraft</groupId>
		    <artifactId>webmagic-core</artifactId>
		    <version>0.7.3</version>
		</dependency>
		<dependency>
		    <groupId>us.codecraft</groupId>
		    <artifactId>webmagic-extension</artifactId>
		    <version>0.7.3</version>
		</dependency>

3. 状态码错误
	![](./blog_images/3.png '描述')  
	解决：主要是与site有关，下边两种解决办法暂时还没弄明白
	
		
		private Site site = Site
			.me()
			.setRetryTimes(3)
			.setSleepTime(3000)
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
		或者  
		private Site site = Site
			.me()
			.setRetryTimes(3)
			.setSleepTime(3000)
			.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
4. 缺少HttpContext类
	解决：添加HTTPContext的Jar包
	
		<dependency>
		    <groupId>org.apache.httpcomponents</groupId>
		    <artifactId>httpclient</artifactId>
		    <version>4.5.4</version>
		</dependency>  

## Webmagic学习配置  

1. 创建一个maven项目
2. 在pom中引用jar包，引用完之后就基本好了

		<dependencies>
		  	<dependency>
			    <groupId>us.codecraft</groupId>
			    <artifactId>webmagic-core</artifactId>
			    <version>0.7.3</version>
			</dependency>
			<dependency>
			    <groupId>us.codecraft</groupId>
			    <artifactId>webmagic-extension</artifactId>
			    <version>0.7.3</version>
			</dependency>
			<dependency>
			    <groupId>org.apache.httpcomponents</groupId>
			    <artifactId>httpclient</artifactId>
			    <version>4.5.4</version>
			</dependency>
	  	</dependencies>
## Webmagic学习参考
1. 参考博客：

		参考博客：https://www.xttblog.com/?s=webmagic   
		其中有些案例，我没有运行成功，我会将我自己的案例发布上去(src/File目录下为正式爬取的案例，src/Test下的程序为学习过程中找到的案例，也有自己测试的案例)
		
		动态页面爬取参考 http://webmagic.io/docs/zh/posts/chx-cases/js-render-page.html
		
		Webmagic的使用说明 http://webmagic.io/docs/zh/

## 案例
1. #### 案例1 汽车之家游记爬取 (src/File/Test2) 
	### 如何判断页面数据为通过js动态获取的？？？

	#### 判断页面是否为js渲染的方式比较简单，在浏览器中直接查看源码（Windows下Ctrl+U，Mac下command+alt+u），如果找不到有效的信息，则基本可以肯定为js渲染。
	  
	![](./blog_images/q1.png '描述') 
	
	1. Windows中按Ctrl+U,再按Ctrl+F查找其中的内容，我查找的是第一个游记，结果没有找到，这就判定该页面的数据是通过js动态获取的。
	![](./blog_images/q2.png '描述') 

	2. 如果判定该页面的数据是通过JS动态获取的，就开始找获取数据的链接：例如：此时只有这几个请求，当点击第二页的时候，又出现了几个链接。  
	![](./blog_images/q4.png '汽车之家') 
	![](./blog_images/q5.png '汽车之家')

	3. 点击新出现的第一个链接，选择preview，出现返回的数据，从中找到详情页的链接。也可以直接复制这个链接，在新的标签页打开(https://you.autohome.com.cn/summary/getsearchresultlist?ps=20&pg=1&type=3&tagCode=&tagName=&sortType=3）	按照上边，同样的点击第三页也同样出现了一个新的请求链接https://you.autohome.com.cn/summary/getsearchresultlist?ps=20&pg=2&type=3&tagCode=&tagName=&sortType=3）	比较这两个链接，发现这两个链接只是pg这个参数不同，根据这两个链接一个是第一页，一个是第二页，可以推测出每个列表页的链接
	![](./blog_images/q6.png '汽车之家')

	4. 如果在新的标签页打开（我的谷歌浏览器安装了一个JsonView插件，查看json格式的数据比较方便）
	![](./blog_images/q7.png '汽车之家')
	
	5. 这里就有详情页的地址，我们再看一下这个详情页的具体地址，链接后边的那个暂时不知道是什么用，大概只是一个标识作用，删去也可以访问，这样就可以根据上边的/details/112251拼接详情页的地址，这样知道了列表页的链接和详情的链接，就可以将这上面的游记都爬取下来了	
	![](./blog_images/q8.png '汽车之家')	
	![](./blog_images/q9.png '汽车之家')	
	
	6. 当初我也就这样试了试，结果能爬下来，也就没管。但是后来查看这些游记，发现有的游记都只有一半，并不全，后来我又看网页，发现有的网页是只有一页（比如上边那个游记），有的网页有好几页（https://you.autohome.com.cn/details/112486#pvareaid=2174234），但时候后边附带着一个扩展全文的按钮，这时候就需要寻找全文的链接，否则爬取的只是一半的内容
	![](./blog_images/q10.png '汽车之家')
	
	
	7. 接下来就是寻找完整页面的链接，点击展开全文按钮（https://you.autohome.com.cn/details/112486/b51ba31c58c7b84e0a52d8f50b811df4?handleType=1）连续找了几个这样的游记之后，发现只是后边的（b51ba31c58c7b84e0a52d8f50b811df4）这个参数不同，这里就需要找这个随机数了。重新刷新一下页面，出现了几个链接，一次点进去看看有没有这个随机数，终于在最后一个链接中找到了这个随机数，现在就需要拼接这个链接获取这个随机数。获取这个随机数后就开始拼接这个完整页面的链接。后来突然发现，该如何判断只有一页的页面，但是后来发现即使是一个页面也有随机数，这样访问同样也可以。
	![](./blog_images/q11.png '汽车之家')
	![](./blog_images/q12.png '汽车之家')

		单页面游记的随机数
	![](./blog_images/q13.png '汽车之家')

	获取这个随机数之后，就可以拼接这个完整页面的链接，然后将链接加入带爬取队列。然后就跟正常的爬取一样了
	
-----

1. #### 案例2 马蜂窝旅游游记爬取（能爬取数据，但是数据爬不全）(src/File/Test0)
	1.	马蜂窝的爬取比较简单，只是最简单的列表页-详情页格式，只是在翻页的时候，页面只有5页，再看看每个列表页的格式(https://www.mafengwo.cn/yj/21536/1-0-2.html) 只是最后的数不同，应该就是代表页数。通过循环拼接链接，并将链接加入带爬取队列  
	![](./blog_images/4.png '描述')   

	2. 但是这里有一个问题还没有解决：马蜂窝的游记都比较长，为了快速加载，每个游记都是分成几次加载，比如刚打开页面的时候，只是显示上边的一部分，当下滑到最下边的时候，就会JS请求继续获取下边的页面
	![](./blog_images/5.png '描述') 

	3. 当随着下滑，会加载出剩下的页面数据
	![](./blog_images/6.png '描述')

	类似的，随着游记的长度不同，会有不同的加载次数，按照一般过程，都会要拼接加载的链接，然后获取数据，追加到同一个文件中，但是通过观察这几个链接，发现他们都有一个随机数，并且这个随机数并没有在页面中找到，导致现在爬取的数据只有一部分

-----
1. #### 案例3 携程旅游游记爬取(src/File/Test1_1)  

	1. 携程旅游也是一个动态获取的页面，只不过他的动态获取只是翻页的时候的列表页是动态获取的，具体的详情页是正常的。所以只要获取翻页的链接就跟正常的列表-详情页的爬取是一样的了。
	![](./blog_images/x1.png '描述')

	2. 这个网站没有具体的翻页按钮，每次向下滑动页面，就会动态加载下一页的游记列表
	![](./blog_images/x2.png '描述')
	
	3. 这样就找到了翻页的链接	(http://you.ctrip.com/TravelSite/Home/IndexTravelListHtml?p=2&Idea=0&Type=100&Plate=0)，	根据上边的链接发现只有那个p属性不同，应该是是代表页数。拼接这个链接，加入带爬取队列就可以了

