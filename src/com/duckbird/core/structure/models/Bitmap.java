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

    public void toggle(int index){
        this.flip(index);
    }

    public void writeOnDisk() throws IOException {
        RandomAccessFile db_file = Connection.getDatabase().file;
        db_file.seek(this.offset);
        byte[] bytes = this.toByteArray();
        db_file.write(bytes);
        db_file.getFD().sync();
    }

    public void initOnDisk() throws IOException {
        this.writeOnDisk();
    }

    @Override
    public String toString(){
        final StringBuilder buffer = new StringBuilder(this.size);
        IntStream.range(0, this.size).mapToObj(i -> get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }

}
