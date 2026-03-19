package com.aisaidea.service;

import com.aisaidea.model.ServicingRecord;
import com.aisaidea.repository.ServicingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class ServicingService {

    @Autowired
    private ServicingRepository repo;

    public ServicingRecord save(ServicingRecord s) { return repo.save(s); }
    public List<ServicingRecord> getAll(Long uid) { return repo.findByUserIdOrderByServiceDateDesc(uid); }
    public void delete(Long id) { repo.deleteById(id); }

    public Map<String, Object> prediction(Long uid, double currentOdo) {
        Map<String, Object> r = new HashMap<>();
        Double lastOdoVal  = repo.lastOdo(uid);
        Double totalCostVal = repo.totalCost(uid);
        Long   svcCount    = repo.countSvc(uid);

        double lastOdo = lastOdoVal != null ? lastOdoVal : 0.0;
        double kmSince = currentOdo - lastOdo;
        int    interval = 3000;
        double nextAt  = lastOdo + interval;
        double kmLeft  = Math.max(0, nextAt - currentOdo);

        String hc, hs, urg;
        if      (kmSince >= interval)        { hs = "Service Overdue";  hc = "var(--danger)";  urg = "IMMEDIATE"; }
        else if (kmSince >= interval * 0.85) { hs = "Service Due Soon"; hc = "var(--warning)"; urg = "URGENT";    }
        else if (kmSince >= interval * 0.60) { hs = "Plan Service";     hc = "var(--warning)"; urg = "SCHEDULE";  }
        else                                 { hs = "Good Condition";   hc = "var(--green)";   urg = "OK";        }

        long daysLeft = (long) Math.max(0, kmLeft / 50.0);
        LocalDate predDate = LocalDate.now().plusDays(daysLeft);

        double avgCost = (svcCount != null && svcCount > 0 && totalCostVal != null)
            ? totalCostVal / svcCount : 800.0;
        double predCost = Math.round(avgCost * 1.05);

        List<String> checks = new ArrayList<>();
        checks.add("Engine Oil Change");
        checks.add("Air Filter Inspection");
        checks.add("Brake Pad Check");
        if (kmSince > 5000)  checks.add("Spark Plug Replacement");
        if (kmSince > 8000)  checks.add("Chain Lubrication & Tension");
        if (kmSince > 10000) checks.add("Tyre Tread & Pressure Check");
        if (kmSince > 15000) checks.add("Battery Health Check");
        checks.add("Coolant Level Check");
        checks.add("Lights & Electricals");

        int pct = (int) Math.min(100, (kmSince / interval) * 100);

        r.put("kmSince", Math.round(kmSince));
        r.put("kmLeft",  Math.round(kmLeft));
        r.put("nextAt",  Math.round(nextAt));
        r.put("healthCondition", hs);
        r.put("healthColor", hc);
        r.put("urgency", urg);
        r.put("predictedDate", predDate.toString());
        r.put("predictedCost", predCost);
        r.put("svcCount", svcCount != null ? svcCount : 0L);
        r.put("totalSvcCost", totalCostVal != null ? Math.round(totalCostVal) : 0L);
        r.put("checkList", checks);
        r.put("interval", interval);
        r.put("lastOdo", Math.round(lastOdo));
        r.put("pct", pct);
        return r;
    }
}
