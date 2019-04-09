package com.kakaopay.pretest.persistence.repository;

public interface CommonProcess<T> {
    T saveIfNotExist(T data);
}