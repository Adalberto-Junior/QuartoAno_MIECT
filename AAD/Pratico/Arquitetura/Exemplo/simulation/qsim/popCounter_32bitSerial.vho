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

-- DATE "11/19/2023 11:59:15"

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

ENTITY 	popCounter_32bitSerial IS
    PORT (
	nGRst : IN std_logic;
	clk : IN std_logic;
	dIn : IN std_logic;
	cnt : OUT std_logic_vector(5 DOWNTO 0)
	);
END popCounter_32bitSerial;

-- Design Ports Information
-- cnt[0]	=>  Location: PIN_N20,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- cnt[1]	=>  Location: PIN_N19,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- cnt[2]	=>  Location: PIN_M18,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- cnt[3]	=>  Location: PIN_L22,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- cnt[4]	=>  Location: PIN_N16,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- cnt[5]	=>  Location: PIN_N21,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- clk	=>  Location: PIN_M16,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- nGRst	=>  Location: PIN_K17,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- dIn	=>  Location: PIN_L17,	 I/O Standard: 2.5 V,	 Current Strength: Default


ARCHITECTURE structure OF popCounter_32bitSerial IS
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
SIGNAL ww_dIn : std_logic;
SIGNAL ww_cnt : std_logic_vector(5 DOWNTO 0);
SIGNAL \~QUARTUS_CREATED_GND~I_combout\ : std_logic;
SIGNAL \clk~input_o\ : std_logic;
SIGNAL \clk~inputCLKENA0_outclk\ : std_logic;
SIGNAL \bc|ff0|Q~0_combout\ : std_logic;
SIGNAL \nGRst~input_o\ : std_logic;
SIGNAL \bc|ff1|Q~0_combout\ : std_logic;
SIGNAL \bc|ff1|Q~q\ : std_logic;
SIGNAL \bc|ff2|Q~0_combout\ : std_logic;
SIGNAL \bc|ff2|Q~q\ : std_logic;
SIGNAL \bc|ff3|Q~0_combout\ : std_logic;
SIGNAL \bc|ff3|Q~q\ : std_logic;
SIGNAL \bc|ff4|Q~0_combout\ : std_logic;
SIGNAL \bc|ff4|Q~q\ : std_logic;
SIGNAL \bc|ff5|Q~0_combout\ : std_logic;
SIGNAL \bc|ff5|Q~q\ : std_logic;
SIGNAL \con|nad2|y~1_combout\ : std_logic;
SIGNAL \con|nad2|y~2_combout\ : std_logic;
SIGNAL \bc|ff0|Q~q\ : std_logic;
SIGNAL \con|nord|y~0_combout\ : std_logic;
SIGNAL \pr5|ff0|Q~q\ : std_logic;
SIGNAL \dIn~input_o\ : std_logic;
SIGNAL \ff|Q~q\ : std_logic;
SIGNAL \hd|hA10|xor20|y~combout\ : std_logic;
SIGNAL \pr6|ff0|Q~0_combout\ : std_logic;
SIGNAL \con|nad2|y~0_combout\ : std_logic;
SIGNAL \con|nad4|y~0_combout\ : std_logic;
SIGNAL \pr6|ff0|Q~q\ : std_logic;
SIGNAL \pr5|ff1|Q~feeder_combout\ : std_logic;
SIGNAL \pr5|ff1|Q~DUPLICATE_q\ : std_logic;
SIGNAL \hd|hA11|xor20|y~combout\ : std_logic;
SIGNAL \pr6|ff1|Q~0_combout\ : std_logic;
SIGNAL \pr6|ff1|Q~q\ : std_logic;
SIGNAL \pr5|ff2|Q~q\ : std_logic;
SIGNAL \hd|hA12|xor20|y~combout\ : std_logic;
SIGNAL \pr6|ff2|Q~0_combout\ : std_logic;
SIGNAL \pr6|ff2|Q~q\ : std_logic;
SIGNAL \pr5|ff3|Q~q\ : std_logic;
SIGNAL \hd|hA13|xor20|y~combout\ : std_logic;
SIGNAL \pr6|ff3|Q~0_combout\ : std_logic;
SIGNAL \pr6|ff3|Q~q\ : std_logic;
SIGNAL \pr5|ff4|Q~q\ : std_logic;
SIGNAL \hd|hA14|xor20|y~combout\ : std_logic;
SIGNAL \pr6|ff4|Q~0_combout\ : std_logic;
SIGNAL \pr6|ff4|Q~q\ : std_logic;
SIGNAL \pr5|ff2|Q~DUPLICATE_q\ : std_logic;
SIGNAL \pr5|ff3|Q~DUPLICATE_q\ : std_logic;
SIGNAL \pr5|ff1|Q~q\ : std_logic;
SIGNAL \hd|hA14|and20|y~combout\ : std_logic;
SIGNAL \pr6|ff5|Q~q\ : std_logic;
SIGNAL \pr5|ff3|ALT_INV_Q~DUPLICATE_q\ : std_logic;
SIGNAL \pr5|ff2|ALT_INV_Q~DUPLICATE_q\ : std_logic;
SIGNAL \pr5|ff1|ALT_INV_Q~DUPLICATE_q\ : std_logic;
SIGNAL \ALT_INV_nGRst~input_o\ : std_logic;
SIGNAL \ALT_INV_clk~input_o\ : std_logic;
SIGNAL \con|nad2|ALT_INV_y~2_combout\ : std_logic;
SIGNAL \con|nad2|ALT_INV_y~1_combout\ : std_logic;
SIGNAL \hd|hA14|xor20|ALT_INV_y~combout\ : std_logic;
SIGNAL \pr5|ff4|ALT_INV_Q~q\ : std_logic;
SIGNAL \hd|hA13|xor20|ALT_INV_y~combout\ : std_logic;
SIGNAL \pr5|ff3|ALT_INV_Q~q\ : std_logic;
SIGNAL \hd|hA12|xor20|ALT_INV_y~combout\ : std_logic;
SIGNAL \pr5|ff2|ALT_INV_Q~q\ : std_logic;
SIGNAL \hd|hA11|xor20|ALT_INV_y~combout\ : std_logic;
SIGNAL \pr5|ff1|ALT_INV_Q~q\ : std_logic;
SIGNAL \con|nad4|ALT_INV_y~0_combout\ : std_logic;
SIGNAL \bc|ff0|ALT_INV_Q~q\ : std_logic;
SIGNAL \bc|ff4|ALT_INV_Q~q\ : std_logic;
SIGNAL \bc|ff3|ALT_INV_Q~q\ : std_logic;
SIGNAL \con|nad2|ALT_INV_y~0_combout\ : std_logic;
SIGNAL \bc|ff2|ALT_INV_Q~q\ : std_logic;
SIGNAL \bc|ff1|ALT_INV_Q~q\ : std_logic;
SIGNAL \bc|ff5|ALT_INV_Q~q\ : std_logic;
SIGNAL \hd|hA10|xor20|ALT_INV_y~combout\ : std_logic;
SIGNAL \pr5|ff0|ALT_INV_Q~q\ : std_logic;
SIGNAL \ff|ALT_INV_Q~q\ : std_logic;
SIGNAL \pr6|ff5|ALT_INV_Q~q\ : std_logic;
SIGNAL \pr6|ff4|ALT_INV_Q~q\ : std_logic;
SIGNAL \pr6|ff3|ALT_INV_Q~q\ : std_logic;
SIGNAL \pr6|ff2|ALT_INV_Q~q\ : std_logic;
SIGNAL \pr6|ff1|ALT_INV_Q~q\ : std_logic;
SIGNAL \pr6|ff0|ALT_INV_Q~q\ : std_logic;

