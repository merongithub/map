CREATE TABLE IF NOT EXISTS `reference` (
   `reference_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
   `reference_type` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
   `shape` geometry NOT NULL,
  `created` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  SPATIAL KEY `shape` (`shape`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


INSERT INTO `reference` (`shape`,`reference_name`,`reference_type`)
VALUES (St_GeomFromText('POINT (-17.821667 16.148889)', 4326), 'Bar Alba', 'Bar');


INSERT INTO `reference` (`shape`,`reference_name`,`reference_type`)
VALUES (St_GeomFromText('POINT (-92.821667 9.148889)', 4326), 'Dadu', 'Cafe');