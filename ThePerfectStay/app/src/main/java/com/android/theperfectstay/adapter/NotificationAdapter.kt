package com.android.theperfectstay.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.R
import com.android.theperfectstay.model.Notification
import com.android.theperfectstay.model.NotificationType
import java.util.Date

class NotificationAdapter(
    private val context: Context,
    private var notificationList: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var listener: OnNotificationActionListener? = null

    interface OnNotificationActionListener {
        fun onNotificationClicked(notification: Notification)
        fun onOptionsClicked(view: View, notification: Notification)
    }

    fun setOnNotificationActionListener(listener: OnNotificationActionListener) {
        this.listener = listener
    }

    fun updateNotifications(newList: List<Notification>) {
        notificationList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.notifications_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notificationList.size

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val readIndicator: View = itemView.findViewById(R.id.read_indicator)
        private val notificationIcon: ImageView = itemView.findViewById(R.id.notification_icon)
        private val notificationTitle: TextView = itemView.findViewById(R.id.notification_title)
        private val notificationMessage: TextView = itemView.findViewById(R.id.notification_message)
        private val notificationTime: TextView = itemView.findViewById(R.id.notification_time)
        private val optionsButton: ImageButton = itemView.findViewById(R.id.btn_options)

        fun bind(notification: Notification) {
            notificationTitle.text = notification.title
            notificationMessage.text = notification.message

            readIndicator.visibility = if (notification.isRead) View.INVISIBLE else View.VISIBLE

            val iconResId = when (notification.type) {
                NotificationType.BOOKING_CONFIRMATION -> R.drawable.ic_notifications
                NotificationType.BOOKING_REMINDER -> R.drawable.ic_notification_reminder
                NotificationType.PROMOTION -> R.drawable.ic_notification_promotion
                NotificationType.PRICE_DROP -> R.drawable.ic_notification_promotion
                NotificationType.SYSTEM -> R.drawable.ic_notifications
                else -> R.drawable.ic_notifications
            }
            notificationIcon.setImageResource(iconResId)

            notificationTime.text = getRelativeTimeSpan(notification.timestamp)

            itemView.setOnClickListener {
                listener?.onNotificationClicked(notification)
            }

            optionsButton.setOnClickListener {
                listener?.onOptionsClicked(it, notification)
            }
        }

        private fun getRelativeTimeSpan(date: Date): String {
            return DateUtils.getRelativeTimeSpanString(
                date.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
        }
    }
}