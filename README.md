# Exchange Rate Calculator App

This is a simple Android application that allows users to calculate exchange rates between different currencies.

## Architecture

The application follows the modern Android architecture guidelines and uses the following technologies:

*   **UI:** Built entirely with [Jetpack Compose](https.developer.android.com/jetpack/compose), Android's modern toolkit for building native UI. The UI state is managed by a ViewModel (`MainViewModel`) and represented by a `ExchangeRateUiState` sealed class.
*   **VM:** The `MainViewModel` is responsible for preparing and managing the data for the UI. It interacts with the repository to fetch data and exposes it to the UI via a `StateFlow`.
*   **Repository:** The app uses the repository pattern to abstract the data source. `ExchangeRateRepository` and its implementation `ExchangeRateRepositoryImpl` are responsible for fetching data from the network.
*   **Networking:** [Retrofit](https://square.github.io/retrofit/) is used for making API calls to the [dolarapp.dev](https://api.dolarapp.dev) API.
*   **DI:** [Hilt](https://dagger.dev/hilt/) is used for dependency injection to manage dependencies between different parts of the application.
*   **Coroutines:** Asynchronous operations are handled using Kotlin Coroutines.

## Features

*   Calculate exchange rates between USDC and other currencies.
*   Swap between primary and secondary currencies.
*   Select from a list of available currencies.
*   Supports both light and dark themes.

## API Limitations

*   The Euro (EUR) currency is not supported by the API endpoint `https://api.dolarapp.dev/v1/tickers?currencies=EUR`.
*   The endpoint to fetch a list of available currencies (`https://api.dolarapp.dev/v1/tickers-currencies`) is not functional. Due to this limitation, the application uses a hardcoded list of currencies: "MXN", "ARS", "BRL", "COP".

## Design Notes

*   **Swap Functionality:** The swap button swaps the values of the two amount fields and inverts the calculation logic.
*   **Font:** The original design specified the "Messina Sans" font. However, Messina Sans is a commercial font that requires a paid license. To avoid legal issues, this app uses the default system font.

## How to Run the App

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Let Gradle sync and build the project.
4.  Run the `app` configuration on an Android emulator or a physical device.

## Code Quality

This project uses `ktlint` to enforce Kotlin coding standards. To run ktlint, execute the following Gradle task:

```bash
./gradlew ktlintCheck
```

To automatically format the code, run:

```bash
./gradlew ktlintFormat
```
