package Trainer;

import Huffman.Huffman;
import Modelhandler.Model;
import Modelhandler.*;
import myFile.corpusReader;
import Negative_Sampling.nagetive_sampling;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trainer {
    private static Huffman hm;
    private nagetive_sampling ns;
    private corpusReader cr;
    private Model md;
    private int Window;
    private boolean weatherfill;
    private double step=0.025;
    private double Step=step;
    private int maxloop=1000;
    private HashMap<String,Integer> dictionary;
    String sgorcbow=null;
    int hmorneg=0;
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
        this.hmorneg=md.modelcategory;
        if(hmorneg==0) {
            this.hm = md.hm;
            this.dictionary = hm.dictionary;
            cr=new corpusReader(md.PATH,windos,weatherfill,md.hm.lowwords,md.language);
        }else{
            this.ns=md.ns;
            this.dictionary=ns.dictionary;
            cr=new corpusReader(md.PATH,windos,weatherfill,md.ns.lowwords,md.language);
        }

        intsigmoid();
    }



    public Model train(String sgorcbow,boolean static_window){
        double k=0.1;
        if(hmorneg==0) {
            String name=sgorcbow+"_"+Integer.toString(hmorneg)+"_"+Boolean.toString(static_window);
            System.out.println(name);
            for (int i = 0; i < maxloop; i++) {
                trainline_hm(sgorcbow,static_window);
                if (i % 2 == 0) {
                    System.out.println(name);
                    step = Step * (1 - (double) i / maxloop);
                    if(step<Step*0.0001){
                        step=Step*0.0001;
                    }
                    System.out.println("获取非叶子节点向量的时间：" + Long.toString(t1));
                    System.out.println("sigmoid函数的时间：" + Long.toString(t2));
                    System.out.println("更新非叶子节点向量的时间：" + Long.toString(t3));
                    System.out.println("更新单词向量的时间：" + Long.toString(t4));
                    System.out.println(md.hm.getNodevector(new byte[]{1, 0, 1}));
                    if(md.language=="eg") {
                        System.out.println(md.getVector("mother"));
                        System.out.println(md.getVector("father"));
                        System.out.println(md.dis("mother", "father"));
                        System.out.println(md.getVector("king"));
                        System.out.println(md.getVector("queen"));
                        System.out.println(md.dis("king", "queen"));
                        System.out.println(md.dis("mother", "queen"));
                    }else{
                        System.out.println(md.getVector("汽车"));
                        System.out.println(md.getVector("轿车"));
                        System.out.println(md.dis("汽车", "轿车"));
                        System.out.println(md.getVector("篮球"));
                        System.out.println(md.getVector("足球"));
                        System.out.println(md.dis("篮球", "足球"));
                        System.out.println(md.dis("篮球", "汽车"));
                    }
                    System.out.println("training has completed " + Double.toString((double) i / maxloop * 100) + "%");
                    System.out.println("---------------------------------");
                }
                if ((double) i / maxloop > k) {
                    k += 0.1;
                    md.Savemodel("E:\\myw2v\\first_tmp"+name+".model");
                }
            }
            return md;
        }
        if(hmorneg==1){
            String name=sgorcbow+"_"+Integer.toString(hmorneg)+"_"+Boolean.toString(static_window);
            System.out.println(name);
            for (int i = 0; i < maxloop; i++) {
                trainline_neg(sgorcbow,static_window);
                if (i % 2 == 0) {
                    System.out.println(name);
                    step = Step * (1 - (double) i / maxloop);
                    if(step<Step*0.0001){
                        step=Step*0.0001;
                    }
                    System.out.println(step);
                    if(md.language=="eg") {
                        System.out.println(md.getVector("mother"));
                        System.out.println(md.getVector("father"));
                        System.out.println(md.dis("mother", "father"));
                        System.out.println(md.getVector("king"));
                        System.out.println(md.getVector("queen"));
                        System.out.println(md.dis("king", "queen"));
                        System.out.println(md.dis("mother", "queen"));
                    }else{
                        System.out.println(md.getVector("汽车"));
                        System.out.println(md.getVector("轿车"));
                        System.out.println(md.dis("汽车", "轿车"));
                        System.out.println(md.getVector("篮球"));
                        System.out.println(md.getVector("足球"));
                        System.out.println(md.dis("篮球", "足球"));
                        System.out.println(md.dis("篮球", "汽车"));
                    }
                    System.out.println("training has completed " + Double.toString((double) i / maxloop * 100) + "%");
                    System.out.println("---------------------------------");
                }
                if ((double) i / maxloop > k) {
                    k += 0.1;
                    md.Savemodel("E:\\myw2v\\first_tmp"+name+".model");
                }

            }
            return md;
        }
        return md;
    }
