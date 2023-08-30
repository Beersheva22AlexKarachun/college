package telran.spring.college.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import telran.spring.college.dto.PersonDto;

@Entity
//@Table(name = "students")
@NoArgsConstructor
public class Student extends Person {

	private Student(PersonDto student) {
		super(student);
	}
	
	public static Student of(PersonDto student) {
		return new Student(student);
	}

	@OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
	List<Mark> marks;
}
