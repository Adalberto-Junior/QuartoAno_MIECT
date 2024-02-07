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
