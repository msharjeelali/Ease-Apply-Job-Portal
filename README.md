# Ease Apply – Job Portal 📱

A mobile application designed to simplify the job-hunting process for job seekers and streamline job posting for employers.

![License](https://img.shields.io/badge/license-MIT-green.svg)  
![Platform](https://img.shields.io/badge/platform-Android-blue.svg)  
![Firebase](https://img.shields.io/badge/backend-Firebase-orange.svg)

---

## ✨ Overview

**Ease Apply** is an Android-based mobile job portal that allows users to sign up, browse job postings, apply for jobs, and track their applications. Employers can post job listings, making it a two-sided platform tailored to simplify the job application lifecycle.

---

## 🚀 Features

- 🔐 **User Authentication** – Register and login securely using Firebase Auth.
- 📋 **Job Listings** – Browse a variety of available job opportunities.
- 📎 **Apply for Jobs** – Seamlessly apply with a few taps.
- 🧾 **Application Tracking** – Monitor status and history of submitted applications.
- 👤 **Profile Management** – Manage profile info and resume uploads.
- 🔍 **Search Functionality** *(upcoming)* – Filter jobs based on categories, location, etc.

---

## 🏗️ Tech Stack

- **Frontend:** Kotlin, Android XML
- **Backend:** Firebase Authentication, Firebase Realtime Database
- **Tools:** Android Studio, Firebase Console

---

## 🛠️ Project Structure

```bash
Ease-Apply-Job-Portal/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/easeapply/
│   │   │   │   ├── activities/        # Screens like Login, Register, Dashboard, JobDetail
│   │   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   │   ├── models/            # Data classes (User, Job, Application)
│   │   │   │   ├── firebase/          # Firebase-related functions
│   │   │   │   └── utils/             # Helper classes/methods
│   │   │   ├── res/
│   │   │   │   ├── layout/            # XML layouts
│   │   │   │   ├── drawable/          # UI graphics/icons
│   │   │   │   └── values/            # Colors, strings
│
└── README.md
