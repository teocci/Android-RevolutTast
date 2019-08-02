package com.github.teocci.taskrevolut.Views;

import com.github.teocci.taskrevolut.interfaces.CurrencyUpdateListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;
import com.github.teocci.taskrevolut.models.CurrencyRateUpdate;
import com.github.teocci.taskrevolut.net.RevolutAPIServiceSingleton;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.github.teocci.taskrevolut.utils.Config.BASE_CURRENCY_TYPE;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyUpdater
{
    private CompositeDisposable disposable;
    private Disposable timerDisposable;

    private double baseCurrencyValue = 1.0;

    private String selectedCurrency = "EUR";
    private double selectedCurrencyValue = 1.0;
    private double selectedCurrencyRate = 1.0;

    private CurrencyUpdateListener listener;

    public CurrencyUpdater(CurrencyUpdateListener listener)
    {
        this.listener = listener;
    }

    private void getRates()
    {
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void onUpdate()
    {
        disposable = new CompositeDisposable();
        disposable.add(RevolutAPIServiceSingleton.getInstance()
                .latestRates(BASE_CURRENCY_TYPE)
                .subscribeOn(Schedulers.io())
                .flattenAsObservable(new Function<CurrencyRateUpdate, Iterable<Map.Entry<String, Double>>>()
                {
                    @Override
                    public Iterable<Map.Entry<String, Double>> apply(CurrencyRateUpdate currencyRateUpdate) throws Exception
                    {
                        return currencyRateUpdate.rates.entrySet();
                    }
                })
                .map(new Function<Map.Entry<String, Double>, CurrencyRate>()
                {
                    @Override
                    public CurrencyRate apply(Map.Entry<String, Double> entry) throws Exception
                    {
                        if (selectedCurrency.equals(entry.getKey())) {
                            selectedCurrencyRate = entry.getValue();
                            //entry.value=selectedCurrencyValue
                            return new CurrencyRate(entry.getKey(), selectedCurrencyRate);
                        }

                        baseCurrencyValue = BASE_CURRENCY_TYPE.equals(selectedCurrency) ?
                                selectedCurrencyValue :
                                1 / selectedCurrencyRate * selectedCurrencyValue;

                        return new CurrencyRate(entry.getKey(), entry.getValue() * baseCurrencyValue);
                    }

                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<CurrencyRate>>()
                        {
                            @Override
                            public void accept(List<CurrencyRate> rateList)
                            {
                                listener.onUpdate(rateList);
                            }
                        },
                        new Consumer<Throwable>()
                        {
                            @Override
                            public void accept(Throwable throwable)
                            {
                                throwable.printStackTrace();
                                listener.onError(throwable.getMessage());
                            }

                        }
                )
        );
    }

    private void disposeAll()
    {
        if (timerDisposable != null) timerDisposable.dispose();
        if (disposable != null) disposable.dispose();
    }

    private String getBaseCurrency()
    {
        return BASE_CURRENCY_TYPE;
    }

    private Double getBaseCurrencyValue()
    {
        return baseCurrencyValue;
    }

    private void setSelectedCurrency(String selectedCurrency)
    {
        this.selectedCurrency = selectedCurrency;
    }

    public void setSelectedCurrencyValue(Double selectedCurrencyValue)
    {
        this.selectedCurrencyValue = selectedCurrencyValue;
    }
}