BEGIN

ww_nGRst <= nGRst;
ww_clk <= clk;
ww_dIn <= dIn;
cnt <= ww_cnt;
ww_devoe <= devoe;
ww_devclrn <= devclrn;
ww_devpor <= devpor;
\pr5|ff3|ALT_INV_Q~DUPLICATE_q\ <= NOT \pr5|ff3|Q~DUPLICATE_q\;
\pr5|ff2|ALT_INV_Q~DUPLICATE_q\ <= NOT \pr5|ff2|Q~DUPLICATE_q\;
\pr5|ff1|ALT_INV_Q~DUPLICATE_q\ <= NOT \pr5|ff1|Q~DUPLICATE_q\;
\ALT_INV_nGRst~input_o\ <= NOT \nGRst~input_o\;
\ALT_INV_clk~input_o\ <= NOT \clk~input_o\;
\con|nad2|ALT_INV_y~2_combout\ <= NOT \con|nad2|y~2_combout\;
\con|nad2|ALT_INV_y~1_combout\ <= NOT \con|nad2|y~1_combout\;
\hd|hA14|xor20|ALT_INV_y~combout\ <= NOT \hd|hA14|xor20|y~combout\;
\pr5|ff4|ALT_INV_Q~q\ <= NOT \pr5|ff4|Q~q\;
\hd|hA13|xor20|ALT_INV_y~combout\ <= NOT \hd|hA13|xor20|y~combout\;
\pr5|ff3|ALT_INV_Q~q\ <= NOT \pr5|ff3|Q~q\;
\hd|hA12|xor20|ALT_INV_y~combout\ <= NOT \hd|hA12|xor20|y~combout\;
\pr5|ff2|ALT_INV_Q~q\ <= NOT \pr5|ff2|Q~q\;
\hd|hA11|xor20|ALT_INV_y~combout\ <= NOT \hd|hA11|xor20|y~combout\;
\pr5|ff1|ALT_INV_Q~q\ <= NOT \pr5|ff1|Q~q\;
\con|nad4|ALT_INV_y~0_combout\ <= NOT \con|nad4|y~0_combout\;
\bc|ff0|ALT_INV_Q~q\ <= NOT \bc|ff0|Q~q\;
\bc|ff4|ALT_INV_Q~q\ <= NOT \bc|ff4|Q~q\;
\bc|ff3|ALT_INV_Q~q\ <= NOT \bc|ff3|Q~q\;
\con|nad2|ALT_INV_y~0_combout\ <= NOT \con|nad2|y~0_combout\;
\bc|ff2|ALT_INV_Q~q\ <= NOT \bc|ff2|Q~q\;
\bc|ff1|ALT_INV_Q~q\ <= NOT \bc|ff1|Q~q\;
\bc|ff5|ALT_INV_Q~q\ <= NOT \bc|ff5|Q~q\;
\hd|hA10|xor20|ALT_INV_y~combout\ <= NOT \hd|hA10|xor20|y~combout\;
\pr5|ff0|ALT_INV_Q~q\ <= NOT \pr5|ff0|Q~q\;
\ff|ALT_INV_Q~q\ <= NOT \ff|Q~q\;
\pr6|ff5|ALT_INV_Q~q\ <= NOT \pr6|ff5|Q~q\;
\pr6|ff4|ALT_INV_Q~q\ <= NOT \pr6|ff4|Q~q\;
\pr6|ff3|ALT_INV_Q~q\ <= NOT \pr6|ff3|Q~q\;
\pr6|ff2|ALT_INV_Q~q\ <= NOT \pr6|ff2|Q~q\;
\pr6|ff1|ALT_INV_Q~q\ <= NOT \pr6|ff1|Q~q\;
\pr6|ff0|ALT_INV_Q~q\ <= NOT \pr6|ff0|Q~q\;

