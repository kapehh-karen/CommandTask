package me.kapehh.CommandTask.task;

/**
 * Created by Karen on 26.11.2014.
 */
public class TaskCommand {
    int id;
    String command;
    long time;

    public TaskCommand() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
