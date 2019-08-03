package com.github.teocci.taskrevolut.net;

import com.github.teocci.taskrevolut.interfaces.CurrencyUpdateListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;
import com.github.teocci.taskrevolut.models.CurrencyRateUpdate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.github.teocci.taskrevolut.utils.LogHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.github.teocci.taskrevolut.utils.Config.DEFAULT_CURRENCY_TYPE;
import static com.github.teocci.taskrevolut.utils.Config.UPDATE_INTERVAL;

/**
 * This class implements an Observable that emits sequential numbers every #UPDATE_INTERVAL of time in seconds,
 * on a specified SchedulerLike.
 * <p>
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyUpdater
{
    private final static String TAG = LogHelper.makeLogTag(CurrencyUpdater.class);

    private final double DEFAULT_CURRENCY_VALUE = 100.0;
    private final double DEFAULT_CURRENCY_RATE = 1.0;

    private CompositeDisposable disposable;
    private Disposable timerDisposable;

    private String selectedCurrencyId = DEFAULT_CURRENCY_TYPE;

    private double selectedCurrencyValue = DEFAULT_CURRENCY_VALUE;
    private double selectedCurrencyRate = DEFAULT_CURRENCY_RATE;

    private CurrencyUpdateListener listener;

    public CurrencyUpdater(CurrencyUpdateListener listener)
    {
        this.listener = listener;
    }

    /**
     * Instantiate an Observable interval.
     */
    public void getRates()
    {
        timerDisposable = Observable.interval(UPDATE_INTERVAL, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> this.onUpdate());
    }

    /**
     * A consumer designed to accept emissions from the ObservableSource
     */
    public void onUpdate()
    {
        onUpdate(false);
    }

    /**
     * @param ignoreFocus if the emission will update the view even if is focused.
     */
    public void onUpdate(boolean ignoreFocus)
    {
        disposable = new CompositeDisposable();
        disposable.add(RevolutAPIServiceSingleton
                .getInstance()
                .latestRates(selectedCurrencyId)
                .subscribeOn(Schedulers.io())
                .flattenAsObservable((Function<CurrencyRateUpdate, Iterable<Map.Entry<String, Double>>>) currencyRateUpdate -> currencyRateUpdate.rates.entrySet())
                .map(entry -> {
                    String currencyId = entry.getKey();
                    double currencyRate = entry.getValue();
                    if (selectedCurrencyId.equals(currencyId)) {
                        selectedCurrencyRate = currencyRate;

                        return new CurrencyRate(currencyId, selectedCurrencyRate, selectedCurrencyValue);
                    }

                    return new CurrencyRate(currencyId, currencyRate, currencyRate * selectedCurrencyValue);
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        rateList -> listener.onUpdate(rateList, ignoreFocus),
                        throwable -> {
                            throwable.printStackTrace();
                            listener.onError(throwable.getMessage());
                        }
                )
        );
    }

    public void disposeAll()
    {
        if (timerDisposable != null) timerDisposable.dispose();
        if (disposable != null) disposable.dispose();
    }

    public void setSelectedCurrencyId(String currencyId)
    {
        selectedCurrencyId = currencyId;
    }

    public void setSelectedCurrencyValue(Double currencyValue)
    {
        this.selectedCurrencyValue = currencyValue;
    }

    public String getSelectedCurrencyId()
    {
        return selectedCurrencyId;
    }

    public double getSelectedCurrencyValue()
    {
        return selectedCurrencyValue;
    }
}
