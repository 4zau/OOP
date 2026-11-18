package ru.nsu.esapronov.task141;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Проверка. */
class RecordBookTest {
    private static Assessment control(String id, int semester, ControlType type) {
        return new Assessment(id, id, semester, type);
    }

    private static RecordBook book(EducationForm form, int semester, Assessment... controls) {
        List<Assessment> plan = new ArrayList<>(List.of(controls));
        plan.add(control("thesis", 8, ControlType.THESIS));
        return new RecordBook("Студент", form, semester, plan);
    }

    @Test
    void emptyAverageIsNotZero() {
        RecordBook book = book(EducationForm.PAID, 1);
        assertTrue(book.averageGrade().isEmpty());
        assertFalse(book.canTransferToBudget());
        assertFalse(book.canGetIncreasedScholarship());
        assertTrue(book.canGetHonorsDiploma());
    }

    @Test
    void averageIncludesNumericControlsAndUsesRetake() {
        RecordBook book = book(EducationForm.PAID, 2,
                control("exam1", 1, ControlType.EXAM),
                control("exam2", 2, ControlType.EXAM),
                control("task", 1, ControlType.ASSIGNMENT),
                control("credit", 2, ControlType.CREDIT));
        book.recordGrade("exam1", Grade.SATISFACTORY);
        book.recordGrade("exam2", Grade.EXCELLENT);
        book.recordGrade("task", Grade.FAIL);
        book.recordGrade("credit", Grade.PASS);
        assertEquals(10.0 / 3, book.averageGrade().orElseThrow(), 1e-10);
        book.recordGrade("exam1", Grade.GOOD);
        assertEquals(11.0 / 3, book.averageGrade().orElseThrow(), 1e-10);
        assertEquals(4, book.getGrades().size());
    }

    @ParameterizedTest
    @EnumSource(ControlType.class)
    void acceptsEveryControlType(ControlType type) {
        RecordBook book = type == ControlType.THESIS ? book(EducationForm.PAID, 8)
                : book(EducationForm.PAID, 8, control("control", 1, type));
        String id = type == ControlType.THESIS ? "thesis" : "control";
        book.recordGrade(id, type == ControlType.CREDIT ? Grade.PASS : Grade.EXCELLENT);
        assertEquals(1, book.getGrades().size());
    }

    @Test
    void supportsAllNinetyFiveControlsFromAttachedTable() {
        int[][] counts = {
            {2, 2, 3, 2, 2, 2, 2, 0},
            {3, 3, 2, 1, 2, 2, 0, 0},
            {1, 1, 0, 0, 0, 0, 0, 0},
            {3, 3, 2, 5, 3, 2, 1, 0},
            {3, 3, 6, 5, 4, 6, 4, 4},
            {3, 2, 0, 0, 0, 0, 1, 1},
            {0, 0, 0, 0, 0, 0, 1, 2},
            {0, 0, 0, 0, 0, 0, 0, 1}
        };
        List<Assessment> plan = new ArrayList<>();
        for (ControlType type : ControlType.values()) {
            for (int semester = 1; semester <= 8; semester++) {
                for (int number = 0; number < counts[type.ordinal()][semester - 1]; number++) {
                    String id = type + "-" + semester + "-" + number;
                    plan.add(control(id, semester, type));
                }
            }
        }
        RecordBook book = new RecordBook("Студент", EducationForm.BUDGET, 8, plan);
        for (Assessment assessment : plan) {
            book.recordGrade(assessment.id(), assessment.type() == ControlType.CREDIT
                    ? Grade.PASS : Grade.EXCELLENT);
        }
        assertEquals(95, book.getStudyPlan().size());
        assertEquals(95, book.getGrades().size());
        assertEquals(5.0, book.averageGrade().orElseThrow());
        assertTrue(book.canGetHonorsDiploma());
        assertTrue(book.canGetIncreasedScholarship());
    }

    @Test
    void transferIgnoresOldAndCurrentSessionAndDifferentiatedCredits() {
        RecordBook book = book(EducationForm.PAID, 4,
                control("old", 1, ControlType.EXAM),
                control("exam2", 2, ControlType.EXAM),
                control("exam3", 3, ControlType.EXAM),
                control("diff", 3, ControlType.DIFFERENTIATED_CREDIT),
                control("current", 4, ControlType.EXAM));
        book.recordGrade("old", Grade.SATISFACTORY);
        book.recordGrade("exam2", Grade.GOOD);
        book.recordGrade("exam3", Grade.EXCELLENT);
        book.recordGrade("diff", Grade.SATISFACTORY);
        book.recordGrade("current", Grade.SATISFACTORY);
        assertTrue(book.canTransferToBudget());
        book.setEducationForm(EducationForm.BUDGET);
        assertFalse(book.canTransferToBudget());
    }

