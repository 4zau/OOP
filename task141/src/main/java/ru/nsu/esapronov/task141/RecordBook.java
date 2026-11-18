package ru.nsu.esapronov.task141;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;

/** Электронная зачетная книжка. */
public class RecordBook {
    private final String studentName;
    private final List<Assessment> studyPlan;
    private final Map<String, Assessment> assessments = new HashMap<>();
    private final Map<String, Grade> grades = new HashMap<>();
    private EducationForm educationForm;
    private int currentSemester;

    /**
     * Создает книжку.
     *
     * @param studentName имя студента
     * @param educationForm форма финансирования
     * @param currentSemester текущий семестр
     * @param studyPlan полный план
     */
    public RecordBook(String studentName, EducationForm educationForm,
                      int currentSemester, List<Assessment> studyPlan) {
        this.studentName = Objects.requireNonNull(studentName, "studentName");
        this.educationForm = Objects.requireNonNull(educationForm, "educationForm");
        this.studyPlan = List.copyOf(studyPlan);
        for (Assessment assessment : this.studyPlan) {
            if (assessments.putIfAbsent(assessment.id(), assessment) != null) {
                throw new IllegalArgumentException("Повтор id: " + assessment.id());
            }
        }
        setCurrentSemester(currentSemester);
    }

    /**
     * Имя владельца книжки.
     *
     * @return имя студента
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Текущий семестр.
     *
     * @return номер семестра начиная с 1
     */
    public int getCurrentSemester() {
        return currentSemester;
    }

    /**
     * Форма финансирования.
     *
     * @return текущая форма обучения
     */
    public EducationForm getEducationForm() {
        return educationForm;
    }

    /**
     * Меняет форму финансирования.
     *
     * @param educationForm новая форма обучения
     */
    public void setEducationForm(EducationForm educationForm) {
        this.educationForm = Objects.requireNonNull(educationForm, "educationForm");
    }

    /**
     * Устанавливает текущий семестр.
     *
     * @param semester новый номер семестра
     */
    public void setCurrentSemester(int semester) {
        if (semester < 1) {
            throw new IllegalArgumentException("Номер семестра должен быть положительным");
        }
        currentSemester = semester;
    }

    /**
     * Возвращает учебный план.
     *
     * @return полный план обучения
     */
    public List<Assessment> getStudyPlan() {
        return studyPlan;
    }

    /**
     * Возвращает копию текущих результатов.
     *
     * @return оценки по идентификаторам контролей
     */
    public Map<String, Grade> getGrades() {
        return Map.copyOf(grades);
    }

    /**
     * Записывает результат или заменяет его.
     * Для обычного зачета разрешены только PASS и NOT_PASSED.
     *
     * @param assessmentId идентификатор из плана
     * @param grade результат контроля
     */
    public void recordGrade(String assessmentId, Grade grade) {
        Objects.requireNonNull(grade, "grade");
        Assessment assessment = assessments.get(assessmentId);
        if (assessment == null) {
            throw new IllegalArgumentException("Неизвестный контроль");
        }
        if ((assessment.type() == ControlType.CREDIT) == grade.isNumeric()) {
            throw new IllegalArgumentException("Результат не соответствует виду контроля");
        }
        grades.put(assessmentId, grade);
    }

    /**
     * Среднее всех выставленных числовых оценок.
     *
     * @return средний балл или пустой результат, если оценок нет
     */
    public OptionalDouble averageGrade() {
        return grades.values().stream().filter(Grade::isNumeric)
                .mapToInt(Grade::points).average();
    }

    /**
     * Возможен ли перевод на бюджет по двум завершенным сессиям перед текущим
     * семестром. Все плановые экзамены этих сессий должны быть сданы на 4 или 5.
     *
     * @return выполнены ли условия перевода
     */
    public boolean canTransferToBudget() {
        if (educationForm != EducationForm.PAID || currentSemester < 3) {
            return false;
        }
        return studyPlan.stream().filter(a -> a.type() == ControlType.EXAM)
                .filter(a -> a.semester() >= currentSemester - 2
                        && a.semester() < currentSemester)
                .allMatch(a -> grades.get(a.id()) == Grade.GOOD
                        || grades.get(a.id()) == Grade.EXCELLENT);
    }

    /**
     * Прогноз диплома с отличием.
     *
     * @return возможен ли диплом с отличием при выполнении оставшегося плана на 5
     */
    public boolean canGetHonorsDiploma() {
        Map<String, Assessment> lastControls = new LinkedHashMap<>();
        for (Assessment assessment : studyPlan) {
            if (assessment.type().isDiplomaControl()) {
                lastControls.merge(assessment.subject(), assessment,
                        (left, right) -> left.semester() > right.semester() ? left : right);
            }
        }
        Assessment thesis = studyPlan.stream()
                .filter(a -> a.type() == ControlType.THESIS)
                .findFirst()
                .orElse(null);
        if (thesis != null
                && grades.getOrDefault(thesis.id(), Grade.EXCELLENT) != Grade.EXCELLENT) {
            return false;
        }
        int excellent = 1;
        for (Assessment assessment : lastControls.values()) {
            Grade grade = grades.getOrDefault(assessment.id(), Grade.EXCELLENT);
            if (grade.points() < Grade.GOOD.points()) {
                return false;
            }
            if (grade == Grade.EXCELLENT) {
                excellent++;
            }
        }
        return excellent * 4 >= (lastControls.size() + 1) * 3;
    }

    /**
     * Повышенная стипендия.
     *
     * @return выполнены ли условия повышенной стипендии
     */
    public boolean canGetIncreasedScholarship() {
        if (educationForm != EducationForm.BUDGET || currentSemester < 2) {
            return false;
        }
        List<Assessment> session = studyPlan.stream()
                .filter(a -> a.semester() == currentSemester - 1)
                .filter(a -> a.type() == ControlType.EXAM
                        || a.type() == ControlType.DIFFERENTIATED_CREDIT
                        || a.type() == ControlType.CREDIT)
                .toList();
        return !session.isEmpty()
                && session.stream().allMatch(a -> grades.get(a.id())
                        == (a.type() == ControlType.CREDIT ? Grade.PASS : Grade.EXCELLENT));
    }
}
