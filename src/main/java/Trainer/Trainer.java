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
    private static Huffman hm;
    private corpusReader cr;
    private Model md;
    private int Window;
    private boolean weatherfill;
    private double step=0.025;
    private double Step=step;
    private int maxloop=1000;
    private HashMap<String,Integer> dictionary;
    String sgorcbow=null;
    String hmorneg=null;
//    private List<Double> sigmoid_key=new ArrayList<Double>();
//    private List<Double> sigmoid_value=new ArrayList<Double>();
    private HashMap<Integer,Double> Sigmoid=new HashMap<Integer, Double>();


    long t1=0;
     long t2=0;
     long t3=0;
     long t4=0;



    public Trainer(Model md,int windos,boolean weatherfill,double step){
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



    public Model train(String sgorcbow,String hmorneg){
        double k=0.1;
        if(hmorneg=="Huffman") {
            for (int i = 0; i < maxloop; i++) {
                trainline_hm(sgorcbow);
                if (i % 2 == 0) {
                    step = Step * (1 - (double) i / maxloop);
                    System.out.println("获取非叶子节点向量的时间：" + Long.toString(t1));
                    System.out.println("sigmoid函数的时间：" + Long.toString(t2));
                    System.out.println("更新非叶子节点向量的时间：" + Long.toString(t3));
                    System.out.println("更新单词向量的时间：" + Long.toString(t4));
                    System.out.println(md.hm.getNodevector(new byte[]{1, 0, 1}));
                    System.out.println(md.getVector("mother"));
                    System.out.println(md.getVector("father"));
                    System.out.println(md.dis("mother", "father"));
                    System.out.println(md.getVector("king"));
                    System.out.println(md.getVector("queen"));
                    System.out.println(md.dis("king", "queen"));
                    System.out.println(md.dis("mother", "queen"));
                    System.out.println("training has completed " + Double.toString((double) i / maxloop * 100) + "%");
                    System.out.println("---------------------------------");
                }
                if ((double) i / maxloop > k) {
                    k += 0.1;
                    md.Savemodel("E:\\myw2v\\first_tmp.model");
                }
            }
            return md;
        }
        if(hmorneg=="Neg"){
            for (int i = 0; i < maxloop; i++) {
                trainline_neg();
                if (i % 2 == 0) {
                    step = Step * (1 - (double) i / maxloop);
                    System.out.println("获取非叶子节点向量的时间：" + Long.toString(t1));
                    System.out.println("sigmoid函数的时间：" + Long.toString(t2));
                    System.out.println("更新非叶子节点向量的时间：" + Long.toString(t3));
                    System.out.println("更新单词向量的时间：" + Long.toString(t4));
                    System.out.println(md.hm.getNodevector(new byte[]{1, 0, 1}));
                    System.out.println(md.getVector("mother"));
                    System.out.println(md.getVector("father"));
                    System.out.println(md.dis("mother", "father"));
                    System.out.println(md.getVector("king"));
                    System.out.println(md.getVector("queen"));
                    System.out.println(md.dis("king", "queen"));
                    System.out.println(md.dis("mother", "queen"));
                    System.out.println("training has completed " + Double.toString((double) i / maxloop * 100) + "%");
                    System.out.println("---------------------------------");
                }
                if ((double) i / maxloop > k) {
                    k += 0.1;
                    md.Savemodel("E:\\myw2v\\first_tmp.model");
                }

            }
            return md;
        }
        return md;
    }
//  根据一行语料训练
    public void trainline_hm(String sgorcbow){
        int window=1+(int)(Math.random()*(Window-1));
        if(sgorcbow.equals("cbow")) {
            HashMap<List<String>, Vector> corpusline = cr.handlesent_cbow(md, window);
            if (corpusline != null) {
                for (List<String> e : corpusline.keySet()) {
                    int termcont = dictionary.get(e.get(window));
                    double t = (double) termcont / hm.totalnum;
                    if (t > 0.0005) {
                        if (Math.random() < 1 - Math.pow((0.0005 / t), 0.5)) {
//                        System.out.println("跳过单词:"+e.get(windos)+"\t"+"频数为:"+Integer.toString(termcont));
                            continue;
                        }
                    }
                    Vector inputvector = corpusline.get(e);
                    byte[] Huffmanofterm = hm.getHuffmancode(e.get(window));
                    if (Huffmanofterm == null) {
                        System.out.println(e.get(window));
                    }
                    if (Huffmanofterm != null) {
                        List<byte[]> pathlist = generatepath(Huffmanofterm);
                        Vector addofinput = new Vector(inputvector.getSize(), 0);
                        int path = 0;
                        for (byte[] subpath : pathlist) {
                            int flag = Math.random() > 0.9999 ? 1 : 1;
//                        获取目标非叶子节点向量的时间
                            long t1_tmp = System.currentTimeMillis();
                            Vector pathvector = hm.getNodevector(subpath);
                            t1 += System.currentTimeMillis() - t1_tmp;


                            if (flag == 0) {
                                System.out.println("参数向量为：" + pathvector);
                            }


//                        sigmoid函数的时间
                            long t2_tmp = System.currentTimeMillis();
                            double q = active(Vector.mult(pathvector, inputvector));
                            t2 += System.currentTimeMillis() - t2_tmp;


                            if (flag == 0) {
                                System.out.println("激活函数参数：" + Double.toString(Vector.mult(pathvector, inputvector)));
                                System.out.println("激活函数：" + Double.toString(q));
                            }
                            double g = step * (1 - Huffmanofterm[path] - q);
                            path++;
                            if (flag == 0) {
                                System.out.println("g等于：" + Double.toString(g) + "\t学习率为：" + Double.toString(step));
                            }


                            addofinput = Vector.adds(addofinput, Vector.mult(g, pathvector));
                            if (flag == 0) {
                                System.out.println("term增量为：" + addofinput);
//                        hm.setVectorofnotleafbyHuffman(subpath, Vector.adds(Vector.mult(g, inputvector), pathvector));
                            }
                            Vector tmp = Vector.mult(g, inputvector);

                            //更新非叶子节点向量
                            long t3_tmp = System.currentTimeMillis();
                            for (int i = 0; i < pathvector.getSize(); i++) {
                                pathvector.vector[i] = pathvector.vector[i] + tmp.vector[i];
                            }
                            t3 += System.currentTimeMillis() - t3_tmp;

                            if (flag == 0) {
                                System.out.print("参数向量增量为：" + Vector.mult(g, inputvector) + "\n" + "\n");
                            }
                        }

//                    更新单词向量
                        long t4_tmp = System.currentTimeMillis();
                        for (int i = 0; i < e.size(); i++) {
                            if (i != window) {
                                try {
                                    Vector tmp = md.getVector(e.get(i));
                                    if (tmp != null) {
//                                    md.setVector(e.get(i), Vector.adds(addofinput, tmp));
                                        for (int k = 0; k < tmp.getSize(); k++) {
                                            tmp.vector[k] = tmp.vector[k] + addofinput.vector[k];
                                        }
                                    } else {
                                        System.out.println("发现单词" + e.get(i) + "没有对应向量");
                                    }
                                } catch (NullPointerException E) {
                                    E.fillInStackTrace();
                                }
                            }
                        }
                        t4 += System.currentTimeMillis() - t4_tmp;
                    }
                }
            } else {
                System.out.println("语料库为空");
            }
        }else{

            HashMap<List<String>, Vector> corpusline = cr.handlesent_sg(md, window);
            if (corpusline != null) {
                for (List<String> e : corpusline.keySet()) {
                    int termcont = dictionary.get(e.get(window));
                    double t = (double) termcont / hm.totalnum;
                    if (t > 0.0005) {
                        if (Math.random() < 1 - Math.pow((0.0005 / t), 0.5)) {
//                        System.out.println("跳过单词:"+e.get(windos)+"\t"+"频数为:"+Integer.toString(termcont));
                            continue;
                        }
                    }
                    Vector inputvector = corpusline.get(e);
                    for (int idx = 0; idx < 2 * window; idx++) {
                        if(idx==window){
                            continue;
                        }
                        byte[] Huffmanofterm = hm.getHuffmancode(e.get(idx));
                        if (Huffmanofterm == null) {
                            System.out.println(e.get(idx));
                        }
                        if (Huffmanofterm != null) {
                            List<byte[]> pathlist = generatepath(Huffmanofterm);
                            Vector addofinput = new Vector(inputvector.getSize(), 0);
                            int path = 0;
                            for (byte[] subpath : pathlist) {
                                int flag = Math.random() > 0.9999 ? 1 : 1;
//                        获取目标非叶子节点向量的时间
                                long t1_tmp = System.currentTimeMillis();
                                Vector pathvector = hm.getNodevector(subpath);
                                t1 += System.currentTimeMillis() - t1_tmp;

                                if (flag == 0) {
                                    System.out.println("参数向量为：" + pathvector);
                                }

//                        sigmoid函数的时间
                                long t2_tmp = System.currentTimeMillis();
                                double q = active(Vector.mult(pathvector, inputvector));
                                t2 += System.currentTimeMillis() - t2_tmp;


                                if (flag == 0) {
                                    System.out.println("激活函数参数：" + Double.toString(Vector.mult(pathvector, inputvector)));
                                    System.out.println("激活函数：" + Double.toString(q));
                                }
                                double g = step * (1 - Huffmanofterm[path] - q);
                                path++;
                                if (flag == 0) {
                                    System.out.println("g等于：" + Double.toString(g) + "\t学习率为：" + Double.toString(step));
                                }

                                addofinput = Vector.adds(addofinput, Vector.mult(g, pathvector));
                                if (flag == 0) {
                                    System.out.println("term增量为：" + addofinput);
//                        hm.setVectorofnotleafbyHuffman(subpath, Vector.adds(Vector.mult(g, inputvector), pathvector));
                                }
                                Vector tmp = Vector.mult(g, inputvector);

                                //更新非叶子节点向量
                                long t3_tmp = System.currentTimeMillis();
                                for (int i = 0; i < pathvector.getSize(); i++) {
                                    pathvector.vector[i] = pathvector.vector[i] + tmp.vector[i];
                                }
                                t3 += System.currentTimeMillis() - t3_tmp;

                                if (flag == 0) {
                                    System.out.print("参数向量增量为：" + Vector.mult(g, inputvector) + "\n" + "\n");
                                }
                            }

//                    更新单词向量
                            long t4_tmp = System.currentTimeMillis();
                            try {
                                Vector tmp = md.getVector(e.get(window));
                                if (tmp != null) {
                                    for (int k = 0; k < tmp.getSize(); k++) {
                                        tmp.vector[k] = tmp.vector[k] + addofinput.vector[k];
                                    }
                                } else {
                                    System.out.println("发现单词" + e.get(window) + "没有对应向量");
                                }
                            } catch (NullPointerException E) {
                                E.fillInStackTrace();
                            }

                            t4 += System.currentTimeMillis() - t4_tmp;
                        }
                    }
                }
            } else {
                System.out.println("语料库为空");
            }
        }
    }

    public void trainline_neg(){

    }






    private double active(double x){
        if(x<-6){
            return 0;
        }
        if(x>6){
            return 1;
        }
        else{
            return Sigmoid.get((int)Math.round(x*100));
        }
    }

    private double sigmoid(double x){
        return 1/(1+Math.pow(Math.E,-x));
    }

    private void intsigmoid(){
        for(double i=-6;i<6;i+=0.01){
            Sigmoid.put((int)Math.round(i*100),sigmoid(i));
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
