package telran.spring.college.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import telran.spring.college.dto.PersonDto;

@Entity
//@Table(name = "lecturers")
@NoArgsConstructor
public class Lecturer extends Person {

	private Lecturer(PersonDto lecturer) {
		super(lecturer);
	}

	public static Lecturer of(PersonDto lecturer) {
		return new Lecturer(lecturer);
	}

}
