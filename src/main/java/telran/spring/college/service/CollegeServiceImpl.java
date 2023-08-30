package telran.spring.college.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.college.dto.*;
import telran.spring.college.entity.*;
import telran.spring.college.repository.*;
import telran.spring.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CollegeServiceImpl implements CollegeService {

	@Value("${app.person.id.min:100000}")
	long minId;
	@Value("${app.person.id.max:999999}")
	long maxId;

	private static final String SUBJECT_NOT_EXIST = "Subject with id '$s' doesn't exist.";
	private static final String LECTURER_NOT_EXIST = "Lecturer with id '%s' doesn't exist.";
	private static final String STUDENT_NOT_EXIST = "Student with id '%s' doesn't exist.";
	private static final String PERSON_ALREADY_EXIST = "%s with id '$s' already exists.";
	private static final String SUBJECT_ALREADY_EXIST = "Subject with id '$s' already exists.";

	private final StudentRepository studentRepo;
	private final LecturerRepository lecturerRepo;
	private final SubjectRepository subjectRepo;
	private final MarkRepository markRepo;

	@Override
	@Transactional(readOnly = false)
	public PersonDto addStudent(PersonDto studentDto) {
		return addPerson(studentDto, Student.class, studentRepo);
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto addLecturer(PersonDto lecturerDto) {
		return addPerson(lecturerDto, Lecturer.class, lecturerRepo);
	}

	private <T> long getUniqueId(JpaRepository<T, Long> repo) {
		Random rd = new Random();
		long id;
		do {
			id = rd.nextLong(minId, maxId + 1);
		} while (repo.existsById(id));
		return id;
	}

	@SuppressWarnings("unused")
	private <T> PersonDto addPerson(PersonDto dto, Class<T> personClazz, JpaRepository<T, Long> repo) {
		if (dto.getId() == null) {
			dto.setId(getUniqueId(repo));
		}
		if (repo.existsById(dto.getId())) {
			throw new IllegalStateException(
					String.format(PERSON_ALREADY_EXIST, personClazz.getSimpleName(), dto.getId()));
		}

		Person person = null;
		try {
			person = (Person) personClazz.getDeclaredMethod("of", PersonDto.class).invoke(null, dto);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
		Person savedPerson = (Person) repo.save(personClazz.cast(person));
		log.debug("Person '{}' has beed added", savedPerson);
		return savedPerson.build();
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto addSubject(SubjectDto subjectDto) {
		if (subjectRepo.existsById(subjectDto.getId())) {
			throw new IllegalStateException(String.format(SUBJECT_ALREADY_EXIST, subjectDto.getId()));
		}
		Lecturer lecturer = null;
		Long lecturerId = subjectDto.getLecturerId();
		if (lecturerId != null) {
			lecturer = lecturerRepo.findById(lecturerId)
					.orElseThrow(() -> new NotFoundException(String.format(LECTURER_NOT_EXIST, lecturerId)));
		}
		Subject subject = Subject.of(subjectDto);
		subject.setLecturer(lecturer);
		log.debug("Subject '{}' has beed added", subject);
		return subjectRepo.save(subject).build();
	}

	@Override
	@Transactional(readOnly = false)
	public MarkDto addMark(MarkDto markDto) {
		Student student = studentRepo.findById(markDto.getStudentId())
				.orElseThrow(() -> new NotFoundException(String.format(STUDENT_NOT_EXIST, markDto.getStudentId())));
		Subject subject = subjectRepo.findById(markDto.getSubjectId())
				.orElseThrow(() -> new NotFoundException(String.format(SUBJECT_NOT_EXIST, markDto.getSubjectId())));
		Mark mark = new Mark(student, subject, markDto.getMark());
		log.debug("Mark '{}' has beed added", mark);
		return markRepo.save(mark).build();
	}

	@Override
	public List<IdName> getBestStudentForLecturer(long lecturerId, int nStudents) {
		return studentRepo.getBestStudentsForLecturer(lecturerId, nStudents);
	}

	@Override
	public List<IdName> getStudentsAvgMarksGreaterCollegeAvg(int nMarksThreshold) {
		return studentRepo.getStudentsAvgMarksGreaterCollegeAvg(nMarksThreshold);
	}

	@Override
	public List<IdNameAvgMark> getStudentsAvgMarks() {
		return studentRepo.getStudentsAvgMarks();
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto updateHours(String subjectId, int hours) {
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new NotFoundException(String.format(SUBJECT_NOT_EXIST, subjectId)));
		subject.setHours(hours);
		return subject.build();
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto updateLecturer(String subjectId, Long lecturerId) {
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new NotFoundException(String.format(SUBJECT_NOT_EXIST, subjectId)));
		Lecturer lecturer = null;
		if (lecturerId != null) {
			lecturer = lecturerRepo.findById(lecturerId)
					.orElseThrow(() -> new NotFoundException(String.format(LECTURER_NOT_EXIST, lecturerId)));
		}
		subject.setLecturer(lecturer);
		return subject.build();
	}

	@Override
	@Transactional(readOnly = false)
	public List<PersonDto> removeStudentsNoMarks() {
		return removeStudentsLessMarks(1);
	}

	@Override
	@Transactional(readOnly = false)
	public List<PersonDto> removeStudentsLessMarks(int nMarks) {
		List<Student> removedStudents = studentRepo.findStudentsLessMark(nMarks);
		removedStudents.forEach(student -> {
			studentRepo.delete(student);
			log.debug("Student {} is going to be deleted", student);
		});

		return removedStudents.stream().map(Student::build).toList();
	}

}
