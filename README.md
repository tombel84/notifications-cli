# notifications-cli
notifications-cli allows users to POST notifications about reports that capture medical assessments associated with image annotations they have created and published. 

## Table of Contents
- [Requirements](#requirements)
- [Build](#Build)
- [Test](#test)
- [Run](#run)

## Requirements
Note: This project uses Gradle wrapper to build it so you don't need to install Gradle on your machine to build/test and run the application

- [Java JDK](https://www.oracle.com/java/technologies/downloads/?er=221886) version 17 or higher
- [Git](https://git-scm.com/downloads)

## Build

Clone the repository:

`git clone https://gitlab.com/java9953823/notifications-cli.git`

then `cd notifications-cli`

To build the app, you can execute:

`./gradlew build`

This command will build the app to the build directory.

## Test
To run the tests, you can execute:

`./gradlew test`

This command will run all the unit tests defined in the project. You can find the test results in build/reports/tests/test/index.html.

## Run
To run the app, you can execute:

`./gradlew run --args='pathToJSONFile'`

Note: See src/main/resources/notification.json for the expected format of these JSON files.

Example: 

Running `./gradlew run --args='./src/main/resources/notification.json'`

Produces this console output:
~~~
Notification URL         : https://webhook.site/d9d87d20-6729-4992-b986-f7e0776ae234,
HTTP Request Body        : {"studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798","reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598"},
HTTP Response Body       : {"success":false,"error":{"message":"Token \"d9d87d20-6729-4992-b986-f7e0776ae234\" not found","id":""}},
HTTP Response StatusCode : 404,
Responsetime             : 3316ms
~~~