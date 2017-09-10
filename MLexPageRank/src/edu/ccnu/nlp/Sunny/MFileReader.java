package edu.ccnu.nlp.Sunny;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class MFileReader {
	
	//读<Text>里<P>标签里的内容,读一个文档
    public static String readText(File file) throws Exception{
    	Parser parser=new Parser(file.getAbsolutePath());
    	NodeFilter filter=new TagNameFilter("summary");//过滤出P节点
    	NodeList p_nodes=parser.extractAllNodesThatMatch(filter);
    	System.out.println("p_nodes == "+p_nodes);
        if(p_nodes!=null){
        	StringBuffer sb=new StringBuffer();
        	System.out.println("p_nodes.size == "+p_nodes.size());
        	for(int i=0;i<p_nodes.size();i++){
        		String s=p_nodes.elementAt(i).toPlainTextString().trim();//取出P节点中的内容；trim函数时去掉前后空格的
        		s=s.replaceAll("\n"," ");
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
  //把一个文档中的句子返回到List中
    public static List<String> fileSentences(File f) throws Exception{
     String s= readText(f);
   	 String[] text=s.split("[.?!]");
   	 List<String> list=new ArrayList<String>();
   	 for(String c:text){
   		 list.add(c);
   	 }
   	 
   	 return list;
    }
    
    //把一个同主题下的文件夹中的所有句子返回到List中
    public static List<String> getAllSentences(File dir) throws Exception{
   	 File[] files=dir.listFiles();
   	 List<String> list=new ArrayList<String>();
    	 for(File f:files){
   		 List<String> file_sentences=fileSentences(f);
   		 list.addAll(file_sentences);
   	 }
    	 return list;
    }
    
    public static int[] sentenceLen(List<String> list){
    	int[] sen_len = new int[list.size()];
    	for(int i = 0;i<list.size();i++){
    		sen_len[i] = list.get(i).length();
    	}
		return sen_len;
    	
    }
}


