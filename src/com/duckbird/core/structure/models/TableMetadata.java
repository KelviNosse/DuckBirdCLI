package com.duckbird.core.structure.models;

public class TableMetadata {
    public int columns_count;
    public int registers;
    public int table_size;
    public int Size(){ return 12; } //don't need a sizeof thanks to the jvm lol.
}