//  根据一行语料训练
    public void trainline_hm(String sgorcbow,boolean static_window){
        int window=3;
        if(static_window){
            window=Window;
        }else {
            window = 1 + (int) (Math.random() * (Window));
        }
//        System.out.println(window);
//        int window=Window;
        if(sgorcbow.equals("cbow")) {
            HashMap<List<String>, Vector> corpusline;
            if(md.language.equals("cn")){
                corpusline=cr.handlesent_cbow_cn(md,window);
            }else {
                corpusline = cr.handlesent_cbow(md, window);
            }
            if (corpusline != null) {
                for (List<String> e : corpusline.keySet()) {
                    int termcont = dictionary.get(e.get(window));
                    double t = (double) termcont / hm.totalnum;
                    if (t > 0.0005) {
                        if (Math.random() < 1 - Math.pow((0.0005 / t), 0.5)) {
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
                            double q = sigmoid(Vector.mult(pathvector, inputvector));
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
            HashMap<List<String>, Vector> corpusline;
            if(md.language.equals("cn")){
                corpusline = cr.handlesent_sg_cn(md, window);
            }else {
                corpusline = cr.handlesent_sg(md, window);
            }
            if (corpusline != null) {
                for (List<String> e : corpusline.keySet()) {
                    int termcont = dictionary.get(e.get(window));
                    double t = (double) termcont / hm.totalnum;
                    if (t > 0.0005) {
                        if (Math.random() < 1 - Math.pow((0.0005 / t), 0.5)) {
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
                                double q = sigmoid(Vector.mult(pathvector, inputvector));
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

    public void trainline_neg(String sgorcbow,boolean static_window){
        int window=3;
        if(static_window){
            window=Window;
        }else {
            window = 1 + (int) (Math.random() * (Window));
        }
        if(sgorcbow.equals("cbow")){
            HashMap<List<String>, Vector> corpusline = cr.handlesent_cbow(md, window);
            if (corpusline != null) {
                for (List<String> e : corpusline.keySet()) {
                    int termcont = dictionary.get(e.get(window));
                    double t = (double) termcont / ns.totalnum;
                    if (t > 0.0005) {
                        if (Math.random() < 1 - Math.pow((0.0005 / t), 0.5)) {
                            continue;
                        }
                    }
                    Vector inputvector = corpusline.get(e);
                    Vector addinput=new Vector(inputvector.getSize(),0);
                    for(String negterm:ns.getsampling(e.get(window),15)){
//                        System.out.println(negterm);
                        Vector assistant_tmp=ns.getVector(negterm);
                        double q=active(Vector.mult(inputvector,assistant_tmp));
                        double g=step*(-q);
                        addinput=Vector.adds(addinput,Vector.mult(g,assistant_tmp));

//                        System.out.println("term增量为：" + addinput);

                        Vector tmp=Vector.mult(g,inputvector);
                        for(int i=0;i<tmp.getSize();i++){
                            assistant_tmp.vector[i]=assistant_tmp.vector[i]+tmp.vector[i];
                        }
                    }
                    Vector assistant_tmp=ns.getVector(e.get(window));
                    double q=active(Vector.mult(inputvector,assistant_tmp));
                    double g=step*(1-q);
                    addinput=Vector.adds(addinput,Vector.mult(g,assistant_tmp));
                    Vector tmp=Vector.mult(g,inputvector);
                    for(int i=0;i<tmp.getSize();i++){
                        assistant_tmp.vector[i]=assistant_tmp.vector[i]+tmp.vector[i];
                    }
                    for(int i=0;i<e.size();i++){
                        if(i==window){
                            continue;
                        }
                        Vector tmp_term=md.getVector(e.get(i));
                        for(int k=0;k<addinput.getSize();k++){
                            tmp_term.vector[k]=tmp_term.vector[k]+addinput.vector[k];
                        }
                    }
                }
            }
        }else{
            HashMap<List<String>, Vector> corpusline = cr.handlesent_sg(md, window);
            if (corpusline != null) {
                for (List<String> e : corpusline.keySet()) {
                    int termcont = dictionary.get(e.get(window));
                    double t = (double) termcont / ns.totalnum;
                    if (t > 0.0005) {
                        if (Math.random() < 1 - Math.pow((0.0005 / t), 0.5)) {
                            continue;
                        }
                    }
                    Vector inputvector = corpusline.get(e);
                    for(int k=0;k<2*window;k++) {
                        if (k == window) {
                            continue;
                        }
                        Vector addinput = new Vector(inputvector.getSize(), 0);
                        for (String negterm : ns.getsampling(e.get(k), 15)) {
                            Vector assistant_tmp = ns.getVector(negterm);
                            double q = active(Vector.mult(inputvector, assistant_tmp));
                            double g = step * (-q);
                            addinput = Vector.adds(addinput, Vector.mult(g, assistant_tmp));
                            Vector tmp = Vector.mult(g, inputvector);
                            for (int i = 0; i < tmp.getSize(); i++) {
                                assistant_tmp.vector[i] = assistant_tmp.vector[i] + tmp.vector[i];
                            }
                        }
                        Vector assistant_tmp = ns.getVector(e.get(k));
                        double q = active(Vector.mult(inputvector, assistant_tmp));
                        double g = step * (1-q);
                        addinput = Vector.adds(addinput, Vector.mult(g, assistant_tmp));
                        Vector tmp = Vector.mult(g, inputvector);
                        for (int i = 0; i < tmp.getSize(); i++) {
                            assistant_tmp.vector[i] = assistant_tmp.vector[i] + tmp.vector[i];
                        }
                        Vector tmp_term = md.getVector(e.get(window));
                        for (int p = 0; p < addinput.getSize(); p++) {
                            tmp_term.vector[p] = tmp_term.vector[p] + addinput.vector[p];
                        }
                    }
                }
            }
        }
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
