package com.project.Justick.Service;

import com.project.Justick.Domain.AgriculturalPrice;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.AgriculturalRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AgriculturalService<T extends AgriculturalPrice, R> {

    protected final AgriculturalRepository<T> repository;

    public AgriculturalService(AgriculturalRepository<T> repository) {
        this.repository = repository;
    }

    protected abstract T toEntity(R request);

    public List<T> findRecentDaysByGrade(Grade grade) {
        Pageable top21 = PageRequest.of(0, 21);
        List<T> recent = repository.findTopByGradeOrderByDateDesc(grade, top21);
        Collections.reverse(recent);
        return recent;
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<T> all = repository.findByGrade(grade);
        return all.stream()
                .filter(e -> {
                    try {
                        var intakeField = e.getClass().getDeclaredField("intake");
                        intakeField.setAccessible(true);
                        return e.getAveragePrice() > 0 && ((int) intakeField.get(e)) > 0;
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .map(e -> {
                    LocalDate d = LocalDate.of(e.getYear(), e.getMonth(), e.getDay());
                    int week = d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                    String key = String.format("%04d-%02d-%02d", d.getYear(), d.getMonthValue(), week);
                    return new AbstractMap.SimpleEntry<>(key, new AbstractMap.SimpleEntry<>(week, e));
                })
                .filter(e -> e.getValue().getKey() <= 10)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream()
                                    .mapToInt(e -> e.getValue().getValue().getAveragePrice())
                                    .average().orElse(0);
                            int avgIntake = (int) list.stream()
                                    .mapToInt(e -> {
                                        try {
                                            var f = e.getValue().getValue().getClass().getDeclaredField("intake");
                                            f.setAccessible(true);
                                            return (int) f.get(e.getValue().getValue());
                                        } catch (Exception ex) {
                                            return 0;
                                        }
                                    })
                                    .average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));
    }

    public Map<String, Map<String, Integer>> getMonthlyAverages(Grade grade) {
        List<T> all = repository.findByGrade(grade);
        return all.stream()
                .filter(e -> {
                    try {
                        var f = e.getClass().getDeclaredField("intake");
                        f.setAccessible(true);
                        return e.getAveragePrice() > 0 && ((int) f.get(e)) > 0;
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .collect(Collectors.groupingBy(
                        e -> String.format("%04d-%02d", e.getYear(), e.getMonth()),
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(AgriculturalPrice::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(e -> {
                                try {
                                    var f = e.getClass().getDeclaredField("intake");
                                    f.setAccessible(true);
                                    return (int) f.get(e);
                                } catch (Exception ex) {
                                    return 0;
                                }
                            }).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));
    }

    @Transactional
    public void saveAll(List<R> requests) {
        List<T> list = requests.stream().map(this::toEntity).toList();
        repository.saveAll(list);
    }

    @Transactional
    public void saveOneAndDeleteOldest(R request) {
        T entity = toEntity(request);
        Grade grade = entity.getGrade();
        long count = repository.countByGrade(grade);
        if (count >= 1) {
            Optional<T> oldest = repository.findByGradeOrderByYearAscMonthAscDayAsc(grade).stream().findFirst();
            oldest.ifPresent(o -> repository.deleteById(o.getId()));
        }
        repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
