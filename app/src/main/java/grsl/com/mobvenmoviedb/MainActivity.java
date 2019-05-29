package grsl.com.mobvenmoviedb;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import grsl.com.mobvenmoviedb.Adapters.ViewPagerAdapter;
import grsl.com.mobvenmoviedb.Fragments.TopRatedMoviesFragment;
import grsl.com.mobvenmoviedb.Fragments.NowPlayingMoviesFragment;
import grsl.com.mobvenmoviedb.Fragments.UpcomingMoviesFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mobven Movie DB");
        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        MenuItem searchButton = menu.findItem(R.id.action_search);
        searchButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TopRatedMoviesFragment(), "Top Rated");
        adapter.addFragment(new UpcomingMoviesFragment(), "Upcoming");
        adapter.addFragment(new NowPlayingMoviesFragment(), "Now Playing");
        viewPager.setAdapter(adapter);
    }

}
