package repositories;

import entities.Student;
import entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s WHERE s.mail = :mail AND s.pswd = :pswd")
    Student findStudentByMailAndPswd(String mail, String pswd);

    @Query("SELECT s FROM Student s WHERE s.mail = :mail")
    Student findStudentByMail(String mail);

    @Query("SELECT s FROM Student s WHERE s.schoolYear = :schoolYear")
    List<Student> findStudentsBySchoolYear(String schoolYear);
}
