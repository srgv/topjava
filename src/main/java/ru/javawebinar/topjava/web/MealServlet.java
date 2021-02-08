package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository mealRepository = new InMemoryMealRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String action = request.getParameter("action");
        log.debug("redirect to meals action={}", action);

        if (action == null) {
            request.setAttribute("meals", MealsUtil.filteredByStreams(new ArrayList(mealRepository.getAll()),
                    LocalTime.MIN,
                    LocalTime.MAX,
                    MealsUtil.CALORIES_DAILY_LIMIT));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = getId(request);
            mealRepository.delete(id);
            response.sendRedirect("meals");
        } else if ("create".equals(action)) {
            Meal meal = new Meal(LocalDateTime.now(), "", 0);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealUpdate.jsp").forward(request, response);
        } else if ("update".equals(action)) {
            Meal meal = mealRepository.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealUpdate.jsp").forward(request, response);
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
        mealRepository.update(meal);
        resp.sendRedirect("meals");
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
