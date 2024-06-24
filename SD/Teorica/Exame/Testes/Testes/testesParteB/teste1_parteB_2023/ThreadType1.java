public class ThreadType1 extends Thread
{
     private int id;
     private GenRegion gen = null;
     private LinRegion inStore = null;
     private LinRegion outStore = null;
     
     public ThreadType1 (int id, GenRegion gen, LinRegion inStore, LinRegion outStore)
     {
          this.id = id;
          this.gen = gen;
          this.inStore = inStore;
          this.outStore = outStore;
     }

     @Override
     public void run ()
     {
          int gVal, tVal;
          boolean val = true;
          boolean transf = (inStore != null);     // inStore = null --> false
     
          do 
          {
               try
               { sleep ((int) (1 + 10*Math.random ()));
               }
               catch (InterruptedException e) {};

               if (val)
               {
                    gVal = gen.produceVal();
                    if ((gVal != 0) || !transf)
                         outStore.putVal(100 * id + gVal);
                    val = (gVal != 0);  
               }

               try
               { sleep ((int) (1 + 10*Math.random ()));
               }
               catch (InterruptedException e) {};

               if (transf)
               {
                    tVal = inStore.getVal();

                    if ( ((tVal % 100) != 0) || !val )
                         outStore.putVal (tVal);

                    transf = ((tVal % 100) != 0);
               }
               
          }while(val || transf);
     }
}
