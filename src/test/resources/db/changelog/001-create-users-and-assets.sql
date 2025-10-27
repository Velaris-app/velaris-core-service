-- Users
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- Assets
CREATE TABLE assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(255),
    description VARCHAR(1024),
    purchase_price DECIMAL(19,2),
    currency VARCHAR(8),
    condition VARCHAR(64),
    purchase_year INT,
    quantity INT,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- User sessions
CREATE TABLE user_sessions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    device VARCHAR(128),
    ip_address VARCHAR(45),
    user_agent VARCHAR(256),
    created_at TIMESTAMP NOT NULL,
    last_used_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    access_token VARCHAR(512),
    refresh_token VARCHAR(512),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Recent activities
CREATE TABLE recent_activities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id UUID,
    asset_id BIGINT,
    activity_type VARCHAR(50),
    snapshot_before TEXT,
    snapshot_after TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recent_activity_asset
    FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE SET NULL
);

-- Foreign keys
ALTER TABLE assets ADD FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE recent_activities ADD CONSTRAINT fk_recent_activity_owner
FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE recent_activities ADD FOREIGN KEY (asset_id) REFERENCES assets(id);

-- Element collections
CREATE TABLE asset_images (
    asset_id BIGINT NOT NULL,
    image VARCHAR(1024),
    CONSTRAINT fk_asset_image FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
);

CREATE TABLE asset_tags (
    asset_id BIGINT NOT NULL,
    tag VARCHAR(64),
    CONSTRAINT fk_asset_tag FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_assets_owner_id ON assets(owner_id);
CREATE INDEX idx_assets_category ON assets(category);
CREATE INDEX idx_assets_created_at ON assets(created_at);