package com.duckbird.core.structure.models;

import java.util.BitSet;

public class Bitmap {
    public BitSet array;
    private int size;
    public Bitmap(int size){
        this.size = size;
        this.array = new BitSet(size);
    }

    public void toggle(int index){
        this.array.flip(index);
    }

}
