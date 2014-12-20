package me.kapehh.CommandTask.crontab;

import java.io.*;

/**
 * Created by Karen on 20.12.2014.
 */
public class CronTabLoader {

    // запрещаем создавать класс этот
    private CronTabLoader() {

    }

    public static void load(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println('[' + line + ']');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
