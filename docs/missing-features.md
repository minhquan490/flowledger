# Missing Features vs Domain Model

This document lists features described in [features.md](/home/quan/projects/FlowLedger/docs/features.md) that are not
currently represented in the `domain` module model (based on existing aggregates/entities/views).

## Identity & User Management
- User registration
- User login / logout
- Password reset
- Email verification
- Multi-factor authentication
- OAuth login (Google, GitHub, etc.)
- Account deletion

## Transaction Management
- Transaction notes and location
- Transaction status & splitting
- Transaction reconciliation

## Budget Management
- Budget alerts & notifications

## Saving Goals
- Automatic contributions

## Recurring Transactions
- Skip, pause & resume occurrence

## Transfers
- Scheduled transfers
- Transfer fees

## Multi-Currency
- Exchange rate history and overrides

## Reporting
- Custom report generation

## Data Import / Export
- Automatic bank synchronization
- Scheduled backup

## Notifications & Alerts
- Delivery channels and schedules (push/email types and schedules)

## Search & Filtering
- Full-text search
- Saved filters
- Query history

## Automation Rules
- Rule-based tagging, alerts, transaction creation
- Rule priority management
- Rule testing & simulation

## Attachments & Documents
- Document preview & search
- Document categorization & export

## Dashboard
- Dashboard widgets and layouts

## Collaboration
- Shared accounts & budgets
- Family finance tracking
- Permission management beyond core RBAC
- Activity log
- Comments on transactions

## Security
- Encryption for sensitive data
- Secure password storage
- Login activity monitoring
- Suspicious login detection
- Data privacy controls

## System / Platform
- Rate limiting
- API authentication & versioning
- Webhooks
- Background jobs & event system
- Health monitoring
- Metrics & observability
