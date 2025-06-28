package com.byusluer.fitnessanalyticsapp1.service;

import com.byusluer.fitnessanalyticsapp1.model.ActivitySession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
                .collect(Collectors.groupingBy(ActivitySession::getWorkoutType,
                        Collectors.averagingDouble(ActivitySession::getSessionDurationHours)));
    }

}
