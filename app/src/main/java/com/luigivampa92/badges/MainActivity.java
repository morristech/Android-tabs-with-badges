package com.luigivampa92.badges;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.luigivampa92.badges.badges.Badge;
import com.luigivampa92.badges.badges.BadgeSpan;
import com.luigivampa92.badges.badges.BadgeType;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Setting custom layout here. I didn't manage to understand why
        // you cannot just set spannable string to tab's default tabTitle
        for (int i = 0; i < tabLayout.getTabCount(); ++i) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            // we set no badge (null) on screen creation
            tab.setCustomView(tabsAdapter.getTabView(i, null));
        }
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    /**
     * Sets given Badge object to tab title by index. Badge contains the number we want to show
     */
    public void setTabBadge(int tabIndex, Badge badge) {
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        tab.setCustomView(null);
        tab.setCustomView(tabsAdapter.getTabView(tabIndex, badge));
    }

    /**
     * Gets current number in badge by tab index
     */
    public Integer getTabBadge(int index) {
        return tabsAdapter.getBadgeValue(index);
    }

    /**
     * Gets BadgeSpan object by BadgeType enum
     */
    public BadgeSpan getBadgeSpanByType(BadgeType badgeType) {
        return tabsAdapter.getBadgeSpanByType(badgeType);
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        private static final String BADGE_MARGIN = " ";
        private String[] tabTitles = { "INCOMING", "CURRENT", "COMPLETED" };
        private int pageCount = 3;
        private Map<Integer, Integer> badgeValues;

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
            badgeValues = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        /**
         *               (!!!!!!!!!)
         * That's where all the important work happens.
         * The trick is to set a SpannableString to a TextView, and make the required background at certain characters
         * Note that I used custom span that extends ReplacementSpan. The ImageSpan would not work correctly
         * at android 5.0+, but we can draw any necessary shape in replacement span, and it will work fine on older version of android
         */
        private View getTabView(int position, Badge badge) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.badged_tab, null);
            TextView tabText = (TextView) v.findViewById(R.id.tab_text);

            if (badge != null && badge.isActual()) {
                    String badgeText = badge.getBadgeText();
                    String tabTitle = tabTitles[position] + BADGE_MARGIN;
                    tabText.setText(tabTitle + badgeText, TextView.BufferType.SPANNABLE);

                    Spannable spannable = (Spannable) tabText.getText();
                    spannable.setSpan(badge.getSpan(), tabTitle.length(), tabTitle.length() + badgeText.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    badgeValues.put(position, badge.getNumber());
            }
            else {
                tabText.setText(tabTitles[position]);
                badgeValues.put(position, 0);
            }

            return v;
        }

        public BadgeSpan getBadgeSpanByType(BadgeType badgeType) {
            if (BadgeType.BRIGHT.equals(badgeType)) {
                int colorBadge = ContextCompat.getColor(MainActivity.this, R.color.colorAccent);
                int colorText = ContextCompat.getColor(MainActivity.this, R.color.colorWhite);
                return new BadgeSpan(colorBadge, colorText, 25);
            }
            else if (BadgeType.FAINT.equals(badgeType)) {
                int colorBadge = ContextCompat.getColor(MainActivity.this, R.color.colorWhite);
                int colorText = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                return new BadgeSpan(colorBadge, colorText, 25);
            }

            return null;
        }

        public Integer getBadgeValue(int index) {
            return badgeValues.get(index);
        }

        @Override
        public int getCount() {
            return this.pageCount;
        }
    }
}
