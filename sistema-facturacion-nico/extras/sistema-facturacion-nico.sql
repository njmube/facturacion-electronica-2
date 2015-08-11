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
-- Create schema sistema_facturacion_nico
--

CREATE DATABASE IF NOT EXISTS sistema_facturacion_nico;
USE sistema_facturacion_nico;

--
-- Definition of table `bancos`
--

DROP TABLE IF EXISTS `bancos`;
CREATE TABLE `bancos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bancos`
--

/*!40000 ALTER TABLE `bancos` DISABLE KEYS */;
INSERT INTO `bancos` (`ID`,`NOMBRE`) VALUES 
 (1,'ABN AMRO'),
 (2,'AMERICAN EXPRESS BANK'),
 (3,'BACS'),
 (4,'BANCO B.I. CREDITANSTALT'),
 (5,'BANCO BRADESCO'),
 (6,'BANCO CETELEM'),
 (7,'BANCO CMF'),
 (8,'BANCO COFIDIS'),
 (9,'BANCO COLUMBIA'),
 (10,'BANCO COMAFI'),
 (11,'BANCO CREDICOOP'),
 (12,'BANCO DE SERVICIOS Y TRANSACCIONES'),
 (13,'BANCO DE VALORES'),
 (14,'BANCO DEL SOL'),
 (15,'BANCO FINANSUR'),
 (16,'BANCO GALICIA'),
 (17,'BANCO INDUSTRIAL'),
 (18,'BANCO ITAÚ'),
 (19,'BANCO JULIO'),
 (20,'BANCO MACRO'),
 (21,'BANCO MARIVA'),
 (22,'BANCO MASVENTAS'),
 (23,'BANCO MERIDIAN'),
 (24,'BANCO PATAGONIA'),
 (25,'BANCO PIANO'),
 (26,'BANCO PRIVADO'),
 (27,'BANCO ROELA'),
 (28,'BANCO SAENZ'),
 (29,'BANCO SANTANDER RÍO'),
 (30,'BANCO SUPERVIELLE'),
 (31,'BANK OF AMERICA'),
 (32,'BANK OF TOKYO-MITSUBISHI UFJ'),
 (33,'BBVA BANCO FRANCÉS'),
 (34,'BNP PARIBAS'),
 (35,'CITIBANK'),
 (36,'DEUTSCHE BANK'),
 (37,'HSBC BANK'),
 (38,'JPMORGAN'),
 (39,'MBA LAZARD BANCO DE INVERSIONES'),
 (40,'RCI BANQU'),
 (41,'STANDARD BANK');
/*!40000 ALTER TABLE `bancos` ENABLE KEYS */;


--
-- Definition of table `cheques`
--

DROP TABLE IF EXISTS `cheques`;
CREATE TABLE `cheques` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NUMERO` varchar(30) NOT NULL,
  `FECHA_EMISION` date NOT NULL,
  `ID_BANCO` int(10) unsigned NOT NULL,
  `IMPORTE` decimal(12,2) NOT NULL,
  `A_NOMBRE_DE` varchar(50) NOT NULL,
  `FECHA_PAGO` date NOT NULL,
  `ID_CLIENTE` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_cheques_1` (`ID_BANCO`),
  KEY `FK_cheques_2` (`ID_CLIENTE`),
  CONSTRAINT `FK_cheques_1` FOREIGN KEY (`ID_BANCO`) REFERENCES `bancos` (`ID`),
  CONSTRAINT `FK_cheques_2` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



--
-- Definition of table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE `clientes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `RAZON_SOCIAL` varchar(50) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `TELEFONO` varchar(30) DEFAULT NULL,
  `CELULAR` varchar(30) DEFAULT NULL,
  `ID_LOCALIDAD` int(10) unsigned NOT NULL,
  `DOCUMENTO` varchar(25) NOT NULL,
  `DIRECCION` varchar(200) NOT NULL,
  `COND_IVA` varchar(50) NOT NULL,
  `TIPO_DOC` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_clientes_2` (`ID_LOCALIDAD`),
  KEY `FK_clientes_1` (`COND_IVA`) USING BTREE,
  CONSTRAINT `FK_clientes_2` FOREIGN KEY (`ID_LOCALIDAD`) REFERENCES `localidades` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `comprobantes`
