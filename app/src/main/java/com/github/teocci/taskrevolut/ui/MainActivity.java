package com.github.teocci.taskrevolut.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.github.teocci.taskrevolut.Adapters.CurrencyRatesAdapter;
import com.github.teocci.taskrevolut.R;
import com.github.teocci.taskrevolut.Views.EditTextWatcher;
import com.github.teocci.taskrevolut.interfaces.CurrencyUpdateListener;
import com.github.teocci.taskrevolut.interfaces.OnItemEventListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;
import com.github.teocci.taskrevolut.net.CurrencyUpdater;
import com.github.teocci.taskrevolut.utils.FlagHelper;
import com.github.teocci.taskrevolut.utils.LogHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements CurrencyUpdateListener, OnItemEventListener
{
    private final static String TAG = LogHelper.makeLogTag(MainActivity.class);

    private Map<String, Drawable> currencyFlags;
    private Map<String, String> currencyNames;

    private volatile boolean isCurrencyValueEditing = false;

    private RecyclerView recyclerView;
    private ProgressBar loaderBar;

    private CurrencyUpdater currencyUpdater;

    private EditTextWatcher textWatcher;
    private CurrencyRatesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyUpdater = new CurrencyUpdater(this);
        textWatcher = new EditTextWatcher(currencyUpdater);

        recyclerView = (RecyclerView) findViewById(R.id.rates_data);
        loaderBar = (ProgressBar) findViewById(R.id.loader_bar);

        initAdapter();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        currencyUpdater.disposeAll();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (currencyUpdater != null) {
            currencyUpdater.getRates();
        }
    }

    @Override
    public void onUpdate(List<CurrencyRate> rateList, boolean ignoreFocus)
    {
        if (rateList == null) return;
        if (currencyUpdater == null) return;
        if (currencyUpdater.getSelectedCurrencyId() == null) return;
        if (adapter == null) return;

        recyclerView.setVisibility(VISIBLE);
        if (!isCurrencyValueEditing || ignoreFocus) {
            String baseCurrency = currencyUpdater.getSelectedCurrencyId();
            CurrencyRate rateUpdate = new CurrencyRate(baseCurrency, currencyUpdater.getSelectedCurrencyValue());
            rateList.add(0, rateUpdate);

            adapter.updateData(rateList);
        }
    }

    @Override
    public void onError(String message)
    {
        LogHelper.e(TAG, message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setNegativeButton("Close", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();

        // Display the alert dialog on app interface and check if the activity has not finished
        if (!isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void onItemClick(String currencyId)
    {
        if (currencyId == null) return;
        if (currencyUpdater == null) return;
        if (adapter == null) return;

        adapter.moveToTop(currencyId);

        double value = adapter.getValueById(currencyId);

        currencyUpdater.setSelectedCurrencyId(currencyId);
        currencyUpdater.setSelectedCurrencyValue(value);
        currencyUpdater.onUpdate();
    }

    @Override
    public void onFocusChange(boolean hasFocus, View view)
    {
        if (view == null) return;
        if (currencyUpdater == null) return;

        this.isCurrencyValueEditing = hasFocus;
        if (hasFocus) {
            String currencyId = view.getTag().toString();
            adapter.moveToTop(currencyId);

            double value = adapter.getValueById(currencyId);

            currencyUpdater.setSelectedCurrencyId(currencyId);
            currencyUpdater.setSelectedCurrencyValue(value);
        }

        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Initialize the RecycleView Adapter.
     */
    private void initAdapter()
    {
        initHashMaps();

        adapter = new CurrencyRatesAdapter(this, textWatcher);
        adapter.setLoaderBar(loaderBar);
        adapter.setCurrencyFlags(currencyFlags);
        adapter.setCurrencyNames(currencyNames);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        try {
            RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            if (itemAnimator != null) {
                ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * Initialize the Flags and Names Lists.
     */
    private void initHashMaps()
    {
        currencyFlags = new HashMap<>();
        currencyNames = new HashMap<>();

        String[] currencyIdNames = getResources().getStringArray(R.array.currency_iso_names);
        String[] currencyNames = getResources().getStringArray(R.array.currency_names);

        int length = currencyIdNames.length;
        for (int i = 0; i < length; i++) {
            this.currencyNames.put(currencyIdNames[i], currencyNames[i]);
            Drawable flag = FlagHelper.getDrawableByFlag(this, currencyIdNames[i]);
            if (flag == null) continue;
            currencyFlags.put(currencyIdNames[i], flag);
        }
    }
}
