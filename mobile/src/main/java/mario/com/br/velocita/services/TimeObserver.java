package mario.com.br.velocita.services;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import mario.com.br.velocita.holder.data.TimeObserverBus;
import mario.com.br.velocita.utils.Dates;

public class TimeObserver extends AsyncTask<Void, Void, Void> {

    private EventBus eventBus;
    private TimeObserverBus timeObserverBus = new TimeObserverBus();

    @Override
    protected void onPreExecute() {
        eventBus = EventBus.getDefault();
        timeObserverBus.setTimeHour(!Dates.isNight() ? TimeHour.DAY : TimeHour.NIGHT);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        TimeHour lastUpdate;

        while (true) {
            // verifica se é dia ou noite
            lastUpdate = !Dates.isNight() ? TimeHour.DAY : TimeHour.NIGHT;

            if (timeObserverBus.getTimeHour() == null || timeObserverBus.getTimeHour() != lastUpdate) {
                timeObserverBus.setTimeHour(lastUpdate);
                eventBus.post(new TimeObserverBus(lastUpdate));
            }

            // aguarda 10 segundos para verificar novamente
            try {
                Thread.sleep(10000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public enum TimeHour {
        DAY, NIGHT
    }
}
