package com.tomgibara.bits;

class CharBitReader implements BitReader {

	private final CharSequence chars;
	private int position;
	
	CharBitReader(CharSequence chars) {
		this.chars = chars;
		this.position = 0;
	}
	
	CharBitReader(CharSequence chars, int position) {
		this.chars = chars;
		this.position = position;
	}
	
	@Override
	public int readBit() {
		return readBoolean() ? 1 : 0;
	}
	
	@Override
	public boolean readBoolean() {
		if (position >= chars.length()) throw new EndOfBitStreamException();
		char c = chars.charAt(position);
		switch (c) {
		case '0' : position ++; return false;
		case '1' : position ++; return true;
		default: throw new BitStreamException("Non binary character '" + c + "' found at index " + position);
		}
	}

	@Override
	public long getPosition() {
		return position;
	}
	
	@Override
	public long setPosition(long newPosition) throws BitStreamException, IllegalArgumentException {
		if (newPosition < 0) throw new IllegalArgumentException();
		position = chars.length();
		if (newPosition < position) position = (int) newPosition; 
		return position;
	}

	@Override
	public long skipBits(long count) throws BitStreamException {
		if (count == 0L) return 0L;
		int newPosition;
		if (count < 0) {
			newPosition = count >= Integer.MIN_VALUE ? Math.max(0, (int) (position + count)) : 0;
		} else {
			newPosition = chars.length();
			if (count <= Integer.MAX_VALUE && position + count <= Integer.MAX_VALUE) {
				newPosition = Math.min(newPosition, (int) (position + count));
			}
		}
		int skipped = newPosition - position;
		position = newPosition;
		return skipped;
	}
}
