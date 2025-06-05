package com.project.Justick.Service.SweetPotato;

import com.project.Justick.DTO.SweetPotato.SweetPotatoPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotatoPredict;
import com.project.Justick.Repository.SweetPotato.SweetPotatoPredictRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SweetPotatoPredictService {

    private final SweetPotatoPredictRepository repo;

    public SweetPotatoPredictService(SweetPotatoPredictRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void saveAll(List<SweetPotatoPredictRequest> requests) {
        List<SweetPotatoPredict> entities = requests.stream()
                .map(this::toEntity)
                .toList();
        repo.saveAll(entities);
    }

    private SweetPotatoPredict toEntity(SweetPotatoPredictRequest req) {
        SweetPotatoPredict e = new SweetPotatoPredict();
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
        List<SweetPotatoPredict> all = repo.findByGrade(grade);

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
                            int avgPrice = (int) list.stream().mapToInt(SweetPotatoPredict::getAveragePrice).average().orElse(0);
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
        List<SweetPotatoPredict> all = repo.findByGrade(grade);

        Map<String, Map<String, Integer>> grouped = all.stream()
                .filter(c -> c.getAveragePrice() > 0 )
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(SweetPotatoPredict::getAveragePrice).average().orElse(0);
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

    public List<SweetPotatoPredict> findRecentDaysWithForecast(Grade grade) {
        SweetPotatoPredict latest = repo.findLatestByGrade(grade);
        if (latest == null) return List.of();

        LocalDate latestDate = LocalDate.of(latest.getYear(), latest.getMonth(), latest.getDay());

        // 42일 전부터 21일 전까지의 데이터
        LocalDate from = latestDate.minusDays(48);
        LocalDate to = latestDate.minusDays(21);

        List<SweetPotatoPredict> result = repo.findByDateRangeAndGrade(
                from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                to.getYear(), to.getMonthValue(), to.getDayOfMonth(),
                grade
        );

        //21개 제한
        return result.size() > 28 ? result.subList(0, 28) : result;
    }

    @Transactional
    public void saveOneAndDeleteOldest(SweetPotatoPredictRequest request) {
        Grade grade = Grade.valueOf(request.getGrade().toUpperCase());
        long count = repo.countByGrade(grade);
        if (count >= 1) {
            // 가장 오래된 데이터 삭제
            repo.findByGrade(grade).stream()
                    .min(Comparator.comparing(SweetPotatoPredict::getYear)
                            .thenComparing(SweetPotatoPredict::getMonth)
                            .thenComparing(SweetPotatoPredict::getDay)).ifPresent(repo::delete);
        }
        repo.save(toEntity(request));
    }
}
