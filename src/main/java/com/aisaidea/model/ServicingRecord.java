package com.aisaidea.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "servicing_records")
public class ServicingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate serviceDate = LocalDate.now();
    private Double odometerAtService;
    private String serviceType;
    private String servicedAt;
    private Double serviceCost;

    @Column(length = 500)
    private String partsReplaced;

    @Column(length = 500)
    private String technicianNotes;

    private String vehicleName;
    private String vehicleCompany;
    private String vehicleType;
    private String status = "Completed";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }
    public Double getOdometerAtService() { return odometerAtService; }
    public void setOdometerAtService(Double odometerAtService) { this.odometerAtService = odometerAtService; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getServicedAt() { return servicedAt; }
    public void setServicedAt(String servicedAt) { this.servicedAt = servicedAt; }
    public Double getServiceCost() { return serviceCost; }
    public void setServiceCost(Double serviceCost) { this.serviceCost = serviceCost; }
    public String getPartsReplaced() { return partsReplaced; }
    public void setPartsReplaced(String partsReplaced) { this.partsReplaced = partsReplaced; }
    public String getTechnicianNotes() { return technicianNotes; }
    public void setTechnicianNotes(String technicianNotes) { this.technicianNotes = technicianNotes; }
    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public String getVehicleCompany() { return vehicleCompany; }
    public void setVehicleCompany(String vehicleCompany) { this.vehicleCompany = vehicleCompany; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