-- Location: IOOBUF_X89_Y35_N79
\cnt[0]~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \pr6|ff0|ALT_INV_Q~q\,
	devoe => ww_devoe,
	o => ww_cnt(0));

-- Location: IOOBUF_X89_Y36_N5
\cnt[1]~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \pr6|ff1|ALT_INV_Q~q\,
	devoe => ww_devoe,
	o => ww_cnt(1));

-- Location: IOOBUF_X89_Y36_N22
\cnt[2]~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \pr6|ff2|ALT_INV_Q~q\,
	devoe => ww_devoe,
	o => ww_cnt(2));

-- Location: IOOBUF_X89_Y36_N56
\cnt[3]~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \pr6|ff3|ALT_INV_Q~q\,
	devoe => ww_devoe,
	o => ww_cnt(3));

-- Location: IOOBUF_X89_Y35_N45
\cnt[4]~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \pr6|ff4|ALT_INV_Q~q\,
	devoe => ww_devoe,
	o => ww_cnt(4));

-- Location: IOOBUF_X89_Y35_N96
\cnt[5]~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \pr6|ff5|ALT_INV_Q~q\,
	devoe => ww_devoe,
	o => ww_cnt(5));

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

-- Location: MLABCELL_X87_Y35_N39
\bc|ff0|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \bc|ff0|Q~0_combout\ = ( !\bc|ff0|Q~q\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111111111111111000000000000000011111111111111110000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datae => \bc|ff0|ALT_INV_Q~q\,
	combout => \bc|ff0|Q~0_combout\);