--

DROP TABLE IF EXISTS `comprobantes`;
CREATE TABLE `comprobantes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `SUBTOTAL` decimal(12,2) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `IVA` decimal(12,2) NOT NULL,
  `PTO_VENTA` varchar(4) DEFAULT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `NUMERO_COMPROBANTE` varchar(10) DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL DEFAULT '0',
  `NUMERO_COMP_FORMATO` varchar(45) DEFAULT NULL,
  `COD_BARRAS` varchar(100) DEFAULT NULL,
  `TIPO` varchar(20) NOT NULL,
  `TIPO_LETRA` varchar(2) NOT NULL,
  `TRIBUTOS` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_comprobantes_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_comprobantes_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `comprobantes_asociados`
--

DROP TABLE IF EXISTS `comprobantes_asociados`;
CREATE TABLE `comprobantes_asociados` (
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  `ID_COMPROBANTE_ASOC` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_COMPROBANTE`,`ID_COMPROBANTE_ASOC`) USING BTREE,
  KEY `FK_notas_creditos_facturas_2` (`ID_COMPROBANTE_ASOC`) USING BTREE,
  CONSTRAINT `FK_notas_creditos_facturas_1` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`),
  CONSTRAINT `FK_notas_creditos_facturas_2` FOREIGN KEY (`ID_COMPROBANTE_ASOC`) REFERENCES `comprobantes` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `comprobantes_asociados`
--

/*!40000 ALTER TABLE `comprobantes_asociados` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprobantes_asociados` ENABLE KEYS */;


--
-- Definition of table `detalles_comprobantes`
--

DROP TABLE IF EXISTS `detalles_comprobantes`;
CREATE TABLE `detalles_comprobantes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned DEFAULT NULL,
  `PRECIO` decimal(12,2) NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  `DETALLE` varchar(100) DEFAULT NULL,
  `COMENTARIO` varchar(100) DEFAULT NULL,
  `ID_TIPO_IVA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_comp_1` (`ID_PRODUCTO`),
  KEY `FK_detalles_comp_2` (`ID_COMPROBANTE`),
  CONSTRAINT `FK_detalles_comp_1` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`),
  CONSTRAINT `FK_detalles_comp_2` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `detalles_notas_debitos`
--

DROP TABLE IF EXISTS `detalles_notas_debitos`;
CREATE TABLE `detalles_notas_debitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned DEFAULT NULL,
  `PRECIO` decimal(12,2) NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `ID_NOTA_DEBITO` int(10) unsigned NOT NULL,
  `DETALLE` varchar(100) DEFAULT NULL,
  `COMENTARIO` varchar(100) DEFAULT NULL,
  `ID_TIPO_IVA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_notas_debitos_1` (`ID_NOTA_DEBITO`),
  KEY `FK_detalles_notas_debitos_2` (`ID_PRODUCTO`),
  CONSTRAINT `FK_detalles_notas_debitos_1` FOREIGN KEY (`ID_NOTA_DEBITO`) REFERENCES `notas_debitos` (`ID`),
  CONSTRAINT `FK_detalles_notas_debitos_2` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;




--
-- Definition of table `detalles_remitos`
--

DROP TABLE IF EXISTS `detalles_remitos`;
CREATE TABLE `detalles_remitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `DETALLE` varchar(50) DEFAULT NULL,
  `ID_REMITO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_remitos_1` (`ID_REMITO`),
  KEY `FK_detalles_remitos_2` (`ID_PRODUCTO`),
  CONSTRAINT `FK_detalles_remitos_1` FOREIGN KEY (`ID_REMITO`) REFERENCES `remitos` (`ID`),
  CONSTRAINT `FK_detalles_remitos_2` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detalles_remitos`
--

/*!40000 ALTER TABLE `detalles_remitos` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalles_remitos` ENABLE KEYS */;


--
-- Definition of table `facturas`
--

