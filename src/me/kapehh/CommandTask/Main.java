package me.kapehh.CommandTask;

import me.kapehh.CommandTask.crontab.CronTabExecuter;
import me.kapehh.CommandTask.crontab.CronTabLoader;
import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import me.kapehh.CommandTask.task.TaskCommandList;
import me.kapehh.CommandTask.task.TaskExecuteCommands;
import me.kapehh.CommandTask.task.TaskUpdateCommands;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import me.kapehh.main.pluginmanager.constants.ConstantSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Karen on 26.11.2014.
 */
public class Main extends JavaPlugin implements CommandExecutor {
    TaskCommandList taskCommandList = new TaskCommandList();
    PluginConfig pluginConfig;
    DBHelper dbHelper;
    DBInfo dbInfo = new DBInfo();
    TaskUpdateCommands taskUpdateCommands;
    TaskExecuteCommands taskExecuteCommands;
    CronTabExecuter cronTabExecuter;

    @EventPluginConfig(EventType.LOAD)
    public void onConfigLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();
        boolean isEnabled = cfg.getBoolean("connect.enabled");
        boolean isCrontab = cfg.getBoolean("crontab.enabled");

        if (dbHelper != null) {
            try {
                dbHelper.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // DBInfo не переслздаю, не обязательно
        dbInfo.setIp(cfg.getString("connect.ip", ""));
        dbInfo.setDb(cfg.getString("connect.db", ""));
        dbInfo.setLogin(cfg.getString("connect.login", ""));
        dbInfo.setPassword(cfg.getString("connect.password", ""));
        dbInfo.setTable(cfg.getString("connect.table", ""));

        // коннектимся
        try {
            if (isEnabled) {
                // создаем экземпляр класса для соединения с БД
                dbHelper = new DBHelper(
                        dbInfo.getIp(),
                        dbInfo.getDb(),
                        dbInfo.getLogin(),
                        dbInfo.getPassword()
                );

                dbHelper.connect();
                getLogger().info("Success connect to MySQL!");
            } else {
                dbHelper = null;
            }
        } catch (SQLException e) {
            dbHelper = null;
            e.printStackTrace();
        }

        // на всякий случай чистим мусор
        taskCommandList.clearCommands();

        // закрываем потоки, если они работают
        if (taskUpdateCommands != null) {
            taskUpdateCommands.cancel();
            taskUpdateCommands = null;
        }
        if (taskExecuteCommands != null) {
            taskExecuteCommands.cancel();
            taskExecuteCommands = null;
        }

        // оставнавливаем кронтабик
        if (cronTabExecuter != null) {
            cronTabExecuter.cancel();
            cronTabExecuter = null;
        }

        if (isEnabled && dbHelper != null) {
            // первое - тики для обновления таблицы, второе - тики для вызова команд
            int tickUpdate = cfg.getInt("ticks.update", 1000);
            int tickExecute = cfg.getInt("ticks.execute", 300);

            taskUpdateCommands = new TaskUpdateCommands(taskCommandList, dbHelper, dbInfo);
            taskUpdateCommands.runTaskTimerAsynchronously(this, 0, tickUpdate);

            taskExecuteCommands = new TaskExecuteCommands(taskCommandList, dbHelper, dbInfo);
            taskExecuteCommands.runTaskTimer(this, 0, tickExecute);
        }

        // ого, есть кронтабчик :3
        if (isCrontab) {
            String fileName = cfg.getString("crontab.filename", null);

            if (fileName != null) {
                if (!getDataFolder().exists()) getDataFolder().mkdirs();
                cronTabExecuter = new CronTabExecuter();
                CronTabLoader.load(cronTabExecuter, new File(getDataFolder(), fileName));
                cronTabExecuter.runTaskTimer(this, 0, ConstantSystem.ticksPerSec);
            }
        }

        getLogger().info("Complete!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (!sender.isOp()) {
            sender.sendMessage("You need OP!");
            return true;
        }

        String cmd = args[0];
        if (cmd.equalsIgnoreCase("reload")) {
            pluginConfig.loadData();
            return true;
        }

        return false;
    }

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("commandtask").setExecutor(this);

        pluginConfig = new PluginConfig(this);
        pluginConfig.addEventClasses(this).setup().loadData();
    }

    /*public static void main(String[] argv) {
        CronTabLoader.load(new File("D:\\Minecraft\\Spigot-179\\plugins\\CommandTask\\tasks.txt"));
    }*/

    @Override
    public void onDisable() {
        if (dbHelper != null) {
            try {
                dbHelper.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
