package com.example.enderpearl;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID_DEFAULT = "Channel_default_priority";
    private static final String CHANNEL_NAME = "Notification Channel";
    private static final int NOTIFICATION_ID = 1;

    Button showDesc, addRemoveFromReadList, remind;
    TextView additionalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        createNotificationChannel(this);


        showDesc = findViewById(R.id.showDesc);
        addRemoveFromReadList = findViewById(R.id.addRemoveFromReadList);
        remind = findViewById(R.id.remind);
        additionalText = findViewById(R.id.additionalText);

        showDesc.setOnClickListener(v -> {
            sendNotification(
                    this,
                    CHANNEL_ID_DEFAULT,
                    this,
                    "Moja Książka",
                    "Krótki opis: Ekscytująca historia pełna zwrotów akcji.");
        });
        addRemoveFromReadList.setOnClickListener(v -> {
            addOrRemoveFromList();
        });
        remind.setOnClickListener(v -> {
            sendNotification(
                    this,
                    CHANNEL_ID_DEFAULT,
                    this,
                    "Moja Książka",
                    "Pamiętaj, aby znaleźć czas na lekturę!");
        });
    }

    public static void createNotificationChannel(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelDefault = new NotificationChannel(CHANNEL_ID_DEFAULT, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channelDefault);
            }
        }
    }

    public static void sendNotification(AppCompatActivity activity,  String CHANNEL_ID, Context context, String title, String message){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
                return;
            }
        }
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ender_pearl)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    public void addOrRemoveFromList(){
        if(additionalText.getVisibility() == TextView.GONE){
            additionalText.setVisibility(TextView.VISIBLE);
            addRemoveFromReadList.setText("USUŃ Z CHCĘ PRZECZYTAĆ");
        }else if(additionalText.getVisibility() == TextView.VISIBLE){
            additionalText.setVisibility(TextView.GONE);
            addRemoveFromReadList.setText("DODAJ DO CHCĘ PRZECZYTAĆ");
        }
    }
}