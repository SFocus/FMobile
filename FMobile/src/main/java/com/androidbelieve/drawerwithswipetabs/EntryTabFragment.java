package com.androidbelieve.drawerwithswipetabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import helpers.CustomTypefaceSpan;
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
    private Typeface font;

    private VideoEntry entry;

    public EntryTabFragment(String link, VideoEntry entry) {
        this.link = link;
        this.entry = entry;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Description(link);
                case 1:
                    return new FilesPopup(link);
                case 2:
                    return new CommentsPopup(link);
                case 3:
                    return new SimilarItemsTab(entry.getSimilarItems());
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Description";
                case 1:
                    return icon_giver(font, "f1b2");
                case 2:
                    return "Comments";
                case 3:
                    return "Similar";
            }
            return null;
        }
    }

    public CharSequence icon_giver(Typeface font, String icon_id) {
        String title_of_page = "";
        SpannableStringBuilder styled;
        title_of_page = icon_id;
        styled = new SpannableStringBuilder(title_of_page);
        styled.setSpan(new CustomTypefaceSpan("", font), 0, title_of_page.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return styled;
    }

}
