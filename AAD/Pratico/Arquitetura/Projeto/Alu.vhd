LIBRARY IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.NUMERIC_STD.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.NUMERIC_STD.all;

ENTITY Alu IS
  PORT (clk, nRst:     IN STD_LOGIC;
			nowt: IN STD_LOGIC_VECTOR(4 downto 0);
		  nexte: OUT STD_LOGIC_VECTOR(4 downto 0));
END Alu;

ARCHITECTURE behavior OF Alu IS
BEGIN
  PROCESS (clk, nowt)
  BEGIN
    IF (nRst = '1')
	    THEN nexte <= "00000";
		ELSIF (clk = '1')
				then nexte <= nowt+"00001";

	 END IF;
  END PROCESS;
END behavior;