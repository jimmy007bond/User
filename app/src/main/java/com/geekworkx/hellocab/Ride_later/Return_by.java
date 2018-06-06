package com.geekworkx.hellocab.Ride_later;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.geekworkx.hellocab.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parag on 06/03/18.
 */

public class Return_by extends AppCompatActivity{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private double My_lat=0,My_long=0;
    private String Unique_ID;
    private Bundle bundle;
    private int Pager=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.return_by);

        toolbar = (Toolbar) findViewById(R.id.toolbar_later_r);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager_r);

        tabLayout = (TabLayout) findViewById(R.id.tabr);
        tabLayout.setupWithViewPager(viewPager);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        Unique_ID=i.getStringExtra("Unique_ID");
        Pager=i.getIntExtra("pager",0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        bundle = new Bundle();
        bundle.putString("Unique_ID", Unique_ID);
        bundle.putInt("pager", Pager);
        setupViewPager(viewPager,bundle);



    }

    private void setupViewPager(ViewPager viewPager, Bundle bundle) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        LeaveOn fragobj = new LeaveOn();
        fragobj.setArguments(bundle);
        adapter.addFragment(fragobj, "Leave On");
        ReturnOn fragObj1=new ReturnOn();
        fragObj1.setArguments(bundle);
        adapter.addFragment(fragObj1,"Return On");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(Pager!=0){
            viewPager.setCurrentItem(Pager);
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_white, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh1:
                recreate();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
