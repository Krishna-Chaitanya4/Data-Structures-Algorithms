package DSCoinPackage;
import HelperClasses.*;
import java.util.*;
public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t){
    this.trarray = t;
    MerkleTree M = new MerkleTree();
    String s = M.Build(t);
    this.Tree = M;
    this.trsummary = this.Tree.rootnode.val;
  }


  public boolean checkTransaction (Transaction t) {

    if(t.coinsrc_block == null){
      return true;
    }
    TransactionBlock curr = this;
    while (curr != t.coinsrc_block){
      for(int k = 0; k<curr.trarray.length;k++){
        if(curr.trarray[k].coinID.equals(t.coinID)){
          return false;
        }
      }
      curr = curr.previous;
    }
    int i = 0;
    while (!curr.trarray[i].coinID.equals(t.coinID)){
      i++;
      if(i==curr.trarray.length){
        return false;
      }
    }
    if(!curr.trarray[i].Destination.UID.equals(t.Source.UID)){
      return false;
    }
    for(int j = i+1; j<curr.trarray.length;j++){
      if(curr.trarray[j].coinID.equals(t.coinID)){
        return false;
      }
    }
    return true;
  }
}
