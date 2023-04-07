package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;
import HelperClasses.Pair;

import java.util.ArrayList;
import java.util.List;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF m = new CRF(64);
    if(tB.previous!=null) {
      if (!tB.dgst.substring(0, 4).equals("0000") || !tB.dgst.equals(m.Fn(tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce))) {
        return false;
      }
    }
    else {
      if (!tB.dgst.substring(0, 4).equals("0000") || !tB.dgst.equals(m.Fn(start_string + "#" + tB.trsummary + "#" + tB.nonce))) {
        return false;
      }
    }
    MerkleTree M = new MerkleTree();
    String r = M.Build(tB.trarray);
    if(!tB.trsummary.equals(r)){
      return false;
    }
    if(tB.previous!=null) {
      for (int i = 0; i < tB.trarray.length; i++) {
        if (!tB.previous.checkTransaction(tB.trarray[i])) {
          return false;
        }
      }
    }
    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    int n = this.lastBlocksList.length;
    Pair<Integer,TransactionBlock> max = lc(this.lastBlocksList[0]);
    for(int i=1; i<n;i++){
      Pair<Integer,TransactionBlock> s = lc(this.lastBlocksList[i]);
      if(s.get_first()>max.get_first()){
        max = s;
      }
    }
    return max.get_second();
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    if(this.lastBlocksList[0]==null){
      this.lastBlocksList[0] = newBlock;
      BlockChain_Honest m = new BlockChain_Honest();
      newBlock.nonce = m.Fnonce(start_string, newBlock.trsummary);
      CRF p = new CRF(64);
      newBlock.dgst = p.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
    }
    else {
      TransactionBlock s = this.FindLongestValidChain();
      BlockChain_Honest m = new BlockChain_Honest();
      newBlock.nonce = m.Fnonce(s.dgst, newBlock.trsummary);
      newBlock.previous = s;
      CRF p = new CRF(64);
      newBlock.dgst = p.Fn(s.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      int k=0;
      int kl = 0;
      while (this.lastBlocksList[k]!=null){
        if (this.lastBlocksList[k]==newBlock.previous){
          this.lastBlocksList[k] = newBlock;
          kl = 1;
          break;
        }
        k++;
      }
      if (kl==0){
        this.lastBlocksList[k] = newBlock;
      }
    }
  }
  public Pair<Integer,TransactionBlock> lc(TransactionBlock s){
    int i = 0;
    TransactionBlock k = s;
    TransactionBlock curr = s;
    while (curr!=null){
      if(checkTransactionBlock(curr)){
        if(i==0){
          k = curr;
        }
        i++;
      }
      else {
        i=0;
      }
      curr = curr.previous;
    }
    Pair<Integer,TransactionBlock> cs = new Pair<Integer,TransactionBlock>(i,k);
    return cs;
  }
}
