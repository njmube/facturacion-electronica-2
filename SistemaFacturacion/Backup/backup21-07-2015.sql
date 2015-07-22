-- MySQL dump 10.13  Distrib 5.1.41, for Win32 (ia32)
--
-- Host: localhost    Database: sistema_facturacion
-- ------------------------------------------------------
-- Server version	5.1.41-community

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

--
-- Table structure for table `bancos`
--

DROP TABLE IF EXISTS `bancos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bancos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bancos`
--

LOCK TABLES `bancos` WRITE;
/*!40000 ALTER TABLE `bancos` DISABLE KEYS */;
INSERT INTO `bancos` VALUES (1,'ABN AMRO'),(2,'AMERICAN EXPRESS BANK'),(3,'BACS'),(4,'BANCO B.I. CREDITANSTALT'),(5,'BANCO BRADESCO'),(6,'BANCO CETELEM'),(7,'BANCO CMF'),(8,'BANCO COFIDIS'),(9,'BANCO COLUMBIA'),(10,'BANCO COMAFI'),(11,'BANCO CREDICOOP'),(12,'BANCO DE SERVICIOS Y TRANSACCIONES'),(13,'BANCO DE VALORES'),(14,'BANCO DEL SOL'),(15,'BANCO FINANSUR'),(16,'BANCO GALICIA'),(17,'BANCO INDUSTRIAL'),(18,'BANCO ITAÚ'),(19,'BANCO JULIO'),(20,'BANCO MACRO'),(21,'BANCO MARIVA'),(22,'BANCO MASVENTAS'),(23,'BANCO MERIDIAN'),(24,'BANCO PATAGONIA'),(25,'BANCO PIANO'),(26,'BANCO PRIVADO'),(27,'BANCO ROELA'),(28,'BANCO SAENZ'),(29,'BANCO SANTANDER RÍO'),(30,'BANCO SUPERVIELLE'),(31,'BANK OF AMERICA'),(32,'BANK OF TOKYO-MITSUBISHI UFJ'),(33,'BBVA BANCO FRANCÉS'),(34,'BNP PARIBAS'),(35,'CITIBANK'),(36,'DEUTSCHE BANK'),(37,'HSBC BANK'),(38,'JPMORGAN'),(39,'MBA LAZARD BANCO DE INVERSIONES'),(40,'RCI BANQU'),(41,'STANDARD BANK');
/*!40000 ALTER TABLE `bancos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cheques`
--

DROP TABLE IF EXISTS `cheques`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cheques` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NUMERO` varchar(45) NOT NULL,
  `FECHA_EMISION` date NOT NULL,
  `ID_BANCO` int(10) unsigned NOT NULL,
  `IMPORTE` decimal(10,2) NOT NULL,
  `RECIBIDO_DE` varchar(50) NOT NULL,
  `FECHA_PAGO` date NOT NULL,
  `ID_ENTREGA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_cheques_1` (`ID_BANCO`),
  CONSTRAINT `FK_cheques_1` FOREIGN KEY (`ID_BANCO`) REFERENCES `bancos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cheques`
--

LOCK TABLES `cheques` WRITE;
/*!40000 ALTER TABLE `cheques` DISABLE KEYS */;
INSERT INTO `cheques` VALUES (10,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(11,'456','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(12,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(13,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(15,'1234567890','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(16,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(17,'456','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(18,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(19,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(21,'1234567890','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(22,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(23,'456','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(24,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(25,'123','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(27,'1234567890','2015-07-04',19,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(28,'123','2015-07-04',20,'10.00','JUAN MANUEL VAZQUEZ','2015-07-04',0),(29,'0303456','2015-07-10',29,'15000.00','JORGE LOPEZ','2015-09-19',0);
/*!40000 ALTER TABLE `cheques` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `RAZON_SOCIAL` varchar(45) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `TELEFONO` varchar(30) DEFAULT NULL,
  `CELULAR` varchar(30) DEFAULT NULL,
  `ID_LOCALIDAD` int(10) unsigned NOT NULL,
  `CUIT` varchar(25) NOT NULL,
  `DIRECCION` varchar(200) NOT NULL,
  `ID_COND_IVA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_clientes_1` (`ID_COND_IVA`),
  KEY `FK_clientes_2` (`ID_LOCALIDAD`),
  CONSTRAINT `FK_clientes_1` FOREIGN KEY (`ID_COND_IVA`) REFERENCES `condiciones_iva` (`ID`),
  CONSTRAINT `FK_clientes_2` FOREIGN KEY (`ID_LOCALIDAD`) REFERENCES `localidades` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (6,'VAZQUEZ HERMANOS',1,'02474-435566','02474-15-123456',110,'30-71214423-4','SAN MARTIN 245',1);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condiciones_iva`
--

DROP TABLE IF EXISTS `condiciones_iva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `condiciones_iva` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DENOMINACION` varchar(50) NOT NULL,
  `ABREV` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condiciones_iva`
--

LOCK TABLES `condiciones_iva` WRITE;
/*!40000 ALTER TABLE `condiciones_iva` DISABLE KEYS */;
INSERT INTO `condiciones_iva` VALUES (1,'RESPONSABLE INSCRIPTO','RESP. INSC.'),(2,'RESPONSABLE NO INSCRIPTO','RESP. NO INSC.');
/*!40000 ALTER TABLE `condiciones_iva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_facturas`
--

DROP TABLE IF EXISTS `detalles_facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalles_facturas` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned DEFAULT NULL,
  `PRECIO` decimal(10,2) NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `PORC_IVA` decimal(10,2) NOT NULL,
  `ID_FACTURA` int(10) unsigned NOT NULL,
  `DETALLE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_facturas_1` (`ID_PRODUCTO`),
  KEY `FK_detalles_facturas_2` (`ID_FACTURA`),
  CONSTRAINT `FK_detalles_facturas_1` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`),
  CONSTRAINT `FK_detalles_facturas_2` FOREIGN KEY (`ID_FACTURA`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_facturas`
--

LOCK TABLES `detalles_facturas` WRITE;
/*!40000 ALTER TABLE `detalles_facturas` DISABLE KEYS */;
INSERT INTO `detalles_facturas` VALUES (92,4,'15105.00',1,'21.00',50,NULL),(93,3,'170.00',1,'21.00',50,NULL),(94,4,'15105.00',1,'21.00',51,NULL),(95,1,'550.00',1,'21.00',51,NULL),(96,3,'170.00',1,'21.00',52,NULL),(97,5,'1000.00',1,'21.00',53,NULL),(98,3,'170.00',1,'21.00',54,NULL),(99,3,'170.00',1,'21.00',55,NULL),(100,5,'1000.00',1,'21.00',56,NULL),(101,1,'550.00',1,'21.00',57,NULL),(102,4,'15105.00',1,'21.00',58,NULL),(103,1,'550.00',1,'21.00',59,NULL),(104,5,'1000.00',1,'21.00',59,NULL),(105,4,'15105.00',1,'21.00',60,NULL),(106,4,'15105.00',1,'21.00',61,NULL),(107,3,'170.00',1,'21.00',61,NULL),(108,1,'550.00',1,'21.00',61,NULL),(109,5,'1000.00',1,'21.00',61,NULL),(110,3,'170.00',1,'21.00',62,NULL),(111,6,'200.00',1,'10.50',63,NULL),(112,4,'15105.00',1,'21.00',64,NULL),(113,4,'15105.00',1,'21.00',65,NULL),(114,3,'170.00',1,'21.00',66,NULL),(115,4,'15105.00',1,'21.00',66,NULL),(116,2,'450.00',1,'10.50',67,NULL),(117,2,'450.00',1,'10.50',68,NULL),(118,5,'1000.00',1,'21.00',69,NULL),(119,4,'15105.00',1,'21.00',70,NULL),(120,4,'15105.00',1,'21.00',71,NULL),(121,2,'450.00',1,'10.50',72,NULL),(122,4,'15105.00',1,'21.00',73,NULL),(123,4,'15105.00',1,'21.00',74,NULL),(124,3,'170.00',1,'21.00',75,NULL),(125,3,'170.00',1,'21.00',76,NULL),(126,2,'450.00',1,'10.50',77,NULL),(127,4,'15105.00',1,'21.00',78,NULL),(128,5,'1000.00',1,'21.00',79,NULL),(129,1,'550.00',1,'21.00',80,NULL),(130,2,'450.00',1,'10.50',81,NULL),(131,5,'1000.00',1,'21.00',82,NULL),(132,4,'15105.00',1,'21.00',83,NULL),(133,6,'200.00',1,'10.50',84,NULL),(134,5,'1000.00',1,'21.00',84,NULL),(135,2,'450.00',1,'10.50',85,NULL),(136,5,'1000.00',2,'21.00',86,NULL),(137,4,'15105.00',1,'21.00',87,NULL),(138,5,'1000.00',1,'21.00',88,NULL);
/*!40000 ALTER TABLE `detalles_facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_notas_creditos`
--

DROP TABLE IF EXISTS `detalles_notas_creditos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalles_notas_creditos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned DEFAULT NULL,
  `PRECIO` decimal(10,2) NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `PORC_IVA` decimal(10,2) NOT NULL,
  `ID_NOTA_CREDITO` int(10) unsigned NOT NULL,
  `COMENTARIO` varchar(100) DEFAULT NULL,
  `DETALLE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_notas_creditos_1` (`ID_PRODUCTO`),
  KEY `FK_detalles_notas_creditos_2` (`ID_NOTA_CREDITO`),
  CONSTRAINT `FK_detalles_notas_creditos_1` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`),
  CONSTRAINT `FK_detalles_notas_creditos_2` FOREIGN KEY (`ID_NOTA_CREDITO`) REFERENCES `notas_creditos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_notas_creditos`
--

LOCK TABLES `detalles_notas_creditos` WRITE;
/*!40000 ALTER TABLE `detalles_notas_creditos` DISABLE KEYS */;
INSERT INTO `detalles_notas_creditos` VALUES (15,4,'15105.00',1,'21.00',9,'SARAZA',NULL),(16,3,'170.00',1,'21.00',10,NULL,NULL),(17,5,'1000.00',1,'21.00',11,NULL,NULL);
/*!40000 ALTER TABLE `detalles_notas_creditos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_notas_debitos`
--

DROP TABLE IF EXISTS `detalles_notas_debitos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalles_notas_debitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned DEFAULT NULL,
  `PRECIO` decimal(10,2) NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `PORC_IVA` decimal(10,2) NOT NULL,
  `ID_NOTA_DEBITO` int(10) unsigned NOT NULL,
  `DETALLE` varchar(100) DEFAULT NULL,
  `COMENTARIO` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_notas_debitos_1` (`ID_NOTA_DEBITO`),
  KEY `FK_detalles_notas_debitos_2` (`ID_PRODUCTO`),
  CONSTRAINT `FK_detalles_notas_debitos_1` FOREIGN KEY (`ID_NOTA_DEBITO`) REFERENCES `notas_debitos` (`ID`),
  CONSTRAINT `FK_detalles_notas_debitos_2` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_notas_debitos`
--

LOCK TABLES `detalles_notas_debitos` WRITE;
/*!40000 ALTER TABLE `detalles_notas_debitos` DISABLE KEYS */;
INSERT INTO `detalles_notas_debitos` VALUES (3,2,'450.00',1,'10.50',2,NULL,NULL),(4,5,'1000.00',1,'21.00',3,NULL,NULL);
/*!40000 ALTER TABLE `detalles_notas_debitos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_remitos`
--

DROP TABLE IF EXISTS `detalles_remitos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_remitos`
--

LOCK TABLES `detalles_remitos` WRITE;
/*!40000 ALTER TABLE `detalles_remitos` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalles_remitos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facturas`
--

DROP TABLE IF EXISTS `facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `facturas` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `SUBTOTAL` decimal(10,2) NOT NULL,
  `TOTAL` decimal(10,2) NOT NULL,
  `IVA` decimal(10,2) NOT NULL,
  `PTO_VENTA` int(10) unsigned DEFAULT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `NUMERO_COMPROBANTE` int(10) unsigned DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_facturas_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_facturas_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facturas`
--

LOCK TABLES `facturas` WRITE;
/*!40000 ALTER TABLE `facturas` DISABLE KEYS */;
INSERT INTO `facturas` VALUES (50,'65291276607227','2015-07-30','2015-07-20',1,6,'15275.00','18482.75','3207.75',13,'',6,1),(51,'65291276607227','2015-07-30','2015-07-20',1,6,'15655.00','18942.55','3287.55',13,'',6,1),(52,'65291276607243','2015-07-30','2015-07-20',1,6,'170.00','205.70','35.70',13,'',7,1),(53,'65291276607303','2015-07-30','2015-07-20',1,6,'1000.00','1210.00','210.00',13,'',8,1),(54,'65291276607332','2015-07-30','2015-07-20',1,6,'170.00','205.70','35.70',13,'',9,1),(55,'65291276607578','2015-07-30','2015-07-20',1,6,'170.00','205.70','35.70',13,'',10,1),(56,'65291276607594','2015-07-30','2015-07-20',1,6,'1000.00','1210.00','210.00',13,'',11,1),(57,'65291276607727','2015-07-30','2015-07-20',1,6,'550.00','665.50','115.50',13,'',12,1),(58,'65291276607730','2015-07-30','2015-07-20',1,6,'15105.00','18277.05','3172.05',13,'',13,1),(59,'65291276607730','2015-07-30','2015-07-20',1,6,'1550.00','1875.50','325.50',13,'',13,1),(60,'65291276655885','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',14,1),(61,'65291276656399','2015-07-31','2015-07-21',1,6,'16825.00','20358.25','3533.25',13,'',15,1),(62,'65291276656417','2015-07-31','2015-07-21',1,6,'170.00','205.70','35.70',13,'',16,1),(63,'65291276656433','2015-07-31','2015-07-21',1,6,'200.00','221.00','21.00',13,'',17,1),(64,'65291276656768','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',18,1),(65,'65291276690926','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',19,1),(66,'65291276690942','2015-07-31','2015-07-21',1,6,'15275.00','18482.75','3207.75',13,'',20,1),(67,'65291276690968','2015-07-31','2015-07-21',1,6,'450.00','497.25','47.25',13,'',21,1),(68,'65291276690971','2015-07-31','2015-07-21',1,6,'450.00','497.25','47.25',13,'',22,1),(69,'65291276690984','2015-07-31','2015-07-21',1,6,'1000.00','1210.00','210.00',13,'',23,1),(70,'65291276691003','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',24,1),(71,'65291276691029','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',25,1),(72,'65291276691134','2015-07-31','2015-07-21',1,6,'450.00','497.25','47.25',13,'',26,1),(73,'65291276691163','2015-07-31','2015-07-21',0,6,'15105.00','18277.05','3172.05',13,'',27,1),(74,'65291276691312','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',28,1),(75,'65291276696626','2015-07-31','2015-07-21',1,6,'170.00','205.70','35.70',0,'',29,1),(76,'65291276697805','2015-07-31','2015-07-21',1,6,'170.00','205.70','35.70',13,'',30,1),(77,'65291276697978','2015-07-31','2015-07-21',1,6,'450.00','497.25','47.25',13,'',31,1),(78,'65291276698000','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',32,1),(79,'65291276699129','2015-07-31','2015-07-21',1,6,'1000.00','1210.00','210.00',13,'',33,1),(80,'65291276699250','2015-07-31','2015-07-21',1,6,'550.00','665.50','115.50',13,'',35,1),(81,'65291276699234','2015-07-31','2015-07-21',1,6,'450.00','497.25','47.25',13,'',34,1),(82,'65291276700256','2015-07-31','2015-07-21',1,6,'1000.00','1210.00','210.00',13,'',38,1),(83,'65291276700201','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',36,1),(84,'65291276700243','2015-07-31','2015-07-21',1,6,'1200.00','1431.00','231.00',13,'',37,1),(85,'65291276700256','2015-07-31','2015-07-21',1,6,'450.00','497.25','47.25',13,'',38,1),(86,'65291276710490','2015-07-31','2015-07-21',1,6,'2000.00','2420.00','420.00',13,'',39,1),(87,'65291276711904','2015-07-31','2015-07-21',1,6,'15105.00','18277.05','3172.05',13,'',40,1),(88,'65291276711946','2015-07-31','2015-07-21',1,6,'1000.00','1210.00','210.00',13,'',41,1);
/*!40000 ALTER TABLE `facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `localidades`
--

DROP TABLE IF EXISTS `localidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localidades` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `localidades`
--

LOCK TABLES `localidades` WRITE;
/*!40000 ALTER TABLE `localidades` DISABLE KEYS */;
INSERT INTO `localidades` VALUES (1,'25 DE MAYO'),(2,'9 DE JULIO'),(3,'ADOLFO ALSINA'),(4,'ADOLFO GONZALES CHAVES'),(5,'ALBERTI'),(6,'ALMIRANTE BROWN'),(7,'AVELLANEDA'),(8,'AYACUCHO'),(9,'AZUL'),(10,'BAHIA BLANCA'),(11,'BALCARCE'),(12,'BARADERO'),(13,'BARTOLOME MITRE'),(14,'BENITO JUAREZ'),(15,'BERAZATEGUI'),(16,'BERISSO'),(17,'BOLIVAR'),(18,'BRAGADO'),(19,'CAMPANA'),(20,'CAPITAN SARMIENTO'),(21,'CARLOS CASARES'),(22,'CARLOS TEJEDOR'),(23,'CARMEN DE ARECO'),(24,'CASTELLI'),(25,'CHACABUCO'),(26,'CHASCOMUS'),(27,'CHIVILCOY'),(28,'COLON'),(29,'CORONEL BRANDSEN'),(30,'CORONEL DORREGO'),(31,'CORONEL PRINGLES'),(32,'CORONEL ROSALES'),(33,'CORONEL SUAREZ'),(34,'DAIREAUX'),(35,'DE LA COSTA'),(36,'DOLORES'),(37,'ENSENADA'),(38,'ESCOBAR'),(39,'ESTEBAN ECHEVERRIA'),(40,'EXALTACION DE LA CRUZ'),(41,'EZEIZA'),(42,'FLORENCIO VARELA'),(43,'FLORENTINO AMEGHINO'),(44,'GENERAL ALVARADO'),(45,'GENERAL ALVEAR'),(46,'GENERAL ARENALES'),(47,'GENERAL BELGRANO'),(48,'GENERAL GUIDO'),(49,'GENERAL LAMADRID'),(50,'GENERAL LAS HERAS'),(51,'GENERAL LAVALLE'),(52,'GENERAL MADARIAGA'),(53,'GENERAL PAZ'),(54,'GENERAL PINTO'),(55,'GENERAL PUEYRREDON'),(56,'GENERAL RODRIGUEZ'),(57,'GENERAL SAN MARTIN'),(58,'GENERAL VIAMONTE'),(59,'GENERAL VILLEGAS'),(60,'GUAMINI'),(61,'HIPOLITO YRIGOYEN'),(62,'HURLINGHAM'),(63,'ITUZAINGO'),(64,'JOSE CLEMENTE PAZ'),(65,'JUNIN'),(66,'LA MATANZA'),(67,'LA PLATA'),(68,'LANUS'),(69,'LAPRIDA'),(70,'LAS FLORES'),(71,'LEANDRO N ALEM'),(72,'LINCOLN'),(73,'LOBERIA'),(74,'LOBOS'),(75,'LOMAS DE ZAMORA'),(76,'LUJAN'),(77,'MAGDALENA'),(78,'MAIPU'),(79,'MALVINAS ARGENTINAS'),(80,'MAR CHIQUITA'),(81,'MARCOS PAZ'),(82,'MERCEDES'),(83,'MERLO'),(84,'MONTE HERMOSO'),(85,'MONTE'),(86,'MORENO'),(87,'MORON'),(88,'NAVARRO'),(89,'NECOCHEA'),(90,'OLAVARRIA'),(91,'PATAGONES'),(92,'PEHUAJO'),(93,'PELLEGRINI'),(94,'PERGAMINO'),(95,'PILA'),(96,'PILAR'),(97,'PINAMAR'),(98,'PRESIDENTE PERON'),(99,'PUAN'),(100,'PUNTA INDIO'),(101,'QUILMES'),(102,'RAMALLO'),(103,'RAUCH'),(104,'RIVADAVIA'),(105,'ROJAS'),(106,'ROQUE PEREZ'),(107,'SAAVEDRA'),(108,'SALADILLO'),(109,'SALLIQUELO'),(110,'SALTO'),(111,'SAN ANDRES DE GILES'),(112,'SAN ANTONIO DE ARECO'),(113,'SAN CAYETANO'),(114,'SAN FERNANDO'),(115,'SAN ISIDRO'),(116,'SAN MIGUEL'),(117,'SAN NICOLAS'),(118,'SAN PEDRO'),(119,'SAN VICENTE'),(120,'SUIPACHA'),(121,'TANDIL'),(122,'TAPALQUE'),(123,'TIGRE'),(124,'TORDILLO'),(125,'TORNQUIST'),(126,'TRENQUE LAUQUEN'),(127,'TRES ARROYOS'),(128,'TRES DE FEBRERO'),(129,'TRES LOMAS'),(130,'VICENTE LOPEZ'),(131,'VILLA GESELL'),(132,'VILLARINO'),(133,'ZARATE');
/*!40000 ALTER TABLE `localidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_creditos`
--

DROP TABLE IF EXISTS `notas_creditos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_creditos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `SUBTOTAL` decimal(10,2) NOT NULL,
  `TOTAL` decimal(10,2) NOT NULL,
  `IVA` decimal(10,2) NOT NULL,
  `PTO_VENTA` int(10) unsigned DEFAULT NULL,
  `NUMERO_COMPROBANTE` int(10) unsigned DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_notas_creditos_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_notas_creditos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_creditos`
--

LOCK TABLES `notas_creditos` WRITE;
/*!40000 ALTER TABLE `notas_creditos` DISABLE KEYS */;
INSERT INTO `notas_creditos` VALUES (9,'65291276710432','2015-07-31','2015-07-21',1,6,'','15105.00','18277.05','3172.05',13,1,1),(10,'65291276710445','2015-07-31','2015-07-21',1,6,'','170.00','205.70','35.70',13,2,1),(11,'65291276712007','2015-07-31','2015-07-21',1,6,'','1000.00','1210.00','210.00',13,3,1);
/*!40000 ALTER TABLE `notas_creditos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_creditos_facturas`
--

DROP TABLE IF EXISTS `notas_creditos_facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_creditos_facturas` (
  `ID_NOTA_CREDITO` int(10) unsigned NOT NULL,
  `ID_FACTURA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_NOTA_CREDITO`,`ID_FACTURA`),
  KEY `FK_notas_creditos_facturas_2` (`ID_FACTURA`),
  CONSTRAINT `FK_notas_creditos_facturas_1` FOREIGN KEY (`ID_NOTA_CREDITO`) REFERENCES `notas_creditos` (`ID`),
  CONSTRAINT `FK_notas_creditos_facturas_2` FOREIGN KEY (`ID_FACTURA`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_creditos_facturas`
--

LOCK TABLES `notas_creditos_facturas` WRITE;
/*!40000 ALTER TABLE `notas_creditos_facturas` DISABLE KEYS */;
INSERT INTO `notas_creditos_facturas` VALUES (9,73);
/*!40000 ALTER TABLE `notas_creditos_facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_debitos`
--

DROP TABLE IF EXISTS `notas_debitos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_debitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  `SUBTOTAL` decimal(10,2) NOT NULL,
  `TOTAL` decimal(10,2) NOT NULL,
  `IVA` decimal(10,2) NOT NULL,
  `PTO_VENTA` int(10) unsigned DEFAULT NULL,
  `NUMERO_COMPROBANTE` int(10) unsigned DEFAULT NULL,
  `ESTADO_FACTURACION` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_notas_debitos_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_notas_debitos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_debitos`
--

LOCK TABLES `notas_debitos` WRITE;
/*!40000 ALTER TABLE `notas_debitos` DISABLE KEYS */;
INSERT INTO `notas_debitos` VALUES (2,'65291276766098','2015-07-31','2015-07-21',1,6,'','450.00','497.25','47.25',13,1,1),(3,'65291276710445','2015-07-31','2015-07-21',1,6,'','1000.00','1210.00','210.00',13,2,1);
/*!40000 ALTER TABLE `notas_debitos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_debitos_facturas`
--

DROP TABLE IF EXISTS `notas_debitos_facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_debitos_facturas` (
  `ID_NOTA_DEBITO` int(10) unsigned NOT NULL,
  `ID_FACTURA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_NOTA_DEBITO`,`ID_FACTURA`),
  KEY `FK_notas_debitos_facturas_2` (`ID_FACTURA`),
  CONSTRAINT `FK_notas_debitos_facturas_1` FOREIGN KEY (`ID_NOTA_DEBITO`) REFERENCES `notas_debitos` (`ID`),
  CONSTRAINT `FK_notas_debitos_facturas_2` FOREIGN KEY (`ID_FACTURA`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_debitos_facturas`
--

LOCK TABLES `notas_debitos_facturas` WRITE;
/*!40000 ALTER TABLE `notas_debitos_facturas` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas_debitos_facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos_recibos_cheques`
--

DROP TABLE IF EXISTS `pagos_recibos_cheques`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagos_recibos_cheques` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_CHEQUE` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_cheques_1` (`ID_RECIBO`),
  KEY `FK_pagos_recibos_cheques_2` (`ID_CHEQUE`),
  CONSTRAINT `FK_pagos_recibos_cheques_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  CONSTRAINT `FK_pagos_recibos_cheques_2` FOREIGN KEY (`ID_CHEQUE`) REFERENCES `cheques` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos_recibos_cheques`
--

LOCK TABLES `pagos_recibos_cheques` WRITE;
/*!40000 ALTER TABLE `pagos_recibos_cheques` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagos_recibos_cheques` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos_recibos_efectivo`
--

DROP TABLE IF EXISTS `pagos_recibos_efectivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagos_recibos_efectivo` (
  `ID` int(10) unsigned NOT NULL,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `TOTAL` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_efectivo_1` (`ID_RECIBO`),
  CONSTRAINT `FK_pagos_recibos_efectivo_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos_recibos_efectivo`
--

LOCK TABLES `pagos_recibos_efectivo` WRITE;
/*!40000 ALTER TABLE `pagos_recibos_efectivo` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagos_recibos_efectivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos_recibos_nc`
--

DROP TABLE IF EXISTS `pagos_recibos_nc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagos_recibos_nc` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_NOTA_CREDITO` int(10) unsigned NOT NULL,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_nc_1` (`ID_NOTA_CREDITO`),
  KEY `FK_pagos_recibos_nc_2` (`ID_RECIBO`),
  CONSTRAINT `FK_pagos_recibos_nc_1` FOREIGN KEY (`ID_NOTA_CREDITO`) REFERENCES `notas_creditos` (`ID`),
  CONSTRAINT `FK_pagos_recibos_nc_2` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos_recibos_nc`
--

LOCK TABLES `pagos_recibos_nc` WRITE;
/*!40000 ALTER TABLE `pagos_recibos_nc` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagos_recibos_nc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parametros`
--

DROP TABLE IF EXISTS `parametros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametros` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NRO_RECIBO` varchar(45) NOT NULL,
  `NRO_REMITO` varchar(45) NOT NULL,
  `PREF_REMITO` varchar(45) NOT NULL,
  `PREF_RECIBO` varchar(45) NOT NULL,
  `RAZON_SOCIAL` varchar(100) NOT NULL,
  `COND_IVA` varchar(100) NOT NULL,
  `CUIT` varchar(30) NOT NULL,
  `DOMICILIO` varchar(100) NOT NULL,
  `ING_BRUTOS` varchar(30) NOT NULL,
  `INICIO_ACT` date NOT NULL,
  `NRO_FACTURA` varchar(45) NOT NULL,
  `NRO_NOTA_CREDITO` varchar(45) NOT NULL,
  `NRO_NOTA_DEBITO` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parametros`
--

LOCK TABLES `parametros` WRITE;
/*!40000 ALTER TABLE `parametros` DISABLE KEYS */;
INSERT INTO `parametros` VALUES (1,'1','17','0001','0001','VAN DER BEKEN FRANCISCO NICOLAS','IVA RESPONSABLE INSCRIPTO','20-04974618-1','PASSO 50 - ROJAS, BUENOS AIRES','200049746181','1994-01-01','42','4','3');
/*!40000 ALTER TABLE `parametros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permisos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CODIGO` varchar(20) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permisos`
--

LOCK TABLES `permisos` WRITE;
/*!40000 ALTER TABLE `permisos` DISABLE KEYS */;
/*!40000 ALTER TABLE `permisos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permisos_usuarios`
--

DROP TABLE IF EXISTS `permisos_usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permisos_usuarios` (
  `ID_USUARIO` int(10) unsigned NOT NULL,
  `ID_PERMISO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_USUARIO`,`ID_PERMISO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permisos_usuarios`
--

LOCK TABLES `permisos_usuarios` WRITE;
/*!40000 ALTER TABLE `permisos_usuarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `permisos_usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` varchar(50) NOT NULL,
  `CODIGO` varchar(20) NOT NULL,
  `COSTO` decimal(10,2) NOT NULL,
  `PRECIO` decimal(10,2) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `IVA` decimal(10,2) NOT NULL,
  `UBICACION` varchar(45) DEFAULT NULL,
  `ID_TIPO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_productos_1` (`ID_TIPO`),
  CONSTRAINT `FK_productos_1` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipos_productos` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'FILTRO PRIMARIO','AJ11399','300.00','550.00',0,'21.00','DDD334',3),(2,'PRODUCTO XXX','XXX','200.00','450.00',0,'10.50','Z900',1),(3,'ASDASD','A33344','50.00','170.00',0,'21.00','ASD122',2),(4,'ASIENTO DE LUJO','56530','10000.00','15105.00',0,'21.00','',1),(5,'FILTRO DE ACEITE XXI','FILT123','100.00','1000.00',0,'21.00','XREG1',3),(6,'TORNILLO DE BRONCE','DD22','50.00','200.00',0,'10.50','REG11',1);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES (2,'NEORIS','30-70993324-4','25 DE MAYO 50','1234-1234',105,'neoris@neoris.com',1);
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recibos`
--

DROP TABLE IF EXISTS `recibos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recibos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FECHA` date NOT NULL,
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `NUMERO` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_recibos_1` (`ID_CLIENTE`),
  CONSTRAINT `FK_recibos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recibos`
--

LOCK TABLES `recibos` WRITE;
/*!40000 ALTER TABLE `recibos` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recibos_facturas`
--

DROP TABLE IF EXISTS `recibos_facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recibos_facturas` (
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_FACTURA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_RECIBO`,`ID_FACTURA`),
  KEY `FK_recibos_facturas_2` (`ID_FACTURA`),
  CONSTRAINT `FK_recibos_facturas_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  CONSTRAINT `FK_recibos_facturas_2` FOREIGN KEY (`ID_FACTURA`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recibos_facturas`
--

LOCK TABLES `recibos_facturas` WRITE;
/*!40000 ALTER TABLE `recibos_facturas` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibos_facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recibos_notas_debito`
--

DROP TABLE IF EXISTS `recibos_notas_debito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recibos_notas_debito` (
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_NOTA_DEBITO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_RECIBO`,`ID_NOTA_DEBITO`),
  KEY `FK_recibos_notas_debito_2` (`ID_NOTA_DEBITO`),
  CONSTRAINT `FK_recibos_notas_debito_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  CONSTRAINT `FK_recibos_notas_debito_2` FOREIGN KEY (`ID_NOTA_DEBITO`) REFERENCES `notas_debitos` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recibos_notas_debito`
--

LOCK TABLES `recibos_notas_debito` WRITE;
/*!40000 ALTER TABLE `recibos_notas_debito` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibos_notas_debito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remitos`
--

DROP TABLE IF EXISTS `remitos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `FECHA` date NOT NULL,
  `NUMERO` varchar(45) NOT NULL,
  `ID_FACTURA` int(10) unsigned DEFAULT NULL,
  `OBSERVACIONES` varchar(200) DEFAULT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_remitos_1` (`ID_CLIENTE`),
  KEY `FK_remitos_2` (`ID_FACTURA`),
  CONSTRAINT `FK_remitos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`),
  CONSTRAINT `FK_remitos_2` FOREIGN KEY (`ID_FACTURA`) REFERENCES `facturas` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remitos`
--

LOCK TABLES `remitos` WRITE;
/*!40000 ALTER TABLE `remitos` DISABLE KEYS */;
/*!40000 ALTER TABLE `remitos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stocks_productos`
--

DROP TABLE IF EXISTS `stocks_productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stocks_productos`
--

LOCK TABLES `stocks_productos` WRITE;
/*!40000 ALTER TABLE `stocks_productos` DISABLE KEYS */;
INSERT INTO `stocks_productos` VALUES (2,2,2,20,'0034-03KKF','2015-07-21');
/*!40000 ALTER TABLE `stocks_productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipos_productos`
--

DROP TABLE IF EXISTS `tipos_productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipos_productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipos_productos`
--

LOCK TABLES `tipos_productos` WRITE;
/*!40000 ALTER TABLE `tipos_productos` DISABLE KEYS */;
INSERT INTO `tipos_productos` VALUES (1,'CORREA'),(2,'BUJIA'),(3,'FILTRO');
/*!40000 ALTER TABLE `tipos_productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DNI` varchar(20) NOT NULL,
  `PASSWORD` varchar(20) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'1','uboEp3KLIIc=',1,'JUAN MANUEL VAZQUEZ'),(2,'2','XMRCqDJaMOM=',1,'JUANI');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-07-21 23:15:56
