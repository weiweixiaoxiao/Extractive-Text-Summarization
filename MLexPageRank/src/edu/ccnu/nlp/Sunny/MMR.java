package edu.ccnu.nlp.Sunny;

import java.util.ArrayList;
import java.util.HashMap;


//some basic information about doc
public class MMR {
   // public int maxlen = 1000;//the maxlen of the summary
    // public String outfile;
    ArrayList<Integer> summary_id = new ArrayList<>();
    HashMap<String, Integer> dic = new HashMap<String, Integer>();
    HashMap<Integer, String> dd= new HashMap<>();
    
/*
 * num：句子数量
 * sim[][]：句子相似度
 * senlen：句子长度
 * score:句子lexPageRank打分
 * para
 * beta*/
    ArrayList<Integer> pick_sentence_MMR(int num, double[][] sim,int[] sen_len, double[] score,double para, double beta,int maxlen)
    {
    	   	
        summary_id=new ArrayList<>();
        int len = 0;
        if (para<0) para=0.7;
        boolean[] chosen = new boolean[num];
        for (int i=0;i<num;i++)
            chosen[i]=false;
        while ( len < maxlen)
        {
            double maxscore = 0;
            int pick = -1;
            for (int i=0;i<num;i++){
              double tmpscore = score[i];
              for (int j : summary_id)
                if (score[i] - sim[i][j] * score[j] *para< tmpscore)
                    tmpscore =  score[i] - sim[i][j] * score[j] *para;

              if (tmpscore/Math.pow(sen_len[i],beta)>maxscore && !chosen[i] && len+sen_len[i]<maxlen && sen_len[i]>=5){
                    maxscore = tmpscore/Math.pow(sen_len[i],beta);
                    pick = i;
                }
            }
            if (pick==-1)
                break;
            chosen[pick]=true;
            len += sen_len[pick];
            System.out.println("MMR'S pick == "+pick);
            summary_id.add(pick);
            if (len>=maxlen-20)
                break;
        }
        for(int i = 0;i<summary_id.size();i++)
        System.out.println("doc.summary_id == "+summary_id.get(i));
        return summary_id;
    }

   
}