package mario.com.br.velocita.holder.view;

import android.view.View;
import android.widget.TextView;

import mario.com.br.velocita.R;

public class ToolbarIndicatorProgress {

    public View view;
    public TextView tvHours;
    public TextView tvMinutes;
    public TextView tvSeconds;

    public ToolbarIndicatorProgress(View view) {
        this.view = view;
        this.tvHours = view.findViewById(R.id.var_hours);
        this.tvMinutes = view.findViewById(R.id.var_minutes);
        this.tvSeconds = view.findViewById(R.id.var_seconds);
    }

    public void show() {
        this.tvHours.setText("00");
        this.tvMinutes.setText("00");
        this.tvMinutes.setText("00");
        this.view.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.view.setVisibility(View.GONE);
    }
}
