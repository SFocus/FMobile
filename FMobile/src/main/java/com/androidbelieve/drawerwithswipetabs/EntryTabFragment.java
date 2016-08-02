package com.androidbelieve.drawerwithswipetabs;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;

import models.VideoEntry;
import popups.CommentsPopup;
import popups.Description;
import popups.FilesPopup;
import popups.SimilarItemsTab;

/**
 * Created by svyatoslav on 31.07.16.
 */
public class EntryTabFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 4;
    protected String link;

    private VideoEntry entry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        Bundle bundle = getArguments();
        VideoEntry entry = (VideoEntry) bundle.getSerializable("entry");
        link = bundle.getString("link");

        assert entry != null;
        int_items = entry.getSimilarItems().size() == 0 ? 3 : 4;

        Drawable[] draw = {
                new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_info_circle).sizeDp(20).color(getResources().getColor(R.color.white)),
                new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_film).sizeDp(20).color(getResources().getColor(R.color.white)),
                new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_comments).sizeDp(20).color(getResources().getColor(R.color.white)),
                new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_folder_open).sizeDp(20).color(getResources().getColor(R.color.white))
        };
        // Set icon  tab
        for (int i = 0; i < int_items; i++) {
            tabLayout.addTab(tabLayout.newTab().setIcon(draw[i]));
        }

        /**
         *Set an Apater for the View Pager
         */

        CommentsPopup commentsPopup = new CommentsPopup();
        commentsPopup.setArguments(bundle);

        FilesPopup filesPopup = new FilesPopup();
        filesPopup.setArguments(bundle);

        SimilarItemsTab similarItemsTab = new SimilarItemsTab();
        similarItemsTab.setArguments(bundle);

        Description description = new Description();
        description.setArguments(bundle);

        MyAdapter adapter = new MyAdapter(getChildFragmentManager());
        adapter.addFragment(description);
        adapter.addFragment(filesPopup);
        adapter.addFragment(commentsPopup);
        adapter.addFragment(similarItemsTab);

        if (int_items == 3)
            adapter.mFragments.remove(3);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}
