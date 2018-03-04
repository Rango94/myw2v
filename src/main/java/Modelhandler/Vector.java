package Modelhandler;

public class Vector {
    public double[] vector;
    static int Size=0;

    public int getSize() {
        return Size;
    }

    public Vector(double[] vec){
        vector=vec;
        Size=vec.length;
    }

    public Vector(){
    }

    public Vector(int size){
        double[] vec=new double[size];
        for(int i=0;i<size;i++){
            vec[i]=(Math.random()-0.5)/size;
        }
        vector=vec;
        Size=size;
    }

    public Vector(int size,int num){
        double[] vec=new double[size];
        for(int i=0;i<size;i++){
            vec[i]=num;
        }
        vector=vec;
        Size=size;
    }

    public static Vector adds(Vector a,Vector b){
        if(b.getSize()!=Size){
            System.out.println("长度不匹配");
            return new Vector(new double[Size+1]);
        }
        double[] out=new double[a.getSize()];
        for(int i=0;i<Size;i++){
            out[i]=a.vector[i]+b.vector[i];
        }
        return new Vector(out);
    }

    public static double mult(Vector a,Vector b){
        double out=0;
        for(int i=0;i<Size;i++){
            out+=a.vector[i]*b.vector[i];
        }
        return out;
    }

    public static Vector mult(double b,Vector vec){
        Vector out=new Vector(Size,0);
        for(int i=0;i<Size;i++){
            out.vector[i]=vec.vector[i]*b;
        }
        return out;
    }


    public Vector divise(int arg){
        if(Size==0){
            System.out.println("向量为空");
        }else{
            for(int i=0;i<Size;i++){
                vector[i]=vector[i]/arg;
            }
        }
        return this;
    }



    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.Size=vector.length;
        this.vector = vector;
    }
    @Override
    public String toString(){
        String vec="";
        for(double e:vector){
            vec+=Double.toString(e)+"\t";
        }
        return vec;
    }

    public static double dis(Vector a,Vector b){
        double out=0.0;
        double stand=0.0;
        for(int i=0;i<a.getSize();i++){
            stand+=(Math.abs(a.vector[i])+Math.abs(b.vector[i]))/2;
            out+=Math.pow((a.vector[i]-b.vector[i]),2)/Math.pow(stand,2);
        }
        return Math.pow(out,0.5);
    }

}
