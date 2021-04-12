package aptech.fatima.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private MapsFragment MapFragment = new MapsFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private WeatherFragment weatherFragment =new WeatherFragment();
    private BottomNavigationView menuItem;
   public String data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItem = findViewById(R.id.menu_item);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("pass_data"));
        Bundle bundle0=new Bundle();
        bundle0.putString("data", data1);
        MapFragment.setArguments(bundle0);
        setFragment(MapFragment);
        menuItem.setSelectedItemId(R.id.menu_map);
        menuItem.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.isChecked()){
                    return true;
                } else {
                    switch (menuItem.getItemId()){

                        case R.id.menu_map:
                            Bundle bundle1=new Bundle();
                            bundle1.putString("data", data1);
                            MapFragment.setArguments(bundle1);
                            setFragment(MapFragment);
                            getSupportActionBar().setTitle("Google Map");
                            return true;

                        case R.id.menu_search:
                            Bundle bundle3=new Bundle();
                            bundle3.putString("data", data1);
                            searchFragment.setArguments(bundle3);
                            setFragment(searchFragment);
                            getSupportActionBar().setTitle("Video Search");
                            return true;

                        case R.id.menu_weather:
                            Bundle bundle2=new Bundle();
                            bundle2.putString("data", data1);
                            weatherFragment.setArguments(bundle2);
                            setFragment(weatherFragment);
                            getSupportActionBar().setTitle("Weather Updates");
                            return true;

                        default:
                            setFragment(MapFragment);
                            getSupportActionBar().setTitle("Google Map");
                            return true;
                    }
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, fragment);
        ft.commit();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            data1 = intent.getStringExtra("desti");
        }
    };
}