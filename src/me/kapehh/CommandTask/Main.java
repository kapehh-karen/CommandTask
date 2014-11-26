package me.kapehh.CommandTask;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import me.kapehh.CommandTask.task.TaskUpdateTable;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

/**
 * Created by Karen on 26.11.2014.
 */
public class Main extends JavaPlugin implements CommandExecutor {
    private PluginConfig pluginConfig;
    private DBHelper dbHelper;
    private DBInfo dbInfo = new DBInfo();
    private TaskUpdateTable taskUpdateTable;

    @EventPluginConfig(EventType.LOAD)
    public void onConfigLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();

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

        dbHelper = new DBHelper(
            dbInfo.getIp(),
            dbInfo.getDb(),
            dbInfo.getLogin(),
            dbInfo.getPassword()
        );

        try {
            dbHelper.connect();
            getLogger().info("Success connect to MySQL!");
        } catch (SQLException e) {
            dbHelper = null;
            e.printStackTrace();
        }

        if (taskUpdateTable != null) {
            taskUpdateTable.cancel();
        }

        int tickUpdate = cfg.getInt("ticks.update", 1000);
        int tickInterval = cfg.getInt("ticks.interval", 1000);

        if (dbHelper != null) {
            taskUpdateTable = new TaskUpdateTable(dbHelper, dbInfo, tickInterval);
            taskUpdateTable.runTaskTimerAsynchronously(this, 50, tickUpdate);
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
