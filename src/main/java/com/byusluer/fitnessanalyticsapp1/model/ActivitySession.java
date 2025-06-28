package com.byusluer.fitnessanalyticsapp1.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySession {

    private Integer age;
    private String gender;
    private Double weightKg;
    private Double heightM;

    private Integer maxBpm;
    private Integer avgBpm;
    private Integer restingBpm;

    private Double sessionDurationHours;
    private Integer caloriesBurned;

    private String workoutType;
    private Double fatPercentage;
    private Double waterIntakeLiters;

    private Integer workoutFrequencyPerWeek;
    private Double experienceLevel;
    private Double bmi;

}
