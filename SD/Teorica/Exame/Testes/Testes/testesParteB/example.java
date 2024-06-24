public class GenRegion
{
     private int n = 0;
     private int [] valSet = {5, 12, 3, 6, 4, 2, 1, 18, 15, 16, 9, 7};

     public synchronized int produceVal ()   /// threads 1
     {
          if (n == valSet.length)
               return 0;
               else { int val = valSet[n];
                         n += 1;
                         return val;
                    }
     }
}

public class StoreRegion
{
     private int mem = 0;
     private int stat = 0;
     public synchronized void putVal (int val)    /// threads 1
     {
          while (stat != 0)   // a thr1[0] n entra aqui pq stat = 0, as outras vao ter de esperar ate esse valor val/mem ser lido
          {    try
               { wait ();
               }
               catch (InterruptedException e) {}
          }
          stat = 1;
          mem = val;
          notifyAll ();  // notifica as threads que esperam para retirar o val/mem --> getVal
          while (stat == 1)
          {    try
               { wait ();
               }
               catch (InterruptedException e) {}
          }
     }

     public synchronized int getVal ()            /// threads 2
     {
          while ((stat != 1) && (stat != 2)) // se stat = 0 --> wait pq n existe nada para ler no mem
          {    try
               { wait ();
               }
               catch (InterruptedException e) {}
          }
          if (stat == 1) stat = 0;
          int val = mem;
          mem = 0;
          notifyAll ();
          return val;
     }

     public synchronized void noWait ()
     {
          stat = 2;
          notifyAll ();
     }
}

public class Resource
{
     private int n;
     private StoreRegion [] store = null;
     public Resource (int n, StoreRegion [] store)
     {
          this.n = n;
          this.store = store;
     }

     public synchronized boolean printVal (int id, int val)
     {
          if (n >= 1)
          { System.out.println ("The value processed by " + (val/100) + " and by " + id + " was " + (val%100) + ".");

               n -= 1;
               if (n == 0)
                    for (int i = 0; i < 2; i++)
                         store[i].noWait ();
          }
          return (n == 0);
     }
}

public class ThreadType1 extends Thread
{
     private int id;
     private GenRegion gen = null;
     private StoreRegion [] store = null;
     public ThreadType1 (int id, GenRegion gen, StoreRegion [] store)
     {
          this.id = id;
          this.gen = gen;
          this.store = store;
     }
     public void run ()
     {
          int val;
          do
          { 
               try
               { sleep ((int) (1 + 10*Math.random ()));
               }
               catch (InterruptedException e) {};
               val = gen.produceVal ();
               try
               { sleep ((int) (1 + 10*Math.random ()));
               }
               catch (InterruptedException e) {};
               if (val != 0)
                    switch (val % 3)
                    {    
                         case 0: store[0].putVal (100*id+2*val);
                                   break;
                         case 1:
                         case 2: store[1].putVal (100*id+val);
                    }
          } while (val != 0);
     }
}

public class ThreadType2 extends Thread
{
     private int id;
     private StoreRegion [] store = null;
     private Resource writer = null;
     public ThreadType2 (int id, StoreRegion [] store, Resource writer)
     {
          this.id = id;
          this.store = store;
          this.writer = writer;
     }

     public void run ()
     {
          int val;
          boolean end = false;
          while (!end)
          { 
               val = store[(id-1)/2].getVal ();
               try
               { sleep ((int) (1 + 10*Math.random ()));
               }
               catch (InterruptedException e) {};
               end = writer.printVal (id, val);
          }
     }
}


public class SimulSituation
{
     public static void main (String [] args)
     {
          StoreRegion [] store = new StoreRegion [2];
          for (int i = 0; i < 2; i++)
               store[i] = new StoreRegion ();

          GenRegion gen = new GenRegion ();

          Resource writer = new Resource (12, store);

          ThreadType1 [] thr1 = new ThreadType1[4];
          for (int i = 0; i < 4; i++)
               thr1[i] = new ThreadType1 (i+1, gen, store);

          ThreadType2 [] thr2 = new ThreadType2[4];
          for (int i = 0; i < 4; i++)
               thr2[i] = new ThreadType2 (i+1, store, writer);

          for (int i = 0; i < 4; i++)
               thr2[i].start ();
          for (int i = 0; i < 4; i++)
               thr1[i].start ();
     }
}
