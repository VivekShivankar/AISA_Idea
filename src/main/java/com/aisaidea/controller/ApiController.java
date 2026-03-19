package com.aisaidea.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/chatbot")
    public ResponseEntity<Map<String, String>> chatbot(@RequestBody Map<String, String> body) {
        String q = body.getOrDefault("message", "").toLowerCase().trim();
        return ResponseEntity.ok(Map.of("reply", getReply(q)));
    }

    private String getReply(String q) {
        if (q.contains("hello") || q.contains("hi") || q.contains("hey") || q.contains("helo"))
            return "👋 Hello! I'm AISA_Vicky, your smart bike AI assistant! I can help you with fuel tips, servicing advice, mileage improvement, engine health, and understanding your AISA analytics. What would you like to know?";
        if (q.contains("who are you") || q.contains("what are you") || q.contains("aisa") || q.contains("vicky"))
            return "🤖 I'm AISA_Vicky — the AI assistant built into AISA Idea platform! I'm trained to help you analyze your bike's performance, predict servicing needs, and give expert fuel & maintenance tips. Ask me anything!";
        if (q.contains("mileage") || q.contains("kmpl") || q.contains("fuel efficiency") || q.contains("milage"))
            return "🚀 To improve mileage: (1) Maintain 40-60 km/h steady speed (2) Check tyre pressure weekly — under-inflated tyres reduce mileage 10% (3) Avoid sudden acceleration/braking (4) Service every 3000 km (5) Use correct grade engine oil. Log every fill-up in AISA to track your mileage trend!";
        if (q.contains("service") || q.contains("servicing") || q.contains("maintenance"))
            return "🔧 Standard bike servicing every 3000 km or 3 months: ✅ Engine oil change ✅ Air filter cleaning ✅ Brake pad inspection ✅ Chain lubrication ✅ Tyre pressure check. Check the Servicing page in AISA for your personalized AI prediction of next service date!";
        if (q.contains("fuel") || q.contains("petrol") || q.contains("diesel") || q.contains("fill"))
            return "⛽ Fuel tips: Fill up in the morning (fuel is denser). Use correct octane rating for your bike. Avoid overfilling. Keep throttle smooth. Log every fill-up in AISA Add Fuel Log section for accurate mileage tracking!";
        if (q.contains("engine") || q.contains("health") || q.contains("engine health"))
            return "🔩 Check Engine Health page in AISA for anomaly detection! Z-score below -2 means abnormal mileage drop — a warning sign. Regular oil changes every 2000-3000 km prevent engine wear. Watch for unusual sounds, excessive smoke, or power loss.";
        if (q.contains("tyre") || q.contains("tire") || q.contains("pressure") || q.contains("wheel"))
            return "🛞 Tyre tips: Check pressure every 2 weeks. Correct pressure: Front 28-32 PSI, Rear 32-36 PSI (varies by bike). Under-inflation causes heat buildup and reduced mileage. Replace when tread depth is below 2mm or after 25,000 km.";
        if (q.contains("oil") || q.contains("engine oil") || q.contains("lubricant"))
            return "🛢️ Engine oil: Change mineral oil every 2000-3000 km, synthetic every 5000 km. Always use manufacturer-recommended grade (e.g., 10W-30 for most bikes). Fresh oil improves performance, mileage, and engine life!";
        if (q.contains("chain") || q.contains("sprocket"))
            return "⛓️ Chain care: Lubricate every 500-700 km with chain lube. Check tension — 20-30mm slack for most bikes. Clean every 1000 km. A worn chain reduces mileage and can damage sprockets. Replace chain + sprockets together every 25,000 km.";
        if (q.contains("brake") || q.contains("braking"))
            return "🛑 Brake tips: Inspect pads every 5000 km. Replace when thickness below 2mm. Bleed hydraulic brakes annually. Use engine braking on descents to reduce brake wear. Properly functioning brakes are critical for safety!";
        if (q.contains("battery") || q.contains("battery health"))
            return "🔋 Bike battery: Typical lifespan 2-3 years. Check electrolyte monthly. Signs of weak battery: slow cranking, dim lights. Keep terminals clean. If bike hasn't run in weeks, use a trickle charger. Voltage below 12V — time to replace!";
        if (q.contains("speed") || q.contains("rpm") || q.contains("riding"))
            return "⚡ Optimal riding: Stay at 40-60 km/h for best fuel economy. Keep RPM between 3000-5000. Avoid riding above 80 km/h for long periods. Use highest gear possible without lugging the engine. Smooth throttle inputs save 15-20% fuel!";
        if (q.contains("prediction") || q.contains("predict") || q.contains("ai") || q.contains("machine learning"))
            return "🤖 AISA uses Weighted Moving Average + Z-score anomaly detection for predictions. The AI learns from your fuel logs to predict next trip mileage and detect engine anomalies. Add at least 3-5 fuel logs for accurate predictions. More data = better AI!";
        if (q.contains("cost") || q.contains("money") || q.contains("expense") || q.contains("price"))
            return "💰 Track all costs in Trip Analytics. Your Cost per KM shows fuel efficiency in money terms. Average bike cost: ₹2-5/km. Regular servicing prevents expensive repairs — a ₹500 oil change can save ₹5000 in engine repairs!";
        if (q.contains("2 wheeler") || q.contains("2wheeler") || q.contains("bike") || q.contains("scooter") || q.contains("motorcycle"))
            return "🏍️ 2-Wheeler tips specific to AISA: Log every fill-up including partial fills. Record odometer precisely. Note road type (City/Highway/Mixed/Hilly) for accurate analysis. Check Engine Health page weekly. Set servicing reminders!";
        if (q.contains("4 wheeler") || q.contains("4wheeler") || q.contains("car"))
            return "🚗 4-Wheeler fuel tracking: Cars typically achieve 12-20 km/L. Highway gives 20-30% better mileage than city. A/C usage reduces mileage by 10-15%. Log every fill-up in AISA for accurate expense tracking!";
        if (q.contains("how") || q.contains("use") || q.contains("help") || q.contains("guide"))
            return "📖 How to use AISA Idea: 1️⃣ Register & Login 2️⃣ Go to Add Fuel Log — fill details after every fuel-up 3️⃣ Dashboard shows AI mileage predictions 4️⃣ Engine Health detects anomalies 5️⃣ Servicing page predicts next service 6️⃣ Statistics gives deep analytics!";
        if (q.contains("dashboard"))
            return "📊 Your AISA Dashboard shows: Real-time avg mileage, Total cost, AI-predicted next trip mileage, Engine health status, Riding score (0-100), Recent trips, and Servicing prediction. All updated automatically every time you add a fuel log!";
        if (q.contains("register") || q.contains("signup") || q.contains("account") || q.contains("login"))
            return "👤 To use AISA: Click Register, enter your name, username, email and password. Login with your username & password. Each user has their own private dashboard, fuel logs, and vehicle data — fully secure!";
        if (q.contains("thanks") || q.contains("thank you") || q.contains("ty"))
            return "😊 You're welcome! I'm always here to help you get the best from your bike. Keep logging your fuel data in AISA for smarter AI predictions. Ride safe! 🏍️";
        return "🤔 Good question! I'm AISA_Vicky. I can help with: 🔧 Bike servicing & maintenance | ⛽ Fuel efficiency tips | 🔩 Engine health | 🛞 Tyre care | 🛢️ Oil changes | 📊 Understanding your AISA dashboard. Try asking about any of these topics!";
    }
}
