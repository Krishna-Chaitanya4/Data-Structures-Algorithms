import java.io.*;
import java.util.*;

public class kdtree {
    public node rootnode;
    public void Build(ArrayList<Pair<Integer, Integer>> l){
        int k = l.size();
        xsort(l,0,k-1);
        node a = new node();
        this.rootnode = a;
        a.num = k;
        if (k == 1) {
            a.isLeaf = true;
        } else {
            a.isLeaf = false;
        }
        a.val = new Pair<Integer, Integer>(l.get((k - 1) / 2).get_first(),l.get((k - 1) / 2).get_second());
        a.yrange = new Pair<Integer, Integer>();
        a.xrange = new Pair<Integer, Integer>();
        a.d = 0;
        a.r = l;
        sk(this.rootnode);
    }

    public void sk(node t) {
        if (t.d % 2 == 0) {
            if (t.num == 1) {
                return;
            }
            else {
                node a = new node();
                t.left = a;
                a.parent = t;
                a.xrange = new Pair<Integer, Integer>(t.xrange.get_first(), t.val.get_first());
                a.yrange = t.yrange;
                a.num = (t.num+1)/2;
                a.d = t.d + 1;
                a.r = new ArrayList<Pair<Integer, Integer>>(t.r.subList(0,(t.num+1)/2));
                ysort(a.r,0,a.num-1);
                if (a.num == 1) {
                    a.isLeaf = true;
                } else {
                    a.isLeaf = false;
                }
                a.val = new Pair<Integer, Integer>(a.r.get((a.r.size() - 1) / 2).get_first(), a.r.get((a.r.size() - 1) / 2).get_second());
                sk(a);
                node b = new node();
                t.right = b;
                b.parent = t;
                b.xrange = new Pair<Integer, Integer>(t.val.get_first()+1, t.xrange.get_second());
                b.yrange = t.yrange;
                b.num = t.num-a.num;
                b.d = t.d + 1;
                b.r = new ArrayList<Pair<Integer, Integer>>(t.r.subList((t.num+1)/2,t.num));
                ysort(b.r,0,b.num-1);
                if (b.num == 1) {
                    b.isLeaf = true;
                } else {
                    b.isLeaf = false;
                }
                b.val = new Pair<Integer, Integer>(b.r.get((b.r.size() - 1) / 2).get_first(), b.r.get((b.num - 1) / 2).get_second());
                sk(b);
            }
        } else {
            if (t.r.size() == 1) {
                return;
            }
            else {
                node a = new node();
                t.left = a;
                a.parent = t;
                a.yrange = new Pair<Integer, Integer>(t.yrange.get_first(), t.val.get_second());
                a.xrange = t.xrange;
                a.d = t.d + 1;
                a.num = (t.num+1)/2;
                a.r = new ArrayList<Pair<Integer, Integer>>(t.r.subList(0,a.num));
                xsort(a.r,0,a.num-1);
                if (a.num == 1) {
                    a.isLeaf = true;
                } else {
                    a.isLeaf = false;
                }
                a.val = new Pair<Integer, Integer>(a.r.get((a.num - 1) / 2).get_first(),a.r.get((a.num - 1) / 2).get_second());
                sk(a);
                node b = new node();
                t.right = b;
                b.parent = t;
                b.yrange = new Pair<Integer, Integer>(t.val.get_second()+1, t.yrange.get_second());
                b.xrange = t.xrange;
                b.d = t.d + 1;
                b.num = t.num-a.num;
                b.r = new ArrayList<Pair<Integer, Integer>>(t.r.subList(a.num,t.num));
                xsort(b.r,0,b.num-1);
                if (b.num == 1) {
                    b.isLeaf = true;
                } else {
                    b.isLeaf = false;
                }
                b.val = new Pair<Integer, Integer>(b.r.get((b.r.size() - 1) / 2).get_first(),b.r.get((b.r.size() - 1) / 2).get_second());
                sk(b);
            }
        }

    }
    public int query(Pair<Integer,Integer> s){
        int c = 0;
        int x1 = s.get_first()-100;
        int x2 = s.get_first()+100;
        int y1 = s.get_second()-100;
        int y2 = s.get_second()+100;
        return this.rootnode.ms(x1,x2,y1,y2);
    }
    public  static void swap(ArrayList<Pair<Integer,Integer>> arr, int i, int j)
    {
        Pair<Integer,Integer> temp = arr.get(i);
        Pair<Integer,Integer> k = arr.set(i,arr.get(j));
        Pair<Integer,Integer> kc = arr.set(j,temp);
    }
    public static int xpartition(ArrayList<Pair<Integer,Integer>> arr, int low, int high) {
        int pivot = arr.get(high).get_first();
        int i = (low - 1);
        for(int j = low; j <= high - 1; j++) {
            if (arr.get(j).get_first() < pivot){
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }
    public static void xsort(ArrayList<Pair<Integer,Integer>> arr, int low, int high){
        if (low < high){
            int pi = xpartition(arr, low, high);
            xsort(arr, low, pi - 1);
            xsort(arr, pi + 1, high);
        }
    }
    public static int ypartition(ArrayList<Pair<Integer,Integer>> arr, int low, int high) {
        int pivot = arr.get(high).get_second();
        int i = (low - 1);
        for(int j = low; j <= high - 1; j++) {
            if (arr.get(j).get_second() < pivot){
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }
    public static void ysort(ArrayList<Pair<Integer,Integer>> arr, int low, int high)
    {
        if (low < high) {
            int pi = ypartition(arr, low, high);
            ysort(arr, low, pi - 1);
            ysort(arr, pi + 1, high);
        }
    }

    public static void main(String[] args) {
        try {
            FileInputStream fs = new FileInputStream("restaurants.txt");
            Scanner s = new Scanner(fs);
            ArrayList<Pair<Integer,Integer>> l1 = new ArrayList<Pair<Integer,Integer>>();
	    String e1 = s.nextLine();
            while (s.hasNextLine()){
		String pi = s.nextLine();
                String[] p = pi.split(",");
                Pair<Integer,Integer> ps = new Pair<Integer, Integer>(Integer.parseInt(p[0]),Integer.parseInt(p[1]));
                l1.add(ps);
            }
            FileInputStream fs2 = new FileInputStream("queries.txt");
            Scanner s2 = new Scanner(fs2);
            ArrayList<Pair<Integer,Integer>> l2 = new ArrayList<Pair<Integer,Integer>>();
	    e1 = s2.nextLine();
            while (s2.hasNextLine()){
		String pi = s2.nextLine();
                String[] p = pi.split(",");
                Pair<Integer,Integer> ps = new Pair<Integer, Integer>(Integer.parseInt(p[0]),Integer.parseInt(p[1]));
                l2.add(ps);
            }
            kdtree k = new kdtree();
            k.Build(l1);
            FileOutputStream fk = new FileOutputStream("output.txt",false);
            PrintStream p  =new PrintStream(fk);
            for (int i = 0; i<l2.size();i++){
                p.println(k.query(l2.get(i)));
            }
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
    }

}

class node {
    public node left;
    public node right;
    public node parent;
    public Pair<Integer, Integer> xrange;
    public Pair<Integer, Integer> yrange;
    public boolean isLeaf;
    public Pair<Integer, Integer> val;
    public int d;
    public int num;
    public ArrayList<Pair<Integer, Integer>> r;
    public int ms(int x1,int x2, int y1, int y2){
        if(this.isLeaf == true && (this.val.get_first()!=null && x1 <= this.val.get_first()) && (this.val.get_first()!=null && this.val.get_first() <= x2) && (this.val.get_second()!=null && y1 <= this.val.get_second()) && (this.val.get_second() != null && this.val.get_second() <= y2)){
            return 1;
        }
	else if(this.isLeaf == true){return 0;}
        else if((this.xrange.get_second() != null && this.xrange.get_second()<x1) || (this.xrange.get_first() != null && x2<this.xrange.get_first()) || (this.yrange.get_second()!= null && this.yrange.get_second()<y1) || (this.yrange.get_first()!=null && y2<this.yrange.get_first())){
            return 0;
        }
        else {
            return this.left.ms(x1, x2, y1, y2)+this.right.ms(x1, x2, y1, y2);
	    
        }
    }
}

class Pair<A, B> {
    public A First;
    public B Second;
    public Pair(){

    }
    public Pair(A _first, B _second) {
        this.First = _first;
        this.Second = _second;
    }
    public A get_first() {
        return First;
    }
    public B get_second() {
        return Second;
    }
}