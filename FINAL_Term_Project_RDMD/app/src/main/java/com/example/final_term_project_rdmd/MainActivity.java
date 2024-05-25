package com.example.final_term_project_rdmd;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.final_term_project_rdmd.databinding.ActivityMainBinding;
import com.example.final_term_project_rdmd.ui.custom.CustomFragment;
import com.example.final_term_project_rdmd.ui.rank.RankFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_camera, R.id.navigation_home,
                R.id.navigation_custom, R.id.navigation_rank)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // MainActivity에서 바텀 네비게이션 이벤트를 처리할 때 NavHostFragment를 사용
        NavigationUI.setupWithNavController(navView, navController);

        // ActionBar를 설정할 때도 NavHostFragment를 사용
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                //다시 home으로 이동 (home 아이콘 클릭 시)
                if (navController.getCurrentDestination().getId() != R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home);
                    return true;
                }
            } else {
//                다른 아이콘 클릭 시 이벤트 처리
                navController.navigate(item.getItemId());
                return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // 뒤로가기 버튼 클릭 시 처리할 코드
            // 예를 들어, HomeFragment로 이동하려면 NavController를 사용하여 navigate() 메서드를 호출
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // CustomFragment 버튼 클릭 이벤트를 처리하는 메서드
    public void mOnClick(View v) {
        if (v.getId() == R.id.Ueser1) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick(v);
            }
        }
    }
    public void mOnClick2(View v) {
        if (v.getId() == R.id.Ueser2) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick2(v);
            }
        }
    }
    public void mOnClick3(View v) {
        if (v.getId() == R.id.Ueser3) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick3(v);
            }
        }
    }
    public void mOnClick4(View v) {
        if (v.getId() == R.id.Ueser4) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick4(v);
            }
        }
    }
    // RankFragment 버튼 클릭 이벤트를 처리하는 메서드
    public void mOnClick_rank1(View v) {
        if (v.getId() == R.id.rg_btn1) {
            RankFragment rankFragment = (RankFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_rank);
            if (rankFragment != null) {
                rankFragment.mOnClick_rank1(v);
            }
        }
    }
    public void mOnClick_rank2(View v) {
        if (v.getId() == R.id.rg_btn2) {
            RankFragment rankFragment = (RankFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_rank);
            if (rankFragment != null) {
                rankFragment.mOnClick_rank2(v);
            }
        }
    }
    public void mOnClick_rank3(View v) {
        if (v.getId() == R.id.rg_btn3) {
            RankFragment rankFragment = (RankFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_rank);
            if (rankFragment != null) {
                rankFragment.mOnClick_rank3(v);
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, (AppBarConfiguration) null)
                || super.onSupportNavigateUp();
    }

}
