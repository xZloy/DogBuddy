# 🐶 DogBuddy

**DogBuddy** is a mobile application powered by artificial intelligence that identifies dog breeds from photos and provides personalized care recommendations. It combines real-time image analysis with a containerized backend and OpenAI integration. The app also connects to Firebase for secure user data storage and authentication.

---

## 🚀 Features

- 📸 Real-time dog breed recognition using AI
- 🧠 Personalized hygiene and nutrition tips via OpenAI
- ☁️ Cloud backend with FastAPI and Docker
- 🔐 User data storage and authentication with Firebase
- 🧪 Lightweight `.tflite` model optimized for inference
- 🔄 CI/CD-friendly with GitHub and Render

---

## 🧰 Technologies Used

### 📱 Mobile App
- Kotlin + Jetpack Compose
- CameraX
- OkHttp
- Firebase Authentication
- Firebase Realtime Database / Firestore
- Android Studio

### ⚙️ Backend
- FastAPI (Python)
- TensorFlow Lite
- Docker
- OpenAI API

### ☁️ Deployment
- Render (PaaS)
- GitHub
- Firebase Console

---

## 🔗 Firebase Integration

- 🔐 **Authentication:** Handles user sign-up and login securely.
- 🗂️ **Database:** Stores user profiles, scanned dog breeds, and care history.
- 📁 **Storage (optional):** Can be used for saving user-submitted dog photos.

---

## ⚙️ How It Works

1. The user captures or selects a dog photo.
2. The image is sent via HTTP to the backend container (`/predict`).
3. The backend runs inference with the TensorFlow Lite model and returns the predicted breed.
4. OpenAI is queried to generate personalized care advice.
5. The app displays results and stores the session in Firebase.

---

## 📦 Setup Instructions

### 🔧 Backend (Local)

```bash
git clone https://github.com/xZloy/dogbuddy-backend.git
cd dogbuddy-backend
docker build -t dogbuddy-backend .
docker run -p 8000:8000 dogbuddy-backend
