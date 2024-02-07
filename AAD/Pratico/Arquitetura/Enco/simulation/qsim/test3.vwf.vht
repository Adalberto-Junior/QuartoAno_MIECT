-- Copyright (C) 2017  Intel Corporation. All rights reserved.
-- Your use of Intel Corporation's design tools, logic functions 
-- and other software and tools, and its AMPP partner logic 
-- functions, and any output files from any of the foregoing 
-- (including device programming or simulation files), and any 
-- associated documentation or information are expressly subject 
-- to the terms and conditions of the Intel Program License 
-- Subscription Agreement, the Intel Quartus Prime License Agreement,
-- the Intel MegaCore Function License Agreement, or other 
-- applicable license agreement, including, without limitation, 
-- that your use is for the sole purpose of programming logic 
-- devices manufactured by Intel and sold by Intel or its 
-- authorized distributors.  Please refer to the applicable 
-- agreement for further details.

-- *****************************************************************************
-- This file contains a Vhdl test bench with test vectors .The test vectors     
-- are exported from a vector file in the Quartus Waveform Editor and apply to  
-- the top level entity of the current Quartus project .The user can use this   
-- testbench to simulate his design using a third-party simulation tool .       
-- *****************************************************************************
-- Generated on "11/18/2023 06:31:48"
                                                             
-- Vhdl Test Bench(with test vectors) for design  :          EncoderCRC8
-- 
-- Simulation tool : 3rd Party
-- 

LIBRARY ieee;                                               
USE ieee.std_logic_1164.all;                                

ENTITY EncoderCRC8_vhd_vec_tst IS
END EncoderCRC8_vhd_vec_tst;
ARCHITECTURE EncoderCRC8_arch OF EncoderCRC8_vhd_vec_tst IS
-- constants                                                 
-- signals                                                   
SIGNAL a : STD_LOGIC;
SIGNAL ar : STD_LOGIC_VECTOR(7 DOWNTO 0);
SIGNAL clk : STD_LOGIC;
SIGNAL nGRst : STD_LOGIC;
COMPONENT EncoderCRC8
	PORT (
	a : IN STD_LOGIC;
	ar : OUT STD_LOGIC_VECTOR(7 DOWNTO 0);
	clk : IN STD_LOGIC;
	nGRst : IN STD_LOGIC
	);
END COMPONENT;
BEGIN
	i1 : EncoderCRC8
	PORT MAP (
-- list connections between master ports and signals
	a => a,
	ar => ar,
	clk => clk,
	nGRst => nGRst
	);

-- nGRst
t_prcs_nGRst: PROCESS
BEGIN
	nGRst <= '0';
	WAIT FOR 40000 ps;
	nGRst <= '1';
WAIT;
END PROCESS t_prcs_nGRst;

-- clk
t_prcs_clk: PROCESS
BEGIN
LOOP
	clk <= '0';
	WAIT FOR 20000 ps;
	clk <= '1';
	WAIT FOR 20000 ps;
	IF (NOW >= 2000000 ps) THEN WAIT; END IF;
END LOOP;
END PROCESS t_prcs_clk;

-- a
t_prcs_a: PROCESS
BEGIN
	a <= '0';
	WAIT FOR 60000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 20000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 140000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 20000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 20000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 60000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 20000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 100000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
	WAIT FOR 20000 ps;
	a <= '1';
	WAIT FOR 20000 ps;
	a <= '0';
WAIT;
END PROCESS t_prcs_a;
END EncoderCRC8_arch;
