-- ================================
-- CATEGORY VIEW
-- ================================
--DROP VIEW IF EXISTS stats_category_view;
--
--CREATE VIEW stats_category_view AS
--SELECT
--    ROW_NUMBER() OVER (ORDER BY a.owner_id) AS id,
--    a.owner_id,
--    a.category,
--    SUM(COALESCE(a.purchase_price, 0) * COALESCE(a.quantity, 0)) AS total_value,
--    SUM(COALESCE(a.quantity, 0)) AS item_count,
--    COUNT(*) AS unique_assets,
--    CAST(
--        SUM(COALESCE(a.purchase_price,0) * COALESCE(a.quantity,0)) * 100.0 /
--        SUM(SUM(COALESCE(a.purchase_price,0) * COALESCE(a.quantity,0))) OVER(PARTITION BY a.owner_id)
--    AS DOUBLE) AS percentage_of_total
--FROM assets a
--WHERE a.category IS NOT NULL
--GROUP BY a.owner_id, a.category;

-- ================================
-- OVERVIEW VIEW
-- ================================
--DROP VIEW IF EXISTS stats_overview_view;
--
--CREATE VIEW stats_overview_view AS
--SELECT
--    ROW_NUMBER() OVER (ORDER BY a.owner_id) AS id,
--    a.owner_id,
--    SUM(COALESCE(a.purchase_price, 0) * COALESCE(a.quantity, 0)) AS total_value,
--    SUM(COALESCE(a.quantity, 0)) AS total_items,
--    COUNT(*) AS asset_count,
--    'USD' AS currency
--FROM assets a
--GROUP BY a.owner_id;

-- ================================
-- TREND VIEW
-- ================================
--DROP VIEW IF EXISTS stats_trend_view;
--
--CREATE VIEW stats_trend_view AS
--SELECT
--    ROW_NUMBER() OVER(ORDER BY owner_id, created_date) AS id,
--    owner_id,
--    created_date AS date,
--    SUM(total_price) AS total_value,
--    COUNT(*) AS items_added
--FROM (
--    SELECT
--        owner_id,
--        created_at AS created_date,
--        COALESCE(purchase_price, 0) * COALESCE(quantity, 0) AS total_price
--    FROM assets
--) AS sub
--GROUP BY owner_id, created_date;

-- ================================
-- TREND DIFF VIEW
-- ================================
DROP VIEW IF EXISTS stats_trend_diff_view;

CREATE VIEW stats_trend_diff_view AS
SELECT
    ROW_NUMBER() OVER() AS id,
    owner_id,
    date,
    total_value,
    total_value - COALESCE(LAG(total_value) OVER(PARTITION BY owner_id ORDER BY date), 0) AS delta_value,
    CAST(
        CASE
            WHEN COALESCE(LAG(total_value) OVER(PARTITION BY owner_id ORDER BY date),0) = 0
            THEN 0
            ELSE (total_value - LAG(total_value) OVER(PARTITION BY owner_id ORDER BY date)) * 100.0 /
                 LAG(total_value) OVER(PARTITION BY owner_id ORDER BY date)
        END
    AS DOUBLE) AS delta_percent
FROM stats_trend_view;

-- ================================
-- RECENT ACTIVITIES VIEW
-- ================================
--DROP VIEW IF EXISTS recent_activities_view;
--
--CREATE VIEW recent_activities_view AS
--SELECT
--    ROW_NUMBER() OVER() AS id,
--    a.owner_id,
--    a.id AS asset_id,
--    a.name,
--    a.category,
--    a.purchase_price,
--    a.quantity,
--    a.created_at,
--    a.updated_at,
--    CASE
--        WHEN a.created_at = a.updated_at THEN 'CREATED'
--        ELSE 'UPDATED'
--    END AS activity_type
--FROM assets a
--ORDER BY a.updated_at DESC;

-- ================================
-- TAG VIEW
-- ================================
--DROP VIEW IF EXISTS stats_tag_view;
--
--CREATE VIEW stats_tag_view AS
--SELECT
--    ROW_NUMBER() OVER() AS id,
--    a.owner_id,
--    t.tag,
--    COUNT(*) AS assets_count,
--    SUM(COALESCE(a.purchase_price, 0) * COALESCE(a.quantity, 0)) AS total_value
--FROM assets a
--JOIN asset_tags t ON t.asset_id = a.id
--GROUP BY a.owner_id, t.tag;

-- ================================
-- TOP MOVERS VIEW
-- ================================
--DROP VIEW IF EXISTS stats_top_movers_view;
--
--CREATE VIEW stats_top_movers_view AS
--SELECT
--    ROW_NUMBER() OVER() AS id,
--    a.owner_id,
--    a.id AS asset_id,
--    a.name,
--    a.category,
--    a.purchase_price * a.quantity AS total_value,
--    a.purchase_price * a.quantity - COALESCE(prev.total_value, 0) AS delta_value
--FROM assets a
--LEFT JOIN (
--    SELECT id AS asset_id, purchase_price * quantity AS total_value
--    FROM assets
--    WHERE created_at < CURRENT_DATE
--) prev ON a.id = prev.asset_id
--ORDER BY delta_value DESC;

-- ================================
-- CATEGORY TREND VIEW
-- ================================
--DROP VIEW IF EXISTS stats_category_trend_view;
--
--CREATE VIEW stats_category_trend_view AS
--SELECT
--    ROW_NUMBER() OVER() AS id,
--    owner_id,
--    category,
--    created_at AS created_date,
--    SUM(COALESCE(purchase_price, 0) * COALESCE(quantity, 0)) AS total_value
--FROM assets
--GROUP BY owner_id, category, created_at;