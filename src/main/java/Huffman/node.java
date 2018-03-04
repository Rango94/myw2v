package Huffman;

import Modelhandler.Vector;

public class node {
//    左边权值更小
    private String name="null";
    private int weight=0;
    private node leftnode;
    private node rightnode;
    private boolean isleaf;
    private Vector vec;

    public Vector getVec() {
        return vec;
    }

    public void setVec(Vector vec) {
        this.vec = vec;
    }

    @Override
    public String toString(){
        return name+"\t"+Integer.toString(weight)+"\t"+leftnode.getName()+"\t"+rightnode.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsleaf() {
        return isleaf;
    }

    public void setIsleaf(boolean isleaf) {
        this.isleaf = isleaf;
    }

    public int getWeight() {
        return weight;
    }

    public node getLeftnode() {
        return leftnode;
    }

    public node getRightnode() {
        return rightnode;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setLeftnode(node left) {
        this.leftnode = left;
    }

    public void setRightnode(node right) {
        this.rightnode = right;
    }


}
