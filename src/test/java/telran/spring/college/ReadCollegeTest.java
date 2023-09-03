package telran.spring.college;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import lombok.extern.slf4j.Slf4j;
import telran.spring.college.dto.IdName;
import telran.spring.college.dto.IdNameAvgMark;
import telran.spring.college.dto.MarkDto;
import telran.spring.college.dto.StudentMark;
import telran.spring.college.dto.SubjectDto;
import telran.spring.college.dto.SubjectType;
import telran.spring.college.service.CollegeService;
import telran.spring.college.entity.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReadCollegeTest {

	@Autowired
	CollegeService service;
	
	@BeforeEach
	void setUp() {
		
	}

	@Test
	@Sql(scripts = { "college-read-test-script.sql" })
	void bestStudentsForLecturerTest() {
		int nStudents = 2;
		long lecturerId = 321;
		List<IdName> bestStudents = service.bestStudentsLecturer(lecturerId, nStudents);
		assertEquals(nStudents, bestStudents.size());
		
		assertEquals(127, bestStudents.get(0).getId());
		assertEquals(123, bestStudents.get(1).getId());
	}
	
	@Test
	@Sql(scripts = { "college-read-test-script.sql" })
	void getStudentsAvgMarksGreaterCollegeAvgTest() {
		int markThreshold90 = 1;
		int markThreshold70 = 2;
		List<IdName> bestStudents70 = service.studentsAvgMarksGreaterCollegeAvg(markThreshold70);
		List<IdName> bestStudents90 = service.studentsAvgMarksGreaterCollegeAvg(markThreshold90);
		bestStudents70.stream().forEach(student -> log.debug("{}-{}", student.getId(), student.getName()));
		bestStudents90.stream().forEach(student -> log.debug("{}-{}", student.getId(), student.getName()));
	}
	
	@Test
	@Sql(scripts = { "college-read-test-script.sql" })
	void getStudentsAvgMarksTest() {
		List<StudentMark> students = service.studentsAvgMarks();
		assertEquals(5, students.size());
		students.stream().forEach(student -> log.debug("{}-{}-{}", student.getId(), student.getName(), student.getMark()));
	}
	
	@Test
	@Sql(scripts = { "college-read-test-script.sql" })
	void findMarksByMethodNameTest() {
		int studentId = 123;
		String subjectId = "J1";
		List<MarkDto> marks = service.findMarksByStudentAndSubject(123, "J1");
		assertEquals(1, marks.size());
		assertEquals(studentId, marks.get(0).getStudentId());
		assertEquals(subjectId, marks.get(0).getSubjectId());
		assertEquals(80, marks.get(0).getMark());
	}
	
	@Test
	@Sql(scripts = { "college-read-test-script.sql" })
	void studentsBySubjectMarkTest() {
		int mark = 90;
		List<IdName> students = service.studentsBySubjectMark(SubjectType.BACK_END, mark);
		assertEquals(2, students.size());
	}
}
