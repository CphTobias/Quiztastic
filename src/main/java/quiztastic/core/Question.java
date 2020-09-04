package quiztastic.core;

/**
 * The Question Class.
 *
 * Should contain information about the questions
 */
public class Question {
    private final int score;
    private final Category category;
    private final String question;
    private final String answer;
    private char questionChar;

    public Question(int score, Category category, String question, String answer, char questionChar) {
        this.score = score;
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.questionChar = questionChar;
    }

    public int getScore() {
        return score;
    }

    public Category getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestionChar(char questionChar) {
        this.questionChar = questionChar;
    }

    @Override
    public String toString() {
        return  "\n" + score;
    }
}
