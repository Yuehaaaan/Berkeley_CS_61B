public class BadTransactionException extends Exception {


	public BadTransactionException(int badTransactionAmount) {
		super("Invalid TransactionAmount:" + badTransactionAmount);
	}
}