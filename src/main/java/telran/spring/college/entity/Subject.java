package telran.spring.college.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.spring.college.dto.SubjectDto;
import telran.spring.college.dto.SubjectType;

@Entity
@Table(name = "subjects")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Subject {

	@Id
	String id;
	String name;
	int hours;
	@Enumerated(EnumType.STRING)
	SubjectType type;
	@ManyToOne
	@JoinColumn(name = "lecturer_id", nullable = true)
	Lecturer lecturer;

	public SubjectDto build() {
		return new SubjectDto(id, name, hours, type, lecturer != null ? lecturer.getId() : null);
	}

	public static Subject of(SubjectDto subject) {
		return new Subject(subject.getId(), subject.getName(), subject.getHours(), subject.getType(), null);
	}
}
