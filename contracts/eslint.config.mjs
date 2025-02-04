import globals from "globals";
import pluginJs from "@eslint/js";
import tseslint from "typescript-eslint";

export default [
	{ languageOptions: { globals: globals.node } },
	pluginJs.configs.recommended,
	...tseslint.configs.recommended,
	{
		ignores: ["typechain-types/", "libs/", "coverage/","build/"],

	},
	{
		rules: {
			"@typescript-eslint/no-unused-expressions": "off",
			"@typescript-eslint/no-non-null-assertion": "off",
			"@typescript-eslint/explicit-function-return-type": "off"
		}
	}
];