package telran.spring.college;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.college.dto.PersonDto;
import telran.spring.college.entity.Student;
import telran.spring.college.service.CollegeService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollegeServiceTest {

	@Autowired
	CollegeService collegeService;

	@Test
	void test() {
		assertNotNull(collegeService);
	}

	@Test
	void addStudentTest() {
		PersonDto studentDto = new PersonDto(10L, "Vasya", "1994-10-10", null, null);
		Student student = Student.of(studentDto);
		assertEquals(studentDto, collegeService.addStudent(studentDto));
	}
	
	@Test 
	void addLecturer(){
		PersonDto lecturerDto = new PersonDto(123L, "Vasya", LocalDate.now().toString(), null, null);
		collegeService.addLecturer(lecturerDto);
		collegeService.addLecturer(lecturerDto);
	}

}
