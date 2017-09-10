package edu.ccnu.nlp.Sunny;
import java.io.File;

public class Main {
	
	public static void main(String args[])throws Exception{
		/* Params and default value, you can modify them */
		String type = "2", abNum = "1000";
		String beta = "0", threshold = "0.85";
		String linkThresh = "0.1";
		String[] arg;
		
		String file = "E:\\DATA\\SUNNY_LexPageRankData&Result\\duc2004_testdocs";
		String outFile = "E:\\DATA\\SUNNY_LexPageRankData&Result\\duc2004Result";
		File dir = new File(file);
		System.out.println("dir = "+dir);
		System.out.println("dir list = "+dir.listFiles());
		File[] files = dir.listFiles();
			
		File outDir = new File(outFile);
		if(!outDir.exists()){
			boolean file_true = outDir.mkdir(); 
			if (!file_true) {
				System.out.println("No valid dir!");
			}
		}
		
		System.out.println("file = "+files);
		for(File fOrd : files) {
			if (fOrd.getName().equals(".DS_Store")) {
				continue;
			}
			System.out.println("fOrd=="+fOrd);
			
			LexPageRank lexpagerank = new LexPageRank();
			arg = new String[13];
			/*输入文件路径file + / + 文件名*/
			arg[0] = file + System.getProperty("file.separator") + fOrd.getName();
			/*摘要输出路径*/
			//arg[1] = String.valueOf(temp);
			arg[1] = outFile + System.getProperty("file.separator") + fOrd.getName();
			/*摘要的语言 1: Chinese, 2: English*/
			//arg[2] = language;
			/*摘要类型 1: single-document summarization, 2: multi-document summarization,*/
			arg[3] = type;						
			/*The expected number of words in summary.*/
			arg[4] = abNum;
			//args[5]是存放阈值
			arg[5] = linkThresh;
			arg[6] = beta;
			arg[7] = threshold;		
 			lexpagerank.Summarize(arg);
			
		}
		
	}
}