-- Location: IOIBUF_X89_Y37_N4
\nGRst~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_nGRst,
	o => \nGRst~input_o\);

-- Location: MLABCELL_X87_Y35_N24
\bc|ff1|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \bc|ff1|Q~0_combout\ = ( !\bc|ff1|Q~q\ & ( \bc|ff0|Q~q\ ) ) # ( \bc|ff1|Q~q\ & ( !\bc|ff0|Q~q\ ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000111111111111111111111111111111110000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datae => \bc|ff1|ALT_INV_Q~q\,
	dataf => \bc|ff0|ALT_INV_Q~q\,
	combout => \bc|ff1|Q~0_combout\);

-- Location: FF_X87_Y35_N26
\bc|ff1|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \bc|ff1|Q~0_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \bc|ff1|Q~q\);

-- Location: MLABCELL_X87_Y35_N15
\bc|ff2|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \bc|ff2|Q~0_combout\ = ( \bc|ff2|Q~q\ & ( \bc|ff0|Q~q\ & ( !\bc|ff1|Q~q\ ) ) ) # ( !\bc|ff2|Q~q\ & ( \bc|ff0|Q~q\ & ( \bc|ff1|Q~q\ ) ) ) # ( \bc|ff2|Q~q\ & ( !\bc|ff0|Q~q\ ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000111111111111111100000000111111111111111100000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datad => \bc|ff1|ALT_INV_Q~q\,
	datae => \bc|ff2|ALT_INV_Q~q\,
	dataf => \bc|ff0|ALT_INV_Q~q\,
	combout => \bc|ff2|Q~0_combout\);

-- Location: FF_X87_Y35_N17
\bc|ff2|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \bc|ff2|Q~0_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \bc|ff2|Q~q\);

