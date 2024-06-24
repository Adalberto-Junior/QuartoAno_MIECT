public class GenRegion
{
     private int n = 0;
     private int [] valSet = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
     private Semaphore access;
     public GenRegion ()
     {
          n = 0;
          access = new Semaphore ();
          access.up ();
     }

     public int produceVal ()
     {
          int val = 0;
          access.down ();
          if (n < valSet.length)
          { 
               val = valSet[n];
               n += 1;
          }
          access.up ();
          return val;
     }
}
