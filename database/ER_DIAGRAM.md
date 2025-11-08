# Entity-Relationship Diagram

## Entities and Relationships

```
┌─────────────────────┐
│      USERS          │
├─────────────────────┤
│ PK user_id          │
│    username         │
│    password_hash    │
│    full_name        │
│    email            │
│    phone            │
│    role             │
│    created_at       │
│    is_active        │
└─────────────────────┘
         │ 1
         │
         │ has
         │
         │ 1..*
         ▼
┌─────────────────────┐
│     ACCOUNTS        │
├─────────────────────┤
│ PK account_id       │
│ FK user_id          │
│    account_number   │
│    account_type     │
│    balance          │
│    created_at       │
│    last_updated     │
│    status           │
└─────────────────────┘
         │ 1
         │
         │ performs
         │
         │ 0..*
         ▼
┌─────────────────────┐
│   TRANSACTIONS      │
├─────────────────────┤
│ PK transaction_id   │
│ FK account_id       │
│    transaction_type │
│    amount           │
│    balance_before   │
│    balance_after    │
│    description      │
│    transaction_date │
│    status           │
└─────────────────────┘
```

## Relationships

1. **USERS → ACCOUNTS** (1:N)
   - One user can have multiple accounts
   - Each account belongs to one user

2. **ACCOUNTS → TRANSACTIONS** (1:N)
   - One account can have multiple transactions
   - Each transaction belongs to one account

## Business Rules

1. User must have unique username and email
2. Account number must be unique (10 digits)
3. Balance cannot be negative
4. Transaction amount must be positive
5. Withdrawal amount cannot exceed current balance
6. Admin role has full access, user role has limited access
7. Passwords must be stored as SHA-256 hash
