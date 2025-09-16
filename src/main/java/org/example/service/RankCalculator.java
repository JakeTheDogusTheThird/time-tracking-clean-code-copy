package org.example.service;

public interface RankCalculator<T> {
  public double calculateRank(T entity);
}
