LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY flipFlopDPET IS
  PORT (clk, D:     IN STD_LOGIC;
        nSet, nRst: IN STD_LOGIC;
        Q, nQ:      OUT STD_LOGIC);
END flipFlopDPET;

ARCHITECTURE behavior OF flipFlopDPET IS
BEGIN
	PROCESS (clk, nSet, nRst)
  BEGIN
    IF (nRst = '0')
	    THEN Q <= '0';
		      nQ <= '1';
		 ELSIF (nSet = '0')
		       THEN Q <= '1';
		            nQ <= '0';
	          ELSIF (clk = '1') AND (clk'EVENT)
	                THEN Q <= D;
		                  nQ <= NOT D;

	 END IF;
  END PROCESS;
END behavior;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY binCounter_6bit IS
  PORT (nRst: IN STD_LOGIC;
        clk:  IN STD_LOGIC;
        c:    OUT STD_LOGIC_VECTOR (5 DOWNTO 0));
END binCounter_6bit;

ARCHITECTURE structure OF binCounter_6bit IS
  SIGNAL pD1, pD2, pD3, pD4: STD_LOGIC;
  SIGNAL iD1, iD2, iD3, iD4, iD5: STD_LOGIC;
  SIGNAL iQ0, iQ1, iQ2, iQ3, iQ4, iQ5: STD_LOGIC;
  SIGNAL inQ0: STD_LOGIC;
  COMPONENT gateAnd2
    PORT (x1, x2: IN STD_LOGIC;
          y:      OUT STD_LOGIC);
  END COMPONENT;
  COMPONENT gateXor2
    PORT (x1, x2: IN STD_LOGIC;
          y:      OUT STD_LOGIC);
  END COMPONENT;
  COMPONENT flipFlopDPET
    PORT (clk, D:     IN STD_LOGIC;
          nSet, nRst: IN STD_LOGIC;
          Q, nQ:      OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  ad1: gateAnd2 PORT MAP (iQ0, iQ1, pD1);
  ad2: gateAnd2 PORT MAP (pD1, iQ2, pD2);
  ad3: gateAnd2 PORT MAP (pD2, iQ3, pD3);
  ad4: gateAnd2 PORT MAP (pD3, iQ4, pD4);
  xr1: gateXor2 PORT MAP (iQ0, iQ1, iD1);
  xr2: gateXor2 PORT MAP (pD1, iQ2, iD2);
  xr3: gateXor2 PORT MAP (pD2, iQ3, iD3);
  xr4: gateXor2 PORT MAP (pD3, iQ4, iD4);
  xr5: gateXor2 PORT MAP (pD4, iQ5, iD5);
  ff0: flipFlopDPET PORT MAP (clk, inQ0, '1', nRst, iQ0, inQ0);
  ff1: flipFlopDPET PORT MAP (clk, iD1,  '1', nRst, iQ1);
  ff2: flipFlopDPET PORT MAP (clk, iD2,  '1', nRst, iQ2);
  ff3: flipFlopDPET PORT MAP (clk, iD3,  '1', nRst, iQ3);
  ff4: flipFlopDPET PORT MAP (clk, iD4,  '1', nRst, iQ4);
  ff5: flipFlopDPET PORT MAP (clk, iD5,  '1', nRst, iQ5);
  c(0) <= iQ0;
  c(1) <= iQ1;
  c(2) <= iQ2;
  c(3) <= iQ3;
  c(4) <= iQ4;
  c(5) <= iQ5;
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY parReg_5bit IS
  PORT (nRst: IN STD_LOGIC;
        clk: IN STD_LOGIC;
        D: IN STD_LOGIC_VECTOR (4 DOWNTO 0);
        Q: OUT STD_LOGIC_VECTOR (4 DOWNTO 0));
END parReg_5bit;

ARCHITECTURE structure OF parReg_5bit IS
  COMPONENT flipFlopDPET
    PORT (clk, D: IN STD_LOGIC;
          nSet, nRst: IN STD_LOGIC;
          Q, nQ: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  ff0: flipFlopDPET PORT MAP (clk, D(0), '1', nRst, Q(0));
  ff1: flipFlopDPET PORT MAP (clk, D(1), '1', nRst, Q(1));
  ff2: flipFlopDPET PORT MAP (clk, D(2), '1', nRst, Q(2));
  ff3: flipFlopDPET PORT MAP (clk, D(3), '1', nRst, Q(3));
  ff4: flipFlopDPET PORT MAP (clk, D(4), '1', nRst, Q(4));
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY parReg_6bit IS
  PORT (nSet: IN STD_LOGIC;
        clk: IN STD_LOGIC;
        D: IN STD_LOGIC_VECTOR (5 DOWNTO 0);
        Q: OUT STD_LOGIC_VECTOR (5 DOWNTO 0));
END parReg_6bit;

ARCHITECTURE structure OF parReg_6bit IS
  COMPONENT flipFlopDPET
    PORT (clk, D: IN STD_LOGIC;
          nSet, nRst: IN STD_LOGIC;
          Q, nQ: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  ff0: flipFlopDPET PORT MAP (clk, D(0), nSet, '1', Q(0));
  ff1: flipFlopDPET PORT MAP (clk, D(1), nSet, '1', Q(1));
  ff2: flipFlopDPET PORT MAP (clk, D(2), nSet, '1', Q(2));
  ff3: flipFlopDPET PORT MAP (clk, D(3), nSet, '1', Q(3));
  ff4: flipFlopDPET PORT MAP (clk, D(4), nSet, '1', Q(4));
  ff5: flipFlopDPET PORT MAP (clk, D(5), nSet, '1', Q(5));
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY parReg_24bit IS
  PORT (nSet: IN STD_LOGIC;
        clk: IN STD_LOGIC;
        D: IN STD_LOGIC_VECTOR (15 DOWNTO 0);
		  R: IN STD_LOGIC_VECTOR (7 DOWNTO 0);
        Q: OUT STD_LOGIC_VECTOR (23 DOWNTO 0));
END parReg_24bit;

ARCHITECTURE structure OF parReg_24bit IS
  COMPONENT flipFlopDPET
    PORT (clk, D: IN STD_LOGIC;
          nSet, nRst: IN STD_LOGIC;
          Q, nQ: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  ff0: flipFlopDPET PORT MAP (clk, R(0), nSet, '1', Q(0));
  ff1: flipFlopDPET PORT MAP (clk, R(1), nSet, '1', Q(1));
  ff2: flipFlopDPET PORT MAP (clk, R(2), nSet, '1', Q(2));
  ff3: flipFlopDPET PORT MAP (clk, R(3), nSet, '1', Q(3));
  ff4: flipFlopDPET PORT MAP (clk, R(4), nSet, '1', Q(4));
  ff5: flipFlopDPET PORT MAP (clk, R(5), nSet, '1', Q(5));
  ff6: flipFlopDPET PORT MAP (clk, R(6), nSet, '1', Q(6));
  ff7: flipFlopDPET PORT MAP (clk, R(7), nSet, '1', Q(7));
  
  ff08: flipFlopDPET PORT MAP (clk, D(0), nSet, '1', Q(8));
  ff09: flipFlopDPET PORT MAP (clk, D(1), nSet, '1', Q(9));
  ff10: flipFlopDPET PORT MAP (clk, D(2), nSet, '1', Q(10));
  ff11: flipFlopDPET PORT MAP (clk, D(3), nSet, '1', Q(11));
  ff12: flipFlopDPET PORT MAP (clk, D(4), nSet, '1', Q(12));
  ff13: flipFlopDPET PORT MAP (clk, D(5), nSet, '1', Q(13));
  ff14: flipFlopDPET PORT MAP (clk, D(6), nSet, '1', Q(14));
  ff15: flipFlopDPET PORT MAP (clk, D(7), nSet, '1', Q(15));
  ff16: flipFlopDPET PORT MAP (clk, D(8), nSet, '1', Q(16));
  ff17: flipFlopDPET PORT MAP (clk, D(9), nSet, '1', Q(17));
  ff18: flipFlopDPET PORT MAP (clk, D(10), nSet, '1', Q(18));
  ff19: flipFlopDPET PORT MAP (clk, D(11), nSet, '1', Q(19));
  ff20: flipFlopDPET PORT MAP (clk, D(12), nSet, '1', Q(20));
  ff21: flipFlopDPET PORT MAP (clk, D(13), nSet, '1', Q(21));
  ff22: flipFlopDPET PORT MAP (clk, D(14), nSet, '1', Q(22));
  ff23: flipFlopDPET PORT MAP (clk, D(15), nSet, '1', Q(23));
  
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY parReg_9bit IS
  PORT (nRst: IN STD_LOGIC;
        clk: IN STD_LOGIC;
		  D0: IN STD_LOGIC;
        D: IN STD_LOGIC_VECTOR (7 DOWNTO 0);
        Q: OUT STD_LOGIC_VECTOR (8 DOWNTO 0));
END parReg_9bit;

ARCHITECTURE structure OF parReg_9bit IS
  COMPONENT flipFlopDPET
    PORT (clk, D: IN STD_LOGIC;
          nSet, nRst: IN STD_LOGIC;
          Q, nQ: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  ff0: flipFlopDPET PORT MAP (clk, D0, '1', nRst, Q(0));
  ff1: flipFlopDPET PORT MAP (clk, D(0), '1', nRst, Q(1));
  ff2: flipFlopDPET PORT MAP (clk, D(1), '1', nRst, Q(2));
  ff3: flipFlopDPET PORT MAP (clk, D(2), '1', nRst, Q(3));
  ff4: flipFlopDPET PORT MAP (clk, D(3), '1', nRst, Q(4));
  ff5: flipFlopDPET PORT MAP (clk, D(4), '1', nRst, Q(5));
  ff6: flipFlopDPET PORT MAP (clk, D(5), '1', nRst, Q(6));
  ff7: flipFlopDPET PORT MAP (clk, D(6), '1', nRst, Q(7));
  ff8: flipFlopDPET PORT MAP (clk, D(7), '1', nRst, Q(8));
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY shiftRegist_24bit IS 
	PORT (nRst: IN STD_LOGIC;
			clk: IN STD_LOGIC;
			shift: IN STD_LOGIC;
			dataPsIn: IN STD_LOGIC_VECTOR(15 DOWNTO 0);
			datalsIn: IN STD_LOGIC_VECTOR(7 DOWNTO 0);
			Q:		OUT STD_LOGIC);
END shiftRegist_24bit;

ARCHITECTURE structure OF shiftRegist_24bit IS

	SIGNAL inverSg: STD_LOGIC;

	SIGNAL out23Sg,andFf22OutShftSg,ff23InSg: STD_LOGIC;
	SIGNAL out22Sg,andFf21OutshftSg,ff22InSg,ff22OutSg: STD_LOGIC;
	SIGNAL out21Sg,andFf20OutShftSg,ff21InSg,ff21OutSg: STD_LOGIC;
	SIGNAL out20Sg,andFf19OutShftSg,ff20InSg,ff20OutSg: STD_LOGIC;
	SIGNAL out19Sg,andFf18OutShftSg,ff19InSg,ff19OutSg: STD_LOGIC;
	SIGNAL out18Sg,andFf17OutShftSg,ff18InSg,ff18OutSg: STD_LOGIC;
	SIGNAL out17Sg,andFf16OutShftSg,ff17InSg,ff17OutSg: STD_LOGIC;
	SIGNAL out16Sg,andFf15OutshftSg,ff16InSg,ff16OutSg: STD_LOGIC;
	SIGNAL out15Sg,andFf14OutShftSg,ff15InSg,ff15OutSg: STD_LOGIC;
	SIGNAL out14Sg,andFf13OutShftSg,ff14InSg,ff14OutSg: STD_LOGIC;
	SIGNAL out13Sg,andFf12OutShftSg,ff13InSg,ff13OutSg: STD_LOGIC;
	SIGNAL out12Sg,andFf11OutShftSg,ff12InSg,ff12OutSg: STD_LOGIC;
	SIGNAL out11Sg,andFf10OutShftSg,ff11InSg,ff11OutSg: STD_LOGIC;
	SIGNAL out10Sg,andFf09OutShftSg,ff10InSg,ff10OutSg: STD_LOGIC;
	SIGNAL out09Sg,andFf08OutShftSg,ff09InSg,ff09OutSg: STD_LOGIC;
	SIGNAL out08Sg,andFf07OutShftSg,ff08InSg,ff08OutSg: STD_LOGIC;
	SIGNAL out07Sg,andFf06OutShftSg,ff07InSg,ff07OutSg: STD_LOGIC;
	SIGNAL out06Sg,andFf05OutshftSg,ff06InSg,ff06OutSg: STD_LOGIC;
	SIGNAL out05Sg,andFf04OutShftSg,ff05InSg,ff05OutSg: STD_LOGIC;
	SIGNAL out04Sg,andFf03OutShftSg,ff04InSg,ff04OutSg: STD_LOGIC;
	SIGNAL out03Sg,andFf02OutShftSg,ff03InSg,ff03OutSg: STD_LOGIC;
	SIGNAL out02Sg,andFf01OutShftSg,ff02InSg,ff02OutSg: STD_LOGIC;
	SIGNAL out01Sg,andFf00OutShftSg,ff01InSg,ff01OutSg: STD_LOGIC;
	SIGNAL ff00InSg,ff00OutSg: STD_LOGIC;
	
	COMPONENT flipFlopDPET
    PORT (clk, D: IN STD_LOGIC;
          nSet, nRst: IN STD_LOGIC;
          Q, nQ: OUT STD_LOGIC);
  END COMPONENT;
  
  COMPONENT gateAnd2
    PORT (x1, x2: IN STD_LOGIC;
          y:      OUT STD_LOGIC);
  END COMPONENT;
  
  COMPONENT gateOr2
	  PORT (x1, x2: IN STD_LOGIC;
			  y:      OUT STD_LOGIC);
	END COMPONENT;

BEGIN
	
	inverSg <= NOT shift;
	
and01: gateAnd2 PORT MAP (inverSg,dataPsIn(15),out23Sg);
and02: gateAnd2 PORT MAP (shift,ff22OutSg,andFf22OutShftSg);
and03: gateAnd2 PORT MAP (inverSg,dataPsIn(14),out22Sg);
and04: gateAnd2 PORT MAP (shift,ff21OutSg,andFf21OutshftSg);
and05: gateAnd2 PORT MAP (inverSg,dataPsIn(13),out21Sg);
and06: gateAnd2 PORT MAP (shift,ff20OutSg,andFf20OutShftSg);
and07: gateAnd2 PORT MAP (inverSg,dataPsIn(12),out20Sg);
and08: gateAnd2 PORT MAP (shift,ff19OutSg,andFf19OutShftSg);
and09: gateAnd2 PORT MAP (inverSg,dataPsIn(11),out19Sg);
and10: gateAnd2 PORT MAP (shift,ff18OutSg,andFf18OutShftSg);
and11: gateAnd2 PORT MAP (inverSg,dataPsIn(10),out18Sg);
and12: gateAnd2 PORT MAP (shift,ff17OutSg,andFf17OutShftSg);
and13: gateAnd2 PORT MAP (inverSg,dataPsIn(9),out17Sg);
and14: gateAnd2 PORT MAP (shift,ff16OutSg,andFf16OutShftSg);
and15: gateAnd2 PORT MAP (inverSg,dataPsIn(8),out16Sg);
and16: gateAnd2 PORT MAP (shift,ff15OutSg,andFf15OutshftSg);
and17: gateAnd2 PORT MAP (inverSg,dataPsIn(7),out15Sg);
and18: gateAnd2 PORT MAP (shift,ff14OutSg,andFf14OutShftSg);
and19: gateAnd2 PORT MAP (inverSg,dataPsIn(6),out14Sg);
and20: gateAnd2 PORT MAP (shift,ff13OutSg,andFf13OutShftSg);
and21: gateAnd2 PORT MAP (inverSg,dataPsIn(5),out13Sg);
and22: gateAnd2 PORT MAP (shift,ff12OutSg,andFf12OutShftSg);
and23: gateAnd2 PORT MAP (inverSg,dataPsIn(4),out12Sg);
and24: gateAnd2 PORT MAP (shift,ff11OutSg,andFf11OutShftSg);
and25: gateAnd2 PORT MAP (inverSg,dataPsIn(3),out11Sg);
and26: gateAnd2 PORT MAP (shift,ff10OutSg,andFf10OutShftSg);
and27: gateAnd2 PORT MAP (inverSg,dataPsIn(2),out10Sg);
and28: gateAnd2 PORT MAP (shift,ff09OutSg,andFf09OutShftSg);
and29: gateAnd2 PORT MAP (inverSg,dataPsIn(1),out09Sg);
and30: gateAnd2 PORT MAP (shift,ff08OutSg,andFf08OutShftSg);
and31: gateAnd2 PORT MAP (inverSg,dataPsIn(0),out08Sg);
and32: gateAnd2 PORT MAP (shift,ff07OutSg,andFf07OutShftSg);
and33: gateAnd2 PORT MAP (inverSg,datalsIn(7),out07Sg);
and34: gateAnd2 PORT MAP (shift,ff06OutSg,andFf06OutShftSg);
and35: gateAnd2 PORT MAP (inverSg,datalsIn(6),out06Sg);
and36: gateAnd2 PORT MAP (shift,ff05OutSg,andFf05OutShftSg);
and37: gateAnd2 PORT MAP (inverSg,datalsIn(5),out05Sg);
and38: gateAnd2 PORT MAP (shift,ff04OutSg,andFf04OutshftSg);
and39: gateAnd2 PORT MAP (inverSg,datalsIn(4),out04Sg);
and40: gateAnd2 PORT MAP (shift,ff03OutSg,andFf03OutShftSg);
and41: gateAnd2 PORT MAP (inverSg,datalsIn(3),out03Sg);
and42: gateAnd2 PORT MAP (shift,ff02OutSg,andFf02OutShftSg);
and43: gateAnd2 PORT MAP (inverSg,datalsIn(2),out02Sg);
and44: gateAnd2 PORT MAP (shift,ff01OutSg,andFf01OutShftSg);
and45: gateAnd2 PORT MAP (inverSg,datalsIn(1),out01Sg);
and46: gateAnd2 PORT MAP (shift,ff00OutSg,andFf00OutShftSg);
and47: gateAnd2 PORT MAP (inverSg,datalsIn(0),ff00InSg);



or01: gateOr2 PORT MAP (out23Sg,andFf22OutShftSg,ff23InSg);
or02: gateOr2 PORT MAP (out22Sg,andFf21OutshftSg,ff22InSg);
or03: gateOr2 PORT MAP (out21Sg,andFf20OutShftSg,ff21InSg);
or04: gateOr2 PORT MAP (out20Sg,andFf19OutShftSg,ff20InSg);
or05: gateOr2 PORT MAP (out19Sg,andFf18OutShftSg,ff19InSg);
or06: gateOr2 PORT MAP (out18Sg,andFf17OutShftSg,ff18InSg);
or07: gateOr2 PORT MAP (out17Sg,andFf16OutShftSg,ff17InSg);
or08: gateOr2 PORT MAP (out16Sg,andFf15OutshftSg,ff16InSg);
or09: gateOr2 PORT MAP (out15Sg,andFf14OutShftSg,ff15InSg);
or10: gateOr2 PORT MAP (out14Sg,andFf13OutShftSg,ff14InSg);
or11: gateOr2 PORT MAP (out13Sg,andFf12OutShftSg,ff13InSg);
or12: gateOr2 PORT MAP (out12Sg,andFf11OutShftSg,ff12InSg);
or13: gateOr2 PORT MAP (out11Sg,andFf10OutShftSg,ff11InSg);
or14: gateOr2 PORT MAP (out10Sg,andFf09OutShftSg,ff10InSg);
or15: gateOr2 PORT MAP (out09Sg,andFf08OutShftSg,ff09InSg);
or16: gateOr2 PORT MAP (out08Sg,andFf07OutShftSg,ff08InSg);
or17: gateOr2 PORT MAP (out07Sg,andFf06OutShftSg,ff07InSg);
or18: gateOr2 PORT MAP (out06Sg,andFf05OutshftSg,ff06InSg);
or19: gateOr2 PORT MAP (out05Sg,andFf04OutShftSg,ff05InSg);
or20: gateOr2 PORT MAP (out04Sg,andFf03OutShftSg,ff04InSg);
or21: gateOr2 PORT MAP (out03Sg,andFf02OutShftSg,ff03InSg);
or22: gateOr2 PORT MAP (out02Sg,andFf01OutShftSg,ff02InSg);
or23: gateOr2 PORT MAP (out01Sg,andFf00OutShftSg,ff01InSg);



ff00: flipFlopDPET PORT MAP (clk, ff00InSg, '1', nRst,ff00OutSg);
ff01: flipFlopDPET PORT MAP (clk, ff01InSg, '1', nRst,ff01OutSg);
ff02: flipFlopDPET PORT MAP (clk, ff02InSg, '1', nRst,ff02OutSg);
ff03: flipFlopDPET PORT MAP (clk, ff03InSg, '1', nRst,ff03OutSg);
ff04: flipFlopDPET PORT MAP (clk, ff04InSg, '1', nRst,ff04OutSg);
ff05: flipFlopDPET PORT MAP (clk, ff05InSg, '1', nRst,ff05OutSg);
ff06: flipFlopDPET PORT MAP (clk, ff06InSg, '1', nRst,ff06OutSg);
ff07: flipFlopDPET PORT MAP (clk, ff07InSg, '1', nRst,ff07OutSg);
ff08: flipFlopDPET PORT MAP (clk, ff08InSg, '1', nRst,ff08OutSg);
ff09: flipFlopDPET PORT MAP (clk, ff09InSg, '1', nRst,ff09OutSg);
ff10: flipFlopDPET PORT MAP (clk, ff10InSg, '1', nRst,ff10OutSg);
ff11: flipFlopDPET PORT MAP (clk, ff11InSg, '1', nRst,ff11OutSg);
ff12: flipFlopDPET PORT MAP (clk, ff12InSg, '1', nRst,ff12OutSg);
ff13: flipFlopDPET PORT MAP (clk, ff13InSg, '1', nRst,ff13OutSg);
ff14: flipFlopDPET PORT MAP (clk, ff14InSg, '1', nRst,ff14OutSg);
ff15: flipFlopDPET PORT MAP (clk, ff15InSg, '1', nRst,ff15OutSg);
ff16: flipFlopDPET PORT MAP (clk, ff16InSg, '1', nRst,ff16OutSg);
ff17: flipFlopDPET PORT MAP (clk, ff17InSg, '1', nRst,ff17OutSg);
ff18: flipFlopDPET PORT MAP (clk, ff18InSg, '1', nRst,ff18OutSg);
ff19: flipFlopDPET PORT MAP (clk, ff19InSg, '1', nRst,ff19OutSg);
ff20: flipFlopDPET PORT MAP (clk, ff20InSg, '1', nRst,ff20OutSg);
ff21: flipFlopDPET PORT MAP (clk, ff21InSg, '1', nRst,ff21OutSg);
ff22: flipFlopDPET PORT MAP (clk, ff22InSg, '1', nRst,ff22OutSg);
ff23: flipFlopDPET PORT MAP (clk, ff23InSg, '1', nRst, Q);

END structure;