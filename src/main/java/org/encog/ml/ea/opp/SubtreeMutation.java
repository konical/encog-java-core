package org.encog.ml.ea.opp;

import java.io.Serializable;
import java.util.Random;

import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.ml.prg.EncogProgram;
import org.encog.ml.prg.EncogProgramContext;
import org.encog.ml.prg.generator.PrgGrowGenerator;
import org.encog.ml.prg.generator.PrgPopulationGenerator;

public class SubtreeMutation implements EvolutionaryOperator, Serializable {

	private final PrgPopulationGenerator generator;
	
	public SubtreeMutation(EncogProgramContext theContext, int theMaxDepth) {
		this.generator = new PrgGrowGenerator(theContext, null, theMaxDepth);
	}

	@Override
	public void performOperation(Random rnd, Genome[] parents, int parentIndex,
			Genome[] offspring, int offspringIndex) {
		EncogProgram program = (EncogProgram)parents[parentIndex];
		EncogProgram result = (EncogProgram)offspring[offspringIndex];
		result.clear();
		
		// find the mutation point, this is simply a node position based on the
		// node count, it does not take int account node-sizes. Also, because this
		// is RPN, the mutation point is the end of the mutation.
		int programSize = program.size();
		int mutationPosition = rnd.nextInt(programSize);
		
		// now find the actual frame index of the end of the mutation
		int mutationIndex = program.findFrame(mutationPosition);
		
		int mutationStart = program.findNodeStart(mutationIndex);
		int mutationSize = (program.nextIndex(mutationIndex) - mutationStart);
		int mutationEnd = mutationStart+mutationSize;
		
		// copy left of the mutation point
		result.copy(program, 0, 0, mutationStart);
		result.setProgramLength(mutationStart);
		result.setProgramCounter(mutationStart);
		
		// handle mutation point
		this.generator.createNode(rnd, result, 0, this.generator.getMaxDepth());
		
		// copy right of the mutation point
		int rightSize = program.getProgramLength()-mutationStart-mutationSize;
		int t = result.getProgramLength();
		result.setProgramLength(result.getProgramLength()+rightSize);
		result.copy(program, mutationEnd, t, rightSize);			
		
		result.size();
		
	}

	@Override
	public int offspringProduced() {
		return 1;
	}

	@Override
	public int parentsNeeded() {
		return 2;
	}

	@Override
	public void init(EvolutionaryAlgorithm theOwner) {
		// TODO Auto-generated method stub
		
	}
}