-- Location: MLABCELL_X87_Y35_N18
\bc|ff3|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \bc|ff3|Q~0_combout\ = ( \bc|ff3|Q~q\ & ( \bc|ff1|Q~q\ & ( (!\bc|ff2|Q~q\) # (!\bc|ff0|Q~q\) ) ) ) # ( !\bc|ff3|Q~q\ & ( \bc|ff1|Q~q\ & ( (\bc|ff2|Q~q\ & \bc|ff0|Q~q\) ) ) ) # ( \bc|ff3|Q~q\ & ( !\bc|ff1|Q~q\ ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000111111111111111100000000001100111111111111001100",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datab => \bc|ff2|ALT_INV_Q~q\,
	datad => \bc|ff0|ALT_INV_Q~q\,
	datae => \bc|ff3|ALT_INV_Q~q\,
	dataf => \bc|ff1|ALT_INV_Q~q\,
	combout => \bc|ff3|Q~0_combout\);

-- Location: FF_X87_Y35_N20
\bc|ff3|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \bc|ff3|Q~0_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \bc|ff3|Q~q\);

-- Location: MLABCELL_X87_Y35_N45
\bc|ff4|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \bc|ff4|Q~0_combout\ = ( \bc|ff4|Q~q\ & ( \bc|ff1|Q~q\ & ( (!\bc|ff0|Q~q\) # ((!\bc|ff2|Q~q\) # (!\bc|ff3|Q~q\)) ) ) ) # ( !\bc|ff4|Q~q\ & ( \bc|ff1|Q~q\ & ( (\bc|ff0|Q~q\ & (\bc|ff2|Q~q\ & \bc|ff3|Q~q\)) ) ) ) # ( \bc|ff4|Q~q\ & ( !\bc|ff1|Q~q\ ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000111111111111111100000000000000111111111111111100",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datab => \bc|ff0|ALT_INV_Q~q\,
	datac => \bc|ff2|ALT_INV_Q~q\,
	datad => \bc|ff3|ALT_INV_Q~q\,
	datae => \bc|ff4|ALT_INV_Q~q\,
	dataf => \bc|ff1|ALT_INV_Q~q\,
	combout => \bc|ff4|Q~0_combout\);

-- Location: FF_X87_Y35_N47
\bc|ff4|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \bc|ff4|Q~0_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \bc|ff4|Q~q\);

-- Location: MLABCELL_X87_Y35_N54
\bc|ff5|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \bc|ff5|Q~0_combout\ = ( \bc|ff5|Q~q\ & ( \bc|ff1|Q~q\ & ( (!\bc|ff3|Q~q\) # ((!\bc|ff0|Q~q\) # ((!\bc|ff2|Q~q\) # (!\bc|ff4|Q~q\))) ) ) ) # ( !\bc|ff5|Q~q\ & ( \bc|ff1|Q~q\ & ( (\bc|ff3|Q~q\ & (\bc|ff0|Q~q\ & (\bc|ff2|Q~q\ & \bc|ff4|Q~q\))) ) ) ) # ( 
-- \bc|ff5|Q~q\ & ( !\bc|ff1|Q~q\ ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000111111111111111100000000000000011111111111111110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \bc|ff3|ALT_INV_Q~q\,
	datab => \bc|ff0|ALT_INV_Q~q\,
	datac => \bc|ff2|ALT_INV_Q~q\,
	datad => \bc|ff4|ALT_INV_Q~q\,
	datae => \bc|ff5|ALT_INV_Q~q\,
	dataf => \bc|ff1|ALT_INV_Q~q\,
	combout => \bc|ff5|Q~0_combout\);

-- Location: FF_X87_Y35_N56
\bc|ff5|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \bc|ff5|Q~0_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \bc|ff5|Q~q\);

-- Location: MLABCELL_X87_Y35_N51
\con|nad2|y~1\ : cyclonev_lcell_comb
-- Equation(s):
-- \con|nad2|y~1_combout\ = ( \bc|ff4|Q~q\ & ( !\bc|ff1|Q~q\ & ( (!\bc|ff5|Q~q\ & (!\bc|ff2|Q~q\ & \bc|ff3|Q~q\)) ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000001010000000000000000000000000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \bc|ff5|ALT_INV_Q~q\,
	datac => \bc|ff2|ALT_INV_Q~q\,
	datad => \bc|ff3|ALT_INV_Q~q\,
	datae => \bc|ff4|ALT_INV_Q~q\,
	dataf => \bc|ff1|ALT_INV_Q~q\,
	combout => \con|nad2|y~1_combout\);

-- Location: LABCELL_X88_Y35_N30
\con|nad2|y~2\ : cyclonev_lcell_comb
-- Equation(s):
-- \con|nad2|y~2_combout\ = ( \con|nad2|y~1_combout\ & ( \clk~input_o\ & ( (!\nGRst~input_o\) # (\bc|ff0|Q~q\) ) ) ) # ( !\con|nad2|y~1_combout\ & ( \clk~input_o\ & ( !\nGRst~input_o\ ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000000000000010101010101010101010101011111111",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \ALT_INV_nGRst~input_o\,
	datad => \bc|ff0|ALT_INV_Q~q\,
	datae => \con|nad2|ALT_INV_y~1_combout\,
	dataf => \ALT_INV_clk~input_o\,
	combout => \con|nad2|y~2_combout\);

-- Location: FF_X87_Y35_N41
\bc|ff0|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \bc|ff0|Q~0_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \bc|ff0|Q~q\);

-- Location: LABCELL_X88_Y35_N6
\con|nord|y~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \con|nord|y~0_combout\ = LCELL((!\bc|ff0|Q~q\ & (!\clk~input_o\ & \con|nad2|y~1_combout\)))

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000010100000000000001010000000000000101000000000000010100000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \bc|ff0|ALT_INV_Q~q\,
	datac => \ALT_INV_clk~input_o\,
	datad => \con|nad2|ALT_INV_y~1_combout\,
	combout => \con|nord|y~0_combout\);

-- Location: FF_X88_Y35_N29
\pr5|ff0|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \hd|hA10|xor20|y~combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff0|Q~q\);

-- Location: IOIBUF_X89_Y37_N21
\dIn~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_dIn,
	o => \dIn~input_o\);

-- Location: FF_X88_Y35_N32
\ff|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \dIn~input_o\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \ff|Q~q\);

-- Location: LABCELL_X88_Y35_N15
\hd|hA10|xor20|y\ : cyclonev_lcell_comb
-- Equation(s):
-- \hd|hA10|xor20|y~combout\ = !\pr5|ff0|Q~q\ $ (!\ff|Q~q\)

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0110011001100110011001100110011001100110011001100110011001100110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \pr5|ff0|ALT_INV_Q~q\,
	datab => \ff|ALT_INV_Q~q\,
	combout => \hd|hA10|xor20|y~combout\);

-- Location: LABCELL_X88_Y35_N9
\pr6|ff0|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \pr6|ff0|Q~0_combout\ = !\hd|hA10|xor20|y~combout\

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1100110011001100110011001100110011001100110011001100110011001100",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datab => \hd|hA10|xor20|ALT_INV_y~combout\,
	combout => \pr6|ff0|Q~0_combout\);

-- Location: MLABCELL_X87_Y35_N30
\con|nad2|y~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \con|nad2|y~0_combout\ = ( !\bc|ff5|Q~q\ & ( !\bc|ff2|Q~q\ & ( !\bc|ff1|Q~q\ ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111000011110000000000000000000000000000000000000000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datac => \bc|ff1|ALT_INV_Q~q\,
	datae => \bc|ff5|ALT_INV_Q~q\,
	dataf => \bc|ff2|ALT_INV_Q~q\,
	combout => \con|nad2|y~0_combout\);

-- Location: LABCELL_X88_Y35_N54
\con|nad4|y~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \con|nad4|y~0_combout\ = ( \bc|ff4|Q~q\ & ( \con|nad2|y~0_combout\ & ( (\clk~input_o\ & !\nGRst~input_o\) ) ) ) # ( !\bc|ff4|Q~q\ & ( \con|nad2|y~0_combout\ & ( (\clk~input_o\ & ((!\nGRst~input_o\) # ((\bc|ff0|Q~q\ & !\bc|ff3|Q~q\)))) ) ) ) # ( 
-- \bc|ff4|Q~q\ & ( !\con|nad2|y~0_combout\ & ( (\clk~input_o\ & !\nGRst~input_o\) ) ) ) # ( !\bc|ff4|Q~q\ & ( !\con|nad2|y~0_combout\ & ( (\clk~input_o\ & !\nGRst~input_o\) ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0100010001000100010001000100010001000101010001000100010001000100",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \ALT_INV_clk~input_o\,
	datab => \ALT_INV_nGRst~input_o\,
	datac => \bc|ff0|ALT_INV_Q~q\,
	datad => \bc|ff3|ALT_INV_Q~q\,
	datae => \bc|ff4|ALT_INV_Q~q\,
	dataf => \con|nad2|ALT_INV_y~0_combout\,
	combout => \con|nad4|y~0_combout\);

-- Location: FF_X88_Y35_N10
\pr6|ff0|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \con|nord|y~0_combout\,
	d => \pr6|ff0|Q~0_combout\,
	clrn => \con|nad4|ALT_INV_y~0_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr6|ff0|Q~q\);

