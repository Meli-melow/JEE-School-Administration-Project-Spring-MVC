package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.AssessmentRepository;
import repositories.ResultRepository;
import repositories.StudentRepository;

@Service
public class ResultService {

    // All autowired components will be injected to the ResultService during the app configuration

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public ResultService(ResultRepository resultRepository, StudentRepository studentRepository,
    AssessmentRepository assessmentRepository) {
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
        this.assessmentRepository = assessmentRepository;
    }
}
