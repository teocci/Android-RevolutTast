package com.github.teocci.taskrevolut.interfaces;

import com.github.teocci.taskrevolut.models.CurrencyRate;

import java.util.List;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public interface CurrencyUpdateListener
{
    void onUpdate(List<CurrencyRate> rateList);

    void onError(String message);
}
