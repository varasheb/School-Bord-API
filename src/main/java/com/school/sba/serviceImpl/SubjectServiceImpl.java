package com.school.sba.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.requestDTO.SubjectRequest;
import com.school.sba.responseDTO.SubjectResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;
@Service
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	@Autowired Subject subject;
	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> addSubjectToAcademicProgram(int programId,
			List<SubjectRequest> subjectRequests) {
		ResponseStructure<List<SubjectResponse>> responseStructure = new ResponseStructure<List<SubjectResponse>>();
		List<SubjectResponse> rsLists = new ArrayList<SubjectResponse>();
		AcademicProgram academicProgram = academicProgramRepository.findById(programId).get();
		academicProgram.getSubjects().clear();;
		for(SubjectRequest subjectRequest : subjectRequests ) {
			subjectRequest.setSubjectName(subjectRequest.getSubjectName().toLowerCase());
				List<Subject> lists = subjectRepository.findAll();
				boolean condition=true;
				for(Subject subject : lists) {
					if(subject.getSubjectName().contains(subjectRequest.getSubjectName()))
						condition=false;
				}
				if(condition){
					subject = subjectRepository.save(mapToSubject(subjectRequest,academicProgram));
					academicProgram.getSubjects().add(subject);
					academicProgramRepository.save(academicProgram);
					rsLists.add(mapToResponse(subject));
				}else {
					subject = subjectRepository.findBySubjectName(subjectRequest.getSubjectName());
					List<Subject> subjects = academicProgram.getSubjects();
					boolean boo = true;
					for(Subject sub : subjects) {
						if(sub.getSubjectName().toString().contains(subjectRequest.getSubjectName()))
							boo = false;
					}
					if(boo) {
						academicProgram.getSubjects().add(subjectRepository.findBySubjectName(subjectRequest.getSubjectName()));
						academicProgramRepository.save(academicProgram);
						rsLists.add(mapToResponse(subject));
					}
				}
		}
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Subject assigned to AcademicProgram");
		responseStructure.setData(rsLists);
		return new ResponseEntity<ResponseStructure<List<SubjectResponse>>>(responseStructure, HttpStatus. CREATED);
	}
	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> fetchAllSubjects() {
		List<Subject> lists = subjectRepository.findAll();
		List<SubjectResponse> subjects = new ArrayList<SubjectResponse>();
		ResponseStructure<List<SubjectResponse>> responseStructure = new ResponseStructure<List<SubjectResponse>>();
		for(Subject list : lists)
			subjects.add(mapToResponse(list));
		responseStructure.setStatus(HttpStatus.FOUND.value());
		responseStructure.setMessage("All Subject Fetched");
		responseStructure.setData(subjects);
		return new ResponseEntity<ResponseStructure<List<SubjectResponse>>>(responseStructure, HttpStatus.FOUND);
	}
	private SubjectResponse mapToResponse(Subject subject) {
		return SubjectResponse.builder()
				.subjectId(subject.getSubjectId())
				.subjectName(subject.getSubjectName())
				.build();
	}
	private Subject mapToSubject(SubjectRequest subjectRequest, AcademicProgram academicProgram) {
		return Subject.builder()
				.subjectName(subjectRequest.getSubjectName())
				.academyProgram(academicProgramRepository.findAll())
				.build();
	}
}
