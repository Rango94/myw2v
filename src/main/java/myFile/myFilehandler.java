package myFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class myFilehandler {
    FileWriter fw;
    BufferedWriter bw;

    public myFilehandler(String filePath){
        try {
            fw = new FileWriter(filePath, false);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writer(String sent){
        try {
            bw.write(sent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bw.close();
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
