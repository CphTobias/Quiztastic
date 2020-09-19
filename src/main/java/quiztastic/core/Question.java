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

    //Question, bliver tilføget af QuestionReaderen, efter opsplitning af .tsv fil fra Quiztastic.
    public Question(int score, Category category, String question, String answer) {
        this.score = score;
        this.category = category;
        this.question = question;
        this.answer = answer;
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

    @Override
    public String toString() {
        return "Question{" +
                "score=" + score +
                ", category=" + category +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
