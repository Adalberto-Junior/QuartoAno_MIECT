LIBRARY ieee;
USE ieee.std_logic_1164.all;
LIBRARY simpleLogic;
USE simpleLogic.all;


ENTITY encoderCRC8 IS
	PORT (an: IN STD_LOGIC_VECTOR(23 DOWNTO 0);
	      error: OUT STD_LOGIC);
END encoderCRC8;

ARCHITECTURE structure OF encoderCRC8 IS
    --C0 <= a0 xor a1; C3 <= a3 xor a4 xor a5 xor a6; ....
	 
    SIGNAL iSg0:STD_LOGIC_VECTOR (7 DOWNTO 0);
	 SIGNAL iSg1:STD_LOGIC_VECTOR (7 DOWNTO 0);
	 SIGNAL iSg2:STD_LOGIC_VECTOR (7 DOWNTO 0);
	 SIGNAL iSg3:STD_LOGIC_VECTOR (1 DOWNTO 0);
	 SIGNAL resto:STD_LOGIC_VECTOR (3 DOWNTO 0);
	 
	 COMPONENT gateXOR2 
	    PORT (x0,x1: IN STD_LOGIC;
		       y: OUT STD_LOGIC);
	 END COMPONENT;
	 COMPONENT gateXOR4
	    PORT (x0,x1,x2,x3: IN STD_LOGIC;
		       y: OUT STD_LOGIC);
	 END COMPONENT;
BEGIN
c0: gateXOR2 PORT MAP(an(8),an(9),iSg0(0));
c1: gateXOR2 PORT MAP(an(9),an(10),iSg0(1));
c2: gateXOR2 PORT MAP(an(10),an(12),iSg0(2));
c3: gateXOR4 PORT MAP(an(11),an(12),an(13),an(14),iSg0(3));
c37:gateXOR2 PORT MAP(an(15),iSg0(1),iSg0(4));
c5: gateXOR2 PORT MAP(an(13),an(15),iSg0(5));
c6: gateXOR2 PORT MAP(an(14),an(16),iSg0(6));
c13: gateXOR2 PORT MAP(an(21),an(23),iSg0(7));

x7a: gateXOR4 PORT MAP(iSg0(0),an(11),iSg0(2),an(20),iSg1(0));
x7b: gateXOR2 PORT MAP(iSg0(7),an(21),iSg1(1));
x6: gateXOR4 PORT MAP(iSg0(3),iSg0(4),an(19),iSg0(7),iSg1(2));
x5a:gateXOR4 PORT MAP(iSg0(0),iSg0(2),iSg0(1),an(18),iSg1(3));
x5b: gateXOR2 PORT MAP(iSg1(2),an(20),iSg1(4));
x5c: gateXOR2 PORT MAP(iSg1(3),an(22),iSg1(5));
x4a: gateXOR4 PORT MAP(iSg0(5),an(15),an(17),an(19),iSg1(6));
x4b: gateXOR2 PORT MAP(iSg1(6),an(20),iSg1(7));
x4c: gateXOR2 PORT MAP(iSg1(7),an(23),iSg2(0));

x3a:  gateXOR4 PORT MAP(an(9),an(11),iSg0(6),an(14),iSg2(1));
x3b:  gateXOR4 PORT MAP(iSg2(1),an(18),an(19),an(22),iSg2(2));
x2a:  gateXOR4 PORT MAP(an(8),an(10),iSg0(2),an(17),iSg2(3));
x2b: gateXOR2 PORT MAP(iSg2(3),an(18),iSg2(4));
x2c: gateXOR2 PORT MAP(iSg2(4),an(21),iSg2(5));
xa: gateXOR4 PORT MAP(an(8),iSg0(4),an(16),an(17),iSg2(6));
xb: gateXOR2 PORT MAP(iSg2(6),iSg0(7),iSg2(7));
x0a:gateXOR4 PORT MAP(iSg0(0),iSg0(5),iSg0(6),an(21),iSg3(0));
x0b: gateXOR2 PORT MAP(iSg3(0),an(22),iSg3(1));

---Comparação:
--rx são blocos de xor feitos entre o resto recebido com o resto calculado

r0: gateXOR4 PORT MAP(iSg1(1),iSg1(2),iSg0(5),iSg2(0),resto(0));
r1: gateXOR4 PORT MAP(iSg2(2),iSg2(5),iSg2(5),iSg3(1),resto(1));
r2: gateXOR4 PORT MAP(an(0),an(1),an(2),an(3),resto(2));
r3: gateXOR4 PORT MAP(an(4),an(5),an(6),an(7),resto(3));
err:gateXOR4 PORT MAP(resto(0),resto(1),resto(2),resto(3),error);

END structure;




   
	 