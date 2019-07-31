CREATE TABLE "organization" (
  "id"          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  "location_id" VARCHAR(8)   NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "address"     VARCHAR(255) NOT NULL,
  "updated_at"  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  "created_at"  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

INSERT INTO "organizaion" ("location_id", "name", "address") VALUES ('14100', '山田太郎', '横浜市旭区');
INSERT INTO "organizaion" ("location_id", "name", "address") VALUES ('14100', '鈴木太郎', '横浜市瀬谷区');
