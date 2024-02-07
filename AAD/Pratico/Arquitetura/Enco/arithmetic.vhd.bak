LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY block_of_type_1 IS
     PORT (ri1_k: IN STD_LOGIC_VECTOR(8 DOWNTO 0);
	        ri_k : OUT STD_LOGIC_VECTOR(7 DOWNTO 0));
END block_of_type_1;

ARCHITECTURE structure OF block_of_type_1 IS
	 COMPONENT gateXOR2 
	    PORT (x0,x1: IN STD_LOGIC;
		       y: OUT STD_LOGIC);
	 END COMPONENT;
	 
BEGIN
ri7: gateXOR2 PORT MAP(ri1_k(8),ri1_k(7),ri_k(7));
ri6: ri_k(6) <= ri1_k(6);
ri5: gateXOR2 PORT MAP(ri1_k(8),ri1_k(5),ri_k(5));
ri4: ri_k(4) <= ri1_k(4);
ri3: ri_k(3) <= ri1_k(3);
ri2: gateXOR2 PORT MAP(ri1_k(8),ri1_k(2),ri_k(2));
ri1: gateXOR2 PORT MAP(ri1_k(8),ri1_k(1),ri_k(1));
ri0: gateXOR2 PORT MAP(ri1_k(8),ri1_k(0),ri_k(0));
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY block_of_type_2 IS
     PORT (ri1_k: IN STD_LOGIC_VECTOR(8 DOWNTO 0);
	        ri_kt : OUT STD_LOGIC_VECTOR(7 DOWNTO 0));
END block_of_type_2;

ARCHITECTURE structure OF block_of_type_2 IS
		Signal ri_k: STD_LOGIC_VECTOR(7 DOWNTO 0);
	 COMPONENT gateXOR2 
	    PORT (x0,x1: IN STD_LOGIC;
		       y: OUT STD_LOGIC);
	 END COMPONENT;
	 
BEGIN
ri7: gateXOR2 PORT MAP(ri1_k(8),ri1_k(7),ri_k(7));
ri6: ri_k(6) <= ri1_k(6);
ri5: gateXOR2 PORT MAP(ri1_k(8),ri1_k(5),ri_k(5));
ri4: ri_k(4) <= ri1_k(4);
ri3: ri_k(3) <= ri1_k(3);
ri2: gateXOR2 PORT MAP(ri1_k(8),ri1_k(2),ri_k(2));
ri1: gateXOR2 PORT MAP(ri1_k(8),ri1_k(1),ri_k(1));
ri0: ri_k(0) <= ri1_k(8); --gateXOR2 PORT MAP(ri1_k(8),ri1_k(0),ri_k(0));

ri_kt <= ri_k;
END structure;


LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY halfAdder_1bit IS
  PORT (a, cI:  IN STD_LOGIC;
        s, cO:  OUT STD_LOGIC);
END halfAdder_1bit;