DROP TABLE IF EXISTS `facturas`;
CREATE TABLE `facturas` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `SUBTOTAL` decimal(12,2) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `IVA` decimal(12,2) NOT NULL,
  `PTO_VENTA` varchar(4) DEFAULT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `NUMERO_COMPROBANTE` varchar(10) DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL DEFAULT '0',
  `NUMERO_COMP_FORMATO` varchar(45) DEFAULT NULL,
  `COD_BARRAS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_facturas_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_facturas_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `localidades`
--

DROP TABLE IF EXISTS `localidades`;
CREATE TABLE `localidades` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `localidades`
--

/*!40000 ALTER TABLE `localidades` DISABLE KEYS */;
INSERT INTO `localidades` (`ID`,`NOMBRE`) VALUES 
 (1,'25 DE MAYO'),
 (2,'9 DE JULIO'),
 (3,'ADOLFO ALSINA'),
 (4,'ADOLFO GONZALES CHAVES'),
 (5,'ALBERTI'),
 (6,'ALMIRANTE BROWN'),
 (7,'AVELLANEDA'),
 (8,'AYACUCHO'),
 (9,'AZUL'),
 (10,'BAHIA BLANCA'),
 (11,'BALCARCE'),
 (12,'BARADERO'),
 (13,'BARTOLOME MITRE'),
 (14,'BENITO JUAREZ'),
 (15,'BERAZATEGUI'),
 (16,'BERISSO'),
 (17,'BOLIVAR'),
 (18,'BRAGADO'),
 (19,'CAMPANA'),
 (20,'CAPITAN SARMIENTO'),
 (21,'CARLOS CASARES'),
 (22,'CARLOS TEJEDOR'),
 (23,'CARMEN DE ARECO'),
 (24,'CASTELLI'),
 (25,'CHACABUCO'),
 (26,'CHASCOMUS'),
 (27,'CHIVILCOY'),
 (28,'COLON'),
 (29,'CORONEL BRANDSEN'),
 (30,'CORONEL DORREGO'),
 (31,'CORONEL PRINGLES'),
 (32,'CORONEL ROSALES'),
 (33,'CORONEL SUAREZ'),
 (34,'DAIREAUX'),
 (35,'DE LA COSTA'),
 (36,'DOLORES'),
 (37,'ENSENADA'),
 (38,'ESCOBAR'),
 (39,'ESTEBAN ECHEVERRIA'),
 (40,'EXALTACION DE LA CRUZ'),
 (41,'EZEIZA'),
 (42,'FLORENCIO VARELA'),
 (43,'FLORENTINO AMEGHINO'),
 (44,'GENERAL ALVARADO'),
 (45,'GENERAL ALVEAR'),
 (46,'GENERAL ARENALES'),
 (47,'GENERAL BELGRANO'),
 (48,'GENERAL GUIDO'),
 (49,'GENERAL LAMADRID'),
 (50,'GENERAL LAS HERAS'),
 (51,'GENERAL LAVALLE'),
 (52,'GENERAL MADARIAGA'),
 (53,'GENERAL PAZ'),
 (54,'GENERAL PINTO'),
 (55,'GENERAL PUEYRREDON'),
 (56,'GENERAL RODRIGUEZ'),
 (57,'GENERAL SAN MARTIN'),
 (58,'GENERAL VIAMONTE'),
 (59,'GENERAL VILLEGAS'),
 (60,'GUAMINI'),
 (61,'HIPOLITO YRIGOYEN'),
 (62,'HURLINGHAM'),
 (63,'ITUZAINGO'),
 (64,'JOSE CLEMENTE PAZ'),
 (65,'JUNIN'),
 (66,'LA MATANZA'),
 (67,'LA PLATA'),
 (68,'LANUS'),
 (69,'LAPRIDA'),
 (70,'LAS FLORES'),
 (71,'LEANDRO N ALEM'),
 (72,'LINCOLN'),
 (73,'LOBERIA'),
 (74,'LOBOS'),
 (75,'LOMAS DE ZAMORA'),
 (76,'LUJAN'),
 (77,'MAGDALENA'),
 (78,'MAIPU'),
 (79,'MALVINAS ARGENTINAS'),
 (80,'MAR CHIQUITA'),
 (81,'MARCOS PAZ'),
 (82,'MERCEDES'),
 (83,'MERLO'),
 (84,'MONTE HERMOSO'),
 (85,'MONTE'),
 (86,'MORENO'),
 (87,'MORON'),
 (88,'NAVARRO'),
 (89,'NECOCHEA'),
 (90,'OLAVARRIA'),
 (91,'PATAGONES'),
 (92,'PEHUAJO'),
 (93,'PELLEGRINI'),
 (94,'PERGAMINO'),
 (95,'PILA'),
 (96,'PILAR'),
 (97,'PINAMAR'),
 (98,'PRESIDENTE PERON'),
 (99,'PUAN'),
 (100,'PUNTA INDIO'),
 (101,'QUILMES'),
 (102,'RAMALLO'),
 (103,'RAUCH'),
 (104,'RIVADAVIA'),
 (105,'ROJAS'),
 (106,'ROQUE PEREZ'),
 (107,'SAAVEDRA'),
 (108,'SALADILLO'),
 (109,'SALLIQUELO'),
 (110,'SALTO'),
 (111,'SAN ANDRES DE GILES'),
 (112,'SAN ANTONIO DE ARECO'),
 (113,'SAN CAYETANO'),
 (114,'SAN FERNANDO'),
 (115,'SAN ISIDRO'),
 (116,'SAN MIGUEL'),
 (117,'SAN NICOLAS'),
 (118,'SAN PEDRO'),
 (119,'SAN VICENTE'),
 (120,'SUIPACHA'),
 (121,'TANDIL'),
 (122,'TAPALQUE'),
 (123,'TIGRE'),
 (124,'TORDILLO'),
 (125,'TORNQUIST'),
 (126,'TRENQUE LAUQUEN'),
 (127,'TRES ARROYOS'),
 (128,'TRES DE FEBRERO'),
 (129,'TRES LOMAS'),
 (130,'VICENTE LOPEZ'),
 (131,'VILLA GESELL'),
 (132,'VILLARINO'),
 (133,'ZARATE');
/*!40000 ALTER TABLE `localidades` ENABLE KEYS */;


--
-- Definition of table `notas_creditos`
--

DROP TABLE IF EXISTS `notas_creditos`;
CREATE TABLE `notas_creditos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `SUBTOTAL` decimal(12,2) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `IVA` decimal(12,2) NOT NULL,
  `PTO_VENTA` varchar(4) DEFAULT NULL,
  `NUMERO_COMPROBANTE` varchar(10) DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL DEFAULT '0',
  `NUMERO_COMP_FORMATO` varchar(45) DEFAULT NULL,
  `COD_BARRAS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_notas_creditos_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_notas_creditos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `notas_debitos`
--

DROP TABLE IF EXISTS `notas_debitos`;
CREATE TABLE `notas_debitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `SUBTOTAL` decimal(12,2) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `IVA` decimal(12,2) NOT NULL,
  `PTO_VENTA` varchar(4) DEFAULT NULL,
  `NUMERO_COMPROBANTE` varchar(10) DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL,
  `NUMERO_COMP_FORMATO` varchar(45) DEFAULT NULL,
  `COD_BARRAS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_notas_debitos_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_notas_debitos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



--
-- Definition of table `notas_debitos_facturas`
--

DROP TABLE IF EXISTS `notas_debitos_facturas`;
CREATE TABLE `notas_debitos_facturas` (
  `ID_NOTA_DEBITO` int(10) unsigned NOT NULL,
  `ID_FACTURA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_NOTA_DEBITO`,`ID_FACTURA`),
  KEY `FK_notas_debitos_facturas_2` (`ID_FACTURA`),
  CONSTRAINT `FK_notas_debitos_facturas_1` FOREIGN KEY (`ID_NOTA_DEBITO`) REFERENCES `notas_debitos` (`ID`),
  CONSTRAINT `FK_notas_debitos_facturas_2` FOREIGN KEY (`ID_FACTURA`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notas_debitos_facturas`
