package com.github.teocci.taskrevolut.interfaces;

import com.github.teocci.taskrevolut.models.CurrencyRateUpdate;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.github.teocci.taskrevolut.utils.Config.PARAMETER;
import static com.github.teocci.taskrevolut.utils.Config.REQUEST;

/**
 * Defines the interface that will be used by Retrofit to generate an implementation.
 * <p>
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-05
 */
public interface RevolutAPIService
{
    @GET(REQUEST)
    Single<CurrencyRateUpdate> latestRates(@Query(PARAMETER) String action);
}
