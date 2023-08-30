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
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.spring.college.dto.IdName;
import telran.spring.college.dto.IdNameAvgMark;
import telran.spring.college.dto.PersonDto;
import telran.spring.college.dto.SubjectDto;
import telran.spring.college.service.CollegeService;
import telran.spring.exceptions.NotFoundException;
import telran.spring.college.entity.*;
import telran.spring.college.repository.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollegeUpdateTest {

	private static final String SUBJECT_ID = "J1";
	private static final Long LECTURER_ID = 421L;
	private static final int HOURS = 200;

	@Autowired
	CollegeService service;
	@Autowired
	SubjectRepository subjectRepo;
	@Autowired
	StudentRepository studentRepo;
	@Autowired
	LecturerRepository lecturerRepo;

	@Test
	@Order(1)
	@Sql(scripts = { "college-read-test-script.sql" })
	void updateHours() {
		service.updateHours(SUBJECT_ID, HOURS);
		assertThrowsExactly(NotFoundException.class, () -> service.updateHours(SUBJECT_ID + 10, HOURS));
	}

	@Test
	@Order(2)
	@Transactional(readOnly = true)
	void updateHoursTest() {
		Subject subject = subjectRepo.findById(SUBJECT_ID).get();
		assertEquals(HOURS, subject.getHours());
	}

	@Test
	@Order(3)
	@Sql(scripts = { "college-read-test-script.sql" })
	void updateLecturer() {
		service.updateLecturer(SUBJECT_ID, LECTURER_ID);
		assertThrowsExactly(NotFoundException.class, () -> service.updateLecturer(SUBJECT_ID + 10, LECTURER_ID));
		assertThrowsExactly(NotFoundException.class, () -> service.updateLecturer(SUBJECT_ID, LECTURER_ID + 10));
	}

	@Test
	@Order(4)
	@Transactional(readOnly = true)
	void updateLecturerTest() {
		Subject subject = subjectRepo.findById(SUBJECT_ID).get();
		assertEquals(LECTURER_ID, subject.getLecturer().getId());
	}

	@Test
	@Order(5)
	@Sql(scripts = { "college-read-test-script.sql" })
	@Disabled
	void removeStudentsNoMarks() {
		List<PersonDto> studentsNoMarks = service.removeStudentsNoMarks();
		assertEquals(1, studentsNoMarks.size());
		assertEquals(126, studentsNoMarks.get(0).getId());
	}

	@Test
	@Order(6)
	@Transactional(readOnly = true)
	@Disabled
	void removeStudentsNoMarksTest() {
		assertEquals(4, studentRepo.findAll().size());
		assertFalse(studentRepo.existsById(126L));
		assertEquals(2, lecturerRepo.findAll().size());
	}

	@Test
	@Order(7)
	@Sql(scripts = { "college-read-test-script.sql" })
	void removeStudentsLessMark() {
		List<PersonDto> studentsNoMarks = service.removeStudentsLessMarks(3);
		assertEquals(2, studentsNoMarks.size());
		assertEquals(125, studentsNoMarks.get(0).getId());
		assertEquals(126, studentsNoMarks.get(1).getId());
	}

	@Test
	@Order(8)
	@Transactional(readOnly = true)
	void removeStudentsLessMarkTest() {
		assertEquals(3, studentRepo.findAll().size());
		assertFalse(studentRepo.existsById(125L));
		assertFalse(studentRepo.existsById(126L));
		assertEquals(2, lecturerRepo.findAll().size());
	}
	
//	@Test
//	@Order(9)
//	void jpaTest() {
//		List<Student> students = service.findJPA();
//	}

}
