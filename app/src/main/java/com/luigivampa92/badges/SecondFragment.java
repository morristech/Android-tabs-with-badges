package com.luigivampa92.badges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.luigivampa92.badges.badges.Badge;
import com.luigivampa92.badges.badges.BadgeSpan;
import com.luigivampa92.badges.badges.BadgeType;

public class SecondFragment extends Fragment {

    private static final int BADGE_INDEX = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, null);

        Button buttonIncrease = (Button) view.findViewById(R.id.button_badge_increase);
        Button buttonDecrease = (Button) view.findViewById(R.id.button_badge_decrease);
        Button buttonClear = (Button) view.findViewById(R.id.button_badge_clear);

        buttonIncrease.setOnClickListener(increaseListener);
        buttonDecrease.setOnClickListener(decreaseListener);
        buttonClear.setOnClickListener(clearListener);

        return view;
    }

    View.OnClickListener increaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity acivity = (MainActivity) getActivity();

            Integer tabBadge = acivity.getTabBadge(BADGE_INDEX);
            BadgeSpan badgeSpan = acivity.getBadgeSpanByType(BadgeType.FAINT);
            Badge badge = new Badge(tabBadge != null ? tabBadge + 1 : 1, badgeSpan);

            acivity.setTabBadge(BADGE_INDEX, badge);
        }
    };

    View.OnClickListener decreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity acivity = (MainActivity) getActivity();

            Integer tabBadge = acivity.getTabBadge(BADGE_INDEX);
            BadgeSpan badgeSpan = acivity.getBadgeSpanByType(BadgeType.FAINT);
            Badge badge = new Badge(tabBadge != null ? tabBadge - 1 : 0, badgeSpan);

            acivity.setTabBadge(BADGE_INDEX, badge);
        }
    };

    View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity acivity = (MainActivity) getActivity();
            acivity.setTabBadge(BADGE_INDEX, null);
        }
    };
}
