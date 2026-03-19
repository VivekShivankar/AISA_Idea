package com.aisaidea.service;

import com.aisaidea.model.FuelLog;
import com.aisaidea.repository.FuelLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FuelLogService {

    @Autowired
    private FuelLogRepository repo;

    private double r1(double v) { return Math.round(v * 10.0) / 10.0; }
    private double r2(double v) { return Math.round(v * 100.0) / 100.0; }
    private double r3(double v) { return Math.round(v * 1000.0) / 1000.0; }
    private double safe(Double v) { return v != null ? v : 0.0; }

    public FuelLog save(FuelLog log) {
        double dist = log.getCurrOdometer() - log.getPrevOdometer();
        log.setDistance(r2(dist));
        log.setMileage(r2(dist / log.getFuelLiters()));
        log.setTotalCost(r2(log.getFuelLiters() * log.getFuelPrice()));
        return repo.save(log);
    }

    public List<FuelLog> getAll(Long uid) { return repo.findByUserIdOrderByDateDesc(uid); }
    public List<FuelLog> getRecent(Long uid) { return repo.findTop5ByUserIdOrderByDateDesc(uid); }
    public void delete(Long id) { repo.deleteById(id); }
    public Optional<FuelLog> findById(Long id) { return repo.findById(id); }

    public Map<String, Object> stats(Long uid) {
        Map<String, Object> s = new HashMap<>();
        double avg = safe(repo.avgMileage(uid));
        double tot = safe(repo.totalCost(uid));
        double dist = safe(repo.totalDist(uid));
        double fuel = safe(repo.totalFuel(uid));
        s.put("avgMileage",    r1(avg));
        s.put("totalCost",     (long) Math.round(tot));
        s.put("totalDistance", r1(dist));
        s.put("totalFuel",     r1(fuel));
        s.put("bestMileage",   r1(safe(repo.maxMileage(uid))));
        s.put("worstMileage",  r1(safe(repo.minMileage(uid))));
        s.put("tripCount",     repo.countLogs(uid));
        s.put("latestOdo",     r1(safe(repo.latestOdo(uid))));
        s.put("costPerKm",     dist > 0 ? r2(tot / dist) : 0.0);
        s.put("estimatedRange",(long)(avg * 3));
        s.put("avgLow",        r1(safe(repo.avgByTraffic(uid, "Low"))));
        s.put("avgMed",        r1(safe(repo.avgByTraffic(uid, "Medium"))));
        s.put("avgHigh",       r1(safe(repo.avgByTraffic(uid, "High"))));
        s.put("avgCity",       r1(safe(repo.avgByRoad(uid, "City"))));
        s.put("avgHighway",    r1(safe(repo.avgByRoad(uid, "Highway"))));
        s.put("avgMixed",      r1(safe(repo.avgByRoad(uid, "Mixed"))));
        s.put("avgHilly",      r1(safe(repo.avgByRoad(uid, "Hilly"))));
        return s;
    }

    public Map<String, Object> ai(Long uid) {
        Map<String, Object> r = new HashMap<>();
        List<FuelLog> logs = repo.findByUserIdOrderByDateDesc(uid);
        if (logs.isEmpty()) {
            r.put("predictedMileage", 0.0);
            r.put("tip", "Add fuel logs to get AI predictions.");
            r.put("healthStatus", "Unknown");
            r.put("healthColor", "var(--muted)");
            r.put("healthMsg", "No data available.");
            r.put("ridingScore", 0);
            r.put("zScore", 0.0); r.put("correlation", 0.0);
            r.put("stdDev", 0.0); r.put("variance", 0.0); r.put("meanMileage", 0.0);
            return r;
        }
        double[] m  = logs.stream().mapToDouble(FuelLog::getMileage).toArray();
        double[] sp = logs.stream().mapToDouble(l -> l.getAvgSpeed() != null ? l.getAvgSpeed() : 40.0).toArray();

        // Weighted moving average
        double sw = 0, mean = 0;
        for (int i = 0; i < m.length; i++) { double w = m.length - i; mean += m[i] * w; sw += w; }
        mean /= sw;

        double var = 0; for (double v : m) var += Math.pow(v - mean, 2); var /= m.length;
        double sd = Math.sqrt(var);
        double z  = sd > 0 ? (m[0] - mean) / sd : 0;

        double sm = 0; for (double v : sp) sm += v; sm /= sp.length;
        double num = 0, dx = 0, dy = 0;
        for (int i = 0; i < m.length; i++) {
            num += (sp[i] - sm) * (m[i] - mean);
            dx  += Math.pow(sp[i] - sm, 2);
            dy  += Math.pow(m[i] - mean, 2);
        }
        double corr = Math.sqrt(dx * dy) == 0 ? 0 : num / Math.sqrt(dx * dy);
        double pred = mean + (m.length >= 2 ? (m[0] - m[m.length - 1]) * 0.1 : 0);

        String hs, hm, hc;
        if      (z < -2) { hs = "Warning"; hm = "Abnormal mileage drop! Check engine."; hc = "var(--danger)"; }
        else if (z < -1) { hs = "Caution"; hm = "Mileage slightly below average.";      hc = "var(--warning)"; }
        else             { hs = "Healthy"; hm = "Engine health looks normal. Keep it up!"; hc = "var(--green)"; }

        int score = (int) Math.min(
            Math.round(Math.min(mean / 60.0 * 50.0, 50.0) + (sm >= 30 && sm <= 60 ? 50 : 35)), 100);
        String tip = mean >= 40 ? "✔ Excellent efficiency! Maintain 40-60 km/h for best mileage."
            : mean >= 35 ? "⚡ Good mileage. Avoid heavy traffic to improve."
            : "⚠ Below average. Try highway trips and reduce idling.";

        r.put("predictedMileage", r2(pred));
        r.put("tip", tip);
        r.put("healthStatus", hs); r.put("healthMsg", hm); r.put("healthColor", hc);
        r.put("ridingScore", score);
        r.put("zScore", r2(z)); r.put("correlation", r2(corr));
        r.put("stdDev", r3(sd)); r.put("variance", r3(var)); r.put("meanMileage", r1(mean));
        return r;
    }
}
