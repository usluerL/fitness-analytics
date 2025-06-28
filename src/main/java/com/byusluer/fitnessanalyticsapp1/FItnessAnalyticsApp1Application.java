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



    }

}
