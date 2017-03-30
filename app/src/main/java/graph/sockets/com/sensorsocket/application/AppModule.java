package graph.sockets.com.sensorsocket.application;


import java.net.URISyntaxException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import graph.sockets.com.sensorsocket.database.SensorDbManager;
import graph.sockets.com.sensorsocket.net.RestApiModule;
import graph.sockets.com.sensorsocket.utils.Navigator;
import graph.sockets.com.sensorsocket.utils.RxBus;
import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * Created by sumandas on 24/03/2017.
 */
@Module
public class AppModule {

    @Singleton
    @Provides
    RxBus providesRxBus(){
        return RxBus.getInstance();
    }

    @Singleton
    @Provides
    Navigator providesNavigator() {
        return new Navigator();
    }

    @Singleton
    @Provides
    SensorDbManager providesSensorManager(){
        return SensorDbManager.initializeManager();
    }

    @Singleton
    @Provides
    Socket providesSocket(){
        try {
           return IO.socket(RestApiModule.API_ROOT);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
