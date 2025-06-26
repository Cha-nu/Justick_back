package com.project.Justick.Service;

import com.project.Justick.Domain.AgriculturalPrice;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.AgriculturalPredictRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AgriculturalPredictService<T extends AgriculturalPrice, R> {

    protected final AgriculturalPredictRepository<T> repository;

    protected AgriculturalPredictService(AgriculturalPredictRepository<T> repository) {
        this.repository = repository;
    }

    protected abstract T toEntity(R request);

    @Transactional
    public void saveAll(List<R> requests) {
        List<T> entities = requests.stream().map(this::toEntity).toList();
        repository.saveAll(entities);
    }

    @Transactional
    public void saveOneAndDeleteOldest(R request) {
        Grade grade = Grade.valueOf(getGradeFromRequest(request).toUpperCase());
        long count = repository.countByGrade(grade);
        if (count >= 1) {
            repository.findByGrade(grade).stream()
                    .min(Comparator.comparing(AgriculturalPrice::getYear)
                            .thenComparing(AgriculturalPrice::getMonth)
                            .thenComparing(AgriculturalPrice::getDay))
                    .ifPresent(repository::delete);
        }
        repository.save(toEntity(request));
    }

    protected abstract String getGradeFromRequest(R request);

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<T> findByGrade(Grade grade) {
        return repository.findByGrade(grade);
    }

    public List<T> findRecentDaysWithForecast(Grade grade) {
        List<T> all = findByGrade(grade);
        all.sort(Comparator.comparing((T p) -> LocalDate.of(p.getYear(), p.getMonth(), p.getDay())).reversed());

        int limit = Math.min(49, all.size());
        List<T> latest = all.subList(0, limit);

        latest.sort(Comparator.comparing(p -> LocalDate.of(p.getYear(), p.getMonth(), p.getDay())));

        return latest.subList(0, Math.min(28, latest.size()));
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<T> all = findByGrade(grade);

        return all.stream()
                .filter(e -> e.getAveragePrice() > 0)
                .collect(Collectors.groupingBy(
                        e -> {
                            LocalDate d = LocalDate.of(e.getYear(), e.getMonth(), e.getDay());
                            int week = d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                            return String.format("%04d-%02d-%02d", d.getYear(), d.getMonth(), week);
                        },
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avg = (int) list.stream().mapToInt(AgriculturalPrice::getAveragePrice).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avg);
                            return m;
                        })
                )).entrySet().stream()
                .skip(Math.max(2, all.size() - 12))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> b, LinkedHashMap::new
                ));
    }

    public Map<String, Map<String, Integer>> getMonthlyAverages(Grade grade) {
        List<T> all = findByGrade(grade);

        return all.stream()
                .filter(e -> e.getAveragePrice() > 0)
                .collect(Collectors.groupingBy(
                        e -> String.format("%04d-%02d", e.getYear(), e.getMonth()),
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avg = (int) list.stream().mapToInt(AgriculturalPrice::getAveragePrice).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avg);
                            return m;
                        })
                )).entrySet().stream()
                .skip(Math.max(0, all.size() - 12))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> b, LinkedHashMap::new
                ));
    }
}
