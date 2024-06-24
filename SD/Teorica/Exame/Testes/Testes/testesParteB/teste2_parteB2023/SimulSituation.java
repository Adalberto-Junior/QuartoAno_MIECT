public class SimulSituation
{
     public static void main (String [] args)
     {
          LinRegion [] store = new LinRegion [3];
          for (int i = 0; i < 3; i++)
               store[i] = new LinRegion ();

          GenRegion gen = new GenRegion ();

          ThreadType1 [] thr1 = new ThreadType1[3];
          for (int i = 2; i >= 0; i--)
          {
               if (i != 0)
                    thr1[i] = new ThreadType1 (i+1, gen, store[3-i], store[2-i]);
               else
                    thr1[i] = new ThreadType1 (i+1, gen, null, store[2-i]);          
          }
          ThreadType2 thr2 = new ThreadType2 (store[0]);

          thr2.start ();

          for (int i = 0; i < 3; i++)
               thr1[i].start ();
     }
}

/*
          ThreadType1 [] thr1 = new ThreadType1[3];
          for (int i = 0; i < 3; i++)
          {
               if (i != 0)
                    thr1[i] = new ThreadType1 (i+1, gen, store[i-1], store[i]);
               else
                    thr1[i] = new ThreadType1 (i+1, gen, null, store[i]);          
          }
          ThreadType2 thr2 = new ThreadType2 (store[2]);
          */
