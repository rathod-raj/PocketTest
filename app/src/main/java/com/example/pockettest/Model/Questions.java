/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Model;

public class Questions {

    private String  quiz_id;
    private String question_id;
    private String title;
    private String marks;
    private Answer answer_1;
    private Answer answer_2;
    private Answer answer_3;
    private Answer answer_4;

    public Questions() {
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public Answer getAnswer_1() {
        return answer_1;
    }

    public void setAnswer_1(Answer answer_1) {
        this.answer_1 = answer_1;
    }

    public Answer getAnswer_2() {
        return answer_2;
    }

    public void setAnswer_2(Answer answer_2) {
        this.answer_2 = answer_2;
    }

    public Answer getAnswer_3() {
        return answer_3;
    }

    public void setAnswer_3(Answer answer_3) {
        this.answer_3 = answer_3;
    }

    public Answer getAnswer_4() {
        return answer_4;
    }

    public void setAnswer_4(Answer answer_4) {
        this.answer_4 = answer_4;
    }
}
