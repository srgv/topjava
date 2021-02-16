package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.debug("get id={}, userid={}", id, userId);
        return service.get(id, userId);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.debug("delete id={}, userid={}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        log.debug("update {}, userid={}", meal, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        log.debug("create {}, userid={}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.debug("getall userid={}", userId);
        return MealsUtil.getTos(service.getAll(userId), authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getAll(); //todo ПОКА СТОИТ ЗАГЛУШКА
    }
}