package Trainer;

import Huffman.Huffman;
import Modelhandler.Model;
import Modelhandler.*;
import myFile.corpusReader;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trainer {
    private static Huffman hm;
    private corpusReader cr;
    private Model md;
    private int Window;
    private boolean weatherfill;
    private float step=0.025f;
    private float Step=step;
    private int maxloop=10000;
    private HashMap<String,Integer> dictionary;
    private static HashMap<Float,Float> Sigmoid=new HashMap<Float,Float>();



    long t1=0;
     long t2=0;
     long t3=0;
     long t4=0;



    public Trainer(Model md,int windos,boolean weatherfill,float step){
        this.weatherfill=weatherfill;
        this.Window=windos;
        this.md=md;
        this.step=step;
        this.Step=step;
        this.hm=md.hm;
        this.dictionary=hm.dictionary;
        cr=new corpusReader(md.PATH,windos,weatherfill,md.hm.lowwords);
        intsigmoid();
    }



    public Model train(){
        float k=0.1f;
        for(int i=0;i<maxloop;i++){
            trainline();
            if(i%20==0) {
                step=Step*(1-(float)i/maxloop);
                System.out.println("获取非叶子节点向量的时间："+Long.toString(t1));
                System.out.println("sigmoid函数的时间："+Long.toString(t2));
                System.out.println("更新非叶子节点向量的时间："+Long.toString(t3));
                System.out.println("更新单词向量的时间："+Long.toString(t4));
                System.out.println(md.hm.getNodevector(new byte[]{1,0,1}));
                System.out.println(md.getVector("mother"));
                System.out.println(md.getVector("father"));
                System.out.println(md.dis("mother","father"));
                System.out.println(md.getVector("king"));
                System.out.println(md.getVector("queen"));
                System.out.println(md.dis("king","queen"));
                System.out.println(md.dis("mother","queen"));
                System.out.println("training has completed "+Float.toString((float)i/maxloop*100)+"%");
                System.out.println("---------------------------------");
            }
            if((float)i/maxloop>k){
                k+=0.1;
                md.Savemodel("E:\\myw2v\\first_tmp.model");
            }
            if(i%5==0){
//                System.out.println(hm.getVectorofnotleafbyHuffman("-1"));
//                System.out.println(hm.getVectorofnotleafbyHuffman("111111"));
//                System.out.println(hm.getVectorofnotleafbyHuffman("1111111"));
//                System.out.println(hm.getVectorofnotleafbyHuffman("11111111"));
//                System.out.println(hm.getVectorofnotleafbyHuffman("111111111"));
//                System.out.println(hm.getVectorofnotleafbyHuffman("1111111111"));
//                System.out.println("----------------------------");
            }
        }
        return md;
    }
//  根据一行语料训练
    public void trainline(){
        int window=1+(int)(Math.random()*(Window-1));
        HashMap<List<String>, Vector> corpusline = cr.handlesent(md,window);
        if (corpusline!=null) {
            for (List<String> e : corpusline.keySet()) {
                int termcont=dictionary.get(e.get(window));
                float t=(float)termcont/hm.totalnum;
                if(t>0.0005){
                    if(Math.random()<1-Math.pow((0.0005/t),0.5)){
//                        System.out.println("跳过单词:"+e.get(windos)+"\t"+"频数为:"+Integer.toString(termcont));
                        continue;
                    }
                }
                Vector inputvector = corpusline.get(e);
                byte[] Huffmanofterm = hm.getHuffmancode(e.get(window));
                if(Huffmanofterm==null){
                    System.out.println(e.get(window));
                }
                if(Huffmanofterm!=null) {
                    List<byte[]> pathlist = generatepath(Huffmanofterm);
                    Vector addofinput = new Vector(inputvector.getSize(), 0);
                    int path=0;
                    for (byte[] subpath : pathlist) {
                        int flag=Math.random()>0.9999?1:1;
//                        获取目标非叶子节点向量的时间
                        long t1_tmp=System.currentTimeMillis();
                        Vector pathvector = hm.getNodevector(subpath);
                        t1+=System.currentTimeMillis()-t1_tmp;


                        if(flag==0) {
                        System.out.println("参数向量为："+pathvector);
                        }


//                        sigmoid函数的时间
                        long t2_tmp=System.currentTimeMillis();
                        float q = active(Vector.mult(pathvector, inputvector));
                        t2+=System.currentTimeMillis()-t2_tmp;


                        if(flag==0) {
                        System.out.println("激活函数参数："+Float.toString(Vector.mult(pathvector, inputvector)));
                        System.out.println("激活函数："+Float.toString(q));
                        }
                        float g = step * (1 - Huffmanofterm[path] - q);
                        path++;
                        if(flag==0) {
                        System.out.println("g等于："+Float.toString(g)+"\t学习率为："+Float.toString(step));
                        }


                        addofinput = Vector.adds(addofinput, Vector.mult(g, pathvector));
                        if(flag==0) {
                        System.out.println("term增量为："+addofinput);
//                        hm.setVectorofnotleafbyHuffman(subpath, Vector.adds(Vector.mult(g, inputvector), pathvector));
                        }
                        Vector tmp=Vector.mult(g, inputvector);

                        //更新非叶子节点向量
                        long t3_tmp=System.currentTimeMillis();
                        for(int i=0;i<pathvector.getSize();i++) {
                            pathvector.vector[i]=pathvector.vector[i]+tmp.vector[i];
                        }
                        t3+=System.currentTimeMillis()-t3_tmp;

                      if(flag==0) {
                            System.out.print("参数向量增量为：" + Vector.mult(g, inputvector)+"\n"+"\n");
                       }
                    }

//                    更新单词向量
                    long t4_tmp=System.currentTimeMillis();
                    for (int i = 0; i < e.size(); i++) {
                        if (i != window) {
                            try {
                                Vector tmp = md.getVector(e.get(i));
                                if (tmp != null) {
//                                    md.setVector(e.get(i), Vector.adds(addofinput, tmp));
                                    for(int k=0;k<tmp.getSize();k++){
                                        tmp.vector[k]=tmp.vector[k]+addofinput.vector[k];
                                    }
                                }else{
                                    System.out.println("发现单词"+e.get(i)+"没有对应向量");
                                }
                            } catch (NullPointerException E) {
                                E.fillInStackTrace();
                            }
                        }
                    }
                    t4+=System.currentTimeMillis()-t4_tmp;
                }
            }
        }
        else{
            System.out.println("语料库为空");
        }
    }

    private float active(float x){
        int x_=(int)x*100;
        int x__=x_%10;
        if(x__>=5){
            Sigmoid.get((float)(x_/10+1)/10);
        }
        if(x__<5){
            Sigmoid.get((x_/10+0.5f)/10);
        }
        if(x<-6){
            return 0;
        }
        else{
            return 1;
        }
    }

    private float sigmoid(float x){
        return (float) (1/(1+Math.pow(Math.E,-x)));
    }

    private void intsigmoid(){
        for(double i=-6;i<6;i+=0.05){
            Sigmoid.put((float) i,sigmoid((float)i));
        }
    }


//    生成路径序列以便训练
    private List<byte[]> generatepath(byte[] path){
        List<byte[]> pathlist=new ArrayList<byte[]>();
        pathlist.add(new byte[]{-1});
        List<Byte> tmpb=new ArrayList<Byte>();
        for(int i=0;i<path.length-1;i++){
            tmpb.add(path[i]);
            byte[] tmp=new byte[tmpb.size()];
            for(int j=0;j<tmp.length;j++){
                tmp[j]=tmpb.get(j);
            }
            pathlist.add(tmp);
        }
        return pathlist;
    }
}
