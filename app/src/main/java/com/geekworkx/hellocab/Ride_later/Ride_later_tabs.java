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

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by parag on 18/02/18.
 */

public class Ride_later_tabs extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private double My_lat=0,My_long=0;
    private Bundle bundle;
    private int Pager=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_later_tab);

        toolbar = (Toolbar) findViewById(R.id.toolbar_later_tabs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager_tabs);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        Pager=i.getIntExtra("pager",0);

        bundle = new Bundle();
        bundle.putDouble("my_lat", My_lat);
        bundle.putDouble("my_long", My_long);
        setupViewPager(viewPager,bundle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(Ride_later_tabs.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();

            }
        });

        if(Pager!=0){
            viewPager.setCurrentItem(1);
        }

    }

    private void setupViewPager(ViewPager viewPager,Bundle bundle) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        PastFragment fragobj = new PastFragment();
        fragobj.setArguments(bundle);
        adapter.addFragment(fragobj, "Past");
        Ride_later_dates fragObj1=new Ride_later_dates();
        fragObj1.setArguments(bundle);
        adapter.addFragment(fragObj1,"Upcoming");
        viewPager.setAdapter(adapter);
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
        Intent i=new Intent(Ride_later_tabs.this, GooglemapApp.class);
        i.putExtra("my_lat", My_lat);
        i.putExtra("my_long", My_long);
        startActivity(i);
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
