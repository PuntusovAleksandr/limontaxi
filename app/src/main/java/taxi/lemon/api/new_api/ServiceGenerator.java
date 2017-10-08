package taxi.lemon.api.new_api;

import android.util.Base64;

import java.io.IOException;

import taxi.lemon.utils.EncodingSHA512;
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
    private static final String TAXI_SERVICE_BASE_URL = "http://185.46.150.170:6969/api/";

    private static OkHttpClient.Builder httpClientTaxi = new OkHttpClient.Builder();

    private static Retrofit.Builder builderTaxiService =
            new Retrofit.Builder()
                    .baseUrl(TAXI_SERVICE_BASE_URL)
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

    public static <S> S createTaxiService(Class<S> serviceClass, String userName, String userPassword) {
        if (userName != null && userPassword != null) {
            String credentials = userName + ":" + EncodingSHA512.generateSHA512(userPassword);
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClientTaxi.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientTaxi.addInterceptor(logging);
        }

        OkHttpClient client = httpClientTaxi.build();
        Retrofit retrofit = builderTaxiService.client(client).build();
        return retrofit.create(serviceClass);
    }
}
