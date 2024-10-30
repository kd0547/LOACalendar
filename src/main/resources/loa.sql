CREATE TABLE `member`
(
    `id`       Long PRIMARY KEY AUTO_INCREMENT,
    `email`    varchar(255) UNIQUE NOT NULL,
    `username` varchar(255),
    `password` varchar(255)        NOT NULL,
    `role`     varchar(255)
);

CREATE TABLE `discode_Info`
(
    `id`                Long PRIMARY KEY AUTO_INCREMENT,
    `calendar_id`       Long,
    `member_email`      varchar(255),
    `server_owner_id`   Long,
    `discode_server_id` long,
    `channel_id`        Long,
    `alarmYN`           varchar(5) DEFAULT 'N'
);

CREATE TABLE `calendar`
(
    `calendar_id`   Long PRIMARY KEY AUTO_INCREMENT,
    `calendar_name` varchar(255),
    `memberId`      Long
);

CREATE TABLE `calendar_detail`
(
    `calendar_detail_id` Long PRIMARY KEY AUTO_INCREMENT,
    `calendar_id`        Long,
    `raidPlan_id`        Long,
    `user_id`            Long
);

CREATE TABLE `RaidPlan`
(
    `raid_plan_id` Long PRIMARY KEY AUTO_INCREMENT,
    `raid_type`    varchar(255),
    `start_date`   LocalDate,
    `start_time`   LocalTime,
    `memberId`     Long
);

CREATE TABLE `guild`
(
    `guild_id`     Long PRIMARY KEY AUTO_INCREMENT,
    `guild_name`   varchar(255),
    `discode_auth` varchar(255),
    `memberId`     Long
);

CREATE TABLE `User`
(
    `user_id`      Long PRIMARY KEY AUTO_INCREMENT,
    `guild_id`     Long,
    `username`     varchar(255),
    `level`        float,
    `class`        varchar(255),
    `memberId`     Long,
    `phone_number` varchar(255)
);

ALTER TABLE `discode_Info`
    ADD FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`calendar_id`);

ALTER TABLE `calendar`
    ADD FOREIGN KEY (`memberId`) REFERENCES `Member` (`id`);

ALTER TABLE `calendar_detail`
    ADD FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`calendar_id`);

ALTER TABLE `calendar_detail`
    ADD FOREIGN KEY (`raidPlan_id`) REFERENCES `RaidPlan` (`raid_plan_id`);

ALTER TABLE `calendar_detail`
    ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `RaidPlan`
    ADD FOREIGN KEY (`memberId`) REFERENCES `Member` (`id`);

ALTER TABLE `guild`
    ADD FOREIGN KEY (`memberId`) REFERENCES `Member` (`id`);

ALTER TABLE `User`
    ADD FOREIGN KEY (`memberId`) REFERENCES `Member` (`id`);

ALTER TABLE `guild`
    ADD FOREIGN KEY (`discode_auth`) REFERENCES `guild` (`guild_name`);