--

/*!40000 ALTER TABLE `notas_debitos_facturas` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas_debitos_facturas` ENABLE KEYS */;


--
-- Definition of table `pagos_recibos_cheques`
--

DROP TABLE IF EXISTS `pagos_recibos_cheques`;
CREATE TABLE `pagos_recibos_cheques` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_CHEQUE` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_cheques_1` (`ID_RECIBO`),
  KEY `FK_pagos_recibos_cheques_2` (`ID_CHEQUE`),
  CONSTRAINT `FK_pagos_recibos_cheques_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  CONSTRAINT `FK_pagos_recibos_cheques_2` FOREIGN KEY (`ID_CHEQUE`) REFERENCES `cheques` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pagos_recibos_cheques`
--

/*!40000 ALTER TABLE `pagos_recibos_cheques` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagos_recibos_cheques` ENABLE KEYS */;


--
-- Definition of table `pagos_recibos_efectivo`
--

DROP TABLE IF EXISTS `pagos_recibos_efectivo`;
CREATE TABLE `pagos_recibos_efectivo` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_efectivo_1` (`ID_RECIBO`),
  CONSTRAINT `FK_pagos_recibos_efectivo_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `parametros`
--

DROP TABLE IF EXISTS `parametros`;
CREATE TABLE `parametros` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NRO_RECIBO` varchar(45) NOT NULL,
  `NRO_REMITO` varchar(45) NOT NULL,
  `PREF_REMITO` varchar(45) NOT NULL,
  `PREF_RECIBO` varchar(45) NOT NULL,
  `NRO_FACTURA_A` varchar(45) NOT NULL,
  `NRO_NOTA_CREDITO_A` varchar(45) NOT NULL,
  `NRO_NOTA_DEBITO_A` varchar(45) NOT NULL,
  `NRO_FACTURA_B` varchar(45) NOT NULL,
  `NRO_NOTA_CREDITO_B` varchar(45) NOT NULL,
  `NRO_NOTA_DEBITO_B` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parametros`
