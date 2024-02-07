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

-- DATE "11/19/2023 21:38:39"

-- 
-- Device: Altera 5CGXFC7C7F23C8 Package FBGA484
-- 

-- 
-- This VHDL file should be used for ModelSim-Altera (VHDL) only
-- 

LIBRARY ALTERA_LNSIM;
LIBRARY CYCLONEV;
LIBRARY IEEE;
USE ALTERA_LNSIM.ALTERA_LNSIM_COMPONENTS.ALL;
USE CYCLONEV.CYCLONEV_COMPONENTS.ALL;
USE IEEE.STD_LOGIC_1164.ALL;

ENTITY 	encoderCRC8 IS
    PORT (
	an : IN std_logic_vector(23 DOWNTO 0);
	error : BUFFER std_logic
	);
END encoderCRC8;

-- Design Ports Information
-- an[11]	=>  Location: PIN_U13,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[13]	=>  Location: PIN_J17,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[14]	=>  Location: PIN_G15,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[16]	=>  Location: PIN_E9,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[21]	=>  Location: PIN_C13,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[22]	=>  Location: PIN_AA20,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- error	=>  Location: PIN_L19,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[5]	=>  Location: PIN_K21,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[12]	=>  Location: PIN_L18,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[23]	=>  Location: PIN_L22,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[0]	=>  Location: PIN_N21,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[1]	=>  Location: PIN_P18,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[2]	=>  Location: PIN_M16,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[3]	=>  Location: PIN_N20,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[4]	=>  Location: PIN_N16,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[6]	=>  Location: PIN_M22,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[7]	=>  Location: PIN_M18,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[8]	=>  Location: PIN_P19,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[9]	=>  Location: PIN_K22,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[10]	=>  Location: PIN_N19,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[15]	=>  Location: PIN_L17,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[17]	=>  Location: PIN_P16,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[18]	=>  Location: PIN_M21,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[19]	=>  Location: PIN_M20,	 I/O Standard: 2.5 V,	 Current Strength: Default
-- an[20]	=>  Location: PIN_K17,	 I/O Standard: 2.5 V,	 Current Strength: Default


ARCHITECTURE structure OF encoderCRC8 IS
SIGNAL gnd : std_logic := '0';
SIGNAL vcc : std_logic := '1';
SIGNAL unknown : std_logic := 'X';
SIGNAL devoe : std_logic := '1';
SIGNAL devclrn : std_logic := '1';
SIGNAL devpor : std_logic := '1';
SIGNAL ww_devoe : std_logic;
SIGNAL ww_devclrn : std_logic;
SIGNAL ww_devpor : std_logic;
SIGNAL ww_an : std_logic_vector(23 DOWNTO 0);
SIGNAL ww_error : std_logic;
SIGNAL \an[11]~input_o\ : std_logic;
SIGNAL \an[13]~input_o\ : std_logic;
SIGNAL \an[14]~input_o\ : std_logic;
SIGNAL \an[16]~input_o\ : std_logic;
SIGNAL \an[21]~input_o\ : std_logic;
SIGNAL \an[22]~input_o\ : std_logic;
SIGNAL \~QUARTUS_CREATED_GND~I_combout\ : std_logic;
SIGNAL \an[4]~input_o\ : std_logic;
SIGNAL \an[3]~input_o\ : std_logic;
SIGNAL \an[1]~input_o\ : std_logic;
SIGNAL \an[0]~input_o\ : std_logic;
SIGNAL \an[2]~input_o\ : std_logic;
SIGNAL \err|y~0_combout\ : std_logic;
SIGNAL \an[12]~input_o\ : std_logic;
SIGNAL \an[10]~input_o\ : std_logic;
SIGNAL \an[6]~input_o\ : std_logic;
SIGNAL \an[7]~input_o\ : std_logic;
SIGNAL \an[9]~input_o\ : std_logic;
SIGNAL \an[8]~input_o\ : std_logic;
SIGNAL \err|y~1_combout\ : std_logic;
SIGNAL \an[5]~input_o\ : std_logic;
SIGNAL \an[23]~input_o\ : std_logic;
SIGNAL \an[20]~input_o\ : std_logic;
SIGNAL \an[19]~input_o\ : std_logic;
SIGNAL \an[15]~input_o\ : std_logic;
SIGNAL \an[18]~input_o\ : std_logic;
SIGNAL \an[17]~input_o\ : std_logic;
SIGNAL \err|y~2_combout\ : std_logic;
SIGNAL \err|y~3_combout\ : std_logic;
SIGNAL \ALT_INV_an[5]~input_o\ : std_logic;
SIGNAL \err|ALT_INV_y~0_combout\ : std_logic;
SIGNAL \err|ALT_INV_y~1_combout\ : std_logic;
SIGNAL \err|ALT_INV_y~2_combout\ : std_logic;
SIGNAL \ALT_INV_an[12]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[23]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[20]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[19]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[18]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[17]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[15]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[10]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[9]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[8]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[7]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[6]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[4]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[3]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[2]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[1]~input_o\ : std_logic;
SIGNAL \ALT_INV_an[0]~input_o\ : std_logic;

