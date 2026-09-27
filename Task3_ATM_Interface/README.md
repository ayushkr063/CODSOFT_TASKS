# CodSoft Internship - Task 3: ATM Interface

An interactive, feature-rich ATM Interface implemented in Java, featuring both a modern **Swing Graphical User Interface (GUI)** and a **Terminal Command-Line Interface (CLI)**.

---

## 🚀 Quick Start

### 1. Using the Shell Script (Easiest)

Navigate to the project directory and run:

```bash
cd Task3_ATM_Interface
chmod +x run.sh
./run.sh
```

**Options with `run.sh`:**
- `./run.sh` &mdash; Launches the application (prompts for GUI or CLI mode).
- `./run.sh --gui` &mdash; Launches the Graphical User Interface directly.
- `./run.sh --cli` &mdash; Launches the Terminal Interface directly.
- `./run.sh --test` &mdash; Executes automated verification test suite.

---

### 2. Using Standard Java Commands

#### Step 1: Navigate & Compile
```bash
cd Task3_ATM_Interface
javac *.java
```

#### Step 2: Run

- **Default Launcher / GUI Mode**:
  ```bash
  java Main
  ```
  *(or `java Main --gui`)*

- **Terminal / Console Mode**:
  ```bash
  java Main --cli
  ```

- **Run Automated Test Suite**:
  ```bash
  java ATMTest
  ```

---

## 🔑 Demo Account Credentials

A pre-configured demo account is automatically initialized on startup:

| Field | Value |
| :--- | :--- |
| **Account Holder** | Alexander Mitchell |
| **Account Number** | `6543210987` |
| **Default PIN** | `1234` |
| **Starting Balance** | `$2,500.00` |

---

## 💡 Key Features

- **Withdrawal**: Validates sufficient balance, minimum amounts, daily limits, and rejects overdrafts.
- **Deposit**: Validates transaction limits and instantly updates account balance.
- **Check Balance**: Displays up-to-date account balance with masked account details.
- **Transaction History**: Tracks timestamps, transaction types, amounts, and post-transaction balances.
- **Security & PIN Management**: Allows changing your 4-digit PIN with validation.
- **Dual UI Modes**: Modern Swing UI with dark theme aesthetics or clean CLI terminal menu.
