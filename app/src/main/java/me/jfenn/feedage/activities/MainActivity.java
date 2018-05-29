package me.jfenn.feedage.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.fragments.BaseFragment;
import me.jfenn.feedage.fragments.CategoriesFragment;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;

public class MainActivity extends AppCompatActivity implements FeedageLib.OnCategoriesUpdatedListener, FragmentManager.OnBackStackChangedListener {

    private Feedage feedage;
    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedage = (Feedage) getApplicationContext();

        if (savedInstanceState == null) {
            fragment = new CategoriesFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, fragment)
                    .commit();
        } else {
            if (fragment == null)
                fragment = new CategoriesFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        feedage.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        feedage.removeListener(this);
    }

    @Override
    public void onFeedsUpdated(List<FeedData> feeds) {

    }

    @Override
    public void onCategoriesUpdated(List<CategoryData> categories) {

    }

    @Override
    public void onBackStackChanged() {
        fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }
}