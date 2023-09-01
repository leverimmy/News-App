package com.java.xiongzeen;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.xiongzeen.data.User;
import com.java.xiongzeen.service.DBManager;
import com.java.xiongzeen.service.MySQLiteOpenHelper;


public class MyApplication extends Application {
    private static Context context;
    private static BottomNavigationView bottomNavigationView;
    private static FragmentContainerView topFragmentContainer;
    public static User myUser;
    public static boolean newsPage = true;
    public static boolean searchPage = false;
    public static boolean userPage = false;
    public static boolean detailsPageFromNews = false;
    public static boolean detailsPageFromSearch = false;
    public static boolean detailsPageFromHistory = false;
    public static boolean detailsPageFromFavorite = false;
    public static boolean historyPage = false;
    public static boolean favoritePage = false;
    public static MySQLiteOpenHelper mySQLiteOpenHelper;
    public static DBManager dbManager;


    public static Context getContext(){
        return context;
    }

    public static BottomNavigationView getBottomNavigationView(){
        return bottomNavigationView;
    }

    public static FragmentContainerView getTopFragmentContainer() {
        return topFragmentContainer;
    }

    public static void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        MyApplication.bottomNavigationView = bottomNavigationView;
    }

    public static void setTopFragmentContainer(FragmentContainerView topFragmentContainer) {
        MyApplication.topFragmentContainer = topFragmentContainer;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        myUser = new User();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        dbManager = new DBManager();
    }
    @Override
    public void onTerminate() {
        DBManager.closeDB();
        super.onTerminate();
    }
}
