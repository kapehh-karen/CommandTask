package me.kapehh.CommandTask.crontab;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Karen on 21.12.2014.
 */
public class CronTabExecuter extends BukkitRunnable {
    List<CronTabTask> cronTabTasks = new ArrayList<CronTabTask>();

    /*
        Calendar.DATE — получим дату
        Calendar.ERA — возвращает эру (до нашей эры или после)
        Calendar.YEAR — получим значение года
        Calendar.MONTH — get() возвратит число, равное номеру месяца
        Calendar.HOUR — часы
        Calendar.MINUTE — минуты
        Calendar.SECOND — секунды
        Calendar.MILLISECOND — миллисекунды
        Calendar.DAY_OF_YEAR — возвратит день в году
        Calendar.DAY_OF_MONTH — день месяца
        Calendar.DAY_OF_WEEK — порядковый номер дня в неделе
        Calendar.DAY_OF_WEEK_IN_MONTH — порядковый номер дня недели в текущем месяце
        Calendar.WEEK_OF_MONTH — номер недели в месяце
        Calendar.WEEK_OF_YEAR — номер недели в году
        Calendar.AM_PM — индикатор до обеда/после обеда
     */

    public void addCronTabTask(CronTabTask task) {
        cronTabTasks.add(task);
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();

        for (CronTabTask task : cronTabTasks) {

        }
    }
}
