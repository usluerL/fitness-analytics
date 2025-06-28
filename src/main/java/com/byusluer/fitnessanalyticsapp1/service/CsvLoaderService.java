package com.byusluer.fitnessanalyticsapp1.service;

import com.byusluer.fitnessanalyticsapp1.model.ActivitySession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.replaceAll;

@Slf4j
@Service
public class CsvLoaderService {

    int totalCount = 0;
    int skippedCount = 0;

    public List<ActivitySession> loadSessionsFromCsv(String fileName) {


        List<ActivitySession> sessions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(fileName))))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                totalCount++;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // skip header
                }

                String[] tokens = line.split(",");

                if (tokens.length < 14) {
                    skippedCount++;
                    log.warn("Skipping malformed line (not enough columns): {}", line);
                    continue;
                }
                String[] paddedTokens = new String[15];
                System.arraycopy(tokens, 0, paddedTokens, 0, tokens.length);
                for (int i = tokens.length; i < 15; i++) {
                    paddedTokens[i] = "";
                }

                ActivitySession session = ActivitySession.builder()
                        .age(optionalInt(paddedTokens[0]).orElse(null))
                        .gender(paddedTokens[1])
                        .weightKg(optionalDouble(paddedTokens[2]).orElse(null))
                        .heightM(optionalDouble(paddedTokens[3]).orElse(null))
                        .maxBpm(optionalInt(paddedTokens[4]).orElse(null))
                        .avgBpm(optionalInt(paddedTokens[5]).orElse(null))
                        .restingBpm(optionalInt(paddedTokens[6]).orElse(null))
                        .sessionDurationHours(optionalDouble(paddedTokens[7]).orElse(null))
                        .caloriesBurned(optionalInt(paddedTokens[8]).orElse(null))
                        .workoutType(cleanWorkoutTypeField(paddedTokens[9]))
                        .fatPercentage(optionalDouble(paddedTokens[10]).orElse(null))
                        .waterIntakeLiters(optionalDouble(paddedTokens[11]).orElse(null))
                        .workoutFrequencyPerWeek(optionalInt(paddedTokens[12]).orElse(null))
                        .experienceLevel(optionalDouble(paddedTokens[13]).orElse(null))
                        .bmi(optionalDouble(paddedTokens[14]).orElse(null))
                        .build();


                sessions.add(session);
            }

        } catch (IOException e) {
            throw new RuntimeException("CSV file couldn't be read: " + fileName, e);
        }

        log.info("Total rows in file: {}", totalCount);
        log.info("Valid sessions loaded: {}", sessions.size());
        log.info("Skipped malformed rows: {}", skippedCount);

        return sessions;
    }


    private Optional<Double> optionalDouble(String value) {

        try {
            String sanitized = value == null ? "" : value.replaceAll("[^0-9.\\-]", "");
            return sanitized.isEmpty() ? Optional.empty() : Optional.of(Double.parseDouble(sanitized));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }


    }

    private Optional<Integer> optionalInt(String value) {

        try {
            String sanitized = value == null ? "" : value.replaceAll("[^0-9.\\-]", "");
            return sanitized.isEmpty() ? Optional.empty() : Optional.of((int) Double.parseDouble(sanitized));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String cleanWorkoutTypeField(String rawType) {
        if (rawType == null) return "Other";
        String cleaned = rawType
                .replaceAll("\\\\[tnr]", "")  // Remove escaped characters like \t \n
                .replaceAll("[\\n\\t\\r]", "") // Remove actual tab, newline, carriage return
                .trim();
        return cleaned.isEmpty() ? "Other" : cleaned;
    }
}
