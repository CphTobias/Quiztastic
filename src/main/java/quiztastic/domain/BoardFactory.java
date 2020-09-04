    package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BoardFactory {
    public final QuestionRepository questionRepository;

    public BoardFactory(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    static char questionChars = 'A';
    public Board.Group makeGroup(Category c) throws IllegalArgumentException {

        List<Question> questions =
                questionRepository.getQuestionsWithCategory(c);
        if (questions.size() >= 5) {
            return new Board.Group(c, questions.subList(0, 5));
        } else {
            throw new IllegalArgumentException("Not enough questions in category");
        }
    }

    public void makeChar(){
        questionChars+=1;
    }

    public Board makeBoard() {
        List<Board.Group> groups = new ArrayList<>();
        for (Category c : questionRepository.getCategories()) {
            if (groups.size() == 6) break;
            try {
                groups.add(makeGroup(c));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        return new Board(groups);
    }
}
