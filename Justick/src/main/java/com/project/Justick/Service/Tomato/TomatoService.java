package com.project.Justick.Service.Tomato;

import com.project.Justick.DTO.Tomato.TomatoRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.Tomato;
import com.project.Justick.Repository.Tomato.TomatoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TomatoService {
    private final TomatoRepository repository;

    public TomatoService(TomatoRepository repository) {
        this.repository = repository;
    }

    // 최근 21일 데이터 조회
    public List<Tomato> findRecentDaysByGrade(Grade grade) {
        Pageable top21 = PageRequest.of(0, 21); // 상위 21개
        List<Tomato> recent = repository.findTopByGradeOrderByDateDesc(grade, top21);
        Collections.reverse(recent); // 날짜 오름차순 정렬로 바꿔줌 (old → new)
        return recent;
    }

    public Map<String, Map<String, Integer>> getWeeklyAverages(Grade grade) {
        List<Tomato> all = repository.findByGrade(grade);

        Map<String, Map<String, Integer>> result = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .map(c -> {
                    LocalDate d = LocalDate.of(c.getYear(), c.getMonth(), c.getDay());
                    int weekOfMonth = d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                    String key = String.format("%04d-%02d-%02d", d.getYear(), d.getMonthValue(), weekOfMonth);
                    return new AbstractMap.SimpleEntry<>(key, new AbstractMap.SimpleEntry<>(weekOfMonth, c));
                })
                .filter(e -> e.getValue().getKey() <= 10) // 10주차까지만
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
        List<Tomato> all = repository.findByGrade(grade);

        Map<String, Map<String, Integer>> fullResult = all.stream()
                .filter(c -> c.getAveragePrice() > 0 && c.getIntake() > 0)
                .collect(Collectors.groupingBy(
                        c -> String.format("%04d-%02d", c.getYear(), c.getMonth()),
                        TreeMap::new, // 자동으로 날짜순 정렬됨
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            int avgPrice = (int) list.stream().mapToInt(Tomato::getAveragePrice).average().orElse(0);
                            int avgIntake = (int) list.stream().mapToInt(Tomato::getIntake).average().orElse(0);
                            Map<String, Integer> m = new HashMap<>();
                            m.put("averagePrice", avgPrice);
                            m.put("intake", avgIntake);
                            return m;
                        })
                ));

        return fullResult.entrySet()
                .stream()
                .skip(Math.max(0, fullResult.size() - 11)) // 최신 9개월만 추출
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new // 순서 유지해서 반환
                ));
    }


    @Transactional
    public void saveAll(List<TomatoRequest> requests) {
        List<Tomato> list = requests.stream().map(this::toEntity).toList();
        repository.saveAll(list);
    }

    private Tomato toEntity(TomatoRequest request) {
        Tomato entity = new Tomato();
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
    public void saveOneAndDeleteOldest(TomatoRequest request) {
        Grade grade = Grade.valueOf(request.getGrade());
        long count = repository.countByGrade(grade);
        if (count >= 1) {
            // 가장 오래된 데이터 id 조회
            Optional<Tomato> oldest = repository.findByGradeOrderByYearAscMonthAscDayAsc(grade).stream().findFirst();
            oldest.ifPresent(c -> repository.deleteById(c.getId()));
        }
        repository.save(toEntity(request));
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
