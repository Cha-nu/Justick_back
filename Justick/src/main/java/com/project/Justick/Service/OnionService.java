package com.project.Justick.Service;

import com.project.Justick.DTO.OnionRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion;
import com.project.Justick.Repository.OnionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OnionService {
    private final OnionRepository repository;

    public OnionService(OnionRepository repository) {
        this.repository = repository;
    }

    public List<Onion> findRecent15DaysByGrade(Grade grade) {
        Onion latest = repository.findLatestByGrade(grade);
        if (latest == null) return List.of();

        LocalDate today = LocalDate.of(latest.getYear(), latest.getMonth(), latest.getDay());
        LocalDate from = today.minusDays(14); // 최근 포함해서 20일

        return repository.findByDateRangeAndGrade(
                from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                today.getYear(), today.getMonthValue(), today.getDayOfMonth(),
                grade
        );
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<Onion> all = repository.findByGrade(grade);

        Map<String, Map<String, Integer>> result = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .map(c -> {
                    LocalDate d = LocalDate.of(c.getYear(), c.getMonth(), c.getDay());
                    int weekOfMonth = d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                    String key = String.format("%04d-%02d-%02d", d.getYear(), d.getMonthValue(), weekOfMonth);
                    return new AbstractMap.SimpleEntry<>(key, new AbstractMap.SimpleEntry<>(weekOfMonth, c));
                })
                .filter(e -> e.getValue().getKey() <= 8) // 8주차까지만
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream()
                                    .mapToInt(e -> e.getValue().getValue().getAveragePrice())
                                    .average().orElse(0);
                            int avgIntake = (int) list.stream()
                                    .mapToInt(e -> e.getValue().getValue().getIntake())
                                    .average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));

        return result;
    }





    public Map<String, Map<String, Integer>> getMonthlyAverages(Grade grade) {
        List<Onion> all = repository.findByGrade(grade);

        Map<String, Map<String, Integer>> fullResult = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new, // 자동으로 날짜순 정렬됨
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(Onion::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(Onion::getIntake).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));

        return fullResult.entrySet()
                .stream()
                .skip(Math.max(0, fullResult.size() - 9)) // 최신 9개월만 추출
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new // 순서 유지해서 반환
                ));
    }


    @Transactional
    public void saveAll(List<OnionRequest> requests) {
        List<Onion> list = requests.stream().map(this::toEntity).toList();
        repository.saveAll(list);
    }

    private Onion toEntity(OnionRequest request) {
        Onion entity = new Onion();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setIntake(request.getIntake());
        entity.setGap(request.getGap());
        entity.setGrade(Grade.valueOf(request.getGrade()));
        return entity;
    }


    @Transactional
    public void saveOneAndDeleteOldest(OnionRequest request) {
        long count = repository.countByGrade(request.getGrade());
        if (count >= 28) {
            repository.deleteOldestByGrade(Grade.valueOf(request.getGrade()));
        }
        repository.save(toEntity(request));
    }
}
