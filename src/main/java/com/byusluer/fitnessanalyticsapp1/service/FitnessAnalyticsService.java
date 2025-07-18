package com.byusluer.fitnessanalyticsapp1.service;

import com.byusluer.fitnessanalyticsapp1.model.ActivitySession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FitnessAnalyticsService {

    // 1. Total Calories Burned

    public Integer getTotalCalories(List<ActivitySession> sessions) {

        return sessions.stream()
                .map(ActivitySession::getCaloriesBurned)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    // 2. Average Calories Burned

    public Double getAverageCaloriesBurned(List<ActivitySession> sessions) {

        return sessions.stream()
                .map(ActivitySession::getCaloriesBurned)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    // 3. Total calories burned by workout type

    public Map<String, Integer> getTotalCaloriesByWoType(List<ActivitySession> sessions) {

        return sessions.stream().filter(activitySession -> activitySession.getCaloriesBurned() != null &&
                        activitySession.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType,
                        Collectors.summingInt(ActivitySession::getCaloriesBurned)));

    }

    // 4. Top 5 most frequent workout types

    public List<String> getTopWoTypes(List<ActivitySession> sessions) {
        Map<String, Long> frequencyMap = sessions.stream()
                .filter(s -> s.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

    }

    // 5. Total Workout Duration (Hours)

    public Double calculateTotalWoDuration(List<ActivitySession> sessions) {
        return sessions.stream()
                .map(ActivitySession::getSessionDurationHours)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    // [6] Average Workout Duration (in hours)

    public Double getAverageWoDuration(List<ActivitySession> sessions) {
        return sessions.stream()
                .map(ActivitySession::getSessionDurationHours)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    // [7] Average workout duration by workout type

    public Map<String, Double> getAveWoDurationByType(List<ActivitySession> sessions) {
        return sessions.stream()
                .filter(s -> s.getSessionDurationHours() != null
                        && s.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType, Collectors.averagingDouble(ActivitySession::getSessionDurationHours)));
    }

    //[8] Most common workout type

    public Map<String, Long> getMostFrequentWo(List<ActivitySession> sessions) {
        Map<String, Long> workOutCountMap = sessions.stream()
                .filter(s -> s.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType, Collectors.counting()));

        return workOutCountMap
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .orElse(Map.of());

    }

    // [9] Workout frequency distribution (how many users workout X times per week)

    public Map<Integer, Long> getWorkOutFrequency(List<ActivitySession> sessions) {
        return sessions.stream()
                .map(ActivitySession::getWorkoutFrequencyPerWeek)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(frequency -> frequency, Collectors.counting()));
    }

    // [10] Which workout type has the highest total duration?

    public Map<String, Double> getWorkoutTypeWithMaxTotalDuration(List<ActivitySession> sessions) {

        Map<String, Double> typeDurationMap = sessions.stream()
                .filter(s -> s.getSessionDurationHours() != null && s.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType, Collectors.summingDouble(ActivitySession::getSessionDurationHours)));

        return typeDurationMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .orElse(Map.of("Unknown", 0.0));

    }

    //  [11] Longest single workout duration →

    public Map<String, Double> getLongestWoSession(List<ActivitySession> sessions) {


        return sessions.stream()
                .filter(s -> s.getWorkoutType() != null && s.getSessionDurationHours() != null)
                .max(Comparator.comparing(ActivitySession::getSessionDurationHours))
                .map(s -> Map.of(s.getWorkoutType(), s.getSessionDurationHours()))
                .orElse(Map.of("Unknown", 0.0));

    }

    // [12] Total water intake by workout type

    public Map<String, Double> getTotalWaterIntakeByWoType(List<ActivitySession> sessions) {

        Map<String, Double> intakeMap = sessions.stream()
                .filter(s -> s.getWaterIntakeLiters() != null && s.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType, Collectors.summingDouble(ActivitySession::getWaterIntakeLiters)));

        return intakeMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    //  [13] Total calories burned by gender

    public Map<String, Double> getTotalCaloriesByGender(List<ActivitySession> sessions) {

        return sessions.stream()
                .filter(s -> s.getCaloriesBurned() != null && s.getGender() != null)
                .collect(Collectors.groupingBy(ActivitySession::getGender, Collectors.summingDouble(ActivitySession::getCaloriesBurned)));

    }

    // [14] Average fat percentage by experience level

    public Map<Double, Double> getAverageFatPercentageByExperience(List<ActivitySession> sessions) {

        return sessions.stream().filter(s -> s.getFatPercentage() != null
                        && s.getExperienceLevel() != null)
                .collect(Collectors.groupingBy(ActivitySession::getExperienceLevel, Collectors.averagingDouble(ActivitySession::getFatPercentage)));


    }

    // [15] Calculate average BMI by workout type


    public Map<String, Double> getAverageBMIbyWoType(List<ActivitySession> sessions) {

        return sessions.stream()
                .filter(s -> s.getBmi() != null
                        && s.getWorkoutType() != null)
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType, Collectors.averagingDouble(ActivitySession::getBmi)));


    }


    // How many activity sessions burned more than 500 calories?

    public Long getCaloriesBurned(List<ActivitySession> sessions) {

        return sessions.stream().filter(s -> s.getCaloriesBurned() != null && s.getCaloriesBurned() > 500).count();

    }

    public Map<Double, Long> getExperienceLevelPerSession(List<ActivitySession> sessions) {

        return sessions.stream().filter(s -> s.getExperienceLevel() != null)
                .collect(Collectors.groupingBy(ActivitySession::getExperienceLevel,
                        Collectors.counting()
                ));

    }

   // For each experience level, find the session with the longest duration.

    public Map<Double, ActivitySession> getMaxDurationPerExpLevel(List<ActivitySession> sessions){

        return sessions.stream().filter(s->s.getExperienceLevel()!=null && s.getSessionDurationHours()!=null)
                .collect(Collectors.groupingBy(ActivitySession::getExperienceLevel,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(ActivitySession::getSessionDurationHours))
                        , optional -> optional.orElse(null))));
    }

}
