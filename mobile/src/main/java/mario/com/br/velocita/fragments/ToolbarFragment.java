package mario.com.br.velocita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mario.com.br.velocita.R;

public class ToolbarFragment extends Fragment {

    private AppCompatActivity appCompatActivity;
    private Toolbar toolbar;

    public static ToolbarFragment newInstance() {
        ToolbarFragment fragment = new ToolbarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appCompatActivity = (AppCompatActivity) getActivity();
        toolbar = appCompatActivity.findViewById(R.id.toolbar);

        setToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toolbar, container, false);
    }

    public void onShowPerfil(View view) {

    }

    private void setToolbar() {
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
