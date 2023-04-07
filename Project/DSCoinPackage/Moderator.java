package DSCoinPackage;

import HelperClasses.Pair;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Moderator{
    public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
        Members mod = new Members();
        mod.UID = "Moderator";
        if(DSObj.latestCoinID==null){
            DSObj.latestCoinID = "99999";
        }
        int si = coinCount/DSObj.memberlist.length;
        Transaction[] list = new Transaction[DSObj.bChain.tr_count];
        int d=0;
        int total = 0;
        List<Transaction> trlist = new ArrayList<Transaction>();
        while (total<coinCount) {
            for (int i = 0; i < DSObj.memberlist.length; i++) {
                if(total<coinCount) {
                    Transaction r = new Transaction();
                    total++;
                    r.coinID = addone(DSObj.latestCoinID);
                    r.Source = mod;
                    r.Destination = DSObj.memberlist[i];
                    DSObj.latestCoinID = addone(DSObj.latestCoinID);
                    trlist.add(r);
                }
            }
        }
        for (int y=0;y<coinCount/DSObj.bChain.tr_count;y++){
            Transaction[] s = new Transaction[DSObj.bChain.tr_count];

            for (int j=0;j<DSObj.bChain.tr_count;j++){
                s[j] = trlist.get(0);
                trlist.remove(0);
            }
            TransactionBlock tb = new TransactionBlock(s);
            DSObj.bChain.InsertBlock_Honest(tb);
            for (int j=0;j<DSObj.bChain.tr_count;j++){
                String coin = s[j].coinID;
                Pair<String,TransactionBlock> pai = new Pair<String,TransactionBlock>(coin,tb);
                s[j].Destination.mycoins.add(pai);
            }
        }
    }
    public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
        Members mod = new Members();
        mod.UID = "Moderator";
        if(DSObj.latestCoinID==null){
            DSObj.latestCoinID = "99999";
        }
        int si = coinCount/DSObj.memberlist.length;
        Transaction[] list = new Transaction[DSObj.bChain.tr_count];
        int d=0;
        int total = 0;
        List<Transaction> trlist = new ArrayList<Transaction>();
        while (total<coinCount) {
            for (int i = 0; i < DSObj.memberlist.length; i++) {
                if(total<coinCount) {
                    Transaction r = new Transaction();
                    total++;
                    r.coinID = addone(DSObj.latestCoinID);
                    r.Source = mod;
                    r.Destination = DSObj.memberlist[i];
                    DSObj.latestCoinID = addone(DSObj.latestCoinID);
                    trlist.add(r);
                }
            }
        }
        for (int y=0;y<coinCount/DSObj.bChain.tr_count;y++){
            Transaction[] s = new Transaction[DSObj.bChain.tr_count];

            for (int j=0;j<DSObj.bChain.tr_count;j++){
                s[j] = trlist.get(0);
                trlist.remove(0);
            }
            TransactionBlock tb = new TransactionBlock(s);
            DSObj.bChain.InsertBlock_Malicious(tb);
            for (int j=0;j<DSObj.bChain.tr_count;j++){
                String coin = s[j].coinID;
                Pair<String,TransactionBlock> pai = new Pair<String,TransactionBlock>(coin,tb);
                s[j].Destination.mycoins.add(pai);
            }
        }
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
