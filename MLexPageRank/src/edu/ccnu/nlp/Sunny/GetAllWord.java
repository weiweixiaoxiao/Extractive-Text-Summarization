package edu.ccnu.nlp.Sunny;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * ��ȡ�����ĵ���ȥ��stopword�����е�word
 * */
public class GetAllWord {
	Map<String,Integer> stopword=new HashMap<String,Integer>();
	/*
	 * ��ȡstopword�ļ�
	 * */
	 public void readStopwords() throws IOException
	    {	
		    String stopwordPath = new String();
		    stopwordPath = "D:\\eclipse-jee-luna-SR1-win32-x86_64\\eclipse\\workspaces\\ MLexPageRank\\data\\1.txt";
			File tmpfile =new File(stopwordPath);
			if (!tmpfile.exists()){
				System.out.println("stopwordPath = "+stopwordPath);
				System.out.println("stopwords file does not exist!");
				System.exit(0);
			}
	    	FileReader inFReader=new FileReader(stopwordPath);
	        BufferedReader inBReader=new BufferedReader(inFReader);
	        String tmpWord;
	        int i=0;
	        while((tmpWord=inBReader.readLine())!=null)
	        {
	            i++;
	            stopword.put(tmpWord, i);
	        }
	        inBReader.close();
	    }
	public boolean ifStopwords(String tmpWord) throws IOException
    {
		readStopwords();	
        if (stopword.get(tmpWord.toLowerCase())!=null) 
        	return true;
        return false;
    }
	
	/*
	 * �������list��list��ÿһ���һ�����ӣ�����list����������ĵ��еľ���
	 * HashMap<String,Integer>��������ĵ��е������ֵ����е��ʣ�Integer��ŵ����ǵڼ�����
	 * */
	public Map<String,Integer> getAllWord(List list) throws IOException{
		Map<String,Integer> wordlist=new HashMap<String,Integer>();
		int num = 0;
		for(int i=0;i<list.size();i++){
	    	String strArray[] = ((String) list.get(i)).split(" ");
	    	for(int j=0;j<strArray.length;j++){
	    		if(!ifStopwords(strArray[j])){	    		
    		    	if(!wordlist.containsKey(strArray[j])){
	    		    		num++;
		    				wordlist.put(strArray[j], num);
	    		    }	    				    			
	    		}
	    		else{
	    			continue;
	    		}
	    	}
		}
		return wordlist;
		}
}
