package com.example.donttouchmyphone.views.activities.intro.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.donttouchmyphone.views.fragments.Intro1Fragment;
import com.example.donttouchmyphone.views.fragments.Intro2Fragment;
import com.example.donttouchmyphone.views.fragments.Intro3Fragment;

public class IntroAdapter extends FragmentStateAdapter {
    public IntroAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Intro1Fragment();
            case 1:
                return new Intro2Fragment();
            case 2:
                return new Intro3Fragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
