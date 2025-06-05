package com.project.Justick.Service.Cabbage;

import com.project.Justick.DTO.Cabbage.CabbagePredictRequest;
import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.Cabbage.CabbagePredictRepository;
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

    @Transactional
    public void saveAll(List<CabbagePredictRequest> requests) {
        List<CabbagePredict> entities = requests.stream()
                .map(this::toEntity)
                .toList();
        repo.saveAll(entities);
    }

    private CabbagePredict toEntity(CabbagePredictRequest req) {
        CabbagePredict e = new CabbagePredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
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
                .filter(c -> c.getAveragePrice() > 0)
                .collect(Collectors.groupingBy(
                        c -> {
                            LocalDate d = LocalDate.of(c.getYear(), c.getMonth(), c.getDay());
                            int weekOfMonth = d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                            return String.format("%04d-%02d-%02d", d.getYear(), d.getMonthValue(), weekOfMonth);
                        },
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(CabbagePredict::getAveragePrice).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            return m;
                        })
                ));

        int size = grouped.size();
        int count = 14;
        int exclude = 2;
        return grouped.entrySet().stream()
                .skip(Math.max(0, size - (count + exclude)))
                .limit(count)
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
                .filter(c -> c.getAveragePrice() > 0)
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(CabbagePredict::getAveragePrice).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
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

    public List<CabbagePredict> findRecentDaysWithForecast(Grade grade) {
        CabbagePredict latest = repo.findLatestByGrade(grade);
        if (latest == null) return List.of();

        LocalDate latestDate = LocalDate.of(latest.getYear(), latest.getMonth(), latest.getDay());

        // 42일 전부터 21일 전까지의 데이터
        LocalDate from = latestDate.minusDays(48);
        LocalDate to = latestDate.minusDays(21);

        List<CabbagePredict> result = repo.findByDateRangeAndGrade(
                from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                to.getYear(), to.getMonthValue(), to.getDayOfMonth(),
                grade
        );

        //21개 제한
        return result.size() > 21 ? result.subList(0, 21) : result;
    }

    @Transactional
    public void saveOneAndDeleteOldest(CabbagePredictRequest request) {
        Grade grade = Grade.valueOf(request.getGrade().toUpperCase());
        long count = repo.countByGrade(grade);
        if (count >= 1) {
            // 가장 오래된 데이터 삭제
            repo.findByGrade(grade).stream()
                    .min(Comparator.comparing(CabbagePredict::getYear)
                            .thenComparing(CabbagePredict::getMonth)
                            .thenComparing(CabbagePredict::getDay)).ifPresent(repo::delete);
        }
        repo.save(toEntity(request));
    }
}
