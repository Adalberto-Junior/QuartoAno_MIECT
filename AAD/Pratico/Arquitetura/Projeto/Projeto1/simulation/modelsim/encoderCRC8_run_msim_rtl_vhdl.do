transcript on
if {[file exists rtl_work]} {
	vdel -lib rtl_work -all
}
vlib rtl_work
vmap work rtl_work

vcom -93 -work work {C:/Users/Adalb/Desktop/QuartoAno/AAD/Pratico/Arquitetura/Projeto/Projeto1/encoderCRC8.vhd}
vcom -93 -work work {C:/Users/Adalb/Desktop/QuartoAno/AAD/Pratico/Arquitetura/Projeto/Projeto1/simpleLogic.vhd}

