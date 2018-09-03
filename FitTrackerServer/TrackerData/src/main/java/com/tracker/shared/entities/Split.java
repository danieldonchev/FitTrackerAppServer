package com.tracker.shared.entities;

import java.util.Objects;

public class Split {

    private int startIndex;
    private int endIndex;

    public Split() {
    }

    public Split(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Split split = (Split) o;
        return startIndex == split.startIndex &&
                endIndex == split.endIndex;
    }

    @Override
    public int hashCode() {

        return Objects.hash(startIndex, endIndex);
    }

    @Override
    public String toString() {
        return "Split{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }
}
