package mario.com.br.velocita.holder.data;

import mario.com.br.velocita.services.TimeObserver;

public class TimeObserverBus {

    private TimeObserver.TimeHour timeHour;

    public TimeObserverBus() {
    }

    public TimeObserverBus(TimeObserver.TimeHour timeHour) {
        this.timeHour = timeHour;
    }

    public TimeObserver.TimeHour getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(TimeObserver.TimeHour timeHour) {
        this.timeHour = timeHour;
    }
}
