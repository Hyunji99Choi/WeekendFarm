package com.example.edrkr.h_network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class AutoRetryCallback<T> implements Callback<T> {
    private final int mRetryLimitCount;
    private int mRetryCount = 0;

    public AutoRetryCallback() {
        this.mRetryLimitCount = 3;
    }
    public AutoRetryCallback(int retryLimitCount) {
        this.mRetryLimitCount = retryLimitCount;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("Fail","통신시도 실패");
        mRetryCount++;
        if (mRetryCount > mRetryLimitCount) {
            onFinalFailure(call, t);
            return;
        }
        retry(call);
    }

    private void retry(Call<T> call) {
        call.clone().enqueue(this);
    }

    public abstract void onFinalFailure(Call<T> call, Throwable t);

}
