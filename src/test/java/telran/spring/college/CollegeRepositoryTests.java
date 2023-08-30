package telran.spring.college;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.college.dto.PersonDto;
import telran.spring.college.entity.Lecturer;
import telran.spring.college.entity.Student;
import telran.spring.college.repository.LecturerRepository;
import telran.spring.college.repository.StudentRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollegeRepositoryTests {

	@Autowired
	LecturerRepository lecturerRepository;
	@Autowired
	StudentRepository studentRepository;
	
	PersonDto lecturerDto = new PersonDto(123L, "Vasya", LocalDate.now().toString(), null, null);
	Lecturer lecturer = Lecturer.of(lecturerDto);
	PersonDto studentDto = new PersonDto(124L, "Vasya", LocalDate.now().toString(), null, null);
	Student student = Student.of(studentDto);

	@Test
	void contextLoads() {
	}

	@Order(1)
	@Test
	void saveLecturerStudent() {
		lecturerRepository.save(lecturer);
		studentRepository.save(student);
	}

	@Order(2)
	@Test
	void findLecturer() {
		Lecturer foundLecturer = lecturerRepository.findById(lecturer.getId()).get();
		assertEquals(lecturer, foundLecturer);
		Student foundStudent = studentRepository.findById(student.getId()).get();
		assertEquals(student, foundStudent);
	}

	@Order(2)
	@Test
	void getAllStudents() {
		List objects = studentRepository.findAll();
		assertEquals(1, objects.size());
	}

}
