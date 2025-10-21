CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(255),
    description VARCHAR(1024),
    purchase_price NUMERIC(19,2),
    currency VARCHAR(8),
    condition VARCHAR(64),
    purchase_year INT,
    quantity INT,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

ALTER TABLE assets ADD FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE;

-- Element collections
CREATE TABLE asset_images (
    asset_id BIGINT NOT NULL,
    image VARCHAR(1024)
);
ALTER TABLE asset_images ADD FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE;

CREATE TABLE asset_tags (
    asset_id BIGINT NOT NULL,
    tag VARCHAR(64)
);
ALTER TABLE asset_tags ADD FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE;

-- Indexes
CREATE INDEX idx_assets_owner_id ON assets(owner_id);
CREATE INDEX idx_assets_category ON assets(category);
CREATE INDEX idx_assets_created_at ON assets(created_at);