--

/*!40000 ALTER TABLE `parametros` DISABLE KEYS */;
INSERT INTO `parametros` (`ID`,`NRO_RECIBO`,`NRO_REMITO`,`PREF_REMITO`,`PREF_RECIBO`,`NRO_FACTURA_A`,`NRO_NOTA_CREDITO_A`,`NRO_NOTA_DEBITO_A`,`NRO_FACTURA_B`,`NRO_NOTA_CREDITO_B`,`NRO_NOTA_DEBITO_B`) VALUES 
 (1,'7','20','0001','0001','57','8','5','7','1','1');
/*!40000 ALTER TABLE `parametros` ENABLE KEYS */;


--
-- Definition of table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
CREATE TABLE `permisos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CODIGO` varchar(20) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `permisos`
--

/*!40000 ALTER TABLE `permisos` DISABLE KEYS */;
/*!40000 ALTER TABLE `permisos` ENABLE KEYS */;


--
-- Definition of table `permisos_usuarios`
--

DROP TABLE IF EXISTS `permisos_usuarios`;
CREATE TABLE `permisos_usuarios` (
  `ID_USUARIO` int(10) unsigned NOT NULL,
  `ID_PERMISO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_USUARIO`,`ID_PERMISO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `permisos_usuarios`
--

/*!40000 ALTER TABLE `permisos_usuarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `permisos_usuarios` ENABLE KEYS */;


--
-- Definition of table `productos`
--

DROP TABLE IF EXISTS `productos`;
CREATE TABLE `productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` varchar(50) NOT NULL,
  `CODIGO` varchar(20) NOT NULL,
  `COSTO` decimal(12,2) NOT NULL,
  `PRECIO` decimal(12,2) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `UBICACION` varchar(45) DEFAULT NULL,
  `ID_TIPO` int(10) unsigned NOT NULL,
  `ID_TIPO_IVA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_productos_1` (`ID_TIPO`),
  CONSTRAINT `FK_productos_1` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipos_productos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



--
-- Definition of table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
CREATE TABLE `proveedores` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `RAZON_SOCIAL` varchar(50) NOT NULL,
  `CUIT` varchar(45) NOT NULL,
  `DIRECCION` varchar(100) DEFAULT NULL,
  `TELEFONO` varchar(45) DEFAULT NULL,
  `ID_LOCALIDAD` int(10) unsigned NOT NULL,
  `MAIL` varchar(100) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_proveedores_1` (`ID_LOCALIDAD`),
  CONSTRAINT `FK_proveedores_1` FOREIGN KEY (`ID_LOCALIDAD`) REFERENCES `localidades` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `recibos`
--

DROP TABLE IF EXISTS `recibos`;
CREATE TABLE `recibos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FECHA` date NOT NULL,
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `NUMERO` varchar(45) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_recibos_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_recibos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



--
-- Definition of table `recibos_comprobantes`
--

DROP TABLE IF EXISTS `recibos_comprobantes`;
CREATE TABLE `recibos_comprobantes` (
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_RECIBO`,`ID_COMPROBANTE`) USING BTREE,
  KEY `FK_recibos_comprobantes_2` (`ID_COMPROBANTE`),
  CONSTRAINT `FK_recibos_comprobantes_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  CONSTRAINT `FK_recibos_comprobantes_2` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



--
-- Definition of table `recibos_notas_debito`
--

DROP TABLE IF EXISTS `recibos_notas_debito`;
CREATE TABLE `recibos_notas_debito` (
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_NOTA_DEBITO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_RECIBO`,`ID_NOTA_DEBITO`),
  KEY `FK_recibos_notas_debito_2` (`ID_NOTA_DEBITO`),
  CONSTRAINT `FK_recibos_notas_debito_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  CONSTRAINT `FK_recibos_notas_debito_2` FOREIGN KEY (`ID_NOTA_DEBITO`) REFERENCES `notas_debitos` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `recibos_notas_debito`
--

/*!40000 ALTER TABLE `recibos_notas_debito` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibos_notas_debito` ENABLE KEYS */;


