LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY control;
USE control.all;

LIBRARY storeDev;
USE storeDev.all;

LIBRARY arithmetic;
USE arithmetic.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY checker IS
	PORT (nGRst: IN STD_LOGIC;
         clk  : IN STD_LOGIC;
			dataIn: IN STD_LOGIC;
	      error   : OUT STD_LOGIC);
END checker;

ARCHITECTURE structure OF checker IS

	SIGNAL restInSg : STD_LOGIC_VECTOR(8 DOWNTO 0);
	SIGNAL restInternoSg : STD_LOGIC_VECTOR(7 DOWNTO 0);
	SIGNAL newASg : STD_LOGIC_VECTOR(8 DOWNTO 0);
	SIGNAL dividendo: STD_LOGIC_VECTOR(8 DOWNTO 0); 
	SIGNAL stat:  STD_LOGIC_VECTOR (5 DOWNTO 0);
	SIGNAL datalesInSg : STD_LOGIC_VECTOR(7 DOWNTO 0); 
	SIGNAL tCntI: STD_LOGIC_VECTOR (4 DOWNTO 0);
	SIGNAL tCntO: STD_LOGIC_VECTOR (5 DOWNTO 0);
	SIGNAL iNSet, iNRst,adin, clkO: STD_LOGIC;
	SIGNAL ri1kSg: STD_LOGIC_VECTOR(8 DOWNTO 0);
	SIGNAL rikSg : STD_LOGIC_VECTOR(7 DOWNTO 0); 
		
	COMPONENT block_of_type_1 IS
     PORT (ri1_k: IN STD_LOGIC_VECTOR(8 DOWNTO 0);
	        ri_k : OUT STD_LOGIC_VECTOR(7 DOWNTO 0));
	END COMPONENT;
	COMPONENT parReg_9bit IS
		PORT (nRst: IN STD_LOGIC;
				clk: IN STD_LOGIC;
				D0: IN STD_LOGIC;
				D: IN STD_LOGIC_VECTOR (7 DOWNTO 0);
				Q: OUT STD_LOGIC_VECTOR (8 DOWNTO 0));
	END COMPONENT;
	COMPONENT shiftRegist_24bit IS 
		PORT (nRst: IN STD_LOGIC;
				clk: IN STD_LOGIC;
				shift: IN STD_LOGIC;
				dataPsIn: IN STD_LOGIC_VECTOR(15 DOWNTO 0);
				datalsIn: IN STD_LOGIC_VECTOR(7 DOWNTO 0);
				Q:		OUT STD_LOGIC);
	END COMPONENT;	
	COMPONENT parReg_5bit
    PORT (nRst: IN STD_LOGIC;
          clk: IN STD_LOGIC;
          D: IN STD_LOGIC_VECTOR (4 DOWNTO 0);
          Q: OUT STD_LOGIC_VECTOR (4 DOWNTO 0));
  END COMPONENT;
  
  COMPONENT parReg_24bit
     PORT (nSet: IN STD_LOGIC;
			  clk: IN STD_LOGIC;
			  D: IN STD_LOGIC_VECTOR (15 DOWNTO 0);
			  R: IN STD_LOGIC_VECTOR (7 DOWNTO 0);
			  Q: OUT STD_LOGIC_VECTOR (23 DOWNTO 0));
  END COMPONENT;
  
  COMPONENT halfAdder_5bit
    PORT (a:  IN STD_LOGIC_VECTOR (4 DOWNTO 0);
          cI: IN STD_LOGIC;
          s:  OUT STD_LOGIC_VECTOR (4 DOWNTO 0);
          cO: OUT STD_LOGIC);
  END COMPONENT;	
	COMPONENT control IS
		PORT (nGRst: IN STD_LOGIC;
			  clk:   IN STD_LOGIC;
			  add:   IN STD_LOGIC_VECTOR (5 DOWNTO 0);
			  nRst:  OUT STD_LOGIC;
			  nSetO: OUT STD_LOGIC;
			  clkO:  OUT STD_LOGIC);
   END COMPONENT;	
	COMPONENT binCounter_6bit
    PORT (nRst: IN STD_LOGIC;
          clk:  IN STD_LOGIC;
          c:    OUT STD_LOGIC_VECTOR (5 DOWNTO 0));
  END COMPONENT;
  COMPONENT flipFlopDPET IS
	  PORT (clk, D:     IN STD_LOGIC;
			  nSet, nRst: IN STD_LOGIC;
			  Q, nQ:      OUT STD_LOGIC);
	END COMPONENT;
	
BEGIN
ff: flipFlopDPET PORT MAP(clk,dataIn,'1',nGRst,adin);

--shfR: shiftRegist_24bit PORT MAP(nGRst,clk,shift,dataIn,datalesInSg,adin);	  
par9:  parReg_9bit PORT MAP(nGRst,clk,adin,restInternoSg,dividendo);
div: block_of_type_1 PORT MAP(dividendo,restInternoSg);

hd:  halfAdder_5bit PORT MAP (tCntI, '1', tCntO(4 DOWNTO 0), tCntO(5));
pr5: parReg_5bit PORT MAP (iNRst, clk, tCntO(4 DOWNTO 0), tCntI);
--pr6: parReg_24bit PORT MAP (iNSet, clkO, dataIn, restInternoSg,resto);
ff2: flipFlopDPET PORT MAP(clkO,adin,INSet,'1',resto);
bc:  binCounter_6bit PORT MAP (iNRst, clk, stat);
con: control  PORT MAP (nGRst, clk, stat, iNRst, INSet, clkO);

END structure;