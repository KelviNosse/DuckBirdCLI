package com.duckbird.core.structure;


import com.duckbird.core.shared.Connection;
import com.duckbird.core.structure.models.*;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DBStructure {
    private long disksize;
    private int blocksize;
    private String name;
    public int num_blocks;
    private int table_count;
    public DBStructure(){
        this.name = Connection.getDatabase().name;
        this.disksize = Connection.getDatabase().size;
        this.blocksize = (int)(long)Connection.getDatabase().blocksize;
        this.num_blocks = (int) (this.disksize/this.blocksize);
        this.table_count = 0;
    }

    public void create(){
        Superblock sb = new Superblock(this.name, this.disksize, this.blocksize, this.table_count);
        Bitmap bmap = new Bitmap(this.num_blocks);
        try {
            sb.write();
            writeBitmap(bmap);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeBitmap(Bitmap bmap) throws IOException {
        RandomAccessFile db_file = Connection.getDatabase().file;
        db_file.seek(this.blocksize);
        byte[] bytes = bmap.array.toByteArray();
        db_file.write(bytes);
        db_file.getFD().sync();
    }
}
