package com.java.xiongzeen;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;

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
import com.java.xiongzeen.ui.SelectPaddleFragment;
import com.java.xiongzeen.ui.TabListFragment;
import com.java.xiongzeen.ui.UserPageFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements TabListFragment.onTabBarListener,
        SelectPaddleFragment.onSelectPaddleListener, SearchFragment.OnSearchInputFinished{

    private ActivityMainBinding binding;
    public BottomNavigationView navView;
    public SearchView searchView;
    public FragmentContainerView mainArea;
    public FragmentContainerView tabs;
    private SelectPaddleFragment selectPaddleFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private TabListFragment tabListFragment;
    private NewsListFragment newsListFragment;



    @Override
    public void selectPaddleConfirmed(){
        drawerLayout.closeDrawer(Gravity.LEFT);

        tabListFragment.update_list();

    }

    @Override
    public void menuBarClicked() {
        Log.d("MainActivity", "menu clicked");
        drawerLayout.openDrawer(Gravity.LEFT);

    }

    @Override
    public void tabSelected(String tag) {
        Log.d("MainActivity", "menu tab"+ tag);
        FetchFromAPIManager.getInstance().setCategory(tag);
        newsListFragment.reloadNews();
    }

    @Override
    public void reportCurrent(List<Category> selected, List<Category> unselected) {
        MyApplication.myUser.selected = selected;
        MyApplication.myUser.unselected = unselected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //这个函数来自2022年科协暑培的代码
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navView = binding.navView;

        tabs = binding.fragmentContainerup;
        mainArea = binding.fragmentContainer;
        navView.setOnItemSelectedListener(this::onNavItemSelected);
        MyApplication.setBottomNavigationView(navView);

        mainArea.setLongClickable(true);

        fragmentManager = getSupportFragmentManager();
        selectPaddleFragment = (SelectPaddleFragment) fragmentManager.findFragmentById(R.id.select_paddle);

        tabListFragment = (TabListFragment) fragmentManager.findFragmentByTag("upper_fragment_in_container");
        newsListFragment = (NewsListFragment) fragmentManager.findFragmentByTag("fragment_in_container");

        MyApplication.setTopFragmentContainer(tabs);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.LEFT);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
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

    private boolean onNavItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.posts) {

            if(!MyApplication.newsPage)
                replaceFragment(NewsListFragment.class);
            MyApplication.newsPage = true;
            MyApplication.searchPage = false;
            MyApplication.userPage = false;

            tabs.setVisibility(View.VISIBLE);

            if (MyApplication.newsPageIsSearchingPage) {
                newsListFragment.reloadNews();
                FetchFromAPIManager.reset();
            }
            MyApplication.newsPageIsSearchingPage = false;

            return true;
        } else if (item.getItemId() == R.id.user) {
            if(! MyApplication.userPage) replaceFragment(UserPageFragment.class);
            MyApplication.newsPage = false;
            MyApplication.searchPage = false;
            MyApplication.userPage = true;

            tabs.setVisibility(View.GONE);

            return true;
        } else if (item.getItemId() == R.id.search) {
            if(!MyApplication.searchPage)
                replaceFragment(SearchFragment.class);
            MyApplication.newsPage = false;
            MyApplication.searchPage = true;
            MyApplication.userPage = false;

            tabs.setVisibility(View.GONE);

           return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (MyApplication.newsPage) {
            MyApplication.getBottomNavigationView().setVisibility(View.GONE);
            MyApplication.getTopFragmentContainer().setVisibility(View.GONE);
        }*/
    }

    @Override
    public void finished() {
        if(! MyApplication.newsPage) replaceFragment(NewsListFragment.class);
        Log.d("finished searching Input", "newsPageIsSearchingPage = true");
        MyApplication.newsPage = true;
        MyApplication.searchPage = false;
        MyApplication.userPage = false;
        MyApplication.newsPageIsSearchingPage = true;
        newsListFragment.reloadNews();
    }
}