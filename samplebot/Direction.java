package appendix;

class Direction {
	int flag;

	public Direction() {
		this.flag = 1;
	}
	
	public int getDir() {
		return this.flag;
	}

	public void flip() {
		this.flag *= -1;
	}
}
