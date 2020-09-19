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
    //Der bliver lavet en liste af svar efter en spiller svarer på et spørgsmål.
    private final List<Answer> answerList;
    //Vi laver et array af de players som kommer ind i vores spil.
    public ArrayList<Player> players = new ArrayList<Player>();

    private volatile Player roundPlayer;
    private volatile int activePlayers = 0;
    private volatile Answer currentanswer = null;

    private int counter = 0;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
    }

    /*public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();
        for (Board.Group group : this.board.getGroups()) {
            Category category = group.getCategory();
            list.add(category);
        }
        return list;
    }*/

    public List<Category> getCategory(){
        return null;
    }

    //Den undersøger om dit svar er = svaret i .tsv filen, og returner null hvis det er rigtigt.
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
        //Active players tæller hvor mange som er i spillet
        activePlayers += 1;
        notifyAll();

        //Den vil side og vente på at alle spillerene i spillet har skrevet play
        while (activePlayers != players.size()){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Der bliver fundet en "Vinder" som er den person som bliver returneret og er den person som skal spille.
        if(roundPlayer == null) {
            roundPlayer = getRandomPlayer();
        }

        return roundPlayer;
    }

    public Player getRandomPlayer() {
        Random random = new Random();
        int randomPlayer = random.nextInt(players.size());
        return players.get(randomPlayer);
    }

    //Counter på hvor mange ID'er vi har i spillet
    public synchronized int makeCounter(){
        counter++;
        return counter;
    }

    //Kan blive kaldt til at tilføje en spiller til spillet.
    public void addPlayer(Player p){
        players.add(p);
    }

    public String getQuestionText(int categoryNumber, int questionNumber) {
        return getQuestion(categoryNumber, questionNumber).getQuestion();
    }

    private Question getQuestion(int categoryNumber, int questionNumber) {
        return this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
    }

    //Bliver kaldt til at fjerne et spørgsmål fra kategorierne
    public boolean isAnswered (int categoryNumber, int questionNumber){
         for (Answer a : answerList) {
              if (a.categoryNumber == categoryNumber && a.questionNumber == questionNumber) {
              //if (a.hasIndex(categoryNumber,questionNumber)){
                   return true;
              }
         }
         return false;
    }

    //Venter på at personen som er blevet valgt til at spille får svaret på sit spørgsmål
    public synchronized Answer waitForAnswer() throws InterruptedException {
        while(currentanswer == null){
            wait();
        }
        return currentanswer;
    }

    //Her tager vi imod alle de svar som kommer ind i spillet.
    public class Answer {
                    private final int categoryNumber;
                    private final int questionNumber;
                    private final String answer;

                private Answer(int categoryNumber, int questionNumber, String answer) {
                    this.categoryNumber = categoryNumber;
                    this.questionNumber = questionNumber;
                    this.answer = answer;
                }

        public int getCategoryNumber() {
            return categoryNumber;
        }

        public int getQuestionNumber() {
            return questionNumber;
        }

        public String getAnswer() {
            return answer;
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
