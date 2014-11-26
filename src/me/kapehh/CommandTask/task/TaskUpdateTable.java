package me.kapehh.CommandTask.task;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import org.bukkit.scheduler.BukkitRunnable;

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

        DBHelper.DBHelperResult result;
        try {
            result = dbHelper.prepareQueryStart("SELECT * FROM ?", dbInfo.getTable());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
