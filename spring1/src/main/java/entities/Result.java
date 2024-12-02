package entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "result")
/**
 * Entity linked to the result table.
 */
public class Result {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    /**
     * Cannot be NULL (see SQL file)
     */
    @Column(name = "field")
    private String field;

    /**
     * Cannot be NULL (see SQL file)
     */
    @Column(name = "course_unit")
    private String courseUnit;

    @Column(name = "unit_coeff", nullable = false)
    private BigDecimal unitCoeff;

    @OneToMany(mappedBy = "result")
    private Set<Assessment> assessments;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    public Result(String field, String courseUnit, BigDecimal unitCoeff, Set<Assessment> assessments,
                  Student student) {
        this.field = field;
        this.courseUnit = courseUnit;
        this.unitCoeff = unitCoeff;
        this.assessments = assessments;
        this.student = student;
    }

    public Result() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCourseUnit() {
        return courseUnit;
    }

    public void setCourseUnit(String courseUnit) {
        this.courseUnit = courseUnit;
    }

    public BigDecimal getUnitCoeff() {
        return unitCoeff;
    }

    public void setUnitCoeff(BigDecimal unitCoeff) {
        this.unitCoeff = unitCoeff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (id != result.id) return false;
        if (field != null ? !field.equals(result.field) : result.field != null) return false;
        if (courseUnit != null ? !courseUnit.equals(result.courseUnit) : result.courseUnit != null) return false;
        if (unitCoeff != null ? !unitCoeff.equals(result.unitCoeff) : result.unitCoeff != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (courseUnit != null ? courseUnit.hashCode() : 0);
        result = 31 * result + (unitCoeff != null ? unitCoeff.hashCode() : 0);
        return result;
    }

    public Set<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(Set<Assessment> assessments) {
        this.assessments = assessments;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
