package Modelhandler;
import Huffman.Huffman;
import Negative_Sampling.nagetive_sampling;
import myFile.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Model {
    public HashMap<String,HashMap<String,Vector>> model=new HashMap<String, HashMap<String,Vector>>();
    public Huffman hm=null;
    public nagetive_sampling ns=null;
    public String PATH;
    private static int Size=0;
    public int termnum=0;
    private List<String> termlist=new ArrayList<String>();
    public int modelcategory=0;

    public static int getSize() {
        return Size;
    }

    public List<String> gettermlist() {
        return termlist;
    }

    public Model(String path, int size,String hmorns){
        if(hmorns.equals("hm")) {
            modelcategory=0;
            System.out.println("building Huffman...");
            hm = new Huffman(path, size);
//        hm.notleafstoString();
            System.out.println("initial model...");
            initModel(hm.Termset, size);
            PATH = path;
        }else{
            modelcategory=1;
            System.out.println("building negative sampling...");
            ns=new nagetive_sampling(path,size);
            System.out.println("initial model...");
            initModel(ns.getTermlist(), size);
            PATH = path;
        }
    }
    public Model(){

    }

    public void similar(String term,int num){
        if(num>50){
            num=50;
        }
        double F=100000;
        Vector term_v=getVector(term);
        if(term_v==null){
            System.out.println("null");
        }else {
            List<String> terms = new ArrayList<String>();
            List<Double> vecs = new ArrayList<Double>();
            for (int i = 0; i < num; i++) {
                String name = "";
                double d = 0;
                for (String e : termlist) {
                    if (!e.equals(term)) {
                        Vector e_v = getVector(e);
                        double temp = Vector.dis(e_v, term_v);
                        if (temp > d && temp <= F && !terms.contains(e)) {
                            d = temp;
                            name = e;
                        }
                    }
                }
                terms.add(name);
                vecs.add(d);
                F = d;
            }
            for (int i = 0; i < terms.size(); i++) {
                System.out.println(terms.get(i) + ":" + vecs.get(i));
            }
        }
    }

    public double dis(String a,String b){
        return Vector.dis(getVector(a),getVector(b));
    }

//  初始化模型：用随机向量填充
    private void initModel(Set<String> termset,int size){
        Size=size;
        for(String e:termset){
            termlist.add(e);
            setVector(e,new Vector(size));
        };
        termnum=termset.size();
        System.out.println("has collected "+termnum+" words");
    }

    public int getTermnum() {
        return termnum;
    }

    public void Loadmodel(String path){
        model = new HashMap<String, HashMap<String, Vector>>();
        while (true) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(new File(path)), "utf-8"));
                String line = br.readLine();
                if (!line.equals("this a word2vec model created by WNZ")) {
                    System.out.println("this is not a model");
                    br.close();
                    break;
                }
                line = br.readLine();
                while (line != null) {
                    String[] Line=line.split(":");
                    setVector(Line[0],toVector(Line[1]));
                    line = br.readLine();
                }
                br.close();
            } catch(IOException e){
                e.printStackTrace();
            }
            for(String e:model.keySet()){
                for(String e1:model.get(e).keySet()) {
                    termnum+=1;
                    termlist.add(e1);
                }
            }
            break;
        }
    }

//  按行生成模型
    private Vector toVector(String sent){
        String[] vecstr=sent.split("\t");
        double[] vecdou=new double[vecstr.length];
        for(int i=0;i<vecdou.length;i++){
            vecdou[i]=Double.parseDouble(vecstr[i]);
        }
        return new Vector(vecdou);
    }

    public void Savemodel(String path){
        myFilehandler file =new myFilehandler(path);
        Set<String> term=model.keySet();
        file.writer("this a word2vec model created by WNZ"+"\n");
        for(String e:term){
            for(String e1:model.get(e).keySet()){
                file.writer(e1+":"+getVector(e1).normalization().toString()+"\n");
            }
        }
        file.close();
    }

//    获取单个term的向量
    public Vector getVector(String term){
        String head=term.substring(0,1);
        return model.get(head).get(term);
    }

    public Vector getVector(int idx){
        return getVector(termlist.get(idx));
    }

    public void setVector(String term,Vector vec){
        String head=term.substring(0,1);
        HashMap<String,Vector> tmp=model.get(head);
        if (tmp==null){
            tmp=new HashMap<String, Vector>();
        }
        tmp.put(term,vec);
        model.put(head,tmp);
    }

    public void train(){
        for(String e:model.keySet()){
        }
    }






}
