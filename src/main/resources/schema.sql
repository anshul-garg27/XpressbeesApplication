-- schema.sql (PostgreSQL)

CREATE TABLE orders (
                        order_id SERIAL PRIMARY KEY,
                        customer_name VARCHAR(255) NOT NULL,
                        address TEXT NOT NULL,
                        delivery_status VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);