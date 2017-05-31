package com.duckbird.dbcli.commands;

public class Help implements Command {
    public void execute() {
        System.out.println("You are viewing the help command.");
    }

    public boolean compare(String command){
        return "help".equalsIgnoreCase(command);
    }
}
