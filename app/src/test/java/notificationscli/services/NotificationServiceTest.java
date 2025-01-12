package notificationscli.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import notificationscli.services.impls.NotificationServiceImpl;

public class NotificationServiceTest {
    private static final String BASE_DIR = "./src/test/resources/test-notifications/";

    @Test
    void usesTheCorrectJSONFileToLoadTheNotification() {
        // Given a JSON Notification File
        var expectedPathToJSONFile = BASE_DIR + "valid.json";
        // And a NotificationService
        var notificationService = new NotificationServiceImpl(expectedPathToJSONFile);

        // When we get the PathToJSONFile
        var actualPathToJSONFile = notificationService.getPathToJSONFile();

        // Then the correct PathToJSONFile is used
        var errorMessage = String.format(
            "Expected PathToJSONFile %s but got %s", 
            expectedPathToJSONFile, 
            actualPathToJSONFile
        );
        assertEquals(expectedPathToJSONFile, actualPathToJSONFile, errorMessage);
    }

    @Test
    void loadsTheDataFromAJSONFile() {
        // Given a JSON Notification File
        var pathToJSONFile = BASE_DIR + "valid.json";
        // And a NotificationService
        var notificationService = new NotificationServiceImpl(pathToJSONFile);

        // And expectedJSONData
        var expectedJSONData = createJSONData();

        // When getting the data from the JSON Notification File
        var actualJSONData = notificationService.getJSONData();

        // Then the correct JSON Data is loaded
        var errorMessage = String.format(
            "Expected JSON Data %s but got %s", 
            expectedJSONData, 
            actualJSONData
        );
        assertEquals(expectedJSONData.toString(), actualJSONData.toString(), errorMessage);
    }

    private JSONObject createJSONData() {
        var jsonContent = new JSONObject("{\"studyInstanceUID\":\"ANY_UID\",\"reportUID\":\"ANY_UID\"}");
        return new JSONObject(Map.of(
            "notificationUrl", "https://example.com",
            "notificationContent", jsonContent
        ));
    }

    /**
     * static members normally are listed first but this helper method is just for this test case so I prefer to list it
     * directly before for easier readability.
     */
    static Stream<Arguments> invalidPathToJSONFileExamples() {
        return Stream.of(
                Arguments.of("notificationUrl", "[#: required key [notificationUrl] not found]"),
                Arguments.of("notificationContent", "[#: required key [notificationContent] not found]"),
                Arguments.of("reportUID", "[#/notificationContent: required key [reportUID] not found]"),
                Arguments.of("studyInstanceUID", "[#/notificationContent: required key [studyInstanceUID] not found]"),
                Arguments.of(
                "reportUID-and-studyInstanceUID",
                "[#/notificationContent: required key [reportUID] not found, #/notificationContent: required key [studyInstanceUID] not found]"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPathToJSONFileExamples")
    void throwsRuntimeExceptionIfJSONFileFailsSchemaValidation(String name, String error) {
        var invalidPathToJSONFile = String.format("%smissing-%s.json", BASE_DIR, name);
        var exception = assertThrows(RuntimeException.class, () -> new NotificationServiceImpl(invalidPathToJSONFile));
        
        var expectedMessage = String.format(
            "JSON file %smissing-%s.json failed Schema validation with errors(s): %s", 
            BASE_DIR, 
            name, 
            error
        );
        var actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void returnsTheCorrectNotificationForAJSONFile(){
        // Given a JSON Notification File
        var pathToJSONFile = BASE_DIR + "valid.json";
        // And a NotificationService
        var notificationService = new NotificationServiceImpl(pathToJSONFile);

        // And expectedNotification
        var expectedNotification = new Notification("https://example.com", "{\"studyInstanceUID\":\"ANY_UID\",\"reportUID\":\"ANY_UID\"}");

        // When getting the Notification
        var actualNotification = notificationService.getNotification();

        // Then the correct Notification is returned
        var errorMessage = String.format(
            "Expected Notification %s but got %s", 
            expectedNotification, 
            actualNotification
        );
        assertEquals(expectedNotification, actualNotification, errorMessage);
    }
}