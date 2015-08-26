-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 26-08-2015 a las 22:59:19
-- Versión del servidor: 5.5.16
-- Versión de PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `sistema_facturacion_nico`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bancos`
--

CREATE TABLE IF NOT EXISTS `bancos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=42 ;

--
-- Volcado de datos para la tabla `bancos`
--

INSERT INTO `bancos` (`ID`, `NOMBRE`) VALUES
(1, 'ABN AMRO'),
(2, 'AMERICAN EXPRESS BANK'),
(3, 'BACS'),
(4, 'BANCO B.I. CREDITANSTALT'),
(5, 'BANCO BRADESCO'),
(6, 'BANCO CETELEM'),
(7, 'BANCO CMF'),
(8, 'BANCO COFIDIS'),
(9, 'BANCO COLUMBIA'),
(10, 'BANCO COMAFI'),
(11, 'BANCO CREDICOOP'),
(12, 'BANCO DE SERVICIOS Y TRANSACCIONES'),
(13, 'BANCO DE VALORES'),
(14, 'BANCO DEL SOL'),
(15, 'BANCO FINANSUR'),
(16, 'BANCO GALICIA'),
(17, 'BANCO INDUSTRIAL'),
(18, 'BANCO ITAÚ'),
(19, 'BANCO JULIO'),
(20, 'BANCO MACRO'),
(21, 'BANCO MARIVA'),
(22, 'BANCO MASVENTAS'),
(23, 'BANCO MERIDIAN'),
(24, 'BANCO PATAGONIA'),
(25, 'BANCO PIANO'),
(26, 'BANCO PRIVADO'),
(27, 'BANCO ROELA'),
(28, 'BANCO SAENZ'),
(29, 'BANCO SANTANDER RÍO'),
(30, 'BANCO SUPERVIELLE'),
(31, 'BANK OF AMERICA'),
(32, 'BANK OF TOKYO-MITSUBISHI UFJ'),
(33, 'BBVA BANCO FRANCÉS'),
(34, 'BNP PARIBAS'),
(35, 'CITIBANK'),
(36, 'DEUTSCHE BANK'),
(37, 'HSBC BANK'),
(38, 'JPMORGAN'),
(39, 'MBA LAZARD BANCO DE INVERSIONES'),
(40, 'RCI BANQU'),
(41, 'STANDARD BANK');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cheques`
--

CREATE TABLE IF NOT EXISTS `cheques` (
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
  KEY `FK_cheques_2` (`ID_CLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE IF NOT EXISTS `clientes` (
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
  KEY `FK_clientes_2` (`ID_LOCALIDAD`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`ID`, `RAZON_SOCIAL`, `ACTIVO`, `TELEFONO`, `CELULAR`, `ID_LOCALIDAD`, `DOCUMENTO`, `DIRECCION`, `COND_IVA`, `TIPO_DOC`) VALUES
(1, 'A', 1, '32', '', 1, '30-69368208-4', 'FVDS', 'RESP_INSC', 'CUIT');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comprobantes`
--

CREATE TABLE IF NOT EXISTS `comprobantes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CAE` varchar(45) DEFAULT NULL,
  `FECHA_CAE` date DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `ID_CLIENTE` int(10) unsigned DEFAULT NULL,
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
  `ID_PROVEEDOR` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_comprobantes_1` (`ID_CLIENTE`),
  KEY `FK_comprobantes_2` (`ID_PROVEEDOR`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comprobantes_asociados`
--

CREATE TABLE IF NOT EXISTS `comprobantes_asociados` (
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  `ID_COMPROBANTE_ASOC` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_COMPROBANTE`,`ID_COMPROBANTE_ASOC`) USING BTREE,
  KEY `FK_notas_creditos_facturas_2` (`ID_COMPROBANTE_ASOC`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalles_comprobantes`
--

CREATE TABLE IF NOT EXISTS `detalles_comprobantes` (
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
  KEY `FK_detalles_comp_2` (`ID_COMPROBANTE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalles_remitos`
--

CREATE TABLE IF NOT EXISTS `detalles_remitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `DETALLE` varchar(50) DEFAULT NULL,
  `ID_REMITO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_detalles_remitos_1` (`ID_REMITO`),
  KEY `FK_detalles_remitos_2` (`ID_PRODUCTO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `localidades`
--

CREATE TABLE IF NOT EXISTS `localidades` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=134 ;

--
-- Volcado de datos para la tabla `localidades`
--

INSERT INTO `localidades` (`ID`, `NOMBRE`) VALUES
(1, '25 DE MAYO'),
(2, '9 DE JULIO'),
(3, 'ADOLFO ALSINA'),
(4, 'ADOLFO GONZALES CHAVES'),
(5, 'ALBERTI'),
(6, 'ALMIRANTE BROWN'),
(7, 'AVELLANEDA'),
(8, 'AYACUCHO'),
(9, 'AZUL'),
(10, 'BAHIA BLANCA'),
(11, 'BALCARCE'),
(12, 'BARADERO'),
(13, 'BARTOLOME MITRE'),
(14, 'BENITO JUAREZ'),
(15, 'BERAZATEGUI'),
(16, 'BERISSO'),
(17, 'BOLIVAR'),
(18, 'BRAGADO'),
(19, 'CAMPANA'),
(20, 'CAPITAN SARMIENTO'),
(21, 'CARLOS CASARES'),
(22, 'CARLOS TEJEDOR'),
(23, 'CARMEN DE ARECO'),
(24, 'CASTELLI'),
(25, 'CHACABUCO'),
(26, 'CHASCOMUS'),
(27, 'CHIVILCOY'),
(28, 'COLON'),
(29, 'CORONEL BRANDSEN'),
(30, 'CORONEL DORREGO'),
(31, 'CORONEL PRINGLES'),
(32, 'CORONEL ROSALES'),
(33, 'CORONEL SUAREZ'),
(34, 'DAIREAUX'),
(35, 'DE LA COSTA'),
(36, 'DOLORES'),
(37, 'ENSENADA'),
(38, 'ESCOBAR'),
(39, 'ESTEBAN ECHEVERRIA'),
(40, 'EXALTACION DE LA CRUZ'),
(41, 'EZEIZA'),
(42, 'FLORENCIO VARELA'),
(43, 'FLORENTINO AMEGHINO'),
(44, 'GENERAL ALVARADO'),
(45, 'GENERAL ALVEAR'),
(46, 'GENERAL ARENALES'),
(47, 'GENERAL BELGRANO'),
(48, 'GENERAL GUIDO'),
(49, 'GENERAL LAMADRID'),
(50, 'GENERAL LAS HERAS'),
(51, 'GENERAL LAVALLE'),
(52, 'GENERAL MADARIAGA'),
(53, 'GENERAL PAZ'),
(54, 'GENERAL PINTO'),
(55, 'GENERAL PUEYRREDON'),
(56, 'GENERAL RODRIGUEZ'),
(57, 'GENERAL SAN MARTIN'),
(58, 'GENERAL VIAMONTE'),
(59, 'GENERAL VILLEGAS'),
(60, 'GUAMINI'),
(61, 'HIPOLITO YRIGOYEN'),
(62, 'HURLINGHAM'),
(63, 'ITUZAINGO'),
(64, 'JOSE CLEMENTE PAZ'),
(65, 'JUNIN'),
(66, 'LA MATANZA'),
(67, 'LA PLATA'),
(68, 'LANUS'),
(69, 'LAPRIDA'),
(70, 'LAS FLORES'),
(71, 'LEANDRO N ALEM'),
(72, 'LINCOLN'),
(73, 'LOBERIA'),
(74, 'LOBOS'),
(75, 'LOMAS DE ZAMORA'),
(76, 'LUJAN'),
(77, 'MAGDALENA'),
(78, 'MAIPU'),
(79, 'MALVINAS ARGENTINAS'),
(80, 'MAR CHIQUITA'),
(81, 'MARCOS PAZ'),
(82, 'MERCEDES'),
(83, 'MERLO'),
(84, 'MONTE HERMOSO'),
(85, 'MONTE'),
(86, 'MORENO'),
(87, 'MORON'),
(88, 'NAVARRO'),
(89, 'NECOCHEA'),
(90, 'OLAVARRIA'),
(91, 'PATAGONES'),
(92, 'PEHUAJO'),
(93, 'PELLEGRINI'),
(94, 'PERGAMINO'),
(95, 'PILA'),
(96, 'PILAR'),
(97, 'PINAMAR'),
(98, 'PRESIDENTE PERON'),
(99, 'PUAN'),
(100, 'PUNTA INDIO'),
(101, 'QUILMES'),
(102, 'RAMALLO'),
(103, 'RAUCH'),
(104, 'RIVADAVIA'),
(105, 'ROJAS'),
(106, 'ROQUE PEREZ'),
(107, 'SAAVEDRA'),
(108, 'SALADILLO'),
(109, 'SALLIQUELO'),
(110, 'SALTO'),
(111, 'SAN ANDRES DE GILES'),
(112, 'SAN ANTONIO DE ARECO'),
(113, 'SAN CAYETANO'),
(114, 'SAN FERNANDO'),
(115, 'SAN ISIDRO'),
(116, 'SAN MIGUEL'),
(117, 'SAN NICOLAS'),
(118, 'SAN PEDRO'),
(119, 'SAN VICENTE'),
(120, 'SUIPACHA'),
(121, 'TANDIL'),
(122, 'TAPALQUE'),
(123, 'TIGRE'),
(124, 'TORDILLO'),
(125, 'TORNQUIST'),
(126, 'TRENQUE LAUQUEN'),
(127, 'TRES ARROYOS'),
(128, 'TRES DE FEBRERO'),
(129, 'TRES LOMAS'),
(130, 'VICENTE LOPEZ'),
(131, 'VILLA GESELL'),
(132, 'VILLARINO'),
(133, 'ZARATE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos_recibos_cheques`
--

CREATE TABLE IF NOT EXISTS `pagos_recibos_cheques` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_CHEQUE` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_cheques_1` (`ID_RECIBO`),
  KEY `FK_pagos_recibos_cheques_2` (`ID_CHEQUE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos_recibos_efectivo`
--

CREATE TABLE IF NOT EXISTS `pagos_recibos_efectivo` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_pagos_recibos_efectivo_1` (`ID_RECIBO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `parametros`
--

CREATE TABLE IF NOT EXISTS `parametros` (
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos`
--

CREATE TABLE IF NOT EXISTS `permisos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CODIGO` varchar(20) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos_usuarios`
--

CREATE TABLE IF NOT EXISTS `permisos_usuarios` (
  `ID_USUARIO` int(10) unsigned NOT NULL,
  `ID_PERMISO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_USUARIO`,`ID_PERMISO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE IF NOT EXISTS `productos` (
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
  KEY `FK_productos_1` (`ID_TIPO`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`ID`, `DESCRIPCION`, `CODIGO`, `COSTO`, `PRECIO`, `ACTIVO`, `UBICACION`, `ID_TIPO`, `ID_TIPO_IVA`) VALUES
(1, 'CDS', 'A', 32.00, 32.00, 0, '', 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE IF NOT EXISTS `proveedores` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `RAZON_SOCIAL` varchar(50) NOT NULL,
  `CUIT` varchar(45) NOT NULL,
  `DIRECCION` varchar(100) DEFAULT NULL,
  `TELEFONO` varchar(45) DEFAULT NULL,
  `ID_LOCALIDAD` int(10) unsigned NOT NULL,
  `MAIL` varchar(100) DEFAULT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `COND_IVA` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_proveedores_1` (`ID_LOCALIDAD`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `proveedores`
--

INSERT INTO `proveedores` (`ID`, `RAZON_SOCIAL`, `CUIT`, `DIRECCION`, `TELEFONO`, `ID_LOCALIDAD`, `MAIL`, `ACTIVO`, `COND_IVA`) VALUES
(3, 'DEDE', '30-71214423-4', 'VFDS', 'VFEW', 1, '', 1, '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recibos`
--

CREATE TABLE IF NOT EXISTS `recibos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FECHA` date NOT NULL,
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `NUMERO` varchar(45) NOT NULL,
  `TOTAL` decimal(12,2) NOT NULL,
  `OBSERVACIONES` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_recibos_1` (`ID_CLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recibos_comprobantes`
--

CREATE TABLE IF NOT EXISTS `recibos_comprobantes` (
  `ID_RECIBO` int(10) unsigned NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_RECIBO`,`ID_COMPROBANTE`) USING BTREE,
  KEY `FK_recibos_comprobantes_2` (`ID_COMPROBANTE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `remitos`
--

CREATE TABLE IF NOT EXISTS `remitos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` int(10) unsigned NOT NULL,
  `FECHA` date NOT NULL,
  `NUMERO` varchar(45) NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned DEFAULT NULL,
  `OBSERVACIONES` varchar(200) DEFAULT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_remitos_1` (`ID_CLIENTE`),
  KEY `FK_remitos_2` (`ID_COMPROBANTE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `stocks_productos`
--

CREATE TABLE IF NOT EXISTS `stocks_productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_PRODUCTO` int(10) unsigned NOT NULL,
  `ID_PROVEEDOR` int(10) unsigned NOT NULL,
  `CANTIDAD` int(10) unsigned NOT NULL,
  `FACTURA` varchar(50) DEFAULT NULL,
  `FECHA` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_stocks_producto_1` (`ID_PRODUCTO`),
  KEY `FK_stocks_producto_2` (`ID_PROVEEDOR`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_productos`
--

CREATE TABLE IF NOT EXISTS `tipos_productos` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `tipos_productos`
--

INSERT INTO `tipos_productos` (`ID`, `NOMBRE`) VALUES
(1, 'A');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tributos_comprobantes`
--

CREATE TABLE IF NOT EXISTS `tributos_comprobantes` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DETALLE` varchar(100) NOT NULL,
  `ALICUOTA` decimal(10,2) NOT NULL,
  `IMPORTE` decimal(10,2) NOT NULL,
  `BASE_IMPONIBLE` decimal(10,2) NOT NULL,
  `ID_COMPROBANTE` int(10) unsigned NOT NULL,
  `TIPO_TRIBUTO` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_tributos_comprobantes_1` (`ID_COMPROBANTE`),
  KEY `FK_tributos_comprobantes_2` (`TIPO_TRIBUTO`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE IF NOT EXISTS `usuarios` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DNI` varchar(20) NOT NULL,
  `PASSWORD` varchar(20) NOT NULL,
  `ACTIVO` tinyint(1) NOT NULL DEFAULT '1',
  `NOMBRE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`ID`, `DNI`, `PASSWORD`, `ACTIVO`, `NOMBRE`) VALUES
(1, '1', 'uboEp3KLIIc=', 1, 'JUAN MANUEL VAZQUEZ');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cheques`
--
ALTER TABLE `cheques`
  ADD CONSTRAINT `FK_cheques_1` FOREIGN KEY (`ID_BANCO`) REFERENCES `bancos` (`ID`),
  ADD CONSTRAINT `FK_cheques_2` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`);

--
-- Filtros para la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD CONSTRAINT `FK_clientes_2` FOREIGN KEY (`ID_LOCALIDAD`) REFERENCES `localidades` (`ID`);

--
-- Filtros para la tabla `comprobantes`
--
ALTER TABLE `comprobantes`
  ADD CONSTRAINT `FK_comprobantes_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`),
  ADD CONSTRAINT `FK_comprobantes_2` FOREIGN KEY (`ID_PROVEEDOR`) REFERENCES `proveedores` (`ID`);

--
-- Filtros para la tabla `comprobantes_asociados`
--
ALTER TABLE `comprobantes_asociados`
  ADD CONSTRAINT `FK_notas_creditos_facturas_1` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`),
  ADD CONSTRAINT `FK_notas_creditos_facturas_2` FOREIGN KEY (`ID_COMPROBANTE_ASOC`) REFERENCES `comprobantes` (`ID`);

--
-- Filtros para la tabla `detalles_comprobantes`
--
ALTER TABLE `detalles_comprobantes`
  ADD CONSTRAINT `FK_detalles_comp_1` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`),
  ADD CONSTRAINT `FK_detalles_comp_2` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`);

--
-- Filtros para la tabla `detalles_remitos`
--
ALTER TABLE `detalles_remitos`
  ADD CONSTRAINT `FK_detalles_remitos_1` FOREIGN KEY (`ID_REMITO`) REFERENCES `remitos` (`ID`),
  ADD CONSTRAINT `FK_detalles_remitos_2` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`);

--
-- Filtros para la tabla `pagos_recibos_cheques`
--
ALTER TABLE `pagos_recibos_cheques`
  ADD CONSTRAINT `FK_pagos_recibos_cheques_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  ADD CONSTRAINT `FK_pagos_recibos_cheques_2` FOREIGN KEY (`ID_CHEQUE`) REFERENCES `cheques` (`ID`);

--
-- Filtros para la tabla `pagos_recibos_efectivo`
--
ALTER TABLE `pagos_recibos_efectivo`
  ADD CONSTRAINT `FK_pagos_recibos_efectivo_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`);

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `FK_productos_1` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipos_productos` (`ID`);

--
-- Filtros para la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD CONSTRAINT `FK_proveedores_1` FOREIGN KEY (`ID_LOCALIDAD`) REFERENCES `localidades` (`ID`);

--
-- Filtros para la tabla `recibos`
--
ALTER TABLE `recibos`
  ADD CONSTRAINT `FK_recibos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`);

--
-- Filtros para la tabla `recibos_comprobantes`
--
ALTER TABLE `recibos_comprobantes`
  ADD CONSTRAINT `FK_recibos_comprobantes_1` FOREIGN KEY (`ID_RECIBO`) REFERENCES `recibos` (`ID`),
  ADD CONSTRAINT `FK_recibos_comprobantes_2` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`);

--
-- Filtros para la tabla `remitos`
--
ALTER TABLE `remitos`
  ADD CONSTRAINT `FK_remitos_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `clientes` (`ID`),
  ADD CONSTRAINT `FK_remitos_2` FOREIGN KEY (`ID`) REFERENCES `comprobantes` (`ID`);

--
-- Filtros para la tabla `stocks_productos`
--
ALTER TABLE `stocks_productos`
  ADD CONSTRAINT `FK_stocks_producto_1` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `productos` (`ID`),
  ADD CONSTRAINT `FK_stocks_producto_2` FOREIGN KEY (`ID_PROVEEDOR`) REFERENCES `proveedores` (`ID`);

--
-- Filtros para la tabla `tributos_comprobantes`
--
ALTER TABLE `tributos_comprobantes`
  ADD CONSTRAINT `FK_tributos_comprobantes_1` FOREIGN KEY (`ID_COMPROBANTE`) REFERENCES `comprobantes` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
