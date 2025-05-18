package com.project.Justick.Service.Onion;


import com.project.Justick.DTO.Onion.OnionRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.OnionPredict;
import com.project.Justick.Repository.Onion.OnionPredictRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OnionPredictService {

    private final OnionPredictRepository repo;

    public OnionPredictService(OnionPredictRepository repo) {
        this.repo = repo;
    }

    //
    @Transactional
    public void saveAll(List<OnionRequest> requests) {
        List<OnionPredict> entities = requests.stream()
                .map(this::toEntity)
                .toList();
        repo.saveAll(entities);
    }

    private OnionPredict toEntity(OnionRequest req) {
        OnionPredict e = new OnionPredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setIntake(req.getIntake());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<OnionPredict> all = repo.findByGrade(grade);

        Map<String, Map<String, Integer>> grouped = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .collect(Collectors.groupingBy(
                        c -> {
                            LocalDate d = LocalDate.of(c.getYear(), c.getMonth(), c.getDay());
                            int weekOfMonth = d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                            return String.format("%04d-%02d-%02d", d.getYear(), d.getMonthValue(), weekOfMonth);
                        },
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(OnionPredict::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(OnionPredict::getIntake).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));

        return grouped.entrySet().stream()
                .skip(Math.max(0, grouped.size() - 12))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }


    public Map<String, Map<String, Integer>> getMonthlyAverages(Grade grade) {
        List<OnionPredict> all = repo.findByGrade(grade);

        Map<String, Map<String, Integer>> grouped = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(OnionPredict::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(OnionPredict::getIntake).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));

        return grouped.entrySet().stream()
                .skip(Math.max(0, grouped.size() - 12))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }

    public List<OnionPredict> findRecent20DaysWithForecast(Grade grade) {
        OnionPredict latest = repo.findLatestByGrade(grade);
        if (latest == null) return List.of();

        LocalDate latestDate = LocalDate.of(latest.getYear(), latest.getMonth(), latest.getDay());

        LocalDate from = latestDate.minusDays(15);
        LocalDate to = latestDate.plusDays(5);

        return repo.findByDateRangeAndGrade(
                from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                to.getYear(), to.getMonthValue(), to.getDayOfMonth(),
                grade
        );
    }

}
