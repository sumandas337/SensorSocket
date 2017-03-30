package graph.sockets.com.sensorsocket.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by sumandas on 24/03/2017.
 */

public class SocketApplication extends MultiDexApplication{

    protected static Context mApp;

    protected IAppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=getApplicationContext();
        initAppComponent();
        initDbFlow();

        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        Stetho.Initializer initializer = initializerBuilder.build();

        Stetho.initialize(initializer);

    }

    public void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .build();
    }

    public IAppComponent getmApplicationComponent() {
        return mAppComponent;
    }

    public void initDbFlow() {
        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());
    }


    public static SocketApplication getAppContext() {
        return (SocketApplication) mApp;
    }

}
