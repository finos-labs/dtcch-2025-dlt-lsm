import IconButton from "@/components/button/io-icon-button";
import SettlementStatus from "@/components/settlement/settlement-status";
import { getSettlementDetails } from "@/service/api-service";
import { Settlement } from "@/types";
import { camelToTitleCase } from "@/utils/format";
import { Box, DataList, Flex, Text } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { BsArrowLeft } from "react-icons/bs";
import { useNavigate, useParams } from "react-router";

const SettlementDetails = () => {
  const { settlementId } = useParams();
  const navigate = useNavigate();
  const [details, setDetails] = useState<Settlement>();

  useEffect(() => {
    if (settlementId) {
      getSettlementDetails(settlementId).then((data) => {
        setDetails(data);
        console.log(data);
      });
    }
  }, [settlementId]);

  return (
    <>
      <Flex gap={5} direction="row">
        <IconButton onClick={() => navigate("/")}>
          <BsArrowLeft />
        </IconButton>
        <Text textStyle="2xl" marginBottom="1rem">
          Settlement Details
        </Text>
      </Flex>
      <Box
        marginTop="1rem"
        px="3rem"
        py="1rem"
        boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
        bg="whiteAlpha.800"
      >
        <DataList.Root orientation="horizontal" py="1rem">
          {details &&
            Object.entries(details)?.map(([label, value]) => (
              <DataList.Item key={label}>
                <DataList.ItemLabel>
                  <b>{camelToTitleCase(label)}:</b>
                </DataList.ItemLabel>
                <DataList.ItemValue>
                  {label === "status" ? (
                    <SettlementStatus status={value.toString()} />
                  ) : (
                    value
                  )}
                </DataList.ItemValue>
              </DataList.Item>
            ))}
        </DataList.Root>
      </Box>
    </>
  );
};

export default SettlementDetails;
