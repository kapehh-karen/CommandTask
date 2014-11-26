package me.kapehh.CommandTask.task;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Karen on 26.11.2014.
 */
public class TaskUpdateTable extends BukkitRunnable {
    TaskCommandList taskCommandList = new TaskCommandList();
    DBHelper dbHelper;
    DBInfo dbInfo;

    public TaskUpdateTable(DBHelper dbHelper, DBInfo dbInfo) {
        this.dbHelper = dbHelper;
        this.dbInfo = dbInfo;
    }

    @Override
    public void run() {
        taskCommandList.clearCommands();

        if (dbHelper != null) {
            try {
                DBHelper.DBHelperResult result;
                result = dbHelper.prepareQueryStart("SELECT `id`, `command`, UNIX_TIMESTAMP(`timestamp`) FROM ?", dbInfo.getTable());
                while (result.getResultSet().next()) {
                    ResultSet resultSet = result.getResultSet();
                    TaskCommand taskCommand = new TaskCommand();
                    taskCommand.setId(resultSet.getInt("cid"));
                    taskCommand.setCommand(resultSet.getString("command"));
                    taskCommand.setTime(resultSet.getLong("timestamp"));
                    taskCommandList.addCommand(taskCommand);
                }
                dbHelper.queryEnd(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("FROM DB: " + taskCommandList);
    }
}
