package com.example.donttouchmyphone.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.donttouchmyphone.intro.Fragment_Intro1;
import com.example.donttouchmyphone.intro.Fragment_Intro2;
import com.example.donttouchmyphone.intro.Fragment_Intro3;

public class IntroAdapter extends FragmentStateAdapter {
    public IntroAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_Intro1();
            case 1:
                return new Fragment_Intro2();
            case 2:
                return new Fragment_Intro3();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
