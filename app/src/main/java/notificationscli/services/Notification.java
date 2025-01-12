package notificationscli.services;

/**
 * Represents a notification with a URL and content.
 * 
 * This record encapsulates the essential information needed to represent 
 * a notification, which includes its destination URL and the actual 
 * content of the notification.
 *
 * @param url     The URL associated with the notification.
 * @param content The content of the notification message.
 */
public record Notification(String url, String content) {
}
