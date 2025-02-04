# ioB Spring Boilerplate

## Overview

This boilerplate project provides a foundational structure to kickstart new projects using Spring Boot. It incorporates
best practices and predefined configurations to ensure consistency and efficiency across projects.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Prerequisites](#prerequisites)
3. [Setup Instructions](#setup-instructions)
4. [Configuration](#configuration)
5. [Built-in Features](#built-in-features)
6. [Eventeum Configuration](#eventeum-configuration)
7. [Surikata Configuration](#surikata-configuration)
8. [Customizations](#customizations)

---

## Project Structure

```
root/
├── src/main/groovy     # Application source code
├── src/main/resources  # Configuration files (e.g., application.yml, liquibase, etc)
├── src/test/groovy     # Unit and integration tests
├── scripts             # Gradle extended configuration files
├── build.gradle        # Gradle configuration file
├── Dockerfile          # Docker container configuration
├── README.md           # Project documentation
```

- **Purpose**: Provide clarity on file locations and their respective roles.

---

## Prerequisites

Ensure the following dependencies and tools are installed:

1. **Java Development Kit (JDK):** Version 21 or higher.
2. **Gradle:** Version 8.11.1 or higher.
3. **Docker:** For containerization support.
4. **Database:** PostgreSQL.
5. **GitHub Personal Access Token**

---

## GitHub Personal Access Token Setup

To ensure the eventeum dependency is downloaded successfully, we need to set up a personal access token in our GitHub
account. You can review the
following [link](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens)
to configure one from your account.

Once the token is set in your account, you need to add the following fields into the `~/gradle/gradle.properties` in
your home directory.

```text
github.user=<username>
github.token=<token>
```

---

## Setup Instructions

Follow these steps to create a new project based on this boilerplate:

1. **Clone the Boilerplate Repository:**
   ```bash
   git clone <boilerplate-repo-url> <new-project-name>
   ```

2. **Navigate to the Project Directory:**
   ```bash
   cd <new-project-name>
   ```

3. **Execute the configuration script:**
   ```bash
   chmod +x configure.sh
   ./configure.sh <group_id> <artifact_id>
   ```

4. **Update the YAML Configuration:**
    - Update database credentials and connection URLs in `application.yml`.
    - Set up Eventeum and Surikata configurations as needed.

5. **Run the Application:**
   ```bash
   gradle bootRun
   ```

---

## Configuration

- **Environment Variables:** Ensure the following variables are set for different environments:
    - `EVENTEUM_DATABASE_*` : Eventeum database connection details.
    - `BLOCKCHAIN_DATABASE_*` : Blockchain database connection details.
    - `SURIKATA_DATABASE_*` : Surikata database connection details.
    - `BLOCKCHAIN_NODE_*` : Blockchain node connection settings.
    - `APP_ENV` : Application environment (e.g., `development`, `production`).

- **Profiles:** Use Spring Profiles (`application-{profile}.yml`) for environment-specific configurations.

- **Database Connections:**
    - Configured with HikariCP for connection pooling.
    - Supports PostgreSQL by default.

---

## Built-in Features

1. **Preconfigured Dependencies:**
    - Spring Boot Starter dependencies.
    - Database connector (PostgreSQL).

2. **Error Handling:**
    - Centralized exception handling with customizable response structures.

3. **Docker Support:**
    - Dockerfile included for containerization.

4. **Spock and Integration Tests:**
    - Spock and MockMvc preconfigured.
    - Test containers preconfigured.

5. **Eventeum Integration:**
    - Supports Ethereum event listening with preconfigured endpoints and health checks.
    - YAML configuration for event broadcasting, block strategies, and transaction confirmations.

6. **Surikata Integration:**
    - Converts blockchain contract events to domain events.
    - Preconfigured via YAML for seamless integration.

---

## Eventeum Configuration

Eventeum settings are defined in the `application.yml` file under the `ethereum` and `eventStore` sections:

- **Node Configuration:**
  ```yaml
  ethereum:
    nodes:
      - name: default
        url: ${EVENTEUM_ETH_NODE_PROTOCOL:http}://${EVENTEUM_ETH_NODE_HOST:localhost}:${EVENTEUM_ETH_NODE_PORT:8545}
        blockStrategy: ${EVENTEUM_ETH_BLOCK_STRATEGY:POLL}
        syncingThreshold: ${EVENTEUM_ETH_SYNCING_THRESHOLD:100}
        healthcheckInterval: ${EVENTEUM_ETH_HEALTH_CHECK_INTERVAL:3000}
  ```

- **Event Broadcasting:**
  ```yaml
  broadcaster:
    type: ${EVENTEUM_BROADCASTER:HTTP}
    cache:
      expirationMillis: ${EVENTEUM_CACHE_EXPIRATION_IN_MILLIS:6000000}
    event:
      confirmation:
        numBlocksToWait: ${EVENTEUM_NUM_BLOCKS_TO_WAIT:0}
  ```

- **Endpoints:** Ensure the correct URLs for contract and transaction events:
  ```yaml
  contractEventsUrl: ${HTTP_PROTOCOL_CONTRACT_EVENTS_URL:http}://${HTTP_PROTOCOL_CONTRACT_EVENTS_HOST:localhost}:${HTTP_PROTOCOL_CONTRACT_EVENTS_PORT:8080}/api/v1/surikata/contract-events
  transactionEventsUrl: ${HTTP_PROTOCOL_TRANSACTION_EVENTS_URL:http}://${HTTP_PROTOCOL_TRANSACTION_EVENTS_HOST:localhost}:${HTTP_PROTOCOL_TRANSACTION_EVENTS_PORT:8080}/api/v1/surikata/transactions
  ```

---

## Surikata Configuration

Surikata settings are defined in the `application.yml` file under the `surikata` section:

- **Database Connection:**
  ```yaml
  spring:
    datasource:
      surikata:
        url: jdbc:postgresql://${SURIKATA_DATABASE_HOST:localhost}:${SURIKATA_DATABASE_PORT:5432}/${SURIKATA_DATABASE_DB:surikata}
        username: ${SURIKATA_DATABASE_USERNAME:surikata}
        password: ${SURIKATA_DATABASE_PWD:surikata}
  ```

- **Event and Transaction Mapping:**
  ```yaml
  surikata:
    event:
      mappers:
        - TokenTransferred: io.builders.demo.event.TokenTransferredDltEvent
    transaction:
      mappers:
        - TokenTransferOrdered: io.builders.demo.event.TokenTransferFailedDltEvent
  ```

---

## Customizations

To tailor the boilerplate to your project needs:

1. **Replace Placeholder Code:**
    - Remove `.gitkeep` files and add relevant application code.

2. **Extend Dependencies:**
    - Add new dependencies to `build.gradle` as required.

3. **Update Documentation:**
    - Ensure the `README.md` and other documents reflect your project's details.
