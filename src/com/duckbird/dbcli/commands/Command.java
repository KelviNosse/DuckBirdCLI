package com.duckbird.dbcli.commands;

import com.beust.jcommander.JCommander;

public interface Command {
    void execute(String[] args, JCommander jc);
    boolean compare(String command);
    String getName();
}
