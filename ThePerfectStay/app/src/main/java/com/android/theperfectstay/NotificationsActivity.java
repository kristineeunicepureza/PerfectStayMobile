package com.android.theperfectstay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;


public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;

    private static final List<NotificationItem> staticNotificationList = new ArrayList<>();

    public static void addNotification(String title, String message) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        NotificationItem newItem = new NotificationItem(title, message, currentTime);

        staticNotificationList.add(newItem);

        System.out.println("Static Notification added: " + title + " - " + message + " at " + currentTime + ". Total notifications: " + staticNotificationList.size());
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Notifications");

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notificationsRecyclerView = findViewById(R.id.notifications_recycler_view);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        java.util.List<NotificationItem> notificationList = staticNotificationList;


        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        notificationsRecyclerView.setAdapter(adapter);

    }

    private static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        private java.util.List<NotificationItem> notifications;

        public NotificationAdapter(java.util.List<NotificationItem> notifications) {
            this.notifications = notifications;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notifications_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            NotificationItem notification = notifications.get(position);
            holder.titleTextView.setText(notification.getTitle());
            holder.messageTextView.setText(notification.getMessage());
            holder.timeTextView.setText(notification.getTime());
        }

        @Override
        public int getItemCount() {
            return notifications.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView messageTextView;
            TextView timeTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.notification_title);
                messageTextView = itemView.findViewById(R.id.notification_message);
                timeTextView = itemView.findViewById(R.id.notification_time);
            }
        }
    }

    public static class NotificationItem {
        private String title;
        private String message;
        private String time;

        public NotificationItem(String title, String message, String time) {
            this.title = title;
            this.message = message;
            this.time = time;
        }

        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTime() { return time; }
    }
}