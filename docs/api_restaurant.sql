-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 30, 2025 at 02:21 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `api_restaurant`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(3, 'Món chay'),
(2, 'món luộc'),
(1, 'Món nướng'),
(4, 'Đồ uống');

-- --------------------------------------------------------

--
-- Table structure for table `dishes`
--

CREATE TABLE `dishes` (
  `id` bigint(20) NOT NULL,
  `description` text DEFAULT NULL,
  `image_url` text DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `start_date` datetime NOT NULL,
  `status` enum('DELETED','ON_SALE','STOPPED') DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dishes`
--

INSERT INTO `dishes` (`id`, `description`, `image_url`, `last_modified`, `name`, `price`, `start_date`, `status`, `category_id`) VALUES
(1, 'Gà ta nướng với muối ớt cay nồng, thơm lừng', NULL, '2025-10-30 14:27:06', 'Gà nướng muối ớt', 650000, '2025-10-30 14:20:22', 'DELETED', 1),
(2, 'Phở bò hương vị truyền thống Việt Nam, nước dùng đậm đà.', 'https://example.com/images/pho-bo.jpg', '2025-10-30 00:00:00', 'Phở Bò Truyền Thống', 45000, '2025-10-30 00:00:00', 'ON_SALE', 1),
(3, 'Bún chả nướng Hà Nội với nước mắm chua ngọt đặc trưng.', 'https://example.com/images/bun-cha.jpg', '2025-10-30 00:00:00', 'Bún Chả Hà Nội', 40000, '2025-10-30 00:00:00', 'ON_SALE', 1),
(4, 'Cơm gà thơm ngon, gà luộc vàng ươm, nước chấm gừng cay nhẹ.', 'https://example.com/images/com-ga.jpg', '2025-10-30 00:00:00', 'Cơm Gà Hải Nam', 50000, '2025-10-30 00:00:00', 'ON_SALE', 2),
(5, 'Gỏi cuốn tươi mát với tôm, thịt, rau sống và bún.', 'https://example.com/images/goi-cuon.jpg', '2025-10-30 00:00:00', 'Gỏi Cuốn Tôm Thịt', 30000, '2025-10-30 00:00:00', 'ON_SALE', 3),
(6, 'Mì Quảng đặc sản miền Trung với thịt heo, tôm và đậu phộng.', 'https://example.com/images/mi-quang.jpg', '2025-10-30 00:00:00', 'Mì Quảng Đà Nẵng', 42000, '2025-10-30 00:00:00', 'ON_SALE', 1),
(7, 'Bánh xèo giòn rụm, nhân tôm thịt, ăn kèm rau sống và nước chấm.', 'https://example.com/images/banh-xeo.jpg', '2025-10-30 00:00:00', 'Bánh Xèo Miền Tây', 35000, '2025-10-30 00:00:00', 'ON_SALE', 3),
(8, 'Lẩu Thái chua cay với hải sản tươi sống và rau ăn kèm.', 'https://example.com/images/lau-thai.jpg', '2025-10-30 00:00:00', 'Lẩu Thái Hải Sản', 120000, '2025-10-30 00:00:00', 'ON_SALE', 4),
(9, 'Cà ri gà thơm nồng gia vị Ấn Độ, dùng kèm cơm hoặc bánh mì.', 'https://example.com/images/ca-ri-ga.jpg', '2025-10-30 00:00:00', 'Cà Ri Gà Kiểu Ấn', 55000, '2025-10-30 00:00:00', 'ON_SALE', 2),
(10, 'Bánh canh cua đặc sản miền Nam với thịt cua tươi và chả cá.', 'https://example.com/images/banh-canh-cua.jpg', '2025-10-30 00:00:00', 'Bánh Canh Cua', 48000, '2025-10-30 00:00:00', 'ON_SALE', 1),
(11, 'Món chè truyền thống với đậu, thạch và nước cốt dừa.', 'https://example.com/images/che-ba-mau.jpg', '2025-10-30 00:00:00', 'Chè Ba Màu', 25000, '2025-10-30 00:00:00', 'ON_SALE', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`);

--
-- Indexes for table `dishes`
--
ALTER TABLE `dishes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgbu6erefir17660qutbbjnma7` (`category_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `dishes`
--
ALTER TABLE `dishes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `dishes`
--
ALTER TABLE `dishes`
  ADD CONSTRAINT `FKgbu6erefir17660qutbbjnma7` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
