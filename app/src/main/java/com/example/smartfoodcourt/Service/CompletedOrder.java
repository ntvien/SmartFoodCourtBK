package com.example.smartfoodcourt.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Model.Order;
import com.example.smartfoodcourt.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompletedOrder extends Service implements ChildEventListener {
    NotificationManagerCompat managerCompat;
    DatabaseReference orderList;

    public CompletedOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        orderList = FirebaseDatabase.getInstance().getReference("Order/CurrentOrder/List");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orderList.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(snapshot.child("phone").getValue().equals(Common.user.getPhone())
                && snapshot.child("status").getValue().equals("1")){
            Order order = snapshot.getValue(Order.class);
            String orderDetail = "Stall " + order.getSupplierID() + ": Your order completed";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "n");
            builder.setAutoCancel((true))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setTicker("SmartFoodCourt")
                    .setContentInfo("")
                    .setContentText(orderDetail)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_baseline_fastfood_24);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            managerCompat = NotificationManagerCompat.from(getBaseContext());
            managerCompat.notify(Integer.parseInt(snapshot.getKey().substring(5)),builder.build());
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(snapshot.child("phone").getValue().equals(Common.user.getPhone())
                && snapshot.child("status").getValue().equals("1")){
            Order order = snapshot.getValue(Order.class);
            String orderDetail = "Stall " + order.getSupplierID() + ": Your order completed";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "n");
            builder.setAutoCancel((true))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setTicker("SmartFoodCourt")
                    .setContentInfo("")
                    .setContentText(orderDetail)
                    .setContentIntent(null)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_baseline_fastfood_24);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            managerCompat = NotificationManagerCompat.from(getBaseContext());
            managerCompat.notify(Integer.parseInt(snapshot.getKey().substring(5)),builder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        managerCompat.cancelAll();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

}
