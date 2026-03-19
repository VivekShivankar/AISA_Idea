package com.aisaidea.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fuel_logs")
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate date = LocalDate.now();
    private Double fuelLiters;
    private Double fuelPrice;
    private Double prevOdometer;
    private Double currOdometer;
    private Double distance;
    private Double mileage;
    private Double avgSpeed = 40.0;
    private Double temperature = 25.0;
    private Double totalCost;
    private String roadType = "City";
    private String trafficLevel = "Low";
    private String vehicleType = "2-Wheeler";
    private String vehicleName;
    private String vehicleCompany;
    private String fuelType = "Petrol";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Double getFuelLiters() { return fuelLiters; }
    public void setFuelLiters(Double fuelLiters) { this.fuelLiters = fuelLiters; }
    public Double getFuelPrice() { return fuelPrice; }
    public void setFuelPrice(Double fuelPrice) { this.fuelPrice = fuelPrice; }
    public Double getPrevOdometer() { return prevOdometer; }
    public void setPrevOdometer(Double prevOdometer) { this.prevOdometer = prevOdometer; }
    public Double getCurrOdometer() { return currOdometer; }
    public void setCurrOdometer(Double currOdometer) { this.currOdometer = currOdometer; }
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    public Double getMileage() { return mileage; }
    public void setMileage(Double mileage) { this.mileage = mileage; }
    public Double getAvgSpeed() { return avgSpeed; }
    public void setAvgSpeed(Double avgSpeed) { this.avgSpeed = avgSpeed; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public String getRoadType() { return roadType; }
    public void setRoadType(String roadType) { this.roadType = roadType; }
    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public String getVehicleCompany() { return vehicleCompany; }
    public void setVehicleCompany(String vehicleCompany) { this.vehicleCompany = vehicleCompany; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
}
