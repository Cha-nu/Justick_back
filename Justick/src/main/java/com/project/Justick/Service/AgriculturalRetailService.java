package com.project.Justick.Service;

import com.project.Justick.Domain.AgriculturalRetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Comparator;
import java.util.List;

public class AgriculturalRetailService<T extends AgriculturalRetail, R> {
    private final JpaRepository<T, Long> repository;

    public AgriculturalRetailService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    protected T toEntity(R request) {
        return null;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public void saveAll(List<R> requests) {
        List<T> list = requests.stream().map(this::toEntity).toList();
        repository.saveAll(list);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void saveWithLimit(R request, int limit) {
        T newData = toEntity(request);
        List<T> all = repository.findAll();

        if (all.size() >= limit) {
            all.stream()
                    .min(Comparator.comparing(AgriculturalRetail::getYear)
                            .thenComparing(AgriculturalRetail::getMonth)
                            .thenComparing(AgriculturalRetail::getDay))
                    .ifPresent(repository::delete);
        }

        repository.save(newData);
    }
}
