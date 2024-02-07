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

ENTITY EncoderCRC8 IS
	PORT (nGRst: IN STD_LOGIC;
         clk  : IN STD_LOGIC;
		   a    : IN STD_LOGIC;--_VECTOR(15 downto 0);
	      ar   : OUT STD_LOGIC_VECTOR(7 DOWNTO 0));
END EncoderCRC8;

ARCHITECTURE structure OF EncoderCRC8 IS
	SIGNAL restInSg : STD_LOGIC_VECTOR(8 DOWNTO 0);
	SIGNAL restInternoSg : STD_LOGIC_VECTOR(7 DOWNTO 0);
	SIGNAL newASg : STD_LOGIC_VECTOR(8 DOWNTO 0);
	SIGNAL dividendo: STD_LOGIC_VECTOR(8 DOWNTO 0); 
	 SIGNAL stat:  STD_LOGIC_VECTOR (5 DOWNTO 0);
	 --SIGNAL cntIn
	SIGNAL iNSet, iNRst, clkO: STD_LOGIC;
	
	
	SIGNAL ri1kSg: STD_LOGIC_VECTOR(8 DOWNTO 0);
	SIGNAL rikSg : STD_LOGIC_VECTOR(7 DOWNTO 0); 
		
	COMPONENT divisionBlock IS 
		PORT(aIn					: IN STD_LOGIC_VECTOR (8 DOWNTO 0);
			  selIn				: IN STD_LOGIC;
			  restoInternoOut	: OUT STD_LOGIC_VECTOR (7 DOWNTO 0); 
			  restoFinalOut	: OUT STD_LOGIC_VECTOR (7 DOWNTO 0));
	END COMPONENT;
	
	COMPONENT parReg_9bit IS
		PORT (nRst: IN STD_LOGIC;
			  clk: IN STD_LOGIC;
			  D: IN STD_LOGIC_VECTOR (8 DOWNTO 0);
			  Q: OUT STD_LOGIC_VECTOR (8 DOWNTO 0));
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
	
BEGIN
pr: newASg(0) <= a;
ex: restInSg <= restInternoSg & a; --Mudar isso
	 
	 
par9:  parReg_9bit PORT MAP(iNRst,clkO,restInSg,dividendo);
div: divisionBlock PORT MAP(dividendo,'0',restInternoSg,rikSg);
bc:  binCounter_6bit PORT MAP (iNRst, clk, stat);
con: control  PORT MAP (nGRst, clk, stat, iNRst, INSet, clkO);

output: ar <= restInternoSg;
END structure;

