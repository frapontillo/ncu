package com.github.frapontillo.ncu.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.frapontillo.ncu.R;

public class DetailFragment extends Fragment {
    private TextView label;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        label = (TextView) inflater.inflate(R.layout.list_item_forecast, container);
        return label;
    }

    @Override
    public void onResume() {
        super.onResume();
        label.setText(getActivity().getIntent().getExtras().getString(Intent.EXTRA_TEXT));
    }
}
