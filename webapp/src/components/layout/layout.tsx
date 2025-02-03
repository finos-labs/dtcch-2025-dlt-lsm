import { PropsWithChildren } from "react";
import Navbar from "./navbar";
import { Box, Flex } from "@chakra-ui/react";

const Layout = (props: PropsWithChildren) => {
  return (
    <Flex direction="column" minH="100vh">
      <Navbar />
      <Box marginTop="4rem" p="2rem">
        {props.children}
      </Box>
    </Flex>
  );
};

export default Layout;
