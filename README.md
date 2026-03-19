# AISA_Idea — Smart Fuel & Bike Analysis Platform

## ✅ Quick Setup (3 Steps)

### Step 1 — Set MySQL Password
Edit: `src/main/resources/application.properties`
```
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
Database `aisa_idea_db` is **auto-created** on first run.

### Step 2 — Import into Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Browse to the `AISA_Idea` folder → Finish
3. Wait for Maven to download dependencies

### Step 3 — Run
Right-click `AISAIdeaApplication.java` → Run As → Spring Boot App

### Open Browser
```
http://localhost:8080
```
Register a new account, then login!

---

## 🌟 Features
| Feature | Description |
|---------|-------------|
| 🔐 Login / Register | Secure BCrypt password authentication |
| 👤 Profile + Photo | Upload profile picture, edit info |
| ⛽ Fuel Log | Add logs with vehicle type, brand, model |
| 📊 Dashboard | AI mileage prediction + all key metrics |
| 🗺️ Trip Analytics | Full trip table with search & delete |
| 🔩 Engine Health | Z-score anomaly detection |
| 📈 Statistics | Deep analytics + mileage trend chart |
| 🔧 Servicing | AI service prediction, history records |
| 💬 AISA_Vicky | Built-in AI chatbot for bike advice |

## 🚗 Vehicle Types Supported
2-Wheeler, 3-Wheeler, 4-Wheeler, 6-Wheeler, 8-Wheeler, 16-Wheeler

## 🔌 REST API
- GET  `/api/chatbot` — AISA_Vicky chatbot

## ⚙️ Tech Stack
- Java 17 + Spring Boot 3.2
- Spring Security (BCrypt)
- Spring Data JPA + MySQL
- Thymeleaf templates
- Premium dark CSS + Canvas charts
