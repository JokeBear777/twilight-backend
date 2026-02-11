-- tag_stats 초기 집계
INSERT INTO tag_stats (tag_id, usage_count, updated_at)
SELECT
    t.tag_id,
    COALESCE(COUNT(bt.tag_id), 0) AS usage_count,
    NOW()
FROM tag t
         LEFT JOIN book_tags bt
                   ON t.tag_id = bt.tag_id
GROUP BY t.tag_id
ON DUPLICATE KEY UPDATE
                     usage_count = VALUES(usage_count),
                     updated_at = NOW();

DELIMITER $$

CREATE TRIGGER trg_book_tags_insert
    AFTER INSERT ON book_tags
    FOR EACH ROW
BEGIN
    INSERT INTO tag_stats (tag_id, usage_count, updated_at)
    VALUES (NEW.tag_id, 1, NOW())
    ON DUPLICATE KEY UPDATE
                         usage_count = usage_count + 1,
                         updated_at = NOW();
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_book_tags_delete
    AFTER DELETE ON book_tags
    FOR EACH ROW
BEGIN
    UPDATE tag_stats
    SET usage_count = usage_count - 1,
        updated_at = NOW()
    WHERE tag_id = OLD.tag_id;
END$$

DELIMITER ;