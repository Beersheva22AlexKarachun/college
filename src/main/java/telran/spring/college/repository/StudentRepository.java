package telran.spring.college.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.spring.college.dto.IdName;
import telran.spring.college.dto.IdNameAvgMark;
import telran.spring.college.entity.Mark;
import telran.spring.college.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query(value = "select sl.id as id, sl.name as name from "
			+ "(select * from students_lecturers where dtype='Student') as sl " 
			+ "join marks on sl.id=student_id "
			+ "join subjects as sbj on subject_id=sbj.id where lecturer_id=:lecturerId "
			+ "group by sl.id order by avg(mark) desc limit :nStudents", 
			nativeQuery = true)
	List<IdName> getBestStudentsForLecturer(long lecturerId, int nStudents);
	
	@Query(value = "select ", nativeQuery = true)
	List<IdName> getStudentsAvgMarksGreaterCollegeAvg(int nMarksThreshold);
	
	@Query(value = "select sl.id as id, sl.name as name, round(avg(mark), 2) as avgMark from "
			+ "(select * from students_lecturers where dtype='Student') as sl " 
			+ "left join marks on sl.id=student_id group by sl.id order by avg(mark) desc", 
			nativeQuery = true)
	List<IdNameAvgMark> getStudentsAvgMarks();

	@Query(value = "select * from students_lecturers where dtype='Student' and id in "
			+ "(select sl.id from students_lecturers as sl left join marks on sl.id=student_id "
			+ "group by sl.id having count(mark) < :nMarks)", nativeQuery = true)
	List<Student> findStudentsLessMark(int nMarks);
	
}
