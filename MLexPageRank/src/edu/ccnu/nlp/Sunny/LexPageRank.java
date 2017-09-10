package edu.ccnu.nlp.Sunny;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LexPageRank {
    public int sumNum = 0;
    public double[][] linkOrNot;//��ž��Ӽ��Ƿ�����
    public int[] C;//����趨��ֵ�󣬺;���i֮��ΪlinkOrNotֵΪ1�ľ�������
    
    public void Summarize(String args[]) throws Exception
    {
    	List<String> list=new ArrayList<String>();
    	/* Read files */
    	/*���ĵ��ļ�����*/
    	/*if (args[3].equals("1"))
        {
    		String[] single_file = new String[1];
            single_file[0] = args[0];            
            //myDoc.readfile(single_file, " ", args[2], args[6]);
        }*/
    	/*���ĵ��ļ�����*/
       if (args[3].equals("2"))
        {
    		File myfile = new File(args[0]);         
            list = MFileReader.getAllSentences(myfile);           
        }
    	/* Calculate sentences' similarity matrix and construct link or not matrix depending on the similarity matrix */
    	/*������ӵ����ƾ��󣬸������ƾ�����ȷ������֮���Ƿ�Ҫ����*/
    	double[][] sim = new double[list.size()][list.size()];
    	sim = GetSimilarity.getAllSentenceSim(list);
    	System.out.println("sim == "+sim.length);
    	/*�����������ƾ�����趨����ֵ��ȷ���Ƿ�Ҫ����*/
    	C = new int[list.size()];
    	linkOrNot = new double[list.size()][list.size()];
    	/*�趨��ֵ */
    	double linkThresh = 0.1;
    	if (Double.parseDouble(args[5])>=0){
    		linkThresh = Double.parseDouble(args[5]);
        }
    	/*�����������ƾ������ֵ��ȷ����������֮���Ƿ�������*/
    	for (int i = 0; i < list.size(); ++i) {
    		C[i] = 0;
    		for(int j = 0; j < list.size(); ++j) {
    			if (j == i) linkOrNot[i][j] = 0;
    			else if(100 * sim[i][j] >= linkThresh) {
    				C[i]++;
    				linkOrNot[i][j] = sim[i][j];
    			}else {
    				linkOrNot[i][j] = 0;
    			}
    		}
    	}
    	
    	/* Calculate the lexPageRank score of sentences */
    	/*�������lexPageRank��score*/
		double[] u_old = new double[list.size()];
		double[] u = new double[list.size()];
		for(int i = 0; i < list.size(); ++i) {
			u_old[i] = 1;
			u[i] = 1;
		}		
		double eps = 0.00001, alpha = 0.85 , minus = 1.0;
		while (minus > eps) {
			u_old = u;
            /* u[i]:���LexPageRankֵ
             * ��ʽ��PR(i) = (1-d) + d(PR(Ti)/C(Ti)+......+PR(Tn)/C(Tn))*/
			for (int i = 0; i < list.size(); ++i) {
				double nowSum = 0.0;
			    for(int j = 0; j < list.size(); ++j) {
			    	if(linkOrNot[i][j] == 1) {
			    		nowSum = nowSum + u_old[j] / (1.0 * C[j]);
			    	}
			    }
			    u[i] = (1 - alpha) + alpha * nowSum;
			}		
			minus = 0.0;
			for (int i = 0; i < list.size(); i++) {
				double add = java.lang.Math.abs(u[i] - u_old[i]);
				minus += add;
			}
		}
    	
		int[] sen_len = new int[list.size()];
		sen_len = MFileReader.sentenceLen(list);
		/* Set redundancy removal method and parameter */
		double threshold = 0.7, Beta = 0.1;
		
		if (Double.parseDouble(args[7])>=0){
			threshold = Double.parseDouble(args[7]);
        }
		
		if (Double.parseDouble(args[6])>=0){
			Beta = Double.parseDouble(args[6]);
        }				
				
		/* Remove redundancy and get the abstract
		 * ʹ��MMR��ȥ���� */		
		MMR myDoc = new MMR();
		int maxlen = Integer.parseInt(args[4]);
		myDoc.pick_sentence_MMR(list.size(),sim,sen_len,u, threshold, Beta,maxlen);    
		
    	/* Output the abstract */  	 		
		String s = new String();
		s = args[1]+".txt";
    	File outfile = new File(s);
    	//File outfile = new File(args[1]);
    	System.out.println("outfile = "+outfile);
    	if(!outfile.exists()){
    		outfile.createNewFile();   		
    	}    	
        BufferedWriter bf = new BufferedWriter(new PrintWriter(outfile));    
       
        for (int i : myDoc.summary_id){        	
            bf.append(list.get(i));
            bf.append("\n");
        }
        bf.close();
    }
}
