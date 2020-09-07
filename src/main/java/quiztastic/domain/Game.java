package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.util.List;

public class Game {
    private final Board board;
    private final List<Answer> answerList;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
    }

    public List<Category> getCategory(){
        return null;
    }

    public String answerQuestion(int categoryNumber, int questionNumber, String answer){
        Question q = this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
        if(q.getAnswer().equals(answer)){
            return null;
        } else {
            return q.getAnswer();
        }
    }

    public boolean isAnswered(int categoryNumber, int questionNumber){
        for (Answer a : answerList) {
            if(a.categoryNumber == categoryNumber && a.questionNumber == questionNumber){
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
    }
}
