package com.ryanhcode.tropicalscanner.notification

class NotificationTracker {
    companion object {
        var notifications: MutableList<Notification> = mutableListOf()

        fun addNotification(notif: Notification){
            notifications.add(notif)
        }

        fun getLatestNotification(): Notification {
            return notifications[notifications.size - 1]
        }
    }
}