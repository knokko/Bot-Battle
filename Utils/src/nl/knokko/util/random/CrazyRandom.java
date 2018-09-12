package nl.knokko.util.random;

import java.util.Random;

import nl.knokko.util.bits.BitHelper;

public class CrazyRandom extends Random {

	private static final long serialVersionUID = -259495590384702071L;
	
	private static int readInt(boolean[] bools, int index){
		return BitHelper.makeInt(BitHelper.byteFromBinary(bools, index), BitHelper.byteFromBinary(bools, index + 8), BitHelper.byteFromBinary(bools, index + 16), BitHelper.byteFromBinary(bools, index + 24));
	}
	
	private static void writeInt(int value, boolean[] bools, int index){
		BitHelper.byteToBinary(BitHelper.int2(value), bools, index);
		BitHelper.byteToBinary(BitHelper.int1(value), bools, index + 8);
		BitHelper.byteToBinary(BitHelper.int3(value), bools, index + 16);
		BitHelper.byteToBinary(BitHelper.int0(value), bools, index + 24);
	}
	
	/**
	 * Length must be 32000
	 */
	private final boolean[] state;
	
	private int index;

	private CrazyRandom(boolean[] data, int startIndex){
		state = data;
		index = startIndex;
	}
	
	public void superMix(){
		Random[] first = new Random[5];
		first[0] = new Random(System.nanoTime());
		first[1] = new Random(System.identityHashCode(this));
		first[2] = new Random(System.currentTimeMillis());
		first[3] = new Random(System.identityHashCode(System.out));
		first[4] = new Random(readInt(state, index));
		Random[] second = new Random[100];
		int randomIndex = first.length / 2;
		for(int index = 0; index < second.length; index++){
			second[index] = new Random(first[randomIndex].nextLong());
			randomIndex = first[randomIndex].nextInt(first.length);
		}
		for(int count = 0; count < 10000; count++){
			add(second[randomIndex].nextInt(), second[randomIndex].nextInt(state.length - 32));
			randomIndex = second[randomIndex].nextInt(second.length);
		}
	}
	
	public void strongMix(){
		Random first = new Random(readInt(state, index));
		Random time = new Random(System.nanoTime());
		boolean fr = false;
		Random[] subs = new Random[28 + first.nextInt(7) + time.nextInt(6)];
		for(int index = 0; index < subs.length; index++){
			subs[index] = new Random(fr ? first.nextLong() : time.nextLong());
			fr = subs[index].nextBoolean();
		}
		int randomIndex = time.nextInt(subs.length);
		for(int count = 0; count < 1000; count++){
			add(subs[randomIndex].nextInt(), subs[randomIndex].nextInt(state.length - 32));
			randomIndex = subs[randomIndex].nextInt(subs.length);
		}
	}
	
	public void mediumMix(){
		Random rand = new Random(readInt(state, index));
		for(int count = 0; count < 100; count++)
			add(rand.nextInt(), rand.nextInt(state.length - 32));
		add(rand.nextInt(), index);
		index += rand.nextInt(state.length - 32);
		if(index > state.length - 32)
			index -= state.length - 32;
	}
	
	public void weakMix(){
		Random rand = new Random(readInt(state, index));
		for(int count = 0; count < 10; count++)
			add(rand.nextInt(), rand.nextInt(state.length - 32));
		if(rand.nextBoolean())
			add(rand.nextInt(), index);
	}
	
	@Override
	public int next(int bits){
		Random rand = new Random(readInt(state, index));
		add(rand.nextInt(), index);
		int mix = rand.nextInt(1000);
		if(mix == 623)
			superMix();
		if(mix > 759 && mix < 782)
			strongMix();
		if(mix < 173)
			mediumMix();
		if(mix > 231 && mix < 429)
			weakMix();
		int result = 0;
		int power = 1;
		for(int bit = 0; bit < bits; bit++){
			if(state[rand.nextInt(state.length)])
				result += power;
			power *= 2;
		}
		index += rand.nextInt(state.length - 32);
		if(index > state.length - 32)
			index -= state.length - 32;
		return result;
	}
	
	protected void add(int value, int index){
		writeInt(readInt(state, index) + value, state, index);
	}
}