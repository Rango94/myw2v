import Negative_Sampling.nagetive_sampling;

import java.util.List;

public class nagtest {

    public static void main(String[] args) {
        nagetive_sampling ns=new nagetive_sampling("F:/w2vcorpus/text8.txt",100);
        List<String> Ns=ns.getsampling("mother",10);
        for (String e :Ns){
            System.out.print(e+"\t");
        }
        System.out.print("\n");
    }



}
