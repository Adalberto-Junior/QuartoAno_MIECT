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
-- Generated on "11/21/2023 06:51:11"
                                                             
-- Vhdl Test Bench(with test vectors) for design  :          checker
-- 
-- Simulation tool : 3rd Party
-- 

LIBRARY ieee;                                               
USE ieee.std_logic_1164.all;                                

ENTITY checker_vhd_vec_tst IS
END checker_vhd_vec_tst;
ARCHITECTURE checker_arch OF checker_vhd_vec_tst IS
-- constants                                                 
-- signals                                                   
SIGNAL an : STD_LOGIC_VECTOR(23 DOWNTO 0);
SIGNAL error : STD_LOGIC;
COMPONENT checker
	PORT (
	an : IN STD_LOGIC_VECTOR(23 DOWNTO 0);
	error : OUT STD_LOGIC
	);
END COMPONENT;
BEGIN
	i1 : checker
	PORT MAP (
-- list connections between master ports and signals
	an => an,
	error => error
	);
-- an[23]
t_prcs_an_23: PROCESS
BEGIN
	an(23) <= '0';
WAIT;
END PROCESS t_prcs_an_23;
-- an[22]
t_prcs_an_22: PROCESS
BEGIN
	an(22) <= '0';
WAIT;
END PROCESS t_prcs_an_22;
-- an[21]
t_prcs_an_21: PROCESS
BEGIN
	an(21) <= '0';
WAIT;
END PROCESS t_prcs_an_21;
-- an[20]
t_prcs_an_20: PROCESS
BEGIN
	an(20) <= '0';
WAIT;
END PROCESS t_prcs_an_20;
-- an[19]
t_prcs_an_19: PROCESS
BEGIN
	an(19) <= '0';
WAIT;
END PROCESS t_prcs_an_19;
-- an[18]
t_prcs_an_18: PROCESS
BEGIN
	an(18) <= '0';
WAIT;
END PROCESS t_prcs_an_18;
-- an[17]
t_prcs_an_17: PROCESS
BEGIN
	an(17) <= '0';
WAIT;
END PROCESS t_prcs_an_17;
-- an[16]
t_prcs_an_16: PROCESS
BEGIN
	an(16) <= '0';
WAIT;
END PROCESS t_prcs_an_16;
-- an[15]
t_prcs_an_15: PROCESS
BEGIN
	an(15) <= '1';
WAIT;
END PROCESS t_prcs_an_15;
-- an[14]
t_prcs_an_14: PROCESS
BEGIN
	an(14) <= '0';
WAIT;
END PROCESS t_prcs_an_14;
-- an[13]
t_prcs_an_13: PROCESS
BEGIN
	an(13) <= '1';
WAIT;
END PROCESS t_prcs_an_13;
-- an[12]
t_prcs_an_12: PROCESS
BEGIN
	an(12) <= '0';
WAIT;
END PROCESS t_prcs_an_12;
-- an[11]
t_prcs_an_11: PROCESS
BEGIN
	an(11) <= '0';
WAIT;
END PROCESS t_prcs_an_11;
-- an[10]
t_prcs_an_10: PROCESS
BEGIN
	an(10) <= '0';
WAIT;
END PROCESS t_prcs_an_10;
-- an[9]
t_prcs_an_9: PROCESS
BEGIN
	an(9) <= '1';
WAIT;
END PROCESS t_prcs_an_9;
-- an[8]
t_prcs_an_8: PROCESS
BEGIN
	an(8) <= '0';
WAIT;
END PROCESS t_prcs_an_8;
-- an[7]
t_prcs_an_7: PROCESS
BEGIN
	an(7) <= '0';
WAIT;
END PROCESS t_prcs_an_7;
-- an[6]
t_prcs_an_6: PROCESS
BEGIN
	an(6) <= '0';
WAIT;
END PROCESS t_prcs_an_6;
-- an[5]
t_prcs_an_5: PROCESS
BEGIN
	an(5) <= '1';
WAIT;
END PROCESS t_prcs_an_5;
-- an[4]
t_prcs_an_4: PROCESS
BEGIN
	an(4) <= '0';
WAIT;
END PROCESS t_prcs_an_4;
-- an[3]
t_prcs_an_3: PROCESS
BEGIN
	an(3) <= '0';
WAIT;
END PROCESS t_prcs_an_3;
-- an[2]
t_prcs_an_2: PROCESS
BEGIN
	an(2) <= '0';
WAIT;
END PROCESS t_prcs_an_2;
-- an[1]
t_prcs_an_1: PROCESS
BEGIN
	an(1) <= '1';
WAIT;
END PROCESS t_prcs_an_1;
-- an[0]
t_prcs_an_0: PROCESS
BEGIN
	an(0) <= '1';
WAIT;
END PROCESS t_prcs_an_0;
END checker_arch;
