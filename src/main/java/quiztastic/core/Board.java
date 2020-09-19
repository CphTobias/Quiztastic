package quiztastic.core;

import java.util.ArrayList;
import java.util.List;

/** A Jeopardy Board
 *
 */
public class Board {
    private final List<Group> groups;

    //Her checker vi vor mange grupper/kategorier vi har
    public Board(List<Group> groups) {
        this.groups = List.copyOf(groups);
        if (this.groups.size() != 5) {
            throw new IllegalArgumentException(
                    "Should be 5 groups, there were " + groups.size());
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "Board{" +
                "groups=" + groups +
                '}';
    }

    public static class Group {
        private final Category category;
        private final List<Question> questions;

        public Group(Category category, List<Question> questions) {
            this.category = category;
            this.questions = List.copyOf(questions);
            validate();
        }

        //Her vælger vi hvor mange spørgsmål vi vil have til vær kategori.
        private void validate() {
            if (questions.size() != 5) {
                throw new IllegalArgumentException(
                        "Should be 5 groups, there were " + questions.size());
            }
            for (Question q : questions) {
                if (!q.getCategory().equals(category)) {
                    throw new IllegalArgumentException("Expected all categories to be "
                            + category + " but was " + q.getCategory());
                }
            }
        }

        public Category getCategory() {
            return category;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "category=" + category +
                    ", questions=" + questions +
                    '}';
        }
    }
}
