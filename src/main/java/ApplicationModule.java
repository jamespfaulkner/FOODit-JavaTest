import com.foodit.test.sample.bi.service.BusinessIntelligenceService;
import com.foodit.test.sample.bi.service.BusinessIntelligenceServiceImpl;
import com.foodit.test.sample.dao.RestaurantDAO;
import com.foodit.test.sample.dao.RestaurantDAOImpl;
import com.foodit.test.sample.model.RestaurantData;
import com.foodit.test.sample.service.GsonRestaurantDataServiceImpl;
import com.foodit.test.sample.service.RestaurantDataService;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.gae.GaeModule;
import com.threewks.thundr.gae.objectify.ObjectifyModule;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;
import com.threewks.thundr.route.Routes;

public class ApplicationModule extends BaseModule {

    private ApplicationRoutes applicationRoutes = new ApplicationRoutes();

    @Override
    public void requires(DependencyRegistry dependencyRegistry) {
        super.requires(dependencyRegistry);
        dependencyRegistry.addDependency(GaeModule.class);
        dependencyRegistry.addDependency(ObjectifyModule.class);
    }

    @Override
    public void configure(UpdatableInjectionContext injectionContext) {
        super.configure(injectionContext);
        configureObjectify();
    }

    @Override
    public void start(UpdatableInjectionContext injectionContext) {
        super.start(injectionContext);
        Routes routes = injectionContext.get(Routes.class);
        applicationRoutes.addRoutes(routes);
    }

    private void configureObjectify() {
        ObjectifyService.register(RestaurantData.class);
    }

    protected void addServices(UpdatableInjectionContext injectionContext) {
        injectionContext.inject(RestaurantDAOImpl.class).as(RestaurantDAO.class);
        injectionContext.inject(GsonRestaurantDataServiceImpl.class).as(RestaurantDataService.class);
        injectionContext.inject(BusinessIntelligenceServiceImpl.class).as(BusinessIntelligenceService.class);
    }
}
