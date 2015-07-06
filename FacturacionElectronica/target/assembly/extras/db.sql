-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.41-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema classlife
--

CREATE DATABASE IF NOT EXISTS classlife;
USE classlife;

--
-- Definition of table `articulos`
--

DROP TABLE IF EXISTS `articulos`;
CREATE TABLE `articulos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  `CODIGO_BARRAS` varchar(30) NOT NULL,
  `COLOR` varchar(20) NOT NULL,
  `TALLE` varchar(5) NOT NULL,
  `PRECIO_PUB` decimal(10,2) NOT NULL,
  `PORCENTAJE` decimal(10,2) NOT NULL,
  `STOCK` int(10) unsigned NOT NULL,
  `USUARIO_ACT` varchar(50) NOT NULL,
  `FECHA_ACT` datetime NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL,
  `COD_ARTICULO` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `articulos`
--

/*!40000 ALTER TABLE `articulos` DISABLE KEYS */;
/*!40000 ALTER TABLE `articulos` ENABLE KEYS */;


--
-- Definition of table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE `clientes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE_COMPLETO` varchar(50) NOT NULL,
  `TELEFONO` varchar(20) DEFAULT NULL,
  `DIRECCION` varchar(40) DEFAULT NULL,
  `ACTIVO` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clientes`
--

/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;


--
-- Definition of table `detalles_ventas`
--

DROP TABLE IF EXISTS `detalles_ventas`;
CREATE TABLE `detalles_ventas` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `PRECIO_UNITARIO` decimal(10,2) NOT NULL,
  `ID_VENTA` int(10) unsigned NOT NULL,
  `PORCENTAJE` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_ventas_1` (`ID_VENTA`),
  KEY `FK_detalles_ventas_2` (`ID_ARTICULO`),
  CONSTRAINT `FK_detalles_ventas_1` FOREIGN KEY (`ID_VENTA`) REFERENCES `ventas` (`ID`),
  CONSTRAINT `FK_detalles_ventas_2` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detalles_ventas`
--

/*!40000 ALTER TABLE `detalles_ventas` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalles_ventas` ENABLE KEYS */;


--
-- Definition of table `movimientos`
--

DROP TABLE IF EXISTS `movimientos`;
CREATE TABLE `movimientos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FECHA` datetime NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  `TIPO_MOVIMIENTO` varchar(20) NOT NULL,
  `ID_CLIENTE` int(10) unsigned DEFAULT NULL,
  `USUARIO` varchar(45) NOT NULL,
  `SALDO_INICIAL` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_MOVIMIENTOS_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_movimientos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `movimientos`
--

/*!40000 ALTER TABLE `movimientos` DISABLE KEYS */;
/*!40000 ALTER TABLE `movimientos` ENABLE KEYS */;


--
-- Definition of table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
CREATE TABLE `permisos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PERMISO` varchar(10) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `permisos`
--

