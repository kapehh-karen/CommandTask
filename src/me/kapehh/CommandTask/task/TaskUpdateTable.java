package me.kapehh.CommandTask.task;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Karen on 26.11.2014.
 */
public class TaskUpdateTable extends BukkitRunnable {
    TaskCommandList taskCommandList = new TaskCommandList();
    DBHelper dbHelper;
    DBInfo dbInfo;
    int interval;
    int currentInterval;

    public TaskUpdateTable(DBHelper dbHelper, DBInfo dbInfo, int interval) {
        this.dbHelper = dbHelper;
        this.dbInfo = dbInfo;
        this.interval = interval;
        this.currentInterval = 0;
    }

    private void updateTable() {
        taskCommandList.clearCommands();
        if (dbHelper != null) {
            try {
                DBHelper.DBHelperResult result;
                result = dbHelper.queryStart("SELECT cid, command, UNIX_TIMESTAMP(timestamp) FROM " + dbInfo.getTable());
                while (result.getResultSet().next()) {
                    ResultSet resultSet = result.getResultSet();
                    TaskCommand taskCommand = new TaskCommand();
                    taskCommand.setId(resultSet.getInt(1));
                    taskCommand.setCommand(resultSet.getString(2));
                    taskCommand.setTime(resultSet.getLong(3));
                    taskCommandList.addCommand(taskCommand);
                }
                dbHelper.queryEnd(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeTasks(long timestamp) {
        if (dbHelper != null) {
            try {
                dbHelper.queryUpdate("DELETE FROM " + dbInfo.getTable() + " WHERE UNIX_TIMESTAMP(timestamp) <= " + timestamp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // Если текущий тик перевалил за нужный
        if (currentInterval >= interval) {
            updateTable(); // обновляем список
            currentInterval = 0;
        }

        CommandSender sender = Bukkit.getConsoleSender();
        long currentTime = System.currentTimeMillis() / 1000;
        if (taskCommandList.isExistsNowTasks(currentTime)) {
            List<TaskCommand> taskCommands = taskCommandList.getNowTasks(currentTime);
            for (TaskCommand command : taskCommands) {
                Bukkit.dispatchCommand(sender, command.getCommand());
            }
            taskCommandList.removeTasks(taskCommands);
            removeTasks(currentTime); // удаляем строки
        }

        currentInterval++;
    }
}
