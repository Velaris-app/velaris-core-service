CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(255),
    description TEXT,
    purchase_price NUMERIC(19,2),
    currency VARCHAR(8),
    condition VARCHAR(64),
    purchase_year INT,
    quantity INT,
    owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Element collections
CREATE TABLE asset_images (
    asset_id BIGINT NOT NULL REFERENCES assets(id) ON DELETE CASCADE,
    image TEXT
);

CREATE TABLE asset_tags (
    asset_id BIGINT NOT NULL REFERENCES assets(id) ON DELETE CASCADE,
    tag TEXT
);

-- Indexes to optimize stats
CREATE INDEX idx_assets_owner_id ON assets(owner_id);
CREATE INDEX idx_assets_category ON assets(category);
CREATE INDEX idx_assets_created_at ON assets(created_at);