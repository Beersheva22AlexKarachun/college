package telran.spring.college.service;

import java.util.List;

import telran.spring.college.dto.*;
import telran.spring.college.entity.Student;

public interface CollegeService {

	PersonDto addStudent(PersonDto student);
	PersonDto addLecturer(PersonDto lecturer);
	SubjectDto addSubject(SubjectDto subject);
	MarkDto addMark(MarkDto mark);
	List<IdName> getBestStudentForLecturer(long lecturerId, int nStudents);
	List<IdName> getStudentsAvgMarksGreaterCollegeAvg(int nMarksThreshold);
	List<IdNameAvgMark> getStudentsAvgMarks();
	SubjectDto updateHours(String subjectId, int hours);
	SubjectDto updateLecturer(String subjectId, Long lecturerId);
	List<PersonDto> removeStudentsNoMarks();
	List<PersonDto> removeStudentsLessMarks(int nMarks);
}
