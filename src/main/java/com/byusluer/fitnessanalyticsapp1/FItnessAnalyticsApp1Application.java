package com.byusluer.fitnessanalyticsapp1;

import com.byusluer.fitnessanalyticsapp1.model.ActivitySession;
import com.byusluer.fitnessanalyticsapp1.service.CsvLoaderService;
import com.byusluer.fitnessanalyticsapp1.service.FitnessAnalyticsService;
import com.byusluer.fitnessanalyticsapp1.util.NumberFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class FItnessAnalyticsApp1Application implements CommandLineRunner {

    final CsvLoaderService csvLoaderService;
    final FitnessAnalyticsService analyticsService;

    public static void main(String[] args) {
        SpringApplication.run(FItnessAnalyticsApp1Application.class, args);
    }

    @Override
    public void run(String... args) {
        List<ActivitySession> sessions = csvLoaderService.loadSessionsFromCsv("fitness_data.csv");


        Integer totalCalories = analyticsService.getTotalCalories(sessions);
        log.info("🔥 [1] Total calories burned: {} kcal", totalCalories);


        Double avgCalories = analyticsService.getAverageCaloriesBurned(sessions);
        log.info("🔥 [2] Average calories burned per session: {} kcal", NumberFormatter.formatDouble(avgCalories));

        Map<String, Integer> caloriesByWorkout = analyticsService.getTotalCaloriesByWoType(sessions);

        log.info("🔥 [3] Total calories burned by workout type:");
        caloriesByWorkout.forEach((type, total) ->
                log.info("   → {} : {} kcal", type, total));

        log.info("🔥 → [4] Top 5 Most Frequent Workout Types:");
        analyticsService.getTopWoTypes(sessions)
                .forEach(type -> log.info("\t• " + type));

        Double hours = analyticsService.calculateTotalWoDuration(sessions);
        log.info("🔥 [5] Total workout duration:: {} hours", NumberFormatter.formatDouble(hours));

        Double avgHours = analyticsService.getAverageWoDuration(sessions);
        log.info("🔥 [6] Average workout duration:: {} hours", NumberFormatter.formatDouble(avgHours));

        log.info("🔥 [7] Average workout duration by workout type:");
        Map<String, Double> avgDurationByType = analyticsService.getAveWoDurationByType(sessions);
        avgDurationByType.forEach((type, avg) -> log.info("   → \t{} : {} hours", type, NumberFormatter.formatDouble(avg)));


        Map<String, Long> mostFrequent = analyticsService.getMostFrequentWo(sessions);
        mostFrequent.forEach((type, count) ->
                log.info("🔥 [8] Most frequent workout → \t{} : {} times", type, count));


        Map<Integer, Long> frequencyMap = analyticsService.getWorkOutFrequency(sessions);
        frequencyMap.forEach((freq, count) ->
                log.info("🔥 [9] Workout frequency → \t{} : {} sessions", freq, count));

        Map<String, Double> maxDurationMap = analyticsService.getWorkoutTypeWithMaxTotalDuration(sessions);
        maxDurationMap.forEach((type, duration) ->
                log.info("🔥 [10] Max total duration → \t{} : {} hours", type, NumberFormatter.formatDouble(duration))
        );

        Map<String, Double> longestSession = analyticsService.getLongestWoSession(sessions);
        longestSession.forEach((type, duration) ->
                log.info("🔥 [11] Longest workout session → \t{} : {} hours", type, NumberFormatter.formatDouble(duration))
        );

        Map<String, Double> waterMap = analyticsService.getTotalWaterIntakeByWoType(sessions);
        waterMap.forEach((type, total) ->
                log.info("🔥 [12] Total water intake → \t{} : {} liters", type, NumberFormatter.formatDouble(total))
        );

        analyticsService.getTotalCaloriesByGender(sessions)
                .forEach((gender, calories) ->
                        log.info("🔥 [13] Calories burned by gender → \t{} : {} kcal", gender, calories));

        analyticsService.getAverageFatPercentageByExperience(sessions)
                .forEach((level, avgFat) ->
                        log.info("🔥 [14] Avg fat percentage by experience → \tLevel {} : {}%",
                                NumberFormatter.formatDouble(level),
                                NumberFormatter.formatDouble(avgFat)));

        analyticsService.getAverageBMIbyWoType(sessions)
                .forEach((type, avgBmi) ->
                        log.info("🔥 [15] Avg BMI by workout type → \t{} : {}",
                                type, NumberFormatter.formatDouble(avgBmi)));
    }



}