ARCHITECTURE structure OF halfAdder_1bit IS
  COMPONENT gateAnd2
    PORT (x1, x2: IN STD_LOGIC;
          y: OUT STD_LOGIC);
  END COMPONENT;
  COMPONENT gateXOr2
    PORT (x1, x2: IN STD_LOGIC;
          y: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  xor20: gateXOr2 PORT MAP (cI, a, s);
  and20: gateAnd2 PORT MAP (cI, a, cO);
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY halfAdder_5bit IS
  PORT (a:  IN STD_LOGIC_VECTOR (4 DOWNTO 0);
        cI: IN STD_LOGIC;
        s:  OUT STD_LOGIC_VECTOR (4 DOWNTO 0);
        cO: OUT STD_LOGIC);
END halfAdder_5bit;

ARCHITECTURE structure OF halfAdder_5bit IS
  SIGNAL z0, z1, z2, z3: STD_LOGIC;
  COMPONENT halfAdder_1bit
    PORT (a, cI:  IN STD_LOGIC;
          s, cO: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  hA10: halfAdder_1bit PORT MAP (a(0), cI, s(0), z0);
  hA11: halfAdder_1bit PORT MAP (a(1), z0, s(1), z1);
  hA12: halfAdder_1bit PORT MAP (a(2), z1, s(2), z2);
  hA13: halfAdder_1bit PORT MAP (a(3), z2, s(3), z3);
  hA14: halfAdder_1bit PORT MAP (a(4), z3, s(4), cO);
END structure;

LIBRARY ieee;
USE ieee.std_logic_1164.all;

LIBRARY simpleLogic;
USE simpleLogic.all;

ENTITY demux IS
  PORT (a:  IN STD_LOGIC_VECTOR (7 DOWNTO 0);
        sel: IN STD_LOGIC;
		  restoInterno:  OUT STD_LOGIC_VECTOR (7 DOWNTO 0);
        restoFinal:  OUT STD_LOGIC_VECTOR (7 DOWNTO 0)
        );
END demux;

ARCHITECTURE structure OF demux IS
  SIGNAL notSel: STD_LOGIC;
  COMPONENT gateAnd2
    PORT (x1, x2: IN STD_LOGIC;
          y: OUT STD_LOGIC);
  END COMPONENT;
BEGIN
  notSel <= not (sel);	
  SaidaInterna: gateAnd2 PORT MAP(notSel,a,restoInterno);
  Saidafinal: gateAnd2 PORT MAP(sel,a,restoFinal);
END structure;
-------------------

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY divisionBlock IS 
	PORT(aIn					: IN STD_LOGIC_VECTOR (8 DOWNTO 0);
        selIn				: IN STD_LOGIC;
		  restoInternoOut	: OUT STD_LOGIC_VECTOR (7 DOWNTO 0); 
		  restoFinalOut	: OUT STD_LOGIC_VECTOR (7 DOWNTO 0));
END divisionBlock;

ARCHITECTURE structure OF divisionBlock IS
  SIGNAL resto: STD_LOGIC_VECTOR(7 DOWNTO 0)
  COMPONENT block_of_type_1 IS
     PORT (ri1_k: IN STD_LOGIC_VECTOR(8 DOWNTO 0);
	        ri_k : OUT STD_LOGIC_VECTOR(7 DOWNTO 0));
	END COMPONENT;
  
  COMPONENT demux
    PORT (a:  IN STD_LOGIC_VECTOR (7 DOWNTO 0);
        sel: IN STD_LOGIC;
		  restoInterno:  OUT STD_LOGIC_VECTOR (7 DOWNTO 0);
        restoFinal:  OUT STD_LOGIC_VECTOR (7 DOWNTO 0)
        );
  END COMPONENT;
BEGIN
divisao: block_of_type_1 PORT MAP(aIn,resto);
EscolheOut: demux PORT MAP(resto,selIn,restoInternoOut,restoFinalOut);
  	
END structure;
--------------------------------------------

LIBRARY IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.NUMERIC_STD.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.NUMERIC_STD.all;

ENTITY shift_divident IS
  PORT (nRst,clk:     IN STD_LOGIC;
			nowt: IN STD_LOGIC_VECTOR(4 downto 0);
        input: IN STD_LOGIC_VECTOR(23 downto 0);
        oute:      OUT STD_LOGIC;
		  nexte: OUT STD_LOGIC_VECTOR(4 downto 0));
END shift_divident;

ARCHITECTURE behavior OF shift_divident IS
BEGIN
  PROCESS (clk,nRst, nowt)
  BEGIN
    IF (nRst = '1')
	    THEN nexte <= "00000";
				oute <= '0';
		ELSIF (clk = '1') AND (clk'EVENT)
			then if (nowt +"00001" /= "00000")
					then oute <= input(IEEE.NUMERIC_STD.to_integer(IEEE.NUMERIC_STD.unsigned(nowt)));
					nexte <= nowt-"00001";

		 END IF;
	 END IF;
  END PROCESS;
END behavior;

LIBRARY IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.NUMERIC_STD.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.NUMERIC_STD.all;

ENTITY alu IS
  PORT (clk, nRst:     IN STD_LOGIC;
			nowt: IN STD_LOGIC_VECTOR(4 downto 0);
		  nexte: OUT STD_LOGIC_VECTOR(4 downto 0));
END Alu;

ARCHITECTURE behavior OF alu IS
BEGIN
  PROCESS (clk, nowt)
  BEGIN
    IF (nRst = '1')
	    THEN nexte <= "00000";
		ELSIF (clk = '1') AND (clk'EVENT)
				then nexte <= nowt + "00001";

	 END IF;
  END PROCESS;
END behavior;

 

