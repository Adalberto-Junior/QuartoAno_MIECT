LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY gateXOR2 IS
    PORT(x0,x1: IN STD_LOGIC;
	      y: out STD_LOGIC);
END gateXOR2;

ARCHITECTURE logicFunction OF gateXOR2 IS
BEGIN
   y <= x0 XOR x1;
END logicFunction;

--------------------------------
LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY gateXOR4 IS
    PORT(x0,x1,x2,x3: IN STD_LOGIC;
	      y: out STD_LOGIC);
END gateXOR4;

ARCHITECTURE logicFunction OF gateXOR4 IS
BEGIN
   y <= x0 XOR x1 XOR x2 XOR x3;
END logicFunction;