package com.android.theperfectstay.model

import java.util.Date

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Date,
    val type: NotificationType,
    var isRead: Boolean = false,
    var isImportant: Boolean = false
)

enum class NotificationType {
    BOOKING_CONFIRMATION,
    BOOKING_REMINDER,
    PROMOTION,
    PRICE_DROP,
    SYSTEM
}
