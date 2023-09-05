package com.java.xiongzeen;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.xiongzeen.data.Category;
import com.java.xiongzeen.databinding.ActivityMainBinding;
import com.java.xiongzeen.service.FetchFromAPIManager;
import com.java.xiongzeen.ui.NewsListFragment;
import com.java.xiongzeen.ui.SearchFragment;
import com.java.xiongzeen.ui.SearchListFragment;
import com.java.xiongzeen.ui.SelectPaddleFragment;
import com.java.xiongzeen.ui.TabListFragment;
import com.java.xiongzeen.ui.UserPageFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements TabListFragment.onTabBarListener,
        SelectPaddleFragment.onSelectPaddleListener, SearchFragment.OnSearchInputFinished{

    private ActivityMainBinding binding;
    public BottomNavigationView navView;
    public FragmentContainerView mainArea;
    public FragmentContainerView mainArea2;
    public FragmentContainerView tabs;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private TabListFragment tabListFragment;
    public NewsListFragment newsListFragment;
    public SearchListFragment searchListFragment;
    private long firstTime = -1L;


    @Override
    public void selectPaddleConfirmed() {

        drawerLayout.closeDrawer(Gravity.LEFT);
        tabListFragment.updateList();
    }

    @Override
    public void menuBarClicked() {

        Log.d("MainActivity", "menu clicked");
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void tabSelected(String tag) {

        Log.d("MainActivity", "menu tab: " + tag);
        FetchFromAPIManager.getInstance().setCategory(tag);
        newsListFragment.reloadNews();
    }

    @Override
    public void reportCurrent(List<Category> selected, List<Category> unselected) {
        MyApplication.myUser.selected = selected;
        MyApplication.myUser.unselected = unselected;
        MyApplication.myUser.writeSelectPreference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //这个函数来自2022年科协暑培的代码
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navView = binding.navView;

        tabs = binding.fragmentContainerUp;
        mainArea = binding.fragmentContainer;
        mainArea2 = binding.fragmentContainer2;
        navView.setOnItemSelectedListener(this::onNavItemSelected);
        MyApplication.setBottomNavigationView(navView);

        mainArea.setLongClickable(true);

        fragmentManager = getSupportFragmentManager();

        tabListFragment = (TabListFragment) fragmentManager.findFragmentByTag("upper_fragment_in_container");
        newsListFragment = (NewsListFragment) fragmentManager.findFragmentByTag("fragment_in_container");
        searchListFragment = (SearchListFragment) fragmentManager.findFragmentByTag("fragment_in_container2");

        hideSearchList();

        MyApplication.setTopFragmentContainer(tabs);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Log.d("MainActivity", "onDrawerOpened");
                
                MyApplication.newsPage = false;
                MyApplication.selectPage = true;
                tabs.setVisibility(View.INVISIBLE);
                mainArea.setVisibility(View.INVISIBLE);
                navView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                Log.d("MainActivity", "onDrawerClosed");

                MyApplication.selectPage = false;
                MyApplication.newsPage = true;
                tabs.setVisibility(View.VISIBLE);
                mainArea.setVisibility(View.VISIBLE);
                navView.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void replaceFragment(Class<? extends Fragment> fragmentClass) { //这个函数来自2022年科协暑培的代码
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null) //代表支持不同的返回栈
                .commit();
    }

    private boolean onNavItemSelected(MenuItem item) {

    firstTime = -1;
    if (item.getItemId() == R.id.posts) {

        if(!MyApplication.newsPage) {
            replaceFragment(NewsListFragment.class);
            hideSearchList();
        }
        MyApplication.newsPage = true;
        MyApplication.searchPage = false;
        MyApplication.userPage = false;
        MyApplication.detailsPageFromNews = false;
        MyApplication.resultPage = false;
        MyApplication.historyPage = false;
        MyApplication.favoritePage = false;
        MyApplication.selectPage = false;

        tabs.setVisibility(View.VISIBLE);

        if (MyApplication.detailsPageFromSearch
                || MyApplication.detailsPageFromHistory
                || MyApplication.detailsPageFromFavorite) {
            newsListFragment.reloadNews();
            FetchFromAPIManager.reset();
        }
        MyApplication.detailsPageFromSearch = false;
        MyApplication.detailsPageFromHistory = false;
        MyApplication.detailsPageFromFavorite = false;

        return true;
    } else if (item.getItemId() == R.id.search) {

        if(!MyApplication.searchPage)
            replaceFragment(SearchFragment.class);
        MyApplication.newsPage = false;
        MyApplication.searchPage = true;
        MyApplication.userPage = false;
        MyApplication.detailsPageFromNews = false;
        MyApplication.detailsPageFromSearch = false;
        MyApplication.detailsPageFromHistory = false;
        MyApplication.detailsPageFromFavorite = false;
        MyApplication.resultPage = false;
        MyApplication.historyPage = false;
        MyApplication.favoritePage = false;
        MyApplication.selectPage = false;

        tabs.setVisibility(View.GONE);

        return true;
    } else if (item.getItemId() == R.id.user) {

        if(!MyApplication.userPage)
            replaceFragment(UserPageFragment.class);
        MyApplication.newsPage = false;
        MyApplication.searchPage = false;
        MyApplication.userPage = true;
        MyApplication.detailsPageFromNews = false;
        MyApplication.detailsPageFromSearch = false;
        MyApplication.detailsPageFromHistory = false;
        MyApplication.detailsPageFromFavorite = false;
        MyApplication.resultPage = false;
        MyApplication.historyPage = false;
        MyApplication.favoritePage = false;
        MyApplication.selectPage = false;

        tabs.setVisibility(View.GONE);

        return true;
    }
    return false;
}

    private void hideSearchList() {
        mainArea2.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .hide(searchListFragment).commit();
    }

    private void showSearchList() {
        mainArea2.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .show(searchListFragment).commit();
    }

    @Override
    public void finished() {
        replaceFragment(SearchListFragment.class);

        showSearchList();

        Log.d("finished searching Input", "resultPage = true");
        MyApplication.newsPage = false;
        MyApplication.searchPage = false;
        MyApplication.userPage = false;
        MyApplication.detailsPageFromNews = false;
        MyApplication.detailsPageFromSearch = false;
        MyApplication.detailsPageFromHistory = false;
        MyApplication.detailsPageFromFavorite = false;
        MyApplication.resultPage = true;
        MyApplication.historyPage = false;
        MyApplication.favoritePage = false;
        MyApplication.selectPage = false;

        searchListFragment.reloadNews();
    }

    @Override
    public void onBackPressed() {

        Log.d("MainActivity", "onBackPressed");
        Log.d("MainActivity", "news? " + MyApplication.newsPage);
        Log.d("MainActivity", "search? " + MyApplication.searchPage);
        Log.d("MainActivity", "user? " + MyApplication.userPage);
        Log.d("MainActivity", "detailsFromNews? " + MyApplication.detailsPageFromNews);
        Log.d("MainActivity", "detailsPageFromSearch? " + MyApplication.detailsPageFromSearch);
        Log.d("MainActivity", "detailsPageFromHistory? " + MyApplication.detailsPageFromHistory);
        Log.d("MainActivity", "detailsPageFromFavorite? " + MyApplication.detailsPageFromFavorite);
        Log.d("MainActivity", "result?" + MyApplication.resultPage);
        Log.d("MainActivity", "history? " + MyApplication.historyPage);
        Log.d("MainActivity", "favorite? " + MyApplication.favoritePage);
        Log.d("MainActivity", "select? " + MyApplication.selectPage);

        if (MyApplication.detailsPageFromNews) {

            MyApplication.detailsPageFromNews = false;
            MyApplication.newsPage = true;
            super.onBackPressed();

        } else if (MyApplication.detailsPageFromSearch) {

            MyApplication.detailsPageFromSearch = false;
            MyApplication.resultPage = true;
            super.onBackPressed();

        } else if (MyApplication.detailsPageFromHistory) {

            MyApplication.detailsPageFromHistory = false;
            MyApplication.historyPage = true;
            super.onBackPressed();

        } else if (MyApplication.detailsPageFromFavorite) {

            MyApplication.detailsPageFromFavorite = false;
            MyApplication.favoritePage = true;
            super.onBackPressed();

        } else if (MyApplication.resultPage) {

            MyApplication.resultPage = false;
            MyApplication.searchPage = true;
            super.onBackPressed();

        } else if (MyApplication.historyPage) {

            MyApplication.historyPage = false;
            MyApplication.userPage = true;
            super.onBackPressed();

        } else if (MyApplication.favoritePage) {

            MyApplication.favoritePage = false;
            MyApplication.userPage = true;
            super.onBackPressed();

        } else if (MyApplication.selectPage) {

            drawerLayout.closeDrawer(Gravity.LEFT);

        } else {

            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime >= 2000) {
                Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {
                finish();
            }

        }
    }
}