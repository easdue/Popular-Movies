package nl.erikduisters.popularmovies.data.remote;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.erikduisters.popularmovies.data.model.Configuration;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public interface TMDBService {
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("configuration")
    Call<Configuration> getConfiguration(@Query("api_key") String api_key);

    class Provider {
        private static TMDBService tmdbService;

        public static TMDBService getTMDBService() {
            if (tmdbService == null) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                tmdbService = retrofit.create(TMDBService.class);
            }

            return tmdbService;
        }
    }
}
