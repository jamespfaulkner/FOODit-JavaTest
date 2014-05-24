import com.foodit.test.sample.controller.DataLoadController;
import com.foodit.test.sample.controller.RestApiController;
import com.threewks.thundr.action.method.MethodAction;
import com.threewks.thundr.route.Route;
import com.threewks.thundr.route.Routes;

import static com.threewks.thundr.route.RouteType.GET;

public class ApplicationRoutes {

    public static class Names {

        public static final String ListTasks = "list";
        public static final String CreateTask = "create-task";
        public static final String ViewTask = "view-task";
        public static final String UpdateTask = "update-task";
        public static final String StartTask = "start-task";
        public static final String StopTask = "stop-task";
        public static final String FinishedTask = "finished-task";
        public static final String ArchiveTask = "archive-task";

        public static final String LoadData = "load-data";

        public static final String ViewInstructions = "view-instructions";
        public static final String ViewData = "view-data";

        public static final String VIEW_MENU = "view-menu";
        public static final String VIEW_ORDER = "view-order";
    }

    public void addRoutes(Routes routes) {

        // Loader
        routes.addRoute(new Route(GET, "/load/", Names.LoadData), new MethodAction(DataLoadController.class, "load"));

        // Instructions
        routes.addRoute(new Route(GET, "/", Names.ViewInstructions), new MethodAction(DataLoadController.class, "instructions"));
        routes.addRoute(new Route(GET, "/restaurant/{restaurant}/download", Names.ViewData), new MethodAction(DataLoadController.class, "viewData"));

        // extras to help
        routes.addRoute(new Route(GET, "/restaurant/{restaurant}/download/menu", Names.VIEW_MENU), new MethodAction(DataLoadController.class, "viewMenu"));
        routes.addRoute(new Route(GET, "/restaurant/{restaurant}/download/orders", Names.VIEW_ORDER), new MethodAction(DataLoadController.class, "viewOrders"));

        routes.addRoute(new Route(GET, "/restaurant/{restaurantName}/orders", null), new MethodAction(RestApiController.class, "orders"));
        routes.addRoute(new Route(GET, "/restaurant/{restaurantName}/sales", null), new MethodAction(RestApiController.class, "sales"));

        routes.addRoute(new Route(GET, "/overall/most_popular_meals", null), new MethodAction(RestApiController.class, "mostPopularMeals"));
    }
}
