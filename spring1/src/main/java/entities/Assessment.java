package entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "assessment", schema = "cy_e_admin")
/**
 * Entity linked to the assessment table.
 */
public class Assessment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "assess_name", nullable = false)
    private String assessName;

    @Column(name = "coefficient", nullable = false)
    private BigDecimal coefficient;

    @Column(name = "test_date", nullable = false)
    private Date testDate;

    @Column(name = "grade", nullable = false)
    private BigDecimal grade;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "result_id", referencedColumnName = "id", nullable = false)
    private Result result;

    public Assessment(String assessName, BigDecimal coefficient, Date testDate, BigDecimal grade,
                      Result result) {
        this.assessName = assessName;
        this.coefficient = coefficient;
        this.testDate = testDate;
        this.grade = grade;
        this.result = result;
    }

    public Assessment() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssessName() {
        return assessName;
    }

    public void setAssessName(String assessName) {
        this.assessName = assessName;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assessment that = (Assessment) o;

        if (id != that.id) return false;
        if (assessName != null ? !assessName.equals(that.assessName) : that.assessName != null) return false;
        if (coefficient != null ? !coefficient.equals(that.coefficient) : that.coefficient != null) return false;
        if (testDate != null ? !testDate.equals(that.testDate) : that.testDate != null) return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (assessName != null ? assessName.hashCode() : 0);
        result = 31 * result + (coefficient != null ? coefficient.hashCode() : 0);
        result = 31 * result + (testDate != null ? testDate.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        return result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
