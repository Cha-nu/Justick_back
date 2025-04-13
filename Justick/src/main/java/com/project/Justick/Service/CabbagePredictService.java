package com.project.Justick.Service;

import com.project.Justick.DTO.CabbageRequest;
import com.project.Justick.Domain.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.CabbagePredictRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CabbagePredictService {

    private final CabbagePredictRepository repo;

    public CabbagePredictService(CabbagePredictRepository repo) {
        this.repo = repo;
    }

    public List<CabbagePredict> findByGrade(Grade grade) {
        return repo.findByGrade(grade);
    }

    public CabbagePredict findLatestByGrade(Grade grade) {
        return repo.findLatestByGrade(grade);
    }

    public List<CabbagePredict> findByDateRange(Grade grade, LocalDate from, LocalDate to) {
        return repo.findByDateRangeAndGrade(
                from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                to.getYear(), to.getMonthValue(), to.getDayOfMonth(),
                grade
        );
    }

    @Transactional
    public void saveAll(List<CabbageRequest> requests) {
        List<CabbagePredict> entities = requests.stream()
                .map(this::toEntity)
                .toList();
        repo.saveAll(entities);
    }

    private CabbagePredict toEntity(CabbageRequest req) {
        CabbagePredict e = new CabbagePredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setIntake(req.getIntake());
        e.setGap(req.getGap());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<CabbagePredict> all = repo.findByGrade(grade);

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
                            int avgPrice = (int) list.stream().mapToInt(CabbagePredict::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(CabbagePredict::getIntake).average().orElse(0);
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
        List<CabbagePredict> all = repo.findByGrade(grade);

        Map<String, Map<String, Integer>> grouped = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(CabbagePredict::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(CabbagePredict::getIntake).average().orElse(0);
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

    public List<CabbagePredict> findRecent20DaysWithForecast(Grade grade) {
        CabbagePredict latest = repo.findLatestByGrade(grade);
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
