package com.example.rolling;

public interface InfoChangedListener<T1, T2> {

    void infoChanged(T1 value1, T1 value2, T2 value3);
}
