package com.project.Justick.Service.SweetPotato;

import com.project.Justick.DTO.SweetPotato.SweetPotatoRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotato;
import com.project.Justick.Repository.SweetPotato.SweetPotatoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SweetPotatoService {
    private final SweetPotatoRepository repository;

    public SweetPotatoService(SweetPotatoRepository repository) {
        this.repository = repository;
    }

    // 최근 21일 데이터 조회
    public List<SweetPotato> findRecentDaysByGrade(Grade grade) {
        Pageable top21 = PageRequest.of(0, 21); // 상위 21개
        List<SweetPotato> recent = repository.findTopByGradeOrderByDateDesc(grade, top21);
        Collections.reverse(recent); // 날짜 오름차순 정렬로 바꿔줌 (old → new)
        return recent;
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<SweetPotato> all = repository.findByGrade(grade);

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
        List<SweetPotato> all = repository.findByGrade(grade);

        Map<String, Map<String, Integer>> fullResult = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new, // 자동으로 날짜순 정렬됨
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(SweetPotato::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(SweetPotato::getIntake).average().orElse(0);
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
    public void saveAll(List<SweetPotatoRequest> requests) {
        List<SweetPotato> list = requests.stream().map(this::toEntity).toList();
        repository.saveAll(list);
    }

    private SweetPotato toEntity(SweetPotatoRequest request) {
        SweetPotato entity = new SweetPotato();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setIntake(request.getIntake());
        Optional<SweetPotato> prev = repository.findByGradeAndYearAndMonthAndDay(
                entity.getGrade(),
                entity.getYear(),
                entity.getMonth(),
                entity.getDay() - 1
        );
        int prevPrice = prev.map(SweetPotato::getAveragePrice).orElse(0);
        entity.setGap(entity.getAveragePrice() - prevPrice);
        entity.setGrade(Grade.valueOf(request.getGrade()));
        return entity;
    }


    @Transactional
    public void saveOneAndDeleteOldest(SweetPotatoRequest request) {
        long count = repository.countByGrade(request.getGrade());
        if (count >= 28) {
            repository.deleteOldestByGrade(Grade.valueOf(request.getGrade()));
        }
        repository.save(toEntity(request));
    }
}
