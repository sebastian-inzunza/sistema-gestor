-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-10-2024 a las 21:19:18
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sistema`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `categoriaId` int(11) NOT NULL,
  `nombre` varchar(250) NOT NULL,
  `estatus` tinyint(1) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`categoriaId`, `nombre`, `estatus`, `fecha`) VALUES
(1, 'Entradas', 1, '2024-09-13 17:32:17'),
(3, 'Platillos Fuertes', 1, '2024-09-13 17:37:10'),
(4, 'Postres', 1, '2024-09-13 17:37:17'),
(5, 'Bebidas', 1, '2024-09-13 17:37:23');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `endpoints`
--

CREATE TABLE `endpoints` (
  `endpointId` int(11) NOT NULL,
  `url` varchar(250) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `method` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `endpoints`
--

INSERT INTO `endpoints` (`endpointId`, `url`, `nombre`, `method`) VALUES
(1, '/api/usuario/obtener', 'Visualizar usuarios', 'GET'),
(4, '/api/categoria/crear', 'Crear Catalogo(Agrupación de productos)', 'POST'),
(5, '/api/categoria/obtener', 'Visualizar Categorias', 'GET'),
(6, '/api/productos/obtener', 'Visualizar Productos', 'GET'),
(7, '/api/productos/obtener/{id}', 'Vizualizar un producto en especifico', 'GET'),
(8, '/api/roles/obtener', 'Visualizar roles', 'GET'),
(9, '/api/roles/obtener/admin', 'Vizualizar rol Administrador', 'GET'),
(10, '/api/roles/obtener/endpoints', 'Visualizar funcionalidades del sistema (Preferible solo Administradores)', 'GET'),
(11, '/api/roles/crear', 'Crear roles', 'POST'),
(12, '/api/roles/editar/{id}', 'Editar rol', 'PUT'),
(13, '/api/roles/obtener/{id}', 'Visualizar rol especifico', 'GET'),
(14, 'api/productos/editar/{id}', 'Editar información de productos', 'PUT');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enpoints_roles`
--

