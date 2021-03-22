package com.tawktest.app.utils;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

public abstract class BackoffCallback<T> implements Callback<T> {
    private static final int RETRY_COUNT = 3;
    /**
     * Base retry delay for exponential backoff, in Milliseconds
     */
    private static final double RETRY_DELAY = 300;
    private int retryCount = 0;

    @Override
    public void onFailure(@NotNull final Call<T> call, @NotNull Throwable t) {
        retryCount++;
        if (retryCount <= RETRY_COUNT) {
            int expDelay = (int) (RETRY_DELAY * Math.pow(2, Math.max(0, retryCount - 1)));
            new Handler(Looper.myLooper()).postDelayed(() -> retry(call), expDelay);
        } else {
            onFailedAfterRetry(t);
        }
    }

    private void retry(Call<T> call) {
        call.clone().enqueue(this);
    }

    public abstract void onFailedAfterRetry(Throwable t);
}