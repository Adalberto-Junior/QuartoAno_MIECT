library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.NUMERIC_STD.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity Enconder_CRC_8 is
  Port (
   data_in : in STD_LOGIC_VECTOR(24 downto 0);
    crc_out : out STD_LOGIC_VECTOR(23 downto 0)
  );
end Enconder_CRC_8 ;

architecture Behavioral of Enconder_CRC_8 is
  constant CRC8_POLYNOMIAL : STD_LOGIC_VECTOR(7 downto 0) := "11000011";
  constant plus_one : STD_LOGIC_VECTOR(3 downto 0) := "0001";
  constant eight : STD_LOGIC_VECTOR(3 downto 0) := "1000";
  

  signal dividend_shifted : STD_LOGIC_VECTOR(7 downto 0);
  signal dividend : STD_LOGIC_VECTOR(15 downto 0);
  signal divisor : STD_LOGIC_VECTOR(7 downto 0);
  signal divident_remainer : STD_LOGIC_VECTOR(7 downto 0);
  signal remainder : STD_LOGIC_VECTOR(7 downto 0);
  signal how_mutch : STD_LOGIC_VECTOR(3 downto 0);
  signal check : STD_LOGIC;
  signal bite : integer range 0 to 15;
  

begin 
process (dividend_shifted, divident_remainer, check)
  variable xor_result : STD_LOGIC_VECTOR(7 downto 0);
begin
  -- Initialize variables
  dividend <= data_in(23 downto 8);
  divisor <= data_in(7 downto 0);
  dividend_shifted <= dividend(15 downto 8);
  remainder <= "00000000";
  divident_remainer <= dividend(7 downto 0);
  check <= '0';

  -- Encoder loop
  while check /= '1' loop
    xor_result := (dividend_shifted(7 downto 0) xor divisor(7 downto 0));

    -- Calculate 'how_mutch' using a separate process
    how_mutch <= "0000";
    for f in xor_result'range loop
      how_mutch <= how_mutch + plus_one;
    end loop;

    -- Update variables
    if how_mutch /= "1000" then
      for j in 0 to 7 loop -- Use a constant range here
        bite <= to_integer(IEEE.NUMERIC_STD.UNSIGNED(eight) xor IEEE.NUMERIC_STD.UNSIGNED(how_mutch));
        for k in 0 to bite loop
          dividend_shifted <= xor_result & divident_remainer(divident_remainer'high);
          divident_remainer <= divident_remainer(divident_remainer'high-1 downto 0) & '0';
        end loop;
      end loop;
    else
      check <= '1';
    end if;
  end loop;

  -- Concatenate the remainder and the dividend to form the CRC-8 code
  crc_out <= remainder & dividend_shifted;
end process;

end Behavioral;

