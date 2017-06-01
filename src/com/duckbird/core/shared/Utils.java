package com.duckbird.core.shared;
import java.io.File;
import java.text.DecimalFormat;

public class Utils {
    private static Utils instance = new Utils();

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
                    return toLong(number);
                else System.out.println("Invalid input size!");
        }
        return -1;
    }

    private Utils() {}
}
