/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE `upload_invoice` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `invoice_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date of invoice ',
  `due_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Due date of invoice ',
  `status` varchar(10) DEFAULT NULL COMMENT 'Status',
  `currency` varchar(128) NOT NULL DEFAULT 'USD' COMMENT 'Currency code by ISO 4217 (https://en.wikipedia.org/wiki/ISO_4217)',
  `exchange_rate` decimal(16,6) NOT NULL DEFAULT '1.000000',
  `discount` decimal(12,2) DEFAULT NULL COMMENT 'Discount',
  `tax` decimal(12,2) DEFAULT NULL COMMENT 'Tax',
  `paid` decimal(12,2) DEFAULT NULL,
  `total` decimal(12,2) DEFAULT NULL COMMENT 'Total in invoice',
  `sparse` tinyint(1) DEFAULT NULL COMMENT 'Is invoice sparse or not',
  `internal_id` varchar(255) NOT NULL COMMENT 'Internal id of invoice in source system',
  `source_system` varchar(255) NOT NULL COMMENT 'Name of source system',
  `invoice_number` varchar(255) NOT NULL COMMENT 'Invoice number in external system',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Is invoice deleted or not',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date of creation ',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `customer_internal_id` varchar(255) NOT NULL COMMENT 'Internal customer id in source system',
  `notes` varchar(5000) DEFAULT NULL COMMENT 'Invoice notes',
  `custom_fields` json DEFAULT NULL,
  `close_date` DATETIME NULL DEFAULT NULL,
  `pdf_url` varchar(200) DEFAULT NULL,
  `terms` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `upload_invoice_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `description` text NOT NULL COMMENT 'Description of invoice item',
  `quantity` decimal(12,2) DEFAULT NULL COMMENT 'Quantity',
  `rate` decimal(16,6) DEFAULT NULL COMMENT 'Rate',
  `amount` decimal(12,2) DEFAULT NULL,
  `internal_id` varchar(255) NOT NULL COMMENT 'Internal invoice item id in source system',
  `source_system` varchar(255) NOT NULL COMMENT 'Contact source system',
  `name` text,
  `custom_fields` json DEFAULT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `item_type` varchar(31) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

ALTER TABLE `upload_invoice_item`
ADD CONSTRAINT `upload_invoice_item_FK`
FOREIGN KEY (id) REFERENCES upload_invoice(id);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

