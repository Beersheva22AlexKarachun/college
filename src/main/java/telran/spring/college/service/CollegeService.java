package telran.spring.college.service;

import java.util.List;

import telran.spring.college.dto.*;
import telran.spring.college.entity.Student;

public interface CollegeService {

	PersonDto addStudent(PersonDto student);

	PersonDto addLecturer(PersonDto lecturer);

	SubjectDto addSubject(SubjectDto subject);

	MarkDto addMark(MarkDto mark);

	List<IdName> bestStudentsLecturer(long lecturerId, int nStudents);

	List<IdName> studentsAvgMarksGreaterCollegeAvg(int nMarksThreshold);

	List<StudentMark> studentsAvgMarks();

	SubjectDto updateHours(String subjectId, int hours);

	SubjectDto updateLecturer(String subjectId, Long lecturerId);

	List<PersonDto> removeStudentsNoMarks();

	List<PersonDto> removeStudentsLessMarks(int nMarks);

	List<MarkDto> findMarksByStudentAndSubject(long studentId, String subjectId);

	List<IdName> studentsBySubjectMark(SubjectType type, int mark);

	PersonDto removeLecturer(Long lecturerId);

	List<String> jpqlQuery(QueryDto queryStr);
}
