#include <detpic32.h>

int main(void)
{
   static const char display7Scodes[] = {0x3f, 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07, 0x7F, 0x6F, 0x77, 0x7C, 0x58, 0x5E, 0x79, 0x71};
   
   TRISB =(TRISB & 0x80FF) | 0x000F;
   TRISD = TRISD & 0xFF9F;
   LATD  = ((LATD  & 0XFF9F) | 0x0020);
 
 while(1)
   {        
       int swishP = PORTB & 0x000F;
       char seg = display7Scodes[swishP];	  
       LATB = (LATB & 0x80ff) | (seg << 8);
   }
   return 0;
}
