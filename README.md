# WebMagic
Wbmagic--Java爬虫框架学习

## 这几天正在整理学习过程 待更新

# WebMagic学习

### 遇到的问题  
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
	
		

2. 状态码错误

