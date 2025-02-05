import SettlementStatus from "@/components/settlement/settlement-status";
import { getBatches, getLooseSettlements } from "@/service/api-service";
import { Batch, Settlement } from "@/types";
import {
  Box,
  Code,
  Collapsible,
  Flex,
  For,
  Separator,
  Table,
  Text,
} from "@chakra-ui/react";
import { useEffect, useState } from "react";

type CollapsibleBatchProps = {
  data: Batch;
  defaultOpen?: boolean;
};

type SettlementTableProps = {
  data: Settlement[];
};

const SettlementTable = ({ data }: SettlementTableProps) => {
  return (
    <Table.Root key={data.length ?? 0} className="light" native>
      <Table.Header>
        <Table.Row>
          <Table.ColumnHeader>
            <b>Id</b>
          </Table.ColumnHeader>
          <Table.ColumnHeader>
            <b>Token Amount</b>
          </Table.ColumnHeader>
          <Table.ColumnHeader>
            <b>Cash Amount</b>
          </Table.ColumnHeader>
          <Table.ColumnHeader>
            <b>Buyer</b>
          </Table.ColumnHeader>
          <Table.ColumnHeader>
            <b>Seller</b>
          </Table.ColumnHeader>
          <Table.ColumnHeader>
            <b>Date</b>
          </Table.ColumnHeader>
          <Table.ColumnHeader>
            <b>Status</b>
          </Table.ColumnHeader>
          {/* <Table.ColumnHeader textAlign="center">
            <b>Details</b>
          </Table.ColumnHeader> */}
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {data.map((item: Settlement) => (
          <Table.Row key={item.id}>
            <Table.Cell>{item.id}</Table.Cell>
            <Table.Cell>{item.securityAmount}</Table.Cell>
            <Table.Cell>{item.cashAmount}</Table.Cell>
            <Table.Cell>{item.buyer}</Table.Cell>
            <Table.Cell>{item.seller}</Table.Cell>
            <Table.Cell>
              {new Date(item.creationDate).toLocaleString()}
            </Table.Cell>
            <Table.Cell>
              <SettlementStatus status={item.status} />
            </Table.Cell>
            {/* <Table.Cell textAlign="center">
              <IconButton onClick={() => navigate(item.id)}>
                <BsEye />
              </IconButton>
            </Table.Cell> */}
          </Table.Row>
        ))}
      </Table.Body>
    </Table.Root>
  );
};

const AIInfo = ({ code }: { code: string }) => {
  return (
    <Flex direction="column" w="50%">
      <Text textStyle="2xl" marginBottom="1rem">
        AI Output
      </Text>
      <Box bg="blackAlpha.100" height="100%" borderRadius="md" p="2rem">
        <Code className="light">{code}</Code>
      </Box>
    </Flex>
  );
};

const CollapsibleBatch = ({ data, defaultOpen }: CollapsibleBatchProps) => {
  return (
    <Collapsible.Root
      padding={4}
      bg="white"
      borderRadius="md"
      border="1px solid"
      borderColor="gray.100"
      boxShadow="0 8px 16px rgba(0,0,0,0.08)"
      color="black"
      defaultOpen={defaultOpen}
    >
      <Collapsible.Trigger
        paddingY="3"
        fontWeight="bold"
        w="100%"
        textAlign="left"
      >
        {data.id ? `Batch # ${data.id}` : "Pending Settlements"} (
        {data.settlements.length})
      </Collapsible.Trigger>
      <Collapsible.Content>
        <Flex padding="4" gap={5} direction="row">
          <SettlementTable data={data.settlements} />
          {data.id && (
            <>
              <Separator orientation="vertical" />
              <AIInfo code={data.aiResult} />
            </>
          )}
        </Flex>
      </Collapsible.Content>
    </Collapsible.Root>
  );
};

const Settlements = () => {
  const [batches, setBatches] = useState<Batch[]>([]);
  const [pendingSettlements, setPendingSettlements] = useState<Batch>();

  useEffect(() => {
    getBatches().then((data) => {
      setBatches(data);
      console.log(data);
    });
  }, []);

  useEffect(() => {
    getLooseSettlements().then((data) => {
      setPendingSettlements(data);
      console.log(data);
    });
  }, []);

  return (
    <>
      <Text textStyle="2xl" marginBottom="1rem">
        {" "}
        Settlements
      </Text>
      <Flex gap={5} direction="column">
        {(pendingSettlements?.settlements.length ?? 0) > 0 && (
          <CollapsibleBatch data={pendingSettlements!} defaultOpen />
        )}
        {batches.length > 0 && (
          <For each={batches.sort((a, b) => b.id - a.id)}>
            {(batch, index) => {
              return (
                <CollapsibleBatch
                  key={batch.id}
                  data={batch}
                  defaultOpen={index === 0}
                />
              );
            }}
          </For>
        )}
      </Flex>
    </>
  );
};

export default Settlements;
