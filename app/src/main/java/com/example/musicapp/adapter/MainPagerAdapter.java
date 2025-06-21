package com.example.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.musicapp.Fragment.HomeFragment;
import com.example.musicapp.Fragment.FavoritesFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    public MainPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new HomeFragment();
        else return new FavoritesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

