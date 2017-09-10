package edu.ccnu.nlp.Sunny;

import java.io.File;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class test {
	//��<Text>��<P>��ǩ�������,��һ���ĵ�
    public static String readText(File file) throws Exception{
    	Parser parser=new Parser(file.getAbsolutePath());
    	NodeFilter filter=new TagNameFilter("summary");//���˳�P�ڵ�
    	NodeList p_nodes=parser.extractAllNodesThatMatch(filter);
    	System.out.println("p_nodes == "+p_nodes);
        if(p_nodes!=null){
        	StringBuffer sb=new StringBuffer();
        	System.out.println("p_nodes.size == "+p_nodes.size());
//        	System.out.println("s == "+p_nodes.elementAt(0).toPlainTextString());
        	for(int i=0;i<p_nodes.size();i++){
        		String s=p_nodes.elementAt(i).toPlainTextString();//ȡ��P�ڵ��е����ݣ�trim����ʱȥ��ǰ��ո��
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
		String file = "E:/PAPER/�������罻ý��Эͬ/DATASET/LCSTS/DATA/PART_II.txt";
		File f = new File(file);
		readText(f);
	}
}
