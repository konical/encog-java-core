package org.encog.ml.prg.train;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;

public class ThreadedGenomeSelector implements Serializable {
	private EvolutionaryAlgorithm owner;
	private Set<Genome> used = new HashSet<Genome>();

	public ThreadedGenomeSelector(EvolutionaryAlgorithm theOwner) {
		this.owner = theOwner;
	}

	public Genome selectGenome() {
		Genome result = null;

		synchronized (this) {
			while (result == null) {
				int selectedID = owner.getSelection().performSelection();
				Genome potentialSelection = (Genome)owner.getPopulation().get(selectedID);
				if (!used.contains(potentialSelection)) {
					used.add(potentialSelection);
					result = potentialSelection;
				}
			}
			return result;
		}
	}
	
	public Genome antiSelectGenome() {
		Genome result = null;

		synchronized (this) {
			while (result == null) {
				int selectedID = owner.getSelection().performAntiSelection();
				Genome potentialSelection = (Genome)owner.getPopulation().get(selectedID);
				if (!used.contains(potentialSelection)) {
					used.add(potentialSelection);
					result = potentialSelection;
				}
			}
			return result;
		}
	}
	
	public void releaseGenome(Genome genome) {
		synchronized(this) {
			this.used.remove(genome);
		}
	}
	
	public void clear() {
		synchronized(this) {
			this.used.clear();
		}
	}
}
