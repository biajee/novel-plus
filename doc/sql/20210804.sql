ALTER TABLE `novel_blockchain`.`book` 
ADD COLUMN `blockchain_token_id` VARCHAR(45) NULL AFTER `blockchain_address`,
ADD COLUMN `blockchain_token_supply` BIGINT(20) NULL AFTER `blockchain_token_id`,
ADD UNIQUE INDEX `blockchain_token_id_UNIQUE` (`blockchain_token_id` ASC);
;
