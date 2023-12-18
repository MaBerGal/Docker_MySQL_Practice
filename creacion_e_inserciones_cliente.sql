CREATE TABLE cliente (
    cliId INT PRIMARY KEY,
    usuario VARCHAR(50),
    pass VARCHAR(50),
    imagen VARCHAR(250),
    saldoTotal FLOAT
);


INSERT IGNORE INTO cliente (cliId, usuario, pass, imagen, saldoTotal) 
VALUES (1, 'Juan', '123', 'fotos/1.jpg', 0);

INSERT IGNORE INTO cliente (cliId, usuario, pass, imagen, saldoTotal) 
VALUES
  (2, 'JuanMartínez', '123', 'fotos/2.jpg', 0),
  (3, 'Pepe', '123', 'fotos/3.jpg', 0),
  (4, 'JoséRamírez', '123', 'fotos/4.jpg', 0),
  (5, 'CarmenRodríguez', '123', 'fotos/5.jpg', 0),
  (6, 'PabloHernández', '123', 'fotos/6.jpg', 0),
  (7, 'IsabelGutiérrez', '123', 'fotos/7.jpg', 0),
  (8, 'SergioMoreno', '123', 'fotos/8.jpg', 0),
  (9, 'ElenaNavarro', '123', 'fotos/9.jpg', 0),
  (10, 'JavierDíaz', '123', 'fotos/10.jpg', 0),
  (11, 'BeatrizSánchez', '123', 'fotos/11.jpg', 0),
  (12, 'FranciscoJiménez', '123', 'fotos/12.jpg', 0),
  (13, 'LuisaTorres', '123', 'fotos/13.jpg', 0),
  (14, 'ÁngelVargas', '123', 'fotos/14.jpg', 0),
  (15, 'MartaRuiz', '123', 'fotos/15.jpg', 0),
  (16, 'AntonioCabrera', '123', 'fotos/16.jpg', 0),
  (17, 'SilviaMolina', '123', 'fotos/17.jpg', 0),
  (18, 'PedroGómez', '123', 'fotos/18.jpg', 0),
  (19, 'LauraOrtega', '123', 'fotos/19.jpg', 0),
  (20, 'ManuelSerrano', '123', 'fotos/20.jpg', 0),
  (21, 'RosaCortés', '123', 'fotos/21.jpg', 0),
  (22, 'DefaultUser', '123', 'fotos/22.jpg', 0),
  (23, 'SinNombre', '123', 'fotos/default.jpg', 0);

