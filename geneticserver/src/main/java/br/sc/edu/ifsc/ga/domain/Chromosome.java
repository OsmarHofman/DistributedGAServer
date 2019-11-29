package br.sc.edu.ifsc.ga.domain;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import br.sc.edu.ifsc.ga.util.DTORating;

public class Chromosome implements Serializable, Remote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Classroom[] classrooms = new Classroom[40];
	private int avaliation;

	public Chromosome() {
		// FIXME valor 8000 apenas para teste
		avaliation = 8000;

	}

	public Chromosome generateRandom() {
		Random random = new Random();
		for (int i = 0; i < classrooms.length; i++) {
			classrooms[i] = new Classroom();
			for (int j = 0; j < classrooms[0].getClassroomSubjects().length; j++) {
				if (i < 30) {
					classrooms[i].getClassroomSubjects()[j] = random.nextInt(30) + 1;
				} else if (i < 39) {
					classrooms[i].getClassroomSubjects()[j] = random.nextInt(9) + 31;
				} else {
					classrooms[i].getClassroomSubjects()[j] = 40;
				}
			}

		}
		return this;
	}

	public Classroom[] getClassrooms() {
		return classrooms;
	}

	public void setClassrooms(Classroom[] classrooms) {
		this.classrooms = classrooms;
	}

	public int getAvaliation() {
		return avaliation;
	}

	public void setAvaliation(int avaliation) {
		this.avaliation = avaliation;
	}

	public void setGene(Classroom value, int position) {
		this.classrooms[position] = value;
	}

	public Classroom getGene(int position) {
		return this.classrooms[position];
	}

	public void setParentToChild(Chromosome self, Chromosome parent) {
		self.setAvaliation(0);
		self.setClassrooms(parent.getClassrooms());
	}

	public static DTORating getBestAvaliation(Chromosome[] chromosomes) {
		DTORating rating = new DTORating();
		int avaliation = 0;
		for (int i = 0; i < chromosomes.length; i++) {
			if (chromosomes[i].getAvaliation() > avaliation) {
				rating.setId(i);
				rating.setChromosome(chromosomes[i]);
				rating.getChromosome().setAvaliation(chromosomes[i].getAvaliation());
				avaliation = chromosomes[i].getAvaliation();
			}
		}
		return rating;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Chromosome that = (Chromosome) o;
		return avaliation == that.avaliation && Arrays.equals(classrooms, that.classrooms);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(avaliation);
		result = 31 * result + Arrays.hashCode(classrooms);
		return result;
	}

	@Override
	public String toString() {
		return "Chromosome{" + "chromosome=" + Arrays.toString(classrooms) + ", avaliation=" + avaliation + '}';
	}
}
