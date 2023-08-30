package telran.spring.college.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.spring.college.dto.MarkDto;
import telran.spring.college.dto.PersonDto;
import telran.spring.college.dto.SubjectDto;
import telran.spring.college.service.CollegeService;

@RestController
@RequestMapping("college")
@RequiredArgsConstructor
public class CollegeController {

	final private CollegeService collegeService;

	@PostMapping("/students")
	public PersonDto addStudent(@RequestBody PersonDto student) {
		return collegeService.addStudent(student);
	}

	@PostMapping("/lecturers")
	public PersonDto addLecturer(@RequestBody PersonDto lecturer) {
		return collegeService.addLecturer(lecturer);
	}

	@PostMapping("/subjects")
	public SubjectDto addSubject(@RequestBody SubjectDto subject) {
		return collegeService.addSubject(subject);
	}

	@PostMapping("/marks")
	public MarkDto addMark(@RequestBody MarkDto mark) {
		return collegeService.addMark(mark);
	}

}
