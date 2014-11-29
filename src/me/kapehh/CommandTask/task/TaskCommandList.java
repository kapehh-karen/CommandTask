package me.kapehh.CommandTask.task;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 26.11.2014.
 */
public class TaskCommandList {
    List<TaskCommand> taskCommands = new ArrayList<TaskCommand>();
    List<Integer> taskCommandsForDelete = new ArrayList<Integer>();

    public TaskCommandList() { }

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
    }

    // MySQL

    public void selectCommands(DBHelper dbHelper, DBInfo dbInfo) {
        synchronized (this) {
            if (dbHelper != null) {
                try {
                    DBHelper.DBHelperResult result;
                    result = dbHelper.queryStart("SELECT cid, command, UNIX_TIMESTAMP(timestamp) FROM " + dbInfo.getTable() + " WHERE timestamp <= now() LIMIT 0,10");
                    while (result.getResultSet().next()) {
                        ResultSet resultSet = result.getResultSet();
                        TaskCommand taskCommand = new TaskCommand();
                        taskCommand.setId(resultSet.getInt(1));
                        taskCommand.setCommand(resultSet.getString(2));
                        taskCommand.setTime(resultSet.getLong(3));
                        addCommand(taskCommand);
                    }
                    dbHelper.queryEnd(result);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // run in main thread!
    public void executeCommands() {
        synchronized (this) {
            CommandSender sender = Bukkit.getConsoleSender();
        /*long currentTime = System.currentTimeMillis() / 1000;
        if (taskCommandList.isExistsNowTasks(currentTime)) {
            List<TaskCommand> taskCommands = taskCommandList.getNowTasks(currentTime);
            for (TaskCommand command : taskCommands) {
                Bukkit.dispatchCommand(sender, command.getCommand());
            }
            taskCommandList.removeTasks(taskCommands);
            removeTasks(currentTime); // удаляем строки
        }*/
            for (TaskCommand command : taskCommands) {
                Bukkit.dispatchCommand(sender, command.getCommand());
                taskCommandsForDelete.add(command.getId());
            }
            taskCommands.clear();
        }
    }

    public void deleteCommands(DBHelper dbHelper, DBInfo dbInfo) {
        synchronized (this) {
            String strArray = StringUtils.join(taskCommandsForDelete, ',');
            if (dbHelper != null) {
                try {
                    dbHelper.queryUpdate("DELETE FROM " + dbInfo.getTable() + " WHERE cid IN (" + strArray + ")");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            taskCommandsForDelete.clear();
        }
    }

    @Override
    public String toString() {
        return "TaskCommandList{" +
                "taskCommands=" + taskCommands +
                '}';
    }
}
