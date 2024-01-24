package servlets;

import data.Coordinates;
import data.Result;
import data.ResultContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "AreaCheckServlet", value = "/check")
public class AreaCheckServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.nanoTime();
        ResultContainer resultList = (ResultContainer) req.getServletContext().getAttribute("results");

        Coordinates coordinates = null;
        Result.Type result = Result.Type.NONE;
        try {
            BigDecimal r = new BigDecimal(req.getParameter("r"));
            BigDecimal x = new BigDecimal(req.getParameter("x"));
            BigDecimal y = new BigDecimal(req.getParameter("y"));
            boolean dot = Boolean.parseBoolean(req.getParameter("dot"));

            if (validate(x, y, r, dot)) {
                boolean inArea = inArea(x.doubleValue(), y.doubleValue(), r.doubleValue());
                result = inArea ? Result.Type.HIT : Result.Type.FAIL;
            } else {
                result = Result.Type.FAILED_VALIDATING;
            }
        } catch (NullPointerException | NumberFormatException e) {
            result = Result.Type.WRONG_FORMAT;
        } finally {
            coordinates = new Coordinates(req.getParameter("x").replace('.', ','), req.getParameter("y").replace('.', ','), req.getParameter("r").replace('.', ','));
            resultList.add(new Result(
                    coordinates,
                    new Date(),
                    (double) (System.nanoTime() - startTime) / 1000000,
                    result
            ));
            req.getRequestDispatcher("/jsp/table.jsp").forward(req, resp);
        }
    }

    public static final Set<Integer> availableX = new HashSet<>(Arrays.asList(-2, -1,5, -1, -0,5, 0, 0,5, 1, 1,5, 2));
    public static final Set<Integer> availableR = new HashSet<>(Arrays.asList(1, 1,5, 2, 2,5, 3));

    public static boolean validate(BigDecimal x, BigDecimal y, BigDecimal r, boolean dot) {
        boolean isXValid = availableX.contains(x.intValue());
        boolean isYValid = y.compareTo(new BigDecimal("-3")) > 0 && y.compareTo(new BigDecimal("3")) < 0;
        return (dot && availableR.contains(r.intValue())
                || isXValid
                && isYValid
                && availableR.contains(r.intValue()));
    }

    public static boolean inArea(double x, double y, double r) {
        boolean triangle = (x >= 0 & y <= 0 & r >= 2*x - y) ;
        boolean square = (x <= 0 & x*x <= r*r & y <= 0 & y*y <= r*r);
        boolean circle = (x >= 0 & y >= 0 & x*x + y*y <= r*r);
        return circle || triangle || square;
    }
}
