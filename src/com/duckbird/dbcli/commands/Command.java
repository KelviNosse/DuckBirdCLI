package com.duckbird.dbcli.commands;

public interface Command {
    void execute();
    boolean compare(String command);
}
