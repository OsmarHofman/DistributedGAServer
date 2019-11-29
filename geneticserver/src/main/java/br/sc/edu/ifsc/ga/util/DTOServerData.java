package br.sc.edu.ifsc.ga.util;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.List;
import java.util.Objects;

import br.sc.edu.ifsc.ga.domain.Chromosome;
import br.sc.edu.ifsc.ga.domain.Classes;
import br.sc.edu.ifsc.ga.domain.Lesson;
import br.sc.edu.ifsc.ga.domain.Subject;
import br.sc.edu.ifsc.ga.domain.Teacher;

public class DTOServerData implements Serializable, Remote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Chromosome> chromosomes;
	private List<Classes> classes;
	private List<Lesson> lessons;
	private List<Subject> subjects;
	private List<Teacher> teachers;

	public DTOServerData() {
	}

	public DTOServerData(List<Chromosome> chromosomes, List<Classes> classes, List<Lesson> lessons,
			List<Subject> subjects, List<Teacher> teachers) {
		this.chromosomes = chromosomes;
		this.classes = classes;
		this.lessons = lessons;
		this.subjects = subjects;
		this.teachers = teachers;
	}

	public List<Chromosome> getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(List<Chromosome> chromosomes) {
		this.chromosomes = chromosomes;
	}

	public List<Classes> getClasses() {
		return classes;
	}

	public void setClasses(List<Classes> classes) {
		this.classes = classes;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public Teacher getTeacherById(int id) {
		for (Teacher teacher : this.teachers) {
			if (teacher.getId() == id) {
				return teacher;
			}
		}
		return null;
	}

	public Lesson getLessonById(int id) {
		for (Lesson lesson : this.lessons) {
			if (lesson.getId() == id) {
				return lesson;
			}
		}
		return null;
	}

	public int getTeacherBySubject(int id) {
		for (Lesson lesson : lessons) {
			if (lesson.getSubjectId() == id) {
				return lesson.getTeacherId();
			}
		}
		return -1;
	}

	public int getInnerTeacher(Chromosome chromosome, int index, int position) {
		return chromosome.getClassrooms()[index].getClassroomSubjects()[position];
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DTOServerData that = (DTOServerData) o;
		return Objects.equals(chromosomes, that.chromosomes) && Objects.equals(classes, that.classes)
				&& Objects.equals(lessons, that.lessons) && Objects.equals(subjects, that.subjects)
				&& Objects.equals(teachers, that.teachers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(chromosomes, classes, lessons, subjects, teachers);
	}

}
