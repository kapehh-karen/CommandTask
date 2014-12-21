package me.kapehh.CommandTask.crontab;

import java.io.*;
import java.util.Arrays;
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
     */

    public static HashSet<Integer> parseInterval(String interval) {
        HashSet<Integer> retSet = new HashSet<Integer>();

        String[] tmpa, tmpb, lst = interval.split(",");
        int a, b, c;
        for (String t : lst) {
            if (t.matches("\\d+")) {
                a = Integer.parseInt(t);
                retSet.add(a);
            } else if (t.matches("\\d+-\\d+")) {
                tmpa = t.split("-");
                a = Integer.parseInt(tmpa[0]);
                b = Integer.parseInt(tmpa[1]);
                for (int i = a; i <= b; i++) retSet.add(i);
            } else if (t.matches("\\d+-\\d+/\\d+")) {
                tmpa = t.split("/");
                tmpb = tmpa[0].split("-");
                a = Integer.parseInt(tmpb[0]);
                b = Integer.parseInt(tmpb[1]);
                c = Integer.parseInt(tmpa[1]);
                for (int i = a; i <= b; i += c) retSet.add(i);
            } else if (t.equals("*")) {
                retSet.add(CronTabTask.EVERY);
            }
        }

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
                cronTabTask.setCommand(command);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