BEGIN

ww_an <= an;
error <= ww_error;
ww_devoe <= devoe;
ww_devclrn <= devclrn;
ww_devpor <= devpor;
\ALT_INV_an[5]~input_o\ <= NOT \an[5]~input_o\;
\err|ALT_INV_y~0_combout\ <= NOT \err|y~0_combout\;
\err|ALT_INV_y~1_combout\ <= NOT \err|y~1_combout\;
\err|ALT_INV_y~2_combout\ <= NOT \err|y~2_combout\;
\ALT_INV_an[12]~input_o\ <= NOT \an[12]~input_o\;
\ALT_INV_an[23]~input_o\ <= NOT \an[23]~input_o\;
\ALT_INV_an[20]~input_o\ <= NOT \an[20]~input_o\;
\ALT_INV_an[19]~input_o\ <= NOT \an[19]~input_o\;
\ALT_INV_an[18]~input_o\ <= NOT \an[18]~input_o\;
\ALT_INV_an[17]~input_o\ <= NOT \an[17]~input_o\;
\ALT_INV_an[15]~input_o\ <= NOT \an[15]~input_o\;
\ALT_INV_an[10]~input_o\ <= NOT \an[10]~input_o\;
\ALT_INV_an[9]~input_o\ <= NOT \an[9]~input_o\;
\ALT_INV_an[8]~input_o\ <= NOT \an[8]~input_o\;
\ALT_INV_an[7]~input_o\ <= NOT \an[7]~input_o\;
\ALT_INV_an[6]~input_o\ <= NOT \an[6]~input_o\;
\ALT_INV_an[4]~input_o\ <= NOT \an[4]~input_o\;
\ALT_INV_an[3]~input_o\ <= NOT \an[3]~input_o\;
\ALT_INV_an[2]~input_o\ <= NOT \an[2]~input_o\;
\ALT_INV_an[1]~input_o\ <= NOT \an[1]~input_o\;
\ALT_INV_an[0]~input_o\ <= NOT \an[0]~input_o\;

-- Location: IOOBUF_X89_Y38_N5
\error~output\ : cyclonev_io_obuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	open_drain_output => "false",
	shift_series_termination_control => "false")
-- pragma translate_on
PORT MAP (
	i => \err|y~3_combout\,
	devoe => ww_devoe,
	o => ww_error);

-- Location: IOIBUF_X89_Y35_N44
\an[4]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(4),
	o => \an[4]~input_o\);

-- Location: IOIBUF_X89_Y35_N78
\an[3]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(3),
	o => \an[3]~input_o\);

-- Location: IOIBUF_X89_Y9_N55
\an[1]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(1),
	o => \an[1]~input_o\);

