package popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidbelieve.drawerwithswipetabs.R;

import java.util.ArrayList;

import adapters.RecyclerBindingAdapter;
import helpers.GridAutofitLayoutManager;
import models.VideoEntry;

/**
 * Created by Andrew on 01.08.2016.
 */
public class SimilarItemsTab extends Fragment {

    private ArrayList<VideoEntry.SimilarItem> items;

    private RecyclerBindingAdapter adapter;

    private static final float GESTURE_THRESHOLD_DP = 170.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_similar_items, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        VideoEntry entry = (VideoEntry) bundle.getSerializable("entry");
        assert entry != null;
        items = entry.getSimilarItems();

        adapter = new RecyclerBindingAdapter<VideoEntry.SimilarItem>(R.layout.similar_item_card, com.androidbelieve.drawerwithswipetabs.BR.model, items);

        RecyclerView similarRecycler = (RecyclerView) view.findViewById(R.id.similar_recycler);

        final float scale = getResources().getDisplayMetrics().density;
        int mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        final GridAutofitLayoutManager gridAutofitLayoutManager = new GridAutofitLayoutManager(view.getContext(), mGestureThreshold);
        similarRecycler.setLayoutManager(gridAutofitLayoutManager);
        similarRecycler.setAdapter(adapter);
    }
}
