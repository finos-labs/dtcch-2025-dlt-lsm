import { Box } from "@chakra-ui/react";
import { useState } from "react";
import { Link } from "react-router";
import RoundedButton from "../button/rounded-button";

const Sidebar = () => {
  const [activeTab, setActiveTab] = useState("tab1");

  return (
    <Box
      as="nav"
      position="fixed"
      left="0"
      w="200px"
      h="100vh"
      bg="white"
      p="2rem"
      color="white"
      boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
    >
      <Link to="/settlements">
        <RoundedButton
          w="100%"
          bg={activeTab === "tab1" ? "blue.700" : "blue.500"}
          mb="2"
          onClick={() => setActiveTab("tab1")}
          color="white"
        >
          Settlements
        </RoundedButton>
      </Link>
      <Link to="/create">
        <RoundedButton
          w="100%"
          bg={activeTab === "tab2" ? "blue.700" : "blue.500"}
          onClick={() => setActiveTab("tab2")}
          color="white"
        >
          Create
        </RoundedButton>
      </Link>
    </Box>
  );
};

export default Sidebar;
