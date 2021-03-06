package org.encog.ml.prg.extension;

import java.util.Random;

import org.encog.ml.prg.EncogProgram;
import org.encog.ml.prg.epl.OpCodeHeader;

public interface ProgramExtensionTemplate {
	String getName();
	int getInstructionSize(OpCodeHeader header);
	int getChildNodeCount();
	void evaluate(EncogProgram prg);
	short getOpcode();
	boolean isVariableValue();
	boolean isOperator();
	void randomize(Random r, EncogProgram program, double degree);
}
