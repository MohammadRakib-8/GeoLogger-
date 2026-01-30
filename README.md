📍 GeoLogger

Background Location Tracking & API Integration Android App

📌 Project Description

GeoLogger is an Android application developed to demonstrate core Android development , including background location tracking, data persistence, and API integration.

The app continuously tracks the device’s location at a 5-minute interval, even when:

The app is closed

The app is removed from recent apps

Each location update is:

Displayed using a Toast message

Stored locally using Room (SQLite) for persistence

Additionally, the app integrates a free public API to fetch and display external data with proper validation and error handling.

🚀 Key Features

📍 Background Location Service

Uses Foreground Service for reliability

Runs continuously in background

Works after app closure or recent-app removal

Fetches location every 5 minutes

Displays latitude & longitude via Toast

💾 Local Data Persistence

Implemented using Room (SQLite)

Automatically saves each location update

Stores:

Latitude

Longitude

Timestamp

Data remains available after app restart

🌐 API Integration

Fetches data from a free public API 

Displays API data in the UI

Handles:

No internet connection

Invalid API responses

Network failures gracefully

🎨 Clean & Simple UI

Built with XML layouts

User-friendly and minimal design

Focused on clarity and usability
