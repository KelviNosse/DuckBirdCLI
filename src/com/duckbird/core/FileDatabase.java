package com.duckbird.core;
import static com.duckbird.core.shared.Utils.*;
public class FileDatabase {
    private String name, formattedSize;
    private long size;

    public FileDatabase(String name, long size){
        this.name = name;
        this.size = size;
        this.formattedSize = formatFileSize(size);
    }
}