-- Location: IOIBUF_X89_Y35_N95
\an[0]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(0),
	o => \an[0]~input_o\);

-- Location: IOIBUF_X89_Y35_N61
\an[2]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(2),
	o => \an[2]~input_o\);

-- Location: LABCELL_X88_Y35_N30
\err|y~0\ : cyclonev_lcell_comb
-- Equation(s):
-- \err|y~0_combout\ = ( \an[0]~input_o\ & ( \an[2]~input_o\ & ( !\an[4]~input_o\ $ (!\an[3]~input_o\ $ (\an[1]~input_o\)) ) ) ) # ( !\an[0]~input_o\ & ( \an[2]~input_o\ & ( !\an[4]~input_o\ $ (!\an[3]~input_o\ $ (!\an[1]~input_o\)) ) ) ) # ( \an[0]~input_o\ 
-- & ( !\an[2]~input_o\ & ( !\an[4]~input_o\ $ (!\an[3]~input_o\ $ (!\an[1]~input_o\)) ) ) ) # ( !\an[0]~input_o\ & ( !\an[2]~input_o\ & ( !\an[4]~input_o\ $ (!\an[3]~input_o\ $ (\an[1]~input_o\)) ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0110100101101001100101101001011010010110100101100110100101101001",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \ALT_INV_an[4]~input_o\,
	datab => \ALT_INV_an[3]~input_o\,
	datac => \ALT_INV_an[1]~input_o\,
	datae => \ALT_INV_an[0]~input_o\,
	dataf => \ALT_INV_an[2]~input_o\,
	combout => \err|y~0_combout\);

-- Location: IOIBUF_X89_Y38_N21
\an[12]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(12),
	o => \an[12]~input_o\);

-- Location: IOIBUF_X89_Y36_N4
\an[10]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(10),
	o => \an[10]~input_o\);

-- Location: IOIBUF_X89_Y36_N38
\an[6]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(6),
	o => \an[6]~input_o\);

-- Location: IOIBUF_X89_Y36_N21
\an[7]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(7),
	o => \an[7]~input_o\);

-- Location: IOIBUF_X89_Y38_N55
\an[9]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(9),
	o => \an[9]~input_o\);

-- Location: IOIBUF_X89_Y9_N38
\an[8]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(8),
	o => \an[8]~input_o\);

-- Location: LABCELL_X88_Y36_N33
\err|y~1\ : cyclonev_lcell_comb
-- Equation(s):
-- \err|y~1_combout\ = ( \an[8]~input_o\ & ( !\an[10]~input_o\ $ (!\an[6]~input_o\ $ (!\an[7]~input_o\ $ (\an[9]~input_o\))) ) ) # ( !\an[8]~input_o\ & ( !\an[10]~input_o\ $ (!\an[6]~input_o\ $ (!\an[7]~input_o\ $ (!\an[9]~input_o\))) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0110100110010110100101100110100101101001100101101001011001101001",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \ALT_INV_an[10]~input_o\,
	datab => \ALT_INV_an[6]~input_o\,
	datac => \ALT_INV_an[7]~input_o\,
	datad => \ALT_INV_an[9]~input_o\,
	datae => \ALT_INV_an[8]~input_o\,
	combout => \err|y~1_combout\);

-- Location: IOIBUF_X89_Y38_N38
\an[5]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(5),
	o => \an[5]~input_o\);

-- Location: IOIBUF_X89_Y36_N55
\an[23]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(23),
	o => \an[23]~input_o\);

-- Location: IOIBUF_X89_Y37_N4
\an[20]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(20),
	o => \an[20]~input_o\);

-- Location: IOIBUF_X89_Y37_N38
\an[19]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(19),
	o => \an[19]~input_o\);

-- Location: IOIBUF_X89_Y37_N21
\an[15]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(15),
	o => \an[15]~input_o\);

