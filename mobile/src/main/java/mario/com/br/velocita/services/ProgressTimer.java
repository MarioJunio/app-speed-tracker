package mario.com.br.velocita.services;

import android.os.Handler;

import mario.com.br.velocita.activities.DashboardActivity;
import mario.com.br.velocita.holder.Data;
import mario.com.br.velocita.holder.data.Time;
import mario.com.br.velocita.holder.view.ToolbarIndicatorProgress;
import mario.com.br.velocita.utils.Converters;

public class ProgressTimer {

    private Data data;
    private Handler handler = new Handler();
    private ToolbarIndicatorProgress toolbarIndicatorProgress;

    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {

            if (data.getStartTime() != 0 && data.isProgress()) {
                Time time = Converters.millisToTime(System.currentTimeMillis() - data.getStartTime());

                setSeconds(time.getSeconds());
                setMinutes(time.getMinutes());
                setHours(time.getHours());

                handler.postDelayed(runnable, Converters.MILLI_IN_SECOND);
            }

        }
    };

    private ProgressTimer(ToolbarIndicatorProgress toolbarIndicatorProgress) {
        this.data = DashboardActivity.data;
        this.toolbarIndicatorProgress = toolbarIndicatorProgress;
    }

    public static ProgressTimer newInstance(ToolbarIndicatorProgress toolbarIndicatorProgress) {
        return new ProgressTimer(toolbarIndicatorProgress);
    }

    public void start() {
        setSeconds(0);
        setMinutes(0);
        setHours(0);

        handler.postDelayed(runnable, Converters.MILLI_IN_SECOND);
    }

    private void setSeconds(long seconds) {
        toolbarIndicatorProgress.tvSeconds.setText(String.format("%02d", seconds));
    }

    private void setMinutes(long minutes) {
        toolbarIndicatorProgress.tvMinutes.setText(String.format("%02d", minutes));
    }

    private void setHours(long hours) {
        toolbarIndicatorProgress.tvHours.setText(String.format("%02d", hours));
    }
}
