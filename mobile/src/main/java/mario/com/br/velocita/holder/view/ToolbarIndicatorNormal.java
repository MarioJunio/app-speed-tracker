package mario.com.br.velocita.holder.view;

import android.view.View;
import android.widget.TextView;

import mario.com.br.velocita.R;

public class ToolbarIndicatorNormal {

    public View view;
    public TextView tvMaxSpeed;
    public TextView tvSpeedUnit;

    public ToolbarIndicatorNormal(View view) {
        this.view = view;
        this.tvMaxSpeed = view.findViewById(R.id.var_max_speed);
        this.tvSpeedUnit = view.findViewById(R.id.var_speed_unit);
    }

    public void show() {
        //TODO: Carregar essas informações do SharedPrefs
        this.tvMaxSpeed.setText("0");
        this.tvSpeedUnit.setText("km/h");
        this.view.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.view.setVisibility(View.GONE);
    }

}
