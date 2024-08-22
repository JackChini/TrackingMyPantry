package com.example.trackingmypantrygiacomochinilam;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import java.util.Calendar;
import java.util.List;

import androidx.core.app.NotificationCompat;

public class BroadcastManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("EXPIRE")){
            scanExpireDate(context);
        }
        if(intent.getAction().equals("EMPTY")){
            scanEmptyItem(context);
        }
    }

    public void createNotification(Context context, Intent intent, String ticker, String title, String description, String longDescription){
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.expire_logo);
        builder.setContentIntent(p);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(longDescription));
        builder.setAutoCancel(false);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setLights(Color.WHITE, 3000, 3000);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.pantry_logo, n);

    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.NOTIFICATION_CHANNEL_ID;
            String description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            channel.setLightColor(Color.WHITE);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scanExpireDate(Context context){
        //ottengo la repo degli item dell'utente attivo
        ItemRepository itemRepository = new ItemRepository((Application)context.getApplicationContext(), Utility.getIdPref(context));
        //controllo gli item che scadono domani
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        String expireSearch = Utility.getDateStringFromCalendar(calendar);
        List<Item> expireList = itemRepository.getExpireList(expireSearch);

        //creo una notifica in base a quanti item scadono
        if(!expireList.isEmpty()){
            String body = "I seguenti prodotti scadranno domani: ";
            for(Item i : expireList){
                body += i.getName()+" - ";
            }
            body += "!";

            createNotificationChannel(context);
            Intent notificationIntent =  new Intent(context, SplashActivity.class);
            createNotification(context, notificationIntent, "Nuova notifica", "Prodotti in scadenza!", body, body);
        }
    }

    private void scanEmptyItem(Context context){
        //ottengo la repo degli item dell'utente attivo
        ItemRepository itemRepository = new ItemRepository((Application)context.getApplicationContext(), Utility.getIdPref(context));
        List<Item> emptyList = itemRepository.getEmptyItemsList();
        //creo una notifica in base a quanti item scadono
        if(!emptyList.isEmpty()){
            String body = "I seguenti prodotti sono esauriti: ";
            for(Item i : emptyList){
                body += i.getName()+", ";
            }
            body += "!";

            createNotificationChannel(context);
            Intent notificationIntent =  new Intent(context, SplashActivity.class);
            createNotification(context, notificationIntent, "Nuova notifica", "Prodotti esauriti!", body, body);
        }
    }
}
