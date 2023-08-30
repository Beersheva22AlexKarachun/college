package telran.spring.college.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

	@OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE) // For hibernate
	@OnDelete(action = OnDeleteAction.CASCADE) // For DB
	List<Mark> marks;
}
