package me.kapehh.CommandTask.task;

import me.kapehh.CommandTask.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 26.11.2014.
 */
public class TaskCommandList {
    List<TaskCommand> taskCommands = new ArrayList<TaskCommand>();

    public TaskCommandList() {
    }

    public void clearCommands() {
        taskCommands.clear();
    }

    public void addCommand(TaskCommand taskCommand) {
        if (!taskCommands.contains(taskCommand)) {
            taskCommands.add(taskCommand);
        }
    }

    public void removeCommand(TaskCommand taskCommand) {
        taskCommands.remove(taskCommand);
    }

    public boolean isExistsNowTasks(long currentUnixTime) {
        for (TaskCommand taskCommand : taskCommands) {
            if (taskCommand.getTime() <= currentUnixTime) {
                return true;
            }
        }
        return false;
    }

    public List<TaskCommand> getNowTasks(long currentUnixTime) {
        List<TaskCommand> retCommands = new ArrayList<TaskCommand>();
        for (TaskCommand taskCommand : taskCommands) {
            if (taskCommand.getTime() <= currentUnixTime) {
                retCommands.add(taskCommand);
            }
        }
        return retCommands;
    }

    public void removeTasks(List<TaskCommand> tasks) {
        taskCommands.removeAll(tasks);
        /*for (TaskCommand taskCommandFind : tasks) {
            for (TaskCommand taskCommandRemove : taskCommands) {
                TODO
            }
        }*/
    }

    @Override
    public String toString() {
        return "TaskCommandList{" +
                "taskCommands=" + taskCommands +
                '}';
    }
}
