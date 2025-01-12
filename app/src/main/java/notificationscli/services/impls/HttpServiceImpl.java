package notificationscli.services.impls;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import notificationscli.services.HttpService;
import notificationscli.services.Notification;

public class HttpServiceImpl implements HttpService {
    private HttpClient httpClient;

    public HttpServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static HttpRequest createHttpPostRequest(String url, String content) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(content))
                .build();
    }

    @Override
    public HttpResponse<String> post(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("A notification must not be null");
        }

        try {
            var postRequest = createHttpPostRequest(notification.url(), notification.content());
            return httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            String message = String.format("HTTP POST request was interrupted: %s", e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException(message, e);
        } catch (IOException e) {
            String message = String.format("I/O Error: %s", e.getMessage());
            throw new RuntimeException(message, e);
        }
    }
}