-- Location: LABCELL_X88_Y35_N39
\pr5|ff1|Q~feeder\ : cyclonev_lcell_comb
-- Equation(s):
-- \pr5|ff1|Q~feeder_combout\ = ( \hd|hA11|xor20|y~combout\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000000000000011111111111111111111111111111111",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataf => \hd|hA11|xor20|ALT_INV_y~combout\,
	combout => \pr5|ff1|Q~feeder_combout\);

-- Location: FF_X88_Y35_N40
\pr5|ff1|Q~DUPLICATE\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \pr5|ff1|Q~feeder_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff1|Q~DUPLICATE_q\);

-- Location: LABCELL_X88_Y35_N42
\hd|hA11|xor20|y\ : cyclonev_lcell_comb
-- Equation(s):
-- \hd|hA11|xor20|y~combout\ = ( \pr5|ff1|Q~DUPLICATE_q\ & ( (!\pr5|ff0|Q~q\) # (!\ff|Q~q\) ) ) # ( !\pr5|ff1|Q~DUPLICATE_q\ & ( (\pr5|ff0|Q~q\ & \ff|Q~q\) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0001000100010001000100010001000111101110111011101110111011101110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \pr5|ff0|ALT_INV_Q~q\,
	datab => \ff|ALT_INV_Q~q\,
	dataf => \pr5|ff1|ALT_INV_Q~DUPLICATE_q\,
	combout => \hd|hA11|xor20|y~combout\);

-- Location: LABCELL_X88_Y35_N12
\pr6|ff1|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \pr6|ff1|Q~0_combout\ = ( !\hd|hA11|xor20|y~combout\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111111111111111111111111111111100000000000000000000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataf => \hd|hA11|xor20|ALT_INV_y~combout\,
	combout => \pr6|ff1|Q~0_combout\);

-- Location: FF_X88_Y35_N13
\pr6|ff1|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \con|nord|y~0_combout\,
	d => \pr6|ff1|Q~0_combout\,
	clrn => \con|nad4|ALT_INV_y~0_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr6|ff1|Q~q\);

