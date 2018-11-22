package com.example.emptytest.testglovo.domain;

public interface DataReceiver<T> {
    public void onSuccess(T data);
    public void onError();
}
