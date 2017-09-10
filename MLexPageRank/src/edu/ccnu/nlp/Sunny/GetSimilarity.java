package edu.ccnu.nlp.Sunny;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetSimilarity
{
	/*
	 * 计算两个字符串(英文字符)的相似度，简单的余弦计算，未添权重
	 */
     public static double getSimilarDegree(String str1, String str2,Map<String, int[]> vectorSpace)
     {    	   	 
    	 //以空格为分隔符，分解字符串
    	 String strArray[] = str1.split(" ");
    	 for(int i=0; i<strArray.length; ++i)
    	 {
    		 if(vectorSpace.containsKey(strArray[i]))
    			 ++(vectorSpace.get(strArray[i])[0]);    		 	    		
    		 else    		
    			 continue;    		 
    	 }
    	 
    	 strArray = str2.split(" ");
    	 for(int i=0; i<strArray.length; ++i)
    	 {
    		 if(vectorSpace.containsKey(strArray[i]))
    			 ++(vectorSpace.get(strArray[i])[1]);
    		 else
    		     continue;
    	 }  
    	 
    	 //计算相似度
    	 double vector1Modulo = 0.00;//向量1的模
    	 double vector2Modulo = 0.00;//向量2的模
    	 double vectorProduct = 0.00; //向量积
    	 Iterator iter = vectorSpace.entrySet().iterator();
    	 
    	 while(iter.hasNext())
    	 {
    		 Map.Entry entry = (Map.Entry)iter.next();
    		 int[] itemCountArray = (int[])entry.getValue();    		 
    		 vector1Modulo += itemCountArray[0]*itemCountArray[0];
    		 vector2Modulo += itemCountArray[1]*itemCountArray[1];   		 
    		 vectorProduct += itemCountArray[0]*itemCountArray[1];
    	 }
    	 
    	 vector1Modulo = Math.sqrt(vector1Modulo);
    	 vector2Modulo = Math.sqrt(vector2Modulo);
    	 
    	 //返回相似度
		return (vectorProduct/(vector1Modulo*vector2Modulo));
     }
      
     public static double[][] getAllSentenceSim(List<String> list) throws IOException{
    	Map<String, int[]> vectorSpace = new HashMap<String, int[]>();
    	GetAllWord getAllWord = new GetAllWord();
    	Set wordset = getAllWord.getAllWord(list).keySet();
    	int[] itemCountArray = new int[2];
    	Iterator it = wordset.iterator();
    	while(it.hasNext()){
    		String word = (String) it.next();
            itemCountArray[0] = 0;
            itemCountArray[1] = 0;
        	vectorSpace.put(word, itemCountArray);
     	}
    	
 		double[][] sim = new double[list.size()][list.size()];
 		double[][] normalSim = new double[list.size()][list.size()];
 		double simsum = 0.0;
 		int i,j;
 		for(i=0;i<list.size();i++)
 			for(j=0;j<list.size();j++)
 			{
 				String[] str = new String[list.size()];
 				list.toArray(str);
 				if(i!=j){
 					sim[i][j] = getSimilarDegree(str[i],str[j],vectorSpace);
 					simsum += sim[i][j];
 				}
 			}
 		/*
 		 * 归一化*/
 		for(i=0;i<list.size();i++)
 			for(j=0;j<list.size();j++)
 	        	normalSim[i][j] = sim[i][j] / simsum;
 		return normalSim;
 		
 	}
     
}
