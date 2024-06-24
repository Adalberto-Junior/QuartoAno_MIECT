public class LinRegion 
{
     private int mem = 0;
     private int stat = 0; 

     public synchronized void putVal (int val)
     {
          while (stat != 0)
          { 
               try
               { wait ();
               }
               catch (InterruptedException e) {};
          }
          stat = 1;

          if ((val % 2 == 0))
               mem = val + val % 100;
          else 
               mem = val;

          notifyAll();
          while (stat == 1)
          { 
               try
               { wait ();
               }
               catch (InterruptedException e) {};
          }
     }
     
     public synchronized int getVal ()
     {
          int val;
          while (stat == 0)
          {
               try{
                    wait();
               }
               catch (InterruptedException e) {};         
          }
          
          if (stat == 1) stat = 0;
          val = mem;
          mem = 0;
          notifyAll ();
          
          return val;
     }
}
