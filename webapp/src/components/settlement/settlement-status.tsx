import React from "react";
import { Status } from "@chakra-ui/react";

interface StatusStatusProps {
  status: string;
}

const StatusStatus: React.FC<StatusStatusProps> = ({ status }) => {
  const formatStatus = (input: string) => {
    return input.charAt(0).toUpperCase() + input.slice(1).toLowerCase();
  };

  const getStatusColor = (input: string) => {
    switch (input) {
      case "PENDING":
        return "blue";
      case "PROCESSING":
        return "yellow";
      case "COMPLETED":
        return "green";
      case "FAILED":
        return "red";
      default:
        return "gray";
    }
  };

  return (
    <Status.Root size="md" width="100px" colorPalette={getStatusColor(status)}>
      <Status.Indicator />
      {formatStatus(status)}
    </Status.Root>
  );
};

export default StatusStatus;
