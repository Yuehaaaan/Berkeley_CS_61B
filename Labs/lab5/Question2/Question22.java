public class Question22 extends Question2 implements interf {
	public void info_print(int x, int y) {
		System.out.println("print from extended class Question22.");
//		return i;
	}

	public static void main(String[] args) {
		Question22 x = new Question22();
		x.info_print(1,2);
		x.info_print(3,4);
	}
	
}