package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Meal get(int id);

    Meal update(Meal meal);

    void delete(int id);

    Collection<Meal> getAll();
}
