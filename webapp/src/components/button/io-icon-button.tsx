import {
  IconButton as CIconButton,
  IconButtonProps as CIconButtonProps,
} from "@chakra-ui/react";
import React from "react";

interface IconButtonProps extends CIconButtonProps {
  ariaLabel?: string;
}

const IconButton: React.FC<IconButtonProps> = ({
  ariaLabel = "View",
  ...props
}) => {
  return (
    <CIconButton bg="#5b33fe" aria-label={ariaLabel} {...props}>
      {props.children}
    </CIconButton>
  );
};

export default IconButton;
