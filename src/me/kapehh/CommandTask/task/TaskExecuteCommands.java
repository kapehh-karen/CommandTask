package me.kapehh.CommandTask.task;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Karen on 29.11.2014.
 */
public class TaskExecuteCommands extends BukkitRunnable {
    TaskCommandList taskCommandList;
    DBHelper dbHelper;
    DBInfo dbInfo;

    public TaskExecuteCommands(TaskCommandList taskCommandList, DBHelper dbHelper, DBInfo dbInfo) {
        this.taskCommandList = taskCommandList;
        this.dbHelper = dbHelper;
        this.dbInfo = dbInfo;
    }

    @Override
    public void run() {
        // выполняем команды
        taskCommandList.executeCommands();

        System.out.println("TICK RUN");
    }
}
