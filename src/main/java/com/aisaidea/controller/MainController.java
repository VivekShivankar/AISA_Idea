package com.aisaidea.controller;

import com.aisaidea.model.*;
import com.aisaidea.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class MainController {

    @Autowired private UserService     userService;
    @Autowired private FuelLogService  fuelLogService;
    @Autowired private ServicingService servicingService;

    private User getUser(UserDetails ud) {
        return userService.findByUsername(ud.getUsername()).orElseThrow();
    }

    private void common(Model m, User u, String page) {
        m.addAttribute("user", u);
        m.addAttribute("activePage", page);
    }

    // ── Dashboard ────────────────────────────────
    @GetMapping({"/", "/dashboard"})
    public String dashboard(@AuthenticationPrincipal UserDetails ud, Model m) {
        User u = getUser(ud);
        common(m, u, "dashboard");
        Map<String,Object> st = fuelLogService.stats(u.getId());
        double odo = st.get("latestOdo") instanceof Number n ? n.doubleValue() : 0.0;
        m.addAttribute("stats",      st);
        m.addAttribute("ai",         fuelLogService.ai(u.getId()));
        m.addAttribute("recentLogs", fuelLogService.getRecent(u.getId()));
        m.addAttribute("svcPred",    servicingService.prediction(u.getId(), odo));
        return "dashboard";
    }

    // ── Add Fuel Log ─────────────────────────────
    @GetMapping("/add")
    public String addPage(@AuthenticationPrincipal UserDetails ud, Model m) {
        common(m, getUser(ud), "add");
        return "add-log";
    }

    @PostMapping("/add")
    public String saveLog(@AuthenticationPrincipal UserDetails ud,
            @RequestParam Double fuelLiters,
            @RequestParam Double fuelPrice,
            @RequestParam Double prevOdometer,
            @RequestParam Double currOdometer,
            @RequestParam(defaultValue = "40.0")  Double avgSpeed,
            @RequestParam(defaultValue = "25.0")  Double temperature,
            @RequestParam(defaultValue = "City")   String roadType,
            @RequestParam(defaultValue = "Low")    String trafficLevel,
            @RequestParam(defaultValue = "2-Wheeler") String vehicleType,
            @RequestParam(required = false)        String vehicleName,
            @RequestParam(required = false)        String vehicleCompany,
            @RequestParam(defaultValue = "Petrol") String fuelType,
            @RequestParam(required = false)        String date,
            RedirectAttributes ra) {
        try {
            if (currOdometer <= prevOdometer) {
                ra.addFlashAttribute("errorMsg", "Current odometer must be greater than previous!");
                return "redirect:/add";
            }
            User u = getUser(ud);
            FuelLog log = new FuelLog();
            log.setUser(u);
            log.setFuelLiters(fuelLiters);
            log.setFuelPrice(fuelPrice);
            log.setPrevOdometer(prevOdometer);
            log.setCurrOdometer(currOdometer);
            log.setAvgSpeed(avgSpeed);
            log.setTemperature(temperature);
            log.setRoadType(roadType);
            log.setTrafficLevel(trafficLevel);
            log.setVehicleType(vehicleType);
            log.setVehicleName(vehicleName);
            log.setVehicleCompany(vehicleCompany);
            log.setFuelType(fuelType);
            if (date != null && !date.isBlank()) log.setDate(LocalDate.parse(date));
            fuelLogService.save(log);
            ra.addFlashAttribute("successMsg", "✔ Fuel log saved successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/add";
    }

    // ── Trip Analytics ───────────────────────────
    @GetMapping("/trips")
    public String trips(@AuthenticationPrincipal UserDetails ud, Model m) {
        User u = getUser(ud);
        common(m, u, "trips");
        m.addAttribute("allLogs", fuelLogService.getAll(u.getId()));
        m.addAttribute("stats",   fuelLogService.stats(u.getId()));
        return "trips";
    }

    // ── Engine Health ────────────────────────────
    @GetMapping("/engine")
    public String engine(@AuthenticationPrincipal UserDetails ud, Model m) {
        User u = getUser(ud);
        common(m, u, "engine");
        m.addAttribute("ai", fuelLogService.ai(u.getId()));
        return "engine";
    }

    // ── Statistics ───────────────────────────────
    @GetMapping("/statistics")
    public String statistics(@AuthenticationPrincipal UserDetails ud, Model m) {
        User u = getUser(ud);
        common(m, u, "statistics");
        m.addAttribute("stats",   fuelLogService.stats(u.getId()));
        m.addAttribute("ai",      fuelLogService.ai(u.getId()));
        m.addAttribute("allLogs", fuelLogService.getAll(u.getId()));
        return "statistics";
    }

    // ── Servicing ────────────────────────────────
    @GetMapping("/servicing")
    public String servicing(@AuthenticationPrincipal UserDetails ud, Model m) {
        User u = getUser(ud);
        common(m, u, "servicing");
        double odo = ((Number) fuelLogService.stats(u.getId()).get("latestOdo")).doubleValue();
        m.addAttribute("records", servicingService.getAll(u.getId()));
        m.addAttribute("pred",    servicingService.prediction(u.getId(), odo));
        return "servicing";
    }

    @PostMapping("/servicing/save")
    public String saveSvc(@AuthenticationPrincipal UserDetails ud,
            @RequestParam(required = false) String  serviceDate,
            @RequestParam(required = false) Double  odometerAtService,
            @RequestParam(required = false) String  serviceType,
            @RequestParam(required = false) String  servicedAt,
            @RequestParam(required = false) Double  serviceCost,
            @RequestParam(required = false) String  partsReplaced,
            @RequestParam(required = false) String  technicianNotes,
            @RequestParam(required = false) String  vehicleName,
            @RequestParam(required = false) String  vehicleCompany,
            @RequestParam(required = false) String  vehicleType,
            @RequestParam(defaultValue = "Completed") String status,
            RedirectAttributes ra) {
        User u = getUser(ud);
        ServicingRecord s = new ServicingRecord();
        s.setUser(u);
        if (serviceDate != null && !serviceDate.isBlank()) s.setServiceDate(LocalDate.parse(serviceDate));
        s.setOdometerAtService(odometerAtService);
        s.setServiceType(serviceType);
        s.setServicedAt(servicedAt);
        s.setServiceCost(serviceCost);
        s.setPartsReplaced(partsReplaced);
        s.setTechnicianNotes(technicianNotes);
        s.setVehicleName(vehicleName);
        s.setVehicleCompany(vehicleCompany);
        s.setVehicleType(vehicleType);
        s.setStatus(status);
        servicingService.save(s);
        ra.addFlashAttribute("successMsg", "✔ Service record saved!");
        return "redirect:/servicing";
    }

    @PostMapping("/service/delete/{id}")
    public String deleteSvc(@PathVariable Long id, RedirectAttributes ra) {
        servicingService.delete(id);
        ra.addFlashAttribute("successMsg", "Record deleted.");
        return "redirect:/servicing";
    }

    // ── Delete Fuel Log ──────────────────────────
    @PostMapping("/delete/{id}")
    public String deleteLog(@PathVariable Long id, RedirectAttributes ra) {
        fuelLogService.delete(id);
        ra.addFlashAttribute("successMsg", "Log deleted.");
        return "redirect:/trips";
    }

    // ── Profile ──────────────────────────────────
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails ud, Model m) {
        User u = getUser(ud);
        common(m, u, "profile");
        m.addAttribute("stats", fuelLogService.stats(u.getId()));
        return "profile";
    }

    @PostMapping("/profile/save")
    public String saveProfile(@AuthenticationPrincipal UserDetails ud,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String city,
            RedirectAttributes ra) {
        User u = getUser(ud);
        if (fullName != null && !fullName.isBlank()) u.setFullName(fullName.trim());
        if (phone    != null) u.setPhone(phone.trim());
        if (city     != null) u.setCity(city.trim());
        userService.save(u);
        ra.addFlashAttribute("successMsg", "✔ Profile updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/upload-image")
    public String uploadImage(@AuthenticationPrincipal UserDetails ud,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes ra) {
        try {
            userService.uploadImage(ud.getUsername(), file);
            ra.addFlashAttribute("successMsg", "✔ Profile photo updated!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Upload failed: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}
