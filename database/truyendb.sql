-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 02, 2019 lúc 06:27 AM
-- Phiên bản máy phục vụ: 10.1.33-MariaDB
-- Phiên bản PHP: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `truyendb`
--

DELIMITER $$
--
-- Thủ tục
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `payChapter` (IN `userSend` BIGINT, IN `userReceived` BIGINT, IN `chapterID` BIGINT, IN `storyID` BIGINT, IN `price` BIGINT, IN `payType` INT, OUT `result` BIT)  BEGIN
	DECLARE exit handler FOR SQLEXCEPTION
  	BEGIN
    	ROLLBACK;
        SET result=0;
  	END;
    START TRANSACTION;

	INSERT INTO pay(userSend,chapterId,storyId,userReceived,money,type) VALUES (userSend,chapterID,storyID,userReceived,price,payType);    
 	UPDATE user SET gold=gold-price WHERE id = userSend;
    IF (userReceived IS NOT NULL) THEN
    	UPDATE user SET gold=gold+price WHERE id = userReceived;
	END IF;
    SET result = 1;

	-- if no errors happened yet, commit transaction
	COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `saveRating` (IN `userID` BIGINT, IN `storyID` BIGINT, IN `myLocationIP` VARCHAR(50), IN `myRating` INT, OUT `result` FLOAT)  NO SQL
BEGIN
	DECLARE average FLOAT DEFAULT 0;
	DECLARE exit handler FOR SQLEXCEPTION
  	BEGIN
    	ROLLBACK;
        SET result=-1;
  	END;
    START TRANSACTION;	
    
	INSERT INTO user_rating(userId,storyId,locationIP,rating) VALUES (userID,storyID,myLocationIP,myRating);
 		
SELECT 
    AVG(rating)
INTO average FROM
    user_rating
WHERE
    storyId = storyID;
    
UPDATE story 
SET 
    rating = average
WHERE
    id = storyID;
    SET result = average;

	-- if no errors happened yet, commit transaction
	COMMIT;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên Thể Loại',
  `metatitle` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'MetaTitle Thể Loại',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `createBy` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên Người Tạo',
  `modifiedDate` datetime DEFAULT NULL COMMENT 'Ngày Sửa',
  `modifiedBy` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Người sửa',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Thể Loại'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thể Loại';

--
-- Đang đổ dữ liệu cho bảng `category`
--

