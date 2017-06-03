package com.duckbird.core.structure.models;

public class TableEntries {
    public char[] table_name = new char[25];
    public int block_offset = 0;
    public TableMetadata TableMetadata = new TableMetadata();
    public TableEntries(){
        this.table_name = new char[25];
        this.block_offset = 0;
        this.TableMetadata = new TableMetadata();
        this.TableMetadata.columns_count = 0;
        this.TableMetadata.registers = 0;
        this.TableMetadata.table_size = 0;
    }

    public int Size() {
        return 25 + 4 + this.TableMetadata.Size();
    }
}
