package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository mealRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealRepository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String action = request.getParameter("action");
        if (action == null) {
            log.debug("forward to meals: main form unfiltered: ");//todo
            request.setAttribute("meals", MealsUtil.filteredByStreams(mealRepository.getAll(),
                    LocalTime.MIN,
                    LocalTime.MAX,
                    MealsUtil.CALORIES_DAILY_LIMIT));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            switch (action) {
                case "delete":
                    int id = getId(request);
                    log.debug("redirect to meals: action={}, id={}", action, id);
                    mealRepository.delete(id);
                    response.sendRedirect("meals");
                    break;
                case "create": {
                    Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
                    log.debug("forward to mealUpdate: action={}, meal={}", action, meal);
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/mealUpdate.jsp").forward(request, response);
                    break;
                }
                case "update": {
                    Meal meal = mealRepository.get(getId(request));
                    log.debug("forward to mealUpdate: action={}, meal={}", action, meal);
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/mealUpdate.jsp").forward(request, response);
                    break;
                }
                default:
                    log.debug("redirect to meals: action={}, invalid action, no changes", action);
                    response.sendRedirect("meals");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");

        String id = req.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
        mealRepository.save(meal);
        resp.sendRedirect("meals");
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
