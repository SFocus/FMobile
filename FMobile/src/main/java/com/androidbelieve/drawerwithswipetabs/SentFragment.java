package com.androidbelieve.drawerwithswipetabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SentFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sent_layout,
                container, false);
        Button btn = (Button) view.findViewById(R.id.search);
        btn.setOnClickListener(onClickListener);
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.search:
                    Toast.makeText(getActivity().getApplicationContext(),"Hello", Toast.LENGTH_LONG).show();
                    WebParser.DataSource.getQuickSearchResult("test");
                    break;
            }
        }
    };


}
