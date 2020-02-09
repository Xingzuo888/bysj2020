package com.example.bysj2020.https;

import java.util.HashMap;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class RxApiManager implements RxActionManager<Object> {
    private static RxApiManager sInstance = null;
    private HashMap<Object, Disposable> maps;

    public static RxApiManager get() {
        if (sInstance == null) {
            synchronized (RxApiManager.class) {
                if (sInstance == null) {
                    sInstance = new RxApiManager();
                }
            }
        }
        return sInstance;
    }

    private RxApiManager() {
        maps = new HashMap<>();
    }

    @Override
    public void add(Object tag, Disposable disposable) {
        maps.put(tag, disposable);
    }

    @Override
    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    @Override
    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    @Override
    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
