package com.biaojiaf.springbootproject.tensorflow;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.tensorflow.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TextTensorflow {

    public static SavedModelBundle bundle = null;

    public static HashMap<String, String> word_set_list_map = null;

    private static final Integer ONE = 1;

    private static String text_cnn_model_path = "C:\\Users\\Administrator\\Desktop\\TextClassifyAPI\\textcnn_model";

    private static String word_set_list_file_name = "word_set_list.txt";

    private static String model_path = "classpath:textcnn_model";

    private static Session tfSession = null;

    private static Operation operationPredict = null;

    private static String tfGraphPath = "inference/softmax/predictions1";

    private static Output output = null;

    private static String max_id = "3895";

    private static Float dropout_keep_prob_val = 0.51f;

    private static JiebaSegmenter segmenter = null;


    public void init_tf() {
        bundle = SavedModelBundle.load(model_path,"serve");

        tfSession = bundle.session();

        operationPredict = bundle.graph().operation(tfGraphPath);

        output = new Output(operationPredict,0);

        segmenter = new JiebaSegmenter();


    }

    public void init_word_set_list(){
        //Map<String, Integer> word_set_list_map = new HashMap<String, Integer>();
        word_set_list_map = new HashMap<String, String>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(text_cnn_model_path + "\\" + word_set_list_file_name)),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if(lineTxt.equalsIgnoreCase(""))
                    continue;
                String[] word_sets = lineTxt.split(":");
                String key = word_sets[0];
                String val = word_sets[1];
                //int val = Integer.valueOf(word_sets[1]);
                word_set_list_map.put(key, val);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }

    public String word2id(String word){

        Iterator iter = word_set_list_map.entrySet().iterator();
        String word_id = null;
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();

            String  word_tag = "'" + word + "'";

            if(key.equalsIgnoreCase(word_tag))
                word_id = val;
        }

        if(word_id==null)
            word_id = max_id;

        return word_id;
    }



    /* （非 Javadoc）
     * @see com.ccb.ai.api.ITextClassify#doClassify(java.lang.String)
     */

    public void doClassify(String sentence) throws Exception{
        // TODO 自动生成的方法存根

        //执行分词、ID转换

        String seg_sentence = segmenter.sentenceProcess(sentence).toString();
        Object[] zj = segmenter.sentenceProcess(sentence).toArray();
        String[] seg_sentence_list = new String[zj.length];
        String[] seg_sentence_list_id = new String[zj.length];
        for(int i=0; i<zj.length; i++){
            seg_sentence_list[i] =zj[i].toString();
            //执行词到词表ID转换
            seg_sentence_list_id[i] = word2id(seg_sentence_list[i]);
        }

        int[][] a = new int[1][1500];
        for(int i=0; i < 1500; i++){
            if(i < zj.length)
                a[0][i] = Integer.valueOf(seg_sentence_list_id[i]);
            else
                a[0][i] = 0;

        }


        //执行预测
        Tensor input_x = Tensor.create(a);
        Tensor dropout_keep_prob = Tensor.create(dropout_keep_prob_val);
        List<Tensor<?>> out = tfSession.runner().feed("fiction:0",input_x).feed("dropout_keep_prob:0",dropout_keep_prob).fetch(output).run();

        for (Tensor s : out) {
            float[][] t = new float[1][5];
            s.copyTo(t);
            for (float i : t[0])
                System.out.println(i);
        }

        return;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        // TODO 自动生成的方法存根
        TextTensorflow textcnn = new TextTensorflow();

        textcnn.init_tf();

        textcnn.init_word_set_list();

        textcnn.doClassify("出发，炸掉主席纪念馆");
        //textcnn.doClassify("夫妇乐园阿里布达年代记");
        //textcnn.doClassify("滚，台湾建国日中国国民党万岁");
        //textcnn.doClassify("我还没回家呢，你丫傻逼清华大雪操你妈");
        //textcnn.doClassify("你真傻，强奸人类");
        //textcnn.doClassify("强奸万岁");
        //textcnn.doClassify("炸掉天安门");
        //textcnn.doClassify("美国万岁");
        //textcnn.doClassify("美国万岁");
        //textcnn.doClassify("明天是礼拜一");

        return;

    }

}
