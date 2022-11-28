package com.example.project;

public class WrapperQuizResult {
    private Number quizId;
    private Number score;
    private Number indexInList;

    public WrapperQuizResult(Number quizId, Number score, Number indexInList) {
        this.quizId = quizId;
        this.score = score;
        this.indexInList = indexInList;
    }

    public Number getQuizId() {
        return quizId;
    }

    public Number getScore() {
        return score;
    }

    public Number getIndexInList() {
        return indexInList;
    }
}