-- Location: FF_X88_Y35_N38
\pr5|ff2|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \hd|hA12|xor20|y~combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff2|Q~q\);

-- Location: LABCELL_X88_Y35_N21
\hd|hA12|xor20|y\ : cyclonev_lcell_comb
-- Equation(s):
-- \hd|hA12|xor20|y~combout\ = ( \pr5|ff2|Q~q\ & ( (!\pr5|ff0|Q~q\) # ((!\ff|Q~q\) # (!\pr5|ff1|Q~DUPLICATE_q\)) ) ) # ( !\pr5|ff2|Q~q\ & ( (\pr5|ff0|Q~q\ & (\ff|Q~q\ & \pr5|ff1|Q~DUPLICATE_q\)) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000100000001000000010000000111111110111111101111111011111110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \pr5|ff0|ALT_INV_Q~q\,
	datab => \ff|ALT_INV_Q~q\,
	datac => \pr5|ff1|ALT_INV_Q~DUPLICATE_q\,
	dataf => \pr5|ff2|ALT_INV_Q~q\,
	combout => \hd|hA12|xor20|y~combout\);

-- Location: LABCELL_X88_Y35_N45
\pr6|ff2|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \pr6|ff2|Q~0_combout\ = !\hd|hA12|xor20|y~combout\

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111000011110000111100001111000011110000111100001111000011110000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	datac => \hd|hA12|xor20|ALT_INV_y~combout\,
	combout => \pr6|ff2|Q~0_combout\);

-- Location: FF_X88_Y35_N46
\pr6|ff2|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \con|nord|y~0_combout\,
	d => \pr6|ff2|Q~0_combout\,
	clrn => \con|nad4|ALT_INV_y~0_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr6|ff2|Q~q\);

-- Location: FF_X88_Y35_N35
\pr5|ff3|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \hd|hA13|xor20|y~combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff3|Q~q\);

-- Location: LABCELL_X88_Y35_N3
\hd|hA13|xor20|y\ : cyclonev_lcell_comb
-- Equation(s):
-- \hd|hA13|xor20|y~combout\ = ( \pr5|ff3|Q~q\ & ( (!\pr5|ff0|Q~q\) # ((!\ff|Q~q\) # ((!\pr5|ff1|Q~DUPLICATE_q\) # (!\pr5|ff2|Q~q\))) ) ) # ( !\pr5|ff3|Q~q\ & ( (\pr5|ff0|Q~q\ & (\ff|Q~q\ & (\pr5|ff1|Q~DUPLICATE_q\ & \pr5|ff2|Q~q\))) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000001000000000000000111111111111111101111111111111110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \pr5|ff0|ALT_INV_Q~q\,
	datab => \ff|ALT_INV_Q~q\,
	datac => \pr5|ff1|ALT_INV_Q~DUPLICATE_q\,
	datad => \pr5|ff2|ALT_INV_Q~q\,
	dataf => \pr5|ff3|ALT_INV_Q~q\,
	combout => \hd|hA13|xor20|y~combout\);

-- Location: LABCELL_X88_Y35_N0
\pr6|ff3|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \pr6|ff3|Q~0_combout\ = ( !\hd|hA13|xor20|y~combout\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111111111111111111111111111111100000000000000000000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataf => \hd|hA13|xor20|ALT_INV_y~combout\,
	combout => \pr6|ff3|Q~0_combout\);

-- Location: FF_X88_Y35_N1
\pr6|ff3|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \con|nord|y~0_combout\,
	d => \pr6|ff3|Q~0_combout\,
	clrn => \con|nad4|ALT_INV_y~0_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr6|ff3|Q~q\);

-- Location: FF_X88_Y35_N59
\pr5|ff4|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \hd|hA14|xor20|y~combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff4|Q~q\);

