package quiztastic.app;

import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class MapQuestionRepository implements QuestionRepository {
    private final HashMap<Category, Set<Question>> questionsByCategory;

    public MapQuestionRepository(HashMap<Category, Set<Question>> questionsByCategory) {
        this.questionsByCategory = questionsByCategory;
    }

    /*
    Hashmap indeholder Category og Question
     */

    public static MapQuestionRepository fromQuestionReader(QuestionReader reader) throws IOException, ParseException {
        HashMap<Category, Set<Question>> questionsByCategory = new HashMap<>();
        Question q;
        //Her putter vi alle questionsne ind i et HashMap
        while ((q = reader.readQuestion()) != null) {
            Set<Question> current = questionsByCategory.get(q.getCategory());
            if (current == null) {
                current = new HashSet<>();
                questionsByCategory.put(q.getCategory(), current);
            }
            current.add(q);
        }
        return new MapQuestionRepository(questionsByCategory);
    }
    @Override
    public List<Category> getCategories() {
        List<Category> categoryNames = List.copyOf(questionsByCategory.keySet());

        return categoryNames;
    }

    @Override
    public List<Question> getQuestionsWithCategory(Category category) {
        List<Question> questionsWithCategory = List.copyOf(questionsByCategory.get(category));
        return questionsWithCategory;
    }

    @Override
    public Iterable<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        for (Set<Question> l : questionsByCategory.values()) {
            questions.addAll(l);
        }
        return questions;
    }
}
