package com.duckbird.core.structure.models;

import com.duckbird.core.shared.Connection;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Blocks {
    public int offset;
    public int blocksUsed;
    public Blocks(){

    }

    public void allocate() throws IOException {
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        int blocks = Connection.getSuperblock().blocks_amount - this.blocksUsed;
        for(int i = 0; i < blocks; i++){
            int offset = (int) (this.offset + (i*Connection.getDatabase().blocksize));
            db_file.seek(offset);
            int nextBlock = (int)(long)(this.offset + ((i+1)*Connection.getDatabase().blocksize));
            db_file.writeInt(nextBlock);
        }
        db_file.getFD().sync();
    }
}
