package com.github.teocci.taskrevolut.net;

import com.github.teocci.taskrevolut.interfaces.RevolutAPIService;

import com.github.teocci.taskrevolut.utils.LogHelper;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.teocci.taskrevolut.utils.Config.BASE_URL;

/**
 * This singleton create a Retrofit instance and pass the interface {@linkplain RevolutAPIService}
 * where the API's requests will be handled.
 *
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class RevolutAPIServiceSingleton
{
    private final static String TAG = LogHelper.makeLogTag(RevolutAPIServiceSingleton.class);

    private static volatile RevolutAPIService instance;
    private static final Object mutex = new Object();

    private RevolutAPIServiceSingleton() {}

    public static RevolutAPIService getInstance()
    {
        RevolutAPIService result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = create();
            }
        }

        return result;
    }

    /**
     * @return a Retrofit instance with the API URL instantiated.
     */
    private static RevolutAPIService create()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(RevolutAPIService.class);
    }
}
