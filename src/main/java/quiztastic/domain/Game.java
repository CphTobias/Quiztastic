package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Player;
import quiztastic.core.Question;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final Board board;
    private final List<Answer> answerList;
    public ArrayList<Player> players = new ArrayList<Player>();

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
    }

    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();
        for (Board.Group group : this.board.getGroups()) {
            Category category = group.getCategory();
            list.add(category);
        }
        return list;
    }

    public List<Category> getCategory(){
        return null;
    }

    public String answerQuestion(int categoryNumber, int questionNumber, String answer){
        Question q = this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
        answerList.add(new Answer(categoryNumber, questionNumber, answer));
        if(q.getAnswer().equals(answer)){
            return null;
        } else {
            return q.getAnswer();
        }
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public String getQuestionText(int categoryNumber, int questionNumber) {
        return getQuestion(categoryNumber, questionNumber).getQuestion();
    }

    private Question getQuestion(int categoryNumber, int questionNumber) {
        return this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
    }

    public boolean isAnswered (int categoryNumber, int questionNumber){
         for (Answer a : answerList) {
              if (a.categoryNumber == categoryNumber && a.questionNumber == questionNumber) {
              //if (a.hasIndex(categoryNumber,questionNumber)){
                   return true;
              }
         }
         return false;
    }

            private class Answer {
                    private final int categoryNumber;
                    private final int questionNumber;
                    private final String answer;

                private Answer(int categoryNumber, int questionNumber, String answer) {
                    this.categoryNumber = categoryNumber;
                    this.questionNumber = questionNumber;
                    this.answer = answer;
                }


                public boolean hasIndex(int categoryNumber, int questionNumber) {
                        return this.categoryNumber == categoryNumber && this.questionNumber == questionNumber;
                    }
                }
            }
