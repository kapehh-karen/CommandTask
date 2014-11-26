package me.kapehh.CommandTask;

import me.kapehh.CommandTask.db.DBHelper;
import me.kapehh.CommandTask.db.DBInfo;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

/**
 * Created by Karen on 26.11.2014.
 */
public class Main extends JavaPlugin {
    private PluginConfig pluginConfig;
    private DBHelper dbHelper;
    private DBInfo dbInfo = new DBInfo();

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
        getLogger().info("Complete!");
    }

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

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
