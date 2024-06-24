public class ThreadType2 extends Thread
{
     private LinRegion inStore = null;
     
     public ThreadType2 (LinRegion inStore)
     {
          this.inStore = inStore;
     }

     @Override
     public void run ()
     {
          int cVal, pVal;

          do 
          {
               cVal = inStore.getVal();
               pVal = cVal % 100;
               
               if (pVal != 0)
               {
                    cVal = cVal / 100;
                    System.out.println ("The value produced by thread " + cVal + " was " + pVal + "!\n");
               }
          }while(pVal != 0);
     }
}
