package com.example.project;

public class WrapperQuizResult {
    private Integer quizId;
    private Integer score;
    private Integer indexInList;

    public WrapperQuizResult(Integer quizId, Integer score, Integer indexInList) {
        this.quizId = quizId;
        this.score = score;
        this.indexInList = indexInList;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getIndexInList() {
        return indexInList;
    }
}
