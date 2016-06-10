package digitalpromo.cabsdemo.api.new_api;

import java.io.IOException;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Администратор on 10.06.2016.
 */
public class ServiceGenerator {
    private static final String TAXI_SERVICE_BASE_URL = "http://95.67.73.202:6969/api/";

    private static final String API_KEY = "&key=" + App.getContext().getString(R.string.geocoding_key);
    private static final String GOOGLE_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?language=ru&%s" + API_KEY;

    private static OkHttpClient.Builder httpClientTaxi = new OkHttpClient.Builder();
    private static OkHttpClient.Builder httpClientGM = new OkHttpClient.Builder();

    private static Retrofit.Builder builderTaxiService =
            new Retrofit.Builder()
                    .baseUrl(TAXI_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderGoogleMaps =
            new Retrofit.Builder()
                    .baseUrl(GOOGLE_MAP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static <S> S createTaxiService(Class<S> serviceClass) {
        httpClientTaxi.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientTaxi.addInterceptor(logging);

        Retrofit retrofit = builderTaxiService.client(httpClientTaxi.build()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createGMService(Class<S> serviceClass) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientGM.addInterceptor(logging);

        Retrofit retrofit = builderGoogleMaps.client(httpClientGM.build()).build();
        return retrofit.create(serviceClass);
    }
}
