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

-- VENDOR "Altera"
-- PROGRAM "Quartus Prime"
-- VERSION "Version 17.0.0 Build 595 04/25/2017 SJ Lite Edition"

-- DATE "11/27/2023 17:52:57"

-- 
-- Device: Altera 5CGXFC7C7F23C8 Package FBGA484
-- 

-- 
-- This VHDL file should be used for ModelSim-Altera (VHDL) only
-- 

LIBRARY ALTERA;
LIBRARY ALTERA_LNSIM;
LIBRARY CYCLONEV;
LIBRARY IEEE;
USE ALTERA.ALTERA_PRIMITIVES_COMPONENTS.ALL;
USE ALTERA_LNSIM.ALTERA_LNSIM_COMPONENTS.ALL;
USE CYCLONEV.CYCLONEV_COMPONENTS.ALL;
USE IEEE.STD_LOGIC_1164.ALL;

ENTITY 	EncoderCRC8 IS
    PORT (
	nGRst : IN std_logic;
	clk : IN std_logic;
	dataIn : IN std_logic;
	resto : BUFFER std_logic
	);
END EncoderCRC8;

-- Design Ports Information
-- resto	=>  Location: PIN_V21,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- clk	=>  Location: PIN_M16,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- nGRst	=>  Location: PIN_U22,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- dataIn	=>  Location: PIN_V18,	 I/O Standard: 2.5 V,	 Current Strength: Default


ARCHITECTURE structure OF EncoderCRC8 IS
SIGNAL gnd : std_logic := '0';
SIGNAL vcc : std_logic := '1';
SIGNAL unknown : std_logic := 'X';
SIGNAL devoe : std_logic := '1';
SIGNAL devclrn : std_logic := '1';
SIGNAL devpor : std_logic := '1';
SIGNAL ww_devoe : std_logic;
SIGNAL ww_devclrn : std_logic;
SIGNAL ww_devpor : std_logic;
SIGNAL ww_nGRst : std_logic;
SIGNAL ww_clk : std_logic;
SIGNAL ww_dataIn : std_logic;
SIGNAL ww_resto : std_logic;
SIGNAL \~QUARTUS_CREATED_GND~I_combout\ : std_logic;
SIGNAL \clk~input_o\ : std_logic;
SIGNAL \clk~inputCLKENA0_outclk\ : std_logic;
SIGNAL \dataIn~input_o\ : std_logic;
SIGNAL \ff|Q~feeder_combout\ : std_logic;
SIGNAL \nGRst~input_o\ : std_logic;
SIGNAL \ff|Q~q\ : std_logic;
SIGNAL \ff2|Q~feeder_combout\ : std_logic;
SIGNAL \ff2|Q~q\ : std_logic;
SIGNAL \ALT_INV_dataIn~input_o\ : std_logic;
SIGNAL \ff|ALT_INV_Q~q\ : std_logic;

BEGIN

ww_nGRst <= nGRst;
ww_clk <= clk;
ww_dataIn <= dataIn;
resto <= ww_resto;
ww_devoe <= devoe;
ww_devclrn <= devclrn;
ww_devpor <= devpor;
\ALT_INV_dataIn~input_o\ <= NOT \dataIn~input_o\;
\ff|ALT_INV_Q~q\ <= NOT \ff|Q~q\;

-- Location: IOOBUF_X70_Y0_N36
\resto~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \ff2|Q~q\,
	devoe => ww_devoe,
	o => ww_resto);

-- Location: IOIBUF_X89_Y35_N61
\clk~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_clk,
	o => \clk~input_o\);

-- Location: CLKCTRL_G10
\clk~inputCLKENA0\ : cyclonev_clkena
-- pragma translate_off
GENERIC MAP (
	clock_type => "global clock",
	disable_mode => "low",
	ena_register_mode => "always enabled",
	ena_register_power_up => "high",
	test_syn => "high")
-- pragma translate_on
PORT MAP (
	inclk => \clk~input_o\,
	outclk => \clk~inputCLKENA0_outclk\);

-- Location: IOIBUF_X70_Y0_N1
\dataIn~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_dataIn,
	o => \dataIn~input_o\);

-- Location: LABCELL_X70_Y1_N57
\ff|Q~feeder\ : cyclonev_lcell_comb
-- Equation(s):
-- \ff|Q~feeder_combout\ = ( \dataIn~input_o\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000000000000011111111111111111111111111111111",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataf => \ALT_INV_dataIn~input_o\,
	combout => \ff|Q~feeder_combout\);

-- Location: IOIBUF_X70_Y0_N52
\nGRst~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_nGRst,
	o => \nGRst~input_o\);

-- Location: FF_X70_Y1_N58
\ff|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \ff|Q~feeder_combout\,
	clrn => \nGRst~input_o\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \ff|Q~q\);

-- Location: LABCELL_X70_Y1_N54
\ff2|Q~feeder\ : cyclonev_lcell_comb
-- Equation(s):
-- \ff2|Q~feeder_combout\ = ( \ff|Q~q\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000000000000011111111111111111111111111111111",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataf => \ff|ALT_INV_Q~q\,
	combout => \ff2|Q~feeder_combout\);

-- Location: FF_X70_Y1_N56
\ff2|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \ff2|Q~feeder_combout\,
	clrn => \nGRst~input_o\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \ff2|Q~q\);

-- Location: LABCELL_X31_Y35_N0
\~QUARTUS_CREATED_GND~I\ : cyclonev_lcell_comb
-- Equation(s):

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000000000000000000000000000000000000000000000",
	shared_arith => "off")
-- pragma translate_on
;
END structure;


