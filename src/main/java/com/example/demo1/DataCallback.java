package com.example.demo1;

public interface DataCallback {
    void onSuccess(String data);
    void onFailure(Exception e);
}