-- Location: IOIBUF_X89_Y37_N55
\an[18]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(18),
	o => \an[18]~input_o\);

-- Location: IOIBUF_X89_Y9_N4
\an[17]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(17),
	o => \an[17]~input_o\);

-- Location: LABCELL_X88_Y37_N33
\err|y~2\ : cyclonev_lcell_comb
-- Equation(s):
-- \err|y~2_combout\ = ( \an[17]~input_o\ & ( !\an[20]~input_o\ $ (!\an[19]~input_o\ $ (!\an[15]~input_o\ $ (\an[18]~input_o\))) ) ) # ( !\an[17]~input_o\ & ( !\an[20]~input_o\ $ (!\an[19]~input_o\ $ (!\an[15]~input_o\ $ (!\an[18]~input_o\))) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0110100110010110100101100110100101101001100101101001011001101001",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \ALT_INV_an[20]~input_o\,
	datab => \ALT_INV_an[19]~input_o\,
	datac => \ALT_INV_an[15]~input_o\,
	datad => \ALT_INV_an[18]~input_o\,
	datae => \ALT_INV_an[17]~input_o\,
	combout => \err|y~2_combout\);

-- Location: LABCELL_X88_Y38_N0
\err|y~3\ : cyclonev_lcell_comb
-- Equation(s):
-- \err|y~3_combout\ = ( \an[23]~input_o\ & ( \err|y~2_combout\ & ( !\err|y~0_combout\ $ (!\an[12]~input_o\ $ (!\err|y~1_combout\ $ (!\an[5]~input_o\))) ) ) ) # ( !\an[23]~input_o\ & ( \err|y~2_combout\ & ( !\err|y~0_combout\ $ (!\an[12]~input_o\ $ 
-- (!\err|y~1_combout\ $ (\an[5]~input_o\))) ) ) ) # ( \an[23]~input_o\ & ( !\err|y~2_combout\ & ( !\err|y~0_combout\ $ (!\an[12]~input_o\ $ (!\err|y~1_combout\ $ (\an[5]~input_o\))) ) ) ) # ( !\an[23]~input_o\ & ( !\err|y~2_combout\ & ( !\err|y~0_combout\ $ 
-- (!\an[12]~input_o\ $ (!\err|y~1_combout\ $ (!\an[5]~input_o\))) ) ) )

-- pragma translate_off
GENERIC MAP (
	extended_lut => "off",
	lut_mask => "0110100110010110100101100110100110010110011010010110100110010110",
	shared_arith => "off")
-- pragma translate_on
PORT MAP (
	dataa => \err|ALT_INV_y~0_combout\,
	datab => \ALT_INV_an[12]~input_o\,
	datac => \err|ALT_INV_y~1_combout\,
	datad => \ALT_INV_an[5]~input_o\,
	datae => \ALT_INV_an[23]~input_o\,
	dataf => \err|ALT_INV_y~2_combout\,
	combout => \err|y~3_combout\);

-- Location: IOIBUF_X50_Y0_N41
\an[11]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(11),
	o => \an[11]~input_o\);

-- Location: IOIBUF_X64_Y81_N35
\an[13]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(13),
	o => \an[13]~input_o\);

-- Location: IOIBUF_X62_Y81_N35
\an[14]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(14),
	o => \an[14]~input_o\);

-- Location: IOIBUF_X28_Y81_N1
\an[16]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(16),
	o => \an[16]~input_o\);

-- Location: IOIBUF_X54_Y81_N18
\an[21]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(21),
	o => \an[21]~input_o\);

-- Location: IOIBUF_X62_Y0_N35
\an[22]~input\ : cyclonev_io_ibuf
-- pragma translate_off
GENERIC MAP (
	bus_hold => "false",
	simulate_z_as => "z")
-- pragma translate_on
PORT MAP (
	i => ww_an(22),
	o => \an[22]~input_o\);

-- Location: LABCELL_X56_Y54_N3
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


