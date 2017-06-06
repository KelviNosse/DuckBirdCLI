package com.duckbird.core.sqltasks.handlers;

import com.duckbird.core.errors.TableNotFound;
import com.duckbird.core.shared.Connection;
import com.duckbird.core.sqltasks.models.ColumnType;
import com.duckbird.core.sqltasks.models.DBColumns;
import com.duckbird.core.sqltasks.models.DBTable;
import com.duckbird.core.structure.models.DirTable;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
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
                this.showColumnedTable(items, tableList);
                break;
            }
        }
    }

    private void showColumnedTable(List<SelectItem> items, List<String> tableList) throws IOException, TableNotFound {
        DirTable dirTable = Connection.getDirTable();
        for(int i = 0; i < tableList.size(); i++){
            if(!dirTable.entryExists(tableList.get(i))) throw new TableNotFound(tableList.get(i));
        }
//        RandomAccessFile db_file = Connection.getDatabase().getFile();
//        int columns_count = dirTable.getEntry(tableList.get(0)).TableMetadata.columns_count;
        for(int i = 0; i < tableList.size(); i++){
//            int block_offset = dirTable.getEntry(tableList.get(i)).block_offset;
//            db_file.seek(block_offset);
//            DBColumns columns = new DBColumns();
            for(int j = 0; j < items.size(); j++) {
                //todo implement validation for correct columns
                //todo show registers too!
                System.out.println("Selected col: "+items.get(j).toString());
//                int length = db_file.readInt();
//                String col_name = "";
//                for (int k = 0; k < length; k++) col_name += db_file.readChar();
//                int type_len = db_file.readInt();
//                String type_name = "";
//                for(int k = 0; k < type_len; k++) type_name += db_file.readChar();
//                ColumnType col_type = ColumnType.valueOf(type_name);
//                columns.add(col_name, col_type);
            }
//            DBTable table = new DBTable(tableList.get(i), columns);
//            table.print();
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
            DBColumns columns = new DBColumns();
            for(int j = 0; j < columns_count; j++) {
                int length = db_file.readInt();
                String col_name = "";
                for (int k = 0; k < length; k++) col_name += db_file.readChar();
                int type_len = db_file.readInt();
                String type_name = "";
                for(int k = 0; k < type_len; k++) type_name += db_file.readChar();
                ColumnType col_type = ColumnType.valueOf(type_name);
                columns.add(col_name, col_type);
            }
            int register_offset = dirTable.getEntry(tableList.get(i)).registers_offset;
            int registers_count = dirTable.getEntry(tableList.get(i)).TableMetadata.registers;
            if(register_offset != 0){
                db_file.seek(register_offset);
                DBTable table = new DBTable(tableList.get(i), columns);
                LinkedList<String> registers = new LinkedList<String>();
                for(int j = 0; j < registers_count*columns_count; j++){
                    int len = db_file.readInt();
                    String val = "";
                    for(int k = 0; k < len; k++) val += db_file.readChar();
                    registers.add(val);
                }
                table.setRows(registers_count, columns_count, registers);
                table.print();
            }
        }
    }
}
