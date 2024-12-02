package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.AssessmentRepository;
import repositories.ResultRepository;

@Service
public class AssessmentService {

    // All autowired components will be injected to the AssessmentService during the app configuration

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ResultRepository resultRepository;

    public AssessmentService(AssessmentRepository assessmentRepository, ResultRepository resultRepository) {
        this.assessmentRepository = assessmentRepository;
        this.resultRepository = resultRepository;
    }
}
