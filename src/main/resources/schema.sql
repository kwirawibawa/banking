-- Table: account
CREATE TABLE account (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT DEFAULT 0
);

-- Table: transaction
CREATE TABLE transaction (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    type VARCHAR(10) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account FOREIGN KEY(account_id) REFERENCES account(id) ON DELETE CASCADE
);
