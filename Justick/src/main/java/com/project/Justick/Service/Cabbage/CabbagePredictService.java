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
        int count = 15;
        return grouped.entrySet().stream()
                .skip(Math.max(0, size - count))
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
        List<CabbagePredict> all = repo.findByGrade(grade);

        // 날짜 내림차순(최신순) 정렬
        all.sort(Comparator.comparing((CabbagePredict p) ->
                LocalDate.of(p.getYear(), p.getMonth(), p.getDay())
        ).reversed());

        // 47개까지만 슬라이스
        int limit = Math.min(48, all.size());
        List<CabbagePredict> latest47 = all.subList(0, limit);

        // 날짜 오름차순으로 다시 정렬
        latest47.sort(Comparator.comparing(p ->
                LocalDate.of(p.getYear(), p.getMonth(), p.getDay())
        ));

        return latest47.subList(0, 28);
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
