#!/bin/bash

# Ensure the script stops on errors
set -e

# Function to print usage
usage() {
  echo "Usage: $0 <new_group> <new_artifact> [options]"
  echo ""
  echo "Options:"
  echo "  -a <existing_artifact>        Existing artifact to replace. Defaults to 'demo'."
  echo "  -d <directory>                Project directory. Defaults to the current directory."
  echo "  -g <existing_group>           Existing group to replace. Defaults to 'io.builders'."
  echo "  -e <exclude_paths>            Comma-separated list of paths to exclude."
  exit 1
}

# Check if the required arguments are provided
if [ "$#" -lt 2 ]; then
  usage
fi

# Assign variables
new_group=$1
new_artifact=$2
directory=$(pwd) # Default to current directory
existing_artifact="demo"
existing_group="io.builders"
exclude_paths=(
  "src/main/resources/db"
  "src/test/resources/db"
)

# Parse options
shift 2
while getopts "a:d:g:e:" opt; do
    case "$opt" in
        a) existing_artifact="$OPTARG" ;;
        d) directory="$OPTARG" ;;
        g) existing_group="$OPTARG" ;;
        e) IFS=',' read -ra exclude_paths <<< "$OPTARG" ;;
        *) usage ;;
    esac
done

# Validate project directory
if [ ! -d "$directory" ]; then
  echo "Error: Directory '$directory' does not exist."
  exit 1
fi

# Print summary of the operation
echo "Configuring project in directory: $(basename "$directory")"
echo "Replacing group '$existing_group' with '$new_group'"
echo "Replacing artifact '$existing_artifact' with '$new_artifact'"

# Build the find exclude options
exclude_args=()
for exclude_path in "${exclude_paths[@]}"; do
  exclude_args+=("! -path \"$directory/$exclude_path/*\"")
done

# Perform the replacement
find_command="find \"$directory\" -type f ! -name \"configure.sh\" ${exclude_args[*]}"
eval "$find_command" | while IFS= read -r file; do
  mime_type=$(file --mime-type -b "$file")
  if [[ "$mime_type" == text/* ]]; then
    sed -i '' -e "s/$existing_group/$new_group/g" -e "s/$existing_artifact/$new_artifact/g" "$file"
  fi
done

# Move files from the old package structure to the new one
old_path="$directory/src/main/groovy/$(echo "$existing_group" | tr '.' '/')/$existing_artifact"
old_test_path="$directory/src/test/groovy/$(echo "$existing_group" | tr '.' '/')/$existing_artifact"
new_path="$directory/src/main/groovy/$(echo "$new_group" | tr '.' '/')/$new_artifact"
new_test_path="$directory/src/test/groovy/$(echo "$new_group" | tr '.' '/')/$new_artifact"

if [ -d "$old_path" ]; then
  echo "Moving files from '$(basename "$old_path")' to '$(basename "$new_path")'"
  mkdir -p "$new_path"
  mv "$old_path"/* "$new_path"/
  rmdir -p "$old_path" || true
  echo "Moving test files from '$(basename "$old_test_path")' to '$(basename "$new_test_path")'"
  mkdir -p "$new_test_path"
  mv "$old_test_path"/* "$new_test_path"/
  rmdir -p "$old_test_path" || true
else
  echo "Warning: Source path '$(basename "$old_path")' does not exist. Skipping file move."
fi

# Completion message
echo "Replacement and file reorganization complete."
