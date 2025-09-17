package org.example.service;

import org.example.model.Person;

import java.util.Comparator;

public class AbstractComparator<T> implements Comparator<T> {
  private final RankCalculator<T> rankCalculator;

  public AbstractComparator(RankCalculator<T> rankCalculator) {
    this.rankCalculator = rankCalculator;
  }

  @Override
  public int compare(T o1, T o2) {
    return Double.compare(rankCalculator.calculateRank(o1),
        rankCalculator.calculateRank(o2));
  }
}
