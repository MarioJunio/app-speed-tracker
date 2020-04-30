package mario.com.br.velocita.holder.data;

public class Time {

    private long seconds;
    private long minutes;
    private long hours;
    private long days;

    public Time(long seconds, long minutes, long hours, long days) {
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }
}
