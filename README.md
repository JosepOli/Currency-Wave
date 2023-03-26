# Currency Wave

Currency Wave is an Android application that provides real-time currency exchange rates and allows users to convert currencies. The app fetches the latest exchange rates from our Firestore database, which is updated periodically.

## Features

- View real-time exchange rates for over 150 currencies.
- Convert currencies using the built-in converter tool.

## Requirements

- Android Studio 4.0 or later
- Java Development Kit (JDK) version 8 or later
- A Firebase project with Firestore enabled

## Getting Started

1. Clone the repository: `git clone https://github.com/JosepOli/currency-wave-firebase.git`
2. Open the project in Android Studio.
3. Connect the app to your Firebase project by following the [Firebase Android setup guide](https://firebase.google.com/docs/android/setup)
4. Set up a Firestore database and update it with the latest exchange rates.
5. Build and run the app.

## Usage

- To view exchange rates, select the desired currencies from the list.
- To convert currencies, use the built-in converter tool by entering the amount and selecting the desired currencies.

## Credits

This app uses the following services and libraries:

- [Firebase Firestore](https://firebase.google.com/products/firestore) - for storing and retrieving exchange rate data
- [AndroidX](https://developer.android.com/jetpack/androidx) - for modern Android development.

## License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