CREATE TABLE `enpoints_roles` (
  `endpointId` int(11) NOT NULL,
  `rolId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `enpoints_roles`
--

INSERT INTO `enpoints_roles` (`endpointId`, `rolId`) VALUES
(1, 1),
(4, 1),
(5, 1),
(6, 2),
(8, 2),
(9, 1),
(6, 1),
(7, 1),
(7, 2),
(11, 1),
(10, 1),
(1, 3),
(6, 3),
(1, 4),
(14, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes`
--

CREATE TABLE `ordenes` (
  `ordenId` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `usuarioId` int(11) NOT NULL,
  `estatus` varchar(50) NOT NULL,
  `total` decimal(10,2) NOT NULL DEFAULT 0.00,
  `llevar` tinyint(1) NOT NULL DEFAULT 0,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `ordenes`
--

INSERT INTO `ordenes` (`ordenId`, `nombre`, `usuarioId`, `estatus`, `total`, `llevar`, `fecha`) VALUES
(6, '2024-Orden-1', 2, 'ESPERANDO', 126.00, 0, '2024-10-02 11:53:18'),
(7, '2024-Orden-2', 2, 'LISTO', 126.00, 0, '2024-10-02 15:06:55'),
(8, '2024-Orden-3', 2, 'PREPARANDO', 126.00, 0, '2024-10-03 12:45:28'),
(9, '2024-Orden-4', 2, 'PREPARANDO', 126.00, 0, '2024-10-03 12:45:41'),
(10, '2024-Orden-5', 2, 'PREPARANDO', 126.00, 0, '2024-10-03 12:57:08'),
(11, '2024-Orden-6', 2, 'ESPERANDO', 126.00, 0, '2024-10-03 13:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes_productos`
--

CREATE TABLE `ordenes_productos` (
  `Id` int(11) NOT NULL,
  `ordenId` int(11) NOT NULL,
  `productoId` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio` decimal(10,2) NOT NULL DEFAULT 0.00,
  `atendido` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `ordenes_productos`
--

INSERT INTO `ordenes_productos` (`Id`, `ordenId`, `productoId`, `cantidad`, `precio`, `atendido`) VALUES
(11, 6, 1, 4, 76.00, 1),
(12, 6, 3, 2, 50.00, 1),
(13, 7, 1, 4, 76.00, 1),
(14, 7, 3, 2, 50.00, 1),
(15, 8, 1, 4, 76.00, 0),
(16, 8, 3, 2, 50.00, 0),
(17, 9, 1, 4, 76.00, 0),
(18, 9, 3, 2, 50.00, 0),
(19, 10, 1, 4, 76.00, 0),
(20, 10, 3, 2, 50.00, 0),
(21, 11, 1, 4, 76.00, 0),
(22, 11, 3, 2, 50.00, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `productoId` int(11) NOT NULL,
  `nombre` varchar(250) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `descripcion` varchar(350) NOT NULL,
  `imagen` varchar(250) DEFAULT NULL,
  `estatus` tinyint(1) NOT NULL DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`productoId`, `nombre`, `precio`, `descripcion`, `imagen`, `estatus`, `fecha`) VALUES
(1, 'Tacos de Birria', 19.00, 'Tacos de birria acompañado de verdura', NULL, 1, '2024-09-13 15:49:12'),
(2, 'Consome', 5.00, 'Consomé de Birria ', NULL, 1, '2024-09-13 20:51:47'),
(3, 'Quesabirrias', 25.00, 'Quesadilla con birria', NULL, 1, '2024-09-17 15:43:43'),
(4, 'Lonche Birria', 45.00, 'Lonche de birria ', NULL, 1, '2024-09-17 15:43:53'),
(5, 'Plato de Birria', 85.00, 'Plato de birria acompañado de arroz, frijoles y verdura al gusto', NULL, 1, '2024-09-17 20:08:11'),
(6, 'Pizza Birria', 250.00, 'Birria servida en una base de pizza', NULL, 1, '2024-09-17 20:26:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_categorias`
--

CREATE TABLE `productos_categorias` (
  `productoId` int(11) NOT NULL,
  `categoriaId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `productos_categorias`
--

INSERT INTO `productos_categorias` (`productoId`, `categoriaId`) VALUES
(2, 3),
(3, 3),
(4, 3),
(5, 3),
(6, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `rolId` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`rolId`, `nombre`) VALUES
(1, 'ROLE_ADMIN'),
(3, 'ROLE_PRUEBA'),
(4, 'ROLE_SUPERVISOR'),
(2, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seguimientos`
--

CREATE TABLE `seguimientos` (
  `seguimientoId` int(11) NOT NULL,
  `ordenId` int(11) NOT NULL,
  `productoId` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `usuarioId` int(11) NOT NULL,
  `nombre` varchar(350) NOT NULL,
  `apellidos` varchar(150) NOT NULL,
  `email` varchar(250) NOT NULL,
  `password` varchar(100) NOT NULL,
  `estatus` tinyint(1) NOT NULL DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`usuarioId`, `nombre`, `apellidos`, `email`, `password`, `estatus`, `fecha`) VALUES
(1, 'David ', 'Torres', 'david.torrest98@gmail.com', '$2a$10$woYXspqf/n8I8IFJlc5zuO8ptnG8.vFHq6mtoZoYdcjds9mqbQ6Z.', 1, '2024-09-18 20:46:21'),
(2, 'Refugio David ', 'Torres Trujillo', 'administrador@siapa.com', '$2a$10$UAPJX2OHDbRShAdcQg5GtugWOSBYTwazh2B9SDwkc4V/36C0dkkF2', 1, '2024-09-18 20:46:32');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios_roles`
--

CREATE TABLE `usuarios_roles` (
  `usuarioId` int(11) NOT NULL,
  `rolId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios_roles`
--

INSERT INTO `usuarios_roles` (`usuarioId`, `rolId`) VALUES
(1, 2),
(1, 4),
(2, 1),
(2, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`categoriaId`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `endpoints`
--
ALTER TABLE `endpoints`
  ADD PRIMARY KEY (`endpointId`);

--
-- Indices de la tabla `enpoints_roles`
--
ALTER TABLE `enpoints_roles`
  ADD KEY `FK_Roles2` (`rolId`),
  ADD KEY `endpointId_2` (`endpointId`),
  ADD KEY `endpointId` (`endpointId`) USING BTREE;

--
-- Indices de la tabla `ordenes`
--
ALTER TABLE `ordenes`
  ADD PRIMARY KEY (`ordenId`),
  ADD KEY `FK_Usuarios` (`usuarioId`);

--
-- Indices de la tabla `ordenes_productos`
--
ALTER TABLE `ordenes_productos`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `productoId` (`productoId`),
  ADD KEY `ordenId` (`ordenId`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`productoId`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `productos_categorias`
--
ALTER TABLE `productos_categorias`
  ADD PRIMARY KEY (`productoId`,`categoriaId`),
  ADD KEY `FK_Categorias` (`categoriaId`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`rolId`),
  ADD UNIQUE KEY `roleId` (`nombre`);

--
-- Indices de la tabla `seguimientos`
--
ALTER TABLE `seguimientos`
  ADD PRIMARY KEY (`seguimientoId`),
  ADD KEY `ordenId` (`ordenId`),
  ADD KEY `productoId` (`productoId`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`usuarioId`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `usuarios_roles`
--
ALTER TABLE `usuarios_roles`
  ADD PRIMARY KEY (`usuarioId`,`rolId`) USING BTREE,
  ADD KEY `FK_Roles` (`rolId`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `categoriaId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `endpoints`
--
ALTER TABLE `endpoints`
  MODIFY `endpointId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `ordenes`
--
ALTER TABLE `ordenes`
  MODIFY `ordenId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `ordenes_productos`
--
ALTER TABLE `ordenes_productos`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `productoId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `rolId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `seguimientos`
--
ALTER TABLE `seguimientos`
  MODIFY `seguimientoId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `usuarioId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `enpoints_roles`
--
ALTER TABLE `enpoints_roles`
  ADD CONSTRAINT `FK_Endpoints` FOREIGN KEY (`endpointId`) REFERENCES `endpoints` (`endpointId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_Roles2` FOREIGN KEY (`rolId`) REFERENCES `roles` (`rolId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `ordenes`
--
ALTER TABLE `ordenes`
  ADD CONSTRAINT `FK_Usuarios` FOREIGN KEY (`usuarioId`) REFERENCES `usuarios` (`usuarioId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `ordenes_productos`
--
ALTER TABLE `ordenes_productos`
  ADD CONSTRAINT `FK_Ordenes` FOREIGN KEY (`ordenId`) REFERENCES `ordenes` (`ordenId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ordenes_productos_ibfk_1` FOREIGN KEY (`productoId`) REFERENCES `productos` (`productoId`);

--
-- Filtros para la tabla `productos_categorias`
--
ALTER TABLE `productos_categorias`
  ADD CONSTRAINT `FK_Categorias` FOREIGN KEY (`categoriaId`) REFERENCES `categorias` (`categoriaId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_Productos` FOREIGN KEY (`productoId`) REFERENCES `productos` (`productoId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `seguimientos`
--
ALTER TABLE `seguimientos`
  ADD CONSTRAINT `seguimientos_ibfk_1` FOREIGN KEY (`ordenId`) REFERENCES `ordenes` (`ordenId`),
  ADD CONSTRAINT `seguimientos_ibfk_2` FOREIGN KEY (`productoId`) REFERENCES `productos` (`productoId`);

--
-- Filtros para la tabla `usuarios_roles`
--
ALTER TABLE `usuarios_roles`
  ADD CONSTRAINT `FK_Roles` FOREIGN KEY (`rolId`) REFERENCES `roles` (`rolId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_Ususarios` FOREIGN KEY (`usuarioId`) REFERENCES `usuarios` (`usuarioId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
