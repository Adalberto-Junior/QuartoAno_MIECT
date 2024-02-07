LIBRARY IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.NUMERIC_STD.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.NUMERIC_STD.all;

ENTITY flipFlop IS
  PORT (clk, nRst:     IN STD_LOGIC;
        input: IN STD_LOGIC_VECTOR(23 downto 0);
		  original_input: IN STD_LOGIC_VECTOR(23 downto 0);
        oute:      OUT STD_LOGIC;
		  next_num: OUT STD_LOGIC_VECTOR(23 downto 0));
END flipFlop;

ARCHITECTURE behavior OF flipFlop IS
BEGIN
PROCESS (clk, nRst, input, original_input)
  BEGIN
		if (nRst = '1')
		then next_num <= original_input;
		oute <= '0';
		elsif(clk = '1')
		then oute <= input(23);
		next_num <= (input(22 downto 0) & '0');
		end if;
		end process;
END behavior;

