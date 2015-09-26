package com.tomgibara.bits;

import com.tomgibara.bits.BitStore.Matches;

//TODO could use better search algorithm
class BitStoreMatches extends BitStore.Matches {

	private final BitStore s;
	private final BitStore t;
	private final int sSize;
	private final int tSize;
	
	BitStoreMatches(BitStore store, BitStore sequence) {
		s = store;
		t = sequence;
		sSize = s.size();
		tSize = t.size();
	}

	@Override
	public BitStore store() {
		return s;
	}

	@Override
	public BitStore sequence() {
		return t;
	}

	@Override
	public Matches range(int from, int to) {
		return s.range(from, to).match(t);
	}

	@Override
	public int count() {
		int count = 0;
		int previous = last();
		while (previous != -1) {
			count ++;
			previous = previous(previous);
		}
		return count;
	}

	@Override
	public int first() {
		return next(0);
	}

	@Override
	public int last() {
//		int limit = sSize - tSize;
//		if (limit < 0) return -1;
//		if (matchesAt(limit)) return limit;
//		return previous(limit);
		//TODO is this a risk? - simpler, but position could be rejected?
		return previous(sSize + 1);
	}

	@Override
	public int next(int position) {
		int limit = sSize - tSize;
		while (position <= limit) {
			if (matchesAt(position)) return position;
			position ++;
		}
		return sSize;
	}

	@Override
	public int previous(int position) {
		position = Math.min(position, sSize - tSize + 1);
		while (position > 0) {
			position --;
			if (matchesAt(position)) return position;
		}
		return -1;
	}
	
	private boolean matchesAt(int position) {
		return s.range(position, position + tSize).equals().store(t);
	}
}
