import myFile.myFilehandler;

public class testfile {

    public static void main(String[] args){
        myFilehandler myFilehandler =new myFilehandler("E:/myw2v/test.txt");
        myFilehandler.writer("jdkfjskldfjs"+"\n");
        myFilehandler.writer("sdfsdfsf");
        myFilehandler.close();
    }
}
