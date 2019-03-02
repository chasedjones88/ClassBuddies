-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 02, 2017 at 12:02 AM
-- Server version: 10.0.31-MariaDB-cll-lve
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `classbuddies`
--

-- --------------------------------------------------------

--
-- Table structure for table `chatMessages`
--

CREATE TABLE `chatMessages` (
  `messageID` int(11) NOT NULL,
  `messageTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `chatID` int(11) NOT NULL,
  `senderID` int(11) NOT NULL,
  `messageText` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `chats`
--

CREATE TABLE `chats` (
  `chatID` int(11) NOT NULL,
  `chatOwner` int(11) NOT NULL,
  `chatFriendID` int(11) NOT NULL,
  `chatDesc` varchar(50) DEFAULT NULL,
  `chatWinURL` varchar(128) DEFAULT NULL,
  `messageCount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `chatUsers`
--

CREATE TABLE `chatUsers` (
  `id` int(11) NOT NULL,
  `chatID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `id` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `friendID` int(11) DEFAULT NULL,
  `friendNote` varchar(160) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `groupmembers`
--

CREATE TABLE `groupmembers` (
  `id` int(11) NOT NULL,
  `groupID` int(11) NOT NULL,
  `userID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `groupID` int(11) NOT NULL,
  `ownerID` int(11) NOT NULL,
  `groupName` varchar(100) NOT NULL,
  `groupClass` varchar(160) NOT NULL,
  `groupDesc` text,
  `groupLocationText` varchar(255) DEFAULT NULL,
  `groupCoordLat` decimal(19,16) DEFAULT NULL,
  `groupCoordLong` decimal(19,16) DEFAULT NULL,
  `groupChatID` int(11) DEFAULT NULL,
  `groupStartTime` datetime NOT NULL,
  `groupEndTime` datetime NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` int(11) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `userPWHash` varchar(32) NOT NULL,
  `userEmail` varchar(50) NOT NULL,
  `userClasses` varchar(255) DEFAULT NULL,
  `userMajor` varchar(50) DEFAULT NULL,
  `userValidation` varchar(255) DEFAULT NULL,
  `userIsValidated` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chatMessages`
--
ALTER TABLE `chatMessages`
  ADD UNIQUE KEY `messageID` (`messageID`);

--
-- Indexes for table `chats`
--
ALTER TABLE `chats`
  ADD PRIMARY KEY (`chatID`),
  ADD UNIQUE KEY `chatID` (`chatID`);

--
-- Indexes for table `chatUsers`
--
ALTER TABLE `chatUsers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `groupmembers`
--
ALTER TABLE `groupmembers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`groupID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `userName` (`userName`),
  ADD UNIQUE KEY `userID` (`userID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chatMessages`
--
ALTER TABLE `chatMessages`
  MODIFY `messageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20000;
--
-- AUTO_INCREMENT for table `chats`
--
ALTER TABLE `chats`
  MODIFY `chatID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=500000;
--
-- AUTO_INCREMENT for table `chatUsers`
--
ALTER TABLE `chatUsers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15000;
--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10000;
--
-- AUTO_INCREMENT for table `groupmembers`
--
ALTER TABLE `groupmembers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5000;
--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `groupID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=250000;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100000;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
