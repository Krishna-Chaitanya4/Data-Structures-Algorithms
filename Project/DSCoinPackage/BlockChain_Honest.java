package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {
  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;
  public void InsertBlock_Honest (TransactionBlock newBlock) {
    CRF m = new CRF(64);
    if(this.lastBlock == null){
      this.lastBlock = newBlock;
      newBlock.nonce = Fnonce(this.start_string,newBlock.trsummary);
      newBlock.dgst = m.Fn(this.start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
    }
    else {
      newBlock.previous = this.lastBlock;
      this.lastBlock = newBlock;
      newBlock.nonce = Fnonce(newBlock.previous.dgst, newBlock.trsummary);
      newBlock.dgst = m.Fn(newBlock.previous.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
    }
  }
  public String Fnonce(String x, String y){
    String s = "1000000001";
    CRF m = new CRF(64);
    String p = m.Fn(x+"#"+y+"#"+s);
    while (!p.substring(0,4).equals("0000")){
      s = addone(s);
      p = m.Fn(x+"#"+y+"#"+s);
    }
    return s;
  }
  public static String addone(String s){
    if(!s.substring(s.length()-1).equals("9")){
      return s.substring(0,s.length()-1)+Integer.toString(Integer.parseInt(s.substring(s.length()-1))+1);
    }
    else if(s.length()==1){
      return Integer.toString(Integer.parseInt(s)+1);
    }
    else {
      return addone(s.substring(0,s.length()-1))+"0";
    }
  }

}
