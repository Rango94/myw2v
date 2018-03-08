package Modelhandler;

public class Vector {
    public float[] vector;
    static int Size=0;

    public int getSize() {
        return Size;
    }

    public Vector(float[] vec){
        vector=vec;
        Size=vec.length;
    }

    public Vector(){
    }

    public Vector(int size){
        float[] vec=new float[size];
        for(int i=0;i<size;i++){
            vec[i]=(Math.random()-0.5)/size;
        }
        vector=vec;
        Size=size;
    }

    public Vector(int size,int num){
        float[] vec=new float[size];
        for(int i=0;i<size;i++){
            vec[i]=num;
        }
        vector=vec;
        Size=size;
    }

    public static Vector adds(Vector a,Vector b){
        if(b.getSize()!=Size){
            System.out.println("长度不匹配");
            return new Vector(new float[Size+1]);
        }
        float[] out=new float[a.getSize()];
        for(int i=0;i<Size;i++){
            out[i]=a.vector[i]+b.vector[i];
        }
        return new Vector(out);
    }

    public static float mult(Vector a,Vector b){
        float out=0;
        for(int i=0;i<a.getSize();i++){
            out+=a.vector[i]*b.vector[i];
        }
        return out;
    }

    public static Vector mult(float b,Vector vec){
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



    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.Size=vector.length;
        this.vector = vector;
    }
    @Override
    public String toString(){
        String vec="";
        for(float e:vector){
            vec+=Float.toString(e)+"\t";
        }
        return vec;
    }

    public Vector normalization(){
        Vector out=new Vector(vector.length,0);
        float len=0;
        for(int i=0;i<getSize();i++){
            len+=vector[i]*vector[i];
        }
        len=Math.pow(len,0.5);
        for(int i=0;i<getSize();i++){
            out.vector[i]=this.vector[i]/len;
        }
        return out;
    }


    public static float dis(Vector a,Vector b){
        float up=mult(a,b);
        float downa=0;
        float downb=0;
        for(int i=0;i<a.getSize();i++){
            downa+=a.vector[i]*a.vector[i];
            downb+=b.vector[i]*b.vector[i];
        }
        downa=Math.pow(downa,0.5);
        downb=Math.pow(downb,0.5);
        return up/(downa*downb);
    }
}
