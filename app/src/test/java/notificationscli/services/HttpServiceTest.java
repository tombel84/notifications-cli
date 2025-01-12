package notificationscli.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import notificationscli.services.impls.HttpServiceImpl;

class HttpServiceTest {
    @Test
    @SuppressWarnings("unchecked")
    void postsAHttpPostRequestToTheCorrectURLWithTheCorrectContent() throws IOException, InterruptedException {
        // Given a HttpService
        var mockHTTPClient = mock(HttpClient.class);
        when(mockHTTPClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mock(HttpResponse.class));
        HttpService sut = new HttpServiceImpl(mockHTTPClient);

        // And a Notification to POST to a specific URL
        var notification = new Notification("https://example.com", "ANY_CONTENT");

        // When we POST the Notification
        sut.post(notification);

        // Then the correct content should be sent to the correct URL
        var expectedHttpPostRequest = HttpServiceImpl.createHttpPostRequest(notification.url(), notification.content());
        var actualHttpPostRequest = getCapturedHttpPostRequest(mockHTTPClient);

        var errorMessage = String.format(
            "Expected HttpPostRequest %s but got %s", 
            expectedHttpPostRequest, 
            actualHttpPostRequest
        );
        assertEquals(expectedHttpPostRequest, actualHttpPostRequest, errorMessage);
    }

    private HttpRequest getCapturedHttpPostRequest(HttpClient mockHTTPClient) throws IOException, InterruptedException {
        var requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(mockHTTPClient).send(requestCaptor.capture(), any());
        return requestCaptor.getValue();
    }
}
