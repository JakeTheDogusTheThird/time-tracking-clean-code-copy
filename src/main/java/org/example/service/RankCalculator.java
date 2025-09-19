package org.example.service;

public interface RankCalculator<T> {
  double calculateRank(T entity);
}
