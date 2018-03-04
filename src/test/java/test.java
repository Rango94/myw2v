import Huffman.Huffman;
import Modelhandler.Vector;
import myFile.corpusReader;
import myFile.myFilehandler;

import java.io.*;
import java.util.HashMap;

public class test {
    public static void main(String[] a) {
        HashMap<String, Vector> te=new HashMap<String,Vector>();
        te.put("a",new Vector(10));
        te.put("b",new Vector(10));
        Vector k=te.get("a");
        k.vector[0]=1;
        System.out.println(te.get("a"));
    }
}
