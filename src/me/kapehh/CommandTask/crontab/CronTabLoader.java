package me.kapehh.CommandTask.crontab;

import java.io.*;
import java.util.HashSet;

/**
 * Created by Karen on 20.12.2014.
 */
public class CronTabLoader {

    // запрещаем создавать класс этот
    private CronTabLoader() {

    }

    /*
        # Формат файла:
        #  s m h dom mon dow command
        # где:
        #  s - секунда
        #  m - минута
        #  h - час
        #  dom - день
        #  mon - месяц
        #  dow - день недели (1 - вс, ... 7 - сб)
        #  command - команда выполняемая от имени консоли

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

    private static HashSet<Integer> parseInterval(String interval) {
        HashSet<Integer> retSet = new HashSet<Integer>();

        // TODO

        return retSet;
    }

    public static void load(File file) {
        try {
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line, cmdLine;

            while ((line = reader.readLine()) != null) {
                cmdLine = line.trim();
                if (cmdLine.length() == 0) continue; // если пустая строка

                byte[] cmdBytes = cmdLine.getBytes();
                int i, c, r;
                for (i = 0, c = 0, r = -1; i < cmdBytes.length; i++) {
                    if (cmdBytes[i] == ' ') {
                        c++;
                        if (c >= 6) {
                            r = i + 1;
                            break;
                        }
                    }
                }
                if (r < 0) continue; // если нужное количество пробелов не найдено

                String command = cmdLine.substring(r);
                String time = cmdLine.substring(0, r - 1);

                String arrTime[] = time.split(" ");
                CronTabTask cronTabTask = new CronTabTask();
                cronTabTask.setSeconds(parseInterval(arrTime[0]));
                cronTabTask.setMinutes(parseInterval(arrTime[1]));
                cronTabTask.setHours(parseInterval(arrTime[2]));
                cronTabTask.setDays(parseInterval(arrTime[3]));
                cronTabTask.setMonths(parseInterval(arrTime[4]));
                cronTabTask.setDays_of_week(parseInterval(arrTime[5]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
