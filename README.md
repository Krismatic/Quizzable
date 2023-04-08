# Quizzable

## Tests

Tests marked with a checkmark were successful.

- [x] MainActivity
  - [x] The user clicks the "Quiz" button and is brought to the quiz.
  
- [x] QuizActivity
  - [x] The user clicks the correct answer, and the score is incremented by one.
  - [x] The user clicks the wrong answer, and the score stays the same.
  
- [x] DatabaseActivity
  - [x] The user adds a new entry, and the number of entries is incremented by one.
  - [x] The user deletes an entry, and the number of entries is decremented by one.

## Gradle 

After using Gradle to run the tests with the command `./gradlew connectedAndroidTest --info`, it revealed that when all the tests were ran in succession, Intents would initialize more than once without releasing, causing a crash. After fixing this and running the command again, the project was successfully built with all tests green. 

During testing, I believe that the APK file named **app-debug-androidTest.apk** is used.

I think that the adb commands used by Gradle are...

- `adb install ...` - To install the APK onto the device for testing.
- `adb logcat` - To enable logcat so that the developer can read the logs from the tests.
- `adb shell am instrument ...` - To start the UI tests.
- `adb shell am start ...` - To start an activity.
- `abd shell am force-stop ...` - To stop the application after finishing a test.
