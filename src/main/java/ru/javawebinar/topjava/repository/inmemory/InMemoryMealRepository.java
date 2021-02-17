package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> usersMealsRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    { //block for tests only; safe to remove
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.meals2.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save meal={} userid={}", meal, userId);
        Map<Integer, Meal> repository = usersMealsRepository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete id={} userid={}", id, userId);
        Map<Integer, Meal> repository = usersMealsRepository.get(userId);
        return repository != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get id={} userid={}", id, userId);
        Map<Integer, Meal> repository = usersMealsRepository.get(userId);
        return repository == null ? null : repository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime start, LocalDateTime end, int userId) {
        return getAllFiltered(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), start, end));
    }


    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> repository = usersMealsRepository.get(userId);
        return repository == null ? Collections.emptyList() :
                repository.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }


//    public List<Meal> getBetweenHalfOpen(LocalDate start, LocalDate end, int userId) {
//        return getAllFiltered(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), start, end));
//    }
//
//    public List<Meal> getBetweenHalfOpen(LocalTime start, LocalTime end, int userId) {
//        return getAllFiltered(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), start, end));
//    }
}