-- Location: LABCELL_X88_Y35_N24
\hd|hA14|xor20|y\ : cyclonev_lcell_comb
-- Equation(s):
-- \hd|hA14|xor20|y~combout\ = ( \pr5|ff2|Q~q\ & ( \pr5|ff4|Q~q\ & ( (!\pr5|ff0|Q~q\) # ((!\ff|Q~q\) # ((!\pr5|ff3|Q~q\) # (!\pr5|ff1|Q~DUPLICATE_q\))) ) ) ) # ( !\pr5|ff2|Q~q\ & ( \pr5|ff4|Q~q\ ) ) # ( \pr5|ff2|Q~q\ & ( !\pr5|ff4|Q~q\ & ( (\pr5|ff0|Q~q\ & 
-- (\ff|Q~q\ & (\pr5|ff3|Q~q\ & \pr5|ff1|Q~DUPLICATE_q\))) ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0000000000000000000000000000000111111111111111111111111111111110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \pr5|ff0|ALT_INV_Q~q\,
	datab => \ff|ALT_INV_Q~q\,
	datac => \pr5|ff3|ALT_INV_Q~q\,
	datad => \pr5|ff1|ALT_INV_Q~DUPLICATE_q\,
	datae => \pr5|ff2|ALT_INV_Q~q\,
	dataf => \pr5|ff4|ALT_INV_Q~q\,
	combout => \hd|hA14|xor20|y~combout\);

-- Location: LABCELL_X88_Y35_N18
\pr6|ff4|Q~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \pr6|ff4|Q~0_combout\ = ( !\hd|hA14|xor20|y~combout\ )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111111111111111111111111111111100000000000000000000000000000000",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataf => \hd|hA14|xor20|ALT_INV_y~combout\,
	combout => \pr6|ff4|Q~0_combout\);

-- Location: FF_X88_Y35_N19
\pr6|ff4|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \con|nord|y~0_combout\,
	d => \pr6|ff4|Q~0_combout\,
	clrn => \con|nad4|ALT_INV_y~0_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr6|ff4|Q~q\);

-- Location: FF_X88_Y35_N37
\pr5|ff2|Q~DUPLICATE\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \hd|hA12|xor20|y~combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff2|Q~DUPLICATE_q\);

-- Location: FF_X88_Y35_N34
\pr5|ff3|Q~DUPLICATE\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	asdata => \hd|hA13|xor20|y~combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	sload => VCC,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff3|Q~DUPLICATE_q\);

-- Location: FF_X88_Y35_N41
\pr5|ff1|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \clk~inputCLKENA0_outclk\,
	d => \pr5|ff1|Q~feeder_combout\,
	clrn => \con|nad2|ALT_INV_y~2_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr5|ff1|Q~q\);

-- Location: LABCELL_X88_Y35_N48
\hd|hA14|and20|y\ : cyclonev_lcell_comb
-- Equation(s):
-- \hd|hA14|and20|y~combout\ = ( \pr5|ff1|Q~q\ & ( \pr5|ff0|Q~q\ & ( (!\pr5|ff4|Q~q\) # ((!\pr5|ff2|Q~DUPLICATE_q\) # ((!\ff|Q~q\) # (!\pr5|ff3|Q~DUPLICATE_q\))) ) ) ) # ( !\pr5|ff1|Q~q\ & ( \pr5|ff0|Q~q\ ) ) # ( \pr5|ff1|Q~q\ & ( !\pr5|ff0|Q~q\ ) ) # ( 
-- !\pr5|ff1|Q~q\ & ( !\pr5|ff0|Q~q\ ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "1111111111111111111111111111111111111111111111111111111111111110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \pr5|ff4|ALT_INV_Q~q\,
	datab => \pr5|ff2|ALT_INV_Q~DUPLICATE_q\,
	datac => \ff|ALT_INV_Q~q\,
	datad => \pr5|ff3|ALT_INV_Q~DUPLICATE_q\,
	datae => \pr5|ff1|ALT_INV_Q~q\,
	dataf => \pr5|ff0|ALT_INV_Q~q\,
	combout => \hd|hA14|and20|y~combout\);

-- Location: FF_X88_Y35_N49
\pr6|ff5|Q\ : dffeas
-- pragma translate_off
GENERIC MAP (
	is_wysiwyg => "true",
	power_up => "low")
-- pragma translate_on
PORT MAP (
	clk => \con|nord|y~0_combout\,
	d => \hd|hA14|and20|y~combout\,
	clrn => \con|nad4|ALT_INV_y~0_combout\,
	devclrn => ww_devclrn,
	devpor => ww_devpor,
	q => \pr6|ff5|Q~q\);

-- Location: LABCELL_X11_Y41_N3
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


