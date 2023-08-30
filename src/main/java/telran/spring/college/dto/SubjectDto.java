package telran.spring.college.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SubjectDto {
	String id;
	String name;
	int hours;
	SubjectType type;
	long lecturerId;
}
