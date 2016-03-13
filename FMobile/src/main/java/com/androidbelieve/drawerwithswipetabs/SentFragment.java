package com.androidbelieve.drawerwithswipetabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SentFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.findViewById(R.id.search_btn).setOnClickListener(this);

        return inflater.inflate(R.layout.sent_layout,null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.search_btn:
                WebParser.DataSource.getQuickSearchResult("test");
                break;
        }
    }
}
