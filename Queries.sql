create database myfinbank;
use myfinbank;
CREATE TABLE `accounts` (
  `accountId` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `accountType` varchar(255) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`accountId`),
  KEY `userId` (`userId`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`)
) ;

CREATE TABLE `transactions` (
  `transactionId` int NOT NULL AUTO_INCREMENT,
  `accountId` int DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `transactionType` varchar(255) DEFAULT NULL,
  `transactionDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transactionId`),
  KEY `accountId` (`accountId`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`accountId`) REFERENCES `accounts` (`accountId`)
);



CREATE TABLE `users` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `userType` varchar(255) NOT NULL,
  `status` tinyint(1) DEFAULT '1',
  `attempts` int DEFAULT '0',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `username` (`username`)
)








