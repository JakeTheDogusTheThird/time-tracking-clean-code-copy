package org.example.service;

import java.util.Comparator;

public class GenericComparator<T> implements Comparator<T> {
  private final RankCalculator<T> rankCalculator;
  private final double marginOfError;

  public GenericComparator(RankCalculator<T> rankCalculator, double marginOfError) {
    this.rankCalculator = rankCalculator;
    this.marginOfError = marginOfError;
  }

  @Override
  public int compare(T o1, T o2) {
    double rank1 = rankCalculator.calculateRank(o1);
    double rank2 = rankCalculator.calculateRank(o2);
    if (Math.abs(rank1 - rank2) < marginOfError) {
      return 0;
    }
    if (rank1 < rank2) {
      return -1;
    }
    return 1;
  }
}
