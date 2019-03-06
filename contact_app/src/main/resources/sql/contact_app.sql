-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3307
-- Généré le :  mer. 06 mars 2019 à 19:41
-- Version du serveur :  10.2.14-MariaDB
-- Version de PHP :  5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `contact_app`
--

-- --------------------------------------------------------

--
-- Structure de la table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `category`
--

INSERT INTO `category` (`id`, `name`) VALUES
(1, 'Sans catégorie'),
(2, 'Amis'),
(3, 'Pro');

-- --------------------------------------------------------

--
-- Structure de la table `contact`
--

DROP TABLE IF EXISTS `contact`;
CREATE TABLE IF NOT EXISTS `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lastname` varchar(40) NOT NULL,
  `firstname` varchar(40) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `id_category` int(11) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `notes` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `contact`
--

INSERT INTO `contact` (`id`, `lastname`, `firstname`, `nickname`, `phone`, `id_category`, `email`, `address`, `birthday`, `notes`) VALUES
(1, 'Bomy', 'Clara', 'Clawawa', '0642398475', 2, 'clara.bomy@isen.yncrea.fr', 'Loos', '1997-09-13', 'aime les IA'),
(2, 'Juzeau', 'Thibaut', 'Thichef', '0623405698', 2, 'thibaut.juzeau@isen.yncrea.fr', 'Lille', '1998-08-31', NULL),
(3, 'Christiaens', 'Mathilde', NULL, '0943582113', 1, 'mathilde.christiaens@isen.yncrea.fr', 'LaMadeleine', NULL, 'chat addict'),
(4, 'Bomy', 'Corinne', NULL, '0432129467', 2, NULL, NULL, NULL, NULL),
(5, 'Jarosset', 'Corentin', NULL, '0723435465', 3, 'coco_du_59@kikoo.fr', NULL, NULL, 'where is corentin?');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
