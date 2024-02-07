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
-- Generated on "11/19/2023 12:33:35"
                                                             
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
SIGNAL clk : STD_LOGIC;
SIGNAL dataIn : STD_LOGIC_VECTOR(15 DOWNTO 0);
SIGNAL nGRst : STD_LOGIC;
SIGNAL resto : STD_LOGIC_VECTOR(23 DOWNTO 0);
SIGNAL shf : STD_LOGIC;
COMPONENT EncoderCRC8
	PORT (
	clk : IN STD_LOGIC;
	dataIn : IN STD_LOGIC_VECTOR(15 DOWNTO 0);
	nGRst : IN STD_LOGIC;
	resto : OUT STD_LOGIC_VECTOR(23 DOWNTO 0);
	shf : IN STD_LOGIC
	);
END COMPONENT;
BEGIN
	i1 : EncoderCRC8
	PORT MAP (
-- list connections between master ports and signals
	clk => clk,
	dataIn => dataIn,
	nGRst => nGRst,
	resto => resto,
	shf => shf
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

-- shf
t_prcs_shf: PROCESS
BEGIN
	shf <= '0';
	WAIT FOR 80000 ps;
	shf <= '1';
WAIT;
END PROCESS t_prcs_shf;
-- dataIn[15]
t_prcs_dataIn_15: PROCESS
BEGIN
	dataIn(15) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_15;
-- dataIn[14]
t_prcs_dataIn_14: PROCESS
BEGIN
	dataIn(14) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_14;
-- dataIn[13]
t_prcs_dataIn_13: PROCESS
BEGIN
	dataIn(13) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_13;
-- dataIn[12]
t_prcs_dataIn_12: PROCESS
BEGIN
	dataIn(12) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_12;
-- dataIn[11]
t_prcs_dataIn_11: PROCESS
BEGIN
	dataIn(11) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_11;
-- dataIn[10]
t_prcs_dataIn_10: PROCESS
BEGIN
	dataIn(10) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_10;
-- dataIn[9]
t_prcs_dataIn_9: PROCESS
BEGIN
	dataIn(9) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_9;
-- dataIn[8]
t_prcs_dataIn_8: PROCESS
BEGIN
	dataIn(8) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_8;
-- dataIn[7]
t_prcs_dataIn_7: PROCESS
BEGIN
	dataIn(7) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_7;
-- dataIn[6]
t_prcs_dataIn_6: PROCESS
BEGIN
	dataIn(6) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_6;
-- dataIn[5]
t_prcs_dataIn_5: PROCESS
BEGIN
	dataIn(5) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_5;
-- dataIn[4]
t_prcs_dataIn_4: PROCESS
BEGIN
	dataIn(4) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_4;
-- dataIn[3]
t_prcs_dataIn_3: PROCESS
BEGIN
	dataIn(3) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_3;
-- dataIn[2]
t_prcs_dataIn_2: PROCESS
BEGIN
	dataIn(2) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_2;
-- dataIn[1]
t_prcs_dataIn_1: PROCESS
BEGIN
	dataIn(1) <= '1';
WAIT;
END PROCESS t_prcs_dataIn_1;
-- dataIn[0]
t_prcs_dataIn_0: PROCESS
BEGIN
	dataIn(0) <= '0';
WAIT;
END PROCESS t_prcs_dataIn_0;
END EncoderCRC8_arch;
