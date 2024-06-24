public class Semaphore
{
     private int val = 0;
     private int numbBlockThreads = 0;

     public synchronized void down ()
     {
          if (val == 0)
          { numbBlockThreads += 1;
               try
               { wait ();
               }
               catch (InterruptedException e) {}
          }
          else val -= 1;
     }

     public synchronized void up ()
     {
          if (numbBlockThreads != 0)
          { 
               numbBlockThreads -= 1;
               notify ();
          }
          else val += 1;
     }
}
