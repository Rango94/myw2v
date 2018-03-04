package Trainer;

import Huffman.Huffman;
import Modelhandler.Model;
import Modelhandler.*;
import myFile.corpusReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trainer {
    static Huffman hm;
    corpusReader cr;
    Model md;
    int windos;
    boolean weatherfill;
    double step=0.01;
    int maxloop=10000;
    HashMap<String,Integer> dictionary;

    public Trainer(Model md,int windos,boolean weatherfill,double step){
        this.weatherfill=weatherfill;
        this.windos=windos;
        this.md=md;
        this.step=step;
        this.hm=md.hm;
        this.dictionary=hm.dictionary;
        cr=new corpusReader(md.PATH,windos,weatherfill,md.hm.lowwords);
    }

    public Model train(){
        for(int i=0;i<maxloop;i++){
            trainline();
            if(i%20==0) {
                step=step*(1-(double)i/maxloop);
                System.out.println(md.getVector("mother"));
                System.out.println(md.getVector("father"));
                System.out.println(md.dis("mother","father"));
                System.out.println(md.getVector("king"));
                System.out.println(md.getVector("queen"));
                System.out.println(md.dis("king","queen"));
                System.out.println(md.dis("mother","queen"));
                System.out.println("training has completed "+Double.toString((double)i/maxloop*100)+"%");
                System.out.println("---------------------------------");
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
        HashMap<List<String>, Vector> corpusline = cr.handlesent(md);
        if (corpusline!=null) {
            for (List<String> e : corpusline.keySet()) {
                int termcont=dictionary.get(e.get(windos));
                double t=(double)termcont/hm.totalnum;
                if(t>0.0005){
                    if(Math.random()<1-Math.pow((0.0005/t),0.5)){
//                        System.out.println("跳过单词:"+e.get(windos)+"\t"+"频数为:"+Integer.toString(termcont));
                        continue;
                    }
                }
                Vector inputvector = corpusline.get(e);
                byte[] Huffmanofterm = hm.getHuffmancode(e.get(windos));
                if(Huffmanofterm==null){
                    System.out.println(e.get(windos));
                }
                if(Huffmanofterm!=null) {
                    List<String> pathlist = generatepath(Huffmanofterm);
                    Vector addofinput = new Vector(inputvector.getSize(), 0);
                    int path=0;
                    for (String subpath : pathlist) {
                        Vector pathvector = hm.getVectorofnotleafbyHuffman(subpath);
//                        System.out.println("参数向量为："+pathvector);
                        double q = active(Vector.mult(pathvector, inputvector));
//                        System.out.println("激活函数参数："+Double.toString(Vector.mult(pathvector, inputvector)));
//                        System.out.println("激活函数："+Double.toString(q));
                        double g = step * (1 - Huffmanofterm[path] - q);
                        path++;
//                        System.out.println("g等于："+Double.toString(g)+"\t学习率为："+Double.toString(step));
                        addofinput = Vector.adds(addofinput, Vector.mult(g, pathvector));
//                        System.out.println("term增量为："+addofinput+"\n");

//                        hm.setVectorofnotleafbyHuffman(subpath, Vector.adds(Vector.mult(g, inputvector), pathvector));
                        Vector tmp=Vector.mult(g, inputvector);
                        for(int i=0;i<pathvector.getSize();i++) {
                            pathvector.vector[i]=pathvector.vector[i]+tmp.vector[i];
                        }
//                        if(Math.random()>0.95) {
//                            System.out.println("参数向量增量为：" + Vector.mult(g, inputvector));
//                        }
                    }
                    for (int i = 0; i < e.size(); i++) {
                        if (i != windos) {
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
                }
            }
        }
        else{
            System.out.println("语料库为空");
        }
    }

    private double active(double x){
        return 1/(1+Math.pow(Math.E,-x));
    }
//    生成路径序列以便训练
    private List<String> generatepath(byte[] path){
        List<String> pathlist=new ArrayList<String>();
        pathlist.add("-1");
        String tmp="";
        for(int i=0;i<path.length-1;i++){
            tmp+=Byte.toString(path[i]);
            pathlist.add(tmp);
        }
        return pathlist;
    }
}
