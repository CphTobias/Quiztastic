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
    public Board.Group makeGroup(Category c) throws IllegalArgumentException {
        //Her sætter vi hvor mange questions vi vil have i vores Category
        List<Question> questions =
                questionRepository.getQuestionsWithCategory(c);
        if (questions.size() >= 5) {
            return new Board.Group(c, questions.subList(0, 5));
        } else {
            throw new IllegalArgumentException("Not enough questions in category");
        }
    }


    public Board makeBoard() {
        List<Board.Group> groups = new ArrayList<>();
        //Her vælger vi hvor mange Categories vi vil have.
        for (Category c : questionRepository.getCategories()) {
            if (groups.size() == 5) break;
            try {
                groups.add(makeGroup(c));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        return new Board(groups);
    }
}
