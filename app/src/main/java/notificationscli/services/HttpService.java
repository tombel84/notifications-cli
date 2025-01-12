package notificationscli.services;

import java.net.http.HttpResponse;

/**
 * This interface represents an HTTP service that is responsible for sending
 * notifications via an HTTP POST request.
 */
public interface HttpService {

    /**
     * Sends a notification by making an HTTP POST request.
     *
     * @param notification the notification to be sent. Must not be null.
     * @return an HttpResponse containing the server's response. 
     *         The response body is expected to be a String.
     * @throws IllegalArgumentException if the notification is null.
     * @throws RuntimeException if an error occurs while making the HTTP POST request.
     */
    HttpResponse<String> post(Notification notification);
}