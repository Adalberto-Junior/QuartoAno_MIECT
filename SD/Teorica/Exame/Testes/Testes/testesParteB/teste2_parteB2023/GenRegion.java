public class GenRegion
{
     private int n = 0;
     private int [] valSet = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

     public synchronized int produceVal ()
     {
          int val = 0;
          
          if (n < valSet.length)
          { 
               val = valSet[n];
               n += 1;
          }
         
          return val;
     }
}
