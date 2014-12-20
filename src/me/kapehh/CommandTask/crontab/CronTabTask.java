package me.kapehh.CommandTask.crontab;

import java.util.HashSet;

/**
 * Created by Karen on 20.12.2014.
 */
public class CronTabTask {
    HashSet<Integer> seconds;
    HashSet<Integer> minutes;
    HashSet<Integer> hours;
    HashSet<Integer> days;
    HashSet<Integer> months;
    HashSet<Integer> days_of_week;
    String command;

    public CronTabTask() {

    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void addSecond(int second) {
        seconds.add(second);
    }

    public void addMinutes(int minute) {
        minutes.add(minute);
    }

    public void addHour(int hour) {
        hours.add(hour);
    }

    public void addDay(int day) {
        days.add(day);
    }

    public void addMonth(int month) {
        months.add(month);
    }

    public void addDayOfWeek(int dow) {
        days_of_week.add(dow);
    }

    public HashSet<Integer> getSeconds() {
        return seconds;
    }

    public void setSeconds(HashSet<Integer> seconds) {
        this.seconds = seconds;
    }

    public HashSet<Integer> getMinutes() {
        return minutes;
    }

    public void setMinutes(HashSet<Integer> minutes) {
        this.minutes = minutes;
    }

    public HashSet<Integer> getHours() {
        return hours;
    }

    public void setHours(HashSet<Integer> hours) {
        this.hours = hours;
    }

    public HashSet<Integer> getDays() {
        return days;
    }

    public void setDays(HashSet<Integer> days) {
        this.days = days;
    }

    public HashSet<Integer> getMonths() {
        return months;
    }

    public void setMonths(HashSet<Integer> months) {
        this.months = months;
    }

    public HashSet<Integer> getDays_of_week() {
        return days_of_week;
    }

    public void setDays_of_week(HashSet<Integer> days_of_week) {
        this.days_of_week = days_of_week;
    }

    @Override
    public String toString() {
        return "CronTabTask{" +
                "seconds=" + seconds +
                ", minutes=" + minutes +
                ", hours=" + hours +
                ", days=" + days +
                ", months=" + months +
                ", days_of_week=" + days_of_week +
                ", command='" + command + '\'' +
                '}';
    }
}
