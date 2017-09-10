package edu.ccnu.nlp.Sunny;

import java.io.File;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class test {
	//读<Text>里<P>标签里的内容,读一个文档
    public static String readText(File file) throws Exception{
    	Parser parser=new Parser(file.getAbsolutePath());
    	NodeFilter filter=new TagNameFilter("summary");//过滤出P节点
    	NodeList p_nodes=parser.extractAllNodesThatMatch(filter);
    	System.out.println("p_nodes == "+p_nodes);
        if(p_nodes!=null){
        	StringBuffer sb=new StringBuffer();
        	System.out.println("p_nodes.size == "+p_nodes.size());
//        	System.out.println("s == "+p_nodes.elementAt(0).toPlainTextString());
        	for(int i=0;i<p_nodes.size();i++){
        		String s=p_nodes.elementAt(i).toPlainTextString();//取出P节点中的内容；trim函数时去掉前后空格的
//        		s=s.replaceAll("\n"," ");
        		System.out.println("s == "+s);
        		sb.append(s+"\n");
        	}
        	System.out.println(sb.toString());
        	return sb.toString();
        }
    	return "";
    }
    
    public static void main(String args[])throws Exception{
		String file = "E:/PAPER/新闻与社交媒体协同/DATASET/LCSTS/DATA/PART_II.txt";
		File f = new File(file);
		readText(f);
	}
}
