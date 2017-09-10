package edu.ccnu.Sunny;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


//some basic information about doc
public class doc {
    public ArrayList<ArrayList<String>> sen = new ArrayList<ArrayList<String>>();//单词的集合，都为小写
    public ArrayList<ArrayList<String>> stemmerSen = new ArrayList<ArrayList<String>>();
    public int[] l_range;//the begin of the i'th document
    public int[] r_range;//the end of the i'th document
    public ArrayList<String> original_sen = new ArrayList<String>();//the original sentence，文档中最原始的句子集合。
    public ArrayList<Integer> sen_len = new ArrayList<>();//the length of original sentence
    public ArrayList<Integer> word_len = new ArrayList<>();// the length of the vector//一句话所包含的word的个数
    public ArrayList<TreeSet<Integer>> vector = new ArrayList<TreeSet<Integer>>();//记录每个句子中词出现的顺序，给每个词标记了一个数字。[最先出现的为0，然后12345...往后标记。要是句子中出现和之前相同的词就标记为之前出现的数字。]
    public ArrayList<ArrayList<Integer>> s_tf = new ArrayList<>();// the tf-vector of the sentence，每个数表示一个句子里每个词出现的次数
    public ArrayList<Integer> d_tf;//一个文档中每个单词的词频
    public TreeSet<Integer> d_vector;//一个文档中每个单词的vector
    public int total_len;
    public int fnum, snum = 0, wnum;//fnum-document num ;wnum-word num; snum-sentence num，文档中所有句子的个数
    public int[] tf;//词频
    public int[] df;//标记单词是否存在?
    public double[] idf;
    public double[][] sim, normalSim;
    public int maxlen = 30;//the maxlen of the summary
    // public String outfile;
    ArrayList<Integer> summary_id = new ArrayList<>();
    HashMap<String, Integer> dic = new HashMap<String, Integer>();
    HashMap<Integer, String> dd= new HashMap<>();
    
    //read file from the documents
    public void readfile(String[] rfiles,String filepath,String language, String stopwordPath) throws IOException {
        int i = 0;
        l_range = new int[rfiles.length];//左长度
        r_range = new int[rfiles.length];//右长度
        fnum = 0;
        total_len = snum;//句子数量
       
        //如果是多文档摘要，那么rfiles里就有很多文档，对这些文档一个个处理
        for (String infile : rfiles) { 
        	
        	if (infile.equals(".DS_Store")) {
				System.out.println("Skiping!!");
				continue;
			}
        	fnum++;//document中包含文档数量
            String path;//存放一个文档的绝对路径
            //如果单文档摘要
            if (!filepath.equals(" ")) {
            	//多文档中的document下的其中一个文档的绝对路径
                path = filepath + System.getProperty("file.separator") + infile;
            }
            else{
                path = infile;
            }
            //将单个文档中的内容读取出来。
            Tokenizer mytoken = new Tokenizer();
            ArrayList<String> tmp = new ArrayList<>();
            if (language.equals("1"))//1 represent Chinese
                tmp = mytoken.tokenize_Chn(path,stopwordPath);
            else
            if (language.equals("2"))//2 represent English
                tmp = mytoken.tokenize_Eng(path, stopwordPath);
			else
			if (language.equals("3"))
				tmp = mytoken.tokenize_Eng(path, stopwordPath);
            int len = tmp.size();
            l_range[i] = total_len;
            total_len += len;
            r_range[i] = total_len;
            
            i++;
            sen.addAll(mytoken.word);//单词的集合           
            stemmerSen.addAll(mytoken.stemmerWord);//stemmerWord的集合
            sen_len.addAll(mytoken.senLen);//           
            original_sen.addAll(tmp);//所有句子的集合，存放在一个数组里
        
        }
        snum = original_sen.size();//句子的数量
       
    }
    

