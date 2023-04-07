package DSCoinPackage;

public class Transaction {

  public String coinID;
  public Transaction next;
  public Transaction previous;
  public Members Source;
  public Members Destination;
  public TransactionBlock coinsrc_block;
}
