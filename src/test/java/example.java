import Huffman.Huffman;
import Modelhandler.Model;
import Modelhandler.Vector;
import Trainer.Trainer;

import java.util.Scanner;

public class example {
    public static void main(String[] a){

//        设定向量长度
        int size=100;

//        读取语料文件并生成模型
        Model md=new Model("F:/w2vcorpus/text8.txt",size);
//
//        新建训练类并设定参数
        Trainer tr=new Trainer(md,3,false,0.025f);

//        训练模型
        md=tr.train();

//        保存模型
//        System.out.println(Vector.dis(new Vector(new double[]{1,1}).normalization(),new Vector(new double[]{2,2}).normalization()));
        md.Savemodel("E:\\myw2v\\first.model");



//        读取模型
        Model md1=new Model();
        md1.Loadmodel("E:\\myw2v\\first.model");
//        寻找训练语料中最相近的两个单词
//        double cha=-1;
//        for(int i=0;i<md1.getTermnum();i++){
//            Vector k=md1.getVector(i);
//            for(int j=i+1;j<md1.getTermnum();j++){
//                    if (cha == -1) {
//                        cha = Vector.dis(k,md1.getVector(j));
//                    }
//                    if (Vector.dis(k,md1.getVector(j)) > cha) {
//                        cha = Vector.dis(k,md1.getVector(j));
//                        System.out.println(cha);
//                        System.out.println(md1.gettermlist().get(i) + "\t" + md1.gettermlist().get(j));
//                    }
//            }
//        }

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("输入");
            String term = sc.nextLine();
            md1.similar(term, 15);
        }
//        System.out.println(md1.getVector("normal"));
//        System.out.println(md1.getVector("mother"));
//        System.out.println(md1.getVector("father"));
//        System.out.println(md1.dis("mother","father"));
    }
}
