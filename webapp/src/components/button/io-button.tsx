import React from "react";
import {
  Button as CButton,
  ButtonProps as CButtonProps,
} from "@chakra-ui/react";

interface ButtonProps extends CButtonProps {
  label?: string;
}

const Button: React.FC<ButtonProps> = ({ children, ...props }) => {
  return (
    <CButton bg="#5b33fe" {...props}>
      {children}
    </CButton>
  );
};

export default Button;