INSERT INTO `category` (`id`, `name`, `metatitle`, `createDate`, `createBy`, `modifiedDate`, `modifiedBy`, `status`) VALUES
(1, 'Đô Thị', 'do-thi', '2018-09-13 09:22:08', 'administrator', NULL, NULL, 1),
(2, 'Tiên Hiệp', 'tien-hiep', '2018-09-13 09:25:14', 'administrator', NULL, NULL, 1),
(3, 'Huyền Ảo', 'huyen-ao', '2018-09-13 09:25:14', 'administrator', NULL, NULL, 1),
(4, 'Kiếm Hiệp', 'kiem-hiep', '2018-09-13 09:25:14', 'administrator', NULL, NULL, 1),
(5, 'Võng Du', 'vong-du', '2018-11-07 22:07:35', 'administrator', NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chapter`
--

CREATE TABLE `chapter` (
  `id` bigint(20) NOT NULL COMMENT 'ID Chapter',
  `chapterNumber` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Chương Thứ',
  `serial` float NOT NULL COMMENT 'Số thứ tự chương',
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên Chương',
  `countView` int(11) DEFAULT '0' COMMENT 'Số View Chương',
  `storyId` bigint(20) DEFAULT NULL COMMENT 'ID Truyện',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nội dung chương',
  `wordCount` int(11) NOT NULL DEFAULT '0' COMMENT 'Số chữ của chương',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Đăng',
  `userPosted` bigint(20) DEFAULT NULL COMMENT 'ID Người Đăng',
  `price` bigint(20) DEFAULT '0' COMMENT 'Giá Chương Trả Phí',
  `dealine` datetime DEFAULT NULL COMMENT 'Ngày Hết Hạn Trả Phí',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Chương'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Chapter Truyện';

--
-- Đang đổ dữ liệu cho bảng `chapter`
--

INSERT INTO `chapter` (`id`, `chapterNumber`, `serial`, `name`, `countView`, `storyId`, `content`, `wordCount`, `createDate`, `userPosted`, `price`, `dealine`, `status`) VALUES
(1, '01', 1, 'Hoàng Sơn Chân Quân cùng Cửu Châu nhất hào quần', 5, 1, '<p><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Năm 2019 ngày 20 tháng 5, ng&agrave;y thứ Hai.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Xu&acirc;n tẫn hạ ch&iacute;.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">M&ugrave;a n&agrave;y, Giang Nam khu ng&agrave;y đ&ecirc;m nhiệt độ ch&ecirc;nh lệch v&ocirc; c&ugrave;ng lớn. Ban ng&agrave;y c&ograve;n mặc quần cộc n&oacute;ng th&agrave;nh ch&oacute;; ban đ&ecirc;m lại đ&ecirc;́n n&uacute;p ở trong chăn đ&ocirc;ng lạnh th&agrave;nh H&agrave;n h&agrave;o điểu.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Giang Nam Đại Học Th&agrave;nh.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hai giờ mười ba ph&uacute;t chiều, c&aacute;i giờ n&agrave;y ch&iacute;nh l&agrave; c&aacute;c học sinh giờ đi học. Tống Thư H&agrave;ng lại một m&igrave;nh ở tại k&yacute; t&uacute;c x&aacute;, b&agrave;n m&aacute;y t&iacute;nh bị k&eacute;o đến b&ecirc;n giường, thuận tiện hắn d&ugrave;ng c&aacute;c loại tư thế quan s&aacute;t phim.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tống Thư H&agrave;ng cũng kh&ocirc;ng c&oacute; trốn học y&ecirc;u th&iacute;ch &mdash;&mdash; buổi tối h&ocirc;m qua nửa đ&ecirc;m thời tiết oi bức, trong l&uacute;c ngủ mơ hắn sử xuất một chi&ecirc;u \'Song Long Xuất Hải\' đạp bay chăn. Nửa đ&ecirc;m về s&aacute;ng, nhiệt độ kh&ocirc;ng kh&iacute; kịch h&agrave;ng. To&agrave;n th&acirc;n tr&ecirc;n dưới chỉ c&oacute; một đầu quần cộc Tống Thư H&agrave;ng lập tức khổ bức, trong l&uacute;c ngủ mơ hai tay của hắn tr&ecirc;n giường đau khổ t&igrave;m t&ograve;i, t&igrave;m kiếm thăm d&ograve;, lại sờ kh&ocirc;ng tới chăn. Cuối c&ugrave;ng chỉ c&oacute; co lại th&agrave;nh B&igrave; B&igrave; t&ocirc;m h&igrave;nh, tại nửa đ&ecirc;m h&agrave;n phong dưới d&acirc;m uy run lẩy bẩy.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Mặt trời mới mọc d&acirc;ng l&ecirc;n l&uacute;c, Tống Thư H&agrave;ng đ&atilde; trở th&agrave;nh m&ugrave;a cảm mạo đại qu&acirc;n một vi&ecirc;n.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bạn c&ugrave;ng ph&ograve;ng đ&atilde; thay hắn xin nghỉ h&ocirc;m nay kh&oacute;a.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Sau đ&oacute;, hắn uống thuốc cảm, ngủ một giấc đến b&acirc;y giờ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Sốt cao r&uacute;t đi, th&acirc;n thể vẫn c&ograve;n c&oacute; ch&uacute;t suy yếu, dạng n&agrave;y trạng th&aacute;i căn bản l&agrave; kh&ocirc;ng c&oacute; c&aacute;ch đi học. Cho n&ecirc;n, hắn chỉ c&oacute; thể một th&acirc;n một m&igrave;nh ở tại k&yacute; t&uacute;c x&aacute; nh&agrave;m ch&aacute;n xem phim.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tr&ecirc;n m&agrave;n h&igrave;nh, phim ph&aacute;t ra thanh tiến độ chậm r&atilde;i tiến l&ecirc;n. Nhưng phim nội dung, Tống Thư H&agrave;ng lại một ch&uacute;t cũng kh&ocirc;ng thấy đi v&agrave;o.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Dược hiệu c&ograve;n kh&ocirc;ng c&oacute; đi qua &agrave;, buồn ngủ qu&aacute;.\" Hắn ng&aacute;p một c&aacute;i, cảm gi&aacute;c m&iacute; mắt c&oacute; ch&uacute;t nặng nề.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\'T&iacute;ch t&iacute;ch t&iacute;ch ~\' l&uacute;c n&agrave;y, m&aacute;y t&iacute;nh dưới g&oacute;c phải phần mềm chat nhảy l&ecirc;n.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đ&acirc;y l&agrave; c&oacute; người đem hắn th&ecirc;m l&agrave;m hảo hữu, hoặc l&agrave; gia nhập quần tổ nhắc nhở.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Ai th&ecirc;m ta?\" Tống Thư H&agrave;ng lẩm bẩm n&oacute;i, hắn đưa tay tại m&aacute;y t&iacute;nh cảm ứng dưới g&oacute;c phải nhẹ nh&agrave;ng điểm một c&aacute;i, nhắc nhở tin tức bắn ra.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">[ Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n (******) thỉnh cầu tăng th&ecirc;m ngươi l&agrave;m hảo hữu. ] k&egrave;m theo tin tức: Kh&ocirc;ng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n? Ai vậy, loại n&agrave;y kỳ qu&aacute;i biệt danh?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"L&agrave; trong lớp đồng học sao?\" Tống Thư H&agrave;ng thầm nghĩ, trong đầu kh&ocirc;ng khỏi nhớ tới trong lớp mấy c&aacute;i kia r&otilde; r&agrave;ng đ&atilde; l&ecirc;n đại học vẫn c&ograve;n ở v&agrave;o thanh xu&acirc;n huyễn tưởng kỳ gia hỏa. Nếu như l&agrave; bọn hắn m&agrave; n&oacute;i, ho&agrave;n to&agrave;n ch&iacute;nh x&aacute;c hội l&ecirc;n loại n&agrave;y kỳ qu&aacute;i biệt danh.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Nghĩ tới đ&acirc;y, hắn điểm \'Đồng &yacute;\' .</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ngay sau đ&oacute;, lại c&oacute; một đầu hệ thống tin tức bắn ra.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">[ Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n mời ngươi gia nhập quần \'Cửu Ch&acirc;u nhất h&agrave;o quần \', c&oacute; đồng &yacute; hay kh&ocirc;ng? ]</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tống Thư H&agrave;ng tiếp tục ấn đồng &yacute;.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\'Thư Sơn &Aacute;p Lực Đại\' đồng &yacute; gia nhập \'Cửu Ch&acirc;u nhất h&agrave;o quần\' .</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">[ ng&agrave;i đ&atilde; đồng &yacute; gia nhập quần tổ, c&ugrave;ng nh&oacute;m quần hữu ch&agrave;o hỏi đi! ] c&ograve;n tặng k&egrave;m c&oacute; c&aacute;i hệ thống khu&ocirc;n mặt tươi cười.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đầu năm nay Chat Messenger l&agrave;m c&agrave;ng ng&agrave;y c&agrave;ng nh&acirc;n t&iacute;nh h&oacute;a.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Li&ecirc;n tiếp nhắc nhở bắn ra ph&iacute;a sau Tống Thư H&agrave;ng đ&agrave;m định tắt đi nhắc nhở c&ugrave;ng quần khung ch&iacute;t ch&aacute;t &mdash;&mdash; hắn hiện tại buồn ngủ d&acirc;ng l&ecirc;n, n&agrave;o c&oacute; tinh lực quan t&acirc;m ch&iacute;nh m&igrave;nh tăng th&ecirc;m c&aacute;i g&igrave; quần?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">D&ugrave; sao, hắn quần thiết tr&iacute; một mực l&agrave; \'Kh&ocirc;ng gợi &yacute; tin tức chỉ biểu hiện số lượng \', quần b&ecirc;n trong n&oacute;i chuyện phiếm sẽ kh&ocirc;ng bắn ra quấy rầy đến hắn, sẽ chỉ ở quần tổ sau biểu hiện n&oacute;i chuyện phiếm số lượng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Chờ hắn thanh tỉnh ch&uacute;t ph&iacute;a sau c&oacute; thể đi lật qua n&oacute;i chuyện phiếm ghi ch&eacute;p, liền c&oacute; thể biết m&igrave;nh gia nhập l&agrave; c&aacute;i g&igrave; quần, c&ograve;n c&oacute; quần b&ecirc;n trong th&agrave;nh vi&ecirc;n n&oacute;i chuyện phiếm ghi ch&eacute;p cũng sẽ kh&ocirc;ng mất đi.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Con mắt c&agrave;ng ng&agrave;y c&agrave;ng nặng nề. . .</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Phim thanh tiến độ vẫn như cũ ngoan cường ti&ecirc;́n l&ecirc;n, Tống Thư H&agrave;ng &yacute; thức lại c&agrave;ng th&ecirc;m mơ hồ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">**** ******</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Cửu Ch&acirc;u nhất h&agrave;o quần b&ecirc;n trong, nh&igrave;n thấy c&oacute; t&acirc;n nh&acirc;n gia nhập ph&iacute;a sau quần b&ecirc;n trong c&oacute; lặn xuống nước th&agrave;nh vi&ecirc;n ngoi đầu l&ecirc;n.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bắc H&agrave; T&aacute;n nh&acirc;n: \"Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n tăng th&ecirc;m vị t&acirc;n đạo hữu đi v&agrave;o sao? Đ&atilde; c&oacute; hơn một năm kh&ocirc;ng c&oacute; th&ecirc;m người mới a?\"&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Lại c&oacute; id v&igrave; \'T&ocirc; thị A Thất\' cấp tốc hồi phục: \"C&oacute; t&acirc;n đạo hữu? Đạo hữu l&agrave; Hoa Hạ c&aacute;i n&agrave;o khu? Ở đ&acirc;u c&aacute;i động phủ tu h&agrave;nh? Đạo hiệu đ&acirc;u? Tu vi mấy phẩm?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">C&aacute;i n&agrave;y li&ecirc;n tiếp vấn đề, lu&ocirc;n cảm gi&aacute;c c&oacute; chỗ n&agrave;o kh&ocirc;ng th&iacute;ch hợp?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Gần như đồng thời, id v&igrave; Cuồng Đao Tam L&atilde;ng bắn ra tin tức: \"T&acirc;n đạo hữu giới t&iacute;nh? C&oacute; phải l&agrave; ti&ecirc;n tử kh&ocirc;ng? L&agrave; th&igrave; b&aacute;o ba v&ograve;ng, b&agrave;y ra c&aacute;i ảnh th&ocirc;i!\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Nh&igrave;n thấy T&ocirc; thị A Thất c&ugrave;ng Cuồng Đao Tam L&atilde;ng tin tức, quần b&ecirc;n trong c&oacute; mấy người kh&oacute;e miệng co giật.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Tam L&atilde;ng huynh, ngươi quả nhi&ecirc;n l&agrave; thuộc c&aacute; v&agrave;ng sao?\" Bắc H&agrave; T&aacute;n nh&acirc;n thở d&agrave;i: \"Ngươi cũng đừng lại t&aacute;c tử, vạn nhất Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n lại tăng th&ecirc;m vị Đại tiền bối tiến đến l&agrave;m sao b&acirc;y giờ?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tam L&atilde;ng c&aacute;i n&agrave;y gia hỏa c&aacute;i g&igrave; cũng tốt, c&oacute; t&igrave;nh c&oacute; nghĩa, lấy gi&uacute;p người l&agrave;m niềm vui, cho n&ecirc;n nh&acirc;n duy&ecirc;n kh&ocirc;ng tệ &mdash;&mdash; ch&iacute;nh l&agrave; b&igrave;nh thường ưa th&iacute;ch miệng ba hoa, t&aacute;c một tay hảo tử.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hết lần n&agrave;y tới lần kh&aacute;c c&aacute;i n&agrave;y gia hỏa may mắn gi&aacute; trị lại thấp để cho người ta giận s&ocirc;i, mỗi lần kh&ocirc;ng c&acirc;́m &yacute; ở giữa t&aacute;c tử l&uacute;c, đắc tội tổng l&agrave; Đại tiền bối. Những n&agrave;y nh&agrave;n rỗi nhức cả trứng Đại tiền bối đang lo kh&ocirc;ng c&oacute; việc vui, tự nhi&ecirc;n rất vui vẻ giày vò l&ecirc;n Cuồng Đao Tam L&atilde;ng c&aacute;i n&agrave;y đưa tới cửa việc vui.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Quỳ cầu kh&ocirc;ng cần x&aacute;ch \'Đại tiền bối\' mấy chữ, bản tọa trong l&ograve;ng c&oacute; b&oacute;ng tối.\" Cuồng Đao Tam L&atilde;ng ph&aacute;t một loạt \'Lệ rơi đầy mặt\' biểu lộ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bốn năm trước hắn trương n&agrave;y ph&aacute; miệng đắc tội một vị xinh đẹp \'Đại tiền bối \', bị giày vò thảm r&ocirc;̀i. . . C&aacute;i kia Đại tiền bối li&ecirc;n tiếp giày vò hắn r&ograve;ng r&atilde; một năm lẻ bốn th&aacute;ng. Ng&agrave;i kh&ocirc;ng nghe lầm, l&agrave; r&ograve;ng r&atilde; một năm lẻ bốn th&aacute;ng a! Nhớ tới c&aacute;i kia đoạn kh&ocirc;ng phải người tranh vanh tuế nguyệt, hốc mắt của hắn đều ẩm ướt.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tam L&atilde;ng mới lời n&agrave;y vừa mới n&oacute;i xong, quần b&ecirc;n trong liền li&ecirc;n tiếp bắn ra cười xấu xa biểu lộ &mdash;&mdash; kh&ocirc;ng che giấu ch&uacute;t n&agrave;o, ngay thẳng cười tr&ecirc;n nỗi đau của người kh&aacute;c.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Quần b&ecirc;n trong biểu hiện tại tuyến trạng th&aacute;i c&oacute; t&aacute;m người, trong đ&oacute; c&oacute; s&aacute;u người c&ugrave;ng nhau bắn ra quét một loạt khu&ocirc;n mặt tươi cười.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"C&aacute;c ngươi bọn n&agrave;y cười tr&ecirc;n nỗi đau của người kh&aacute;c gia hỏa, bản tọa nhớ kỹ c&aacute;c ngươi mỗi người, đừng cho bản tọa gặp gỡ c&aacute;c ngươi, nếu kh&ocirc;ng nhất định phải l&agrave;m cho c&aacute;c ngươi nếm thử bản tọa bảy mươi hai đường kho&aacute;i đao lợi hại!\" Cuồng Đao Tam L&atilde;ng o&aacute;n hận n&oacute;i. Hắn đối với m&igrave;nh kho&aacute;i đao rất c&oacute; tự tin, vừa rồi cười xấu xa s&aacute;u c&aacute;i gia hỏa, đơn đấu lời n&oacute;i kh&ocirc;ng c&oacute; một c&aacute;i n&agrave;o l&agrave; đối thủ của hắn.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Cuồng Đao Tam L&atilde;ng vừa mới n&oacute;i xong.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Quần b&ecirc;n trong lập tức lại đ&aacute;nh l&ecirc;n một c&aacute;i cười xấu xa biểu lộ, l&agrave; T&ocirc; thị A Thất.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tiếp lấy T&ocirc; thị A Thất rất hưng phấn n&oacute;i: \"Lúc nào đơn đấu?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hiển nhi&ecirc;n, T&ocirc; thị A Thất cũng kh&ocirc;ng c&oacute; cười tr&ecirc;n nỗi đau của người kh&aacute;c &yacute; tứ &mdash;&mdash; hắn ch&iacute;nh l&agrave; muốn t&igrave;m người đ&aacute;nh một trận.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\". . .\" Cuồng Đao Tam L&atilde;ng lập tức suy sụp.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bởi v&igrave; hắn đ&aacute;nh kh&ocirc;ng lại A Thất!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hắn tu vi tinh th&acirc;m, đ&atilde; đạt tới Ngũ phẩm Linh Ho&agrave;ng hậu kỳ cảnh giới, c&aacute;ch Lục phẩm Linh Qu&acirc;n cũng chỉ c&oacute; hai bước khoảng c&aacute;ch, nhưng l&agrave; hắn đ&aacute;nh kh&ocirc;ng lại A Thất.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hắn một tay bảy mươi hai đường đao ph&aacute;p vừa nhanh vừa độc, c&ograve;n c&oacute; nhanh như thiểm điện th&acirc;n ph&aacute;p, nhưng l&agrave; đ&aacute;nh kh&ocirc;ng lại A Thất.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hắn danh xưng Cuồng Đao, cuồng l&uacute;c ngay cả m&igrave;nh đều sợ, nhưng ch&iacute;nh l&agrave; đ&aacute;nh kh&ocirc;ng lại A Thất!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Quần b&ecirc;n trong người nh&igrave;n thấy Tam L&atilde;ng suy sụp ph&iacute;a sau lại l&agrave; một chuỗi kh&ocirc;ng ch&uacute;t ki&ecirc;ng kỵ khu&ocirc;n mặt tươi cười.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\". . .\" Lần n&agrave;y, Cuồng Đao Tam L&atilde;ng chỉ c&oacute; thể buồn bực ph&aacute;t một chuỗi dấu hai chấm.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Quần b&ecirc;n trong người nh&aacute;o đằng nửa ng&agrave;y, lại kh&ocirc;ng nh&igrave;n thấy t&acirc;n nh&acirc;n l&ecirc;n tiếng, hơi nghi hoặc một ch&uacute;t.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"T&acirc;n đạo hữu kh&ocirc;ng ra?\" Bắc H&agrave; T&aacute;n nh&acirc;n l&ecirc;n tiếng hỏi.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đ&aacute;ng tiếc, bởi v&igrave; thuốc cảm mạo dược hiệu, Tống Thư H&agrave;ng đ&atilde; lần nữa tiến v&agrave;o nửa ngủ trạng th&aacute;i.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">L&uacute;c n&agrave;y, T&ocirc; thị A Thất lại rất vui vẻ ph&aacute;t c&aacute;i tin: \"Ta xem dưới, t&acirc;n đạo hữu gọi \'Thư Sơn &Aacute;p Lực Đại\' . C&oacute; nghe qua gọi c&aacute;i n&agrave;y đạo hiệu cao thủ sao? C&aacute;i n&agrave;y đạo hiệu nghe c&oacute; ch&uacute;t giống như l&agrave; Nho m&ocirc;n h&agrave;nh giả? Thật l&agrave;m cho người chờ mong a! Những năm n&agrave;y, Nho m&ocirc;n h&agrave;nh giả ẩn cư v&ocirc; c&ugrave;ng s&acirc;u, t&igrave;m cũng kh&ocirc;ng t&igrave;m tới. Ta đ&atilde; c&oacute; gần trăm năm kh&ocirc;ng c&oacute; đ&aacute;nh qua bọn hắn! Hồi tưởng lại, Nho m&ocirc;n h&agrave;nh giả so Phật m&ocirc;n c&ograve;n muốn đ&aacute;nh thoải m&aacute;i, kh&ocirc;ng chỉ c&oacute; mồm m&eacute;p lợi hại, nắm đấm cũng đủ cứng. M&agrave; lại đ&aacute;nh tới h&agrave;o hứng l&uacute;c sẽ c&ograve;n ph&oacute;ng kho&aacute;ng ng&acirc;m Thi trợ hứng, lần thoải m&aacute;i! Th&iacute;ch nhất đ&aacute;nh bọn hắn.\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"A Thất, ta n&oacute;i, ngươi đối t&acirc;n đạo hữu chờ mong vĩnh viễn chỉ c&oacute; c&oacute; được hay kh&ocirc;ng đ&aacute;nh, c&ugrave;ng đ&aacute;nh sướng hay kh&ocirc;ng? Sao?\" Cuồng Đao Tam L&atilde;ng ph&aacute;t c&aacute;i lệ rơi đầy mặt biểu lộ n&oacute;i. Đ&acirc;y quả thực l&agrave; &aacute;c b&aacute; h&agrave;nh vi c&oacute; được hay kh&ocirc;ng? !</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"&Acirc;y.\" T&ocirc; thị A Thất c&oacute; ch&uacute;t xấu hổ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bắc H&agrave; T&aacute;n nh&acirc;n cười xấu xa n&oacute;i: \"C&oacute; thể hay kh&ocirc;ng lại l&agrave; c&aacute;i sẽ kh&ocirc;ng d&ugrave;ng Chat Messenger \'Đại tiền bối\' ?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hắn vừa n&oacute;i như thế, tất cả mọi người cảm gi&aacute;c tr&agrave;ng diện n&agrave;y rất c&oacute; tức thị cảm đ&acirc;u?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đ&uacute;ng a, kh&ocirc;ng sai biệt lắm bốn năm trước tựa hồ cũng c&oacute; một vị bế quan mấy trăm năm sau xuất quan tiền bối , đồng dạng thật vất vả l&ecirc;n phần mềm chat, bị Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n gia nhập quần. Nhưng bởi v&igrave; sẽ kh&ocirc;ng đ&aacute;nh chữ, kh&ocirc;ng c&oacute; ph&aacute;t biểu.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Sau đ&oacute;, một vị gọi Cuồng Đao Tam L&atilde;ng gia hỏa rất vui vẻ tại vị n&agrave;y tiền bối trước mặt miệng ba hoa, lại phải vị tiền bối n&agrave;y b&aacute;o ba v&ograve;ng, lại phải n&agrave;ng ph&aacute;t ảnh chụp, lại phải giọng n&oacute;i n&oacute;i chuyện phiếm c&aacute;i g&igrave;.&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Sau đ&oacute;. . . Kh&ocirc;ng c&oacute; qua mấy ng&agrave;y, Cuồng Đao Tam L&atilde;ng liền tận mắt thấy vị tiền bối n&agrave;y. Đ&oacute; l&agrave; vị rất đẹp tiền bối, như l&agrave; trong bầu trời đ&ecirc;m minh nguyệt đồng dạng lo&aacute; mắt mỹ lệ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Lại n&oacute;i tiếp, vị n&agrave;y mỹ lệ tiền bối giày vò Cuồng Đao Tam L&atilde;ng r&ograve;ng r&atilde; một năm lẻ bốn th&aacute;ng, mới h&agrave;i l&ograve;ng rời đi.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Cuồng Đao Tam L&atilde;ng lập tức quỳ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Ho&agrave;ng Sơn?\" L&uacute;c n&agrave;y, một c&aacute;i gọi \'Dược Sư\' id ph&aacute;t biểu.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Kh&ocirc;ng hiểu thấu ngắn gọn tin tức, kh&ocirc;ng đầu kh&ocirc;ng đu&ocirc;i.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Cũng may quần b&ecirc;n trong người sớm quen thuộc Dược Sư ngắn gọn n&oacute;i chuyện phiếm th&oacute;i quen &mdash;&mdash; hắn l&agrave; đang hỏi quần chủ Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n người ở đ&acirc;u?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ph&aacute;t biểu ngắn gọn cũng kh&ocirc;ng phải l&agrave; Dược Sư t&iacute;nh c&aacute;ch cao qu&yacute; l&atilde;nh ngạo, m&agrave; bởi v&igrave; hắn đ&aacute;nh chữ d&ugrave;ng ch&iacute;nh l&agrave; hai chỉ thiền cộng viết tay, tốc độ tặc chậm. Số lượng từ nhiều thời điểm c&ograve;n dễ d&agrave;ng sai, xóa xóa viết viết v&ocirc; c&ugrave;ng thống khổ. Cho n&ecirc;n Dược Sư th&oacute;i quen ph&aacute;t biểu có th&ecirc;̉ ngắn th&igrave; ngắn. Dần d&agrave;, liền biến th&agrave;nh b&acirc;y giờ loại n&agrave;y t&iacute;ch chữ như v&agrave;ng giao lưu phương thức.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Hắn tăng th&ecirc;m người sau liền lập tức logout, nghe n&oacute;i hắn nh&agrave; c&aacute;i kia bảo bối Đại y&ecirc;u khuyển lại bị tức giận rời nh&agrave; đi ra ngo&agrave;i, Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n lại đuổi theo. Ứng ph&oacute; c&aacute;i kia bảo bối Đại y&ecirc;u khuyển cũng kh&ocirc;ng dễ d&agrave;ng, hiện tại Ch&acirc;n Qu&acirc;n khẳng định rất bận rộn, c&oacute; thể l&ecirc;n tuyến cộng người đều l&agrave; kh&oacute; được d&agrave;nh thời gian.\" Bắc H&agrave; T&aacute;n nh&acirc;n trả lời.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\". . .\" Dược Sư.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Vậy chỉ c&oacute; thể chờ t&acirc;n đạo hữu học được d&ugrave;ng Chat Messenger sau lại h&agrave;n huy&ecirc;n.\" T&ocirc; thị A Thất cảm th&aacute;n n&oacute;i. Bọn hắn đều v&agrave;o trước l&agrave; chủ, cho rằng mới gia nhập cũng l&agrave; người trong đồng đạo.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Gặp t&acirc;n đạo hữu kh&ocirc;ng c&oacute; phản ứng, tại tuyến mấy vị gặp kh&ocirc;ng c&oacute; việc vui, cũng đều nhao nhao lặn xuống nước.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">**** ******</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ước chừng sau một tiếng, Tống Thư H&agrave;ng tho&aacute;ng tỉnh t&aacute;o lại.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Nhớ kỹ vừa rồi c&oacute; người cộng ta quần đi, giống như gọi Cửu Ch&acirc;u nhất h&agrave;o quần đến?\" Hắn thấp giọng th&igrave; th&agrave;o, tiện tay ấn mở dưới g&oacute;c phải Chat Messenger, l&ocirc;i ra Cửu Ch&acirc;u nhất h&agrave;o quần khung ch&iacute;t ch&aacute;t.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đến c&ugrave;ng l&agrave; c&aacute;i g&igrave; quần?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Rất nhanh, một giờ trước n&oacute;i chuyện phiếm ghi ch&eacute;p xuất hiện ở trước mặt hắn.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tống Thư H&agrave;ng đại kh&aacute;i du l&atilde;m một lần.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đạo hữu? Động phủ? Tu vi mấy phẩm?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">C&ograve;n c&oacute; tiền bối? Ch&acirc;n Qu&acirc;n? Bản tọa? Đuổi bắt Đại y&ecirc;u khuyển?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">C&aacute;c loại ti&ecirc;n hiệp trong tiểu thuyết chuy&ecirc;n dụng từ ngữ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Quần b&ecirc;n trong nh&acirc;n vi&ecirc;n n&oacute;i chuyện phiếm phương thức n&oacute;i chuyện cũng rất th&uacute; vị &mdash;&mdash; nửa cổ kh&ocirc;ng cổ, nửa Bạch kh&ocirc;ng Bạch. Cho người cảm gi&aacute;c ch&iacute;nh l&agrave; người hiện đại &yacute; đồ d&ugrave;ng cổ ngữ giao lưu, hết lần n&agrave;y tới lần kh&aacute;c lại bởi v&igrave; cổ văn bản lĩnh thất bại, dẫn đến giao lưu phương thức rất kh&oacute; chịu.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Xoẹt ~~\" Tống Thư H&agrave;ng cười ra tiếng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Xem ra đ&oacute; l&agrave; c&aacute;i ti&ecirc;n hiệp kẻ y&ecirc;u th&iacute;ch x&acirc;y quần?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">A kh&ocirc;ng, đ&acirc;y tuyệt đối kh&ocirc;ng phải phổ th&ocirc;ng ti&ecirc;n hiệp kẻ y&ecirc;u th&iacute;ch quần!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Quần b&ecirc;n trong mỗi người đều cho m&igrave;nh l&ecirc;n c&aacute;i đạo hiệu, chỗ ở muốn xưng động phủ, quần chủ lạc đường ch&oacute; cảnh đều muốn h&igrave;nh dung th&agrave;nh gia b&ecirc;n trong Đại y&ecirc;u khuyển rời nh&agrave; trốn đi. C&ograve;n c&oacute; người tự xưng tr&ecirc;n trăm năm kh&ocirc;ng c&oacute; đ&aacute;nh qua Nho m&ocirc;n h&agrave;nh giả c&aacute;i g&igrave;, n&oacute;i c&aacute;ch kh&aacute;c người kia tự xưng đ&atilde; sống hơn mấy trăm tuổi?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Chỉ l&agrave; nh&igrave;n lấy những n&agrave;y n&oacute;i chuyện phiếm ghi ch&eacute;p th&igrave; c&oacute; loại thật xấu hổ cảm gi&aacute;c.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Loại n&agrave;y si m&ecirc; tr&igrave;nh độ, đ&atilde; đạt đến chuunibyou tr&igrave;nh độ đi, m&agrave; lại l&agrave; rất c&oacute; Hoa Hạ đặc sắc ti&ecirc;n hiệp tự kỷ.\" Tống Thư H&agrave;ng &acirc;m thầm gật đầu.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Xem ra, đ&oacute; l&agrave; c&aacute;i ti&ecirc;n hiệp chuunibyou người bệnh trại tập trung!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đ&acirc;y cũng l&agrave; hắn đối \'Cửu Ch&acirc;u nhất h&agrave;o quần\' c&ugrave;ng quần b&ecirc;n trong th&agrave;nh vi&ecirc;n ấn tượng đầu ti&ecirc;n.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bất qu&aacute; v&igrave; sao lại cộng hắn nhập quần?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Hắn nh&igrave;n xuống quần chủ Ho&agrave;ng Sơn Ch&acirc;n Qu&acirc;n tư liệu, cũng kh&ocirc;ng phải l&agrave; bạn học của m&igrave;nh, bản th&acirc;n cũng khẳng định kh&ocirc;ng biết hắn.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">L&agrave; lầm th&ecirc;m sao?</span></p>', 0, '2018-09-10 22:48:35', 1, 100, '2019-02-17 20:59:09', 2);
INSERT INTO `chapter` (`id`, `chapterNumber`, `serial`, `name`, `countView`, `storyId`, `content`, `wordCount`, `createDate`, `userPosted`, `price`, `dealine`, `status`) VALUES
(2, '1', 1, 'Hắn gọi Bạch Tiểu Thuần', 27, 2, 'Nhưng mà sự thật chính là như thế.<br/><br/>Trần Tường nói.<br><br>\"Chắc hẳn ngươi hẳn là, cũng đã đi phái người điều tra cái này tên là 《 Tây Du chiến kỷ 》 trò chơi a? Nhưng là bất kể là đưa vào hoạt động trò chơi này \"Thần thoại\" công ty, cùng cái công ty này lão bản , mặc kệ tin tức cũng không tra ra.\"<br><br>Trần Tường tiếp tục nói. <br><br>\"Không sai, đúng là như thế.\"<br><br>Lão nhân nghĩ đến mệnh lệnh mình bí thư đi thăm dò tin tức, gật đầu nói.<br><br>\"Nói thực ra, ta cũng không biết, ta thậm chí hoài nghi cái này 《 Tây Du chiến kỷ 》 là thần linh hoặc là ác ma vận doanh một trò chơi, bất kể thế nào nói, trò chơi này xuất hiện, đều cho nhân loại trong tương lai cái nguy cơ đó nặng nề thế giới, một tia sống tiếp cơ hội \"<br><br>Trần Tường nói.<br><br>\"Người tuổi trẻ kia, theo ý kiến của ngươi, Trung Hoa nên như thế nào né qua kiếp nạn này?\"<br><br>Sau đó lão nhân như có điều suy nghĩ.<br><br>\"Chính phủ tuyên truyền, toàn dân tiến vào trò chơi, nhất là quân đội.\"<br><br>Trần Tường bình tĩnh nói.<br><br>\"Người trẻ tuổi, của ngươi đó thân thực lực, là từ trong trò chơi lấy được sao?\"<br><br>Sau đó lão nhân hỏi.<br><br>\"Đúng thế.\"<br><br>Cái này không có gì đáng giá giấu giếm, Trần Tường gật đầu nói.<br><br>\"Tốt, ta hiểu được, một vấn đề cuối cùng, người trẻ tuổi, ngươi lại là làm sao biết, tương lai địa cầu trận kia kiếp nạn đâu?\"<br><br>Lão nhân cầm trong lòng mình, nghi ngờ nhất một vấn đề hỏi lên.<br><br>\"Ngươi có thể hiểu thành, ta cơ duyên xảo hợp đăng nhập vào trò chơi, sau đó thu được một cái biết trước tương lai năng lực, trước giờ biết trước được tương lai trận kia đại kiếp tồn tại.\"<br><br>Trần Tường bình tĩnh nói.<br><br>\"Người trẻ tuổi, ngươi nguyện ý làm Trung Hoa tướng quân sao?\"<br><br>Lão nhân bất thình lình hỏi.<br><br>\"Không có hứng thú.\"<br><br>Nghe được ý của ông lão, Trần Tường không hề nghĩ ngợi, chỉ lắc đầu cự tuyệt.<br><br>Tại thế giới trò chơi bên trong, hắn đã là một phương đại đế, đối với quyền thế các loại đồ vật , có thể nói hắn hoàn toàn không chút nào tham luyến.<br><br>Với lại thế giới trò chơi mới là trọng yếu nhất, thế giới trò chơi thực lực đề cao, thế giới hiện thật thực lực mới có thể tương ứng nước lên thì thuyền lên.<br><br>Hắn cũng không muốn trở thành tướng quân, tiếp những cái kia lão đại chơi cái gì quyền lợi chính trị trò chơi.<br> <br>Đó hoàn toàn là đang lãng phí thời gian.<br><br>\"Chuyện này là ngươi đến, không có ngươi chỉ sợ không được, nếu như ngươi sợ phiền toái, ta có thể cho ngươi một cái nhàn chức.\"<br><br>Sau đó lão nhân nói.<br><br>Hắn biết rõ thế hệ này lớn lên người trẻ tuổi, cũng không phải là mỗi một cái, đều cam nguyện vì quốc gia vô tư dâng hiến.<br><br>Trần Tường vốn đang là muốn cự tuyệt, nhưng nhìn đến già người trên trán nếp nhăn, cùng cái kia tha thiết ánh mắt, không biết vì sao, hắn trong lúc nhất thời mềm lòng.<br><br>\"Tốt, ta có thể tiếp thụ, nhưng trước giờ nói xong, chỉ là trên danh nghĩa, ta không quản sự.\"<br><br>Trần Tường cuối cùng nói.<br><br>\"Liền xem như trên danh nghĩa cũng tốt, Trần Tường, ngươi đón lấy chính là Trung Hoa Trung tướng, chức vụ là 749 cục danh dự Cục Trưởng.\"<br><br>Xem Trần Tường không có tiếp tục cự tuyệt, lão nhân cảm thấy hết sức vui mừng.<br><br>\"749 cục?\"<br><br>Trần Tường nghi ngờ nói.<br><br>\"Đây là quốc gia chúng ta, ở trên thế kỷ thành lập một cái, người nghiên cứu thể khoa học, hiện tượng linh dị, đặc dị công năng bí mật quân đội, bởi vì cục thế cùng thời đại nguyên nhân, luôn luôn không chịu đến quốc gia coi trọng, nhưng là bây giờ những quái vật kia xuất hiện, xem ra là thời điểm bắt đầu dùng 749 cục.\"<br><br>Sau đó lão nhân giới thiệu nói.<br><br>\"Quân phục cùng Quân Quan Chứng, ngày mai liền sẽ có người đưa tới nếu như 749 cục công tác cần tiểu bằng hữu chỉ đạo, kính xin tiểu bằng hữu ngươi phải nhiều hơn hao tâm tổn trí.\"<br><br>Sau đó tới người đứng lên nói.<br><br>\"Được rồi.\"<br><br>Trần Tường đáp ứng.<br><br>\"Đây là thư ký của ta Quách Phong, nếu như có chuyện ta về thông qua hắn liên hệ ngươi.\"<br><br>Sau đó trước lúc rời đi, lão nhân chắp tay nói.\"Không có vấn đề.\"<br><br>Trần Tường gật đầu.<br><br>Cái này Quách Phong tại lưu lại Trần Tường phương thức liên lạc về sau, liền cùng lão nhân cùng rời đi.<br><br>\"Ca ca, ngươi thành tướng quân a!\"<br><br>Chờ lão nhân sau khi rời đi, Trần Hải Nhi tỉnh táo lại, gương mặt kinh hỉ nói.<br><br>Đây là nàng ít có xưng hô Trần Tường vì ca ca.<br><br>\"Một tên tướng quân có gì đáng giá cao hứng.\" Ai biết Trần Tường, trên mặt một điểm cao thần sắc đều không có, giống như chuyện gì đều không phát sinh một dạng.<br><br>\"Đây chính là tướng quân à! Mà lại là trung tướng, ngươi chỉ sợ là Trung Hoa trong lịch sử, tuổi trẻ nhỏ nhất Trung tướng đi.\"<br> <br>Trần Hải Nhi gương mặt cao hứng nói.<br><br>\"Vậy thì thế nào?\" Trần Tường hỏi lại Trần Hải Nhi.<br><br>Trần Tường hỏi lại , lệnh Trần Hải Nhi thần sắc đọng lại.<br><br>. . . .<br><br>Đây chính là Trung Hoa trung tướng à!<br><br>Trung tướng thân phận, tại toàn bộ Trung Hoa chỉ sợ đều xem như một vị đại lão a?<br><br>Trần Tường này làm sao nói cũng coi là quang tông diệu tổ a?<br><br>Kết quả Trần Tường à nha?<br><br>Thần sắc này thậm chí còn không bằng, chính mình trong đại học những bạn học kia, được tuyển cán bộ lớp cao hứng.<br><br>Trần Hải Nhi lúc này mới cảm thấy, chính mình không có chút nào hiểu rõ chính mình cái này ca ca.<br><br>\"Nhớ kỹ ta nói với ngươi, có thời gian chơi nhiều chơi 《 Tây Du chiến kỷ 》 trò chơi này, khả năng này sẽ trở thành ngươi tương lai sống sót tiền vốn.\"<br><br>Trần Tường dặn dò một tiếng Trần Hải Nhi về sau, sau đó liền về tới gian phòng của mình.<br><br>Hôm nay là 《 Tây Du chiến kỷ 》 đổi mới ngày ba ngày sau, 《 Tây Du chiến kỷ 》 đổi mới về sau, sẽ tại buổi tối hôm nay rạng sáng 12 giờ tiếp tục mở phục tùng.<br><br>. . . .<br><br>Xem ca ca của mình cùng lão nhân nói chuyện nội dung, không giống như là đùa giỡn.<br><br>Trần Hải Nhi đã quyết tâm , đợi lát nữa liền đi chơi dưới cái này tên là 《 Tây Du chiến kỷ 》 trò chơi.<br><br>Trần Tường nhưng lại không biết.<br><br>Lão nhân tại rời đi trụ sở của hắn về sau, liền trực tiếp ngồi máy bay riêng bay trở về Yến Kinh.<br><br>Sau đó có quan hệ như thế nào quảng bá 《 Tây Du chiến kỷ 》 trò chơi này mệnh lệnh, bị quốc gia trao quyền cho cấp dưới đến Trung Hoa cả nước các thành phố lớn, thậm chí huyện trấn chính phủ.<br><br>Sau đó Trung Hoa các thành phố lớn cao ốc, tàu điện ngầm, cửa hàng nhấp nhô bài bên trên, khắp nơi tất cả đều là có quan hệ 《 Tây Du chiến kỷ 》 trò chơi quảng bá.<br><br>Tại dạng này quảng bá cường độ dưới, liền xem như chất lượng kém đi nữa trò chơi, đều sẽ có như vậy một nắm người chơi cùng sinh ra mãnh liệt lòng hiếu kỳ, muốn đi vào thử một lần.<br><br>Huống chi 《 Tây Du chiến kỷ 》 trò chơi này chất lượng, là quốc tế đứng đầu.<br><br>Cho nên đối với 《 Tây Du chiến kỷ 》 trò chơi này, sinh ra hứng thú người chơi, càng nhiều.<br><br>Bọn hắn đều ở đây mình trí năng đồng hồ bên trên, lắp xong 《 Tây Du chiến kỷ 》 khách hàng, chỉ còn chờ rạng sáng 12 giờ, liền tiến vào trong trò chơi.<br><br>Mặt khác, hôm nay nước Hoa quân đội. Cũng tất cả đều nhận được một cái đặc biệt nhiệm vụ.<br><br>Cái kia chính là toàn quân download, một cái tên là 《 Tây Du chiến kỷ 》 trò chơi khách hàng, toàn quân tổ đội chơi đùa. Quân lệnh này vừa đưa ra, không biết kinh sợ đã hỏng bao nhiêu quân nhân cùng quân đội Sĩ Quan. <p>&nbsp;</p><p>Main hay, lão quái vật thích giấu nghề, pet vô sỉ. ', 0, '2018-09-10 22:52:09', 1, 10, '2018-11-23 01:03:30', 1),
(3, '2', 2, 'Ta muốn, khiêu chiến ngươi!', 45, 2, 'c2', 0, '2018-09-11 09:44:09', 1, 0, NULL, 2),
(4, '3', 3, 'Sáu câu chân ngôn', 33, 2, 'c3', 0, '2018-11-07 22:30:00', 1, 100, '2018-11-29 00:00:00', 2),
(6, '01', 1, 'Giúp Đại sư tỷ gây sự tình', 6, 6, '<p><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Bạch V&acirc;n sơn, Tề V&acirc;n phong, một gian trong nh&agrave; gỗ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n dung hợp xong k&yacute; ức, một mặt đờ đẫn k&ecirc;u r&ecirc;n một tiếng: \"Tư chất thấp nhất? C&aacute;i n&agrave;y c&ograve;n thế n&agrave;o tu ch&acirc;n, tu ch&acirc;n em g&aacute;i nh&agrave; ngươi a?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tỉnh lại sau giấc ngủ, Dương Ch&acirc;n đi tới một c&aacute;i t&ecirc;n l&agrave; U Ch&acirc;u đại lục tu ch&acirc;n thế giới, lại l&agrave; một c&aacute;i Thượng Nguy&ecirc;n t&ocirc;ng tư chất thấp nhất củi mục, tựa như l&agrave; c&oacute; người n&oacute;i cho hắn biết tr&uacute;ng 100 vạn thưởng lớn, c&ograve;n kh&ocirc;ng c&oacute; cao hứng đ&acirc;u, người n&agrave;y lại n&oacute;i cho hắn biết kh&ocirc;ng c&oacute; &yacute; tứ, ta nhận lầm người.&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">C&aacute;i n&agrave;y một chậu con nước lạnh dội xuống đến, r&oacute;t Dương Ch&acirc;n một c&aacute;i lạnh xuy&ecirc;n tim.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ngay tại Dương Ch&acirc;n t&acirc;m chết như bụi thời điểm, tr&ecirc;n người hắn chợt bộc ph&aacute;t ra một đo&agrave;n thất thải h&agrave;o quang, như c&oacute; v&ocirc; tận thi&ecirc;n đạo hồng xướng vang vọng ở b&ecirc;n tai, v&ocirc; số kh&iacute; lưu hướng về Dương Ch&acirc;n tr&ecirc;n th&acirc;n qu&aacute;n ch&uacute; m&agrave; tới.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Sau một khắc, Dương Ch&acirc;n cảm thấy trước mặt hắn to&agrave;n bộ thế giới cũng kh&ocirc;ng giống nhau, th&acirc;n thể nhẹ nh&agrave;ng phảng phất giống như dung nhập thi&ecirc;n địa, tai mắt trong trẻo tựa như nh&igrave;n thấu thế gian bản nguy&ecirc;n, liền liền một h&iacute;t một thở đều như l&agrave; kh&ocirc;ng b&agrave;n m&agrave; hợp thi&ecirc;n địa đại đạo.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Một c&aacute;i giao diện thuộc t&iacute;nh tại Dương Ch&acirc;n trước mắt l&oacute;e l&ecirc;n một c&aacute;i rồi biến mất.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Căn cốt: Sssssssssr</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ngộ t&iacute;nh: Sssssssssr</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Sức chịu đựng: Sssssssssr</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Linh gi&aacute;c: Sssssssssr</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Một mực đến bảng biến mất, Dương Ch&acirc;n c&ograve;n chưa kịp phản ứng, tự lẩm bẩm: \"Lần n&agrave;y k&eacute;o banh trời!\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Căn cốt hiển lộ r&otilde; r&agrave;ng thi&ecirc;n ph&uacute;, ngộ t&iacute;nh quyết định tham đạo, sức chịu đựng ảnh hưởng tu h&agrave;nh, linh gi&aacute;c đại biểu tiềm lực.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">U Ch&acirc;u đại lục b&ecirc;n tr&ecirc;n tu sĩ bốn hạng tư chất, Dương Ch&acirc;n tất cả đều ph&aacute; trần, đ&acirc;y l&agrave; cho tới b&acirc;y giờ chưa từng xuất hiện t&igrave;nh huống, vạn năm qua. . . Chỉ c&oacute; Dương Ch&acirc;n một người!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Cửa ph&ograve;ng bỗng nhi&ecirc;n bị đẩy ra, một người mặc m&agrave;u trắng &aacute;o d&agrave;i nữ tử trẻ tuổi vội v&atilde; đi đến.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Nữ tử trẻ tuổi kh&ocirc;ng thi phấn trang điểm, nguyệt mi mắt hạnh mang theo kh&ocirc;ng d&iacute;nh kh&oacute;i lửa trần gian lịch sự tao nh&atilde;, r&iacute;u r&iacute;t miệng thơm thơm như đỏ y&ecirc;n hoa một dạng để d&ograve;ng người liền, g&oacute;t sen uyển chuyển mang theo lo lắng thần sắc, đi v&agrave;o Dương Ch&acirc;n trước mặt, c&oacute; ch&uacute;t đau l&ograve;ng n&oacute;i ra: \"Sư đệ, lại thấy &aacute;c mộng?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n nh&igrave;n xem trước mặt một th&acirc;n cổ trang t&oacute;c buộc th&agrave;nh kinh hộc b&uacute;i t&oacute;c nữ tử trẻ tuổi, biết n&agrave;ng l&agrave; Thượng Nguy&ecirc;n t&ocirc;ng Đại sư tỷ Liễu Nhược Ngưng, Ngưng Nguy&ecirc;n Kỳ tam trọng tu vi, m&agrave; ch&iacute;nh m&igrave;nh, ch&iacute;nh l&agrave; Thượng Nguy&ecirc;n t&ocirc;ng Trường Nguyệt Ch&acirc;n Nh&acirc;n một c&aacute;i nhỏ nhất đệ tử, vừa mới đột ph&aacute; Ngưng Nguy&ecirc;n Kỳ nhất trọng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Sư đệ, ngươi kh&ocirc;ng sao chứ?\" Liễu Nhược Ngưng gặp Dương Ch&acirc;n ngẩn người, tr&ecirc;n mặt lo lắng c&agrave;ng đậm.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Ngươi kh&ocirc;ng cần liều mạng như vậy, cho d&ugrave; ngươi kh&ocirc;ng biết ng&agrave;y đ&ecirc;m tu luyện, cũng kh&ocirc;ng phải c&aacute;i kia Đoạn L&atilde;ng T&agrave;i đối thủ, ta. . . Ta cuối c&ugrave;ng vẫn l&agrave; phải gả tới Thị Kiếm m&ocirc;n.\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n dung hợp k&yacute; ức sau đ&oacute;, biết chuyện n&agrave;y, Thị Kiếm m&ocirc;n m&ocirc;n chủ mang theo Đoạn L&atilde;ng T&agrave;i hướng Thượng Nguy&ecirc;n t&ocirc;ng cầu h&ocirc;n, ưng thuận Thượng Nguy&ecirc;n t&ocirc;ng kh&oacute; m&agrave; cự tuyệt chỗ tốt, m&agrave; hắn sở dĩ c&oacute; thể xuy&ecirc;n qua, ch&iacute;nh l&agrave; Dương Ch&acirc;n c&aacute;i n&agrave;y đồ đần v&igrave; trợ gi&uacute;p Đại sư tỷ, tu luyện qu&aacute; mức liều lĩnh dẫn đến c&ocirc;ng ph&aacute;p phản phệ m&agrave; chết.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Sư tỷ, ngươi thật cam t&acirc;m gả đưa cho người kia cặn b&atilde;?\"&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Li&ecirc;n quan tới Đoạn L&atilde;ng T&agrave;i truyền ng&ocirc;n, to&agrave;n bộ U Ch&acirc;u người đều biết, c&aacute;i n&agrave;y con b&ecirc; kh&ocirc;ng chỉ c&oacute; trời sinh t&iacute;nh &acirc;m hiểm, c&ograve;n lu&ocirc;n y&ecirc;u th&iacute;ch ch&agrave; đạp nữ nh&acirc;n, kh&ocirc;ng c&oacute; bất kỳ c&aacute;i g&igrave; nữ nh&acirc;n nguyện &yacute; tr&ecirc;u chọc hỗn đản n&agrave;y.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Liễu Nhược Ngưng cười khổ một tiếng, đ&aacute;y mắt hiện l&ecirc;n một tia tuyệt vọng, lắc đầu n&oacute;i ra:</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Kh&ocirc;ng cam t&acirc;m th&igrave; phải l&agrave;m thế n&agrave;o đ&acirc;y? N&oacute;i cho ngươi cũng n&oacute;i kh&ocirc;ng r&otilde; r&agrave;ng, được rồi, đ&atilde; ngươi kh&ocirc;ng c&oacute; việc g&igrave;, liền cố gắng tu luyện cho tốt đi, sư tỷ sự t&igrave;nh, l&agrave; t&ocirc;ng m&ocirc;n quyết định, trừ phi ta có th&ecirc;̉ tại mười ng&agrave;y sau tự tay đ&aacute;nh bại Đoạn L&atilde;ng T&agrave;i, nếu kh&ocirc;ng chuyện n&agrave;y tuyệt kh&ocirc;ng quay lại chỗ trống, có th&ecirc;̉. . . Hắn hiện tại đ&atilde; l&agrave; Ngưng Nguy&ecirc;n Kỳ cửu trọng cảnh giới, lại tu luyện địa cấp v&otilde; kỹ, ta chỗ n&agrave;o c&oacute; thể l&agrave; đối thủ của hắn.\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ngưng Nguy&ecirc;n Kỳ cửu trọng cảnh giới, địa cấp v&otilde; kỹ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Vẻn vẹn hai điểm n&agrave;y, Liễu Nhược Ngưng v&ocirc; luận như thế n&agrave;o cố gắng, cũng kh&ocirc;ng cải biến được vận mệnh của m&igrave;nh.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Liễu Nhược Ngưng quay người rời đi, đi tới cửa thời điểm, thanh &acirc;m thăm thẳm truyền đến: \"M&ecirc;nh mang th&agrave;nh bụi mấy vạn thu, ho&agrave;ng đạo ti&ecirc;n lộ cả đời sầu, sư đệ ngươi nhớ kỹ, chỉ c&oacute; ch&iacute;nh m&igrave;nh cường đại l&ecirc;n, mới c&oacute; thể tại từ từ đường tu ti&ecirc;n b&ecirc;n trong chưởng khống vận mệnh của m&igrave;nh.\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Địa cấp v&otilde; kỹ sao?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Liễu Nhược Ngưng đi sau đ&oacute;, Dương Ch&acirc;n ngồi tại tr&ecirc;n giường tr&uacute;c nh&iacute;u m&agrave;y khổ tư, bỗng nhi&ecirc;n nghĩ đến Liễu Nhược Ngưng tu luyện ho&agrave;ng cấp cao giai v&otilde; kỹ, Phong L&ocirc;i Kiếm!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ho&agrave;ng cấp c&ugrave;ng địa cấp k&eacute;m r&ograve;ng r&atilde; hai cấp bậc, mặc d&ugrave; đều l&agrave; ph&agrave;m phẩm c&ocirc;ng ph&aacute;p v&otilde; kỹ, có th&ecirc;̉ ch&ecirc;nh lệch cũng tuyệt kh&ocirc;ng phải một c&aacute;i cao giai liền c&oacute; thể cải biến được.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Thi&ecirc;n Địa Huyền Ho&agrave;ng bốn c&aacute;i c&acirc;́p b&acirc;̣c c&ocirc;ng ph&aacute;p v&otilde; kỹ, đều thuộc về ph&agrave;m phẩm, có th&ecirc;̉ cho d&ugrave; l&agrave; ph&agrave;m phẩm, to&agrave;n bộ Thượng Nguy&ecirc;n t&ocirc;ng đều t&igrave;m kh&ocirc;ng ra một loại địa cấp v&otilde; kỹ đến, chớ đừng n&oacute;i chi l&agrave; so địa cấp c&ograve;n muốn cao hơn một cấp bậc cấp thi&ecirc;n cấp.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n cũng tu luyện qua Phong L&ocirc;i Kiếm, chỉ bất qu&aacute; hắn tư chất ngu dốt, đến b&acirc;y giờ đều kh&ocirc;ng c&oacute; nắm giữ trong đ&oacute; ch&acirc;n tủy, thế nhưng l&agrave; c&aacute;i n&agrave;y cũng kh&ocirc;ng ảnh hưởng hắn biết Đạo Phong l&ocirc;i kiếm phương ph&aacute;p tu luyện.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Vừa mới nghĩ đến Phong L&ocirc;i Kiếm thời điểm, Dương Ch&acirc;n trong đầu liền oanh một trận v&ugrave; v&ugrave;, xuất hiện một bộ c&agrave;ng th&ecirc;m huyền ảo th&acirc;m th&uacute;y c&ocirc;ng ph&aacute;p, Đại Diễn Phong L&ocirc;i Kiếm!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đ&acirc;y l&agrave;!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n con mắt lập tức liền s&aacute;ng l&ecirc;n.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đại Diễn Phong L&ocirc;i Kiếm, c&ugrave;ng Phong L&ocirc;i Kiếm c&oacute; c&aacute;ch l&agrave;m kh&aacute;c nhau nhưng kết quả lại giống nhau đến k&igrave; diệu, liền giống như tại Phong L&ocirc;i Kiếm tr&ecirc;n cơ sở, tăng l&ecirc;n uy lực gấp mấy lần kết quả.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Để Dương Ch&acirc;n khiếp sợ l&agrave;, hắn cho tới b&acirc;y giờ chưa thấy qua loại c&ocirc;ng ph&aacute;p n&agrave;y, m&agrave; lại loại c&ocirc;ng ph&aacute;p n&agrave;y l&agrave;. . . Thi&ecirc;n cấp!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Thi&ecirc;n cấp c&ocirc;ng ph&aacute;p, đừng n&oacute;i Thượng Nguy&ecirc;n t&ocirc;ng kh&ocirc;ng c&oacute;, ch&iacute;nh l&agrave; cho tới nay vững v&agrave;ng &aacute;p chế Thượng Nguy&ecirc;n t&ocirc;ng một đầu Thị Kiếm m&ocirc;n, cũng kh&ocirc;ng bỏ ra nổi tới.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n trong l&ograve;ng cao hứng trở lại, vội v&agrave;ng đứng dậy đi ra ngo&agrave;i, tiện tay đem tr&ecirc;n giường tr&uacute;c trường kiếm mang ở tr&ecirc;n người.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Phong L&ocirc;i Kiếm chẳng qua l&agrave; ho&agrave;ng cấp c&ocirc;ng ph&aacute;p, Dương Ch&acirc;n tu luyện nửa năm đều kh&ocirc;ng c&oacute; nắm giữ ch&acirc;n tủy, thế nhưng l&agrave; Dương Ch&acirc;n l&uacute;c n&agrave;y c&oacute; thể cảm gi&aacute;c được, hắn đ&atilde; triệt để nắm giữ Đại Diễn Phong L&ocirc;i Kiếm, thậm ch&iacute; đạt đến l&ocirc; hỏa thuần thanh tr&igrave;nh độ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đ&acirc;y cũng l&agrave; tư chất ph&aacute; trần mang tới chỗ tốt?&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n quyết định t&igrave;m một chỗ kh&ocirc;ng người thử một lần, nếu quả như thật như vậy, vậy liền lợi hại.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Thượng Nguy&ecirc;n t&ocirc;ng hết thảy c&oacute; ba t&ograve;a sơn phong, Dương Ch&acirc;n chỗ sơn phong t&ecirc;n l&agrave; Trường Nguyệt, l&agrave; Trường Nguyệt Ch&acirc;n Nh&acirc;n tọa hạ đệ tử tu luyện sinh hoạt địa phương.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Tại Trường Nguyệt phong chỗ cao nhất, c&oacute; một c&aacute;i b&igrave;nh đ&agrave;i, nơi đ&oacute; quanh năm cương phong như đao, m&ocirc;n hạ đệ tử ng&agrave;y b&igrave;nh thường kh&ocirc;ng ai sẽ đi.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đi v&agrave;o Trường Nguyệt b&igrave;nh đ&agrave;i thời điểm, Dương Ch&acirc;n bỗng nhi&ecirc;n sững sờ, cương phong lạnh thấu xương b&ecirc;n trong, một c&aacute;i quật cường th&acirc;n ảnh m&agrave;u trắng vũ động trường kiếm, như l&ocirc;i tự điện, tay &aacute;o hết lần n&agrave;y tới lần kh&aacute;c, đẹp kh&ocirc;ng sao tả xiết, ch&iacute;nh l&agrave; Đại sư tỷ Liễu Nhược Ngưng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Đột nhi&ecirc;n, Liễu Nhược Ngưng r&ecirc;n l&ecirc;n một tiếng, th&acirc;n ảnh lảo đảo ph&iacute;a dưới ngồi sập xuống đất, tuyệt vọng tự lẩm bẩm: \"Vẫn chưa được sao?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">N&oacute;i xong, Liễu Nhược Ngưng bỗng nhi&ecirc;n đứng dậy, ti&ecirc;u điều như l&agrave; người tuyệt vọng, bước li&ecirc;n tục nhẹ nh&agrave;ng đi v&agrave;o b&igrave;nh đ&agrave;i bi&ecirc;n giới.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Lại tiến l&ecirc;n một bước, ch&iacute;nh l&agrave; v&aacute;ch đ&aacute; vạn trượng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"Sư t&ocirc;n, đồ nhi bất hiếu, cho d&ugrave; th&acirc;n tử đạo ti&ecirc;u, cũng sẽ kh&ocirc;ng gả cho Đoạn L&atilde;ng T&agrave;i c&aacute;i kia c&oacute; tiếng xấu tiểu nh&acirc;n.\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Mắt thấy Liễu Nhược Ngưng liền muốn thả người nhảy l&ecirc;n, Dương Ch&acirc;n vội v&agrave;ng đứng ra h&ocirc;: \"Sư tỷ chậm đ&atilde;!\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Liễu Nhược Ngưng to&agrave;n th&acirc;n chấn động, quay người nh&igrave;n thấy Dương Ch&acirc;n, đắng chát n&oacute;i ra: \"Sư đệ, sao ngươi lại tới đ&acirc;y?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n đi đến Liễu Nhược Ngưng b&ecirc;n người, kh&ocirc;ng n&oacute;i một lời nh&igrave;n xem Liễu Nhược Ngưng.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Liễu Nhược Ngưng bị Dương Ch&acirc;n &aacute;nh mắt lạnh l&ugrave;ng nh&igrave;n c&oacute; ch&uacute;t cục x&uacute;c bất an, nghi hoặc nh&igrave;n Dương Ch&acirc;n.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Ba!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"A! Sư đệ ngươi. . .\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Dương Ch&acirc;n một b&agrave;n tay đập v&agrave;o Liễu Nhược Ngưng c&aacute;i m&ocirc;ng b&ecirc;n tr&ecirc;n, trừng tr&ograve;ng mắt n&oacute;i ra: \"Ngươi muốn l&agrave;m g&igrave;, đ&aacute;nh kh&ocirc;ng lại liền đi chết?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">Liễu Nhược Ngưng chấn kinh ph&iacute;a dưới lui lại hai bước, ngọc dung đỏ bừng như h&agrave;, trong mắt lại đều l&agrave; vẻ tuyệt vọng, buồn b&atilde; n&oacute;i ra: \"Ta n&ecirc;n l&agrave;m c&aacute;i g&igrave;?\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">\"L&agrave;m sao b&acirc;y giờ?\" Dương Ch&acirc;n vừa trừng mắt: \"Đương nhi&ecirc;n l&agrave; g&acirc;y sự t&igrave;nh, khiến cho c&agrave;ng lớn c&agrave;ng tốt, ta gi&uacute;p ngươi.\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\" /><span style=\"color: #333333; font-family: \'Palatino Linotype\', sans-serif; font-size: 26px; background-color: #eae4d3;\">S&aacute;ch mới c&ocirc;ng bố, cầu độc giả l&atilde;o gia thuận tay điểm c&aacute;i cất giữ nu&ocirc;i sống, nhất định l&agrave; một bản thoải m&aacute;i bay tr&ecirc;n trời tiểu thuyết, kh&oacute; chịu ta biểu diễn dựng ngược!</span></p>', 0, '2019-01-18 21:14:23', 1, 0, NULL, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comment`
--

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL COMMENT 'ID bình luận',
  `userPosted` bigint(20) DEFAULT NULL COMMENT 'ID User',
  `storyId` bigint(20) DEFAULT NULL COMMENT 'ID Story',
  `content` longtext COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nội dung comment',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Bình Luận',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Bình Luận'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Bình Luận';

--
-- Đang đổ dữ liệu cho bảng `comment`
--

INSERT INTO `comment` (`id`, `userPosted`, `storyId`, `content`, `createDate`, `status`) VALUES
(1, 2, 1, 'Truyện Hay.<br />Cố gắng convert tiếp.', '2019-04-17 01:15:29', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `history`
--

CREATE TABLE `history` (
  `id` bigint(20) NOT NULL COMMENT 'ID User Favorites',
  `userId` bigint(20) DEFAULT NULL COMMENT 'ID User',
  `chapterId` bigint(20) DEFAULT NULL COMMENT 'ID Chapter',
  `locationIP` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Địa chỉ máy đọc',
  `dateView` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời Gian Đọc',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Tính Lượt Đọc'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Yêu Thích';

--
-- Đang đổ dữ liệu cho bảng `history`
--

INSERT INTO `history` (`id`, `userId`, `chapterId`, `locationIP`, `dateView`, `status`) VALUES
(54, NULL, 2, '0:0:0:0:0:0:0:2', '2018-11-07 21:14:37', 1),
(55, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 21:14:55', 1),
(56, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 21:14:56', 0),
(57, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-07 21:20:03', 1),
(58, 1, 1, '0:0:0:0:0:0:0:2', '2018-11-07 21:20:14', 1),
(59, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-07 21:21:40', 0),
(60, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-07 21:22:52', 0),
(61, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-07 21:23:11', 0),
(62, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-07 21:23:47', 0),
(63, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 21:24:13', 0),
(64, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:30:49', 1),
(65, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:30:52', 1),
(66, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:31:02', 0),
(67, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:32:03', 0),
(68, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:32:12', 0),
(69, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:35:30', 0),
(70, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:35:32', 0),
(71, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:35:35', 0),
(72, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:37:10', 0),
(73, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:37:14', 0),
(74, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-07 22:37:16', 1),
(75, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:37:19', 0),
(76, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:37:21', 0),
(77, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 22:37:24', 0),
(79, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:51:03', 0),
(80, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:51:08', 0),
(81, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:54:32', 0),
(82, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:54:34', 0),
(83, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:55:16', 0),
(84, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:55:19', 0),
(85, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:57:36', 0),
(86, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:57:38', 0),
(87, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:57:41', 0),
(88, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:59:06', 0),
(89, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:59:08', 0),
(90, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:59:50', 0),
(91, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 22:59:53', 0),
(92, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:00:26', 0),
(93, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:00:30', 0),
(94, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:01:07', 0),
(95, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:01:11', 0),
(96, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:01:34', 0),
(97, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:01:40', 0),
(98, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:02:06', 0),
(99, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:02:10', 0),
(100, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:04:20', 0),
(101, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:04:24', 0),
(102, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:04:50', 0),
(103, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:04:54', 0),
(104, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:05:58', 0),
(105, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:06:01', 0),
(106, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:06:37', 0),
(107, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:06:41', 0),
(108, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:06:47', 0),
(109, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-07 23:06:51', 0),
(110, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:07:06', 0),
(111, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:07:08', 0),
(112, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:08:09', 0),
(113, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:08:09', 0),
(114, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:08:11', 0),
(115, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:08:29', 0),
(116, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:08:31', 0),
(117, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:23', 0),
(118, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:23', 0),
(119, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:24', 0),
(120, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:26', 0),
(121, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:27', 0),
(122, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:39', 0),
(123, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:41', 0),
(124, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:42', 0),
(125, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:10:44', 0),
(126, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:12:27', 0),
(127, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:12:29', 0),
(128, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:12:31', 0),
(129, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:15:38', 1),
(130, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:15:41', 0),
(131, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:16:31', 0),
(132, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:16:35', 0),
(133, 1, 1, '0:0:0:0:0:0:0:1', '2018-11-07 23:16:56', 1),
(134, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:16:56', 0),
(135, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-07 23:16:59', 1),
(136, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-07 23:17:49', 0),
(137, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:17:50', 0),
(138, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-07 23:17:59', 0),
(139, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-07 23:18:00', 0),
(140, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-07 23:18:02', 1),
(141, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-08 10:51:09', 1),
(142, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-08 10:51:14', 1),
(143, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-08 10:51:16', 0),
(144, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-08 10:51:18', 1),
(145, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-08 20:12:38', 1),
(146, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-08 20:13:04', 0),
(147, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-08 20:18:26', 0),
(148, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-08 20:19:16', 1),
(149, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-08 20:21:55', 1),
(150, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-08 20:21:57', 0),
(151, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-13 21:55:23', 1),
(152, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-13 21:57:30', 0),
(153, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-13 22:17:35', 0),
(154, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-14 10:12:30', 1),
(155, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:06', 1),
(156, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:10', 1),
(157, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:12', 0),
(158, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:13', 0),
(159, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:14', 0),
(160, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:15', 0),
(161, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:27', 0),
(162, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:18:28', 0),
(163, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:19:02', 0),
(164, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:19:06', 0),
(165, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:19:44', 0),
(166, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:20:51', 1),
(167, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:22:00', 0),
(168, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:22:03', 0),
(169, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:25:29', 0),
(170, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:25:33', 0),
(171, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:26:20', 0),
(172, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:26:31', 0),
(173, 2, 3, '0:0:0:0:0:0:0:1', '2018-11-15 09:26:43', 1),
(174, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:26:44', 0),
(175, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:30:05', 0),
(176, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:30:33', 0),
(177, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:30:52', 0),
(178, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:31:12', 0),
(179, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:33:20', 0),
(180, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:33:29', 0),
(181, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:34:27', 0),
(182, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:34:47', 0),
(183, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:35:34', 0),
(184, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:35:44', 0),
(185, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:35:46', 0),
(186, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:39:27', 0),
(187, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:39:54', 0),
(188, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:39:59', 0),
(189, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:40:19', 0),
(190, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:40:21', 0),
(191, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:43:08', 0),
(192, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:43:11', 0),
(193, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:43:50', 0),
(194, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:43:56', 0),
(195, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:44:06', 0),
(196, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:44:36', 0),
(197, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:44:39', 0),
(198, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:45:59', 0),
(199, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:50:54', 0),
(200, 2, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:50:57', 0),
(201, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:53:20', 0),
(202, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-15 09:53:25', 0),
(203, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-15 09:54:22', 1),
(204, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-20 11:04:44', 1),
(205, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-20 11:07:38', 0),
(206, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-20 11:07:48', 1),
(207, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-20 11:07:53', 1),
(208, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-20 11:11:39', 1),
(209, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-20 11:11:45', 0),
(210, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-25 14:13:56', 1),
(211, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-25 14:14:12', 1),
(212, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 14:14:15', 1),
(213, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-25 14:14:17', 1),
(214, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 14:14:48', 0),
(215, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-25 15:06:00', 1),
(216, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:06:03', 1),
(217, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:07:01', 0),
(218, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:07:19', 0),
(219, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:16:18', 0),
(220, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:16:53', 0),
(221, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:17:56', 0),
(222, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 15:18:25', 0),
(223, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-25 22:13:05', 1),
(224, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:13:09', 1),
(225, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:14:58', 0),
(226, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:15:18', 0),
(227, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:17:15', 0),
(228, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:23:28', 0),
(229, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:23:29', 0),
(230, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:23:31', 0),
(231, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:24:51', 0),
(232, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:27:44', 0),
(233, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:27:44', 0),
(234, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:28:55', 0),
(235, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:29:10', 0),
(236, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:29:55', 0),
(237, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:43:13', 0),
(238, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:53:03', 0),
(239, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:54:05', 0),
(240, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:54:09', 0),
(241, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:54:13', 0),
(242, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:56:20', 0),
(243, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-25 22:57:40', 0),
(244, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-26 00:21:13', 1),
(245, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-26 00:21:19', 0),
(246, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-26 00:21:46', 0),
(247, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-26 00:23:20', 0),
(248, NULL, 1, '0:0:0:0:0:0:0:1', '2018-11-26 00:29:55', 0),
(249, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-26 00:34:50', 1),
(250, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-26 21:19:02', 1),
(251, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:19:06', 1),
(252, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:19:31', 0),
(253, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:20:57', 0),
(254, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:25:38', 0),
(255, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:26:37', 0),
(256, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:28:48', 0),
(257, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:28:54', 0),
(258, NULL, 4, '127.0.0.1', '2018-11-26 21:34:23', 1),
(259, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:38:38', 0),
(260, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:38:44', 0),
(261, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:43:46', 0),
(262, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:45:49', 0),
(263, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:48:52', 0),
(264, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:52:34', 0),
(265, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:52:40', 0),
(266, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:55:24', 0),
(267, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:58:05', 0),
(268, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:58:20', 0),
(269, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:58:25', 0),
(270, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:59:19', 0),
(271, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 21:59:36', 0),
(272, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:00:44', 1),
(273, 1, 4, '0:0:0:0:0:0:0:2', '2018-11-26 22:00:48', 0),
(274, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:02:06', 0),
(275, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:08:27', 0),
(276, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:08:54', 0),
(277, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:08:58', 0),
(278, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:15:56', 0),
(279, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:16:07', 0),
(280, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:23:14', 0),
(281, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:23:18', 0),
(282, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:23:33', 0),
(283, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:32:51', 0),
(284, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:32:55', 1),
(285, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-26 22:32:57', 1),
(286, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-26 22:34:03', 0),
(287, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:34:07', 0),
(288, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:34:08', 0),
(289, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:34:13', 0),
(290, 1, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:34:14', 0),
(291, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:38:28', 0),
(292, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:38:34', 0),
(293, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:38:37', 0),
(294, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:38:40', 0),
(295, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:38:42', 0),
(296, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:39:50', 0),
(297, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:42:07', 0),
(298, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:42:09', 0),
(299, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-26 22:49:00', 0),
(300, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:49:02', 0),
(301, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:49:20', 0),
(302, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:51:29', 0),
(303, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-26 22:55:56', 0),
(304, 2, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:04:49', 1),
(305, 2, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:04:51', 1),
(306, 3, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:05:11', 0),
(307, 3, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:05:12', 0),
(308, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:05:15', 0),
(309, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:06:47', 0),
(310, NULL, 4, '127.0.0.1', '2018-11-27 10:06:48', 1),
(311, 3, 4, '127.0.0.1', '2018-11-27 10:07:01', 0),
(312, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:08:31', 0),
(313, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:10:24', 0),
(314, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:14:58', 0),
(315, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:15:04', 0),
(316, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:20:04', 0),
(317, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:20:36', 0),
(318, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:21:45', 0),
(319, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:23:29', 0),
(320, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:24:38', 0),
(321, 3, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:24:56', 0),
(322, 3, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:24:57', 0),
(323, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:24:58', 0),
(324, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:26:26', 0),
(325, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:26:26', 0),
(326, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:27:03', 0),
(327, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:27:48', 0),
(328, 3, 4, '0:0:0:0:0:0:0:1', '2018-11-27 10:28:37', 0),
(329, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:41:14', 0),
(330, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:42:55', 0),
(331, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-27 10:43:03', 0),
(332, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-27 20:05:54', 1),
(333, 1, 1, '0:0:0:0:0:0:0:1', '2018-11-27 20:39:08', 1),
(334, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-27 22:10:47', 1),
(335, NULL, 4, '0:0:0:0:0:0:0:1', '2018-11-28 08:57:09', 1),
(336, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-28 11:31:43', 1),
(337, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-28 11:31:44', 0),
(338, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-28 11:32:28', 1),
(339, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-28 11:34:30', 0),
(340, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-28 11:35:20', 0),
(341, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-28 11:36:59', 0),
(342, NULL, 2, '127.0.0.1', '2018-11-28 11:37:25', 1),
(343, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-28 11:39:07', 0),
(344, 2, 3, '0:0:0:0:0:0:0:1', '2018-11-28 12:05:12', 0),
(345, 2, 3, '0:0:0:0:0:0:0:1', '2018-11-28 12:05:14', 0),
(346, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-29 01:13:59', 1),
(347, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-29 01:14:00', 0),
(348, 1, 3, '0:0:0:0:0:0:0:1', '2018-11-29 10:02:50', 1),
(349, 1, 2, '0:0:0:0:0:0:0:1', '2018-11-29 10:02:56', 1),
(350, NULL, 3, '0:0:0:0:0:0:0:1', '2018-11-29 18:11:55', 1),
(351, NULL, 2, '0:0:0:0:0:0:0:1', '2018-11-29 18:11:56', 1),
(352, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-02 11:08:46', 1),
(353, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-02 11:09:37', 0),
(354, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-02 11:09:51', 0),
(355, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-02 11:13:20', 0),
(356, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-02 11:15:08', 0),
(357, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:15:23', 1),
(358, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:20:03', 0),
(359, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:22:22', 0),
(360, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:22:42', 0),
(361, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:23:29', 0),
(362, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:23:50', 0),
(363, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:29:10', 0),
(364, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:30:03', 0),
(365, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:30:22', 0),
(366, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:30:33', 0),
(367, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:30:33', 0),
(368, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:30:43', 0),
(369, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:30:52', 0),
(370, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:31:14', 0),
(371, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:48:55', 0),
(372, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:49:27', 0),
(373, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:50:12', 0),
(374, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:50:42', 0),
(375, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:51:31', 0),
(376, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 11:52:16', 0),
(377, 1, 4, '0:0:0:0:0:0:0:1', '2018-12-02 15:53:23', 1),
(378, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 18:54:22', 1),
(379, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 18:55:21', 0),
(380, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 18:56:03', 0),
(381, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 18:56:35', 0),
(382, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 18:59:21', 0),
(383, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 18:59:27', 0),
(384, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:03:15', 0),
(385, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:03:27', 0),
(386, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:05:49', 0),
(387, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:06:40', 0),
(388, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:07:52', 0),
(389, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:08:07', 0),
(390, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:08:56', 0),
(391, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:09:14', 0),
(392, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:09:28', 0),
(393, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:16:41', 0),
(394, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 19:17:13', 0),
(395, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 21:52:06', 1),
(396, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-02 21:52:34', 0),
(397, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-04 10:40:30', 1),
(398, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-04 21:11:23', 1),
(399, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:18', 1),
(400, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:21', 0),
(401, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:23', 1),
(402, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:26', 0),
(403, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:29', 0),
(404, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:50', 0),
(405, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:55', 0),
(406, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-04 21:13:56', 0),
(407, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-04 21:37:59', 1),
(408, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-04 21:43:44', 0),
(409, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-05 01:39:40', 1),
(410, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-06 00:56:08', 1),
(411, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-06 01:06:34', 0),
(412, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-06 01:06:37', 0),
(413, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-06 01:10:29', 1),
(414, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-06 10:46:14', 1),
(415, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-07 22:05:57', 1),
(416, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-08 10:56:05', 1),
(417, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:53:08', 1),
(418, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:53:47', 0),
(419, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:53:57', 0),
(420, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:54:26', 0),
(421, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:54:53', 0),
(422, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:55:49', 0),
(423, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 17:55:58', 0),
(424, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-10 20:09:29', 1),
(425, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-10 21:54:21', 1),
(426, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 21:54:35', 0),
(427, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 21:58:06', 0),
(428, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 21:58:57', 0),
(429, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 21:59:01', 0),
(430, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 22:27:20', 0),
(431, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 22:27:37', 0),
(432, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 22:27:44', 0),
(433, 1, 4, '0:0:0:0:0:0:0:1', '2018-12-10 22:29:14', 1),
(434, 1, 3, '0:0:0:0:0:0:0:1', '2018-12-10 22:29:16', 0),
(435, 1, 2, '0:0:0:0:0:0:0:1', '2018-12-10 22:29:18', 1),
(436, 2, 1, '0:0:0:0:0:0:0:1', '2018-12-11 11:58:31', 1),
(437, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-18 09:06:54', 1),
(438, 2, 4, '0:0:0:0:0:0:0:1', '2018-12-18 11:19:52', 1),
(439, 2, 4, '0:0:0:0:0:0:0:1', '2018-12-18 11:22:51', 0),
(440, 2, 1, '0:0:0:0:0:0:0:1', '2018-12-18 11:48:34', 1),
(441, 2, 1, '0:0:0:0:0:0:0:1', '2018-12-18 11:49:03', 0),
(442, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:26:58', 1),
(443, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:27:30', 0),
(444, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:28:09', 0),
(445, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:29:17', 0),
(446, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:30:09', 0),
(447, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:31:07', 0),
(448, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:31:54', 0),
(449, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-19 09:32:28', 0),
(450, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-20 08:48:33', 1),
(451, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 08:48:38', 1),
(452, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 08:50:22', 0),
(453, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 08:50:27', 0),
(454, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 08:55:29', 0),
(455, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 09:06:02', 0),
(456, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 09:06:37', 0),
(457, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 09:07:45', 0),
(458, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-20 17:44:45', 1),
(459, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:44:49', 1),
(460, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:46:08', 0),
(461, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:46:35', 0),
(462, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:46:43', 0),
(463, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:47:56', 0),
(464, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:51:53', 0),
(465, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 17:58:37', 0),
(466, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-20 18:25:26', 1),
(467, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 18:25:33', 0),
(468, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 18:28:23', 0),
(469, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-20 21:41:12', 1),
(470, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 22:56:31', 1),
(471, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 22:56:51', 0),
(472, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 22:57:18', 0),
(473, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 22:58:22', 0),
(474, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 22:58:36', 0),
(475, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 22:58:47', 0),
(476, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:01:10', 0),
(477, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:01:19', 0),
(478, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:01:35', 0),
(479, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:01:43', 0),
(480, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:07:27', 0),
(481, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:07:46', 0),
(482, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:07:55', 0),
(483, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:09:43', 0),
(484, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:11:21', 0),
(485, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:11:39', 0),
(486, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:11:51', 0),
(487, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:11:52', 0),
(488, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:48:40', 1),
(489, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-20 23:49:06', 0),
(490, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-21 01:04:10', 1),
(491, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-21 01:04:36', 0),
(492, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-21 01:07:48', 0),
(493, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-21 01:09:34', 0),
(494, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-21 01:09:38', 0),
(495, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 09:56:48', 1),
(496, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 10:02:39', 0),
(497, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 10:07:23', 0),
(498, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 11:11:03', 1),
(499, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 11:11:03', 0),
(500, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 11:13:39', 0),
(501, NULL, 1, '0:0:0:0:0:0:0:1', '2018-12-21 11:13:39', 0),
(502, NULL, 4, '0:0:0:0:0:0:0:1', '2018-12-23 21:09:02', 1),
(503, NULL, 3, '0:0:0:0:0:0:0:1', '2018-12-23 21:09:10', 1),
(504, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-23 21:09:14', 1),
(505, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-23 21:09:41', 0),
(506, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-23 21:13:15', 0),
(507, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-23 21:27:33', 0),
(508, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-23 21:31:02', 0),
(509, NULL, 2, '0:0:0:0:0:0:0:1', '2018-12-23 21:45:52', 0),
(510, NULL, 4, '0:0:0:0:0:0:0:1', '2019-01-15 10:47:44', 1),
(511, NULL, 4, '0:0:0:0:0:0:0:1', '2019-01-15 11:05:21', 0),
(512, NULL, 3, '0:0:0:0:0:0:0:1', '2019-01-15 19:21:31', 1),
(513, NULL, 1, '0:0:0:0:0:0:0:1', '2019-01-15 19:50:02', 1),
(514, NULL, 1, '0:0:0:0:0:0:0:1', '2019-01-15 19:50:16', 0),
(515, NULL, 1, '0:0:0:0:0:0:0:1', '2019-01-15 20:07:45', 0),
(516, NULL, 1, '0:0:0:0:0:0:0:1', '2019-01-15 20:08:20', 0),
(517, NULL, 4, '0:0:0:0:0:0:0:1', '2019-01-15 20:08:45', 1),
(518, NULL, 3, '0:0:0:0:0:0:0:1', '2019-01-15 20:08:56', 1),
(519, NULL, 3, '0:0:0:0:0:0:0:1', '2019-01-15 20:09:14', 0),
(520, NULL, 3, '0:0:0:0:0:0:0:1', '2019-01-15 20:09:47', 0),
(521, NULL, 3, '0:0:0:0:0:0:0:1', '2019-01-15 20:09:59', 0),
(522, NULL, 3, '0:0:0:0:0:0:0:1', '2019-01-15 20:10:21', 0),
(523, 1, 1, '0:0:0:0:0:0:0:1', '2019-01-18 11:09:07', 1),
(524, 1, 4, '127.0.0.1', '2019-01-18 23:12:21', 1),
(525, 1, 6, '0:0:0:0:0:0:0:1', '2019-01-19 04:07:08', 1),
(526, 1, 6, '0:0:0:0:0:0:0:1', '2019-01-19 04:09:30', 0),
(527, NULL, 1, '0:0:0:0:0:0:0:1', NULL, 1),
(528, NULL, 1, '0:0:0:0:0:0:0:1', NULL, 1),
(529, NULL, 1, '0:0:0:0:0:0:0:1', NULL, 1),
(530, NULL, 1, '0:0:0:0:0:0:0:1', NULL, 1),
(531, NULL, 6, '0:0:0:0:0:0:0:1', NULL, 1),
(532, NULL, 6, '0:0:0:0:0:0:0:1', NULL, 1),
(533, NULL, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(534, NULL, 3, '0:0:0:0:0:0:0:1', NULL, 1),
(535, NULL, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(536, NULL, 3, '0:0:0:0:0:0:0:1', NULL, 1),
(537, NULL, 2, '0:0:0:0:0:0:0:1', NULL, 1),
(538, NULL, 3, '0:0:0:0:0:0:0:1', NULL, 1),
(539, NULL, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(540, NULL, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(541, NULL, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(542, 2, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(543, 2, 3, '0:0:0:0:0:0:0:1', NULL, 1),
(544, 2, 2, '0:0:0:0:0:0:0:1', NULL, 1),
(545, 2, 3, '0:0:0:0:0:0:0:1', NULL, 1),
(546, 2, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(547, 2, 3, '0:0:0:0:0:0:0:1', NULL, 1),
(548, 2, 4, '0:0:0:0:0:0:0:1', NULL, 1),
(549, 2, 4, '0:0:0:0:0:0:0:1', NULL, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `information`
--

CREATE TABLE `information` (
  `id` int(11) NOT NULL COMMENT 'ID Infomation',
  `introduce` text COLLATE utf8_unicode_ci COMMENT 'Thông tin giới Thiệu Web',
  `email` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Email WebSite',
  `phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Điện Thoại Liên Hệ',
  `skype` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Địa Chỉ Skype',
  `logo` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Logo Website',
  `favicon` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Favicon WebSite'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thông Tin Web';

--
-- Đang đổ dữ liệu cho bảng `information`
--

INSERT INTO `information` (`id`, `introduce`, `email`, `phone`, `skype`, `logo`, `favicon`) VALUES
(1, 'TruyenOnline là website đọc truyện convert online cập nhật liên tục và nhanh nhất các truyện tiên hiệp, kiếm hiệp, huyền ảo được các thành viên liên tục đóng góp rất nhiều truyện hay và nổi bật.', 'apt.truyenonline@gmail.com', NULL, 'truyenonline', 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', 'https://res.cloudinary.com/thang1988/image/upload/v1541176897/truyenmvc/favicon.ico');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `pay`
--

CREATE TABLE `pay` (
  `id` bigint(20) NOT NULL COMMENT 'ID pay',
  `userSend` bigint(20) DEFAULT NULL COMMENT 'ID người trả phí',
  `storyId` bigint(20) DEFAULT NULL COMMENT 'ID Truyện',
  `chapterId` bigint(20) DEFAULT NULL COMMENT 'ID Chapter Trả',
  `userReceived` bigint(20) DEFAULT NULL COMMENT 'ID người Nhận',
  `money` bigint(20) DEFAULT NULL COMMENT 'Price Trả Phí',
  `type` int(11) NOT NULL COMMENT 'Loại giao dịch',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Trả Phí',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Thanh Toán',
  `vote` int(11) DEFAULT '0' COMMENT 'Số Phiếu Đề Cử'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thanh Toán Phí';

--
-- Đang đổ dữ liệu cho bảng `pay`
--

INSERT INTO `pay` (`id`, `userSend`, `storyId`, `chapterId`, `userReceived`, `money`, `type`, `createDate`, `status`, `vote`) VALUES
(6, 1, NULL, 1, 2, 100, 1, '2019-01-15 09:38:31', 1, 0),
(7, 1, NULL, 1, 2, 100, 1, '2019-01-15 09:39:18', 1, 0),
(8, 1, 5, NULL, NULL, 1000, 4, '2019-01-15 09:40:13', 1, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL COMMENT 'ID Phân QUyền ',
  `name` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên Phân Quyền'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Phân Quyền Người Dùng';

--
-- Đang đổ dữ liệu cho bảng `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_SMOD'),
(3, 'ROLE_CONVERTER'),
(4, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `story`
--

CREATE TABLE `story` (
  `id` bigint(20) NOT NULL COMMENT 'ID Truyện',
  `vnName` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'Tên truyện Tiếng Việt',
  `cnName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên truyện tiếng trung',
  `cnLink` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Link truyện tiếng Trung',
  `images` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Hình ảnh truyện',
  `author` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tác giả truyện',
  `infomation` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Thông tin truyện',
  `countAppoint` int(11) DEFAULT '0',
  `countView` int(11) DEFAULT '0' COMMENT 'Số lượt xem',
  `rating` float DEFAULT '0' COMMENT 'Điểm đánh giá',
  `userPosted` bigint(20) NOT NULL COMMENT 'Người Đăng',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `price` int(11) DEFAULT '0' COMMENT 'Giá truyện',
  `timeDeal` int(11) DEFAULT NULL COMMENT 'Thời gian trả phí',
  `dealStatus` int(11) DEFAULT '0' COMMENT 'Trạng Thái Trả Phí',
  `updateDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Cập Nhật',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Truyện'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Truyện';

--
-- Đang đổ dữ liệu cho bảng `story`
--

INSERT INTO `story` (`id`, `vnName`, `cnName`, `cnLink`, `images`, `author`, `infomation`, `countAppoint`, `countView`, `rating`, `userPosted`, `createDate`, `price`, `timeDeal`, `dealStatus`, `updateDate`, `status`) VALUES
(1, 'Tu Chân Nói Chuyện Phiếm Quần', '修真聊天群', 'http://www.qidian.com/Book/3602691.aspx', 'https://res.cloudinary.com/thang1988/image/upload/v1539535310/truyenmvc/tu-chan-noi-chuyen-phiem-quan.jpg', 'Thánh kỵ sĩ truyền thuyết', '<p>Một ng&agrave;y, Tống Thư H&agrave;ng ngo&agrave;i &yacute; muốn gia nhập một c&aacute;i ti&ecirc;n hiệp chuunibyou th&acirc;m ni&ecirc;n người bệnh giao lưu quần, b&ecirc;n trong quần bạn nh&oacute;m đều lấy \'Đạo hữu\' tương xứng, quần danh thiếp đều l&agrave; c&aacute;c loại Phủ chủ, Động chủ, Ch&acirc;n nh&acirc;n, Thi&ecirc;n Sư. Liền chủ nhóm lạc đường ch&oacute; cảnh đều xưng l&agrave; Đại y&ecirc;u khuyển rời nh&agrave; trốn đi. Cả ng&agrave;y n&oacute;i chuyện l&agrave; luyện đan, x&ocirc;ng b&iacute; cảnh, luyện c&ocirc;ng kinh nghiệm c&aacute;i g&igrave;. Đột nhi&ecirc;n c&oacute; một ng&agrave;y, lặn xuống nước hồi l&acirc;u hắn đột nhi&ecirc;n ph&aacute;t hiện... Trong đ&aacute;m mỗi một c&aacute;i quần vi&ecirc;n, v&acirc;̣y mà to&agrave;n bộ l&agrave; Tu ch&acirc;n giả, có th&ecirc;̉ di sơn đảo hải, trường sinh ng&agrave;n năm cái chủng loại kia!</p>', 1, 30, 10, 1, '2018-09-01 22:48:36', 100, 30, 1, '2018-09-10 22:48:35', 1),
(2, 'Nhất Niệm Vĩnh Hằng', '一念永恒', 'http://www.qidian.com/Book/1003354631.aspx', 'http://res.cloudinary.com/thang1988/image/upload/v1/truyenmvc/administrator-476962173686570', 'Nhĩ Căn', '<p>Nhất niệm th&agrave;nh thương hải , nhất niệm h&oacute;a tang điền . nhất niệm trảm thi&ecirc;n ma , nhất niệm tru vạn ti&ecirc;n . duy ng&atilde; niệm &hellip;&hellip; vĩnh hằng</p>', 1, 105, 8, 1, '2018-09-10 22:49:46', 120, 15, 1, '2018-11-07 22:30:00', 2),
(3, 'Dị Thế Giới Mỹ Thực Gia', '异世界的美食家', 'https://www.lread.net/read/51823/', 'https://res.cloudinary.com/thang1988/image/upload/v1539535665/truyenmvc/di-the-gioi-my-thuc-gia.jpg', 'Lý Hồng Thiên', '<p><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Ở Vũ Giả nhấc tay c&oacute; thể Liệt Sơn xuy&ecirc;n, s&uacute;y ch&acirc;n c&oacute; thể đoạn trường h&agrave; Huyền Huyễn tr&ecirc;n thế giới, tồn tại như vậy một nh&agrave; nh&agrave; h&agrave;ng nhỏ .&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Nh&agrave; h&agrave;ng nhỏ kh&ocirc;ng lớn, nhưng l&agrave; v&ocirc; số cường giả đỉnh cao xua như xua vịt chi địa .&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Ở đ&agrave;ng kia ngươi c&oacute; thể thưởng thức được d&ugrave;ng trứng Phượng Ho&agrave;ng c&ugrave;ng Long Huyết m&eacute;t x&agrave;o th&agrave;nh cơm x&agrave;o trứng .&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Ở đ&agrave;ng kia ngươi c&oacute; thể H&aacute;t đ&aacute;o Sinh Mệnh Chi Tuyền xứng Chu Quả chế ri&ecirc;ng Liệt Tửu .&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Ở đ&agrave;ng kia ngươi c&oacute; thể ăn được Cửu Giai Th&aacute;nh Th&uacute; nhục th&acirc;n hợp với Hắc hồ ti&ecirc;u thịt quay .&nbsp;</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">C&aacute;i g&igrave; ngươi nghĩ đem đầu bếp bắt về nh&agrave; ? Kh&ocirc;ng c&oacute; khả năng, bởi v&igrave; nh&agrave; h&agrave;ng cửa nằm một con giữ cửa Thập Giai Thần Th&uacute;, Địa Ngục Khuyển .&nbsp;</span></p>', 99, 0, 10, 1, '2018-09-10 22:49:46', 125, 40, 1, '2018-09-10 22:49:47', 1),
(4, 'Mục Thần Ký', '牧神记', 'https://book.qidian.com/info/1009704712', 'https://res.cloudinary.com/thang1988/image/upload/v1539535810/truyenmvc/muc-than-ky.jpg', 'Trạch Trư', '<p>Đại khư tổ huấn n&oacute;i, trời tối, đừng đi ra ngo&agrave;i. Đại khư t&agrave;n l&atilde;o th&ocirc;n gi&agrave; yếu t&agrave;n tật bọn họ từ bờ s&ocirc;ng nhặt được một đứa b&eacute;, lấy t&ecirc;n Tần Mục, ngậm đắng nuốt cay đem hắn nu&ocirc;i lớn. Một ng&agrave;y n&agrave;y m&agrave;n đ&ecirc;m bu&ocirc;ng xuống, b&oacute;ng tối bao tr&ugrave;m đại khư, Tần Mục đi ra cửa ch&iacute;nh. . . L&agrave;m gi&oacute; xu&acirc;n b&ecirc;n trong nhộn nhạo nh&acirc;n vật phản diện đi! Người m&ugrave; n&oacute;i với hắn. Tần Mục nh&acirc;n vật phản diện chi lộ, ngay tại quật khởi!</p>', 0, 0, 0, 1, '2018-09-13 09:29:52', 0, 0, 0, '2018-09-13 09:29:52', 3),
(5, 'Đột Nhiên Vô Địch', '突然无敌了', 'http://www.uukanshu.com/b/81355/', 'http://res.cloudinary.com/thang1988/image/upload/v1/truyenmvc/administrator-479161545197145', 'Đạp Tiên Lộ Băng Trần', '<p><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Đạo Thi&ecirc;n Qu&acirc;n</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Một c&aacute;i b&igrave;nh thường, danh tự tự kỷ thiếu ni&ecirc;n lang, một giấc thức tỉnh, ph&aacute;t hiện tự m&igrave;nh đi v&agrave;o thế giới xa lạ.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Ở tr&ecirc;n bầu trời đại điểu l&agrave; c&aacute;i g&igrave;, kia so núi c&ograve;n cao hầu tử l&agrave; c&aacute;i g&igrave;. . .</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Nguy&ecirc;n lai m&igrave;nh chỗ tinh vực gọi Bắc Đẩu.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">A, c&aacute;c loại, Bắc Đẩu, tu luyện cấp bậc l&agrave; Lu&acirc;n Hải cảnh, Đạo Cung b&iacute; cảnh. . . C&aacute;i n&agrave;y mẹ n&oacute; kh&ocirc;ng phải Gi&agrave; Thi&ecirc;n thế giới đi. Giống như lại kh&ocirc;ng đ&uacute;ng, ta thế n&agrave;o nghe n&oacute;i c&ograve;n c&oacute; mười hai Ti&ecirc;n Thể, hẳn l&agrave; đ&acirc;y l&agrave; nhất đại Bức Vương thế giới. Kh&ocirc;ng đ&uacute;ng, kh&ocirc;ng đ&uacute;ng, ta mẹ n&oacute; thấy c&aacute;i g&igrave;, Trấn Thi&ecirc;n Hải Th&agrave;nh, thế nhưng l&agrave; tại sao kh&ocirc;ng c&oacute; Bức Đế đ&acirc;u, trong lịch sử kh&ocirc;ng c&oacute; &Acirc;m Nha.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Đạo Thi&ecirc;n Qu&acirc;n suy nghĩ c&oacute; ch&uacute;t lộn xộn.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">Một ng&agrave;y n&agrave;o đ&oacute;.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">\"Đối diện kẻ chăn tr&acirc;u ph&iacute;a trước rất nguy hiểm, l&agrave; c&aacute;i nuốt người ti&ecirc;n động, đừng đi\" . . . Đạo Thi&ecirc;n Qu&acirc;n hữu hảo khuy&ecirc;n can.</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\">\"Tạ ơn, ngươi người thật tốt, ngươi t&ecirc;n l&agrave; g&igrave;, ta gọi L&yacute; Thất Dạ.\"</span></p>', 1, 6, 0, 1, '2019-01-17 23:01:09', 0, 0, 0, '2019-01-17 23:01:09', 1),
(6, 'Vô Địch Từ Max Cấp Thuộc Tính Bắt Đầu', '無敵從滿級屬性開始', 'https://www.wfxs.org/html/297589/', 'http://res.cloudinary.com/thang1988/image/upload/v1/truyenmvc/administrator-558834158888772', 'Nhất Xích Nam Phong', '<p style=\"text-align: left;\"><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">Xuy&ecirc;n qua th&agrave;nh tu ch&acirc;n đại lục một c&aacute;i củi mục, c&aacute;i kia c&ograve;n tu ch&acirc;n em g&aacute;i nh&agrave; ngươi?</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">Một đạo thất thải h&agrave;o quang sau đ&oacute;, Dương Ch&acirc;n trực tiếp k&eacute;o banh trời!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">Hắn nh&igrave;n qua c&ocirc;ng ph&aacute;p, trực tiếp m&atilde;n phẩm max cấp, học đều kh&ocirc;ng học hết!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">Hắn luyện chế đan dược, chẳng những khởi tử hồi sinh, c&ograve;n c&oacute; thể thanh xu&acirc;n m&atilde;i m&atilde;i, bao nhi&ecirc;u th&aacute;nh nữ ti&ecirc;n tử đau khổ muốn nhờ, điều kiện g&igrave; đều chịu đ&aacute;p ứng!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">Hắn r&egrave;n đ&uacute;c vũ kh&iacute;, tr&ecirc;n đ&aacute;nh Thần Vương Đại Đế, dưới đ&acirc;m Ho&agrave;ng Tuyền U Ngục, mỗi một kiện đều để thi&ecirc;n địa run rẩy, để thần ma tr&aacute;nh lui!</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">Khụ khụ. . .</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">\"Ta từ trước tới giờ kh&ocirc;ng đ&oacute;ng vai heo trang bức, bởi v&igrave; ta thực ngưu một nh&oacute;m!\"</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">☆☆☆ Luca Chi Hố ☆☆☆</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">_Si&ecirc;u Ph&agrave;m cảnh gồm: Ngưng Nguy&ecirc;n Kỳ - Tr&uacute;c Cơ Kỳ - Tiểu Thừa Kỳ - Kim Đan Kỳ - Nguy&ecirc;n Anh Kỳ - Luyện Hư Kỳ - Thần Du Kỳ</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">_Thi&ecirc;n Nh&acirc;n cảnh</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">_Th&aacute;nh Giả cảnh</span><br style=\"box-sizing: border-box; color: #333333; font-family: \'source sans pro\', sans-serif; font-size: 15px;\" /><span style=\"color: #333333; font-family: \'times new roman\', times, serif; font-size: 15px;\">_Đại Đế cảnh.</span></p>', 0, 9, 0, 1, '2019-01-18 21:08:31', 150, 30, 0, '2019-01-18 21:08:31', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `story_category`
--

CREATE TABLE `story_category` (
  `storyId` bigint(20) NOT NULL COMMENT 'ID của Truyện',
  `categoryId` int(11) NOT NULL COMMENT 'ID Của Category'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thể Loại Truyện';

--
-- Đang đổ dữ liệu cho bảng `story_category`
--

INSERT INTO `story_category` (`storyId`, `categoryId`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 5),
(2, 2),
(2, 3),
(3, 1),
(3, 3),
(3, 4),
(4, 2),
(4, 3),
(5, 3),
(6, 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `displayName` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `notification` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gold` double DEFAULT '0',
  `avatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'avatar.jpg',
  `modifiedDate` datetime DEFAULT NULL,
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Người dùng';

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `displayName`, `email`, `notification`, `gold`, `avatar`, `modifiedDate`, `createDate`, `status`) VALUES
(1, 'administrator', '$2a$10$N0eqNiuikWCy9ETQ1rdau.XEELcyEO7kukkfoiNISk/9F7gw6eB0W', 'Chủ Biên', 'administrator@gmail.com', 'Đời Không Như Là Mơ!', 45800, 'http://res.cloudinary.com/thang1988/image/upload/v1/truyenmvc/administrator-305181592391916', '2018-11-08 11:31:13', '2018-08-15 18:26:31', 1),
(2, 'nhubang8x', '$2a$10$14udn9Gd4K6txSOB9Do5Y.qwUHVCvPe0Xag6GvO/RnR8ggxX1Hnee', 'Huy Thắng', 'tieupham304@gmail.com', 'Đời Không Như Là Mơ Đâu Cưng', 500, 'http://res.cloudinary.com/thang1988/image/upload/v1/truyenmvc/nhubang8x-385209869233200', '2018-10-16 10:11:53', '2018-08-15 18:30:13', 1),
(3, 'hieu13', '$2a$10$JlnArVWUSRMccWGTGM5Xv.5DzWNEQRgSJWbhkQLTovNtv6oKEBhX.', 'hieu13', 'hieu13@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', '2019-01-18 23:43:52', '2018-09-23 19:22:24', 1),
(5, 'tieupham304', '$2a$10$F6AV3A2HvcfHGk4fUje35.caAKEDAJs0CAi4/APoGEWRR.JPkt2ve', 'tieupham304', 'tieupham1988@gmail.com', NULL, 0, NULL, NULL, '2018-10-15 08:34:46', 1),
(6, 'tieupham1988', '$2a$10$dtGgOrabNjGVN1joaH6M7Ourdu/sC11DTlH8uw6Yea7eHjKbiq9Y6', 'tieupham1988', 'tieupham30488@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 08:35:49', 1),
(8, 'nttvirgos', '$2a$10$FyPDfA1IV8251Ztuj8dzCek8yMBaectgCNbjI7xBaFQ9ZyUFgSoYi', 'nttvirgos', 'nttvirgos@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 08:37:17', 1),
(9, 'ngocanh58', '$2a$10$ixQ06gs9YAqo.sq7rPTc6ujyLIhZ.mt.H7HrCgxCPadYtAXyLPjQ.', 'ngocanh58', 'ngocnha58@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 18:02:17', 1),
(10, 'hieu131', '$2a$10$Ctl/GBtacG7Xbk8Dn9zG8uGFqNk6BFdRz/QfOYtozwlZ6Mo55kWLG', 'hieu131', 'hieu131@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 18:10:48', 1),
(11, 'nhubang99x', '$2a$10$ETb09C4yyeAP7NaeMWcJ5e1d1LlqubudZHT7Yacuzn5NyLU6UlGKO', 'nhubang99x', 'nhubang9x@yahoo.com.vn', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 21:10:36', 1),
(13, 'aahaah', '$2a$10$JPpZHSBIMQT1k4aneplE2uaDVcRJfFz.kfkbFpPF5ldUADJ6UK9HS', 'aahaah', 'aahaah@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:07:11', 1),
(14, 'aahaah2', '$2a$10$njIa/OvEICKvCV06yVkrY.9H9BGUYyoAP0doZwvi/KsO.gzqBu8a6', 'aahaah2', 'aahaah2@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:10:34', 1),
(15, 'aahaah22', '$2a$10$N.AxvloIYKjkdYDCU.NBzeEjilHxqSv1uPyxJ.NA403NPetkL54mW', 'aahaah22', 'aahaah22@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:12:08', 1),
(16, 'aahaah221', '$2a$10$6tFyOuGD8LnAK/bkNST5m.S8uQNCh1ZCLMBnbZqI341i/8cIIhvdu', 'aahaah221', 'aahaah122@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:14:20', 1),
(17, 'aahaah2212', '$2a$10$HMjuC6/AgR2XR.jUG/DN0OglJV9LctXT3w/XAZN3IK2AJYJktH8Ey', 'aahaah2212', 'aahaah2212@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:16:23', 1),
(18, 'aahaah22122', '$2a$10$PNdQaj/23pQdxLbDlTezFOltMyS2HpBUKI9oo83s6gl/mTk1HGEVm', 'aahaah22122', 'aahaah22122@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:18:16', 1),
(19, 'hhhhhh', '$2a$10$TSQUJ3hg1XvuiPSbJQw2VeYSvwuik3e/.IybwcM.R0j/sRsyIfw76', 'hhhhhh', 'hhhhhh@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:52:22', 1),
(20, 'as22', '$2a$10$5NYlytPaKXNgyJk.ekH2JOl7jKguHH8rFzhK6gSBMkCLbo1bCKbHy', 'as22', 'as22@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:04:28', 1),
(21, 'asdasda35', '$2a$10$9RHeI5.xRP8vBYll/kLgsuWNa3emRqRn2w1BppGe7KwC60AIQtVHi', 'asdasda35', 'asdasda35@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:07:23', 1),
(22, '12311', '$2a$10$gUorG4gvQHwOyZyXCMNcnuLJ14at3g4Grsf3mGElkrQobBofQljtC', '12311', '12311@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:10:54', 1),
(23, 'nhubang8x11', '$2a$10$ODzw6XeVg8HzdedHtJJwde/CFDSnMU3kvy1lPt137xMz477bemMWm', 'nhubang8x11', 'nhubang8x11@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:21:48', 1),
(24, 't43t3t43', '$2a$10$Mv6m.7W98X89bsDu9i11ke4t9AQI7CdLi9uH/5UN9c9iC2TThIKXi', 't43t3t43', 't43t3t43@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:35:07', 1),
(25, '324234', '$2a$10$QfHy9FRvQINh/OI/1X4ZcuAcjFiHfISwpfHAzxj1yLpK8l4LKrnja', '324234', '324234@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:43:36', 1),
(26, '32r23r', '$2a$10$2O65AuKFxE7oO/io0nTkTeICZkFKNK0MywaR8orpgvI//euBKThCS', '32r23r', '32r23r@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:45:15', 1),
(27, 'tiupham1988', '$2a$10$erVd7K9uqFxq.0fnszkFdOmxsLESffbXPF2fS/8Rp8XJtuZ6RD7cG', 'tiupham1988', 'tiupham1988@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-29 19:56:39', 1),
(28, 'dsafsf', '$2a$10$ufI8Fn4sWWmnjwepSHPl3ec2buBwhfXD/9CXlBgBxmUKejdw1Dtn6', NULL, 'asdfa@gmail.com', NULL, 0, NULL, NULL, '2018-12-02 19:23:21', 1),
(29, '123', '$2a$08$opRiSa2ejbhYOw097aZlUuRb/UOt.bcZbJn4Xev.wnd63LPatIUGS', NULL, 'huythang304@gmail.com', NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_rating`
--

CREATE TABLE `user_rating` (
  `userId` bigint(20) NOT NULL COMMENT 'ID User',
  `storyId` bigint(20) NOT NULL COMMENT 'ID Truyện',
  `locationIP` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Địa chỉ IP',
  `rating` int(11) NOT NULL COMMENT 'Đánh Giá',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Đánh Giá'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Đánh Giá Truyện';

--
-- Đang đổ dữ liệu cho bảng `user_rating`
--

INSERT INTO `user_rating` (`userId`, `storyId`, `locationIP`, `rating`, `createDate`) VALUES
(2, 1, '0:0:0:0:0:0:0:1', 10, '2019-01-15 09:56:03'),
(3, 1, '0:0:0:0:0:0:0:1', 10, '2018-12-18 22:47:02'),
(3, 3, '0:0:0:0:0:0:0:1', 10, '2018-12-18 22:52:48'),
(3, 4, '0:0:0:0:0:0:0:1', 10, '2018-12-18 22:52:14');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_role`
--

CREATE TABLE `user_role` (
  `userId` bigint(20) NOT NULL COMMENT 'ID User',
  `roleId` int(11) NOT NULL COMMENT 'ID của Role'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Quyền Của User';

--
-- Đang đổ dữ liệu cho bảng `user_role`
--

INSERT INTO `user_role` (`userId`, `roleId`) VALUES
(1, 1),
(1, 4),
(2, 3),
(2, 4),
(3, 4),
(5, 4),
(6, 4),
(8, 2),
(8, 4),
(9, 4),
(10, 2),
(10, 4),
(11, 4),
(13, 4),
(14, 4),
(15, 4),
(16, 4),
(17, 4),
(18, 4),
(19, 4),
(20, 4),
(21, 4),
(22, 4),
(23, 4),
(24, 4),
(25, 4),
(26, 4),
(27, 4),
(28, 4),
(29, 4);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cateogry_cName_uindex` (`name`),
  ADD UNIQUE KEY `cateogry_cMetatitle_uindex` (`metatitle`);

--
-- Chỉ mục cho bảng `chapter`
--
ALTER TABLE `chapter`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chapter_story_sID_fk` (`storyId`),
  ADD KEY `chapter_user_uID_fk` (`userPosted`);

--
-- Chỉ mục cho bảng `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `comment_user_uID_fk` (`userPosted`),
  ADD KEY `comment_story_sID_fk` (`storyId`);

--
-- Chỉ mục cho bảng `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ufavorites_user_uID_fk` (`userId`),
  ADD KEY `ufavorites_chapter_chID_fk` (`chapterId`);

--
-- Chỉ mục cho bảng `information`
--
ALTER TABLE `information`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `pay`
--
ALTER TABLE `pay`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chdeal_user_uID_fk` (`userSend`),
  ADD KEY `chdeal_chapter_chID_fk` (`chapterId`),
  ADD KEY `pay_user_uID_fk` (`userReceived`),
  ADD KEY `pay_story_id_fk` (`storyId`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `story`
--
ALTER TABLE `story`
  ADD PRIMARY KEY (`id`),
  ADD KEY `story_user_uID_fk` (`userPosted`);

--
-- Chỉ mục cho bảng `story_category`
--
ALTER TABLE `story_category`
  ADD PRIMARY KEY (`storyId`,`categoryId`),
  ADD KEY `_scategory_cateogry_cID_fk` (`categoryId`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_uName_uindex` (`username`),
  ADD UNIQUE KEY `user_uEmail_uindex` (`email`),
  ADD UNIQUE KEY `UKq0357f7kx1k6ntp6bvus0x694` (`email`),
  ADD UNIQUE KEY `UKstx4oc1652ecsjfu7ln8e9bmi` (`username`),
  ADD UNIQUE KEY `user_uDname_uindex` (`displayName`),
  ADD UNIQUE KEY `UK6jmiamq45k36v745fac2gwwnu` (`displayName`);

--
-- Chỉ mục cho bảng `user_rating`
--
ALTER TABLE `user_rating`
  ADD PRIMARY KEY (`userId`,`storyId`),
  ADD KEY `_srating_story_sID_fk` (`storyId`);

--
-- Chỉ mục cho bảng `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`userId`,`roleId`),
  ADD KEY `_urole_role_rID_fk` (`roleId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `chapter`
--
ALTER TABLE `chapter`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID Chapter', AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `comment`
--
ALTER TABLE `comment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID bình luận', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `history`
--
ALTER TABLE `history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID User Favorites', AUTO_INCREMENT=550;

--
-- AUTO_INCREMENT cho bảng `information`
--
ALTER TABLE `information`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID Infomation', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `pay`
--
ALTER TABLE `pay`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID pay', AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID Phân QUyền ', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `story`
--
ALTER TABLE `story`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID Truyện', AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chapter`
--
ALTER TABLE `chapter`
  ADD CONSTRAINT `FKoiqsft4egp7cxq41euj56hglu` FOREIGN KEY (`userPosted`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `chapter_story_sID_fk` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`),
  ADD CONSTRAINT `chapter_user_uID_fk` FOREIGN KEY (`userPosted`) REFERENCES `user` (`id`);

--
-- Các ràng buộc cho bảng `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK7k38tac8pkcf20qqy1g3g1q5w` FOREIGN KEY (`userPosted`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `comment_story_sID_fk` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`),
  ADD CONSTRAINT `comment_user_uID_fk` FOREIGN KEY (`userPosted`) REFERENCES `user` (`id`);

--
-- Các ràng buộc cho bảng `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `FKsa4pkhu41dl3hk88g1ibv82nh` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `ufavorites_chapter_chID_fk` FOREIGN KEY (`chapterId`) REFERENCES `chapter` (`id`),
  ADD CONSTRAINT `ufavorites_user_uID_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`);

--
-- Các ràng buộc cho bảng `pay`
--
ALTER TABLE `pay`
  ADD CONSTRAINT `chdeal_chapter_chID_fk` FOREIGN KEY (`chapterId`) REFERENCES `chapter` (`id`),
  ADD CONSTRAINT `chdeal_user_uID_fk` FOREIGN KEY (`userSend`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `pay_story_id_fk` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`),
  ADD CONSTRAINT `pay_user_uID_fk` FOREIGN KEY (`userReceived`) REFERENCES `user` (`id`);

--
-- Các ràng buộc cho bảng `story`
--
ALTER TABLE `story`
  ADD CONSTRAINT `FK6ya89n0d9vlpvws2c6aq5bu6t` FOREIGN KEY (`userPosted`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `story_user_uID_fk` FOREIGN KEY (`userPosted`) REFERENCES `user` (`id`);

--
-- Các ràng buộc cho bảng `story_category`
--
ALTER TABLE `story_category`
  ADD CONSTRAINT `_scategory_cateogry_cID_fk` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`),
  ADD CONSTRAINT `_scategory_story_sID_fk` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`);

--
-- Các ràng buộc cho bảng `user_rating`
--
ALTER TABLE `user_rating`
  ADD CONSTRAINT `FKl6uc4pj2bg6o8wmos0ihxuu4n` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `_srating_story_sID_fk` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`),
  ADD CONSTRAINT `_srating_user_uID_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`);

--
-- Các ràng buộc cho bảng `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FK6lxlgyqxdujskwtghdho3l5cn` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `_urole_role_rID_fk` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `_urole_user_uID_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
