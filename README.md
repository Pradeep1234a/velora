# 📱 Velora Tracker

<p align="center">
  <img src="branding/ic_launcher-playstore.png" width="128" height="128" alt="Velora Tracker Logo" style="border-radius: 28px;" />
  <br/>
  <b>Intelligent Personal Finance & Day-to-Day Expense Tracker</b>
  <br/><br/>
  <img src="https://img.shields.io/badge/Velora_Tracker-Android_App-1B6B52?style=for-the-badge&logo=android&logoColor=white" alt="Velora Tracker" />
  <br/>
  <a href="https://github.com/Pradeep1234a/velora/releases/latest">
    <img src="https://img.shields.io/badge/Download-Latest_Release_APK-2E7D32?style=for-the-badge&logo=android&logoColor=white" alt="Download Release APK" />
  </a>
  <a href="https://github.com/Pradeep1234a/velora/releases/latest">
    <img src="https://img.shields.io/github/v/release/Pradeep1234a/velora?style=for-the-badge&color=1B6B52" alt="Latest Release" />
  </a>
  <a href="https://github.com/Pradeep1234a/velora/actions">
    <img src="https://img.shields.io/github/actions/workflow/status/Pradeep1234a/velora/release.yml?style=for-the-badge&logo=githubactions&logoColor=white" alt="Build Status" />
  </a>
</p>

---

### ⬇️ Quick Download for Android

Click below to download the latest production **Release APK**:

👉 **[📥 Download Latest Release APK (`velora-release.apk`)](https://github.com/Pradeep1234a/velora/releases/latest/download/velora-release.apk)**

> 📌 Every push to `main` triggers an automatic GitHub Actions workflow that increments the `versionCode`, builds a signed release APK, and publishes it directly to [GitHub Releases](https://github.com/Pradeep1234a/velora/releases/latest).

---

## ✨ Features

- 💰 **Precision Financial Math**: Built using 64-bit integer minor units (paise) to guarantee 100% accuracy with zero floating-point rounding errors.
- 📊 **Real-Time Cumulative Balance**: Current balance represents `Opening Balance + All Income - All Expenses` cumulatively, while period filters isolate specific time windows.
- 🤖 **Intelligent Automatic Categorization**: Debounced AI categorization suggestions with merchant preference learning.
- 📈 **Dynamic Analytics & Charts**: Custom Canvas-based spending trends with Daily, Weekly, and Monthly granularity, category progress bars, and top transaction breakdowns.
- 🏷️ **Comprehensive Category Management**: Preloaded with 20 default categories across Income & Expense with curated semantic palettes, plus custom category creation and safe transaction re-assignment upon deletion.
- 🎨 **Google-Quality Material 3 UI**: Dynamic and static color schemes, full Light and Dark theme reactivity, Indian number system formatting (`₹1,25,000`), and compact amount notations (`₹50K`, `₹2L`, `₹1Cr`).
- ⚡ **Offline-First & Responsive**: Room Database + Coroutines Flow with manual DI and zero cold-start delay.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin 2.0.0 |
| **UI Toolkit** | Jetpack Compose + Material 3 (BOM 2024.05.00) |
| **Architecture** | MVVM + Clean Architecture |
| **Database** | Room 2.6.1 + KSP |
| **Preferences** | Jetpack DataStore Preferences 1.1.1 |
| **Async / Stream** | Kotlin Coroutines 1.8.1 + StateFlow / SharedFlow |
| **Navigation** | Jetpack Navigation Compose 2.7.7 |
| **CI / CD** | GitHub Actions (Automated Release APK Build & Tagging) |

---

## 🚀 Automated CI/CD & Incremental Versioning

Every successful build on `main` runs an automated GitHub Actions pipeline:
1. Sets JDK 17 and sets up Gradle.
2. Generates a valid release keystore.
3. Dynamically increments `versionCode` via `${{ github.run_number }}` and produces `1.0.<run_number>`.
4. Executes `./gradlew assembleRelease` to compile a signed production APK.
5. Publishes a new GitHub Release with `Velora-v1.0.<run_number>.apk` and `velora-release.apk` attached.

---

## 💻 Local Building

To build the APK locally:

```bash
# Clone the repository
git clone https://github.com/Pradeep1234a/velora.git
cd velora

# Build Debug APK
./gradlew assembleDebug

# Build Release APK
./gradlew assembleRelease

# Run Unit Tests
./gradlew test
```

The release APK will be located at:
```
app/build/outputs/apk/release/app-release.apk
```

---

## 📄 License

Open-source under the Apache 2.0 License.
