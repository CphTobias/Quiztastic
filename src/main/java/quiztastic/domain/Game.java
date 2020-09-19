package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Player;
import quiztastic.core.Question;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Board board;
    private final List<Answer> answerList;
    public ArrayList<Player> players = new ArrayList<Player>();

    private volatile Player roundPlayer;
    private volatile int activePlayers = 0;

    private volatile Answer currentanswer = null;

    private int counter = 0;

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


    public synchronized String answerQuestion(int categoryNumber, int questionNumber, String answer){
        Question q = this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
        currentanswer = new Answer(categoryNumber, questionNumber, answer);
        notifyAll();
        answerList.add(currentanswer);
        if(q.getAnswer().equals(answer)){
            return null;
        } else {
            return q.getAnswer();
        }
    }


    public synchronized Player startGame() {
        if(currentanswer != null){
            activePlayers = 0;
            currentanswer = null;
            roundPlayer = null;
        }
        activePlayers += 1;
        notifyAll();
        while (activePlayers != players.size()){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(roundPlayer == null) {
            roundPlayer = getRandomPlayer();
        }

        return roundPlayer;
    }

    private Player getRandomPlayer() {
        Random random = new Random();
        int randomPlayer = random.nextInt(players.size());
        return players.get(randomPlayer);
    }

    public synchronized int makeCounter(){
        counter++;
        return counter;
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

    public synchronized Answer waitForAnswer() throws InterruptedException {
        while(currentanswer == null){
            wait();
        }
        return currentanswer;
    }

    public class Answer {
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

        @Override
        public String toString() {
            char alfabet[] = {'A', 'B', 'C', 'D', 'E'};

            return  "Spillerens svar på " + alfabet[categoryNumber] + (questionNumber+1)*100 +
                    " er: " + answer;
        }
    }
}
