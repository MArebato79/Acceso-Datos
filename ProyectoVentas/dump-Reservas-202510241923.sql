/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: Reservas
-- ------------------------------------------------------
-- Server version	11.5.2-MariaDB-ubu2404

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `Clientes`
--

DROP TABLE IF EXISTS `Clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Clientes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `apellidos` varchar(25) DEFAULT NULL,
  `telefonoContacto` char(9) DEFAULT NULL,
  `direccionHabitual` varchar(25) DEFAULT NULL,
  `direccionEnvio` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clientes`
--

LOCK TABLES `Clientes` WRITE;
/*!40000 ALTER TABLE `Clientes` DISABLE KEYS */;
INSERT INTO `Clientes` VALUES
(1,'12345678A','Carlos Pérez',NULL,'600123456','Calle Sol 12, Madrid','Calle Sol 12, Madrid'),
(2,'87654321B','Lucía Gómez',NULL,'600987654','Av. Luna 45, Barcelona','Av. Luna 45, Barcelona'),
(3,'11223344C','Ana Torres',NULL,'600112233','Calle Norte 8, Valencia','Calle Norte 20, Valencia');
/*!40000 ALTER TABLE `Clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Empresa`
--

DROP TABLE IF EXISTS `Empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Empresa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cif` char(9) NOT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `domicilio` varchar(25) DEFAULT NULL,
  `localidad` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_empresa` (`cif`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Empresa`
--

LOCK TABLES `Empresa` WRITE;
/*!40000 ALTER TABLE `Empresa` DISABLE KEYS */;
INSERT INTO `Empresa` VALUES
(1,'A12345678','TechWorld','Calle Sol 12','Madrid'),
(2,'B98765432','ElectroMax','Av. Luna 45','Barcelona'),
(3,'C11122233','GadgetPro','Calle Norte 8','Valencia');
/*!40000 ALTER TABLE `Empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Lineas_venta`
--

DROP TABLE IF EXISTS `Lineas_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Lineas_venta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idProducto` int(11) NOT NULL,
  `idVenta` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `descuento` int(11) DEFAULT 0,
  `precioVenta` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_lineas` (`idVenta`,`idProducto`),
  KEY `fk_linea_producto` (`idProducto`),
  CONSTRAINT `fk_linea_producto` FOREIGN KEY (`idProducto`) REFERENCES `Productos` (`id`),
  CONSTRAINT `fk_lineas_venta` FOREIGN KEY (`idVenta`) REFERENCES `Ventas` (`id`),
  CONSTRAINT `ck_cantidad` CHECK (`cantidad` > 0),
  CONSTRAINT `ck_descuento` CHECK (`descuento` >= 0),
  CONSTRAINT `ck_precioVenta` CHECK (`precioVenta` >= 0)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Lineas_venta`
--

LOCK TABLES `Lineas_venta` WRITE;
/*!40000 ALTER TABLE `Lineas_venta` DISABLE KEYS */;
INSERT INTO `Lineas_venta` VALUES
(1,1,1,1,0,NULL),
(2,2,1,2,0,NULL),
(3,3,2,1,0,NULL),
(4,4,3,1,0,NULL),
(5,2,3,1,0,NULL);
/*!40000 ALTER TABLE `Lineas_venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER recalcularVenta
AFTER DELETE
ON Lineas_venta FOR EACH ROW
BEGIN
	
	UPDATE Productos SET stock = stock + OLD.cantidad WHERE id = idProducto;
	UPDATE Ventas SET total=CALCULAR_TOTAL_LINEAS_VENTAS(idVenta) WHERE id = idVenta;
	
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Productos`
--

DROP TABLE IF EXISTS `Productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Productos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) DEFAULT NULL,
  `descripcion` varchar(50) DEFAULT NULL,
  `precioRecomendado` double DEFAULT 0,
  `stock` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `ck_stock` CHECK (`stock` > 0)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Productos`
--

LOCK TABLES `Productos` WRITE;
/*!40000 ALTER TABLE `Productos` DISABLE KEYS */;
INSERT INTO `Productos` VALUES
(1,'Portátil X1','Portátil 15\" con 16GB RAM',999.99,10),
(2,'Ratón Óptico','Ratón inalámbrico',25.5,50),
(3,'Teclado Mecánico','Teclado retroiluminado',79.99,20),
(4,'Monitor 24\"','Full HD IPS',149,15);
/*!40000 ALTER TABLE `Productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ventas`
--

DROP TABLE IF EXISTS `Ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Ventas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clienteId` int(11) DEFAULT NULL,
  `total` double DEFAULT 0,
  `fecha_venta` date DEFAULT curdate(),
  PRIMARY KEY (`id`),
  KEY `fk_venta_cliente` (`clienteId`),
  CONSTRAINT `fk_venta_cliente` FOREIGN KEY (`clienteId`) REFERENCES `Clientes` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ventas`
--

LOCK TABLES `Ventas` WRITE;
/*!40000 ALTER TABLE `Ventas` DISABLE KEYS */;
INSERT INTO `Ventas` VALUES
(1,1,1051.99,'2025-10-10'),
(2,2,79.99,'2025-10-11'),
(3,3,174.5,'2025-10-12');
/*!40000 ALTER TABLE `Ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER ASIGNAR_TOTAL
BEFORE INSERT
ON Ventas FOR EACH ROW
BEGIN
	
	SET NEW.total=CALCULAR_TOTAL_LINEAS_VENTA(NEW.id);
	
	IF NEW.total <= 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El total de la venta debe ser mayor a cero';
    END IF;
	
	SET NEW.fecha_venta = NOW(); 
	
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping routines for database 'Reservas'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP FUNCTION IF EXISTS `CALCULAR_TOTAL_LINEAS_VENTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `CALCULAR_TOTAL_LINEAS_VENTA`(id_Venta INT) RETURNS double
BEGIN
	DECLARE total_lineas DOUBLE;
	SELECT SUM(p.precioRecomendado*v.cantidad) 
	INTO total_lineas FROM Lineas_venta  v JOIN Productos p on idProducto=id WHERE idVenta = id_venta GROUP BY idVenta; 
	return total_lineas;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-10-24 19:23:27
