export const camelToTitleCase = (input: string): string => {
  return input
    .replace(/([A-Z])/g, " $1") // Add space before uppercase letters
    .replace(/^./, (char) => char.toUpperCase()) // Capitalize the first letter
    .trim(); // Remove any leading spaces
};