/*!40000 ALTER TABLE `permisos` DISABLE KEYS */;
INSERT INTO `permisos` (`ID`,`PERMISO`,`DESCRIPCION`) VALUES 
 (1,'ING-ART','Ingreso a la Adm. de Artículos'),
 (2,'NUE-ART','Nuevo Artículo'),
 (3,'EDI-ART','Editar Artículo'),
 (4,'ELI-ART','Eliminar Artículo'),
 (5,'CAR-ART','Carga Stock Artículo'),
 (6,'ING-CLI','Ingreso a la Adm. de Clientes'),
 (7,'NUE-CLI','Nuevo Cliente'),
 (8,'EDI-CLI','Editar Cliente'),
 (9,'ELI-CLI','Eliminar Cliente'),
 (10,'ING-CTE','Ingreso Cta. Cte.'),
 (11,'NUE-CTE','Nuevo Movimiento Cta. Cte.'),
 (12,'EDI-CTE','Editar Movimiento Cta. Cte.'),
 (13,'ELI-CTE','Eliminar Movimiento Cta. Cte.'),
 (14,'ING-MOV','Ingreso a la Adm. de Movimientos'),
 (15,'NUE-MOV','Nuevo Movimiento'),
 (16,'EDI-MOV','Editar Movimiento'),
 (17,'ELI-MOV','Eliminar Movimiento'),
 (18,'ING-USU','Ingreso a la Adm. de Usuarios'),
 (19,'NUE-USU','Nuevo Usuario'),
 (20,'EDI-USU','Editar Usuario'),
 (21,'ELI-USU','Eliminar Usuario'),
 (22,'ING-VTA','Ingreso a la Adm. de Ventas'),
 (23,'NUE-VTA','Nueva Venta'),
 (24,'EDI-VTA','Editar Venta'),
 (25,'ELI-VTA','Eliminar Venta'),
 (26,'REP-STO','Reporte de Stock'),
 (27,'REP-CTA','Reporte de Cta. Cte.'),
 (28,'REP-CAJ','Reporte de Caja Diaria'),
 (29,'REP-MOV','Reporte de Movimientos');
/*!40000 ALTER TABLE `permisos` ENABLE KEYS */;


--
-- Definition of table `permisos_usuarios`
--

DROP TABLE IF EXISTS `permisos_usuarios`;
CREATE TABLE `permisos_usuarios` (
  `ID_USUARIO` int(10) unsigned NOT NULL,
  `ID_PERMISO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_USUARIO`,`ID_PERMISO`),
  KEY `FK_permisos_usuarios_2` (`ID_PERMISO`),
  CONSTRAINT `FK_permisos_usuarios_1` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuarios` (`ID`),
  CONSTRAINT `FK_permisos_usuarios_2` FOREIGN KEY (`ID_PERMISO`) REFERENCES `permisos` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `permisos_usuarios`
--

/*!40000 ALTER TABLE `permisos_usuarios` DISABLE KEYS */;
INSERT INTO `permisos_usuarios` (`ID_USUARIO`,`ID_PERMISO`) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,4),
 (1,5),
 (1,6),
 (1,7),
 (1,8),
 (1,9),
 (1,10),
 (1,11),
 (1,12),
 (1,13),
 (1,14),
 (1,15),
 (1,16),
 (1,17),
 (1,18),
 (1,19),
 (1,20),
 (1,21),
 (1,22),
 (1,23),
 (1,24),
 (1,25),
 (1,26),
 (1,27),
 (1,28),
 (1,29);
/*!40000 ALTER TABLE `permisos_usuarios` ENABLE KEYS */;


--
-- Definition of table `tipos_pagos`
--

DROP TABLE IF EXISTS `tipos_pagos`;
CREATE TABLE `tipos_pagos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TIPO_PAGO` varchar(20) NOT NULL,
  `MONTO` decimal(10,2) NOT NULL,
  `ID_MOVIMIENTO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_tipos_pagos_1` (`ID_MOVIMIENTO`),
  CONSTRAINT `FK_tipos_pagos_1` FOREIGN KEY (`ID_MOVIMIENTO`) REFERENCES `movimientos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tipos_pagos`
--

/*!40000 ALTER TABLE `tipos_pagos` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipos_pagos` ENABLE KEYS */;


--
-- Definition of table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DNI` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `NOMBRE` varchar(45) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usuarios`
--

/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`ID`,`DNI`,`PASSWORD`,`NOMBRE`,`ACTIVO`) VALUES 
 (1,'1','uboEp3KLIIc=','JUAN MANUEL VAZQUEZ',1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;


--
-- Definition of table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
CREATE TABLE `ventas` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_MOVIMIENTO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ventas_2` (`ID_MOVIMIENTO`),
  CONSTRAINT `FK_ventas_2` FOREIGN KEY (`ID_MOVIMIENTO`) REFERENCES `movimientos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ventas`
--

/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
