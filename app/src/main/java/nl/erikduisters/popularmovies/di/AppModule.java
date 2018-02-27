package nl.erikduisters.popularmovies.di;

import android.app.Application;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import nl.erikduisters.popularmovies.MyApplication;
import nl.erikduisters.popularmovies.data.remote.TMDBService;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivitySubcomponent;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivitySubcomponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Erik Duisters on 04-12-2017.
 */

@Module(subcomponents = {MainActivitySubcomponent.class, MovieDetailActivitySubcomponent.class})
abstract class AppModule {
    @Binds
    @Singleton
    abstract Application application(MyApplication myApplication);

    @Provides
    @ApplicationContext
    static Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    static TMDBService provideTMDBService(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDBService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(TMDBService.class);
    }
}
