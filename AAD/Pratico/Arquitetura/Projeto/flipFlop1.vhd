LIBRARY IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.NUMERIC_STD.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.NUMERIC_STD.all;

ENTITY Shift_divident IS
  PORT (clk, nRst:     IN STD_LOGIC;
			nowt: IN STD_LOGIC_VECTOR(4 downto 0);
        input: IN STD_LOGIC_VECTOR(23 downto 0);
        oute:      OUT STD_LOGIC;
		  nexte: OUT STD_LOGIC_VECTOR(4 downto 0));
END Shift_divident;

ARCHITECTURE behavior OF Shift_divident IS
BEGIN
  PROCESS (clk, nowt)
  BEGIN
    IF (nRst = '1')
	    THEN nexte <= "00000";
				oute <= '0';
		ELSIF (clk = '1') AND (clk'EVENT)
			then if (nowt+"00001" /= "00000")
					then oute <= input(IEEE.NUMERIC_STD.to_integer(IEEE.NUMERIC_STD.unsigned(nowt)));
					nexte <= nowt-"00001";

		 END IF;
	 END IF;
  END PROCESS;
END behavior;

