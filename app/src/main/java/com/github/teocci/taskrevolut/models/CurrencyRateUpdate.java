package com.github.teocci.taskrevolut.models;

import java.util.Map;

/**
 * Receives JSON responses and converted as class with the GSON library.
 *
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRateUpdate
{
    public String base;
    public String date;
    public Map<String, Double> rates;
}
