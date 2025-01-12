package notificationscli.services.impls;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import notificationscli.services.Notification;
import notificationscli.services.NotificationService;

public class NotificationServiceImpl implements NotificationService {
    //Using a framework like SpringBoot I could just add this property to application.properties etc.
    private static final String NOTIFICATION_SCHEMA = "./src/main/resources/notification-schema.json";
    private static final String NOTIFICATION_URL_KEY = "notificationUrl";
    private static final String NOTIFICATION_CONTENT_KEY = "notificationContent";

    private final String pathToJSONFile;
    private final JSONObject jsonData;

    public NotificationServiceImpl(final String pathToJSONFile) {
        this.pathToJSONFile = pathToJSONFile;
        
        try (Reader jsonReader = new FileReader(pathToJSONFile);
             Reader schemaReader = new FileReader(NOTIFICATION_SCHEMA)) {
             
            Schema schema = loadSchema(schemaReader);
            jsonData = loadJSONData(jsonReader);
            schema.validate(jsonData);
        } catch (FileNotFoundException e) {
            String message = String.format("JSON file not found: %s", pathToJSONFile);
            throw new RuntimeException(message, e);
        } catch (ValidationException e) {
            String message = String.format(
                "JSON file %s failed Schema validation with errors(s): %s", 
                pathToJSONFile, 
                e.getAllMessages().toString());
            throw new RuntimeException(message, e);
        } catch (IOException e) {
            String message = String.format("I/O Error: %s", e.getMessage());
            throw new RuntimeException(message, e);
        }
    }

    private Schema loadSchema(Reader schemaReader) {
        JSONObject jsonSchema = new JSONObject(new JSONTokener(schemaReader));
        return SchemaLoader.load(jsonSchema);
    }

    private JSONObject loadJSONData(Reader jsonReader) {
        JSONTokener tokener = new JSONTokener(jsonReader);
        return new JSONObject(tokener);
    }

    @Override
    public Notification getNotification() {
        return toNotification(jsonData);
    }

    private Notification toNotification(JSONObject jsonData) {
        String url = (String) jsonData.get(NOTIFICATION_URL_KEY);
        JSONObject jsonContent = (JSONObject) jsonData.get(NOTIFICATION_CONTENT_KEY);
        return new Notification(url, jsonContent.toString());
    }

    public final String getPathToJSONFile() {
        return pathToJSONFile;
    }

    public final JSONObject getJSONData() {
        return jsonData;
    }
}