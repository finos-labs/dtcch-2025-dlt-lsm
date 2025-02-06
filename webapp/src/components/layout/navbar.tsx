import {
  createRandomSettlements,
  executeLsm,
  resetBalances,
} from "@/service/api-service";
import { Flex, Image, NumberInputRoot, Text } from "@chakra-ui/react";
import { useState } from "react";
import image from "../../assets/Simbolo_ioB.png";
import Button from "../button/io-button";
import { NumberInputField } from "../ui/number-input";

const Navbar = () => {
  const [amount, setAmount] = useState("5");
  const [balanceAmount, setBalanceAmount] = useState("1000");

  return (
    <Flex
      as="nav"
      position="fixed"
      backgroundColor="rgba(255, 255, 255, 1)"
      backdropFilter="saturate(180%) blur(5px)"
      alignItems="center"
      justifyContent="space-between"
      top="0"
      w="100%"
      h="4rem"
      paddingX="2rem"
      boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
      zIndex="1000"
    >
      <Flex gap={6} alignItems="center">
        <Image src={image} height="24px" />
        <Text color="gray.600" fontWeight="500">
          DTCC Hackathon 2025 | LSM - DLT
        </Text>
      </Flex>
      <Flex gap={4} alignItems="center">
        |<Text>Balance:</Text>
        <NumberInputRoot
          maxW="50px"
          value={balanceAmount}
          onValueChange={(e) => {
            setBalanceAmount(e.value);
          }}
        >
          <NumberInputField />
        </NumberInputRoot>
        <Button
          onClick={() => {
            resetBalances(Number.parseInt(amount));
          }}
        >
          Reset Balances
        </Button>
        |<Text>Amount:</Text>
        <NumberInputRoot
          maxW="50px"
          value={amount}
          onValueChange={(e) => {
            setAmount(e.value);
          }}
        >
          <NumberInputField />
        </NumberInputRoot>
        <Button
          onClick={() => {
            createRandomSettlements(Number.parseInt(amount));
          }}
        >
          Create Random Settlements
        </Button>
        |
        <Button
          onClick={() => {
            executeLsm();
          }}
        >
          Exectue LSM
        </Button>
      </Flex>
    </Flex>
  );
};

export default Navbar;
