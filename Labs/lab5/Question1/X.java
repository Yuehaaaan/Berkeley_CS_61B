/* Question 1*/

public class X {
	int x;
	public class Y extends X {
		int y;
	}
	public static void main(String[] args) {
		X[] xa;
		Y[] ya = new Y[10];
		xa = ya;
		ya = (Y[]) xa;
		System.out.println("Finish Q1(a) and (b)!");

		X[] xa3 = new Y[10];
		Y[] ya3;
		ya3 = (Y[]) xa3;
		System.out.println("Finish Q1(c)");
	}
}
