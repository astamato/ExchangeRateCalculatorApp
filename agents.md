# AI Agent Context (agents.md)

This file provides context and instructions for AI agents working on the **Exchange Rate Calculator App**.

## Project Overview
A Jetpack Compose Android app for calculating exchange rates between USDC and local currencies (MXN, ARS, BRL, COP).

## Core Architecture & Tech Stack
- **Architecture:** MVVM + Repository Pattern.
- **UI:** 100% Jetpack Compose.
- **Dependency Injection:** Hilt.
- **Networking:** Retrofit + Kotlinx Serialization.
- **State Management:** `ExchangeRateUiState` (Sealed Class) exposed via `StateFlow`.
- **Testing:** JUnit 4, MockK, Turbine for Flow testing, Compose Test Rule for UI.

## Critical Business Logic
- **Base Currency:** The application always uses **USDC** as the base for calculations.
- **Exchange Rates:**
    - The app distinguishes between buying and selling directions to use the correct rate:
        - **USDC → Local (Selling USDC):** Uses the **`bid`** price.
        - **Local → USDC (Buying USDC):** Uses the **`ask`** price.
- **Hardcoded Currencies:** Due to API limitations, the available currencies are hardcoded to `["MXN", "ARS", "BRL", "COP"]`.

## UI & Design Conventions
- **Typography:** Both primary and secondary amount fields must use `FontWeight.Bold` regardless of focus.
- **Input:** Use `BasicTextField` in `CurrencyRow` with `CurrencyVisualTransformation` for currency formatting.
- **Formatting:** Numbers are formatted to 2 decimal places.
- **Trailing Zeros:** Keep `stripTrailingZeros()` in `MainViewModel.formatAmount()` as the design preference is to show compact numbers (e.g., "100" instead of "100.00") when possible.

## Development Workflow
- **Linting:** `ktlint` is used for formatting. Always check formatting after edits:
  ```bash
  ./gradlew ktlintCheck
  ./gradlew ktlintFormat
  ```
- **Tests:**
    - Unit tests: `./gradlew :app:testDebugUnitTest`
    - Instrumentation tests: `./gradlew :app:connectedDebugAndroidTest`

## Agent Instructions
1. **Preserve Formatting:** Do not remove `.stripTrailingZeros()` in the ViewModel unless explicitly asked, as tests expect this behavior.
2. **Bold Styling:** Ensure any new currency input fields maintain `FontWeight.Bold`.
3. **Mocking:** When writing tests, use `mockk` for repository/logger and `turbine` for Flow verification.
4. **Context Awareness:** Refer to `README.md` for high-level user info and `agents.md` for implementation constraints.
5. **Update Context:** If a change is made that fundamentally contradicts any part of this document, update `agents.md` to reflect the new state of the project.
