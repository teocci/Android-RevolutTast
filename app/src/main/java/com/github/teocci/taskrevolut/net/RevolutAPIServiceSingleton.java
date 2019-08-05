package com.github.teocci.taskrevolut.net;

import com.github.teocci.taskrevolut.interfaces.RevolutAPIService;
import com.github.teocci.taskrevolut.utils.LogHelper;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.teocci.taskrevolut.utils.Config.BASE_URL;

/**
 * This singleton create a Retrofit instance that will generate the implementation of the interface {@linkplain RevolutAPIService}
 * which will handled the API's requests.
 * <p>
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
     * @return a Retrofit instance that generates the implementation of the interface {@linkplain RevolutAPIService}
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
