package telran.spring.college.entity;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.spring.college.dto.MarkDto;

@Entity
@Data
@NoArgsConstructor
@Table(name = "marks", indexes = { @Index(columnList = "student_id") })
public class Mark {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@ManyToOne
	@JoinColumn(nullable = false, name = "student_id")
	Student student;

	@ManyToOne
	@JoinColumn(nullable = false, name = "subject_id")
	Subject subject;

	@Range(min = 60, max = 100)
	@Column(nullable = false)
	int mark;

	public Mark(Student student, Subject subject, @Range(min = 60, max = 100) int mark) {
		this.student = student;
		this.subject = subject;
		this.mark = mark;
	}

	public MarkDto build() {
		return new MarkDto(id, student.getId(), subject.getId(), mark);
	}

}
