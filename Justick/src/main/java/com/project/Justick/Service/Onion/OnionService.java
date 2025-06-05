package com.project.Justick.Service.Onion;

import com.project.Justick.DTO.Onion.OnionRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.Onion;
import com.project.Justick.Repository.Onion.OnionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // 최근 21일 데이터 조회
    public List<Onion> findRecentDaysByGrade(Grade grade) {
        Pageable top21 = PageRequest.of(0, 21); // 상위 21개
        List<Onion> recent = repository.findTopByGradeOrderByDateDesc(grade, top21);
        Collections.reverse(recent); // 날짜 오름차순 정렬로 바꿔줌 (old → new)
        return recent;
    }

    // 주별 평균 가격 및 출하량 계산
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

        // 최신 10주차만 반환
        return result.entrySet().stream()
                .skip(Math.max(0, result.size() - 10))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }

    // 월별 평균 가격 및 출하량 계산
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
                .skip(Math.max(0, fullResult.size() - 11)) // 최신 11개월만 추출
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new // 순서 유지해서 반환
                ));
    }

    // DTO를 Entity로 변환 후 저장
    @Transactional
    public void saveAll(List<OnionRequest> requests) {
        List<Onion> list = requests.stream().map(this::toEntity).toList();
        repository.saveAll(list);
    }

    // DTO를 Entity로 변환
    private Onion toEntity(OnionRequest request) {
        Onion entity = new Onion();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setIntake(request.getIntake());
        entity.setGrade(Grade.valueOf(request.getGrade()));
        entity.setGap(request.getGap());
        return entity;
    }


    // 최신 데이터 저장 및 오래된 데이터 삭제
    @Transactional
    public void saveOneAndDeleteOldest(OnionRequest request) {
        Grade grade = Grade.valueOf(request.getGrade());
        long count = repository.countByGrade(grade);
        if (count >= 1) {
            // 가장 오래된 데이터 id 조회
            Optional<Onion> oldest = repository.findByGradeOrderByYearAscMonthAscDayAsc(grade).stream().findFirst();
            oldest.ifPresent(c -> repository.deleteById(c.getId()));
        }
        repository.save(toEntity(request));
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