    // op 1 represent tf-isf; 2 and 3 represent tf-idf
    // stemOrNot 1 represent no stemmer; 2 represent stemmer
    void calc_tfidf(int op, int stemOrNot) {
        int i = 0,wlen = 0;
        wnum = 0;
        dic = new HashMap<String, Integer>();
        d_tf = new ArrayList<>();
        d_vector = new TreeSet<>();
        int[] all_tf = new int [100000];
        Arrays.fill(all_tf,0);
        word_len = new ArrayList<>();
        int dnum = 0;
        tf = new int[100000];
        df = new int[100000];
        boolean[] occur = new boolean[10000];
        
        ArrayList<ArrayList<String>> calTfIdfVec = new ArrayList<ArrayList<String>>();
        if(stemOrNot == 1) {
        	calTfIdfVec = sen;
        }else {
        	calTfIdfVec = stemmerSen;
        }
        
        for (ArrayList<String> tmpSen : calTfIdfVec) {
            wlen=0;
            TreeSet<Integer> tmpSet = new TreeSet<Integer>();
            Arrays.fill(tf,0);
            if (op == 2 || op == 3) {
                if (i == r_range[dnum]){
                    dnum++;
                    Arrays.fill(occur,false);
                }
            }else
                Arrays.fill(occur,false);
            for (String tmpWord : tmpSen) {
                wlen++;
                if (dic.get(tmpWord) != null) {
                    int k = dic.get(tmpWord);
                    tmpSet.add(k);
                    tf[k]++;//词频
                    all_tf[k]++;
                    if (!occur[k]) {
                        occur[k] = true;
                        df[k]++;//第几个词出现了
                    }                

                } else {
                    dic.put(tmpWord, wnum);
                    dd.put(wnum,tmpWord);
                    tf[wnum]++;
                    all_tf[wnum]++;
                    df[wnum]++;
                    tmpSet.add(wnum);
                    occur[wnum] = true;
                                      
                    wnum++;
                }
               
            }
            
            word_len.add(wlen);//一句话所包含的word的多少
            ArrayList<Integer> tmp_tf=new ArrayList<>();
            for (int j:tmpSet)
            {
                tmp_tf.add(tf[j]);
            }
            s_tf.add(tmp_tf);//tf-vector，每个数表示一个句子里每个词出现的次数
            vector.add(tmpSet);//记录每个句子中词出现的顺序，给每个词标记了一个数字。[最先出现的为0，然后12345...往后标记。要是句子中出现和之前相同的词就标记为之前出现的数字。           
            i++;
        }
        idf = new double[wnum];
        if (op == 2 || op == 3){
            for (i=0;i<wnum;i++)
            {
                idf[i]= Math.log((double)(1+fnum)/df[i]);
     
            }
        }else
        {
            for (i=0;i<wnum;i++)
            {
                idf[i]= Math.log((double)(1+snum)/df[i]);
                
                   }
        }
        for (i=0;i<wnum;i++){
            if (all_tf[i]!=0)
            {
                d_vector.add(i);//文档的word-vector
                d_tf.add(all_tf[i]);//一个文档中每个单词的词频
            }
      
        }
    }
    
   /*用在cal_sim计算相似度的方法中*/ 
    double calc_cos(TreeSet<Integer> a1 ,ArrayList<Integer> a2,int len_a ,TreeSet<Integer> b1, ArrayList<Integer>b2, int len_b)
    {
        int x1 = 0,x2 = 0;
        double l1 = 0,l2 = 0;
        int id_a = 0, id_b = 0;
        double cos = 0;
        TreeSet<Integer> a = new TreeSet<>();
        TreeSet<Integer> b = new TreeSet<>();
        a.addAll(a1);
        b.addAll(b1);
        while (a.size() > 0 && b.size() > 0)
        {

            x1 = a.first();
            x2 = b.first();
            if ( x1 == x2 )
            {
                l1 += Math.pow((double)a2.get(id_a)/(double)len_a*idf[x1],2);
                l2 += Math.pow((double)b2.get(id_b)/(double)len_b*idf[x2],2);
                cos += Math.pow(idf[x1],2)*(double)a2.get(id_a)/(double)len_a*(double)b2.get(id_b)/(double)len_b;
                a.pollFirst();
                id_a++;
                b.pollFirst();
                id_b++;
            }else
            if ( x1 < x2 )
            {
                l1 += Math.pow((double)a2.get(id_a)/(double)len_a*idf[x1],2);
                a.pollFirst();
                id_a++;
            }else
            if ( x1 > x2)
            {
                l2 += Math.pow((double)b2.get(id_b)/(double)len_b*idf[x2],2);
                b.pollFirst();
                id_b++;
            }
        }
        while (a.size() > 0)
        {
            x1 = a.first();
            l1 += Math.pow((double)a2.get(id_a)/(double)len_a*idf[x1],2);
            a.pollFirst();
            id_a++;
        }
        while (b.size() > 0)
        {
            x2 = b.first();
            l2 += Math.pow((double)b2.get(id_b)/(double)len_b*idf[x2],2);
            b.pollFirst();
            id_b++;
        }

        if (l1==0 || l2==0) return 0;
        return cos/Math.pow(l1*l2,0.5) ;
    }
    
    void calc_sim()
    {
        sim = new double[snum][snum];
        normalSim = new double[snum][snum];
        for (int i = 0 ; i < snum; i++){
        	double sumISim = 0.0;
        	for (int j = 0; j < snum; j++)
        	{
        		if (i == j) {
        			sim[i][j] = 1;
        			/*sumISim += sim[i][j];
        			continue;*/
        		}
        		else if (i > j) {
        			sim[i][j] = sim[j][i];
        			/*sumISim += sim[i][j];
        			continue;*/
        		}
        		else{
        			sim[i][j] = calc_cos(vector.get(i), s_tf.get(i),word_len.get(i),vector.get(j), s_tf.get(j),word_len.get(j));
        		}
        		sumISim += sim[i][j];
        	}
        	for(int j = 0; j < snum; ++j) {
        		if(sumISim != 0.0) {
        			//System.out.println(sumISim);
        			normalSim[i][j] = sim[i][j] / sumISim;
        		}
        		else 
        			normalSim[i][j] = 0.0;
        	}
        }
    }
}