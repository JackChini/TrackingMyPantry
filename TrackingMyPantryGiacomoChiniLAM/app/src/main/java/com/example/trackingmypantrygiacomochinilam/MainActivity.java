package com.example.trackingmypantrygiacomochinilam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration homeAppBarConfig;
    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //view model
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if(!mainViewModel.userDataIsSet()){
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mainViewModel.setData(extras.getString("id"), extras.getString("user"),extras.getString("email"));
            }
        }

        //alarm per gli item che scadono domani, l'if serve per evitare di creare un alarm nel caso sia già presente
        boolean expireIsUp = (PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), BroadcastManager.class).setAction("EXPIRE"), PendingIntent.FLAG_NO_CREATE) != null);
        if(!expireIsUp){
            AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), BroadcastManager.class);
            intent.setAction("EXPIRE");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(calendar.SECOND, 0);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        //alarm per gli item che sono esauriti, l'if serve per evitare di creare un alarm nel caso sia già presente
        boolean emptyIsUp = (PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), BroadcastManager.class).setAction("EMPTY"), PendingIntent.FLAG_NO_CREATE) != null);
        if(!emptyIsUp){
            AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), BroadcastManager.class);
            intent.setAction("EMPTY");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(calendar.SECOND, 0);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        //Imposto la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Imposto il drawer e la navigationview
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navHeaderEmail);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navHeaderUser);
        navEmail.setText(mainViewModel.getEmail());
        navUsername.setText(mainViewModel.getUser());
        //Imposto la barra di navigazione
        homeAppBarConfig = new AppBarConfiguration.Builder(R.id.nav_pantry, R.id.nav_insert, R.id.nav_shopping, R.id.nav_profile, R.id.nav_settings).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, homeAppBarConfig);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, homeAppBarConfig) || super.onSupportNavigateUp();
    }
}