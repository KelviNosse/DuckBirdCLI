package com.duckbird.core.structure.models;

import com.duckbird.core.shared.Connection;

import java.io.*;

public class DirTable {
    private TableEntries[] tableEntries;
    public int offset;
    public DirTable(){
        this.tableEntries = new TableEntries[this.Length()];
        this.init();
    }

    public DirTable(TableEntries[] tableEntries){
        this.tableEntries = tableEntries;
    }

    private void init(){
        for(int i = 0; i < this.Length(); i++)
            this.tableEntries[i] = new TableEntries();
    }

    public int Length(){
        int blocksize = (int)(float) Connection.getDatabase().blocksize;
        float len = ((float)blocksize/(new TableMetadata()).Size());
        return (int)Math.ceil(len);
    }

    public int Size(){
        return (new TableEntries()).Size() * this.Length();
    }

    public void writeOnDisk() throws IOException {
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        db_file.seek(this.offset);
        for(int i = 0; i < this.tableEntries.length; i++){
            db_file.writeChars(new String(this.tableEntries[i].table_name));
            db_file.writeInt(this.tableEntries[i].block_offset);
            db_file.writeInt(this.tableEntries[i].TableMetadata.table_size);
            db_file.writeInt(this.tableEntries[i].TableMetadata.registers);
            db_file.writeInt(this.tableEntries[i].TableMetadata.columns_count);
        }
        db_file.getFD().sync();
    }
}
