package DSCoinPackage;

import java.util.*;
import HelperClasses.*;

public class Members {
  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    Pair<String,TransactionBlock> s = this.mycoins.get(0);
    this.mycoins.remove(0);
    Transaction tr = new Transaction();
    tr.coinID = s.get_first();
    tr.Source = this;
    tr.coinsrc_block = s.get_second();
    for (int i=0;i<DSobj.memberlist.length;i++){
      if(DSobj.memberlist[i].UID.equals(destUID)){
        tr.Destination = DSobj.memberlist[i];
        break;
      }
    }
    int i=0;
    while (this.in_process_trans[i]!=null){
      i++;
    }
    this.in_process_trans[i] = tr;
    DSobj.pendingTransactions.AddTransactions(tr);
  }

  /*public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Pair<String,TransactionBlock> s = this.mycoins.get(0);
    this.mycoins.remove(0);
    Transaction tr = new Transaction();
    tr.coinID = s.get_first();
    tr.Source = this;
    tr.coinsrc_block = s.get_second();
    for (int i=0;i<DSobj.memberlist.length;i++){
      if(DSobj.memberlist[i].UID.equals(destUID)){
        tr.Destination = DSobj.memberlist[i];
        break;
      }
    }
    int i=0;
    while (this.in_process_trans[i]!=null){
      i++;
    }
    this.in_process_trans[i] = tr;
    DSobj.pendingTransactions.AddTransactions(tr);
  }*/


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    int z = 0;
    TransactionBlock curr =  DSObj.bChain.lastBlock;
    while (z==0&&curr!=null){
      for (int i=0;i<curr.trarray.length;i++){
        if(curr.trarray[i].coinID.equals(tobj.coinID)&&curr.trarray[i].Destination.UID.equals(tobj.Destination.UID)&&curr.trarray[i].Source.UID.equals(tobj.Source.UID)){
          z=1;
          break;
        }
      }
      if(z==0){
        curr=curr.previous;
      }
    }
    if (curr == null) {
      throw new MissingTransactionException();
    }
    int k =0;
    for (int i=0;i<curr.trarray.length;i++){
      if(curr.trarray[i].coinID.equals(tobj.coinID)&&curr.trarray[i].Destination.UID.equals(tobj.Destination.UID)&&curr.trarray[i].Source.UID.equals(tobj.Source.UID)){
        k=i;
        break;
      }
    }
    List<TransactionBlock> sa = new ArrayList<TransactionBlock>();
    TransactionBlock de = DSObj.bChain.lastBlock;
    while (de!=curr.previous){
      sa.add(de);
      de=de.previous;
    }
    int c = (int) Math.round(Math.log(curr.trarray.length) / Math.log(2));
    int[] path = new int[c];
    for (int i = c - 1; i >= 0; i--) {
      if (k % 2 == 0) {
        path[i] = 0;
        k = k / 2;
      } else {
        path[i] = 1;
        k = (k - 1) / 2;
      }
    }
    TreeNode curr1 = curr.Tree.rootnode;
    for (int i = 0; i < c; i++) {
      if (path[i] == 0) {
        curr1 = curr1.left;
      } else {
        curr1 = curr1.right;
      }
    }
    curr1 = curr1.parent;
    List<Pair<String, String>> l = new ArrayList<Pair<String, String>>();
    while (curr1 != null) {
      Pair<String, String> u = new Pair<String, String>(curr1.left.val, curr1.right.val);
      l.add(u);
      curr1 = curr1.parent;
    }
    Pair<String, String> u = new Pair<String, String>(curr.Tree.rootnode.val, null);
    l.add(u);
    List<Pair<String, String>> pk = new ArrayList<Pair<String, String>>();
    int pl = 0;
    for (int i = sa.size() - 1; i >= 0; i--) {
      if (sa.get(i).previous == null) {
        Pair<String, String> e1 = new Pair<String, String>(DSObj.bChain.start_string, null);
        Pair<String, String> e2 = new Pair<String, String>(sa.get(i).dgst, DSObj.bChain.start_string + "#" + sa.get(i).trsummary + "#" + sa.get(i).nonce);
        pk.add(e1);
        pk.add(e2);
      } else {
        if (pl == 0) {
          Pair<String, String> e1 = new Pair<String, String>(sa.get(i).previous.dgst, null);
          pk.add(e1);
          pl = 1;
        }
        Pair<String, String> e2 = new Pair<String, String>(sa.get(i).dgst, sa.get(i).previous.dgst + "#" + sa.get(i).trsummary + "#" + sa.get(i).nonce);
        pk.add(e2);
      }
    }
    int i = 0;
    int hp = 0;
    while (this.in_process_trans[i]!=null){
      if(hp==1){
        this.in_process_trans[i-1]=this.in_process_trans[i];
        this.in_process_trans[i] = null;
      }
      if(this.in_process_trans[i]==tobj){
        hp=1;
        this.in_process_trans[i] = null;
      }
      i++;
    }
    Pair<List<Pair<String, String>>, List<Pair<String, String>>> w = new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(l, pk);
    Pair<String, TransactionBlock> r = new Pair<String, TransactionBlock>(tobj.coinID, curr);
    tobj.Destination.mycoins.add(r);
    sort(tobj.Destination.mycoins,0,tobj.Destination.mycoins.size()-1);
    return w;
  }

  public void MineCoin(DSCoin_Honest DSObj) {
    Transaction[] s = new Transaction[DSObj.bChain.tr_count];
    Transaction curr = DSObj.pendingTransactions.firstTransaction;
    int i = 0;
    while (i != DSObj.bChain.tr_count - 1) {
      if (DSObj.bChain.lastBlock.checkTransaction(curr) && check(curr, s)) {
        s[i] = curr;
        i++;
      }
      curr = curr.next;
      try {
        Transaction rem = DSObj.pendingTransactions.RemoveTransaction();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    Transaction minerRewardTransaction = new Transaction();
    minerRewardTransaction.coinID = addone(DSObj.latestCoinID);
    minerRewardTransaction.Destination = this;
    s[i] = minerRewardTransaction;
    TransactionBlock g = new TransactionBlock(s);
    DSObj.bChain.InsertBlock_Honest(g);
    Pair<String, TransactionBlock> t = new Pair<String, TransactionBlock>(minerRewardTransaction.coinID, g);
    this.mycoins.add(t);
    sort(this.mycoins,0,this.mycoins.size()-1);
    DSObj.latestCoinID = addone(DSObj.latestCoinID);
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
    Transaction[] s = new Transaction[DSObj.bChain.tr_count];
    Transaction curr = DSObj.pendingTransactions.firstTransaction;
    TransactionBlock gh = DSObj.bChain.FindLongestValidChain();
    int i = 0;
    while (i != DSObj.bChain.tr_count - 1) {
      if (gh.checkTransaction(curr) && check(curr, s)) {
        s[i] = curr;
        i++;
      }
      curr = curr.next;
      try {
        Transaction rem = DSObj.pendingTransactions.RemoveTransaction();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    Transaction minerRewardTransaction = new Transaction();
    minerRewardTransaction.coinID = addone(DSObj.latestCoinID);
    minerRewardTransaction.Destination = this;
    s[i] = minerRewardTransaction;
    TransactionBlock g = new TransactionBlock(s);
    DSObj.bChain.InsertBlock_Malicious(g);
    Pair<String, TransactionBlock> t = new Pair<String, TransactionBlock>(minerRewardTransaction.coinID, g);
    this.mycoins.add(t);
    sort(this.mycoins,0,this.mycoins.size()-1);
    DSObj.latestCoinID = addone(DSObj.latestCoinID);
  }
  public boolean check(Transaction a, Transaction[] l) {
    if(l[0]==null)return true;
    for (int i = 0; i < l.length; i++) {
      if(l[i]!=null) {
        if (l[i].Source != null) {
          if (l[i].coinID.equals(a.coinID) && l[i].Source.UID.equals(a.Source.UID) && l[i].Destination.UID.equals(a.Destination.UID)) {
            return false;
          }
        } else {
          if (l[i].coinID.equals(a.coinID) && l[i].Destination.UID.equals(a.Destination.UID)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public static String addone(String s) {
    if (!s.substring(s.length() - 1).equals("9")) {
      return s.substring(0, s.length() - 1) + Integer.toString(Integer.parseInt(s.substring(s.length() - 1)) + 1);
    } else if (s.length() == 1) {
      return Integer.toString(Integer.parseInt(s) + 1);
    } else {
      return addone(s.substring(0, s.length() - 1)) + "0";
    }
  }
  public static void swap(List<Pair<String,TransactionBlock>> arr, int i, int j)
  {
    Pair<String,TransactionBlock> temp = arr.get(i);
    Pair<String,TransactionBlock> k = arr.set(i,arr.get(j));
    Pair<String,TransactionBlock> kc = arr.set(j,temp);
  }
  public static int partition(List<Pair<String,TransactionBlock>> arr, int low, int high) {
    String pivot = arr.get(high).get_first();
    int i = (low - 1);
    for(int j = low; j <= high - 1; j++) {
      if (arr.get(j).get_first().compareTo(pivot)<0){
        i++;
        swap(arr, i, j);
      }
    }
    swap(arr, i + 1, high);
    return (i + 1);
  }
  public static void sort(List<Pair<String,TransactionBlock>> arr, int low, int high){
    if (low < high){
      int pi = partition(arr, low, high);
      sort(arr, low, pi - 1);
      sort(arr, pi + 1, high);
    }
  }
}
