package Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式练习
 * @author Mr_Zhang
 *
 */

public class Regular {

	public static void main(String[] args) {
		
		//匹配
		check();
		checkTel();
		splitDemo();
		splitDemo1();
		replaceAllDemo();
		replaceAllDemo1();
		getDemo();
	}
	
	/*
	 * 匹配：用规则匹配整个字符串，
	 * 只要有一处不符合规则，就匹配结束，返回false
	 */
	public static void check(){
		String qq = "123456a789";
		String reg = "[1-9]\\d{4,14}";//是否全都是数字
		boolean b = qq.matches(reg);
		System.out.println(b);
	}
	
	public static void checkTel()
    {
        String tel = "16900001111";
        String telReg = "1[358]\\d{9}";
        System.out.println(tel.matches(telReg));
    }
	
	/**
	 * 
	 */
	public static void splitDemo(){
		String str = "avg   bb   geig   glsd   abc";
        String reg = " +";//按照多个空格来进行切割
        String[] arr = str.split(reg);  
        System.out.println(arr.length);
        for(String s : arr)
        {
            System.out.println(s);
        }
	}
	public static void splitDemo1()
    {

	    String str = "erkktyqqquizzzzzo";
	    String reg ="(.)\\1+";//按照叠词来进行切割
	        //可以将规则封装成一个组。用()完成。组的出现都有编号。
	        //从1开始。 想要使用已有的组可以通过  \n(n就是组的编号)的形式来获取。
	    String[] arr = str.split(reg);  
	    System.out.println(arr.length);
	    for(String s : arr)
	    {
	        System.out.println(s);
	    }
    }
	/**
	 * 替换:将字符串中符合规则的字符替换成指定字符
	 */
	public static void replaceAllDemo()
    {
    
        String str = "wer1389980000ty1234564uiod234345675f";//将字符串中的数字替换成#。
 
        str = str.replaceAll("\\d{5,}","#");

        System.out.println(str);
    }
	public static void replaceAllDemo1()
    {
        String str = "erkktyqqquizzzzzo";//将叠词替换成$.  //将重叠的字符替换成单个字母。zzzz->z
        str = str.replaceAll("(.)\\1+","$1");
        
        System.out.println(str);
    }
	
	public static void getDemo(){
		String str = "yin yu shi wo zui cai de yu yan";
		System.out.println(str);
		String reg = "\\b[a-z]{3}\\b";//匹配只有三个字母的单词
		//将规则封装成对象。
		Pattern p = Pattern.compile(reg);
		//让正则对象和要作用的字符串相关联。获取匹配器对象。
		Matcher m  = p.matcher(str); 
		while(m.find())
        {
            System.out.println(m.group());
            System.out.println(m.start()+"...."+m.end());
                // start()  字符的开始下标（包含）
                //end()  字符的结束下标（不包含）
        }
	}
}
