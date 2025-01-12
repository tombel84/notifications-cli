package notificationscli;

import java.net.http.HttpClient;

import notificationscli.services.HttpService;
import notificationscli.services.NotificationService;

import notificationscli.services.impls.HttpServiceImpl;
import notificationscli.services.impls.NotificationServiceImpl;

public class App {
    private static final String LOG_TEMPLATE = """
            Notification URL         : %s,
            HTTP Request Body        : %s,
            HTTP Response Body       : %s,
            HTTP Response StatusCode : %s,
            Response time            : %sms
            """;

    private final HttpService httpService;
    private final NotificationService notificationService;

    public App(HttpService httpService, NotificationService notificationService) {
        this.httpService = httpService;
        this.notificationService = notificationService;
    }

    private void run() {
        var notification = notificationService.getNotification();
        long start = System.currentTimeMillis();
        var httpResponse = httpService.post(notification);
        long responseTime = System.currentTimeMillis() - start;

        System.out.printf(LOG_TEMPLATE, 
                          notification.url(), 
                          notification.content(), 
                          httpResponse.body(), 
                          httpResponse.statusCode(), 
                          responseTime);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: ./gradlew run --args='pathToJSONFile'");
            System.exit(-1);
        }

        try{
            /*
            * We only have 2 dependencies at this point so elect to manually inject these rather
            * than use a library such as Guice or Picocontainer etc.
            */

            var httpClient = HttpClient.newHttpClient();
            var httpService = new HttpServiceImpl(httpClient);
            var pathToJSONFile = args[0];
            var notificationService = new NotificationServiceImpl(pathToJSONFile);

            new App(httpService, notificationService).run();
        } catch (RuntimeException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
