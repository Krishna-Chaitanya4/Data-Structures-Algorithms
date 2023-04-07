package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if(this.numTransactions == 0){
      this.lastTransaction = transaction;
      this.firstTransaction = transaction;
    }
    else {
      this.lastTransaction.next = transaction;
      transaction.previous = this.lastTransaction;
      this.lastTransaction = transaction;
    }
    this.numTransactions = this.numTransactions + 1;
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    if(this.firstTransaction == null){
      throw new EmptyQueueException();
    }
    else if(this.numTransactions == 1) {
      Transaction a = this.firstTransaction;
      this.firstTransaction = null;
      this.lastTransaction = null;
      this.numTransactions = this.numTransactions - 1;
      return a;
    }
    else {
      Transaction a = this.firstTransaction;
      this.firstTransaction = this.firstTransaction.next;
      this.firstTransaction.previous = null;
      this.numTransactions = this.numTransactions - 1;
      return a;
    }
  }

  public int size() {
    return this.numTransactions;
  }
}
