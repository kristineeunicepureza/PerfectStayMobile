# The Perfect Stay - Android Booking System

**The Perfect Stay** is a native Android application designed to streamline the discovery and booking process for regional resorts in Cebu and Bohol. This project was developed as part of the **CSIT335** course to demonstrate proficiency in functional requirement documentation, UI/UX design, and local data persistence.

## 📌 Project Overview

The application allows travelers to browse a curated list of resorts, view detailed amenities, and manage bookings through a localized transaction engine. A key focus of this project was the implementation of a **Fixed-Rate Pricing Logic** and manual cash transaction validation.

## 🚀 Key Features

* **Multi-Criteria Search:** Real-time filtering by resort name and location.
* **Dynamic Presentation:** High-fidelity resort cards with star ratings and global pricing units ($).
* **Localized Transactions:** A secure checkout flow that handles manual payment inputs and calculates change in local currency (₱).
* **Persistent History:** Local storage of booking records that remain accessible after application restarts.
* **Temporal Validation:** Logic-gate system to prevent invalid check-in/check-out date selections.

## 🛠 Tech Stack

* **Platform:** Android (Min SDK 24)
* **Language:** Java/Kotlin
* **UI Components:** RecyclerView, ConstraintLayout, Material Design Components
* **Data Management:** Local Storage (SharedPreferences/Internal Storage)

## 📋 Functional Specifications

This project follows a strict **Functional Specification Document (FSD)**. Key architectural decisions include:

* **Numerical Consistency:** Maintaining a 1:1 integer ratio between discovery and settlement prices to simplify manual accounting.
* **Validation Logic:** Ensuring the "Confirm" action is only enabled when `Amount Paid >= Total Price`.

## 👥 Contributors

* **Kristine Eunice S. Pureza** (BSIT-2)
