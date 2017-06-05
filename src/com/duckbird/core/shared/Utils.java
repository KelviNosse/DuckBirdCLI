package com.duckbird.core.shared;
import com.duckbird.core.errors.InvalidBlockSize;
import com.duckbird.core.errors.InvalidDiskSize;
import com.duckbird.core.structure.models.Bitmap;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class Utils {
    private static Utils instance = new Utils();
    public static int REGISTERS = 500;
    public static int MAGIC_NUMBER = 0xDEADCAFE;
    public int NUM_BLOCKS = 0;
    public int BITMAP_SIZE = 0;
    public int DIR_TABLE_SIZE = 0;
    public String file_path;
    public static Utils getInstance() {
        return instance;
    }

    public static String formatFileSize(long size) {
        String hrSize = null;
        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);
        DecimalFormat dec = new DecimalFormat("0.00");
        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }
        return hrSize;
    }

    public void dropFileDB(String dbName){
        (new File(dbName+".duck")).delete();
    }
    public static long toLong(String number){
        return Long.parseLong(number);
    }

    public static long fromFormatToSize(String size){
        String suffix = size.substring(size.length()-2);
        String number = "";
        switch (suffix.toUpperCase()){
            case "MB":
                number = size.substring(0, size.length()-2);
                return toLong(number) * 1024 * 1024;
            case "GB":
                number = size.substring(0, size.length()-2);
                return toLong(number) * 1024 * 1024 * 1024;
            case "KB":
                number = size.substring(0, size.length()-2);
                return toLong(number) * 1024;
            default:
                if(!size.chars().anyMatch(Character::isLetter))
                    return toLong(size);
                else System.out.println("Invalid input size!");
        }
        return -1;
    }

    public static int checkValidBlockSize(int blocksize, long disksize) throws InvalidBlockSize {
        if(blocksize >= disksize || blocksize < 0) throw new InvalidBlockSize("Size of blocks is invalid!");
        if(blocksize % 2 != 0) throw new InvalidBlockSize("Block size must be divisible by 2");
        return blocksize;
    }

    public static long checkValidSize(long size) throws InvalidDiskSize {
        if(size < 0) throw  new InvalidDiskSize("Size of disk is invalid!");
        return size;
    }

    public static Bitmap fromByteArray(byte[] bytes) {
        Bitmap bits = new Bitmap();
        for (int i=0; i<bytes.length*8; i++) {
            if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    public void SetSharedMetadata(int num_blocks, int bmap_size, int dir_table_size){
        this.NUM_BLOCKS = num_blocks;
        this.BITMAP_SIZE = bmap_size;
        this.DIR_TABLE_SIZE = dir_table_size;
    }

    public static boolean isCharArrayEmpty(char[] arr){
        int count = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == '\u0000') count++;
        }
        return (count == arr.length);
    }

    private Utils() {}
}
