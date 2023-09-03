package telran.spring.college.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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

	private final EntityManager entityManager;
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
			lecturer = getLecturerById(lecturerId);
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
	public List<IdName> bestStudentsLecturer(long lecturerId, int nStudents) {
		return studentRepo.getBestStudentsForLecturer(lecturerId, nStudents);
	}

	@Override
	public List<IdName> studentsAvgMarksGreaterCollegeAvg(int nMarksThreshold) {
		return studentRepo.getStudentsAvgMarksGreaterCollegeAvg(nMarksThreshold);
	}

	@Override
	public List<StudentMark> studentsAvgMarks() {
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
			lecturer = getLecturerById(lecturerId);
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
		List<Student> removedStudents = studentRepo.findStudentsLessMarkJpql(nMarks);
//		removedStudents.forEach(studentRepo::delete);
		studentRepo.removeStudentsLessMarkJpql(nMarks);
		return removedStudents.stream().map(Student::build).toList();
	}

	@Override
	public List<MarkDto> findMarksByStudentAndSubject(long studentId, String subjectId) {
		List<Mark> marks = markRepo.findByStudentIdAndSubjectId(studentId, subjectId);
		return marks.stream().map(Mark::build).toList();
	}

	@Override
	public List<IdName> studentsBySubjectMark(SubjectType type, int mark) {
		return studentRepo.findDistinctByMarksSubjectTypeAndMarksMarkGreaterThanOrderById(type, mark);
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto removeLecturer(Long lecturerId) {
		Lecturer lecturer = getLecturerById(lecturerId);
		lecturerRepo.delete(lecturer);
		return lecturer.build();
	}

	private Lecturer getLecturerById(Long lecturerId) {
		return lecturerRepo.findById(lecturerId)
				.orElseThrow(() -> new NotFoundException(String.format(LECTURER_NOT_EXIST, lecturerId)));
	}

	@Override
	public List<String> jpqlQuery(QueryDto queryDto) {
		Query query = entityManager.createQuery(queryDto.query());
		Integer limit = queryDto.limit();
		if (limit != null && limit > 0) {
			query.setMaxResults(limit);

		}
		List<?> resultList = query.getResultList();
		List<String> result = Collections.emptyList();
		if (!resultList.isEmpty()) {
			result = resultList.get(0).getClass().isArray() ? processMultiProjectionQuery((List<Object[]>) resultList)
					: processSingleProjectionQuery((List<Object>) resultList);
		}
		return result;
	}

	private List<String> processSingleProjectionQuery(List<Object> resultList) {
		return resultList.stream().map(Object::toString).toList();
	}

	private List<String> processMultiProjectionQuery(List<Object[]> resultList) {
		return resultList.stream().map(Arrays::deepToString).toList();
	}

}