    @Test
    void transferNeedsTwoCompletedSessionsAndAllTheirExams() {
        RecordBook book = book(EducationForm.PAID, 2,
                control("exam1", 1, ControlType.EXAM),
                control("exam2", 2, ControlType.EXAM));
        book.recordGrade("exam1", Grade.EXCELLENT);
        assertFalse(book.canTransferToBudget());
        book.setCurrentSemester(3);
        assertFalse(book.canTransferToBudget());
        book.recordGrade("exam2", Grade.SATISFACTORY);
        assertFalse(book.canTransferToBudget());
        book.recordGrade("exam2", Grade.FAIL);
        assertFalse(book.canTransferToBudget());
        book.recordGrade("exam2", Grade.GOOD);
        assertTrue(book.canTransferToBudget());
    }

    @Test
    void honorsForecastUsesFutureFinalGradeInsteadOfEarlierThree() {
        RecordBook book = book(EducationForm.PAID, 1,
                new Assessment("math1", "Математика", 1, ControlType.EXAM),
                new Assessment("math2", "Математика", 4, ControlType.DIFFERENTIATED_CREDIT));
        book.recordGrade("math1", Grade.SATISFACTORY);
        assertTrue(book.canGetHonorsDiploma());
        book.setCurrentSemester(4);
        book.recordGrade("math2", Grade.SATISFACTORY);
        assertFalse(book.canGetHonorsDiploma());
        book.recordGrade("math2", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
    }

    @Test
    void honorsUsesSemesterOrderInsteadOfInsertionOrder() {
        RecordBook book = book(EducationForm.BUDGET, 8,
                new Assessment("last", "Предмет", 6, ControlType.EXAM),
                new Assessment("first", "Предмет", 1, ControlType.EXAM));
        book.recordGrade("first", Grade.SATISFACTORY);
        book.recordGrade("last", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
    }

    @Test
    void honorsBoundaryIsExactlySeventyFivePercentIncludingThesis() {
        RecordBook book = book(EducationForm.BUDGET, 8,
                control("a", 1, ControlType.EXAM),
                control("b", 2, ControlType.DIFFERENTIATED_CREDIT),
                control("c", 7, ControlType.PRACTICE_REPORT));
        book.recordGrade("a", Grade.GOOD);
        book.recordGrade("b", Grade.EXCELLENT);
        book.recordGrade("c", Grade.EXCELLENT);
        book.recordGrade("thesis", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
        book.recordGrade("b", Grade.GOOD);
        assertFalse(book.canGetHonorsDiploma());
    }

    @ParameterizedTest
    @EnumSource(value = ControlType.class,
            names = {"EXAM", "DIFFERENTIATED_CREDIT", "PRACTICE_REPORT"})
    void finalThreeOrFailBlocksHonors(ControlType type) {
        RecordBook book = book(EducationForm.BUDGET, 8,
                control("a", 1, type), control("b", 1, ControlType.EXAM),
                control("c", 1, ControlType.EXAM), control("d", 1, ControlType.EXAM));
        book.recordGrade("a", Grade.SATISFACTORY);
        assertFalse(book.canGetHonorsDiploma());
        book.recordGrade("a", Grade.FAIL);
        assertFalse(book.canGetHonorsDiploma());
    }

    @Test
    void thesisMustBeExcellentButCanStillBePending() {
        RecordBook book = book(EducationForm.BUDGET, 8);
        assertTrue(book.canGetHonorsDiploma());
        book.recordGrade("thesis", Grade.GOOD);
        assertFalse(book.canGetHonorsDiploma());
        book.recordGrade("thesis", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
    }

    @Test
    void intermediateMarksDoNotEnterDiploma() {
        RecordBook book = book(EducationForm.PAID, 2,
                control("task", 1, ControlType.TEST), control("credit", 1, ControlType.CREDIT));
        book.recordGrade("task", Grade.SATISFACTORY);
        book.recordGrade("credit", Grade.PASS);
        assertTrue(book.canGetHonorsDiploma());
    }

    @Test
    void scholarshipRequiresBudgetAndCompleteExcellentPreviousSession() {
        RecordBook book = book(EducationForm.PAID, 2,
                control("exam", 1, ControlType.EXAM),
                control("diff", 1, ControlType.DIFFERENTIATED_CREDIT),
                control("credit", 1, ControlType.CREDIT),
                control("current", 2, ControlType.EXAM));
        book.recordGrade("exam", Grade.EXCELLENT);
        book.recordGrade("diff", Grade.EXCELLENT);
        assertFalse(book.canGetIncreasedScholarship());
        book.setEducationForm(EducationForm.BUDGET);
        assertFalse(book.canGetIncreasedScholarship());
        book.recordGrade("credit", Grade.NOT_PASSED);
        assertFalse(book.canGetIncreasedScholarship());
        book.recordGrade("credit", Grade.PASS);
        book.recordGrade("current", Grade.SATISFACTORY);
        assertTrue(book.canGetIncreasedScholarship());
        book.recordGrade("diff", Grade.GOOD);
        assertFalse(book.canGetIncreasedScholarship());
        book.recordGrade("diff", Grade.EXCELLENT);
        book.setEducationForm(EducationForm.PAID);
        assertFalse(book.canGetIncreasedScholarship());
    }

    @Test
    void scholarshipRequiresPreviousSessionButDoesNotRequireExams() {
        assertFalse(book(EducationForm.BUDGET, 1).canGetIncreasedScholarship());
        assertFalse(book(EducationForm.BUDGET, 2).canGetIncreasedScholarship());
        RecordBook book = book(EducationForm.BUDGET, 2, control("c", 1, ControlType.CREDIT));
        book.recordGrade("c", Grade.PASS);
        assertTrue(book.canGetIncreasedScholarship());
    }

    @Test
    void invalidResultsAreRejected() {
        RecordBook book = book(EducationForm.PAID, 1,
                control("e", 1, ControlType.EXAM), control("c", 1, ControlType.CREDIT));
        assertThrows(IllegalArgumentException.class, () -> book.recordGrade("missing", Grade.GOOD));
        assertThrows(IllegalArgumentException.class, () -> book.recordGrade("e", Grade.PASS));
        assertThrows(IllegalArgumentException.class, () -> book.recordGrade("c", Grade.GOOD));
        assertThrows(NullPointerException.class, () -> book.recordGrade("e", null));
        assertThrows(NullPointerException.class, () -> book.setEducationForm(null));
        assertThrows(IllegalArgumentException.class, () -> book.setCurrentSemester(0));
        assertEquals(EducationForm.PAID, book.getEducationForm());
        assertEquals("Студент", book.getStudentName());
    }

    @Test
    void invalidPlansAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment(" ", "Предмет", 1, ControlType.EXAM));
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment("id", " ", 1, ControlType.EXAM));
        assertThrows(IllegalArgumentException.class,
                () -> control("id", 0, ControlType.EXAM));
        assertThrows(NullPointerException.class,
                () -> control("id", 1, null));
        assertThrows(IllegalArgumentException.class,
                () -> book(EducationForm.PAID, 1, control("thesis", 1, ControlType.EXAM)));
    }

    @Test
    void multipleFinalControlsInSameSemesterUseLastInPlan() {
        RecordBook book = book(EducationForm.PAID, 1,
                new Assessment("exam", "Математика", 1, ControlType.EXAM),
                new Assessment("diff", "Математика", 1, ControlType.DIFFERENTIATED_CREDIT));
        book.recordGrade("exam", Grade.SATISFACTORY);
        book.recordGrade("diff", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
        book.recordGrade("diff", Grade.SATISFACTORY);
        assertFalse(book.canGetHonorsDiploma());
    }

    @Test
    void planCanBeEmptyOrHaveNoThesis() {
        RecordBook empty = new RecordBook("", EducationForm.PAID, 1, List.of());
        assertTrue(empty.getStudyPlan().isEmpty());
        assertTrue(empty.canGetHonorsDiploma());
        RecordBook book = new RecordBook("Студент", EducationForm.PAID, 1,
                List.of(control("exam", 1, ControlType.EXAM)));
        book.recordGrade("exam", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
        book.recordGrade("exam", Grade.SATISFACTORY);
        assertFalse(book.canGetHonorsDiploma());
    }

    @Test
    void multipleThesisControlsUseLastOne() {
        RecordBook book = book(EducationForm.PAID, 8,
                control("earlierThesis", 7, ControlType.THESIS));
        book.recordGrade("earlierThesis", Grade.GOOD);
        book.recordGrade("thesis", Grade.EXCELLENT);
        assertTrue(book.canGetHonorsDiploma());
        book.recordGrade("thesis", Grade.GOOD);
        assertFalse(book.canGetHonorsDiploma());
    }

    @Test
    void semesterCanMoveBackOrBeyondPlanAndGradesCanBeRecordedInAdvance() {
        RecordBook book = book(EducationForm.PAID, 1);
        book.recordGrade("thesis", Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, book.getGrades().get("thesis"));
        book.setCurrentSemester(9);
        assertEquals(9, book.getCurrentSemester());
        book.setCurrentSemester(2);
        assertEquals(2, book.getCurrentSemester());
        book.setCurrentSemester(1);
        assertEquals(1, book.getCurrentSemester());
    }

    @Test
    void externalCollectionsCannotChangeBook() {
        List<Assessment> plan = new ArrayList<>();
        plan.add(control("thesis", 8, ControlType.THESIS));
        RecordBook book = new RecordBook("Студент", EducationForm.BUDGET, 8, plan);
        plan.clear();
        assertEquals(1, book.getStudyPlan().size());
        assertThrows(UnsupportedOperationException.class, () -> book.getStudyPlan().clear());
        book.recordGrade("thesis", Grade.EXCELLENT);
        assertThrows(UnsupportedOperationException.class, () -> book.getGrades().clear());
        assertEquals(Grade.EXCELLENT, book.getGrades().get("thesis"));
    }
}
