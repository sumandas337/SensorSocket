package graph.sockets.com.sensorsocket.application;

import javax.inject.Singleton;

import dagger.Component;
import graph.sockets.com.sensorsocket.net.RestApiModule;

/**
 * Created by sumandas on 29/01/2017.
 */
@Singleton
@Component(modules = {AppModule.class, RestApiModule.class})
public interface AppComponent extends IAppComponent {
}
