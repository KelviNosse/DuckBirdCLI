package com.duckbird.core.shared;

import com.duckbird.core.FileDatabase;
import com.duckbird.core.errors.InvalidBlockSize;
import com.duckbird.core.errors.InvalidDBFile;
import com.duckbird.core.errors.InvalidDiskSize;
import com.duckbird.core.structure.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static com.duckbird.core.shared.Utils.*;

public class IOStreamDB extends RandomAccessFile{
    private File db_file;
    public IOStreamDB(File db) throws FileNotFoundException {
        super(db, "rw");
        db_file = db;
    }

    public void load() throws IOException, InvalidDBFile {
         Connection.setSuperblock(this.loadSuperblock());
         Connection.setBitmap(this.loadBitmap());
         Connection.setDirTable(this.loadDirTable());
         Connection.setDatabase(new FileDatabase());
         Connection.getDatabase().setFile(this);
    }

    private DirTable loadDirTable() throws IOException {
        int blocks = Connection.getSuperblock().blocks_amount;
        int size = (int)Math.ceil(((float)blocks/8));
        int bmapBlocks = (int)Math.ceil(((float)size/Connection.getSuperblock().blocksize));
        int offset = (int) (long) (Connection.getSuperblock().blocksize + (bmapBlocks*Connection.getSuperblock().blocksize));
        this.seek(offset);
        int table_amount = (int)Math.ceil(((float)Connection.getSuperblock().blocksize/(new TableMetadata()).Size()));
        TableEntries[] tableEntries = new TableEntries[table_amount];
        for(int i = 0; i < tableEntries.length; i++){
            String name = "";
            tableEntries[i] = new TableEntries();
            tableEntries[i].TableMetadata = new TableMetadata();
            for(int k = 0; k < 25; k++) name += this.readChar();
            tableEntries[i].table_name = name.toCharArray();
            tableEntries[i].block_offset = this.readInt();
            tableEntries[i].registers_offset = this.readInt();
            tableEntries[i].TableMetadata.table_size = this.readInt();
            tableEntries[i].TableMetadata.registers = this.readInt();
            tableEntries[i].TableMetadata.columns_count = this.readInt();
        }
        DirTable dirTable = new DirTable(tableEntries);
        dirTable.offset = offset;
        int dirTableBlocks = (int)Math.ceil(((float)dirTable.Size()/Connection.getSuperblock().blocksize));
        System.out.println("Dir table blocks: "+dirTableBlocks);
        Utils.getInstance().SetSharedMetadata(blocks, bmapBlocks*(int)Connection.getSuperblock().blocksize, dirTableBlocks*(int)Connection.getSuperblock().blocksize);
        return dirTable;
    }

    private Bitmap loadBitmap() throws IOException {
        this.seek(Connection.getSuperblock().blocksize);
        int blocks = Connection.getSuperblock().blocks_amount;
        int size = (int)Math.ceil(((float)blocks/8));
        int bytes_size = (int)Math.ceil(((float)size/Connection.getSuperblock().blocksize));
        byte[] bytes = new byte[bytes_size*8];
        this.read(bytes);
        reverse(bytes);
        Bitmap bmap = fromByteArray(bytes);
        bmap.offset = (int) (long) Connection.getSuperblock().blocksize;
        return bmap;
    }

    private Superblock loadSuperblock() throws InvalidDBFile, IOException {
        this.seek(0);
        int magicNumber = this.readInt();
        if(magicNumber != MAGIC_NUMBER) throw new InvalidDBFile(db_file.getName());
        String disk_name = "";
        int name_length = this.readInt();
        for(int i = 0; i < name_length; i++){
            char letter = this.readChar();
            disk_name += letter;
        }
        System.out.println("Disk name: "+disk_name);
        long disk_size = this.readLong();
        System.out.println("\nDisk Size: "+disk_size);
        long block_size = this.readLong();
        System.out.println("\nBlock size: "+block_size);
        int used_size = this.readInt();
        this.readInt();
        int table_count = this.readInt();
        int tableLimit = this.readInt();
        Superblock sb = new Superblock(disk_name, disk_size, block_size, table_count);
        sb.used_size = used_size;
        sb.tableLimit = tableLimit;
        return sb;
    }
}
