package com.duckbird.core.sqltasks.handlers;

import com.duckbird.core.errors.TableNotFound;
import com.duckbird.core.shared.Connection;
import com.duckbird.core.structure.models.DirTable;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class Selection {
    public Selection(){}

    public void show(List<SelectItem> items, List<String> tableList) throws IOException, TableNotFound {
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).toString() == "*"){
                //display all table
                this.showFullTable(tableList);
            }else{
                //display listed columns from table
            }
        }
    }

    private void showFullTable(List<String> tableList) throws IOException, TableNotFound {
        DirTable dirTable = Connection.getDirTable();
        for(int i = 0; i < tableList.size(); i++){
            if(!dirTable.entryExists(tableList.get(i))) throw new TableNotFound(tableList.get(i));
        }
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        int columns_count = dirTable.getEntry(tableList.get(0)).TableMetadata.columns_count;
        for(int i = 0; i < tableList.size(); i++){
            int block_offset = dirTable.getEntry(tableList.get(i)).block_offset;
            db_file.seek(block_offset);
            for(int j = 0; j < columns_count; j++) {
                int length = db_file.readInt();
                String col_name = "";
                for (int k = 0; k < length; k++) col_name += db_file.readChar();
                System.out.println("Column name?: " + col_name);
            }
        }
    }
}
