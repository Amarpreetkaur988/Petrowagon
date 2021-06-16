package com.omninos.petrowagon.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.omninos.petrowagon.Fragments.CurrentOrdersFragment;
import com.omninos.petrowagon.Fragments.PastOrdersFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;


        switch (position){
            case 0:
                fragment=new CurrentOrdersFragment();
                break;
            case 1:
                fragment =new PastOrdersFragment();
                break;


        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
