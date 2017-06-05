package com.duckbird.core.structure.models;

import com.duckbird.core.shared.Connection;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;
import java.util.stream.IntStream;

public class Bitmap extends BitSet{
    private int size;
    public int offset;
    public Bitmap(int size){
        super(size);
        this.size = size;
    }

    public Bitmap(){
        super();
    }


    public void toggle(int index){
        this.flip(index);
    }

    public void writeOnDisk() throws IOException {
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        db_file.seek(this.offset);
        byte[] bytes = this.toByteArray();
        db_file.write(bytes);
        db_file.getFD().sync();
    }

    public int freeBlock(){
        for(int i = 0; i < this.size(); i++){
            if(!this.get(i)) return i;
        }
        return -1;
    }

//    @Override
//    public byte[] toByteArray(){
//        byte[] bytes = new byte[this.size()/8+1];
//        for(int i = 0; i < this.size(); i++){
//            if(this.get(i)){
//                bytes[bytes.length - i/8-1] |= 1 << (i%8);
//            }
//        }
//        return bytes;
//    }

    @Override
    public String toString(){
        final StringBuilder buffer = new StringBuilder(this.size());
        IntStream.range(0, this.size()).mapToObj(i -> get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }

}