--
-- Definition of table `remitos`
--

DROP TABLE IF EXISTS `remitos`;
CREATE TABLE `remitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `FECHA` date NOT NULL,
  `NUMERO` varchar(45) NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned DEFAULT NULL,
  `OBSERVACIONES` varchar(200) DEFAULT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_remitos_1` (`ID_CLIENTE`),
  KEY `FK_remitos_2` (`ID_COMPROBANTE`) USING BTREE,
  CONSTRAINT `FK_remitos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`),
  CONSTRAINT `FK_remitos_2` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `remitos`
--

/*!40000 ALTER TABLE `remitos` DISABLE KEYS */;
/*!40000 ALTER TABLE `remitos` ENABLE KEYS */;


--
-- Definition of table `stocks_productos`
--

DROP TABLE IF EXISTS `stocks_productos`;
CREATE TABLE `stocks_productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned NOT NULL,
  `ID_PROVEEDOR` int(10) unsigned NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `FACTURA` varchar(50) DEFAULT NULL,
  `FECHA` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_stocks_producto_1` (`ID_PRODUCTO`),
  KEY `FK_stocks_producto_2` (`ID_PROVEEDOR`),
  CONSTRAINT `FK_stocks_producto_1` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`),
  CONSTRAINT `FK_stocks_producto_2` FOREIGN KEY (`ID_PROVEEDOR`) REFERENCES `proveedores` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stocks_productos`
--

/*!40000 ALTER TABLE `stocks_productos` DISABLE KEYS */;
/*!40000 ALTER TABLE `stocks_productos` ENABLE KEYS */;


--
-- Definition of table `tipos_productos`
--

DROP TABLE IF EXISTS `tipos_productos`;
CREATE TABLE `tipos_productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Definition of table `tributos_comprobantes`
--

DROP TABLE IF EXISTS `tributos_comprobantes`;
CREATE TABLE `tributos_comprobantes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DETALLE` varchar(100) NOT NULL,
  `ALICUOTA` decimal(10,2) NOT NULL,
  `IMPORTE` decimal(10,2) NOT NULL,
  `BASE_IMPONIBLE` decimal(10,2) NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  `TIPO_TRIBUTO` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_tributos_comprobantes_1` (`ID_COMPROBANTE`),
  KEY `FK_tributos_comprobantes_2` (`TIPO_TRIBUTO`) USING BTREE,
  CONSTRAINT `FK_tributos_comprobantes_1` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



--
-- Definition of table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DNI` varchar(20) NOT NULL,
  `PASSWORD` varchar(20) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usuarios`
--

/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`ID`,`DNI`,`PASSWORD`,`ACTIVO`,`NOMBRE`) VALUES 
 (1,'1','uboEp3KLIIc=',1,'JUAN MANUEL VAZQUEZ');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
