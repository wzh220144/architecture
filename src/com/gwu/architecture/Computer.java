package com.gwu.architecture;

public class Computer {

	Cpu gwuCpu;
	IO gwuIO;
	Memory gwuMemory;

	public Computer() {
		gwuCpu = new Cpu();
		gwuIO = new IO();
		gwuMemory = new Memory();
	}

	public void init(GUI gwuGui) {

		gwuCpu.init();
		gwuIO.init(gwuGui);
		gwuMemory.init();
		// System.out.println(gwuCpu.IR.value);

	}

	public void debug() {

	}

	public void powerup() { // start up the computer
		gwuCpu.powerup();
		gwuIO.powerup();
		gwuMemory.powerup();
		fetchInstruction();
	}

	public void powerdown() { // shutdown the computer
		gwuCpu.powerdown();
		gwuIO.powerdown();
		gwuMemory.powerdown();
	}

	public void destory() {
		gwuCpu.destroy();
		gwuIO.destroy();
		gwuMemory.destroy();
	}

	/*********************************** computer operation **********************************************************/

	public void fetchInstruction() { // get instruction
		this.gwuCpu.fetchInstruction(this.gwuMemory);
	}

	public void decode() { // decode instruction
		this.gwuCpu.decode();
	}

	public void effAddress() { // compute effective address
		this.gwuCpu.effAddress(gwuMemory);
	}

	public void execute() { // do execute
		this.gwuCpu.execute(gwuMemory, gwuIO);
	}

	public void storeResult() { // store result
		this.gwuCpu.storeResult(gwuMemory, gwuIO);
	}

	public void nextInstruction() { // get next instruction address
		this.gwuCpu.nextInstruction();
	}

	/*********************************** main operation **********************************************************/
	/*
	 * routine for Main
	 */
	public void step() {
		decode();
		effAddress();
		execute();
		storeResult();
		nextInstruction();
		fetchInstruction();
	}

}
