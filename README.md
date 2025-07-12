# Smart Daily Expense Tracker App

A full-featured Smart Daily Expense Tracker module built with **Jetpack Compose**, **MVVM architecture**, and **Room Database**. Designed for small business owners to log, analyze, and export daily expenses with ease.

---

### Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/32948e67-82e8-483c-b8eb-c7ef7453f010" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/5692cc6a-8dc3-4d28-8f0c-341405b8da7d" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/024a6a62-41b6-40fb-8e78-dc0a6e4cda70" width="200"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/5e7cec44-42d4-4417-b7cd-9d81c03896a7" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/1a723997-dd23-459a-90ca-ea731da8beb2" width="200"/></td>
  </tr>
</table>


## ✨ Features

### ✅ Expense Entry Screen
- Add new expense with:
  - Title
  - Amount (₹)
  - Category (Staff, Travel, Food, Utility)
  - Optional notes
  - Optional receipt image (via image picker)
- Displays **Total Spent Today** in real-time
- Smooth entry animations + validation

### 📋 Expense List Screen
- View expenses for today and past dates (via filter)
- Group by **category** or **time**
- Shows:
  - Total count
  - Total amount
  - Empty state UI

### 📊 Expense Report Screen
- Shows:
  - Daily totals (last 7 days)
  - Category-wise totals
- Bar/line chart (mocked)
- Export as **CSV**
- Share via system share intent

---

## 🧱 Architecture

- **Jetpack Compose**: Declarative UI
- **MVVM Pattern**: Clean state management
- **Room DB + DAO**: Local data persistence
- **Dagger (without Hilt)**: Dependency injection

---

## 🚀 Getting Started

### Requirements
- Android Studio Hedgehog or newer
- Kotlin 1.9+
- Gradle 8+

- Minimum SDK 24+

### Run the App
1. Clone the repo:
   ```bash
   git clone https://github.com/Anoop0712/ExpenseTrackerApp
2. Do gradle sync and do cmd+ R to run the app


### Apk download
https://github.com/Anoop0712/ExpenseTrackerApp/releases/download/v1.0/app-debug.apk
