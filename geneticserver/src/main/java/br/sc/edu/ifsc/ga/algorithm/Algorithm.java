package br.sc.edu.ifsc.ga.algorithm;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import br.sc.edu.ifsc.ga.domain.Chromosome;
import br.sc.edu.ifsc.ga.domain.Classroom;
import br.sc.edu.ifsc.ga.domain.Lesson;
import br.sc.edu.ifsc.ga.domain.Teacher;
import br.sc.edu.ifsc.ga.interfaces.IRating;
import br.sc.edu.ifsc.ga.util.DTORating;
import br.sc.edu.ifsc.ga.util.DTOServerData;

public class Algorithm extends UnicastRemoteObject implements IRating {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Algorithm() throws RemoteException {
	}

	@Override
	public List<DTORating> rate(DTOServerData serverData) {

		// TODO realizar teste distribuido
//        Classes classes = serverData.getClasses().get(0);
        System.out.println("Processando...");
		List<DTORating> dtoRatings = new ArrayList<>();
		int count = 0;
		for (Chromosome chromosome : serverData.getChromosomes()) {

			int avaliation = 0;
			// Demérito: Professor Indisponível -5
			avaliation = teacherIsUnavailable(chromosome, avaliation, serverData);

			// Demérito: Choque de horario Professor -10
			avaliation = scheduleConflict(chromosome, avaliation, serverData);

			// Demérito: Turma sem alguma disciplina -30
			avaliation = subjectlessClass(chromosome, avaliation, serverData);
			
			if(chromosome.getAvaliation() == 0) {
				chromosome.setAvaliation(5000);
			}
			chromosome.setAvaliation(chromosome.getAvaliation() - avaliation);

			dtoRatings.add(new DTORating(chromosome, count));
			count++;
		}
		System.out.println("Finalizando....");

		return dtoRatings;
	}

	private int scheduleConflict(Chromosome chromosome, int avaliation, DTOServerData serverData) {
		for (int i = 0; i < chromosome.getClassrooms().length; i++) {
			for (int j = 0; j < chromosome.getClassrooms()[i].getClassroomSubjects().length; j++) {
				int idTeacher = serverData.getTeacherBySubject(j);
				int limitInf;
				int limitSup;
				if (i < 30) {
					limitInf = 0;
					limitSup = 30;
				} else if (i < 39) {
					limitInf = 30;
					limitSup = 39;
				} else {
					limitInf = 39;
					limitSup = 40;
				}
				for (int k = limitInf; k < limitSup; k++) {
					if (k != i) {
						int currentIdTeacher = serverData.getInnerTeacher(chromosome, k, j);
						if (currentIdTeacher == idTeacher) {
							avaliation += 10;
						}
					}
				}
			}
		}

		return avaliation;
	}

	// TODO Alterar para a nova modelagem
	private int subjectlessClass(Chromosome chromosome, int avaliation, DTOServerData serverData) {
		int currentClassId = serverData.getLessons().get(0).getClassesId();
		// iterando pelas disciplinas
		for (int i = 0; i < serverData.getClasses().size(); i++) {
			Lesson lesson = serverData.getLessons().get(i);
			int idClass = lesson.getClassesId();
			int countWhile = 0;
			while (idClass == currentClassId) {
				int idSubject = lesson.getSubjectId();
				int qtdPerWeek = lesson.getPeriodsPerWeek();
				int[] classroomSubjects = chromosome.getClassrooms()[i].getClassroomSubjects();
				for (int classroomSubject : classroomSubjects) {
					if (classroomSubject == idSubject) {
						qtdPerWeek--;
					}
				}

				if (qtdPerWeek > 0) {
					avaliation += 30 * qtdPerWeek;
				}
				currentClassId = serverData.getLessons().get(countWhile + 1).getClassesId();
				countWhile++;
			}
		}
		return avaliation;
	}

	private int teacherIsUnavailable(Chromosome chromosome, int avaliation, DTOServerData serverData) {
		Classroom[] actualClassroom = chromosome.getClassrooms();
		for (int i = 0; i < actualClassroom.length; i++) {
			int[] classRoomLessons = actualClassroom[i].getClassroomSubjects();
			for (int j = 0; j < classRoomLessons.length; j++) {
				int teacherId = serverData.getLessons().get(classRoomLessons[j] - 1).getTeacherId();
				Teacher teacher = serverData.getTeacherById(teacherId);
				if (isWorkingInTimeoff(teacher, i, j)) {
					avaliation += 5;
				}
			}

		}
		return avaliation;
	}

	private boolean isWorkingInTimeoff(Teacher teacher, int shiftIndex, int chromosomeIndex) {
		String daysRaw = teacher.getTimeoff().replace(".", "").replace(",", "").substring(0, 60);
		String days;
		if (shiftIndex < 30) {
			days = timeoffShift(daysRaw, 0);
		} else if (shiftIndex < 39) {
			days = timeoffShift(daysRaw, 1);
		} else {
			days = timeoffShift(daysRaw, 2);
		}
		if (isThereTimeOff(days)) {
			return days.charAt(chromosomeIndex) == '0';
		}
		return false;
	}

	// 0 é manha, 1 tarde e 2 a noite
	private String timeoffShift(String rawTimeoff, int shift) {
		int limit;
		if (shift == 0) {
			limit = 0;
		} else if (shift == 1) {
			limit = 4;
		} else {
			limit = 8;
		}

		String newShift = "";
		for (int i = 0; i < 5; i++) {
			newShift += rawTimeoff.substring(limit, limit + 4);
			limit += 12;
		}
		return newShift;
	}

	private boolean isThereTimeOff(String days) {
		for (int i = 0; i < days.length(); i++) {
			if (days.charAt(i) == '0') {
				return true;
			}
		}
		return false;
	}

}
