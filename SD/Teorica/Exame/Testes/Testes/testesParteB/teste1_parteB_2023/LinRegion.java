public class LinRegion 
{
     private int mem = 0;
     private Semaphore [] stat; 
     
     public LinRegion ()
     {
          stat = new Semaphore[3];
          for (int i = 0; i < 3; i++)
          {
               stat[i] = new Semaphore ();
               if (i != 2) stat[i].up();
          }
     }

     public void putVal (int val)
     {
          stat[1].down();
          stat[0].down();
          if ((val % 2 == 0))
               mem = val + val % 100;
          else 
               mem = val;
          stat[0].up();
          stat[2].up();
     }
     
     public int getVal ()
     {
          int val;
          stat[2].down();
          stat[0].down();
          val = mem;
          stat[0].up();
          stat[1].up();
          return val;
     }
}
