package repositories;

import entities.Teacher;
import entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("SELECT t FROM Teacher t WHERE t.mail = :mail AND t.pswd = :pswd")
    /***/
    Teacher findTeacherByMailAndPswd(String mail, String pswd);

    @Query("SELECT t FROM Teacher t WHERE t.mail = :mail")
    /***/
    Teacher findTeacherByMail(String mail);

    @Query("SELECT t FROM Teacher t WHERE t.field = :field")
    /***/
    List<Teacher> findTeachersByField(String field);
}
