package com.duckbird.dbcli.commands;

import com.beust.jcommander.JCommander;
import com.duckbird.core.sqltasks.SQLEditor;

public class RunSQL implements Command {

    public void execute(String[] args, JCommander jc) {
        try{
            jc.parse(args);
            SQLEditor editor = new SQLEditor();
            editor.run();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Invalid arguments!\nRun => help -name "+this.getName()+" to view usage.");
        }
    }

    public boolean compare(String command){
        return this.getName().contentEquals(command.toLowerCase());
    }

    public String getName() {
        return "runsql";
    }
}
